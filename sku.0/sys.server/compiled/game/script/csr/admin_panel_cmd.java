package script.csr;

import script.dictionary;
import script.obj_id;
import script.library.utils;
import script.player.player_ui;

/**
 * Admin Panel Command Handler
 * Provides '/admin' and '/adminpanel' commands to access the comprehensive admin panel
 *
 * Access: God level 15+ only
 *
 * Usage:
 *   /admin          - Opens the admin panel
 *   /adminpanel     - Opens the admin panel
 *
 * Features:
 * - Static Item Lookup & Spawn
 * - Creature Lookup & Spawn
 * - Buff Lookup & Grant
 * - Skill Lookup & Grant
 *
 * @author Titan Admin System
 * @version 1.0
 */
public class admin_panel_cmd extends script.base_script
{
    public admin_panel_cmd()
    {
    }

    private static final int GOD_LEVEL_REQUIRED = 15;
    private static final String COMMAND_ADMIN = "/admin";
    private static final String COMMAND_ADMINPANEL = "/adminpanel";

    /**
     * Handle the admin command
     */
    public int OnSpeaking(obj_id self, String text) throws InterruptedException
    {
        if (text == null || text.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }

        String lowerText = text.toLowerCase().trim();

        if (lowerText.equals(COMMAND_ADMIN) || lowerText.equals(COMMAND_ADMINPANEL))
        {
            if (!validateAccess(self))
            {
                sendSystemMessage(self, "You do not have permission to use the Admin Panel.", null);
                CustomerServiceLog("adminPanel", getPlayerName(self) + " (" + self + ") attempted to use admin panel without permission");
                return SCRIPT_OVERRIDE;
            }

            openAdminPanel(self);
            CustomerServiceLog("adminPanel", getPlayerName(self) + " (" + self + ") opened the Admin Panel");
            return SCRIPT_OVERRIDE;
        }

        return SCRIPT_CONTINUE;
    }

    /**
     * Validate admin access
     */
    private boolean validateAccess(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !isPlayer(player))
        {
            return false;
        }

        if (!isGod(player))
        {
            return false;
        }

        int godLevel = getGodLevel(player);
        if (godLevel < GOD_LEVEL_REQUIRED)
        {
            return false;
        }

        return true;
    }

    /**
     * Open the admin panel for a player
     */
    private void openAdminPanel(obj_id player) throws InterruptedException
    {
        // Attach the player_ui script if not already attached
        if (!hasScript(player, "player.player_ui"))
        {
            attachScript(player, "player.player_ui");
        }

        // Use the static method from player_ui to open the panel
        player_ui.showAdminPanel(player);
    }
}
