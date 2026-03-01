package script.terminal;

import script.dictionary;
import script.library.sui;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class magic_video_emitter extends script.base_script
{
    private static final int MENU_EMITTER_ROOT = menu_info_types.SERVER_MENU37;
    private static final int MENU_EMITTER_VOLUME = menu_info_types.SERVER_MENU38;
    private static final int MENU_EMITTER_INFO = menu_info_types.SERVER_MENU39;
    private static final int MENU_EMITTER_DESTROY = menu_info_types.SERVER_MENU40;

    private static final String OBJVAR_EMITTER_PARENT_ID = "video_emitter.parent_id";
    private static final String OBJVAR_EMITTER_VOLUME = "video_emitter.volume";

    private static final String SUI_VOLUME = "Script.videoPlayerVolume";
    private static final String SV_VOLUME_PID = "video_emitter_volume.pid";

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        int root = mi.addRootMenu(MENU_EMITTER_ROOT, string_id.unlocalized("Speaker"));

        mi.addSubMenu(root, MENU_EMITTER_VOLUME, string_id.unlocalized("Volume"));

        String parentId = "";
        if (hasObjVar(self, OBJVAR_EMITTER_PARENT_ID))
            parentId = getStringObjVar(self, OBJVAR_EMITTER_PARENT_ID);

        mi.addSubMenu(root, MENU_EMITTER_INFO, string_id.unlocalized("Linked to: " + (parentId.isEmpty() ? "(none)" : parentId)));

        if (canModifyEmitter(player))
        {
            mi.addSubMenu(root, MENU_EMITTER_DESTROY, string_id.unlocalized("Destroy Speaker"));
        }

        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == MENU_EMITTER_VOLUME)
        {
            showVolumeSlider(self, player);
            return SCRIPT_CONTINUE;
        }

        if (item == MENU_EMITTER_DESTROY)
        {
            if (!canModifyEmitter(player))
            {
                sendSystemMessage(player, string_id.unlocalized("You do not have permission to destroy this speaker."));
                return SCRIPT_CONTINUE;
            }

            sendSystemMessage(player, string_id.unlocalized("Speaker destroyed."));
            LOG("video_player", "[VideoEmitter] " + getName(player) + " destroyed speaker " + self);
            destroyObject(self);
            return SCRIPT_CONTINUE;
        }

        return SCRIPT_CONTINUE;
    }

    private void showVolumeSlider(obj_id self, obj_id player) throws InterruptedException
    {
        int pid = createSUIPage(SUI_VOLUME, self, player, "handleVolumeClose");

        if (pid < 0)
        {
            sendSystemMessage(player, string_id.unlocalized("Failed to open volume control."));
            return;
        }

        int currentVolume = 100;
        if (hasObjVar(self, OBJVAR_EMITTER_VOLUME))
        {
            String volStr = getStringObjVar(self, OBJVAR_EMITTER_VOLUME);
            if (volStr != null && !volStr.isEmpty())
            {
                try { currentVolume = Integer.parseInt(volStr); }
                catch (NumberFormatException ignored) {}
            }
        }

        if (currentVolume < 0) currentVolume = 0;
        if (currentVolume > 100) currentVolume = 100;

        setSUIProperty(pid, "comp.slider", "Value", String.valueOf(currentVolume));
        setSUIProperty(pid, "comp.lblVolume", sui.PROP_TEXT, "Volume: " + currentVolume + "%");

        subscribeToSUIEvent(pid, sui_event_type.SET_onButton, "btnOk", "handleVolumeSet");
        subscribeToSUIPropertyForEvent(pid, sui_event_type.SET_onButton, "btnOk", "comp.slider", "Value");

        showSUIPage(pid);
        utils.setScriptVar(player, SV_VOLUME_PID, pid);
    }

    public int handleVolumeSet(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
            return SCRIPT_CONTINUE;

        utils.removeScriptVar(player, SV_VOLUME_PID);

        String valueStr = params.getString("comp.slider.Value");
        if (valueStr == null || valueStr.isEmpty())
            return SCRIPT_CONTINUE;

        int volume = 100;
        try { volume = Integer.parseInt(valueStr); }
        catch (NumberFormatException ignored) { return SCRIPT_CONTINUE; }

        if (volume < 0) volume = 0;
        if (volume > 100) volume = 100;

        setObjVar(self, OBJVAR_EMITTER_VOLUME, String.valueOf(volume));
        sendSystemMessage(player, string_id.unlocalized("Speaker volume set to " + volume + "%."));
        LOG("video_player", "[VideoEmitter] " + getName(player) + " set volume on " + self + " to " + volume);
        return SCRIPT_CONTINUE;
    }

    public int handleVolumeClose(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (isIdValid(player))
            utils.removeScriptVar(player, SV_VOLUME_PID);
        return SCRIPT_CONTINUE;
    }

    private boolean canModifyEmitter(obj_id player) throws InterruptedException
    {
        if (isGod(player))
            return true;

        if (hasSkill(player, "social_entertainer_master"))
            return true;

        return false;
    }
}
