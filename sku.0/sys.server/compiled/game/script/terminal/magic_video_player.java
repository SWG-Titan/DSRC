package script.terminal;

import script.dictionary;
import script.library.sui;
import script.library.utils;
import script.location;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class magic_video_player extends script.base_script
{
    private static final int MENU_VIDEO_ROOT = menu_info_types.SERVER_MENU30;
    private static final int MENU_VIDEO_MANAGE = menu_info_types.SERVER_MENU31;
    private static final int MENU_VIDEO_INFO = menu_info_types.SERVER_MENU32;

    private static final String OBJVAR_STREAM_URL = "stream.url";
    private static final String OBJVAR_TIMESTAMP = "timestamp";
    private static final String OBJVAR_STREAM_LOOP = "stream.loop";
    private static final String OBJVAR_STREAM_ASPECT = "stream.aspect";
    private static final String OBJVAR_STREAM_START_TIME = "stream.startTime";
    private static final String OBJVAR_EMITTER_PARENT_ID = "video_emitter.parent_id";

    private static final String SPEAKER_TEMPLATE = "object/tangible/loot/misc/speaker_s01.iff";

    private static final String MASTER_ENTERTAINER_SKILL = "social_entertainer_master";

    private static final String SV_PREFIX = "video_mgmt.";
    private static final String SV_PID = SV_PREFIX + "pid";

    // ======================================================================
    // Radial menu
    // ======================================================================

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        String currentUrl = "";
        if (hasObjVar(self, OBJVAR_STREAM_URL))
            currentUrl = getStringObjVar(self, OBJVAR_STREAM_URL);

        boolean isPlaying = hasCondition(self, CONDITION_MAGIC_VIDEO_PLAYER) && !currentUrl.isEmpty();

        int root = mi.addRootMenu(MENU_VIDEO_ROOT, string_id.unlocalized("Video Management"));

        if (canModifyVideoPlayer(player))
            mi.addSubMenu(root, MENU_VIDEO_MANAGE, string_id.unlocalized("Open Control Panel"));

        String statusText = isPlaying ? "\\#00FF00 Playing" : (currentUrl.isEmpty() ? "\\#888888 No URL" : "\\#FF4444 Stopped");
        mi.addSubMenu(root, MENU_VIDEO_INFO, string_id.unlocalized("Status: " + statusText));

        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == MENU_VIDEO_MANAGE)
        {
            if (!canModifyVideoPlayer(player))
            {
                sendSystemMessage(player, string_id.unlocalized("You do not have permission to modify this video player."));
                return SCRIPT_CONTINUE;
            }
            showControlPanel(self, player);
        }
        else if (item == MENU_VIDEO_INFO)
        {
            showInfoPanel(self, player);
        }

        return SCRIPT_CONTINUE;
    }

    // ======================================================================
    // Control Panel (table-based SUI for authorized users)
    // ======================================================================

    private void showControlPanel(obj_id self, obj_id player) throws InterruptedException
    {
        String url = hasObjVar(self, OBJVAR_STREAM_URL) ? getStringObjVar(self, OBJVAR_STREAM_URL) : "";
        if (url == null) url = "";

        boolean isPlaying = hasCondition(self, CONDITION_MAGIC_VIDEO_PLAYER) && !url.isEmpty();

        String loop = "Off";
        if (hasObjVar(self, OBJVAR_STREAM_LOOP))
        {
            String lv = getStringObjVar(self, OBJVAR_STREAM_LOOP);
            if (lv != null && lv.equals("1")) loop = "On";
        }

        String aspect = "4:3";
        if (hasObjVar(self, OBJVAR_STREAM_ASPECT))
        {
            String av = getStringObjVar(self, OBJVAR_STREAM_ASPECT);
            if (av != null && !av.isEmpty()) aspect = av;
        }

        String timestamp = "0";
        if (hasObjVar(self, OBJVAR_TIMESTAMP))
        {
            String tv = getStringObjVar(self, OBJVAR_TIMESTAMP);
            if (tv != null && !tv.isEmpty()) timestamp = tv;
        }

        String elapsed = "-";
        if (isPlaying && hasObjVar(self, OBJVAR_STREAM_START_TIME))
        {
            String st = getStringObjVar(self, OBJVAR_STREAM_START_TIME);
            if (st != null && !st.isEmpty())
            {
                try
                {
                    int startEpoch = Integer.parseInt(st);
                    int now = getCalendarTime();
                    int secs = now - startEpoch;
                    if (secs < 0) secs = 0;
                    int mins = secs / 60;
                    int hours = mins / 60;
                    elapsed = (hours > 0 ? hours + "h " : "") + (mins % 60) + "m " + (secs % 60) + "s";
                }
                catch (NumberFormatException ignored) {}
            }
        }

        String name = getName(self);
        if (name == null || name.isEmpty()) name = "(unnamed)";

        String urlDisplay = url.isEmpty() ? "(none)" : url;

        String[] columnNames = { "Setting", "Value" };
        String[] columnTypes = { "text", "text" };

        String[][] tableData = {
            { "Status",       isPlaying ? "\\#00FF00 PLAYING" : "\\#FF4444 STOPPED" },
            { "URL",          urlDisplay },
            { "Loop",         loop },
            { "Aspect Ratio", aspect },
            { "Seek Offset",  timestamp + "s" },
            { "Elapsed",      elapsed },
            { "Object ID",    self.toString() },
            { "Object Name",  name },
            { "---",          "--- ACTIONS ---" },
            { "Set URL",      "Enter a new stream URL" },
            { "Set Timestamp","Enter seek offset in seconds" },
            { isPlaying ? "Stop Video" : "Play Video", isPlaying ? "Stop playback" : "Resume playback" },
            { "Toggle Loop",  "Currently: " + loop },
            { "Toggle Aspect","Currently: " + aspect },
            { "Spawn Speaker","Create linked audio emitter" }
        };

        int pid = sui.tableRowMajor(self, player, sui.OK_CANCEL_REFRESH, "Video Player Control Panel", "handleControlPanelOk", null, columnNames, columnTypes, tableData);

        setSUIProperty(pid, sui.TABLE_TABLE, "Selectable", "true");
        setSUIProperty(pid, sui.TABLE_TABLE, "SelectionAllowedMultiRow", "false");

        sui.tableButtonSetup(pid, sui.OK_CANCEL_REFRESH);
        setSUIProperty(pid, sui.TABLE_BTN_OK, sui.PROP_TEXT, "Execute");
        setSUIProperty(pid, sui.TABLE_BTN_OTHER, sui.PROP_TEXT, "Refresh");

        flushSUIPage(pid);
        showSUIPage(pid);

        utils.setScriptVar(player, SV_PID, pid);
    }

    public int handleControlPanelOk(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null)
            return SCRIPT_CONTINUE;

        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
            return SCRIPT_CONTINUE;

        if (!canModifyVideoPlayer(player))
        {
            sendSystemMessage(player, string_id.unlocalized("You do not have permission to modify this video player."));
            return SCRIPT_CONTINUE;
        }

        int event = sui.getIntButtonPressed(params);

        if (event == sui.BP_CANCEL)
        {
            utils.removeScriptVar(player, SV_PID);
            return SCRIPT_CONTINUE;
        }

        if (event == sui.BP_REVERT)
        {
            showControlPanel(self, player);
            return SCRIPT_CONTINUE;
        }

        int row = sui.getTableSelectedRow(params);
        if (row < 0)
        {
            sendSystemMessage(player, string_id.unlocalized("Select an action row and press Execute."));
            return SCRIPT_CONTINUE;
        }

        switch (row)
        {
            case 9:
                sui.inputbox(self, player, "Enter the video stream URL:", sui.OK_CANCEL, "Set Video URL", sui.INPUT_NORMAL, null, "handleSetVideoUrl", null);
                break;
            case 10:
                sui.inputbox(self, player, "Enter the timestamp in seconds to seek to:", sui.OK_CANCEL, "Set Timestamp", sui.INPUT_NORMAL, null, "handleSetVideoTimestamp", null);
                break;
            case 11:
                executePlayStop(self, player);
                showControlPanel(self, player);
                break;
            case 12:
                executeToggleLoop(self, player);
                showControlPanel(self, player);
                break;
            case 13:
                executeToggleAspect(self, player);
                showControlPanel(self, player);
                break;
            case 14:
                executeSpawnSpeaker(self, player);
                showControlPanel(self, player);
                break;
            default:
                sendSystemMessage(player, string_id.unlocalized("Select an action row (Set URL, Set Timestamp, Play/Stop, etc.) and press Execute."));
                break;
        }

        return SCRIPT_CONTINUE;
    }

    // ======================================================================
    // Info Panel (read-only, for all users)
    // ======================================================================

    private void showInfoPanel(obj_id self, obj_id player) throws InterruptedException
    {
        String url = hasObjVar(self, OBJVAR_STREAM_URL) ? getStringObjVar(self, OBJVAR_STREAM_URL) : "(none)";
        if (url == null) url = "(none)";
        boolean isPlaying = hasCondition(self, CONDITION_MAGIC_VIDEO_PLAYER) && !url.equals("(none)");

        String name = getName(self);
        if (name == null || name.isEmpty()) name = "(unnamed)";

        String loop = "Off";
        if (hasObjVar(self, OBJVAR_STREAM_LOOP))
        {
            String lv = getStringObjVar(self, OBJVAR_STREAM_LOOP);
            if (lv != null && lv.equals("1")) loop = "On";
        }

        String aspect = "4:3";
        if (hasObjVar(self, OBJVAR_STREAM_ASPECT))
        {
            String av = getStringObjVar(self, OBJVAR_STREAM_ASPECT);
            if (av != null && !av.isEmpty()) aspect = av;
        }

        String info = "\\#00BFFF === VIDEO PLAYER INFO ===\\#FFFFFF\n\n"
            + "Name:    " + name + "\n"
            + "Status:  " + (isPlaying ? "\\#00FF00 Playing" : "\\#FF4444 Stopped") + "\\#FFFFFF\n"
            + "URL:     " + url + "\n"
            + "Loop:    " + loop + "\n"
            + "Aspect:  " + aspect + "\n"
            + "ID:      " + self;

        sui.msgbox(self, player, info, sui.OK_ONLY, "Video Player Info", sui.MSG_NORMAL, "noHandler");
    }

    // ======================================================================
    // SUI Callbacks
    // ======================================================================

    public int handleSetVideoUrl(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null)
            return SCRIPT_CONTINUE;

        int event = sui.getIntButtonPressed(params);
        if (event == sui.BP_CANCEL)
            return SCRIPT_CONTINUE;

        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
            return SCRIPT_CONTINUE;

        if (!canModifyVideoPlayer(player))
        {
            sendSystemMessage(player, string_id.unlocalized("You do not have permission to modify this video player."));
            return SCRIPT_CONTINUE;
        }

        String url = sui.getInputBoxText(params);
        if (url == null || url.isEmpty())
        {
            sendSystemMessage(player, string_id.unlocalized("Invalid URL."));
            return SCRIPT_CONTINUE;
        }

        url = url.trim();
        setObjVar(self, OBJVAR_STREAM_URL, url);
        if (!hasObjVar(self, OBJVAR_TIMESTAMP))
            setObjVar(self, OBJVAR_TIMESTAMP, "0");
        if (!hasObjVar(self, OBJVAR_STREAM_LOOP))
            setObjVar(self, OBJVAR_STREAM_LOOP, "1");
        if (!hasObjVar(self, OBJVAR_STREAM_ASPECT))
            setObjVar(self, OBJVAR_STREAM_ASPECT, "4:3");
        setObjVar(self, OBJVAR_STREAM_START_TIME, String.valueOf(getCalendarTime()));
        setCondition(self, CONDITION_MAGIC_VIDEO_PLAYER);
        sendSystemMessage(player, string_id.unlocalized("Video URL set to: " + url));
        LOG("video_player", "[VideoPlayer] " + getName(player) + " (" + player + ") set stream URL on " + self + " to: " + url);

        showControlPanel(self, player);
        return SCRIPT_CONTINUE;
    }

    public int handleSetVideoTimestamp(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null)
            return SCRIPT_CONTINUE;

        int event = sui.getIntButtonPressed(params);
        if (event == sui.BP_CANCEL)
            return SCRIPT_CONTINUE;

        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
            return SCRIPT_CONTINUE;

        if (!canModifyVideoPlayer(player))
        {
            sendSystemMessage(player, string_id.unlocalized("You do not have permission to modify this video player."));
            return SCRIPT_CONTINUE;
        }

        String timestampStr = sui.getInputBoxText(params);
        if (timestampStr == null || timestampStr.isEmpty())
        {
            sendSystemMessage(player, string_id.unlocalized("Invalid timestamp."));
            return SCRIPT_CONTINUE;
        }

        try
        {
            int timestamp = Integer.parseInt(timestampStr.trim());
            if (timestamp < 0)
                timestamp = 0;
            setObjVar(self, OBJVAR_TIMESTAMP, String.valueOf(timestamp));
            sendSystemMessage(player, string_id.unlocalized("Timestamp set to: " + timestamp + " seconds."));
        }
        catch (NumberFormatException e)
        {
            sendSystemMessage(player, string_id.unlocalized("Invalid timestamp. Please enter a number in seconds."));
        }

        showControlPanel(self, player);
        return SCRIPT_CONTINUE;
    }

    public int noHandler(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    // ======================================================================
    // Action executors
    // ======================================================================

    private void executePlayStop(obj_id self, obj_id player) throws InterruptedException
    {
        String url = hasObjVar(self, OBJVAR_STREAM_URL) ? getStringObjVar(self, OBJVAR_STREAM_URL) : "";
        boolean isPlaying = hasCondition(self, CONDITION_MAGIC_VIDEO_PLAYER) && url != null && !url.isEmpty();

        if (isPlaying)
        {
            clearCondition(self, CONDITION_MAGIC_VIDEO_PLAYER);
            sendSystemMessage(player, string_id.unlocalized("Video stopped."));
        }
        else
        {
            if (url != null && !url.isEmpty())
            {
                setObjVar(self, OBJVAR_STREAM_START_TIME, String.valueOf(getCalendarTime()));
                setCondition(self, CONDITION_MAGIC_VIDEO_PLAYER);
                sendSystemMessage(player, string_id.unlocalized("Video playing."));
            }
            else
            {
                sendSystemMessage(player, string_id.unlocalized("No URL set. Use Set URL first."));
            }
        }
    }

    private void executeToggleLoop(obj_id self, obj_id player) throws InterruptedException
    {
        String currentLoop = "0";
        if (hasObjVar(self, OBJVAR_STREAM_LOOP))
            currentLoop = getStringObjVar(self, OBJVAR_STREAM_LOOP);

        String newLoop = (currentLoop != null && currentLoop.equals("1")) ? "0" : "1";
        setObjVar(self, OBJVAR_STREAM_LOOP, newLoop);
        sendSystemMessage(player, string_id.unlocalized("Loop " + (newLoop.equals("1") ? "enabled" : "disabled") + "."));
    }

    private void executeToggleAspect(obj_id self, obj_id player) throws InterruptedException
    {
        String currentAspect = "4:3";
        if (hasObjVar(self, OBJVAR_STREAM_ASPECT))
            currentAspect = getStringObjVar(self, OBJVAR_STREAM_ASPECT);

        String newAspect = (currentAspect != null && currentAspect.equals("16:9")) ? "4:3" : "16:9";
        setObjVar(self, OBJVAR_STREAM_ASPECT, newAspect);
        sendSystemMessage(player, string_id.unlocalized("Aspect ratio set to " + newAspect + "."));
    }

    private void executeSpawnSpeaker(obj_id self, obj_id player) throws InterruptedException
    {
        location loc = getLocation(player);
        obj_id speaker = createObject(SPEAKER_TEMPLATE, loc);
        if (!isIdValid(speaker))
        {
            sendSystemMessage(player, string_id.unlocalized("Failed to create speaker object."));
            return;
        }
        setObjVar(speaker, OBJVAR_EMITTER_PARENT_ID, self.toString());
        setName(speaker, "Video Speaker");
        sendSystemMessage(player, string_id.unlocalized("Speaker spawned and linked to this video player."));
        LOG("video_player", "[VideoPlayer] " + getName(player) + " spawned speaker " + speaker + " linked to " + self);
    }

    // ======================================================================
    // Permission check
    // ======================================================================

    private boolean canModifyVideoPlayer(obj_id player) throws InterruptedException
    {
        if (isGod(player))
            return true;

        if (hasSkill(player, MASTER_ENTERTAINER_SKILL))
            return true;

        return false;
    }
}
