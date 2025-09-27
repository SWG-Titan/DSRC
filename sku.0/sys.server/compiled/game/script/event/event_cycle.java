package script.event;/*
@Origin: script.developer.bubbajoe.
@Author: BubbaJoeX
@Purpose: Event Cycler
@Requirements: script.event.event_cycle
@Notes: Must have all four configs disabled.
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class event_cycle extends base_script
{
    private static final String[] EVENTS = {"loveday", "empireday", "halloween", "lifeday"};
    private static int currentEventIndex = 0;
    private static String currentEvent = "";

    public event_cycle()
    {
    }

    public int OnInitialize(obj_id self)
    {
        messageTo(self, "cycleEvents", null, 14400, false);
        return SCRIPT_CONTINUE;
    }

    public int cycleEvents(obj_id self, dictionary params)
    {
        if (!currentEvent.isEmpty())
        {
            LOG("events", "[Event Cycle]: Stopping " + currentEvent + " event.");
            stopUniverseWideEvent(currentEvent);
        }

        currentEvent = EVENTS[currentEventIndex];

        startUniverseWideEvent(currentEvent);
        LOG("events", "[Event Cycle]: Started " + currentEvent + " event.");

        currentEventIndex = (currentEventIndex + 1) % EVENTS.length;
        LOG("events", "[Event Cycle]: Shifting to next event in 4 hours.");

        messageTo(self, "cycleEvents", null, 14400, false);

        return SCRIPT_CONTINUE;
    }
}
