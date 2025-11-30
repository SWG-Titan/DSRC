package script.library;/*
@Origin: dsrc.script.library
@Author:  BubbaJoeX
@Purpose: Handles renown for factions.
@Requirements: <no requirements>
@Notes: Renown is a system that allows players to gain ranks and points within a faction.
Only players with a standing of 5000 or higher can use the renown system.
You can gain renown, but you cannot lose it. So if a player drops below 5000, the renown isn't jeopardized.
@Created: Tuesday, 2/25/2025, at 10:25 PM, 
@Copyright © SWG: Titan 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.mapped_strings;
import script.obj_id;
import script.string_id;

public class renown extends factions
{

    public static final boolean LOGGING = true;
    public static final String RENOWN_VAR = "renown.";
    //RENOWN_VAR + faction + ".rank"
    //RENOWN_VAR + faction + ".points"
    public static final String RENOWN_RANK_VAR = ".rank";
    public static final String RENOWN_POINTS_VAR = ".points";
    public static final String FACTION_TABLE = "datatables/faction/faction.iff";
    private static final int MAX_RENOWN = 25;

    public static void checkForRankUp(obj_id player) throws InterruptedException
    {
        String[] FACTIONS = dataTableGetStringColumn(FACTION_TABLE, "factionName");
        String[] FACTIONS_ALLOW = dataTableGetStringColumn(FACTION_TABLE, "playerAllowed");
        if (FACTIONS != null && FACTIONS_ALLOW != null)
        {
            for (int i = 0; i < FACTIONS.length; i++)
            {
                if (FACTIONS_ALLOW[i].equals("1") && canUseRenown(player, FACTIONS[i]))
                {
                    initializeRenown(player, FACTIONS[i]);
                }
            }
        }
    }

    public static void initializeRenown(obj_id player, String faction) throws InterruptedException
    {
        if (!hasObjVar(player, RENOWN_VAR + faction + RENOWN_RANK_VAR))
        {
            setRenownRank(player, faction, 0);
            setRenownPoints(player, faction, 0);
            return;
        }

        if (canRankUp(player, faction))
        {
            handleRankUpEvent(player, faction);
        }
    }

    public static int getRenownRank(obj_id player, String faction)
    {
        return getIntObjVar(player, RENOWN_VAR + faction + RENOWN_RANK_VAR);
    }

    public static void grantRenownRank(obj_id player, String faction, int ranks)
    {
        int rank = getRenownRank(player, faction);
        if (rank >= MAX_RENOWN)
        {
            blog("Player " + player + " is already at max renown rank in faction " + faction);
            return;
        }
        if (rank + ranks > MAX_RENOWN)
        {
            ranks = MAX_RENOWN - rank;
        }
        rank += ranks;
        setObjVar(player, RENOWN_VAR + faction + RENOWN_RANK_VAR, rank);
        blog("Granted " + ranks + " ranks to " + player + " to faction " + faction);
    }

    public static int revokeRenownRank(obj_id player, String faction, int ranks)
    {
        int rank = getRenownRank(player, faction);
        rank -= ranks;
        setObjVar(player, RENOWN_VAR + faction + RENOWN_RANK_VAR, rank);
        blog("Revoked " + ranks + " ranks from " + player + " from faction " + faction);
        return rank;
    }

    public static void setRenownRank(obj_id player, String faction, int rank)
    {
        setObjVar(player, RENOWN_VAR + faction + RENOWN_RANK_VAR, rank);
        blog("Set " + player + " to rank " + rank + " in faction " + faction);
    }

    public static int getRenownPoints(obj_id player, String faction)
    {
        int points = getIntObjVar(player, RENOWN_VAR + faction + RENOWN_POINTS_VAR);
        if (points < 0)
        {
            points = 0;
        }
        return points;
    }

    public static int grantRenownPoints(obj_id player, String faction, int amount)
    {
        int points = getRenownPoints(player, faction);
        points += amount;
        setObjVar(player, RENOWN_VAR + faction + RENOWN_POINTS_VAR, points);
        blog("Granted " + amount + " points to " + player + " to faction " + faction);
        return points;
    }

    public static void revokeRenownPoints(obj_id player, String faction, int amount)
    {
        int points = getRenownPoints(player, faction);
        points -= amount;
        setObjVar(player, RENOWN_VAR + faction + RENOWN_POINTS_VAR, points);
        blog("Revoked " + amount + " points from " + player + " from faction " + faction);
    }

    public static void setRenownPoints(obj_id player, String faction, int amount)
    {
        setObjVar(player, RENOWN_VAR + faction + RENOWN_POINTS_VAR, amount);
        blog("Set " + player + " to " + amount + " points in faction " + faction);
    }

    public static boolean canUseRenown(obj_id player, String faction) throws InterruptedException
    {
        return getFactionStanding(player, faction) == 5000f;
    }

    public static boolean canRankUp(obj_id player, String faction)
    {
        int currentRank = getRenownRank(player, faction);
        int currentPoints = getRenownPoints(player, faction);
        int requiredPoints = getRequiredPointsForNextRank(currentRank);
        return currentPoints >= requiredPoints;
    }

    public static int getRequiredPointsForNextRank(int currentRank)
    {
        return (currentRank + 1) * 1000;
    }

    public static int getRequiredPointsForNextRank(obj_id player, String faction)
    {
        return getRequiredPointsForNextRank(getRenownRank(player, faction));
    }

    public static boolean rankUp(obj_id player, String faction)
    {
        if (canRankUp(player, faction))
        {
            int currentRank = getRenownRank(player, faction);
            int currentPoints = getRenownPoints(player, faction);
            int requiredPoints = getRequiredPointsForNextRank(currentRank);

            revokeRenownPoints(player, faction, requiredPoints);
            grantRenownRank(player, faction, 1);
            broadcast(player, "Congratulations! You have ranked up within the " + faction + " faction.");
            blog("Player " + player + " ranked up in faction " + faction);

            return true;
        }
        else
        {
            broadcast(player, "You do not have enough points to rank up within the " + faction + " faction.");
            blog("Player " + player + " does not have enough points to rank up in faction " + faction);
            return false;
        }
    }

    public static void handleRankUpEvent(obj_id player, String faction) throws InterruptedException
    {
        if (rankUp(player, faction))
        {
            broadcast(player, "You have been granted a credit case for your rank up!");
            grantRenownCreditCase(player);
        }
    }

    private static void grantRenownCreditCase(obj_id player) throws InterruptedException
    {
        blog("Granting renown credit case to " + player);
        static_item.createNewItemFunction("renown_credit_case", utils.getInventoryContainer(player));
    }

    public static void blog(String msg)
    {
        if (LOGGING)
        {
            LOG("ethereal", "[Renown]: " + msg);
        }
    }

    public void sendRenownMail(obj_id player, String faction)
    {
        String factionName = localize(new string_id("faction/faction_names", faction));
        String subject = "Renown Update";
        String message = "Congratulations! You have gained a renown level within the " + factionName + " faction! Seek out a Factional Record Keeper to learn more.";
        chatSendPersistentMessage("Renown Manager", player, "Renown Increase!", message, "");
    }

    public static void showRenownTable(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return;
        }
        mapped_strings factionStandings = getAllFactionStanding(player);
        if (factionStandings.isEmpty())
        {
            return;
        }

        String[] factions = factionStandings.getKeys();
        String[][] tableData = new String[factions.length][5];
        String[] tableTitles = {"Faction", "Current Standing", "Renown Rank", "Renown Points", "Next Rank Cost"};
        String[] tableTypes = {"text", "text", "text", "text", "text"};

        for (int i = 0; i < factions.length; i++)
        {
            float factionLitmus = getFactionStanding(player, factions[i]);
            tableData[i][0] = localize(new string_id("faction/faction_names", factions[i]));
            tableData[i][1] = String.valueOf(factionLitmus);
            tableData[i][2] = String.valueOf(getRenownRank(player, factions[i]));
            tableData[i][3] = String.valueOf(getRenownPoints(player, factions[i]));
            tableData[i][4] = String.valueOf(getRequiredPointsForNextRank(player, factions[i]));
        }

        String title = "Renown Standings - " + getPlayerFullName(player);
        int pid = sui.tableRowMajor(player, player, sui.OK_CANCEL, title, "handleRenownTableFunction", null, tableTitles, tableTypes, tableData);
        setSUIProperty(pid, "comp.tablePage.table", "Selectable", "true");
        setSUIProperty(pid, "comp.tablePage.table", "SelectionAllowedMultiRow", "true");
        setSUIProperty(pid, "comp.tablePage.table", "CellHeight", "30");
        setSUIProperty(pid, "comp.tablePage.table", "CellPadding", "4");
        setSUIProperty(pid, "comp.tablePage.table", "DefaultTextStyle", "bold_22");
        setSUIProperty(pid, "comp.tablePage.table", "DefaultTextColor", "#FFFFFF");
        sui.tableButtonSetup(pid, sui.OK_CANCEL_REFRESH);
        flushSUIPage(pid);
        showSUIPage(pid);
    }

    public static mapped_strings getAllFactionStanding(obj_id player) throws InterruptedException
    {
        mapped_strings standings = new mapped_strings();

        // load all factions
        String[] factions = dataTableGetStringColumn(FACTION_TABLE, "factionName");
        if (factions == null || factions.length == 0)
        {
            blog("getAllFactionStanding: No factions found in " + FACTION_TABLE);
            return standings;
        }

        // iterate factions and get the player's standing
        for (String faction : factions)
        {
            int standing = (int) getFactionStanding(player, faction); // cast to int
            if (standing != 0)
            {
                standings.put(faction, String.valueOf(standing));
            }
        }

        return standings;
    }
}
