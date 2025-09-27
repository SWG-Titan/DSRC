package script.library;/*
@Origin: dsrc.script.library
@Author:  BubbaJoeX
@Purpose: Git manipulation
@Requirements: 3.1
@Notes: <no notes>
@Created: Sunday, 10/13/2024, at 9:50 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.obj_id;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static script.base_class.SCRIPT_CONTINUE;

public class git
{

    public static File WORKING = new File("/home/swg/swg-main/dsrc/");

    // Executes a shell command and returns the result as a list of strings
    private static List<String> executeCommand(obj_id issuer, String command, File workingDirectory)
    {
        List<String> output = new ArrayList<>();
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.directory(workingDirectory);
        try
        {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                output.add(line);
            }
            process.waitFor();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return output;
    }

    // Git login is handled by SSH key or token so no need for manual login as in Perforce
    public static void setupGit()
    {
        System.out.println("?");
    }

    // Opens a file for edit (in git this is more like staging the file for commit)
    public static void stageFileForCommit(obj_id issuer, String filePath)
    {
        String command = "git add " + filePath;
        executeCommand(issuer, command, WORKING);
        System.out.println("File staged for commit: " + filePath);
    }

    // Checks the list of modified and staged files
    public static String[] opened(obj_id issuer)
    {
        List<String> result = executeCommand(issuer, "git status -s", WORKING);
        return result.toArray(new String[0]);
    }

    // Creates a commit with a specified message
    public static void commit(obj_id author, String message) throws InterruptedException
    {
        String command = "git commit -m \"" + message + "\"";
        String output = executeCommand(author, command, WORKING).toString();
        sui.msgbox(author, author, output, sui.OK_CANCEL, "Git - Commit", "handleGitCommit");
    }

    // Shows the differences between the working directory and the latest commit
    public static String diff(obj_id issuer, String filePath)
    {
        String command = "git diff " + filePath;
        List<String> result = executeCommand(issuer, command, WORKING);
        StringBuilder diffOutput = new StringBuilder();
        for (String line : result)
        {
            diffOutput.append(line).append("\n");
        }
        return diffOutput.toString();
    }

    // Pushes the committed changes to the remote repository
    public static void push(obj_id issuer, String origin, String branch)
    {
        executeCommand(issuer, "git push " + origin + " " + branch, WORKING);
        System.out.println("Pushed changes to remote repository");
    }

    // Pulls the latest changes from the remote repository
    public static void pull(obj_id issuer, String origin, String branch)
    {
        executeCommand(issuer, "git pull " + origin + " " + branch, WORKING);
        System.out.println("Pulled latest changes from remote repository");
    }

    public static String getHistory(obj_id issuer)
    {
        // Execute the git log command to get a list of commits with one line summaries
        List<String> result = executeCommand(issuer, "git log --oneline", WORKING);

        // Combine the result into a single string
        StringBuilder history = new StringBuilder();
        for (String line : result)
        {
            history.append(line).append("\n");
        }
        return history.toString();
    }

    public int handleGitCommit(obj_id self, dictionary params)
    {
        params.getInt("buttonPressed");
        return SCRIPT_CONTINUE;
    }
}

