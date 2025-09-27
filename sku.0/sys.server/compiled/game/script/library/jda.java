package script.library;

/*
@Origin: dsrc.script.library
@Author: BubbaJoeX
@Purpose: Java X Discord
@Requirements: <no requirements>
@Notes: Bot library
@Created: Wednesday, 2/5/2025, at 6:11 PM,
@Copyright © SWG: New Beginnings 2025.
Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.sql.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static script.base_class.LOG;

public class jda extends ListenerAdapter
{
    private static final String descriptorFilePath_linux = "/home/swg/swg-main/exe/linux/resource_conversions.tab";
    String[] badColumns = new String[]{"OBJECT_ID", "WAYPOINT_ID", "APPEARANCE_NAME_CRC", "LOCATION_CELL", "LOCATION_SCENE", "COLOR", "ACTIVE"};
    String[] auctionColumns = new String[]{"ITEM_NAME", "BUY_NOW_PRICE"};

    public jda()
    {
    }

    public static void executeBot()
    {
        createBot();
    }

    public static void createBot()
    {
        try
        {
            JDA jda = JDABuilder.createLight("NzA2NDE5Mzg0NjY2Njg1NDUy.GIh-Rh.onSy544jEtK4ar20-KKKCnQBT4DuWynN5YJgKQ", EnumSet.noneOf(GatewayIntent.class))
                    .addEventListeners(new jda())
                    .build();

            // Ensure bot is fully connected before proceeding
            jda.awaitReady(); // This blocks until the bot is fully ready

            CommandListUpdateAction commands = jda.updateCommands();
            commands.addCommands(
                    Commands.slash("say", "Echo a message.")
                            .addOption(OptionType.STRING, "message", "What Message?", true),
                    Commands.slash("query", "Query the Database")
                            .addOption(OptionType.STRING, "expression", "SQL Statement", true),
                    Commands.slash("find", "Character Lookup (Case Sensitive)")
                            .addOption(OptionType.STRING, "player", "A-Z Name", true),
                    Commands.slash("id", "Lists database information on that object.")
                            .addOption(OptionType.STRING, "object", "NetworkID", true),
                    Commands.slash("waypoint", "Find common waypoint names.")
                            .addOption(OptionType.STRING, "name", "Name", true),
                    Commands.slash("market", "Find items listed on the Bazaar.")
                            .addOption(OptionType.STRING, "keyword", "Keyword of item.", true),
                    Commands.slash("dumpresources", "Uploads the Resource Dump from the Database to Pastebin")
                            .addOption(OptionType.BOOLEAN, "option", "Link to pastebin (deprecated)", false),
                    Commands.slash("searchresources", "Find a resource by a given name.")
                            .addOption(OptionType.STRING, "name", "Resource Name", true),
                    Commands.slash("status", "Update the server status.")
                            .addOption(OptionType.STRING, "state", "Message", true),
                    Commands.slash("findcharacter", "Search for characters by partial name.")
                            .addOption(OptionType.STRING, "fragment", "A fragment of the characters name to look up.", true),
                    Commands.slash("contact", "Contact BubbaJoe")
                            .addOption(OptionType.STRING, "message", "Message contents", true),
                    Commands.slash("togglecharacter", "Lock/unlock a character by first name.")
                            .addOption(OptionType.STRING, "character", "The non-capitalized name of who you wish to toggle.", true)
                            .addOption(OptionType.BOOLEAN, "togglevalue", "Lock or unlock (true or false)", true)
            );

            commands.queue();  // Queue the commands
            CompletableFuture<List<Command>> var10000 = commands.submit();
            var10000.thenAccept(result -> System.out.println("Commands loaded successfully"));
            //set status to "Playing: SWG - OR
            jda.getPresence().setActivity(net.dv8tion.jda.api.entities.Activity.customStatus("Monitoring SWG - OR"));
        } catch (Exception e)
        {
            e.printStackTrace();  // Handle any initialization errors
        }
    }


    public static String execQuery(String statement) throws ClassNotFoundException, SQLException
    {

        Connection conn = oracle.connect();
        Statement stmt = conn.createStatement();
        stmt.setMaxRows(12);
        ResultSet rs = stmt.executeQuery(statement);
        StringBuilder result = new StringBuilder();
        int column_count = rs.getMetaData().getColumnCount();

        while (rs.next())
        {
            for (int i = 1; i <= column_count; ++i)
            {
                String column_name = rs.getMetaData().getColumnName(i);
                result.append(column_name).append(":\t").append(rs.getString(i)).append("\n");
            }
        }

        return result.toString();
    }

    public static String execQueryWithDiscrim(String statement, String[] column_excludes) throws ClassNotFoundException, SQLException
    {
        Connection conn = oracle.connect();
        Statement stmt = conn.createStatement();
        stmt.setMaxRows(12);
        ResultSet rs = stmt.executeQuery(statement);
        StringBuilder result = new StringBuilder();
        int column_count = rs.getMetaData().getColumnCount();

        while (rs.next())
        {
            for (int i = 1; i <= column_count; ++i)
            {
                String column_name = rs.getMetaData().getColumnName(i);
                if (!Arrays.asList(column_excludes).contains(column_name))
                {
                    result.append(column_name).append(":\t").append(rs.getString(i)).append("\n");
                }
            }
        }

        return "```" + result + "```";
    }

    public static String execQueryWithBias(String statement, String[] column_includes) throws ClassNotFoundException, SQLException
    {
        Connection conn = oracle.connect();
        Statement stmt = conn.createStatement();
        stmt.setMaxRows(12);
        ResultSet rs = stmt.executeQuery(statement);
        StringBuilder result = new StringBuilder();
        int column_count = rs.getMetaData().getColumnCount();

        while (rs.next())
        {
            for (int i = 1; i <= column_count; ++i)
            {
                String column_name = rs.getMetaData().getColumnName(i);
                if (Arrays.asList(column_includes).contains(column_name))
                {
                    switch (column_name)
                    {
                        case "ITEM_NAME":
                            column_name = "Item Name";
                            break;
                        case "BUY_NOW_PRICE":
                            column_name = "Cost";
                    }

                    result.append(column_name).append(":\t").append(rs.getString(i)).append("\n");
                }
            }
        }

        if (result.toString().isEmpty())
        {
            return "No results found, please broaden your search. Note: search is case sensitive.";
        }
        else if (result.length() > 2000)
        {
            return "Too many results found, please narrow your search.";
        }
        else
        {
            return "```" + result + "```";
        }
    }

    public static String getNumberOfAccounts() throws ClassNotFoundException, SQLException
    {
        return execQuery("SELECT COUNT(*) FROM ACCOUNTS");
    }

    public static String convertResourceText(String text)
    {
        String attb;
        switch (text)
        {
            case "res_cold_resist":
                attb = "CR";
                break;
            case "res_conductivity":
                attb = "CD";
                break;
            case "res_decay_resist":
                attb = "DR";
                break;
            case "res_heat_resist":
                attb = "HR";
                break;
            case "res_malleability":
                attb = "MA";
                break;
            case "res_quality":
                attb = "OQ";
                break;
            case "res_shock_resistance":
                attb = "SR";
                break;
            case "res_toughness":
                attb = "UT";
                break;
            case "res_potential_energy":
                attb = "PE";
                break;
            case "res_flavor":
                attb = "FL";
                break;
            case "res_entangle_resistance":
                attb = "ER";
                break;
            default:
                attb = "UNK";
        }

        return attb;
    }

    public static String getCharacterInfoByName(String name) throws ClassNotFoundException, SQLException
    {
        String query = execQuery("SELECT * FROM SWG_CHARACTERS WHERE CHARACTER_NAME = '" + name + "'");
        String prompt;
        if (query.equals(" "))
        {
            prompt = "Character not found.";
        }
        else
        {
            String title = "Character Info for " + name + "\n";
            prompt = title + query;
        }

        return prompt;
    }

    public static String getObjectInfo(String id) throws ClassNotFoundException, SQLException
    {
        String query = execQuery("SELECT * FROM OBJECTS WHERE OBJECT_ID = '" + id + "'");
        if (query.isEmpty())
        {
            return "Object not found.";
        }
        else
        {
            String title = "Object Information ( " + id + ")\n";
            String[] queryArray = query.split("\n");
            int var5 = queryArray.length;
            byte var6 = 0;
            if (var6 >= var5)
            {
                return "Object not found.";
            }
            else
            {
                String line = queryArray[var6];
                if (line.contains("OBJECT_ID"))
                {
                    line = "";
                }

                if (line.contains("OBJVAR_"))
                {
                    line = "\n";
                }

                if (line.contains("SCRIPT_LIST"))
                {
                    String[] scriptArray = line.split(":");
                    StringBuilder lineBuilder = new StringBuilder(line);
                    String[] var10 = scriptArray;
                    int var11 = scriptArray.length;

                    for (int var12 = 0; var12 < var11; ++var12)
                    {
                        String script = var10[var12];
                        if (script.contains("SCRIPT_LIST"))
                        {
                            String[] scriptCount = script.split(",");
                            lineBuilder.append("SCRIPT_LIST: ").append(scriptCount.length).append(" scripts");
                        }
                    }
                }

                return title + Arrays.stream(queryArray);
            }
        }
    }

    public static String getCharacterInfoById(long id) throws ClassNotFoundException, SQLException
    {
        String query = execQuery("SELECT * FROM SWG_CHARACTERS WHERE STATION_ID = '" + id + "'");
        String prompt;
        if (query.isEmpty())
        {
            prompt = "Character not found.";
        }
        else
        {
            String title = "Character Info for " + id + "\n";
            if (query.isEmpty())
            {
                prompt = "Character not found.";
            }
            else
            {
                prompt = title + query;
            }
        }

        return prompt;
    }

    public static String getCharacterInfoByEnabled(String flag) throws ClassNotFoundException, SQLException
    {
        String query;
        String prompt;
        if (flag.equals("Y"))
        {
            query = execQuery("SELECT COUNT(*) FROM SWG_CHARACTERS WHERE ENABLED = '" + flag + "'");
            prompt = "Enabled characters found: " + query;
            return prompt;
        }
        else
        {
            query = execQuery("SELECT * FROM SWG_CHARACTERS WHERE ENABLED = '" + flag + "'");
            if (query.isEmpty())
            {
                prompt = "No disabled characters found.";
                return prompt;
            }
            else
            {
                String title = "Disabled Characters\n";
                prompt = title + query;
                return prompt;
            }
        }
    }

    public static long getTime()
    {
        return System.currentTimeMillis();
    }

    public static String getResourceAttributes(String name) throws ClassNotFoundException, SQLException
    {
        if (name.length() < 3)
        {
            return "Resource name must be longer than 3 characters.";
        }
        else
        {
            String query = execQuery("SELECT * from RESOURCE_TYPES where RESOURCE_NAME = '" + name + "'");
            StringBuilder prompt = new StringBuilder();
            if (query.isEmpty())
            {
                return "Resource not found.";
            }
            else
            {
                String title = "Statistics for resource " + name + "\n\n";
                String[] temp = query.split("\n");

                for (int i = 0; i < temp.length; ++i)
                {
                    if (temp[i].contains("RESOURCE_ID"))
                    {
                        temp[i] = "";
                    }
                    else if (temp[i].contains("RESOURCE_NAME"))
                    {
                        temp[i] = temp[i].replace("RESOURCE_NAME", "Name");
                    }
                    else
                    {
                        String input;
                        if (temp[i].contains("RESOURCE_CLASS"))
                        {
                            input = temp[i].substring(19);
                            String resourceType = getValueFromTable(input);
                            temp[i] = "\nType: " + resourceType + "\n";
                        }
                        else
                        {
                            String strDate;
                            if (!temp[i].contains("ATTRIBUTES"))
                            {
                                if (temp[i].contains("FRACTAL_SEEDS"))
                                {
                                    temp[i] = "";
                                }

                                if (temp[i].contains("DEPLETED_TIMESTAMP"))
                                {
                                    temp[i] = temp[i].replace("DEPLETED_TIMESTAMP", "\nDepletion Date");
                                    String[] words = temp[i].split(":");
                                    words[1] = words[1].replaceAll("[^0-9]", "");
                                    long depletionTime = words[1].length() > 0 ? Long.parseLong(words[1]) : 0L;
                                    Date date = new Date(Long.parseLong(String.valueOf(depletionTime + getTime())));
                                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                    strDate = formatter.format(date);
                                    temp[i] = temp[i].replace(words[1], strDate + " CST\n");
                                }
                            }
                            else
                            {
                                temp[i] = temp[i].replace("ATTRIBUTES", "\nAttributes");
                                input = "res_decay_resist 499:res_malleability 680:res_quality 945:res_shock_resistance 955:res_toughness 704:";
                                Pattern pattern = Pattern.compile("\\b(\\w+)\\s+(\\d{1,3}|[*])\\b");
                                Matcher matcher = pattern.matcher(input);
                                StringBuilder output = new StringBuilder();

                                while (matcher.find())
                                {
                                    String attributeName = convertResourceText(matcher.group(1));
                                    strDate = matcher.group(2);
                                    output.append(attributeName).append(": ").append(strDate).append("\n");
                                }

                                temp[i] = output.toString();
                            }
                        }
                    }
                }

                String[] var12 = temp;
                int var14 = temp.length;

                for (int var17 = 0; var17 < var14; ++var17)
                {
                    String s = var12[var17];
                    prompt.append(s);
                }

                return title + prompt;
            }
        }
    }

    private static String convertPlanetId(String word)
    {
        int planet_id = Integer.parseInt(word);
        String planet_name;
        switch (planet_id)
        {
            case 0:
                planet_name = "";
                break;
            case 10000006:
                planet_name = "Corellia";
                break;
            case 10000007:
                planet_name = "Dantooine";
                break;
            case 10000008:
                planet_name = "Dathomir";
                break;
            case 10000009:
                planet_name = "Endor";
                break;
            case 10000010:
                planet_name = "Lok";
                break;
            case 10000011:
                planet_name = "Naboo";
                break;
            case 10000012:
                planet_name = "Rori";
                break;
            case 10000013:
                planet_name = "Talus";
                break;
            case 10000014:
                planet_name = "Tatooine";
                break;
            case 10000015:
                planet_name = "Unknown Regions";
                break;
            case 10000016:
                planet_name = "Yavin 4";
                break;
            case 10000031:
            case 10000032:
            case 10000033:
            case 10000034:
            case 10000035:
            case 10000036:
            case 10000037:
                planet_name = "Kashyyyk System";
                break;
            case 10000039:
                planet_name = "Mustafar";
                break;
            default:
                planet_name = "UNKNOWN PLANET";
        }

        return planet_name;
    }

    public static String getValueFromTable(String entry)
    {
        File file = new File(descriptorFilePath_linux);
        String searchString = entry;

        try
        {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line.contains(searchString))
                {
                    Scanner lineScanner = new Scanner(line);
                    lineScanner.useDelimiter("\t");
                    String firstValue = lineScanner.next();
                    String nextValue = lineScanner.next();
                    return nextValue;
                }
            }

            scanner.close();
        } catch (FileNotFoundException var8)
        {
            FileNotFoundException e = var8;
            e.printStackTrace();
        }

        return "ERROR";
    }

    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        String[] id = event.getComponentId().split(":");
        String authorId = id[0];
        String type = id[1];
        if (authorId.equals(event.getUser().getId()))
        {
            event.deferEdit().queue();
            MessageChannel channel = event.getChannel();
            switch (type)
            {
                case "prune":
                    int amount = Integer.parseInt(id[2]);
                    CompletableFuture<List<Message>> futureMessages = event.getChannel().getIterableHistory().skipTo(event.getMessageIdLong()).takeAsync(amount);
                    futureMessages.thenAccept(messages ->
                    {
                        // Ensure that the messages are passed to purgeMessages
                        if (messages != null && !messages.isEmpty())
                        {
                            channel.purgeMessages(messages); // Purge the messages
                        }
                    });
                    break;
                case "delete":
                    event.getHook().deleteOriginal().queue();
                    break;
                default:
                    // Handle any other cases if needed
                    break;
            }
        }
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (event.getGuild() != null)
        {
            switch (event.getName())
            {
                case "say":
                    this.say(event, event.getOption("message").getAsString());
                    LOG("ethereal", "[JDA]: Say command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "query":
                    try
                    {
                        this.query(event);
                    } catch (ClassNotFoundException | SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]: Query command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "dumpresources":
                    try
                    {
                        this.dumpresources(event);
                    } catch (ClassNotFoundException | IOException | TransformerException | ParserConfigurationException | SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]: Dumpresources command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "find":
                    try
                    {
                        this.find(event);
                    } catch (ClassNotFoundException | SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]: Find command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "id":
                    try
                    {
                        this.id(event);
                    } catch (ClassNotFoundException | SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]: Id command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "waypoint":
                    try
                    {
                        this.waypoint(event);
                    } catch (ClassNotFoundException | SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]: Waypoint command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "market":
                    try
                    {
                        this.market(event);
                    } catch (ClassNotFoundException | SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]: Market command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "searchresources":
                    try
                    {
                        this.searchresources(event);
                    } catch (SQLException | ClassNotFoundException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]: Searchresources command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "status":
                    this.status(event);
                    LOG("ethereal", "[JDA]: Status command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "findcharacter":
                    try
                    {
                        this.findcharacter(event);
                    } catch (SQLException | ClassNotFoundException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]:  findcharacter command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "togglecharacter":
                    try
                    {
                        this.handletogglecharacter(event);
                    } catch (SQLException | ClassNotFoundException e)
                    {
                        throw new RuntimeException(e);
                    }
                    LOG("ethereal", "[JDA]: Togglecharacter command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                    break;
                case "contact":
                    this.contact(event);
                    LOG("ethereal", "[JDA]: Contact command executed by " + event.getUser().getName() + " [# " + event.getUser().getId() + " ]");
                default:
                    event.reply("Unable to process instructions at this time.").setEphemeral(false).queue();
            }

        }
    }

    public void handletogglecharacter(SlashCommandInteractionEvent event) throws SQLException, ClassNotFoundException
    {
        if (this.isDeveloperRole(event.getMember()))
        {
            String character = event.getOption("character").getAsString();
            boolean toggle = event.getOption("togglevalue").getAsBoolean();
            if (this.isDangerous(character))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            String query = "UPDATE SWG_CHARACTERS SET ENABLED = '" + toggle + "' WHERE CHARACTER_NAME = '" + character + "'";
            execQueryWithDiscrim(query, this.badColumns);
            String message = toggle ? "Character " + character + " has been unlocked." : "Character " + character + " has been locked.";
            event.reply(message).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").setEphemeral(true).queue();
        }

    }

    private void contact(SlashCommandInteractionEvent event)
    {
        if (this.isDeveloperRole(event.getMember()))
        {
            String message = event.getOption("message").getAsString();
            String content = "Message from: " + event.getUser().getName() + " [" + event.getUser().getId() + "]: \n\n\t" + message;
            event.reply("Message sent.").queue();
            TextChannel channel = event.getGuild().getTextChannelById("1327674541765431408");
            channel.sendMessage(content).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").queue();
        }

    }

    private void findcharacter(SlashCommandInteractionEvent event) throws SQLException, ClassNotFoundException
    {
        if (!this.isVerifiedUserRole(event.getMember()) && this.isDeveloperRole(event.getMember()))
        {
            String fragment = event.getOption("fragment").getAsString();
            if (this.isDangerous(fragment))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            String query = "SELECT * FROM SWG_CHARACTERS WHERE CHARACTER_NAME LIKE '%" + fragment + "%'";
            String statement = execQueryWithDiscrim(query, this.badColumns);
            event.reply(statement).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").queue();
        }

    }

    public void say(SlashCommandInteractionEvent event, String content)
    {
        if (!this.isVerifiedUserRole(event.getMember()) && !this.isDeveloperRole(event.getMember()))
        {
            event.reply("You must have the \"Member\" role to use this command.").queue();
        }
        else
        {
            event.reply(content).queue();
        }

    }

    public void query(SlashCommandInteractionEvent event) throws ClassNotFoundException, SQLException
    {
        if (this.isDeveloperRole(event.getMember()))
        {
            String query = event.getOption("expression").getAsString();
            if (this.isDangerous(query))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            query = execQuery(query);
            event.reply(query).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").queue();
        }

    }

    public void find(SlashCommandInteractionEvent event) throws ClassNotFoundException, SQLException
    {
        if (this.isDeveloperRole(event.getMember()))
        {
            String query = event.getOption("player").getAsString();
            if (this.isDangerous(query))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            String content = getCharacterInfoByName(query);
            event.reply(content).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").queue();
        }

    }

    public void id(SlashCommandInteractionEvent event) throws ClassNotFoundException, SQLException
    {
        if (this.isDeveloperRole(event.getMember()))
        {
            String query = event.getOption("object").getAsString();
            if (this.isDangerous(query))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            String content = getObjectInfo(query);
            event.reply(content).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").queue();
        }

    }

    public void waypoint(SlashCommandInteractionEvent event) throws ClassNotFoundException, SQLException
    {
        if (this.isDeveloperRole(event.getMember()))
        {
            String parameter = event.getOption("name").getAsString();
            if (this.isDangerous(parameter))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            String query = "SELECT * FROM WAYPOINTS WHERE NAME LIKE '%" + parameter + "%'";
            String statement = execQueryWithDiscrim(query, this.badColumns);
            statement = this.processWaypointOutput(statement).toString();
            event.reply(statement).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").queue();
        }

    }

    public List<String> processWaypointOutput(String statement)
    {
        String[] lines = statement.split("\\r?\\n");
        List<String> processedOutput = new ArrayList();

        for (int i = 0; i < lines.length; i += 4)
        {
            StringBuilder groupOutput = new StringBuilder();

            for (int j = i; j < Math.min(i + 4, lines.length); ++j)
            {
                String line = lines[j];
                line = line.replace("LOCATION_X", "X").replace("LOCATION_Y", "Y").replace("LOCATION_Z", "Z").replace("NAME", "Waypoint Name");
                if (!line.contains("X") && !line.contains("Y") && !line.contains("Z"))
                {
                    groupOutput.append(line).append("\n");
                }
                else
                {
                    String[] parts = line.split("\\s+");
                    float value = Float.parseFloat(parts[1]);
                    double roundedValue = (double) Math.round((double) value * 10.0) / 10.0;
                    groupOutput.append(parts[0]).append(" - ").append(roundedValue).append("\n");
                }
            }

            processedOutput.add(groupOutput.toString());
        }

        return processedOutput;
    }

    public void market(SlashCommandInteractionEvent event) throws ClassNotFoundException, SQLException
    {
        if (this.isDeveloperRole(event.getMember()))
        {
            String parameter = event.getOption("keyword").getAsString();
            if (this.isDangerous(parameter))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            String query = "SELECT * FROM MARKET_AUCTIONS WHERE ITEM_NAME LIKE '%" + parameter + "%'";
            String statement = execQueryWithBias(query, this.auctionColumns);
            event.reply(statement).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").queue();
        }

    }

    public void dumpresources(SlashCommandInteractionEvent event) throws ClassNotFoundException, SQLException, ParserConfigurationException, TransformerException, IOException
    {
        if (this.isDeveloperRole(event.getMember()))
        {
            boolean option = event.getOption("option").getAsBoolean();
            if (option)
            {
                String query = "SELECT * FROM resource_types a WHERE a.depleted_timestamp > (SELECT clock.last_save_time FROM clock WHERE last_save_time>0)";
                String statement = execQuery(query);
                File file = new File(descriptorFilePath_linux);
                FileWriter writer = new FileWriter(file);
                writer.write(statement);
                writer.close();
                TextChannel resources = event.getGuild().getTextChannelById("1166122475311075428");
                resources.sendFiles(new FileUpload[]{FileUpload.fromData(file)}).queue((m) ->
                {
                    resources.sendMessage("Here is the resource dump file:").queue();
                    if (file.exists())
                    {
                        file.delete();
                    }

                });
            }
            else
            {
                event.reply("Sorry. beep boop.").setEphemeral(true).queue();
            }
        }
        else
        {
            event.reply("You do not have permission to use this command.").queue();
        }

    }

    private void searchresources(SlashCommandInteractionEvent event) throws SQLException, ClassNotFoundException
    {
        if (!this.isVerifiedUserRole(event.getMember()) && !this.isDeveloperRole(event.getMember()))
        {
            event.reply("You do not have permission to use this command.").setEphemeral(false).queue();
        }
        else
        {
            String name = event.getOption("name").getAsString();
            if (this.isDangerous(name))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            String result = getResourceAttributes(name);
            event.reply(result).setEphemeral(false).queue();
        }

    }

    private void status(SlashCommandInteractionEvent event)
    {
        String message = event.getOption("state").getAsString();
        TextChannel destinationChannel = event.getGuild().getTextChannelById("1046676280902430831");
        if (this.isDeveloperRole(event.getMember()))
        {
            if (this.isDangerous(message))
            {
                event.reply("SQL Injection detected. Request denied.").setEphemeral(true).queue();
                return;
            }

            destinationChannel.sendMessage(message).queue();
        }
        else
        {
            event.reply("You do not have permission to use this command.").setEphemeral(false).queue();
        }

    }

    public boolean isDeveloperRole(Member member)
    {
        if (member.getGuild().getId().equals("900040325811286127"))
        {
            return true;
        }
        else
        {
            List<Role> roles = member.getRoles();
            Iterator var3 = roles.iterator();

            Role role;
            do
            {
                if (!var3.hasNext())
                {
                    return false;
                }

                role = (Role) var3.next();
            } while (!role.getId().equals("1327679321065652295") && !role.getId().equals("1294414289704058934") && !role.getId().equals("1294414289704058934"));

            return true;
        }
    }

    public boolean isVerifiedUserRole(Member member)
    {
        if (member.getGuild().getId().equals("900040325811286127"))
        {
            return true;
        }
        else
        {
            List<Role> roles = member.getRoles();
            Iterator var3 = roles.iterator();

            Role role;
            do
            {
                if (!var3.hasNext())
                {
                    return false;
                }

                role = (Role) var3.next();
            } while (!role.getId().equals("1294414289704058934"));

            return true;
        }
    }

    public boolean isDangerous(String statement)
    {
        return statement.contains("DROP") || statement.contains("DELETE") || statement.contains("TRUNCATE") ||
                statement.startsWith("DROP") || statement.startsWith("DELETE") || statement.startsWith("TRUNCATE") || statement.startsWith(";");
    }


    public void lockCharacter(String character)
    {
        String query = "UPDATE SWG_CHARACTERS SET ENABLED = 'N' WHERE CHARACTER_NAME = '" + character + "'";

        try
        {
            Connection conn = oracle.connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException var5)
        {
            Exception e = var5;
            e.printStackTrace();
        }

    }

    public void unlockCharacter(String character)
    {
        String query = "UPDATE SWG_CHARACTERS SET ENABLED = 'Y' WHERE CHARACTER_NAME = '" + character + "'";

        try
        {
            Connection conn = oracle.connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException var5)
        {
            Exception e = var5;
            e.printStackTrace();
        }

    }
}