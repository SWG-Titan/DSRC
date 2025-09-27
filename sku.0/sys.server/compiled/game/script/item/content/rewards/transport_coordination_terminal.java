package script.item.content.rewards;

import script.*;
import script.library.sui;
import script.library.utils;

public class transport_coordination_terminal extends base_script
{
    public String VAR_ITV_LOCATION_ROOT = "travel_tcg.stationary_itv.location";
    public String VAR_ITV_NAME = "travel_tcg.stationary_itv.name";
    public String VAR_ITV_TRANSIT_PREFIX = "travel_tcg.hub_";


    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Travel"));
        if ((getOwner(self) == player) || isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Synchronize"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            clearTravelVars(self);
            obj_id[] contents = getContents(self);
            int index = 0;

            if (contents != null)
            {
                for (obj_id content : contents)
                {
                    if (hasObjVar(content, VAR_ITV_LOCATION_ROOT))
                    {
                        setObjVar(self, VAR_ITV_TRANSIT_PREFIX + index + ".location", getLocationObjVar(content, VAR_ITV_LOCATION_ROOT + ".1"));
                        setObjVar(self, VAR_ITV_TRANSIT_PREFIX + index + ".name", getStringObjVar(content, VAR_ITV_NAME + ".1"));
                        index++;

                        setObjVar(self, VAR_ITV_TRANSIT_PREFIX + index + ".location", getLocationObjVar(content, VAR_ITV_LOCATION_ROOT + ".2"));
                        setObjVar(self, VAR_ITV_TRANSIT_PREFIX + index + ".name", getStringObjVar(content, VAR_ITV_NAME + ".2"));
                        index++;
                    }
                }
                setName(self, "Holowan Laboratories Transit Facilitator");
                setDescriptionString(self, "This container can be filled with ITVs to combine locations into one single hub.");
                broadcast(self, "Transit locations have been updated.");
                playClientEffectLoc(getAllPlayers(getLocation(self), 64f), "appearance/pt_rare_chest.prt", getLocation(self), 1.0f);
                showFlyText(self, new string_id("Refreshed!"), 1.0f, color.GREEN);
            }
        }
        else if (item == menu_info_types.ITEM_USE)
        {
            displayTravelOptions(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    private void displayTravelOptions(obj_id self, obj_id player) throws InterruptedException
    {
        java.util.Vector<String> locationNames = new java.util.Vector<>();
        java.util.Vector<location> locationData = new java.util.Vector<>();

        int i = 0;
        while (hasObjVar(self, VAR_ITV_TRANSIT_PREFIX + i + ".name"))
        {
            locationNames.add(getStringObjVar(self, VAR_ITV_TRANSIT_PREFIX + i + ".name"));
            locationData.add(getLocationObjVar(self, VAR_ITV_TRANSIT_PREFIX + i + ".location"));
            i++;
        }

        if (!locationNames.isEmpty())
        {
            sui.listbox(self, player, "Select a location to travel to.", sui.OK_CANCEL, "Travel", locationNames, "handleLocationSelect");
            utils.setScriptVar(self, "locationData", locationData);
        }
    }

    public int handleLocationSelect(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int buttonPressed = sui.getIntButtonPressed(params);
        if (buttonPressed == sui.BP_CANCEL)
        {
            broadcast(player, "You cancel your plans to travel.");
            return SCRIPT_CONTINUE;
        }
        if (idx >= 0)
        {
            location[] locationData = utils.getLocationArrayScriptVar(self, "locationData");
            if (locationData != null && idx < locationData.length)
            {
                location targetLocation = locationData[idx];
                if (targetLocation != null)
                {
                    warpPlayerToLocation(player, targetLocation);
                    String locationName = getStringObjVar(self, VAR_ITV_TRANSIT_PREFIX + idx + ".name");
                    broadcast(player, "Traveling to " + locationName + "...");
                    playClientEffectLoc(getAllPlayers(getLocation(self), 64f), "appearance/pt_rare_chest.prt", getLocation(self), 1.0f);
                    showFlyText(self, new string_id("WHOOSH!"), 2.0f, color.GOLDENROD);
                }
                else
                {
                    broadcast(player, "Invalid location selected.");
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    private void clearTravelVars(obj_id self) throws InterruptedException
    {
        removeAllObjVars(self);
    }

    public void warpPlayerToLocation(obj_id player, location targetLocation)
    {
        warpPlayer(player, targetLocation.area, targetLocation.x, targetLocation.y, targetLocation.z, targetLocation.cell, targetLocation.x, targetLocation.y, targetLocation.z, "", true);
    }
}
