package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 7:27 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.DiscordWebhook;
import script.dictionary;
import script.library.chat;
import script.library.sui;
import script.obj_id;

public class tos_feedback extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Customer Service Representative");
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Customer Service Representative");
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnStartNpcConversation(obj_id self, obj_id speaker) throws InterruptedException
    {
        chat.chat(self, "Hello, " + speaker + ". Do you have any feedback to give?");
        showFeedbackWindow(self, speaker);
        return SCRIPT_CONTINUE;
    }

    public int showFeedbackWindow(obj_id self, obj_id speaker)
    {
        int page = createSUIPage("/Script.editScript", self, speaker);
        setSUIProperty(page, "pageText.text", "Font", "bold_22");
        setSUIProperty(page, "pageText.text", "Editable", "True");
        setSUIProperty(page, "pageText.text", "GetsInput", "True"); // allow copy and pasting.
        setSUIProperty(page, "outputPage.text", "Text", "Enter feedback in the box above.");
        setSUIProperty(page, "btnOk", "Text", "Send Feedback");//save button
        setSUIProperty(page, "bg.caption.text", "LocalText", "Feedback");//window title
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnOk", "pageText.text", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnOk", "outputPage.text", "LocalText");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnOk", "sendFeedback");
        setSUIAssociatedObject(page, speaker);
        showSUIPage(page);
        setObjVar(speaker, "feedback", page);
        return SCRIPT_CONTINUE;
    }

    public int sendFeedback(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id speaker = sui.getPlayerId(params);
        String feedback = params.getString("pageText.text.LocalText");
        DiscordWebhook d_feedBack = new DiscordWebhook("https://discord.com/api/webhooks/1239195164732424232/59zZtIke_vf9LgS4x8owNOE_nZOgxZdjfwZ-sEimCMKBJW4m8YcKp6bLby7zi_IdW0GN");
        d_feedBack.setContent("Player " + getPlayerFullName(speaker) + " has submitted feedback: \n\n" + feedback + ".");
        d_feedBack.setTts(true);
        d_feedBack.execute();
        chat.chat(self, "Thank you for your feedback, " + getPlayerFullName(speaker) + "!");
        sui.closeSUI(speaker, getIntObjVar(self, "feedback"));
        return SCRIPT_CONTINUE;
    }

}
