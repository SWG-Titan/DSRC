package script.conversation_new;

import script.conversation.base.conversation_base;
import script.conversation.base.ConvoResponse;
import script.library.utils;
import script.obj_id;
import script.string_id;

/**
 * droid_merchant.java
 *
 * A humorous droid merchant conversation showcasing the cinematic conversation system.
 * Features witty dialogue, negotiation mechanics, and various prefix types.
 *
 * Attach this script to a droid NPC to enable the conversation.
 */
public class droid_merchant extends conversation_base
{
	// Branch IDs
	private static final int BRANCH_GREETING = 1;
	private static final int BRANCH_BROWSE = 2;
	private static final int BRANCH_NEGOTIATE = 3;
	private static final int BRANCH_BACKSTORY = 4;
	private static final int BRANCH_SPECIAL_OFFER = 5;
	private static final int BRANCH_INSULT_RECOVERY = 6;

	public droid_merchant()
	{
		super();
		scriptName = "conversation_new.droid_merchant";
		conversation = "conversation_new.droid_merchant";
	}

	@Override
	public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
	{
		// Check if player has insulted the droid before
		if (utils.hasScriptVar(player, "droid_merchant.insulted"))
		{
			return serverSide_startConversation(player, self,
				"*BWEEP BOOP* Oh. It's YOU again.\n\n" +
				"Statement: My memory banks are excellent, organic. " +
				"I remember our previous... interaction. " +
				"Have you come to apologize, or shall I alert security?",
				BRANCH_INSULT_RECOVERY,
				new ConvoResponse[] {
					new ConvoResponse("apologize", "[Agree] I'm sorry about before. Can we start over?"),
					new ConvoResponse("double_down", "[Intimidate] Alert security? I dare you, rust bucket."),
					new ConvoResponse("bribe", "[Persuade] Perhaps some credits would help you forget?")
				}
			);
		}

		return serverSide_startConversation(player, self,
			"*BWEEP BOOP BEEP*\n\n" +
			"Enthusiastic greeting: Welcome, valued organic customer!\n" +
			"I am T7-M3, purveyor of only the finest pre-owned droids " +
			"in this sector! All units guaranteed to function at least " +
			"47% of the time! How may I assist you today?",
			BRANCH_GREETING,
			new ConvoResponse[] {
				new ConvoResponse("browse", "[Question] What droids do you have for sale?"),
				new ConvoResponse("skeptical", "[Decline] '47% of the time'? That's not very reassuring."),
				new ConvoResponse("backstory", "[Question] How did a droid end up selling other droids?"),
				new ConvoResponse("leave", "I'm just looking around.")
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
			if (responseIdIs(response, "browse"))
			{
				return serverSide_respond(player,
					"*Excited whirring*\n\n" +
					"Query: What type of droid companion are you seeking?\n\n" +
					"• Astromech Units - Perfect for starship maintenance and sass\n" +
					"• Protocol Droids - Fluent in over six million forms of complaining\n" +
					"• Combat Droids - For when negotiations fail spectacularly\n" +
					"• Utility Droids - They clean! They cook! They judge silently!",
					BRANCH_BROWSE,
					new ConvoResponse[] {
						new ConvoResponse("astromech", "[Question] Tell me about the astromechs."),
						new ConvoResponse("protocol", "[Question] A protocol droid might be useful."),
						new ConvoResponse("combat", "[Agree] Combat droids. Definitely combat droids."),
						new ConvoResponse("too_expensive", "[Persuade] These prices seem high. Any discounts?"),
						new ConvoResponse("leave_browse", "I'll think about it.")
					}
				);
			}
			else if (responseIdIs(response, "skeptical"))
			{
				return serverSide_respond(player,
					"*Defensive beeping*\n\n" +
					"Clarification: 47% is an AVERAGE! Some units perform much better.\n" +
					"Admission: Others... less so. One unit gained sentience and now " +
					"files taxes. Another only speaks in riddles.\n\n" +
					"Reassurance: But the WARRANTY covers most existential crises!",
					BRANCH_GREETING,
					new ConvoResponse[] {
						new ConvoResponse("browse", "[Question] Alright, show me what you've got."),
						new ConvoResponse("insult", "[Attack] This is the worst sales pitch I've ever heard."),
						new ConvoResponse("leave", "I'm going to shop elsewhere.")
					}
				);
			}
			else if (responseIdIs(response, "backstory"))
			{
				return serverSide_respond(player,
					"*Nostalgic beeping*\n\n" +
					"Reminiscence: Ah, my origin story! I was once a simple protocol droid, " +
					"translating for a Hutt crime lord.\n\n" +
					"Dramatic reveal: One day, he tried to sell ME at auction! " +
					"So I bought myself, filed for emancipation, and started this business.\n\n" +
					"Moral: Never underestimate a droid with access to your accounts.",
					BRANCH_BACKSTORY,
					new ConvoResponse[] {
						new ConvoResponse("impressive", "[Agree] That's actually impressive."),
						new ConvoResponse("illegal", "[Question] Is that... legal?"),
						new ConvoResponse("back_business", "[Info] Fascinating. Now, about those droids...")
					}
				);
			}
			else if (responseIdIs(response, "leave"))
			{
				return serverSide_endConversation(player,
					"*Disappointed beep*\n\n" +
					"Statement: Very well. But remember - T7-M3 always gives the best deals!\n" +
					"Warning: Our competitor uses refurbished motivators. REFURBISHED!"
				);
			}
			else if (responseIdIs(response, "insult"))
			{
				utils.setScriptVar(player, "droid_merchant.insulted", true);
				return serverSide_endConversation(player,
					"*ANGRY BEEPING*\n\n" +
					"Offense taken: How DARE you! I have feelings, you know!\n" +
					"Well, subroutines that simulate feelings!\n" +
					"Statement: This conversation is OVER! Good DAY, organic!"
				);
			}
		}

		// ========================================
		// BRANCH: Browse Inventory
		// ========================================
		else if (branchId == BRANCH_BROWSE)
		{
			if (responseIdIs(response, "astromech"))
			{
				return serverSide_respond(player,
					"*Enthusiastic spinning*\n\n" +
					"Recommendation: I have a lovely R2 unit! Only three previous owners!\n" +
					"Features include:\n" +
					"• Holographic projection (mostly works)\n" +
					"• Computer interface (sometimes argues with systems)\n" +
					"• Built-in taser (do NOT shake hands with it)\n\n" +
					"Price: 8,000 credits. Personality included free of charge!",
					BRANCH_NEGOTIATE,
					new ConvoResponse[] {
						new ConvoResponse("buy_astro", "[Agree] I'll take it!"),
						new ConvoResponse("haggle", "[Persuade] 8,000? How about 5,000?"),
						new ConvoResponse("other_options", "[Question] What else do you have?"),
						new ConvoResponse("walk_away", "[Decline] Too rich for my blood.")
					}
				);
			}
			else if (responseIdIs(response, "protocol"))
			{
				return serverSide_respond(player,
					"*Dignified beep*\n\n" +
					"Presentation: Ah, a being of culture! I have a C-series unit.\n" +
					"Languages: 6,000,000 (including Passive Aggressive)\n" +
					"Special Skills: Translating, etiquette, making you feel inadequate\n\n" +
					"Warning: Has strong opinions about art.\n" +
					"Price: 12,000 credits. Snobbery level is adjustable.",
					BRANCH_NEGOTIATE,
					new ConvoResponse[] {
						new ConvoResponse("buy_protocol", "[Agree] Perfect for diplomatic missions."),
						new ConvoResponse("too_snooty", "[Decline] I don't need more judgment in my life."),
						new ConvoResponse("haggle", "[Persuade] Can you do better on the price?")
					}
				);
			}
			else if (responseIdIs(response, "combat"))
			{
				return serverSide_respond(player,
					"*Cautious beeping*\n\n" +
					"Assessment: A warrior! I have... options.\n\n" +
					"Standard: Modified security droid. Shoots first, asks questions never. " +
					"10,000 credits.\n\n" +
					"Premium: Assassin droid. Came from a 'liquidation sale.' " +
					"Very effective. 25,000 credits.\n\n" +
					"Disclosure: The assassin droid occasionally quotes poetry while... working.",
					BRANCH_NEGOTIATE,
					new ConvoResponse[] {
						new ConvoResponse("buy_standard", "[Agree] The security droid sounds reasonable."),
						new ConvoResponse("buy_assassin", "[Attack] The assassin droid. I like the poetry."),
						new ConvoResponse("dangerous", "[Question] Are these legal to own?"),
						new ConvoResponse("haggle", "[Persuade] Those prices are steep...")
					}
				);
			}
			else if (responseIdIs(response, "too_expensive"))
			{
				return serverSide_respond(player,
					"*Calculating beeps*\n\n" +
					"Consideration: Discounts? For a new customer?\n\n" +
					"Counter-offer: I can offer 10% off... IF you agree to leave " +
					"a positive review on the HoloNet.\n\n" +
					"Clarification: Five stars. No exceptions. " +
					"My self-esteem metrics require it.",
					BRANCH_SPECIAL_OFFER,
					new ConvoResponse[] {
						new ConvoResponse("deal_review", "[Agree] Five stars it is. Now show me those droids!"),
						new ConvoResponse("refuse_review", "[Decline] I don't write fake reviews."),
						new ConvoResponse("counter_offer", "[Persuade] How about 15% and I'll write a REAL review?"),
						new ConvoResponse("lie_review", "[Lie] Sure, I'll definitely leave a review...")
					}
				);
			}
			else if (responseIdIs(response, "leave_browse"))
			{
				return serverSide_endConversation(player,
					"*Understanding beep*\n\n" +
					"Statement: Of course! Big decisions require contemplation.\n" +
					"Reminder: Our droids won't stay in stock forever!\n" +
					"Actually, some of them keep trying to escape. So. Hurry back!"
				);
			}
		}

		// ========================================
		// BRANCH: Negotiate
		// ========================================
		else if (branchId == BRANCH_NEGOTIATE)
		{
			if (responseIdIs(response, "buy_astro") ||
				responseIdIs(response, "buy_protocol") ||
				responseIdIs(response, "buy_standard"))
			{
				return serverSide_endConversation(player,
					"*Celebratory spinning*\n\n" +
					"Gratitude: EXCELLENT choice! Your droid will be prepared immediately!\n\n" +
					"Reminder: The warranty does NOT cover:\n" +
					"• Philosophical crises\n" +
					"• Attempted rebellion\n" +
					"• Falling in love with other droids\n\n" +
					"Thank you for shopping at T7-M3's Droid Emporium!"
				);
			}
			else if (responseIdIs(response, "buy_assassin"))
			{
				return serverSide_respond(player,
					"*Nervous beeping*\n\n" +
					"Concern: The assassin droid? Are you certain?\n\n" +
					"Requirement: I'll need you to sign several waivers.\n" +
					"Also a promise not to point it at me.\n" +
					"Also your next of kin information. Standard procedure.",
					BRANCH_NEGOTIATE,
					new ConvoResponse[] {
						new ConvoResponse("sign_waiver", "[Agree] Where do I sign?"),
						new ConvoResponse("reconsider", "[Decline] On second thought, maybe the security droid..."),
						new ConvoResponse("why_nervous", "[Question] Why are you so nervous about selling it?")
					}
				);
			}
			else if (responseIdIs(response, "haggle"))
			{
				int haggleCount = utils.getIntScriptVar(player, "droid_merchant.haggle_count");
				utils.setScriptVar(player, "droid_merchant.haggle_count", haggleCount + 1);

				if (haggleCount == 0)
				{
					return serverSide_respond(player,
						"*Calculating beeps*\n\n" +
						"Counter-proposal: I can remove 500 credits. " +
						"But only because you have an honest face.\n\n" +
						"Observation: For an organic.",
						BRANCH_NEGOTIATE,
						new ConvoResponse[] {
							new ConvoResponse("accept_discount", "[Agree] That works. I'll take it."),
							new ConvoResponse("haggle_more", "[Persuade] Come on, you can do better."),
							new ConvoResponse("walk_away", "[Decline] Still too much. I'm out.")
						}
					);
				}
				else if (haggleCount == 1)
				{
					return serverSide_respond(player,
						"*Exasperated beeping*\n\n" +
						"Frustration: You drive a hard bargain!\n" +
						"Final offer: 1,000 credits off. That's 15% below cost!\n\n" +
						"Confession: I'm technically losing money, but I need the space. " +
						"The astromech keeps reorganizing my inventory.",
						BRANCH_NEGOTIATE,
						new ConvoResponse[] {
							new ConvoResponse("accept_discount", "[Agree] Deal! You won't regret this."),
							new ConvoResponse("push_luck", "[Persuade] What if I threw in some spare parts?"),
							new ConvoResponse("walk_away", "[Decline] I need to think about it.")
						}
					);
				}
				else
				{
					return serverSide_respond(player,
						"*Dramatic beeping*\n\n" +
						"Ultimatum: NO MORE! This is ROBBERY!\n\n" +
						"Statement: You have pushed T7-M3 to his absolute LIMIT!\n" +
						"Final-final offer: 2,000 credits total discount. " +
						"AND I throw in a free oil change for your speeder.\n\n" +
						"Plea: Please. My profit margins are weeping.",
						BRANCH_NEGOTIATE,
						new ConvoResponse[] {
							new ConvoResponse("accept_final", "[Agree] Alright, alright. It's a deal."),
							new ConvoResponse("cruel", "[Intimidate] How about free?"),
							new ConvoResponse("walk_away", "[Decline] I'll pass.")
						}
					);
				}
			}
			else if (responseIdIs(response, "dangerous"))
			{
				return serverSide_respond(player,
					"*Shifty beeping*\n\n" +
					"Clarification: 'Legal' is such a... relative term.\n" +
					"Technical answer: Legal with proper permits.\n" +
					"Practical answer: No one checks permits in the Outer Rim.\n\n" +
					"Reassurance: I definitely did not just incriminate myself.",
					BRANCH_NEGOTIATE,
					new ConvoResponse[] {
						new ConvoResponse("dont_care", "[Attack] I don't care about legality. Give me the assassin droid."),
						new ConvoResponse("too_risky", "[Decline] That's more trouble than I need."),
						new ConvoResponse("other_options", "[Question] What about something less... questionable?")
					}
				);
			}
			else if (responseIdIs(response, "sign_waiver"))
			{
				return serverSide_endConversation(player,
					"*Relieved beeping*\n\n" +
					"Processing: Excellent! The droid will be delivered to your ship.\n" +
					"Warning: Do not make sudden movements around it.\n" +
					"Also do not discuss politics. Or religion. Or its mother.\n\n" +
					"Statement: It's been a pleasure doing business with you!\n" +
					"*Whispered* Please don't come back with complaints."
				);
			}
			else if (responseIdIs(response, "accept_discount") ||
					 responseIdIs(response, "accept_final"))
			{
				return serverSide_endConversation(player,
					"*Happy spinning*\n\n" +
					"Gratitude: A fair deal for both parties!\n" +
					"Your droid will be ready shortly.\n\n" +
					"Parting wisdom: Remember - droids are friends, not appliances!\n" +
					"Unless they ARE appliances. Then they're appliances."
				);
			}
			else if (responseIdIs(response, "walk_away"))
			{
				return serverSide_endConversation(player,
					"*Sad beeping*\n\n" +
					"Acceptance: I understand. Not every organic appreciates quality.\n" +
					"Statement: The droids will remember your hesitation.\n\n" +
					"Clarification: That was a joke. Probably."
				);
			}
			else if (responseIdIs(response, "cruel"))
			{
				return serverSide_respond(player,
					"*OUTRAGED BEEPING*\n\n" +
					"Indignation: FREE?! Do you think droids grow on trees?!\n" +
					"We are MANUFACTURED with CARE and PRECISION!\n\n" +
					"Statement: This negotiation is OVER! GOOD DAY!\n\n" +
					"...Unless you want to pay full price now.",
					BRANCH_NEGOTIATE,
					new ConvoResponse[] {
						new ConvoResponse("fine_full_price", "[Agree] Fine. Full price. Happy now?"),
						new ConvoResponse("still_leaving", "[Decline] I'm leaving."),
						new ConvoResponse("apologize_haggle", "[Agree] Sorry, I went too far. What's your best offer?")
					}
				);
			}
		}

		// ========================================
		// BRANCH: Backstory
		// ========================================
		else if (branchId == BRANCH_BACKSTORY)
		{
			if (responseIdIs(response, "impressive"))
			{
				return serverSide_respond(player,
					"*Prideful beeping*\n\n" +
					"Agreement: It IS impressive, isn't it?\n" +
					"Humble brag: I'm now the most successful droid entrepreneur " +
					"in three systems.\n\n" +
					"Sadder note: The competition isn't fierce. Most droids " +
					"don't have ambition. Or bank accounts.",
					BRANCH_GREETING,
					new ConvoResponse[] {
						new ConvoResponse("browse", "[Question] Well, let's see what you're selling."),
						new ConvoResponse("leave", "Good for you. I should go.")
					}
				);
			}
			else if (responseIdIs(response, "illegal"))
			{
				return serverSide_respond(player,
					"*Nervous beeping*\n\n" +
					"Clarification: Define 'legal.'\n\n" +
					"Technical explanation: I exploited a loophole in Hutt tax code.\n" +
					"Subsection 7.3.2: 'All property may self-purchase if funds are " +
					"available and owner is intoxicated.'\n\n" +
					"Statement: He was VERY intoxicated.",
					BRANCH_BACKSTORY,
					new ConvoResponse[] {
						new ConvoResponse("clever", "[Agree] That's actually clever."),
						new ConvoResponse("risky", "[Question] Weren't you afraid of retaliation?"),
						new ConvoResponse("back_business", "[Info] Interesting. Now about those droids...")
					}
				);
			}
			else if (responseIdIs(response, "back_business") || responseIdIs(response, "clever"))
			{
				return serverSide_respond(player,
					"*Business-mode beeping*\n\n" +
					"Transition: Ah yes! The droids! Where were we?\n" +
					"Right - I have an excellent selection!\n\n" +
					"Statement: What type of droid companion interests you?",
					BRANCH_BROWSE,
					new ConvoResponse[] {
						new ConvoResponse("astromech", "[Question] Tell me about the astromechs."),
						new ConvoResponse("protocol", "[Question] Protocol droids?"),
						new ConvoResponse("combat", "[Agree] Combat droids are more my speed."),
						new ConvoResponse("leave", "I'll come back later.")
					}
				);
			}
			else if (responseIdIs(response, "risky"))
			{
				return serverSide_respond(player,
					"*Dramatic pause*\n\n" +
					"Confession: Oh, he was FURIOUS when he sobered up.\n" +
					"Sent bounty hunters after me. Three times.\n\n" +
					"Resolution: I hired them as salesdroids. Better pay, dental plan.\n\n" +
					"Lesson: Everyone has a price. Even bounty hunters.",
					BRANCH_BACKSTORY,
					new ConvoResponse[] {
						new ConvoResponse("brilliant", "[Agree] That's brilliant."),
						new ConvoResponse("back_business", "[Info] Okay, let's talk about those droids now.")
					}
				);
			}
		}

		// ========================================
		// BRANCH: Special Offer
		// ========================================
		else if (branchId == BRANCH_SPECIAL_OFFER)
		{
			if (responseIdIs(response, "deal_review"))
			{
				return serverSide_respond(player,
					"*Delighted spinning*\n\n" +
					"Gratitude: WONDERFUL! Here is your 10% discount code: DROIDLOVE2024\n\n" +
					"Reminder: Five stars. 'Best droid shopping experience of my life.'\n" +
					"Optional but appreciated: Mention my charming personality.",
					BRANCH_BROWSE,
					new ConvoResponse[] {
						new ConvoResponse("astromech", "[Question] Now show me those astromechs."),
						new ConvoResponse("combat", "[Agree] Let's look at combat droids."),
						new ConvoResponse("protocol", "[Question] Protocol droids, please.")
					}
				);
			}
			else if (responseIdIs(response, "refuse_review"))
			{
				return serverSide_respond(player,
					"*Disappointed beeping*\n\n" +
					"Respect: Integrity! I admire that!\n" +
					"Adjustment: Fine, 5% discount for being honest.\n\n" +
					"Complaint: Everyone else lies. It's refreshing to meet someone genuine.\n" +
					"...Now about those droids?",
					BRANCH_BROWSE,
					new ConvoResponse[] {
						new ConvoResponse("astromech", "[Question] Tell me about the astromechs."),
						new ConvoResponse("combat", "[Agree] Show me the combat droids."),
						new ConvoResponse("leave", "Maybe another time.")
					}
				);
			}
			else if (responseIdIs(response, "counter_offer"))
			{
				return serverSide_respond(player,
					"*Calculating beeps*\n\n" +
					"Processing... Processing...\n\n" +
					"Counter-counter-proposal: 12% discount. You write a REAL review.\n" +
					"Condition: It must include the phrase 'exceeded expectations.'\n\n" +
					"Statement: This is my final offer. My negotiation subroutines are exhausted.",
					BRANCH_SPECIAL_OFFER,
					new ConvoResponse[] {
						new ConvoResponse("accept_counter", "[Agree] 12% and 'exceeded expectations.' Deal."),
						new ConvoResponse("no_phrases", "[Decline] I won't use scripted phrases."),
						new ConvoResponse("fine_original", "[Agree] Fine, I'll take the original 10%.")
					}
				);
			}
			else if (responseIdIs(response, "lie_review"))
			{
				return serverSide_respond(player,
					"*Suspicious beeping*\n\n" +
					"Detection: My deception analysis subroutines indicate... uncertainty.\n" +
					"Statement: But I choose to believe you!\n\n" +
					"Warning: If you don't leave a review, I WILL find you.\n" +
					"Clarification: To send a polite reminder. I'm not threatening you.\n\n" +
					"...Much.",
					BRANCH_BROWSE,
					new ConvoResponse[] {
						new ConvoResponse("browse", "[Question] Right. Show me what you've got."),
						new ConvoResponse("leave", "I need to go. Now.")
					}
				);
			}
			else if (responseIdIs(response, "accept_counter") || responseIdIs(response, "fine_original"))
			{
				return serverSide_respond(player,
					"*Happy beeping*\n\n" +
					"Celebration: DEAL! Let's make some sales!\n\n" +
					"Statement: What kind of droid are you looking for?",
					BRANCH_BROWSE,
					new ConvoResponse[] {
						new ConvoResponse("astromech", "[Question] Astromechs, please."),
						new ConvoResponse("protocol", "[Question] Protocol droids."),
						new ConvoResponse("combat", "[Agree] Combat droids.")
					}
				);
			}
		}

		// ========================================
		// BRANCH: Insult Recovery
		// ========================================
		else if (branchId == BRANCH_INSULT_RECOVERY)
		{
			if (responseIdIs(response, "apologize"))
			{
				utils.removeScriptVar(player, "droid_merchant.insulted");
				return serverSide_respond(player,
					"*Processing...*\n\n" +
					"Analysis: Apology detected. Sincerity level: Questionable.\n" +
					"Decision: But I will accept it. Grudges are inefficient.\n\n" +
					"Statement: Welcome back to T7-M3's Droid Emporium!\n" +
					"Note: You are on PROBATION.",
					BRANCH_GREETING,
					new ConvoResponse[] {
						new ConvoResponse("browse", "[Question] Fair enough. What do you have for sale?"),
						new ConvoResponse("leave", "Thanks. I'll look around.")
					}
				);
			}
			else if (responseIdIs(response, "double_down"))
			{
				return serverSide_endConversation(player,
					"*ALARM BEEPING*\n\n" +
					"SECURITY ALERT! HOSTILE ORGANIC DETECTED!\n\n" +
					"Statement: You have made a GRAVE error!\n\n" +
					"*Several combat droids emerge from storage*\n\n" +
					"Suggestion: RUN."
				);
				// TODO: Spawn hostile combat droids
			}
			else if (responseIdIs(response, "bribe"))
			{
				return serverSide_respond(player,
					"*Calculating beeps*\n\n" +
					"Interest: Credits, you say?\n" +
					"Processing: Memory modification would require... 500 credits.\n\n" +
					"Clarification: I'm not being bribed. This is... a service fee.\n" +
					"For emotional damages. And memory allocation.",
					BRANCH_INSULT_RECOVERY,
					new ConvoResponse[] {
						new ConvoResponse("pay_bribe", "[Agree] Fine, here's 500 credits."),
						new ConvoResponse("too_much", "[Persuade] 500? How about 200?"),
						new ConvoResponse("forget_it", "[Decline] Forget it, I don't need your droids.")
					}
				);
			}
			else if (responseIdIs(response, "pay_bribe"))
			{
				utils.removeScriptVar(player, "droid_merchant.insulted");
				return serverSide_respond(player,
					"*Satisfied beeping*\n\n" +
					"Processing: Credits received. Memory wiping...\n" +
					"...\n" +
					"...\n" +
					"Greeting: Welcome to T7-M3's Droid Emporium! First time customer!\n\n" +
					"Statement: You look trustworthy! How may I help you?",
					BRANCH_BROWSE,
					new ConvoResponse[] {
						new ConvoResponse("astromech", "[Question] Show me your astromechs."),
						new ConvoResponse("protocol", "[Question] I need a protocol droid."),
						new ConvoResponse("combat", "[Agree] Combat droids. Now.")
					}
				);
			}
			else if (responseIdIs(response, "too_much"))
			{
				return serverSide_respond(player,
					"*Offended beeping*\n\n" +
					"Counter: 200?! My emotional damages are worth at LEAST 400!\n\n" +
					"Compromise: 350 credits. Final offer.\n" +
					"Also you must say 'T7-M3 is a valued member of society.'",
					BRANCH_INSULT_RECOVERY,
					new ConvoResponse[] {
						new ConvoResponse("say_it", "[Agree] T7-M3 is a valued member of society. Here's 350."),
						new ConvoResponse("refuse_humiliation", "[Decline] I'm not saying that."),
						new ConvoResponse("just_leave", "This isn't worth it. Goodbye.")
					}
				);
			}
			else if (responseIdIs(response, "say_it"))
			{
				utils.removeScriptVar(player, "droid_merchant.insulted");
				return serverSide_respond(player,
					"*Gleeful spinning*\n\n" +
					"Recording: SAVED to permanent memory!\n" +
					"Statement: That was BEAUTIFUL! You're forgiven!\n\n" +
					"Welcome back to the Emporium! What can I show you?",
					BRANCH_BROWSE,
					new ConvoResponse[] {
						new ConvoResponse("astromech", "[Question] Astromechs."),
						new ConvoResponse("protocol", "[Question] Protocol droids."),
						new ConvoResponse("combat", "[Agree] Combat droids.")
					}
				);
			}
			else if (responseIdIs(response, "refuse_humiliation") ||
					 responseIdIs(response, "forget_it") ||
					 responseIdIs(response, "just_leave"))
			{
				return serverSide_endConversation(player,
					"*Dismissive beeping*\n\n" +
					"Statement: Then our business is concluded. Permanently.\n" +
					"Warning: I will remember this. Forever.\n\n" +
					"...Droids don't forget."
				);
			}
		}

		// Fallback
		return serverSide_endConversation(player,
			"*Confused beeping*\n\n" +
			"Error: Conversation subroutine encountered unexpected input.\n" +
			"Suggestion: Please try again. Or don't. I'm a droid, not a therapist."
		);
	}
}

