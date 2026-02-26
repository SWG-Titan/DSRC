package script;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DiscordWebhook
{
    private final String m_url;
    private final List<EmbedObject> m_embeds = new ArrayList<EmbedObject>();
    private String m_content;
    private String m_username;
    private String m_avatarUrl;
    private boolean m_tts;

    public DiscordWebhook(String url)
    {
        m_url = url;
    }

    public void setContent(String content)
    {
        m_content = content;
    }

    public void setUsername(String username)
    {
        m_username = username;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        m_avatarUrl = avatarUrl;
    }

    public void setTts(boolean tts)
    {
        m_tts = tts;
    }

    public void addEmbed(EmbedObject embed)
    {
        if (embed != null)
            m_embeds.add(embed);
    }

    public void execute()
    {
        if (m_url == null || m_url.length() == 0)
            return;

        if ((m_content == null || m_content.length() == 0) && m_embeds.isEmpty())
            return;

        String payload = buildPayloadJson();
        HttpsURLConnection connection = null;
        OutputStream output = null;
        try
        {
            URL url = new URL(m_url);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Titan-DiscordWebhook");

            output = connection.getOutputStream();
            output.write(payload.getBytes(StandardCharsets.UTF_8));
            output.flush();

            // Force request completion and avoid leaking sockets.
            if (connection.getResponseCode() >= 400)
                connection.getErrorStream();
            else
                connection.getInputStream();
        }
        catch (Exception ignored)
        {
            // Webhook failures should never break gameplay scripts.
        }
        finally
        {
            if (output != null)
            {
                try
                {
                    output.close();
                }
                catch (Exception ignored)
                {
                }
            }

            if (connection != null)
                connection.disconnect();
        }
    }

    private String buildPayloadJson()
    {
        StringBuilder sb = new StringBuilder(256);
        sb.append("{");

        boolean wrote = false;
        wrote = appendJsonString(sb, "content", m_content, wrote);
        wrote = appendJsonString(sb, "username", m_username, wrote);
        wrote = appendJsonString(sb, "avatar_url", m_avatarUrl, wrote);

        if (wrote)
            sb.append(",");
        sb.append("\"tts\":").append(m_tts ? "true" : "false");
        wrote = true;

        if (!m_embeds.isEmpty())
        {
            if (wrote)
                sb.append(",");
            sb.append("\"embeds\":[");
            for (int i = 0; i < m_embeds.size(); ++i)
            {
                if (i > 0)
                    sb.append(",");
                sb.append(m_embeds.get(i).toJson());
            }
            sb.append("]");
        }

        sb.append("}");
        return sb.toString();
    }

    private static boolean appendJsonString(StringBuilder sb, String key, String value, boolean wroteAny)
    {
        if (value == null)
            return wroteAny;

        if (wroteAny)
            sb.append(",");
        sb.append("\"").append(key).append("\":\"").append(escapeJson(value)).append("\"");
        return true;
    }

    private static String escapeJson(String input)
    {
        StringBuilder sb = new StringBuilder(input.length() + 16);
        for (int i = 0; i < input.length(); ++i)
        {
            char c = input.charAt(i);
            switch (c)
            {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20)
                    {
                        sb.append("\\u00");
                        String hex = Integer.toHexString(c & 0xff);
                        if (hex.length() < 2)
                            sb.append("0");
                        sb.append(hex);
                    }
                    else
                    {
                        sb.append(c);
                    }
                    break;
            }
        }
        return sb.toString();
    }

    public static class EmbedObject
    {
        private final List<Field> m_fields = new ArrayList<Field>();
        private String m_title;
        private String m_description;
        private String m_url;
        private Integer m_color;
        private String m_footerText;
        private String m_footerIcon;
        private String m_thumbnailUrl;
        private String m_imageUrl;
        private String m_authorName;
        private String m_authorUrl;
        private String m_authorIcon;

        public EmbedObject setTitle(String title)
        {
            m_title = title;
            return this;
        }

        public EmbedObject setDescription(String description)
        {
            m_description = description;
            return this;
        }

        public EmbedObject setUrl(String url)
        {
            m_url = url;
            return this;
        }

        public EmbedObject setColor(int color)
        {
            m_color = Integer.valueOf(color);
            return this;
        }

        public EmbedObject setFooter(String text, String icon)
        {
            m_footerText = text;
            m_footerIcon = icon;
            return this;
        }

        public EmbedObject setThumbnail(String url)
        {
            m_thumbnailUrl = url;
            return this;
        }

        public EmbedObject setImage(String url)
        {
            m_imageUrl = url;
            return this;
        }

        public EmbedObject setAuthor(String name, String url, String icon)
        {
            m_authorName = name;
            m_authorUrl = url;
            m_authorIcon = icon;
            return this;
        }

        public EmbedObject addField(String name, String value, boolean inline)
        {
            m_fields.add(new Field(name, value, inline));
            return this;
        }

        private String toJson()
        {
            StringBuilder sb = new StringBuilder(256);
            sb.append("{");
            boolean wrote = false;

            wrote = appendJsonString(sb, "title", m_title, wrote);
            wrote = appendJsonString(sb, "description", m_description, wrote);
            wrote = appendJsonString(sb, "url", m_url, wrote);

            if (m_color != null)
            {
                if (wrote)
                    sb.append(",");
                sb.append("\"color\":").append(m_color.intValue());
                wrote = true;
            }

            if (m_footerText != null || m_footerIcon != null)
            {
                if (wrote)
                    sb.append(",");
                sb.append("\"footer\":{");
                boolean wroteFooter = false;
                wroteFooter = appendJsonString(sb, "text", m_footerText, wroteFooter);
                appendJsonString(sb, "icon_url", m_footerIcon, wroteFooter);
                sb.append("}");
                wrote = true;
            }

            if (m_thumbnailUrl != null)
            {
                if (wrote)
                    sb.append(",");
                sb.append("\"thumbnail\":{\"url\":\"").append(escapeJson(m_thumbnailUrl)).append("\"}");
                wrote = true;
            }

            if (m_imageUrl != null)
            {
                if (wrote)
                    sb.append(",");
                sb.append("\"image\":{\"url\":\"").append(escapeJson(m_imageUrl)).append("\"}");
                wrote = true;
            }

            if (m_authorName != null || m_authorUrl != null || m_authorIcon != null)
            {
                if (wrote)
                    sb.append(",");
                sb.append("\"author\":{");
                boolean wroteAuthor = false;
                wroteAuthor = appendJsonString(sb, "name", m_authorName, wroteAuthor);
                wroteAuthor = appendJsonString(sb, "url", m_authorUrl, wroteAuthor);
                appendJsonString(sb, "icon_url", m_authorIcon, wroteAuthor);
                sb.append("}");
                wrote = true;
            }

            if (!m_fields.isEmpty())
            {
                if (wrote)
                    sb.append(",");
                sb.append("\"fields\":[");
                for (int i = 0; i < m_fields.size(); ++i)
                {
                    if (i > 0)
                        sb.append(",");
                    sb.append(m_fields.get(i).toJson());
                }
                sb.append("]");
            }

            sb.append("}");
            return sb.toString();
        }
    }

    private static class Field
    {
        private final String m_name;
        private final String m_value;
        private final boolean m_inline;

        private Field(String name, String value, boolean inline)
        {
            m_name = name;
            m_value = value;
            m_inline = inline;
        }

        private String toJson()
        {
            StringBuilder sb = new StringBuilder(64);
            sb.append("{");
            boolean wrote = false;
            wrote = appendJsonString(sb, "name", m_name, wrote);
            wrote = appendJsonString(sb, "value", m_value, wrote);
            if (wrote)
                sb.append(",");
            sb.append("\"inline\":").append(m_inline ? "true" : "false");
            sb.append("}");
            return sb.toString();
        }
    }
}
