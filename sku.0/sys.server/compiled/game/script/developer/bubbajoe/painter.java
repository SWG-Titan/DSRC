package script.developer.bubbajoe;

import script.DiscordWebhook;
import script.location;
import script.obj_id;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class painter extends script.base_script
{

    public float PLANETSIDE = 8192f;
    public location ORIGIN = new location(0, 0, 0, getCurrentSceneName(), null);
    public int dotSize = 15;
    public String header = "\n";

    public static boolean imageExists(String imagePath)
    {
        File imageFile = new File(imagePath);
        return imageFile.exists();
    }

    public static void createImage(String imagePath)
    {
        int width = 8192;
        int height = 8192;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        try
        {
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(new Color(0, 0, 0, 0));
            g2d.fillRect(0, 0, width, height);
            g2d.dispose();
            ImageIO.write(image, "png", new File(imagePath));
            System.out.println("Image created successfully.");
        } catch (IOException e)
        {
            System.out.println("Error creating image: " + e.getMessage());
        }
    }

    public int OnAttach(obj_id self)
    {
        broadcast(self, "Heatmap script attached.");
        return SCRIPT_CONTINUE;
    }

    public int plot(obj_id self, String fileName, Color markerColor, obj_id[] targets, String type) throws IOException, InterruptedException
    {
        String fileHome = "/home/swg/swg-main/exe/linux/server/";
        String backgroundImagePath = fileHome + "/planets/" + getCurrentSceneName() + ".png";
        File backgroundFile = new File(backgroundImagePath);

        if (!backgroundFile.exists())
        {
            broadcast(self, "Background image for " + getCurrentSceneName() + " does not exist.");
            return SCRIPT_CONTINUE;
        }

        BufferedImage background = ImageIO.read(backgroundFile);
        BufferedImage plotImage = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = plotImage.createGraphics();
        g2d.drawImage(background, 0, 0, null);
        g2d.setColor(markerColor);

        int imageWidth = 650;
        int imageHeight = 650;

        int paddingX = 50;
        int paddingY = 50;

        float scale = (float) (imageWidth - 2 * paddingX) / 8192;

        for (obj_id target : targets)
        {
            if (!isIdValid(target))
            {
                continue;
            }
            location here = getLocation(target);

            // Calculate image coordinates from world coordinates
            int plotX = Math.round((here.x + 4096f) * scale) + paddingX;
            int plotZ = Math.round((here.z + 4096f) * scale) + paddingY;

            // Draw black border for marker
            g2d.setColor(Color.BLACK);
            g2d.drawOval(plotX - dotSize / 2, plotZ - dotSize / 2, dotSize, dotSize);

            // Draw the marker
            g2d.setColor(markerColor);
            g2d.fillOval(plotX - dotSize / 2, plotZ - dotSize / 2, dotSize, dotSize);
        }

        File newImage = new File(fileHome + fileName);
        g2d.dispose();
        ImageIO.write(plotImage, "png", newImage);

        broadcast(self, "Plotted " + targets.length + " targets to file " + fileName + " on " + getCurrentSceneName() + ".");
        uploadToDiscord(newImage, type);

        return SCRIPT_CONTINUE;
    }


    public int OnSpeaking(obj_id self, String text) throws IOException, InterruptedException
    {
        if (text.equalsIgnoreCase("plotSingle"))
        {
            obj_id[] targets = new obj_id[1];
            targets[0] = getIntendedTarget(self);
            LOG("heatmap", "[Heatmaps]: Generating heatmap of 1 object.");
            plot(self, "single_" + self + "_" + getCurrentSceneName() + ".png", new Color(255, 0, 255, 255), targets, "Single object on " + getCurrentSceneName());
        }
        else if (text.equalsIgnoreCase("plotPlayers"))
        {
            obj_id[] targets = getPlayerCreaturesInRange(ORIGIN, PLANETSIDE);
            if (targets == null || targets.length == 0)
            {
                broadcast(self, "No targets found.");
                return SCRIPT_CONTINUE;
            }
            LOG("heatmap", "[Heatmaps]: Generating heatmap of " + targets.length + " players.");
            plot(self, "players_" + getCurrentSceneName() + ".png", new Color(0, 68, 255, 128), targets, "Players on " + getCurrentSceneName());
        }
        else if (text.equalsIgnoreCase("plotByTarget"))
        {
            String templateName = getTemplateName(getIntendedTarget(self));
            obj_id[] targets = getAllObjectsWithTemplate(ORIGIN, PLANETSIDE, templateName);
            if (targets == null || targets.length == 0)
            {
                broadcast(self, "No objects found for template: " + templateName);
                return SCRIPT_CONTINUE;
            }
            LOG("heatmap", "[Heatmaps]: Generating heatmap of " + targets.length + " objects.");
            plot(self, "target_objects_" + self + "_" + getCurrentSceneName() + ".png", new Color(231, 108, 29, 255), targets, "Objects on " + getCurrentSceneName() + " with template: " + templateName);
        }
        else if (text.equalsIgnoreCase("plotAllHousing"))
        {
            obj_id[] targets = getAllObjectsWithObjVar(ORIGIN, PLANETSIDE, "player_structure");
            if (targets == null || targets.length == 0)
            {
                broadcast(self, "No housing found.");
                return SCRIPT_CONTINUE;
            }
            LOG("heatmap", "[Heatmaps]: Generating heatmap of " + targets.length + " housing objects.");
            plot(self, "housing_" + getCurrentSceneName() + ".png", new Color(0, 255, 0, 128), targets, "Housing on " + getCurrentSceneName());
        }
        else if (text.equalsIgnoreCase("plotAllStructures"))
        {
            obj_id[] targets = getAllObjectsWithObjVar(ORIGIN, PLANETSIDE, "portalProperty.crc");
            if (targets == null || targets.length == 0)
            {
                broadcast(self, "No structures found.");
                return SCRIPT_CONTINUE;
            }
            LOG("heatmap", "[Heatmaps]: Generating heatmap of " + targets.length + " structure objects.");
            plot(self, "structures_" + getCurrentSceneName() + ".png", new Color(44, 4, 98, 128), targets, "Structures on " + getCurrentSceneName());
        }
        else if (text.equalsIgnoreCase("plotAllBuildouts"))
        {
            obj_id[] targets = getAllObjectsWithObjVar(ORIGIN, PLANETSIDE, "buildoutObjectId");
            if (targets == null || targets.length == 0)
            {
                broadcast(self, "No structures found.");
                return SCRIPT_CONTINUE;
            }
            LOG("heatmap", "[Heatmaps]: Generating heatmap of " + targets.length + "'s buildouts.");
            plot(self, "buildout_" + getCurrentSceneName() + ".png", new Color(3, 235, 191, 255), targets, "Buildout map of " + getCurrentSceneName());
        }
        else if (text.equalsIgnoreCase("plotAllCreatures"))
        {
            obj_id[] targets = getCreaturesInRange(ORIGIN, PLANETSIDE);
            if (targets == null || targets.length == 0)
            {
                broadcast(self, "No creatures found.");
                return SCRIPT_CONTINUE;
            }
            LOG("heatmap", "[Heatmaps]: Generating heatmap of " + targets.length + " creatures.");
            plot(self, "creatures_" + getCurrentSceneName() + ".png", new Color(255, 32, 0, 255), targets, "Creatures on " + getCurrentSceneName());
        }
        return SCRIPT_CONTINUE;
    }

    public void uploadToDiscord(File plotImage, String label)
    {
        String webhookUrl = "https://discord.com/api/webhooks/1295091213044809820/U7E-DElCOyPRgzW5UGg4iO4eWr1GfbB8aTYhj1dJvDrQ7rABXwNynjkdT-rC-32LMoha";
        String boundary = "Boundary-" + System.currentTimeMillis();

        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream())
            {
                outputStream.write(("--" + boundary + "\r\n").getBytes());
                outputStream.write("Content-Disposition: form-data; name=\"content\"\r\n\r\n".getBytes());
                outputStream.write((label + "\r\n").getBytes());
                outputStream.write(("--" + boundary + "\r\n").getBytes());
                outputStream.write("Content-Disposition: form-data; name=\"file\"; filename=\"plot.png\"\r\n".getBytes());
                outputStream.write("Content-Type: image/png\r\n\r\n".getBytes());
                Files.copy(plotImage.toPath(), outputStream);
                outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
            {
                broadcast(getSelf(), "Image uploaded to #gm-chat successfully.");
            }
            else
            {
                broadcast(getSelf(), "Failed to upload to Discord. Response code: " + responseCode);
            }

        } catch (IOException e)
        {
            broadcast(getSelf(), "Error uploading to Discord: " + e.getMessage());
        } finally
        {
            if (plotImage.exists())
            {
                boolean deleted = plotImage.delete();
                if (deleted)
                {
                    broadcast(getSelf(), "Temporary plot image deleted successfully.");
                }
                else
                {
                    broadcast(getSelf(), "Failed to delete temporary plot image.");
                }
            }
        }
    }


}
