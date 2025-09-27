package script.item.content.rewards;

import script.*;
import script.library.sui;
import script.library.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class storage_management_terminal extends script.base_script
{

    public static final boolean LOGGING = false;

    public int OnAttach(obj_id self)
    {
        blog("Entered OnAttach");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        blog("Entered OnInitialize");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        blog("Entered OnObjectMenuRequest");
        if (canManipulate(player, self, true, true, 15, true) || isGod(player))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, toDummy("Manage Storage"));
        }
        else
        {
            broadcast(player, "You do not have access to this terminal.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        blog("Entered OnObjectMenuSelect");

        if (item == menu_info_types.ITEM_USE)
        {
            obj_id structure = getTopMostContainer(self);
            if (isIdValid(structure))
            {
                obj_id owner = getOwner(structure);
                if (owner == player || isGod(player))
                {
                    ArrayList<obj_id> allContents = new ArrayList<>();
                    obj_id[] cells = getContents(structure);
                    for (obj_id cell : cells)
                    {
                        if (isIdValid(cell))
                        {
                            allContents.addAll(getValidContents(cell, player));
                        }
                    }

                    if (allContents.isEmpty())
                    {
                        blog("No valid contents found in the structure.");
                    }
                    else
                    {
                        blog("Found " + allContents.size() + " valid contents.");
                    }

                    // Convert ArrayList to arrays for names and contents
                    obj_id[] contents = allContents.toArray(new obj_id[0]);
                    String[] names = getSortedEncodedNames(contents);  // Sorted names array

                    blog("Contents array length: " + contents.length);
                    blog("Names array length: " + names.length);

                    // If names array is empty, we want to return without attempting to open the listbox.
                    if (names.length == 0)
                    {
                        blog("No items to display in the listbox.");
                        return SCRIPT_CONTINUE;
                    }

                    // Save state variables for later use in the UI interaction
                    utils.setScriptVar(player, "item_manager.structure", structure);
                    utils.setScriptVar(player, "item_manager.contents", contents);
                    utils.setScriptVar(player, "item_manager.names", names);

                    // Logging values
                    blog("Attempting to open the first listbox!");

                    // Open the listbox with item names as options
                    sui.listbox(self, player, "Select an item and an option.", sui.OK_CANCEL, "Storage Management Terminal", names, "handleSelection", true);
                }
                else
                {
                    blog("No permissions to use radial menu!");
                    broadcast(player, "You must be the owner of this structure to access the storage management terminal.");
                }
            }
            else
            {
                blog("Structure not found or invalid.");
            }
        }
        return SCRIPT_CONTINUE;
    }


    public int handleSelection(obj_id self, dictionary params) throws InterruptedException
    {
        blog("Entered handleSelection");
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }

        obj_id[] contents = utils.getObjIdArrayScriptVar(player, "item_manager.contents");
        if (idx < contents.length)
        {
            obj_id selected = contents[idx];
            utils.setScriptVar(player, "item_manager.selected", selected);

            String[] options = {"Pick Up", "Examine", "Duplicate", "Set Count", "Cancel"};
            blog("Attempting to open the second listbox!");
            sui.listbox(self, player, "Select an option for " + getEncodedName(selected) + ":", sui.OK_CANCEL, "Storage Management Terminal", options, "handleOptionSelection", true);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleOptionSelection(obj_id self, dictionary params) throws InterruptedException
    {
        blog("Entered handleOptionSelection");
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }

        int buttonPressed = sui.getIntButtonPressed(params);
        if (buttonPressed == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }

        obj_id selected = utils.getObjIdScriptVar(player, "item_manager.selected");
        switch (idx)
        {
            case 0:
                pickUpItem(player, selected);
                break;
            case 1:
                examineItem(player, selected);
                break;
            case 2:
                String template = getTemplateName(selected);
                obj_id dupe = createObject(template, utils.getInventoryContainer(player), "");
                if (isIdValid(dupe))
                {
                    // Attach scripts to the new object
                    String[] scripts = getScriptList(selected);
                    for (String script : scripts)
                    {
                        attachScript(dupe, script);
                    }

                    // Get and apply all ObjVars from the selected object to the new object
                    String allObjVars = getPackedObjvars(selected);
                    setPackedObjvars(dupe, allObjVars);

                    // Set other properties as needed
                    if (getCount(selected) > 1)
                    {
                        setCount(dupe, getCount(selected));
                    }

                    if (isIdValid(getCrafter(selected)))
                    {
                        setCrafter(dupe, getCrafter(selected));
                    }

                    // Broadcast the cloning success
                    broadcast(player, "Cloned " + selected + " to " + dupe + "!");
                }
                break;
            case 3:
                setCount(selected, 10 * 100);
                broadcast(player, "Set " + selected + " to 1000 count.");
                break;
            case 4:
                break;
        }
        return SCRIPT_CONTINUE;
    }


    public void examineItem(obj_id player, obj_id selected)
    {
        blog("opening examine window");
        openExamineWindow(player, selected);
    }

    public void pickUpItem(obj_id player, obj_id selected) throws InterruptedException
    {
        if (canManipulate(player, selected, true, true, 64f, false) || isGod(player))
        {
            obj_id invCont = utils.getInventoryContainer(player);
            blog("Putting " + selected + " into " + invCont);
            putIn(invCont, selected);
        }
        else
        {
            blog("No permission for " + getPlayerFullName(player) + " to pick-up item!");
            broadcast(player, "You do not have permission to pick-up " + getEncodedName(selected));
        }
    }

    public ArrayList<obj_id> getValidContents(obj_id cell, obj_id player) throws InterruptedException
    {
        ArrayList<obj_id> validContents = new ArrayList<>();
        ArrayList<Integer> badGameObjectTypes = new ArrayList<>();

        // Define invalid game object types
        badGameObjectTypes.add(GOT_vendor);
        badGameObjectTypes.add(GOT_terminal_player_structure);
        badGameObjectTypes.add(GOT_static);
        badGameObjectTypes.add(GOT_ship);
        badGameObjectTypes.add(GOT_installation);

        obj_id[] contents = getContents(cell);

        for (obj_id content : contents)
        {
            // Skip invalid objects
            if (!isIdValid(content) || badGameObjectTypes.contains(getGameObjectType(content)) ||
                    getTemplateName(content).contains("visible_crafting_station") || isPlayer(content) ||
                    getTemplateName(content).startsWith("object/tangible/hopper")) // Additional condition
            {
                continue; // Skip this object if any invalid condition is met
            }

            // Add the valid object to the list
            validContents.add(content);

            // Check if this object contains more items (recursive call)
            obj_id[] subContents = getContents(content);
            if (subContents != null && subContents.length > 0)
            {
                validContents.addAll(getValidContents(content, player)); // Recursive call
            }
        }

        return validContents;
    }


    private boolean isTopContainer(obj_id container)
    {
        return getTemplateName(container).equals("object/building");
    }

    private String[] getSortedEncodedNames(obj_id[] contents) throws InterruptedException
    {
        String[] encodedNames = new String[contents.length];

        for (int i = 0; i < contents.length; i++)
        {
            encodedNames[i] = getEncodedName(contents[i]) + "\t\t(" + getGameObjectTypeName(getGameObjectType(contents[i])) + ")";
        }

        Integer[] indices = new Integer[contents.length];
        for (int i = 0; i < indices.length; i++)
        {
            indices[i] = i;
        }

        Arrays.sort(indices, Comparator.comparing(i -> encodedNames[i]));

        obj_id[] sortedContents = new obj_id[contents.length];
        String[] sortedNames = new String[contents.length];
        for (int i = 0; i < indices.length; i++)
        {
            sortedContents[i] = contents[indices[i]];
            sortedNames[i] = encodedNames[indices[i]];
        }

        System.arraycopy(sortedContents, 0, contents, 0, contents.length);
        System.arraycopy(sortedNames, 0, encodedNames, 0, encodedNames.length);

        return encodedNames;
    }

    public string_id toDummy(String txt)
    {
        return new string_id(txt);
    }

    public void blog(String message)
    {
        if (LOGGING)
        {
            LOG("ethereal", "[SMT]: " + message);
        }
    }

}
