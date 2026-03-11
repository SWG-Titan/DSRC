package script.conversation_new;

import script.conversation.base.conversation_base;
import script.conversation.base.ConvoResponse;
import script.library.utils;
import script.obj_id;
import script.string_id;

/**
 * mysterious_informant.java
 *
 * A detailed example conversation showcasing the KOTOR-style cinematic conversation system.
 * This conversation features:
 * - Multiple branching paths
 * - All response prefix types ([Agree], [Decline], [Persuade], etc.)
 * - Deep narrative with meaningful choices
 * - State tracking for quest progression
 *
 * Attach this script to an NPC object to enable the conversation.
 */
public class mysterious_informant extends conversation_base
{
	// Branch IDs for tracking conversation state
	private static final int BRANCH_GREETING = 1;
	private static final int BRANCH_MISSION_DETAILS = 2;
	private static final int BRANCH_ACCEPT_MISSION = 3;
	private static final int BRANCH_DECLINE_REASON = 4;
	private static final int BRANCH_PERSUADE_REWARD = 5;
	private static final int BRANCH_INTIMIDATE_PATH = 6;
	private static final int BRANCH_ASK_ABOUT_TARGET = 7;
	private static final int BRANCH_ASK_ABOUT_DANGER = 8;
	private static final int BRANCH_FINAL_CHOICE = 9;
	private static final int BRANCH_LYING_PATH = 10;
	private static final int BRANCH_ATTACK_PATH = 11;

	// Response definitions - reusable across branches
	private static final ConvoResponse RESP_LISTEN = new ConvoResponse("listen", "[Agree] I'm listening. Tell me everything.");
	private static final ConvoResponse RESP_NOT_INTERESTED = new ConvoResponse("not_interested", "[Decline] I'm not interested in your schemes.");
	private static final ConvoResponse RESP_WHATS_IN_IT = new ConvoResponse("whats_in_it", "[Question] What's in it for me?");
	private static final ConvoResponse RESP_WHO_ARE_YOU = new ConvoResponse("who_are_you", "[Question] Who are you, exactly?");

	private static final ConvoResponse RESP_ACCEPT_MISSION = new ConvoResponse("accept", "[Agree] Count me in. I'll do it.");
	private static final ConvoResponse RESP_NEED_MORE_CREDITS = new ConvoResponse("more_credits", "[Persuade] The pay seems light for this risk.");
	private static final ConvoResponse RESP_THREATEN = new ConvoResponse("threaten", "[Intimidate] Maybe I should just take what I want.");
	private static final ConvoResponse RESP_DECLINE_FINAL = new ConvoResponse("decline_final", "[Decline] This isn't my kind of work.");

	private static final ConvoResponse RESP_TELL_TARGET = new ConvoResponse("tell_target", "[Question] Tell me about the target.");
	private static final ConvoResponse RESP_TELL_DANGER = new ConvoResponse("tell_danger", "[Question] How dangerous is this really?");
	private static final ConvoResponse RESP_ENOUGH_TALK = new ConvoResponse("enough_talk", "[Agree] Enough talk. Let's finalize the deal.");

	private static final ConvoResponse RESP_PRETEND_ACCEPT = new ConvoResponse("pretend", "[Lie] Of course I'll help. You can trust me completely.");
	private static final ConvoResponse RESP_CHANGE_MIND = new ConvoResponse("change_mind", "[Agree] Actually, I've reconsidered. I'll help.");
	private static final ConvoResponse RESP_LEAVE = new ConvoResponse("leave", "I should go.");

	private static final ConvoResponse RESP_ATTACK_NOW = new ConvoResponse("attack", "[Attack] I don't need your permission!");
	private static final ConvoResponse RESP_BACK_DOWN = new ConvoResponse("back_down", "[Decline] Forget I said anything.");

	private static final ConvoResponse RESP_UNDERSTOOD = new ConvoResponse("understood", "[Info] Understood. I know what to do.");
	private static final ConvoResponse RESP_ONE_MORE_QUESTION = new ConvoResponse("one_more", "[Question] One more question before I go.");

	public mysterious_informant()
	{
		super();
		scriptName = "conversation_new.mysterious_informant";
		conversation = "conversation_new.mysterious_informant";
	}

	@Override
	public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
	{
		// Check if player has already completed this conversation's quest
		if (utils.hasScriptVar(player, "mysterious_informant.quest_accepted"))
		{
			return serverSide_startConversation(player, self,
				"Ah, you've returned. Is it done?",
				BRANCH_GREETING,
				new ConvoResponse[] {
					new ConvoResponse("done", "[Agree] The job is complete."),
					new ConvoResponse("not_yet", "[Info] Not yet. I'm still working on it."),
					new ConvoResponse("changed_mind", "[Decline] I've changed my mind about the job.")
				}
			);
		}

		// Initial greeting for new players
		return serverSide_startConversation(player, self,
			"*The hooded figure glances around nervously before speaking in a hushed tone*\n\n" +
			"You look capable. I've been watching you for some time now. " +
			"I have a... proposition that requires someone with your particular talents. " +
			"But this conversation never happened. Understood?",
			BRANCH_GREETING,
			new ConvoResponse[] {
				RESP_LISTEN,
				RESP_NOT_INTERESTED,
				RESP_WHATS_IN_IT,
				RESP_WHO_ARE_YOU
			}
		);
	}

	@Override
	public int OnNpcConversationResponse(obj_id self, String conversationId, obj_id player, string_id response) throws InterruptedException
	{
		int branchId = utils.getIntScriptVar(player, conversation + ".branchId");

		// ========================================
		// BRANCH: Initial Greeting
		// ========================================
		if (branchId == BRANCH_GREETING)
		{
			if (responseIdIs(response, "listen"))
			{
				return serverSide_respond(player,
					"*A thin smile crosses their face*\n\n" +
					"Good. There's a data courier operating out of the starport. " +
					"He carries information that certain... interested parties would pay handsomely to acquire. " +
					"The courier doesn't know what he carries, but his employers do. " +
					"I need you to intercept the data before it reaches its destination.",
					BRANCH_MISSION_DETAILS,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						RESP_NEED_MORE_CREDITS,
						RESP_TELL_TARGET,
						RESP_TELL_DANGER,
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "not_interested"))
			{
				return serverSide_respond(player,
					"*Their eyes narrow*\n\n" +
					"A pity. I had hoped you'd be more... pragmatic. " +
					"Credits can change minds, as can circumstances. " +
					"Perhaps you'll reconsider when the alternative becomes clear.",
					BRANCH_DECLINE_REASON,
					new ConvoResponse[] {
						RESP_CHANGE_MIND,
						RESP_THREATEN,
						RESP_LEAVE
					}
				);
			}
			else if (responseIdIs(response, "whats_in_it"))
			{
				return serverSide_respond(player,
					"Straight to business. I respect that.\n\n" +
					"Ten thousand credits upon delivery of the data. " +
					"Another five thousand if you ensure the courier can't identify you. " +
					"This is more than fair for a few hours of work, wouldn't you say?",
					BRANCH_PERSUADE_REWARD,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						RESP_NEED_MORE_CREDITS,
						RESP_TELL_TARGET,
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "who_are_you"))
			{
				return serverSide_respond(player,
					"*They chuckle softly*\n\n" +
					"Who I am is irrelevant. What matters is what I represent: opportunity. " +
					"I work for people who value discretion and results. " +
					"People who reward loyalty generously... and punish betrayal severely. " +
					"Now, shall we discuss the task at hand?",
					BRANCH_GREETING,
					new ConvoResponse[] {
						RESP_LISTEN,
						RESP_NOT_INTERESTED,
						RESP_WHATS_IN_IT
					}
				);
			}
		}

		// ========================================
		// BRANCH: Mission Details
		// ========================================
		else if (branchId == BRANCH_MISSION_DETAILS)
		{
			if (responseIdIs(response, "accept"))
			{
				utils.setScriptVar(player, "mysterious_informant.quest_accepted", true);
				return serverSide_respond(player,
					"Excellent. I knew you were the right choice.\n\n" +
					"The courier arrives at docking bay 94 within the hour. " +
					"He's a Rodian, green jacket, silver case chained to his wrist. " +
					"Get the case, bring it to the cantina backroom. " +
					"And remember... we never spoke.",
					BRANCH_ACCEPT_MISSION,
					new ConvoResponse[] {
						RESP_UNDERSTOOD,
						RESP_ONE_MORE_QUESTION
					}
				);
			}
			else if (responseIdIs(response, "more_credits"))
			{
				return handlePersuadeReward(player);
			}
			else if (responseIdIs(response, "tell_target"))
			{
				return serverSide_respond(player,
					"*They lean closer, voice dropping*\n\n" +
					"The courier is a professional. Ex-military, trained in close combat. " +
					"Don't underestimate him just because he looks like a simple messenger. " +
					"He's survived three assassination attempts. The fourth shouldn't be yours to make... " +
					"unless absolutely necessary.",
					BRANCH_ASK_ABOUT_TARGET,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						RESP_TELL_DANGER,
						new ConvoResponse("lethal", "[Attack] Sounds like I should just eliminate him."),
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "tell_danger"))
			{
				return serverSide_respond(player,
					"*A heavy sigh escapes them*\n\n" +
					"I won't lie to you. The courier's employers have... reach. " +
					"If they discover your involvement, they will come for you. " +
					"But that's why discretion is paramount. Do this cleanly, " +
					"and you'll be richer with no one the wiser.",
					BRANCH_ASK_ABOUT_DANGER,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						new ConvoResponse("protection", "[Persuade] I'll need protection if things go wrong."),
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "decline_final"))
			{
				return handleFinalDecline(player);
			}
		}

		// ========================================
		// BRANCH: Persuade for Better Reward
		// ========================================
		else if (branchId == BRANCH_PERSUADE_REWARD)
		{
			if (responseIdIs(response, "accept"))
			{
				utils.setScriptVar(player, "mysterious_informant.quest_accepted", true);
				return serverSide_endConversation(player,
					"Then we have a deal. Docking bay 94, one hour. Don't be late."
				);
			}
			else if (responseIdIs(response, "more_credits"))
			{
				return handlePersuadeReward(player);
			}
			else if (responseIdIs(response, "tell_target"))
			{
				return serverSide_respond(player,
					"The target is merely a courier. What matters is what he carries.\n\n" +
					"Focus on the objective. Minimize complications. " +
					"The less attention drawn to this operation, the better for everyone.",
					BRANCH_MISSION_DETAILS,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						RESP_TELL_DANGER,
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "decline_final"))
			{
				return handleFinalDecline(player);
			}
		}

		// ========================================
		// BRANCH: Decline Reason / Second Chance
		// ========================================
		else if (branchId == BRANCH_DECLINE_REASON)
		{
			if (responseIdIs(response, "change_mind"))
			{
				return serverSide_respond(player,
					"*Relief flickers across their face*\n\n" +
					"I thought you might see reason. " +
					"Now, let me explain what needs to be done...",
					BRANCH_MISSION_DETAILS,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						RESP_NEED_MORE_CREDITS,
						RESP_TELL_TARGET,
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "threaten"))
			{
				return serverSide_respond(player,
					"*Their hand moves toward a concealed weapon*\n\n" +
					"That would be... unwise. I may look like an easy mark, " +
					"but I assure you, I am not alone. " +
					"Several weapons are trained on you as we speak. " +
					"Now, shall we return to civilized discourse?",
					BRANCH_INTIMIDATE_PATH,
					new ConvoResponse[] {
						RESP_BACK_DOWN,
						RESP_ATTACK_NOW,
						RESP_PRETEND_ACCEPT
					}
				);
			}
			else if (responseIdIs(response, "leave"))
			{
				return serverSide_endConversation(player,
					"*They watch you leave with cold, calculating eyes*\n\n" +
					"Remember: I know your face now. And I never forget."
				);
			}
		}

		// ========================================
		// BRANCH: Intimidate Path
		// ========================================
		else if (branchId == BRANCH_INTIMIDATE_PATH)
		{
			if (responseIdIs(response, "back_down"))
			{
				return serverSide_respond(player,
					"*They relax slightly*\n\n" +
					"A wise choice. Impulsive violence rarely solves problems. " +
					"Now, perhaps we can discuss this job like civilized beings?",
					BRANCH_MISSION_DETAILS,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						RESP_TELL_TARGET,
						RESP_LEAVE
					}
				);
			}
			else if (responseIdIs(response, "attack"))
			{
				// This would trigger combat
				return serverSide_endConversation(player,
					"*They signal to hidden allies*\n\n" +
					"You'll regret this decision."
				);
				// TODO: Start combat encounter
			}
			else if (responseIdIs(response, "pretend"))
			{
				return serverSide_respond(player,
					"*They study your face intently*\n\n" +
					"Your words say one thing, but your eyes... " +
					"No matter. If you betray me, I will know. " +
					"And the consequences will be... unpleasant. " +
					"For now, let's proceed with the arrangement.",
					BRANCH_LYING_PATH,
					new ConvoResponse[] {
						new ConvoResponse("continue_lie", "[Lie] You have nothing to worry about."),
						new ConvoResponse("actually_honest", "[Agree] Actually, let me be straight with you."),
						RESP_LEAVE
					}
				);
			}
		}

		// ========================================
		// BRANCH: Lying Path
		// ========================================
		else if (branchId == BRANCH_LYING_PATH)
		{
			if (responseIdIs(response, "continue_lie"))
			{
				utils.setScriptVar(player, "mysterious_informant.player_lying", true);
				utils.setScriptVar(player, "mysterious_informant.quest_accepted", true);
				return serverSide_endConversation(player,
					"*A dangerous smile appears*\n\n" +
					"We shall see. Docking bay 94. One hour. " +
					"And stranger... I'll be watching."
				);
			}
			else if (responseIdIs(response, "actually_honest"))
			{
				return serverSide_respond(player,
					"*Their expression softens slightly*\n\n" +
					"Honesty. How... refreshing. Most in this business " +
					"wouldn't hesitate to deceive. Your candor earns you a measure of respect. " +
					"So, what's your true position on my offer?",
					BRANCH_MISSION_DETAILS,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						RESP_DECLINE_FINAL,
						RESP_NEED_MORE_CREDITS
					}
				);
			}
			else if (responseIdIs(response, "leave"))
			{
				return serverSide_endConversation(player,
					"*They make no move to stop you, but their gaze follows you out*\n\n" +
					"Until next time..."
				);
			}
		}

		// ========================================
		// BRANCH: Target Information
		// ========================================
		else if (branchId == BRANCH_ASK_ABOUT_TARGET)
		{
			if (responseIdIs(response, "accept"))
			{
				utils.setScriptVar(player, "mysterious_informant.quest_accepted", true);
				return serverSide_endConversation(player,
					"The courier awaits. Remember: efficiency and discretion. " +
					"I expect results, not excuses."
				);
			}
			else if (responseIdIs(response, "tell_danger"))
			{
				return serverSide_respond(player,
					"Danger is relative. To a skilled operative like yourself? " +
					"This should be routine. To an amateur? Fatal. " +
					"Which are you?",
					BRANCH_ASK_ABOUT_DANGER,
					new ConvoResponse[] {
						RESP_ACCEPT_MISSION,
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "lethal"))
			{
				return serverSide_respond(player,
					"*They shake their head slowly*\n\n" +
					"Killing creates complications. Bodies draw attention. " +
					"Attention draws investigators. I need the data, not a corpse. " +
					"Incapacitate if necessary, but avoid lethal measures. " +
					"Can you handle that level of... restraint?",
					BRANCH_FINAL_CHOICE,
					new ConvoResponse[] {
						new ConvoResponse("i_can", "[Agree] I can be surgical when needed."),
						new ConvoResponse("no_promises", "[Intimidate] I make no promises."),
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "decline_final"))
			{
				return handleFinalDecline(player);
			}
		}

		// ========================================
		// BRANCH: Danger Information
		// ========================================
		else if (branchId == BRANCH_ASK_ABOUT_DANGER)
		{
			if (responseIdIs(response, "accept"))
			{
				utils.setScriptVar(player, "mysterious_informant.quest_accepted", true);
				return serverSide_endConversation(player,
					"Your courage is noted. Or perhaps it's foolishness. " +
					"Either way, results are what matter. Go."
				);
			}
			else if (responseIdIs(response, "protection"))
			{
				return serverSide_respond(player,
					"*They consider this for a moment*\n\n" +
					"Protection can be arranged... for a price. " +
					"Ten percent of your payment would secure a safehouse and false documents. " +
					"Should things go wrong, you'd have an exit strategy. " +
					"Is this arrangement acceptable?",
					BRANCH_FINAL_CHOICE,
					new ConvoResponse[] {
						new ConvoResponse("take_insurance", "[Agree] That sounds wise. I'll take the insurance."),
						new ConvoResponse("no_insurance", "[Decline] I'll take my chances."),
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "decline_final"))
			{
				return handleFinalDecline(player);
			}
		}

		// ========================================
		// BRANCH: Final Choice
		// ========================================
		else if (branchId == BRANCH_FINAL_CHOICE)
		{
			if (responseIdIs(response, "i_can") || responseIdIs(response, "take_insurance") || responseIdIs(response, "no_insurance"))
			{
				utils.setScriptVar(player, "mysterious_informant.quest_accepted", true);

				if (responseIdIs(response, "take_insurance"))
				{
					utils.setScriptVar(player, "mysterious_informant.has_insurance", true);
				}

				return serverSide_endConversation(player,
					"*They nod with satisfaction*\n\n" +
					"Then our business is concluded. For now. " +
					"Docking bay 94. One hour. Don't disappoint me."
				);
			}
			else if (responseIdIs(response, "no_promises"))
			{
				return serverSide_respond(player,
					"*Their voice turns icy*\n\n" +
					"Then perhaps you are not the operative I need. " +
					"I require precision, not a blunt instrument. " +
					"Make your choice now: follow instructions, or walk away.",
					BRANCH_FINAL_CHOICE,
					new ConvoResponse[] {
						new ConvoResponse("follow_orders", "[Agree] Fine. I'll do it your way."),
						RESP_DECLINE_FINAL
					}
				);
			}
			else if (responseIdIs(response, "follow_orders"))
			{
				utils.setScriptVar(player, "mysterious_informant.quest_accepted", true);
				return serverSide_endConversation(player,
					"That's what I wanted to hear. Now go. Time is running out."
				);
			}
			else if (responseIdIs(response, "decline_final"))
			{
				return handleFinalDecline(player);
			}
		}

		// ========================================
		// BRANCH: After Mission Accepted (Return Visit)
		// ========================================
		else if (branchId == BRANCH_ACCEPT_MISSION)
		{
			if (responseIdIs(response, "understood"))
			{
				return serverSide_endConversation(player,
					"*They fade back into the shadows*\n\n" +
					"Good hunting, stranger."
				);
			}
			else if (responseIdIs(response, "one_more"))
			{
				return serverSide_respond(player,
					"*An impatient sigh*\n\n" +
					"Make it quick. Time is not our ally.",
					BRANCH_ACCEPT_MISSION,
					new ConvoResponse[] {
						new ConvoResponse("what_if_fail", "[Question] What happens if I fail?"),
						new ConvoResponse("backup", "[Question] Will I have any backup?"),
						RESP_UNDERSTOOD
					}
				);
			}
			else if (responseIdIs(response, "what_if_fail"))
			{
				return serverSide_respond(player,
					"*A cold smile*\n\n" +
					"Failure is not an option I've prepared for. " +
					"If you fail, you're on your own. " +
					"I was never here. We never spoke. " +
					"Do I make myself clear?",
					BRANCH_ACCEPT_MISSION,
					new ConvoResponse[] {
						RESP_UNDERSTOOD,
						new ConvoResponse("reconsider", "[Decline] Maybe I should reconsider...")
					}
				);
			}
			else if (responseIdIs(response, "backup"))
			{
				return serverSide_respond(player,
					"*They almost laugh*\n\n" +
					"Backup? This is a solo operation. " +
					"The fewer people involved, the smaller the trail. " +
					"You're on your own, but that's how the best work anyway, isn't it?",
					BRANCH_ACCEPT_MISSION,
					new ConvoResponse[] {
						RESP_UNDERSTOOD
					}
				);
			}
			else if (responseIdIs(response, "reconsider"))
			{
				utils.removeScriptVar(player, "mysterious_informant.quest_accepted");
				return serverSide_endConversation(player,
					"*Their expression darkens*\n\n" +
					"Cold feet at the last moment. Disappointing. " +
					"Get out of my sight before I reconsider your usefulness."
				);
			}
		}

		// Fallback
		return serverSide_endConversation(player,
			"*They turn away, the conversation clearly over*\n\n" +
			"We're done here."
		);
	}

	// ========================================
	// Helper Methods
	// ========================================

	private int handlePersuadeReward(obj_id player) throws InterruptedException
	{
		// Track persuasion attempts
		int attempts = utils.getIntScriptVar(player, "mysterious_informant.persuade_attempts");
		utils.setScriptVar(player, "mysterious_informant.persuade_attempts", attempts + 1);

		if (attempts == 0)
		{
			return serverSide_respond(player,
				"*They pause, considering*\n\n" +
				"You drive a hard bargain. Fifteen thousand total. " +
				"That's my final offer. Take it or leave it.",
				BRANCH_PERSUADE_REWARD,
				new ConvoResponse[] {
					new ConvoResponse("accept_better", "[Agree] Fifteen thousand works. I'm in."),
					new ConvoResponse("push_more", "[Persuade] I was thinking more like twenty."),
					RESP_DECLINE_FINAL
				}
			);
		}
		else if (attempts == 1)
		{
			return serverSide_respond(player,
				"*Their patience is clearly wearing thin*\n\n" +
				"You test my patience. Eighteen thousand, " +
				"and you'll handle the job personally without farming it out. " +
				"This is genuinely my final offer.",
				BRANCH_PERSUADE_REWARD,
				new ConvoResponse[] {
					new ConvoResponse("accept_final", "[Agree] Eighteen thousand. Deal."),
					RESP_THREATEN,
					RESP_DECLINE_FINAL
				}
			);
		}
		else
		{
			return serverSide_respond(player,
				"*They stand abruptly*\n\n" +
				"Enough. You've wasted enough of my time with your haggling. " +
				"The original offer stands, or you walk away with nothing. " +
				"Choose. Now.",
				BRANCH_FINAL_CHOICE,
				new ConvoResponse[] {
					new ConvoResponse("accept_original", "[Agree] Fine. Original terms. I'll do it."),
					RESP_DECLINE_FINAL
				}
			);
		}
	}

	private int handleFinalDecline(obj_id player) throws InterruptedException
	{
		utils.removeScriptVar(player, "mysterious_informant.quest_accepted");
		utils.removeScriptVar(player, "mysterious_informant.persuade_attempts");

		return serverSide_endConversation(player,
			"*They stare at you with unreadable eyes*\n\n" +
			"Your loss. But remember this encounter, stranger. " +
			"In this galaxy, those who refuse opportunity often find " +
			"themselves on the wrong side of it.\n\n" +
			"*They melt back into the shadows and disappear*"
		);
	}
}

