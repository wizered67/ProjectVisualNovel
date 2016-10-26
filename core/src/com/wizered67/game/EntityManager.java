package com.wizered67.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.wizered67.game.Entities.Entity;
import com.wizered67.game.Entities.PlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adam on 8/13/2016.
 */
public class EntityManager {
    private static String currentMap;
    private static String nextMap;
    private static HashMap<String, ArrayList<Entity>> allEntities = new HashMap<String, ArrayList<Entity>>();
    private static ArrayList<Entity> removeList = new ArrayList<Entity>();
    private static ArrayList<Entity> addList = new ArrayList<Entity>();
    private static ArrayList<Entity> bodyDisableList = new ArrayList<Entity>();
    private static ArrayList<Entity> bodyEnableList = new ArrayList<Entity>();
    private static HashMap<String, int[][]> staticMap = new HashMap<String, int[][]>();
    private static HashMap<String, Body> allGroundBodies = new HashMap<String, Body>();
    private static PlayerEntity player;

    public static void changeMap(String newMap){
        if (currentMap != null && allEntities.get(currentMap) != null) {
            for (Entity e : allEntities.get(currentMap)) {
                removeEntity(e);
            }
        }
        nextMap = newMap;
        if (allEntities.get(nextMap) == null){
            allEntities.put(nextMap, new ArrayList<Entity>());
        }
    }

    public static void update(float delta){
        if (allEntities.get(currentMap) == null){
            System.out.println("Updating null map");
            return;
        }
        for (Entity e : allEntities.get(currentMap)){
            if (e != null){
                e.update(delta);
            }
        }
    }

    public static void process(){
        for (Entity e : removeList){
            if (!e.getPersistant() || nextMap == currentMap) {
                if (player != e){
                    e.destroy();
                    WorldManager.world.destroyBody(e.getBody());
                    System.out.println("Destroyed body: " + e);
                }
                allEntities.get(currentMap).remove(e);
            }
            else{
                if (e.getBody() != null && e != player && !e.stayActive()){
                    e.getBody().setActive(false);
                }
            }
        }
        removeList.clear();

        if (nextMap != currentMap){
            if (allGroundBodies.get(currentMap) != null)
                allGroundBodies.get(currentMap).setActive(false);
            currentMap = nextMap;
            for (Entity e : allEntities.get(currentMap)){
                if (e.getBody() != null){
                    e.getBody().setActive(true);
                }
            }
            allEntities.get(currentMap).add(player);
            player.changeMap();
        }

        for (Entity e : addList){
            allEntities.get(currentMap).add(e);
        }
        addList.clear();
    }

    public static ArrayList<Entity> getEntities(){
        return allEntities.get(currentMap);
    }

    public static void addEntity(Entity e){
        addEntity(e, nextMap);
    }

    public static void addEntity(Entity e, String map){
        if (allEntities.get(map) == null){
            System.out.println("Entities on null map cannot be added");
            return;
        }
        if (!allEntities.get(map).contains(e)) {
            addList.add(e);
        }
    }

    public static void removeEntity(Entity e){
        if (allEntities.get(currentMap) == null){
            System.out.println("Entities on null map cannot be removed");
            return;
        }

        if (allEntities.get(currentMap).contains(e) && !removeList.contains(e)) {
            removeList.add(e);
        }
    }

    public static void setStaticMap(String mapName, int[][] staticGrid){
        staticMap.put(mapName, staticGrid);
    }

    public static int[][] getStaticMap(String mapName){
        return staticMap.get(mapName);
    }

    public static void setAllGroundBody(String mapName, Body ground){
        allGroundBodies.put(mapName, ground);
    }

    public static Body getAllGroundBody(String mapName){
        return allGroundBodies.get(mapName);
    }

    public static PlayerEntity getPlayer() {
        return player;
    }

    public static void setPlayer(PlayerEntity player) {
        EntityManager.player = player;
    }
}
