package script.terminal;

import script.dictionary;
import script.library.sui;
import script.library.utils;
import script.location;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class video_player_manager extends script.base_script
{
    private static final int MENU_MANAGER_ROOT = menu_info_types.SERVER_MENU1;
    private static final int MENU_MANAGER_LIST = menu_info_types.SERVER_MENU2;
    private static final int MENU_MANAGER_LIST_SPEAKERS = menu_info_types.SERVER_MENU3;
    private static final int MENU_MANAGER_STOP_ALL = menu_info_types.SERVER_MENU4;

    private static final String OBJVAR_STREAM_URL = "stream.url";
    private static final String OBJVAR_TIMESTAMP = "timestamp";
    private static final String OBJVAR_STREAM_LOOP = "stream.loop";
    private static final String OBJVAR_STREAM_ASPECT = "stream.aspect";
    private static final String OBJVAR_STREAM_START_TIME = "stream.startTime";
    private static final String OBJVAR_EMITTER_PARENT_ID = "video_emitter.parent_id";

    private static final String SV_PREFIX = "video_manager.";
    private static final String SV_IDS = SV_PREFIX + "ids";
    private static final String SV_SPEAKER_IDS = SV_PREFIX + "speaker_ids";
    private static final String SV_SELECTED = SV_PREFIX + "selected";
    private static final String SV_PID = SV_PREFIX + "pid";

    private static final float SCAN_RANGE = 8192.0f;

    // ======================================================================
    // Radial menu
    // ======================================================================

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!isGod(player))
            return SCRIPT_CONTINUE;

        int root = mi.addRootMenu(MENU_MANAGER_ROOT, string_id.unlocalized("\\#00BFFF Video Manager"));
        mi.addSubMenu(root, MENU_MANAGER_LIST, string_id.unlocalized("Video Players"));
        mi.addSubMenu(root, MENU_MANAGER_LIST_SPEAKERS, string_id.unlocalized("Speakers"));
        mi.addSubMenu(root, MENU_MANAGER_STOP_ALL, string_id.unlocalized("\\#FF4444 Stop All Video Players"));

        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!isGod(player))
            return SCRIPT_CONTINUE;

        if (item == MENU_MANAGER_LIST)
        {
            showVideoPlayerTable(self, player);
        }
        else if (item == MENU_MANAGER_LIST_SPEAKERS)
        {
            showSpeakerTable(self, player);
        }
        else if (item == MENU_MANAGER_STOP_ALL)
        {
            stopAllVideoPlayers(self, player);
        }

        return SCRIPT_CONTINUE;
    }

    // ======================================================================
    // Video Player Table
    // ======================================================================

    private void showVideoPlayerTable(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id[] nearby = getObjectsInRange(player, SCAN_RANGE);
        if (nearby == null || nearby.length == 0)
        {
            sendSystemMessage(player, string_id.unlocalized("No objects found in range."));
            return;
        }

        java.util.Vector rowList = new java.util.Vector();
        java.util.Vector idList = new java.util.Vector();

        for (int i = 0; i < nearby.length; i++)
        {
            if (!isIdValid(nearby[i]))
                continue;

            boolean hasUrl = hasObjVar(nearby[i], OBJVAR_STREAM_URL);
            boolean hasCond = hasCondition(nearby[i], CONDITION_MAGIC_VIDEO_PLAYER);
            if (!hasUrl && !hasCond)
                continue;

            String url = hasUrl ? getStringObjVar(nearby[i], OBJVAR_STREAM_URL) : "";
            if (url == null) url = "";

            String status = hasCond ? "\\#00FF00 PLAYING" : "\\#FF4444 STOPPED";

            String name = getName(nearby[i]);
            if (name == null || name.isEmpty())
                name = getTemplateName(nearby[i]);
            if (name == null || name.isEmpty())
                name = "(unknown)";

            String loop = "Off";
            if (hasObjVar(nearby[i], OBJVAR_STREAM_LOOP))
            {
                String lv = getStringObjVar(nearby[i], OBJVAR_STREAM_LOOP);
                if (lv != null && lv.equals("1"))
                    loop = "On";
            }

            String aspect = "4:3";
            if (hasObjVar(nearby[i], OBJVAR_STREAM_ASPECT))
            {
                String av = getStringObjVar(nearby[i], OBJVAR_STREAM_ASPECT);
                if (av != null && !av.isEmpty())
                    aspect = av;
            }

            String elapsed = "";
            if (hasCond && hasObjVar(nearby[i], OBJVAR_STREAM_START_TIME))
            {
                String st = getStringObjVar(nearby[i], OBJVAR_STREAM_START_TIME);
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
            if (elapsed.isEmpty())
                elapsed = "-";

            float dist = getDistance(player, nearby[i]);
            String distStr = String.valueOf((int) dist) + "m";

            String urlShort = url.length() > 50 ? url.substring(0, 50) + "..." : url;

            String[] row = new String[]{ status, name, urlShort, loop, aspect, elapsed, distStr, nearby[i].toString() };
            rowList.add(row);
            idList.add(nearby[i].toString());
        }

        if (rowList.isEmpty())
        {
            sendSystemMessage(player, string_id.unlocalized("No video players found within " + (int)SCAN_RANGE + "m."));
            return;
        }

        String[] columnNames = { "Status", "Name", "URL", "Loop", "Aspect", "Elapsed", "Dist", "Object ID" };
        String[] columnTypes = { "text", "text", "text", "text", "text", "text", "text", "text" };

        String[][] tableData = new String[rowList.size()][];
        for (int i = 0; i < rowList.size(); i++)
            tableData[i] = (String[]) rowList.get(i);

        String[] idArray = new String[idList.size()];
        idList.toArray(idArray);
        utils.setScriptVar(player, SV_IDS, idArray);

        int pid = sui.tableRowMajor(self, player, sui.OK_CANCEL_REFRESH, "Video Player Manager  (" + rowList.size() + " found)", "handleVideoPlayerTableOk", null, columnNames, columnTypes, tableData, true);

        setSUIProperty(pid, sui.TABLE_TABLE, "Selectable", "true");
        setSUIProperty(pid, sui.TABLE_TABLE, "SelectionAllowedMultiRow", "false");

        sui.tableButtonSetup(pid, sui.OK_CANCEL_REFRESH);
        setSUIProperty(pid, sui.TABLE_BTN_OK, sui.PROP_TEXT, "Details");
        setSUIProperty(pid, sui.TABLE_BTN_OTHER, sui.PROP_TEXT, "Refresh");

        flushSUIPage(pid);
        showSUIPage(pid);

        utils.setScriptVar(player, SV_PID, pid);
    }

    public int handleVideoPlayerTableOk(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null)
            return SCRIPT_CONTINUE;

        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player) || !isGod(player))
            return SCRIPT_CONTINUE;

        int event = sui.getIntButtonPressed(params);

        if (event == sui.BP_CANCEL)
        {
            utils.removeScriptVar(player, SV_IDS);
            utils.removeScriptVar(player, SV_PID);
            return SCRIPT_CONTINUE;
        }

        if (event == sui.BP_REVERT)
        {
            showVideoPlayerTable(self, player);
            return SCRIPT_CONTINUE;
        }

        int row = sui.getTableSelectedRow(params);
        if (row < 0)
        {
            sendSystemMessage(player, string_id.unlocalized("No row selected."));
            return SCRIPT_CONTINUE;
        }

        if (!utils.hasScriptVar(player, SV_IDS))
            return SCRIPT_CONTINUE;

        String[] ids = utils.getStringArrayScriptVar(player, SV_IDS);
        if (ids == null || row >= ids.length)
            return SCRIPT_CONTINUE;

        obj_id target = obj_id.getObjId(Long.parseLong(ids[row]));
        if (!isIdValid(target))
        {
            sendSystemMessage(player, string_id.unlocalized("Object no longer valid."));
            return SCRIPT_CONTINUE;
        }

        showVideoPlayerDetail(self, player, target);
        return SCRIPT_CONTINUE;
    }

    // ======================================================================
    // Video Player Detail
    // ======================================================================

    private void showVideoPlayerDetail(obj_id self, obj_id player, obj_id target) throws InterruptedException
    {
        String url = hasObjVar(target, OBJVAR_STREAM_URL) ? getStringObjVar(target, OBJVAR_STREAM_URL) : "(none)";
        boolean isPlaying = hasCondition(target, CONDITION_MAGIC_VIDEO_PLAYER);
        String name = getName(target);
        if (name == null || name.isEmpty()) name = "(unnamed)";

        String loop = "Off";
        if (hasObjVar(target, OBJVAR_STREAM_LOOP))
        {
            String lv = getStringObjVar(target, OBJVAR_STREAM_LOOP);
            if (lv != null && lv.equals("1")) loop = "On";
        }

        String aspect = "4:3";
        if (hasObjVar(target, OBJVAR_STREAM_ASPECT))
        {
            String av = getStringObjVar(target, OBJVAR_STREAM_ASPECT);
            if (av != null && !av.isEmpty()) aspect = av;
        }

        String startTime = "N/A";
        if (hasObjVar(target, OBJVAR_STREAM_START_TIME))
            startTime = getStringObjVar(target, OBJVAR_STREAM_START_TIME);

        location loc = getLocation(target);
        String locStr = loc != null ? ("(" + (int)loc.x + ", " + (int)loc.y + ", " + (int)loc.z + ") " + loc.area) : "unknown";

        String info = "\\#00BFFF === VIDEO PLAYER DETAILS ===\\#FFFFFF\n\n"
            + "Object ID:  " + target + "\n"
            + "Name:       " + name + "\n"
            + "Location:   " + locStr + "\n\n"
            + "\\#00BFFF --- Playback ---\\#FFFFFF\n\n"
            + "Status:     " + (isPlaying ? "\\#00FF00 PLAYING" : "\\#FF4444 STOPPED") + "\\#FFFFFF\n"
            + "URL:        " + url + "\n"
            + "Loop:       " + loop + "\n"
            + "Aspect:     " + aspect + "\n"
            + "Start Time: " + startTime + "\n\n"
            + "\\#FFFF00 Press OK to " + (isPlaying ? "STOP" : "START") + " this video player.\\#FFFFFF\n"
            + "Press Cancel to go back.";

        utils.setScriptVar(player, SV_SELECTED, target.toString());

        int pid = createSUIPage(sui.SUI_MSGBOX, self, player, "handleVideoPlayerDetailAction");
        setSUIProperty(pid, sui.MSGBOX_TITLE, sui.PROP_TEXT, "Video Player: " + name);
        setSUIProperty(pid, sui.MSGBOX_PROMPT, sui.PROP_TEXT, info);
        sui.msgboxButtonSetup(pid, sui.OK_CANCEL);
        setSUIProperty(pid, sui.MSGBOX_BTN_OK, sui.PROP_TEXT, isPlaying ? "Stop" : "Start");
        showSUIPage(pid);
    }

    public int handleVideoPlayerDetailAction(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null)
            return SCRIPT_CONTINUE;

        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player) || !isGod(player))
            return SCRIPT_CONTINUE;

        int event = sui.getIntButtonPressed(params);
        if (event == sui.BP_CANCEL)
        {
            showVideoPlayerTable(self, player);
            return SCRIPT_CONTINUE;
        }

        if (!utils.hasScriptVar(player, SV_SELECTED))
            return SCRIPT_CONTINUE;

        String targetStr = utils.getStringScriptVar(player, SV_SELECTED);
        obj_id target = obj_id.getObjId(Long.parseLong(targetStr));

        if (!isIdValid(target))
        {
            sendSystemMessage(player, string_id.unlocalized("Object no longer valid."));
            showVideoPlayerTable(self, player);
            return SCRIPT_CONTINUE;
        }

        if (hasCondition(target, CONDITION_MAGIC_VIDEO_PLAYER))
        {
            clearCondition(target, CONDITION_MAGIC_VIDEO_PLAYER);
            sendSystemMessage(player, string_id.unlocalized("Video player " + target + " stopped."));
            LOG("video_manager", "[VideoManager] " + getName(player) + " stopped video player " + target);
        }
        else
        {
            if (hasObjVar(target, OBJVAR_STREAM_URL))
            {
                setObjVar(target, OBJVAR_STREAM_START_TIME, String.valueOf(getCalendarTime()));
                setCondition(target, CONDITION_MAGIC_VIDEO_PLAYER);
                sendSystemMessage(player, string_id.unlocalized("Video player " + target + " started."));
                LOG("video_manager", "[VideoManager] " + getName(player) + " started video player " + target);
            }
            else
            {
                sendSystemMessage(player, string_id.unlocalized("No URL set on this object."));
            }
        }

        showVideoPlayerDetail(self, player, target);
        return SCRIPT_CONTINUE;
    }

    // ======================================================================
    // Speaker Table
    // ======================================================================

    private void showSpeakerTable(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id[] nearby = getObjectsInRange(player, SCAN_RANGE);
        if (nearby == null || nearby.length == 0)
        {
            sendSystemMessage(player, string_id.unlocalized("No objects found in range."));
            return;
        }

        java.util.Vector rowList = new java.util.Vector();
        java.util.Vector idList = new java.util.Vector();

        for (int i = 0; i < nearby.length; i++)
        {
            if (!isIdValid(nearby[i]))
                continue;
            if (!hasObjVar(nearby[i], OBJVAR_EMITTER_PARENT_ID))
                continue;

            String parentId = getStringObjVar(nearby[i], OBJVAR_EMITTER_PARENT_ID);
            if (parentId == null) parentId = "";

            String parentStatus = "\\#888888 Unknown";
            try
            {
                obj_id parentObj = obj_id.getObjId(Long.parseLong(parentId));
                if (isIdValid(parentObj))
                    parentStatus = hasCondition(parentObj, CONDITION_MAGIC_VIDEO_PLAYER) ? "\\#00FF00 Playing" : "\\#FF4444 Stopped";
                else
                    parentStatus = "\\#FF4444 Invalid";
            }
            catch (NumberFormatException ignored)
            {
                parentStatus = "\\#FF4444 Invalid";
            }

            String name = getName(nearby[i]);
            if (name == null || name.isEmpty())
                name = "(unnamed)";

            float dist = getDistance(player, nearby[i]);
            String distStr = String.valueOf((int) dist) + "m";

            String[] row = new String[]{ name, parentId, parentStatus, distStr, nearby[i].toString() };
            rowList.add(row);
            idList.add(nearby[i].toString());
        }

        if (rowList.isEmpty())
        {
            sendSystemMessage(player, string_id.unlocalized("No speakers found within " + (int)SCAN_RANGE + "m."));
            return;
        }

        String[] columnNames = { "Name", "Linked To", "Parent Status", "Dist", "Object ID" };
        String[] columnTypes = { "text", "text", "text", "text", "text" };

        String[][] tableData = new String[rowList.size()][];
        for (int i = 0; i < rowList.size(); i++)
            tableData[i] = (String[]) rowList.get(i);

        String[] idArray = new String[idList.size()];
        idList.toArray(idArray);
        utils.setScriptVar(player, SV_SPEAKER_IDS, idArray);

        int pid = sui.tableRowMajor(self, player, sui.OK_CANCEL_REFRESH, "Speaker Manager  (" + rowList.size() + " found)", "handleSpeakerTableOk", null, columnNames, columnTypes, tableData, true);

        setSUIProperty(pid, sui.TABLE_TABLE, "Selectable", "true");
        setSUIProperty(pid, sui.TABLE_TABLE, "SelectionAllowedMultiRow", "false");

        sui.tableButtonSetup(pid, sui.OK_CANCEL_REFRESH);
        setSUIProperty(pid, sui.TABLE_BTN_OK, sui.PROP_TEXT, "Destroy");
        setSUIProperty(pid, sui.TABLE_BTN_OTHER, sui.PROP_TEXT, "Refresh");

        flushSUIPage(pid);
        showSUIPage(pid);
    }

    public int handleSpeakerTableOk(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null)
            return SCRIPT_CONTINUE;

        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player) || !isGod(player))
            return SCRIPT_CONTINUE;

        int event = sui.getIntButtonPressed(params);

        if (event == sui.BP_CANCEL)
        {
            utils.removeScriptVar(player, SV_SPEAKER_IDS);
            return SCRIPT_CONTINUE;
        }

        if (event == sui.BP_REVERT)
        {
            showSpeakerTable(self, player);
            return SCRIPT_CONTINUE;
        }

        int row = sui.getTableSelectedRow(params);
        if (row < 0)
        {
            sendSystemMessage(player, string_id.unlocalized("No row selected."));
            return SCRIPT_CONTINUE;
        }

        if (!utils.hasScriptVar(player, SV_SPEAKER_IDS))
            return SCRIPT_CONTINUE;

        String[] ids = utils.getStringArrayScriptVar(player, SV_SPEAKER_IDS);
        if (ids == null || row >= ids.length)
            return SCRIPT_CONTINUE;

        obj_id target = obj_id.getObjId(Long.parseLong(ids[row]));
        if (!isIdValid(target))
        {
            sendSystemMessage(player, string_id.unlocalized("Speaker no longer valid."));
            showSpeakerTable(self, player);
            return SCRIPT_CONTINUE;
        }

        sendSystemMessage(player, string_id.unlocalized("Speaker " + target + " destroyed."));
        LOG("video_manager", "[VideoManager] " + getName(player) + " destroyed speaker " + target);
        destroyObject(target);

        showSpeakerTable(self, player);
        return SCRIPT_CONTINUE;
    }

    // ======================================================================
    // Stop All
    // ======================================================================

    private void stopAllVideoPlayers(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id[] nearby = getObjectsInRange(player, SCAN_RANGE);
        if (nearby == null || nearby.length == 0)
        {
            sendSystemMessage(player, string_id.unlocalized("No objects found in range."));
            return;
        }

        int count = 0;
        for (int i = 0; i < nearby.length; i++)
        {
            if (!isIdValid(nearby[i]))
                continue;
            if (!hasCondition(nearby[i], CONDITION_MAGIC_VIDEO_PLAYER))
                continue;

            clearCondition(nearby[i], CONDITION_MAGIC_VIDEO_PLAYER);
            count++;
        }

        sendSystemMessage(player, string_id.unlocalized("Stopped " + count + " video player(s) within " + (int)SCAN_RANGE + "m."));
        LOG("video_manager", "[VideoManager] " + getName(player) + " stopped " + count + " video players (bulk)");
    }
}
