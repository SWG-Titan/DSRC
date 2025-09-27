package script.content.fun;

import script.library.create;
import script.location;
import script.obj_id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class maze_maker extends script.base_script
{
    private static final int WALL_SIZE = 16;
    private static final String WALL_TEMPLATE = "object/static/structure/corellia/corl_imprv_wall_4x16_s04.iff";
    private static final String MAZE_BOARDER_TEMPLATE = "object/static/structure/corellia/corl_imprv_wall_4x16_s04.iff";
    private static final String PILLAR_TEMPLATE = "object/static/structure/corellia/corl_imprv_column_s01.iff";
    private static final String ENTER_EXIT_TEMPLATE = "object/static/structure/corellia/corl_imprv_arch_lg_s02.iff";
    private static final int ENTER_EXIT_TEMPLATE_WIDTH = 8;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    private static final int[][] DIRECTIONS = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
    private static final boolean[][] visited = new boolean[WIDTH][HEIGHT];

    public int OnAttach(obj_id self) throws InterruptedException
    {
        generateMaze(self, 0, 0, getLocation(self));
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        generateMaze(self, 0, 0, getLocation(self));
        return SCRIPT_CONTINUE;
    }

    private void generateMaze(obj_id self, int x, int y, location origin) throws InterruptedException
    {

        createBorder(self, origin);


        visited[x][y] = true;
        List<int[]> dirs = Arrays.asList(DIRECTIONS);
        Collections.shuffle(dirs);

        for (int[] dir : dirs)
        {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (isValid(nx, ny))
            {
                placeWallBetween(self, x, y, nx, ny, origin);
                generateMaze(self, nx, ny, origin);
            }
        }


        createEntranceExit(self, origin);
    }

    private boolean isValid(int x, int y)
    {
        return x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT && !visited[x][y];
    }

    private void placeWallBetween(obj_id self, int x1, int y1, int x2, int y2, location origin) throws InterruptedException
    {
        float wx = origin.x + (float) ((x1 + x2) * WALL_SIZE) / 2;
        float wy = origin.y;
        float wz = origin.z + (float) ((y1 + y2) * WALL_SIZE) / 2;
        float rotation = (x1 == x2) ? 0f : 90f;
        location wallLoc = new location(wx, wy, wz, getLocation(self).area);
        obj_id wall = create.object(WALL_TEMPLATE, wallLoc);
        setObjVar(wall, "maze_clean_tag", 1);
        setYaw(wall, rotation);

        createPillar(self, x1, y1, origin);
        createPillar(self, x2, y2, origin);
    }

    private void createPillar(obj_id self, int x, int y, location origin) throws InterruptedException
    {
        float px = origin.x + x * WALL_SIZE;
        float py = origin.y;
        float pz = origin.z + y * WALL_SIZE;
        obj_id pillar = create.object(PILLAR_TEMPLATE, new location(px, py, pz, getLocation(self).area));
        setObjVar(pillar, "maze_clean_tag", 1);
    }

    private void createBorder(obj_id self, location origin) throws InterruptedException
    {

        for (int i = 0; i < WIDTH; i++)
        {

            placeWallBetween(self, i, 0, i + 1, 0, origin);

            placeWallBetween(self, i, HEIGHT, i + 1, HEIGHT, origin);
        }


        for (int i = 0; i < HEIGHT; i++)
        {

            placeWallBetween(self, 0, i, 0, i + 1, origin);

            placeWallBetween(self, WIDTH, i, WIDTH, i + 1, origin);
        }


        placeDoors(self, origin);
    }

    private void placeDoors(obj_id self, location origin) throws InterruptedException
    {
        int entranceSide = (int) (Math.random() * 4);
        int exitSide;

        do
        {
            exitSide = (int) (Math.random() * 4);
        } while (entranceSide == exitSide);


        if (entranceSide == 0)
        {
            placeEntranceExit(self, origin, 0, 0, true);
        }
        else if (entranceSide == 1)
        {
            placeEntranceExit(self, origin, WIDTH, 0, true);
        }
        else if (entranceSide == 2)
        {
            placeEntranceExit(self, origin, 0, HEIGHT, true);
        }
        else
        {
            placeEntranceExit(self, origin, 0, 0, true);
        }


        if (exitSide == 0)
        {
            placeEntranceExit(self, origin, WIDTH, 0, false);
        }
        else if (exitSide == 1)
        {
            placeEntranceExit(self, origin, WIDTH, HEIGHT, false);
        }
        else if (exitSide == 2)
        {
            placeEntranceExit(self, origin, 0, HEIGHT, false);
        }
        else
        {
            placeEntranceExit(self, origin, 0, 0, false);
        }
    }


    private void placeEntranceExit(obj_id self, location origin, int x, int y, boolean isEntrance) throws InterruptedException
    {
        float wx = origin.x + x * WALL_SIZE;
        float wy = origin.y;
        float wz = origin.z + y * WALL_SIZE;
        location wallLoc = new location(wx, wy, wz, getLocation(self).area);


        float rotation = (x == 0 || x == WIDTH) ? 90f : 0f;
        obj_id door = create.object(ENTER_EXIT_TEMPLATE, wallLoc);
        setYaw(door, rotation);
        setObjVar(door, "maze_clean_tag", 1);


        if (isEntrance)
        {
            debugSpeakMsg(self, "Entrance placed at: " + x + ", " + y);
        }
        else
        {
            debugSpeakMsg(self, "Exit placed at: " + x + ", " + y);
        }


        createPillar(self, x - 1, y, origin);
        createPillar(self, x + 1, y, origin);
        if (rotation == 90f)
        {
            createPillar(self, x, y - 1, origin);
            createPillar(self, x, y + 1, origin);
        }
        else
        {
            createPillar(self, x - 1, y, origin);
            createPillar(self, x + 1, y, origin);
        }
    }


    private void createEntranceExit(obj_id self, location origin) throws InterruptedException
    {

        List<int[]> borderLocations = new ArrayList<>();


        for (int i = 1; i < WIDTH - 1; i++)
        {
            borderLocations.add(new int[]{i, 0});
            borderLocations.add(new int[]{i, HEIGHT - 1});
        }


        for (int i = 1; i < HEIGHT - 1; i++)
        {
            borderLocations.add(new int[]{0, i});
            borderLocations.add(new int[]{WIDTH - 1, i});
        }


        Collections.shuffle(borderLocations);


        placeEntranceExit(self, borderLocations.get(0)[0], borderLocations.get(0)[1], origin, true);
        placeEntranceExit(self, borderLocations.get(1)[0], borderLocations.get(1)[1], origin, false);
    }

    private void placeEntranceExit(obj_id self, int x, int y, location origin, boolean isEntrance) throws InterruptedException
    {
        float ex = origin.x + x * WALL_SIZE;
        float ey = origin.y;
        float ez = origin.z + y * WALL_SIZE;
        location doorLoc = new location(ex, ey, ez, getLocation(self).area);


        float rotation = 0f;
        if (y == 0 || y == HEIGHT - 1)
        {
            rotation = 90f;
        }


        obj_id door = create.object(ENTER_EXIT_TEMPLATE, doorLoc);
        setYaw(door, rotation);
        setObjVar(door, "maze_clean_tag", 1);

    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (!isGod(speaker))
        {
            return SCRIPT_CONTINUE;
        }
        if (text.equalsIgnoreCase("clear") || text.equalsIgnoreCase("clean"))
        {
            debugSpeakMsg(speaker, "REMOVING MAZE");
            obj_id[] taggedObjects = getAllObjectsWithObjVar(getLocation(self), 1024f, "maze_clean_tag");
            for (obj_id tag : taggedObjects)
            {
                destroyObject(tag);
            }
            return SCRIPT_CONTINUE;
        }
        if (text.equalsIgnoreCase("maze") || text.equalsIgnoreCase("mazeme"))
        {
            generateMaze(self, 0, 0, getLocation(self));
        }
        return SCRIPT_CONTINUE;
    }
}
