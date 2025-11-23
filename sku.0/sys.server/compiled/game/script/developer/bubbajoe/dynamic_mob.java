package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: Add dynamic-ness to a mob to make worldbuilding less static feeling.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 10/8/2025, at 9:15 PM, 
@Copyright © SWG - OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.chat;

import java.util.LinkedList;

import static script.library.ai_lib.aiFollow;

public class dynamic_mob extends script.base_script
{
    // Task class to hold task details
    private static class Task {
        obj_id actor;
        String task;
        dictionary params;
        Task(obj_id actor, String task, dictionary params) {
            this.actor = actor;
            this.task = task;
            this.params = params;
        }
    }

    // Per-mob task queue and processing flag
    private LinkedList<Task> taskQueue = new LinkedList<>();
    private boolean isProcessing = false;

    public int OnAttach(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int sync(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }


    public String[] validTasks = {
            "speak",
            "emote",
            "moveTo",
            "follow",
            "wander"
    };

    public boolean isValidTask(String task)
    {
        for (String valid : validTasks)
        {
            if (valid.equals(task))
            {
                return true;
            }
        }
        return false;
    }

    /*
    @Note: call this function to queue a task for the mob to perform. do not call doTask directly.
     */

    public int queueTask(obj_id actor, String task, dictionary params) throws InterruptedException
    {
        if (!isIdValid(actor) || !isMob(actor))
        {
            LOG("dynamic_mob", "queueTask: Invalid actor.");
            return SCRIPT_CONTINUE;
        }
        taskQueue.add(new Task(actor, task, params));
        if (!isProcessing) {
            LOG("dynamic_mob", "Queuing task: " + task + " for actor: " + actor);
            processQueue();
        }
        return SCRIPT_CONTINUE;
    }

    // Process the queue, one task at a time
    private void processQueue() throws InterruptedException
    {
        if (taskQueue.isEmpty()) {
            isProcessing = false;
            return;
        }
        isProcessing = true;
        Task current = taskQueue.peek();
        doTask(current.actor, current.task, current.params);
        // Schedule next task after delay (e.g., 1 second)
        messageTo(getSelf(), "processNextTask", null, 1.0f, false);
    }

    // Message handler to continue processing
    public int processNextTask(obj_id self, dictionary params) throws InterruptedException
    {
        if (!taskQueue.isEmpty()) {
            taskQueue.poll(); // Remove completed task
        }
        processQueue();
        return SCRIPT_CONTINUE;
    }

    public int doTask(obj_id actor, String task, dictionary params) throws InterruptedException
    {
        if (params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        if (task.equals("speak"))
        {
            String message = params.getString("message");
            if (message != null && !message.isEmpty())
            {
                chat.chat(actor, message);
            }
        }
        else if (task.equals("emote"))
        {
            String emote = params.getString("emote");
            if (emote != null && !emote.isEmpty())
            {
                doAnimationAction(actor, emote);
            }
        }
        else if (task.equals("moveTo"))
        {
            location loc = params.getLocation("location");
            if (loc != null)
            {
                setLocation(actor, loc);
            }
        }
        else if (task.equals("follow"))
        {
            obj_id target = params.getObjId("target");
            if (isIdValid(target))
            {
                aiFollow(actor, target);
            }
        }
        else if (task.equals("wander"))
        {
            // Simple random walk within a radius
            float radius = params.getFloat("radius");
            if (radius <= 0) radius = 5.0f;
            location here = getLocation(actor);
            float angle = rand(0, 360);
            float distance = rand(1, radius);
            float xOffset = distance * (float)Math.cos(Math.toRadians(angle));
            float zOffset = distance * (float)Math.sin(Math.toRadians(angle));
            location dest = new location(here.x + xOffset, here.y, here.z + zOffset, here.area, here.cell);
            setLocation(actor, dest);
        }
        else if (task.equals("aggroArea"))
        {
            float radius = params.getFloat("radius");
            if (radius <= 0) radius = 5.0f;
            obj_id[] players = getPlayerCreaturesInRange(actor, radius);
            if (players != null && players.length > 0)
            {
                for (obj_id player : players)
                {
                    if (isIdValid(player) && !isDead(player))
                    {
                        startCombat(actor, player);
                        break;
                    }
                }
            }
        }
        else
        {
            //show flytext ?, realistically this should never happen.
            showFlyText(actor, string_id.unlocalized("???"), 1.5f, color.RED);
        }
        LOG("dynamic_mob", "Completed task: " + task + " for actor: " + actor);
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws Exception
    {
        if (!isGod(speaker) || !isMob(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (text.equalsIgnoreCase("clear tasks"))
        {
            taskQueue.clear();
            isProcessing = false;
            debugSpeakMsg(self, "Task queue cleared.");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

}
