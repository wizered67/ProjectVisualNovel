package com.wizered67.game.GUI.Conversations.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wizered67.game.GUI.Conversations.Commands.images.ImageAction;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GameManager;

import java.util.*;

/**
 * Represents a Scene with a set of characters that are updated
 * and drawn each frame. Alerts the ConversationController when
 * an Animation is completed.
 * @author Adam Victor
 */
public class SceneManager {
    /** Set of all CharacterSprites in this scene. */
    private Set<SceneCharacter> sceneCharacters;
    /** Reference to the current ConversationController so that it can be alerted of
     * Animations being completed. */
    private ConversationController conversationController;
    /** SpriteBatch used to draw Sprites for each SceneCharacter. */
    private transient SpriteBatch batch;
    /** Maps character names to their corresponding SceneCharacter. */
    private static HashMap<String, SceneCharacter> allCharacters = new HashMap<String, SceneCharacter>();
    /** Texture to draw as background. */
    private transient Texture background;
    /** Identifier used for background texture. */
    private String backgroundIdentifier;
    /** List of identifiers of CharacterSprites to be removed from the scene at the end of the next update loop. */
    private List<String> removeList;
    private Map<String, Set<SceneImage>> imagesByGroup;
    private Map<String, SceneImage> imagesByInstance;
    private List<SceneEntity> sortedEntities;

    /** No argument constructor. Needed for serialization.*/
    public SceneManager() {
        conversationController = null;
        sceneCharacters = null;
        batch = new SpriteBatch();
        allCharacters = null;
        background = null;
        removeList = new ArrayList<>();
        imagesByGroup = new HashMap<>();
        sortedEntities = new ArrayList<>();
        imagesByInstance = new HashMap<>();
    }
    /** Creates a new SceneManager with ConversationController MW and no CharacterSprites. */
    public SceneManager(ConversationController mw) {
        conversationController = mw;
        sceneCharacters = new HashSet<>();
        batch = new SpriteBatch();
        backgroundIdentifier = "";
        removeList = new ArrayList<>();
        imagesByGroup = new HashMap<>();
        imagesByInstance = new HashMap<>();
        sortedEntities = new ArrayList<>();
    }
    /** Called each frame to draw the background, update the Animation of each SceneCharacter, and
     * then draw them. DELTA is the amount of time that has elapsed since the
     * last frame.
     */
    public void update(float delta) {
        /*
        if (Gdx.input.justTouched()) {
            SceneImage newImage = new SceneImage("test", "icons", "icon", 5);
            newImage.addToScene(this);
            newImage.setFade(2f);
            newImage.setPosition(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            SceneImage newImage = new SceneImage("test2", "icons", "icon2", -5);
            newImage.addToScene(this);
            newImage.setFade(1f);
            newImage.setPosition(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
        }
        */
        batch.begin();
        if (background != null) {
            batch.draw(background, 0, 0);
        }

        Iterator<SceneEntity> entityIterator = sortedEntities.iterator();
        while (entityIterator.hasNext()) {
            SceneEntity entity = entityIterator.next();
            if (entity.isRemoved()) {
                entityIterator.remove();
            } else {
                entity.update(delta);
                entity.draw(batch);
            }
        }
        batch.end();
    }

   /** Adds the SceneCharacter with identifier IDENTIFIER to this scene. */
    public void addCharacter(String identifier) {
        sceneCharacters.add(allCharacters.get(identifier.toLowerCase()));
    }
    /** Removes the SceneCharacter with identifier IDENTIFIER from the scene. */
    public void removeCharacter(String identifier) {
        sceneCharacters.remove(allCharacters.get(identifier.toLowerCase()));
    }
    /** Removes all CharacterSprites from the scene and sets their visibility to false. */ //todo fix???
    public void removeAllCharacters() {
        for (SceneCharacter sceneCharacter : sceneCharacters) {
            sceneCharacter.finishVisibility(false);
        }
        sceneCharacters.clear();
    }
    public static void createCharacter(String identifier, String name, String speakingSound) {
        if (!allCharacters.containsKey(identifier.toLowerCase())) {
            SceneCharacter newCharacter = new SceneCharacter(identifier.toLowerCase(), null, speakingSound);
            newCharacter.setKnownName(name);
            allCharacters.put(identifier.toLowerCase(), newCharacter);
        }
    }
    /** Returns the SceneCharacter with the identifier IDENTIFIER, or
     * outputs an error if no such character is in the scene.
     */
    public SceneCharacter getCharacterByIdentifier(String identifier) {
        SceneCharacter character = allCharacters.get(identifier.toLowerCase());
        if (character == null) {
            GameManager.error("No character of name " + identifier.toLowerCase());
        }
        return character;
    }
    /** Sets the background to the Texture with identifier IMAGEIDENTIFIER. */
    public void setBackground(String imageIdentifier) {
        if (!backgroundIdentifier.equals(imageIdentifier)) {
            backgroundIdentifier = imageIdentifier;
            if (!GameManager.assetManager().isLoaded(imageIdentifier)) {
                GameManager.error("Background with filename " + imageIdentifier + " is not loaded.");
            }
            background = GameManager.assetManager().get(imageIdentifier);
        }
    }
    /** Returns the identifier of the current background. */
    public String getBackgroundIdentifier() {
        return backgroundIdentifier;
    }

    public boolean applyImageCommand(String instance, String group, ImageAction action) {
        if (instance.isEmpty() && !group.isEmpty()) {
            Set<SceneImage> images = getImagesByGroup(group);
            if (images == null || images.size() == 0) {
                return false;
            }
            for (SceneImage image : images) {
                action.apply(image);
            }
            return true;
        } else {
            SceneImage image = getImage(instance);
            if (image != null) {
                action.apply(image);
                return true;
            }
            return false;
        }
    }

    public void addImage(SceneImage image) {
        addImageToGroup(image, image.getGroup());
        imagesByInstance.put(image.getInstanceIdentifier(), image);
    }

    public void addToSorted(SceneEntity entity) {
        int newIndex = Collections.binarySearch(sortedEntities, entity);
        if (newIndex < 0) {
            newIndex = -(newIndex + 1);
        }
        sortedEntities.add(newIndex, entity);
    }

    public void removeFromSorted(SceneEntity entity) {
        int oldIndex = Collections.binarySearch(sortedEntities, entity);
        if (oldIndex < 0) {
            return;
        }
        Iterator<SceneEntity> iter = sortedEntities.listIterator(oldIndex);
        while (iter.hasNext()) {
            SceneEntity e = iter.next();
            if (e.equals(entity)) {
                iter.remove();
                return;
            }
        }
    }

    public void removeImage(SceneImage image) {
        removeImageFromGroup(image, image.getGroup());
        imagesByInstance.remove(image.getInstanceIdentifier());
    }

    public void changeImageGroup(SceneImage image, String oldGroup) {
        removeImageFromGroup(image, oldGroup);
        addImageToGroup(image, image.getGroup());
    }

    public void addImageToGroup(SceneImage image, String group) {
        if (group != null && !group.isEmpty()) {
            Set<SceneImage> imagesOfGroup = imagesByGroup.get(group);
            if (imagesOfGroup == null) {
                imagesOfGroup = new HashSet<>();
                imagesByGroup.put(group, imagesOfGroup);
            }
            imagesOfGroup.add(image);
        }
    }

    public void removeImageFromGroup(SceneImage image, String group) {
        if (group != null && !group.isEmpty()) {
            Set<SceneImage> imagesOfGroup = imagesByGroup.get(group);
            if (imagesOfGroup != null) {
                imagesOfGroup.remove(image);
            }
        }
    }

    public SceneImage getImage(String instanceIdentifier) {
        return imagesByInstance.get(instanceIdentifier);
    }

    public Set<SceneImage> getImagesByGroup(String group) {
        return imagesByGroup.get(group);
    }

    /** Passes the complete event to the ConversationController to be passed to the last executed command. */
    public void complete(CompleteEvent event) {
        conversationController.complete(event);
    }
}
