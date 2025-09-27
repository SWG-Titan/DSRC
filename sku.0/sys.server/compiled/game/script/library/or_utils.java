package script.library;/*
@Origin: dsrc.script.library
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 8/6/2024, at 7:51 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.location;
import script.obj_id;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class or_utils extends script.base_script
{
    public static void swapLocation(obj_id a, obj_id b)
    {
        location temp = getLocation(a);
        setLocation(a, getLocation(b));
        setLocation(b, temp);
    }

    public static int waveFrom(location origin, float heading, float radius, int points, String templateToSpawn) throws InterruptedException
    {
        float angle = (float) 360 / points;
        for (int i = 0; i < points; i++)
        {
            location spawnLocation = new location(origin.x + radius * (float) Math.cos(Math.toRadians(heading + angle * i)), origin.y + radius * (float) Math.sin(Math.toRadians(heading + angle * i)), origin.z, origin.area);
            obj_id spawnedObject = create.object(templateToSpawn, spawnLocation);
            if (!isIdValid(spawnedObject))
            {
                return -1;
            }
        }
        return points;
    }

    public static boolean compileTable(String strFileName)
    {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "/home/swg/swg-main/exe/linux/bin/DataTableTool",
                "-i", "/home/swg/swg-main/dsrc/sku.0/sys.server/compiled/game/" + strFileName + ".tab"
        );

        processBuilder.redirectErrorStream(true);

        try
        {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (line.contains("success"))
                    {
                        return true;
                    }
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static void reloadTable(obj_id admin, String strFileName, boolean compile)
    {
        if (compile)
        {
            if (compileTable(strFileName))
            {
                broadcast(admin, "Table compilation successful. Reloading...");
                if (sendConsoleCommand("/server reloadTable " + strFileName + ".iff", admin))
                {
                    broadcast(admin, "Table reloaded successfully.");
                }
                else
                {
                    broadcast(admin, "Table reload failed.");
                }
            }
            else
            {
                broadcast(admin, "Table compilation failed, not reloading.");
            }
        }
        else
        {
            if (sendConsoleCommand("/server reloadTable " + strFileName + ".iff", admin))
            {
                broadcast(admin, "Table reloaded successfully.");
            }
            else
            {
                broadcast(admin, "Table reload failed.");
            }
        }
    }

    public static void waypointCreate(obj_id owner, location where, String name, String color)
    {
        obj_id waypoint = createWaypointInDatapad(owner, where);
        setWaypointActive(waypoint, true);
        setWaypointName(waypoint, name);
        setWaypointColor(waypoint, color);
        setWaypointLocation(waypoint, where);
        setWaypointVisible(waypoint, true);
    }

    public static void mailCreate(obj_id source, obj_id target, String subject, String body, boolean hideGM)
    {
        if (hideGM)
        {
            chatSendPersistentMessage("system", target, subject, body, null);
        }
        else
        {
            chatSendPersistentMessage(getPlayerFullName(source), target, subject, body, null);
        }
    }

    public static void mailCreateAttachment(obj_id source)
    {
        //copy waypoint logic to here.
    }

    public static void markAsEventSpawn(obj_id creature)
    {
        setObjVar(creature, "eventSpawn", true);
    }

    public obj_id[] makeSortedObjectArray(obj_id[] source)
    {
        Set<obj_id> set = new HashSet<>(Arrays.asList(source));
        obj_id[] sortedArray = set.toArray(new obj_id[0]);
        Arrays.sort(sortedArray);
        return sortedArray;
    }

    public obj_id[] getAllPlayersWithQuest(String questFile, location where, float range) throws InterruptedException
    {
        obj_id[] samplePool = getAllPlayers(where, range);
        Set<obj_id> set = new HashSet<>(Arrays.asList(samplePool));
        if (questFile == null)
        {
            return null;
        }
        for (obj_id i : samplePool)
        {
            if (!groundquests.isQuestActive(i, questFile))
            {
                set.remove(i);
            }
        }
        return set.toArray(new obj_id[0]);
    }

    public obj_id[] getAllPlayersWithQuestTask(String questFile, String questTask, location where, float range) throws InterruptedException
    {
        obj_id[] samplePool = getAllPlayers(where, range);
        Set<obj_id> set = new HashSet<>(Arrays.asList(samplePool));
        if (questFile == null)
        {
            return null;
        }
        if (questTask == null)
        {
            return null;
        }
        for (obj_id i : samplePool)
        {
            if (!groundquests.isTaskActive(i, questFile, questTask))
            {
                set.remove(i);
            }
        }
        return set.toArray(new obj_id[0]);
    }



    /*
     * This method checks if a location is within a rectangle.
     * @param point The location to check.
     * @param topLeft The top left corner of the rectangle.
     * @param bottomRight The bottom right corner of the rectangle.
     * @return True if the location is within the rectangle, false otherwise.
     */

    public boolean isInPlayerHousing(obj_id self)
    {
        obj_id topMostContainer = getTopMostContainer(self);
        return hasObjVar(topMostContainer, "player_structure");
    }

    public boolean canPlaceIntoContainer(obj_id containerSource, obj_id containerDestination)
    {
        return getVolumeFree(containerDestination) >= getFilledVolume(containerSource);
    }

    public obj_id getPlayerInventory(obj_id player)
    {
        if (!isIdValid(player))
        {
            return null;
        }
        return getObjectInSlot(player, "inventory");
    }

    public void rotateAroundPoint(obj_id[] targets, location origin, float degrees)
    {
        for (obj_id target : targets)
        {
            location targetLocation = getLocation(target);
            float offsetX = targetLocation.x - origin.x;
            float offsetY = targetLocation.y - origin.y;
            double radians = Math.toRadians(degrees);
            float rotatedX = (float) (offsetX * Math.cos(radians) - offsetY * Math.sin(radians));
            float rotatedY = (float) (offsetX * Math.sin(radians) + offsetY * Math.cos(radians));
            targetLocation.x = origin.x + rotatedX;
            targetLocation.y = origin.y + rotatedY;
            setLocation(target, targetLocation);
        }
    }

    public void runResourceDump()
    {
        try
        {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "SWGAideDump"});
            process.waitFor();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void enhancePlayer(obj_id player) throws InterruptedException
    {
        float buffTime = 3600f;
        buff.applyBuff(player, "me_buff_health_2", buffTime, 245);
        buff.applyBuff(player, "me_buff_action_3", buffTime, 245);
        buff.applyBuff(player, "me_buff_strength_3", buffTime, 75);
        buff.applyBuff(player, "me_buff_agility_3", buffTime, 75);
        buff.applyBuff(player, "me_buff_precision_3", buffTime, 75);
        buff.applyBuff(player, "me_buff_melee_gb_1", buffTime, 10);
        buff.applyBuff(player, "me_buff_ranged_gb_1", buffTime, 5);
    }

    public void enhanceGod(obj_id player) throws InterruptedException
    {
        float buffTime = 3600f;
        buff.applyBuff(player, "me_buff_health_2", buffTime, 9999);
        buff.applyBuff(player, "me_buff_action_3", buffTime, 9999);
        buff.applyBuff(player, "me_buff_strength_3", buffTime, 9999);
        buff.applyBuff(player, "me_buff_agility_3", buffTime, 9999);
        buff.applyBuff(player, "me_buff_precision_3", buffTime, 9999);
        buff.applyBuff(player, "me_buff_melee_gb_1", buffTime, 9999);
        buff.applyBuff(player, "me_buff_ranged_gb_1", buffTime, 9999);
    }

    /*
     *
     * This method swaps the location of two objects.
     * @param a The first object.
     * @param b The second object.
     * @void
     */

    public void createEventTravel(String name, location destinationPoint, int cost, boolean interplanetary, int type)
    {
        String pointName = "Event: " + name;
        if (cost <= 1000)
        {
            cost = 1000;
        }
        LOG("ethereal", "[Event Travel]: Creating travel point [" + pointName + "] at " + destinationPoint.toLogFormat() + " with a cost of " + cost);
        addPlanetTravelPoint(destinationPoint.area, pointName, destinationPoint, cost, interplanetary, type);
    }

    /*
     * Creates a wave of objects from a specific location with heading specified from getYaw, radius of wave, number of points inside the wave and the template to spawn at those points.
     *@param origin The location to spawn the wave from.
     *               heading The heading of the wave.
     *                radius The radius of the wave.
     *                  points The number of points in the wave.
     *                   templateToSpawn The template to spawn at each point.
     *@return The number of points spawned.
     *
     *
     */

    public boolean isInRectangle(location point, location topLeft, location bottomRight)
    {
        return point.x >= topLeft.x && point.x <= bottomRight.x && point.y >= bottomRight.y && point.y <= topLeft.y;
    }

    /*
     * This method creates a slope between two locations to be used for bridges, ramps, etc.
     * @param start The starting location.
     * @param end The ending location.
     * @param slope The slope of the location.
     * @return The location of the slope.
     */

    /*
     * This method checks if a location is within a circle.
     * @param point
     * @param center
     * @param radius
     * @return True if the location is within the circle, false otherwise.
     */
    public boolean isInCircle(location point, location center, float radius)
    {
        return Math.pow(point.x - center.x, 2) + Math.pow(point.y - center.y, 2) <= Math.pow(radius, 2);
    }

    /*
     * This method checks if a location is within a triangle.
     * @param point The location to check.
     * @param a The first point of the triangle.
     * @param b The second point of the triangle.
     * @param c The third point of the triangle.
     * @return True if the location is within the triangle, false otherwise.
     */
    public boolean isInTriangle(location point, location a, location b, location c)
    {
        float as_x = point.x - a.x;
        float as_y = point.y - a.y;
        boolean s_ab = (b.x - a.x) * as_y - (b.y - a.y) * as_x > 0;
        if ((c.x - a.x) * as_y - (c.y - a.y) * as_x > 0 == s_ab)
        {
            return false;
        }
        return (c.x - b.x) * (point.y - b.y) - (c.y - b.y) * (point.x - b.x) > 0 == s_ab;
    }

    /*
     * This method returns all players within a circle.
     * @param range The range to check around the circle.
     * @param center The center of the circle.
     * @return An array of players within the circle.
     */
    public obj_id[] getAllPlayersInRectangle(float range, location topLeft, location bottomRight)
    {
        // Correct midpoint calculation
        location midPoint = new location((topLeft.x + bottomRight.x) / 2, (topLeft.y + bottomRight.y) / 2, topLeft.z, topLeft.area);

        range += (float) (Math.sqrt(Math.pow(topLeft.x - bottomRight.x, 2) + Math.pow(topLeft.y - bottomRight.y, 2)) / 2);

        obj_id[] players = getAllPlayers(midPoint, range);
        Set<obj_id> set = new HashSet<>(Arrays.asList(players));

        for (obj_id player : players)
        {
            location playerLocation = getLocation(player);
            if (!isInRectangle(playerLocation, topLeft, bottomRight))
            {
                set.remove(player);
            }
        }

        return set.toArray(new obj_id[0]);
    }

    /*
     * This method returns all players within a triangle.
     * @param range The range to check around the triangle.
     * @param a The first point of the triangle.
     * @param b The second point of the triangle.
     * @param c The third point of the triangle.
     * @return An array of players within the triangle.
     */
    public obj_id[] getAllPlayersInTriangle(float range, location a, location b, location c)
    {
        // Correct midpoint calculation
        location midPoint = new location((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3, a.z, a.area);

        range += (float) (Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2)) / 2);

        obj_id[] players = getAllPlayers(midPoint, range);
        Set<obj_id> set = new HashSet<>(Arrays.asList(players));

        for (obj_id player : players)
        {
            location playerLocation = getLocation(player);
            if (!isInTriangle(playerLocation, a, b, c))
            {
                set.remove(player);
            }
        }

        return set.toArray(new obj_id[0]);
    }

    /*
     * This method checks if a location is populated by players.
     * @param where The location to check.
     * @param range The range to check around the location.
     * @return True if the location is populated with at least 4 people, false otherwise.
     */
    public boolean isPopulated(location where, float range)
    {
        obj_id[] players = getAllPlayers(where, range);
        return players.length > 4;
    }

    /*
     * This method returns the most center location of a specific group of objects.
     * @param petitioner The object (player) requesting the center of mass.
     * @param objects The objects to calculate the center of mass.
     */
    public location getCenterOfMass(obj_id petitioner, obj_id[] objects)
    {
        float x = 0;
        float y = 0;
        float z = 0;

        for (obj_id object : objects)
        {
            location objectLocation = getLocation(object);
            x += objectLocation.x;
            y += objectLocation.y;
            z += objectLocation.z;
        }
        location center = new location(x / objects.length, y / objects.length, z / objects.length, getLocation(objects[0]).area);
        broadcast(petitioner, "Center of mass: " + center.toPrettyPrint());

        return center;
    }

    public location makeSlope(location start, location end, float slope) throws InterruptedException
    {
        float distance = utils.getDistance2D(start, end);
        float height = distance * slope;
        return new location(end.x, end.y, end.z + height, end.area);
    }

}