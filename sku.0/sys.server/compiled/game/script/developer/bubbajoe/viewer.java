package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Friday, 8/23/2024, at 3:25 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.sui;
import script.menu_info;
import script.obj_id;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class viewer extends script.base_script
{
    public static String PAGE_TITLE = "Object Viewer";
    public static String PAGE_PROMPT = "Select an object to view.";
    public static String PAGE_SOURCE = "/Script.viewer";
    public static String PAGE_VIEWER_WIDGET = "ViewerPage.viewer";
    public static String PAGE_VIEWER_WIDGET_PROPERTY = "SetObject";
    public static String PAGE_TREE_NODE_ROOT = "objectNav";
    public static String PAGE_TREE_NODE_PROPERTY = "dataTree";
    public static String PAGE_BUTTON_REFRESH = "btnRefresh";

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnSpeaking(obj_id self, String text) throws InterruptedException
    {
        if (isGod(self))
        {
            if (text.equals("viewer"))
            {
                broadcast(self, "Opening object viewer.");
                openTreeView(self);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void openTreeView(obj_id self) throws InterruptedException
    {
        int page = createSUIPage("/Script.viewer", self, self);
        if (page == 0)
        {
            broadcast(self, "Failed to create SUI page.");
        }
        else
        {
            broadcast(self, "Created SUI page: " + page);
        }
        HashMap<String, HashMap<String, List<String>>> tree = new HashMap<>();
        String[] openedFiles = getAllTangibleFiles();
        StringBuilder outputText = new StringBuilder("Files. \n");

        for (String file : openedFiles)
        {
            String[] pathParts = file.split("/");
            if (pathParts.length > 2)
            {
                String root = pathParts[0] + "/" + pathParts[1]; // "object/tangible"
                String subfolder = pathParts[2]; // e.g., "item", "quest", etc.
                String filename = pathParts[pathParts.length - 1];

                tree.putIfAbsent(root, new HashMap<>());
                tree.get(root).putIfAbsent(subfolder, new ArrayList<>());
                tree.get(root).get(subfolder).add(filename);
            }
            outputText.append(file).append("\n");
        }

        clearSUIDataSourceContainer(page, "objectNav.dataTree");

        for (String root : tree.keySet())
        {
            addSUIDataSourceContainer(page, "objectNav.dataTree", root);
            setSUIProperty(page, "objectNav.dataTree." + root, "Text", root);

            HashMap<String, List<String>> subfolders = tree.get(root);
            for (String subfolder : subfolders.keySet())
            {
                String subfolderPath = root + "/" + subfolder;
                addSUIDataSourceContainer(page, "objectNav.dataTree." + root, subfolderPath);
                setSUIProperty(page, "objectNav.dataTree." + subfolderPath, "Text", subfolder);

                List<String> files = subfolders.get(subfolder);
                for (String filename : files)
                {
                    addSUIDataSourceContainer(page, "objectNav.dataTree." + subfolderPath, filename);
                    setSUIProperty(page, "objectNav.dataTree." + subfolderPath + "." + filename, "Text", filename);
                    subscribeToSUIProperty(page, "objectNav.dataTree", "Text");
                    subscribeToSUIProperty(page, "objectNav.dataTree", "dataTree");
                }
            }
        }
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "onRefreshButtonPressed", "btnRefresh");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "onSyncButtonPressed", "btnRefresh");
        flushSUIPage(page);
        showSUIPage(page);
    }

    public int onRefreshButtonPressed(obj_id self, dictionary params) throws InterruptedException
    {
        broadcast(self, "Refresh button pressed.");
        openTreeView(self);
        return SCRIPT_CONTINUE;
    }

    public int setOutputText(obj_id self, String text) throws InterruptedException
    {
        int page = getIntObjVar(self, "viewerPageId");
        sui.setSUIProperty(page, "outputPage.text", "Text", text);
        return SCRIPT_CONTINUE;
    }

    public int setViewerTarget(obj_id self, String text)
    {
        int page = getIntObjVar(self, "viewerPageId");
        sui.setSUIProperty(page, "ViewerPage.data.repo.repo", "appearanceTemplate", text);
        return SCRIPT_CONTINUE;

    }

    public int onSyncButtonPressed(obj_id self, dictionary params) throws InterruptedException
    {
        broadcast(self, "Sync button pressed.");
        openTreeView(self);
        return SCRIPT_CONTINUE;
    }

    public int onObjectSelected(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String selectedObject = params.getString("dataTree.text");
        broadcast(self, "Selected object: " + selectedObject);
        return SCRIPT_CONTINUE;
    }

    public String[] getAllTangibleFiles()
    {
        File tangibleDir = new File("/home/swg/swg-main/data/sku.0/sys.server/compiled/game/");
        List<String> matchingFiles = new ArrayList<>();
        String prefix = "object/tangible/";

        if (!tangibleDir.exists() || !tangibleDir.isDirectory())
        {
            System.out.println("Directory not found: " + tangibleDir.getAbsolutePath());
            return new String[0];
        }

        scanDirectory(tangibleDir, matchingFiles, prefix);

        return matchingFiles.toArray(new String[0]);
    }

    private void scanDirectory(File dir, List<String> matchingFiles, String prefix)
    {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files)
        {
            if (file.isDirectory())
            {
                scanDirectory(file, matchingFiles, prefix);
            }
            else
            {
                String fullPath = file.getPath().replace("\\", "/"); // Normalize paths for Linux
                int index = fullPath.indexOf(prefix);
                if (index != -1)
                {
                    matchingFiles.add(fullPath.substring(index));
                }
            }
        }
    }

}
