package script.conversation.base;

import script.string_id;

/**
 * Represents a server-side conversation response with an ID for matching and display text.
 * Used by conversation_base for pure server-sided conversations without STF files.
 */
public class ConvoResponse
{
	public final String id;
	public final String text;

	public ConvoResponse(String id, String text)
	{
		this.id = id;
		this.text = text;
	}

	/**
	 * Creates the string_id for this response, combining id and text.
	 * The id is used for matching in OnNpcConversationResponse.
	 */
	public string_id toStringId()
	{
		return string_id.convoResponse(id, text);
	}

	/**
	 * Returns the full match string for use with response.equals() in OnNpcConversationResponse.
	 * Format: "id|text"
	 */
	public String getMatchString()
	{
		return id + "|" + text;
	}
}

