package script.developer.bubbajoe;

import script.dictionary;
import script.library.sui;
import script.location;
import script.obj_id;
import script.transform;

import java.io.*;

import static script.library.sui.*;
import static script.library.sui.OK_CANCEL;
import static script.library.utils.*;

public class housing_layout extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnSpeaking(obj_id self, String text) throws InterruptedException
    {
        if (text.equals("manageLayout"))
        {
            showImportExportOptions(self);
        }
        return SCRIPT_CONTINUE;
    }

    public void showImportExportOptions(obj_id self) throws InterruptedException
    {
        String[] options = {"Import Housing Layout", "Export Housing Layout"};
        sui.listbox(self, self, "Select an option:", sui.OK_CANCEL, "Housing Management", options, "handleImportExportOption");
    }

    public int handleImportExportOption(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = getPlayerId(params);
        int idx = getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        switch (idx)
        {
            case 0:
                getSavedHousingLayouts(self, player);
                break;
            case 1:
                exportHousingContents(player, getTopMostContainer(self), getPlayerAccountUsername(player) + "_" + player + "_" + getChoppedName(getTopMostContainer(player)));
                break;
        }
        return SCRIPT_CONTINUE;
    }

    private String getChoppedName(obj_id topMostContainer)
    {
        String fullName = getTemplateName(topMostContainer);
        int lastSlashIndex = fullName.lastIndexOf('/');

        int dotIndex = fullName.lastIndexOf('.');

        if (lastSlashIndex == -1 || dotIndex == -1)
        {
            return null;
        }

        return fullName.substring(lastSlashIndex + 1, dotIndex);
    }

    public void getSavedHousingLayouts(obj_id self, obj_id player) throws InterruptedException
    {
        String directoryPath = "/home/swg/swg-main/exe/linux/server/housing_exports/";
        String[] allFiles = listAllFiles(directoryPath);

        // Check if there are files to display
        if (allFiles.length == 0)
        {
            LOG("ethereal", "[Housing Import/Export]: No saved layout files found.");
            return;
        }

        setScriptVar(self, "housing.import.targets", allFiles);
        int pid = listbox(self, player, "Select a saved layout file to load:", OK_CANCEL, "Saved Housing Layouts", allFiles, "handleHousingLayoutSelection");
        sui.showSUIPage(pid);
    }

    public int handleHousingLayoutSelection(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "[Housing Import/Export]: Handler Params: " + params);
        obj_id player = getPlayerId(params);
        int selectedIndex = getListboxSelectedRow(params);
        String[] fileList = getStringArrayScriptVar(self, "housing.import.targets");
        LOG("ethereal", "[Housing Import/Export]: Importing housing layout from file: " + fileList[selectedIndex]);
        importHousingContents(player, getTopMostContainer(player), fileList[selectedIndex]);
        return SCRIPT_CONTINUE;
    }

    public int importHousingContents(obj_id who, obj_id building, String filename)
    {
        String filePath = "/home/swg/swg-main/exe/linux/server/housing_exports/" + filename;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null)
            {
                if (line.trim().isEmpty())
                {
                    continue;
                }
                String[] data = line.split("\t");
                if (data.length < 7)
                {
                    LOG("ethereal", "[Housing Importer]: Invalid file format, skipping line.");
                    continue;
                }

                String cellName = data[0].trim();
                String template = data[1].trim();
                float locX, locY, locZ;

                try
                {
                    locX = Float.parseFloat(data[2]);
                    locY = Float.parseFloat(data[3]);
                    locZ = Float.parseFloat(data[4]);
                } catch (NumberFormatException e)
                {
                    LOG("ethereal", "[Housing Importer]: Invalid location format, skipping line: " + e.getMessage());
                    continue;
                }

                if (!isValidLocation(locX, locY, locZ))
                {
                    LOG("ethereal", "[Housing Importer]: Invalid object location, skipping: " + locX + ", " + locY + ", " + locZ);
                    continue;
                }

                String rotationStr = data[5].trim();
                String scripts = data[6].trim();

                String[] matrixElements = rotationStr.split(":");
                if (matrixElements.length != 12)
                {
                    LOG("ethereal", "[Housing Importer]: Invalid matrix format. Expected 12 elements, found " + matrixElements.length);
                    continue;
                }

                float[][] matrix = new float[3][4];
                try
                {
                    for (int i = 0; i < 12; i++)
                    {
                        matrix[i / 4][i % 4] = Float.parseFloat(matrixElements[i]);
                    }
                } catch (NumberFormatException e)
                {
                    LOG("ethereal", "[Housing Importer]: Invalid number in matrix data: " + e.getMessage());
                    continue;
                }

                transform rotation = new transform(toTransform(matrix));
                obj_id cell = getCellId(building, cellName);

                if (!isIdValid(cell))
                {
                    LOG("ethereal", "[Housing Importer]: Cell not found or invalid: " + cellName);
                    continue;
                }

                location spawnLocation = new location(locX, locY, locZ, getCurrentSceneName(), cell);
                obj_id newObj = createObject(template, spawnLocation);

                if (isIdValid(newObj))
                {
                    setTransform_o2p(newObj, rotation);
                    setLocation(newObj, spawnLocation);
                    if (!scripts.isEmpty() && !scripts.equals("none"))
                    {
                        setPackedScripts(newObj, scripts);
                    }
                }
                else
                {
                    LOG("ethereal", "[Housing Importer]: Failed to create object from template: " + template);
                }
            }
            reader.close();
        } catch (IOException e)
        {
            LOG("ethereal", "[Housing Importer]: Error reading file: " + e.getMessage());
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }

    private boolean isValidLocation(float x, float y, float z)
    {
        return x >= -8192 && x <= 8192 && y >= -500 && y <= 500 && z >= -8192 && z <= 8192;
    }

    public int exportHousingContents(obj_id who, obj_id building, String filename) throws InterruptedException
    {
        StringBuilder fileContent = new StringBuilder();
        obj_id[] cells = getCellIds(building);

        for (obj_id cell : cells)
        {
            obj_id[] contents = getContents(cell);

            for (obj_id content : contents)
            {
                if (hasScript(content, "ai.ai") || hasScript(content, "ai.beast") || hasScript(content, "ai.creature_combat") || hasScript(content, "ai.pet"))
                {
                    continue;
                }

                String template = getTemplateName(content);
                if (isBadTemplate(template))
                {
                    continue;
                }

                location loc = getLocation(content);
                transform rotation = getTransform_o2p(content);

                StringBuilder contentString = new StringBuilder();
                contentString.append(getCellName(getContainedBy(content)))
                        .append("\t").append(template)
                        .append("\t").append(loc.x)
                        .append("\t").append(loc.y)
                        .append("\t").append(loc.z)
                        .append("\t").append(rotation.toColonString());

                String scripts = getPackedScripts(content);
                if (scripts != null && !scripts.isEmpty())
                {
                    String scriptList = scripts.replace("script.", "");
                    contentString.append("\t").append(scriptList);
                }
                else
                {
                    contentString.append("\tnone");
                }
                fileContent.append(contentString).append("\n");
            }
        }

        try
        {
            FileWriter fw = new FileWriter("/home/swg/swg-main/exe/linux/server/housing_exports/" + filename + ".house");
            fw.write(fileContent.toString());
            fw.close();
        } catch (IOException e)
        {
            LOG("ethereal", "[Housing Exporter]: " + e);
        }

        saveTextOnClient(who, filename, fileContent.toString());
        return SCRIPT_CONTINUE;
    }

    private boolean isBadTemplate(String template)
    {
        return template.startsWith("object/creature/") || template.startsWith("object/mobile/") || template.equals("object/tangible/terminal/terminal_player_structure.iff");
    }

    public transform toTransform(float[][] matrix)
    {
        float rotX = matrix[0][0];
        float rotY = matrix[1][0];
        float rotZ = matrix[2][0];
        float rotW = 1.0f;
        return new transform(rotX, rotY, rotZ, rotW);
    }

    private void setPackedScripts(obj_id newObj, String scripts)
    {
        String[] scriptList = scripts.split(",");
        for (String script : scriptList)
        {
            script = script.replace("script.", "");
            attachScript(newObj, script);
        }
    }

    private String[] listAllFiles(String directoryPath)
    {
        // Assume this function will interact with the file system to get all files
        // Here is a pseudo-implementation. Replace it with actual file reading logic.
        File dir = new File(directoryPath);

        // Get all files with .house extension in the directory
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".house"));

        // Convert to a String array
        if (files != null)
        {
            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++)
            {
                fileNames[i] = files[i].getName();
            }
            return fileNames;
        }

        // Return an empty array if no files are found
        return new String[0];
    }

    /**
     * Extracts the third underscore-separated value (house type) from a template name.
     * Example: "object/tangible/player_house/tatooine_small_style_01.iff" -> "tatooine"
     */
    private String extractHouseType(String buildingTemplate)
    {
        if (buildingTemplate == null || buildingTemplate.isEmpty())
        {
            return null;
        }

        // Split the template by underscores
        String[] parts = buildingTemplate.split("_");

        // Ensure there are at least three parts (dynamic checks)
        if (parts.length >= 3)
        {
            return parts[2]; // Return the third part
        }

        return null;
    }

    private String[] listFilesMatchingPattern(String directoryPath, String pattern)
    {
        // List all files in the directory
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory())
        {
            LOG("ethereal", "[getSavedHousingLayouts]: Directory not found: " + directoryPath);
            return null;
        }

        // Filter files based on the pattern
        File[] files = dir.listFiles((d, name) -> name.startsWith(pattern));
        if (files == null)
        {
            return new String[0];
        }

        // Collect matching file names
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++)
        {
            fileNames[i] = files[i].getName();
        }

        return fileNames;
    }
}
