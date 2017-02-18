package com.wizered67.game.gui.conversations.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wizered67.game.gui.conversations.commands.images.ImageAction;
import com.wizered67.game.gui.conversations.CompleteEvent;
import com.wizered67.game.gui.conversations.ConversationController;
import com.wizered67.game.gui.GUIManager;
import com.wizered67.game.GameManager;

import java.util.*;

/**
 * Represents a Scene with a set of entities, in effect characters and images, that are updated
 * and drawn each frame. Alerts the ConversationController when an animation or fade is completed.
 * @author Adam Victor
 */
public class SceneManager {
    /** Set of all CharacterSprites in this scene. */
    private Set<SceneCharacter> sceneCharacters;
    /** Reference to the current ConversationController so that it can be alerted of
     * Animations being completed. */
    private ConversationController conversationController;
    /** Batch used to draw Sprites for each SceneCharacter. */
    private transient Batch batch;
    /** Texture used for fading screen. */
    private transient Texture fadeTexture;
    /** Maps character names to their corresponding SceneCharacter. */
    private transient static HashMap<String, SceneCharacter> allCharacters = new HashMap<String, SceneCharacter>();
    /** A mapping of image group names to sets of SceneImages in that group. */
    private Map<String, Set<SceneImage>> imagesByGroup;
    /** A mapping of image instance names to the SceneImage with that name. */
    private Map<String, SceneImage> imagesByInstance;
    /** A list of all entities to be drawn. Must always be kept in sorted order! Insertion is done with binary search. */
    private List<SceneEntity> sortedEntities;
    /** The Color to be used for the current fading. */
    private Color fadeColor;
    /** Fade used for keeping track of interpolation type and progress. */
    private Fade fade;

    /** No argument constructor. Needed for serialization.*/
    public SceneManager() {
        batch = GUIManager.getBatch();
        createFadeTexture();
        /*
        conversationController = null;
        sceneCharacters = null;
        batch = new SpriteBatch();
        background = null;
        imagesByGroup = new HashMap<>();
        sortedEntities = new ArrayList<>();
        imagesByInstance = new HashMap<>();
        */
    }

    /** Creates a new SceneManager with ConversationController MW and no CharacterSprites. */
    public SceneManager(ConversationController mw) {
        conversationController = mw;
        sceneCharacters = new HashSet<>();
        batch = new SpriteBatch();
        imagesByGroup = new HashMap<>();
        imagesByInstance = new HashMap<>();
        sortedEntities = new ArrayList<>();
        createFadeTexture();
    }

    /** Returns the Conversation Controller associated with this SceneManager. */
    public ConversationController conversationController() {
        return conversationController;
    }

    /** Creates the Texture used for fading. */
    private void createFadeTexture() {
        fadeTexture = new Texture(1, 1, Pixmap.Format.RGBA8888);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(0, 0, 1, 1);
        fadeTexture.draw(pixmap, 0, 0);
        pixmap.dispose();
    }

    /** Called each frame to draw the background, update the Animation of each SceneCharacter, and
     * then draw them. DELTA is the amount of time that has elapsed since the
     * last frame.
     */
    public void update(float delta) {
        batch.begin();

        Iterator<SceneEntity> entityIterator = sortedEntities.iterator();
        while (entityIterator.hasNext()) {
            SceneEntity entity = entityIterator.next();
            if (entity.isRemoved()) {
                entityIterator.remove();
            } else {
                if (!conversationController.isPaused()) {
                    entity.update(delta);
                }
                entity.draw(batch);
            }
        }
        batch.end();
        GUIManager.updateAndRenderStage(delta);

        batch.begin();
        drawFade(delta);
        batch.end();
    }

    /** Draws the screen fade currently in effect. DELTA TIME is time elapsed since last frame. */
    private void drawFade(float deltaTime) {
        if (fade != null) {
            float alpha;
            if (!conversationController.isPaused()) {
                alpha = fade.update(deltaTime);
            } else {
                alpha = fade.update(0);
            }
            fadeColor.a = alpha;
            batch.setColor(fadeColor);
            batch.draw(fadeTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setColor(Color.WHITE);
            if (alpha <= 0 || alpha >= 1) {
                fade = null;
                complete(CompleteEvent.fade(this, this));
            }
        }
    }
    /** Used to set the SceneManager's current fade. */
    public void setFade(Fade fade, Color color) {
        this.fade = fade;
        fadeColor = color;
    }

    public Map<String, SceneCharacter> allCharacters() {
        return allCharacters;
    }

   /** Adds the SceneCharacter with identifier IDENTIFIER to this scene. */
    public void addCharacter(String identifier) {
        sceneCharacters.add(allCharacters.get(identifier.toLowerCase()));
    }
    /** Removes the SceneCharacter with identifier IDENTIFIER from the scene. */
    public void removeCharacter(String identifier) {
        sceneCharacters.remove(allCharacters.get(identifier.toLowerCase()));
    }
    /** Removes all SceneEntities from the scene and sets their visibility to false. */
    public void removeAllEntities() {
        sceneCharacters.clear();
        imagesByInstance.clear();
        imagesByGroup.clear();
        for (SceneEntity entity : sortedEntities) {
            entity.finishVisibility(false);
        }
        sortedEntities.clear();
    }

    /** Adds to the map of allCharacters a new character with identifier IDENTIFIER,
     * initial name NAME, and speaking sound SPEAKING SOUND.
     */
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

    /** Applies some ImageAction ACTION to either a single SceneImage with id INSTANCE or an entire
     * group with group id GROUP, if INSTANCE is empty. Returns true if the action was successfully
     * applied to at least one image and false otherwise.
     */
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

    /** Adds the SceneImage IMAGE to the scene by adding it to the correct group
     * and mapping its instance identifier to it. It is not, however, added to the sorted list.
     * That is called from within SceneImage once its depth has been set.
     */
    public void addImage(SceneImage image) {
        addImageToGroup(image, image.getGroup());
        imagesByInstance.put(image.getInstanceIdentifier(), image);
    }

    /** Adds the SceneEntity ENTITY to the proper place in the list of sortedEntities.
     * Uses binary search on the list to find the correct index to add it to. Therefore, sortedEntities
     * list must always be kept in sorted order.
     */
    public void addToSorted(SceneEntity entity) {
        int newIndex = Collections.binarySearch(sortedEntities, entity);
        if (newIndex < 0) {
            newIndex = -(newIndex + 1);
        }
        sortedEntities.add(newIndex, entity);
    }

    /** Removes the SceneEntity ENTITY from the sorted list. It uses binary search to locate the
     * index of the first SceneEntity with the same depth as the entity. From there, it iterates through
     * the list until it finds ENTITY. Therefore, removing is not O(logN) like adding, but is instead
     * O(logN + D), where N is the total number of entities and D is the number with the same depth as this
     * entity. The different, however, should hopefully be negligable in practice, as long as the number of entities
     * at one depth is not some absurd amount.
     */
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

    /** Removes the SceneImage IMAGE from the scene by removing it from its group
     * and removing the mapping between its instance identifier and it.
     */
    public void removeImage(SceneImage image) {
        removeImageFromGroup(image, image.getGroup());
        imagesByInstance.remove(image.getInstanceIdentifier());
    }

    /** Changes the image group of SceneImage IMAGE by first removing it from the old group
     * and then adding it to the new one. Because it is necessary to remove from the old,
     * the OLD GROUP must be passed in as an argument.
     */
    public void changeImageGroup(SceneImage image, String oldGroup) {
        removeImageFromGroup(image, oldGroup);
        addImageToGroup(image, image.getGroup());
    }
    /** Adds the SceneImage IMAGE to group GROUP. If the group does not already exist,
     * it first creates it by mapping the group name to an empty set. Afterwards, or if the
     * group does exist, IMAGE is added to the set of images in the group. */
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
    /** Removes the SceneImage IMAGE from the group GROUP. */
    public void removeImageFromGroup(SceneImage image, String group) {
        if (group != null && !group.isEmpty()) {
            Set<SceneImage> imagesOfGroup = imagesByGroup.get(group);
            if (imagesOfGroup != null) {
                imagesOfGroup.remove(image);
            }
        }
    }
    /** Returns the SceneImage with identifier INSTANCE IDENTIFIER. */
    public SceneImage getImage(String instanceIdentifier) {
        return imagesByInstance.get(instanceIdentifier);
    }
    /** Returns the Set of all SceneImages with the group identifier GROUP. */
    public Set<SceneImage> getImagesByGroup(String group) {
        return imagesByGroup.get(group);
    }
    /** Disposes resources that can be freed from memory. */
    public void dispose() {
        fadeTexture.dispose();
    }

    /** Passes the complete event to the ConversationController to be passed to the last executed command. */
    public void complete(CompleteEvent event) {
        conversationController.complete(event);
    }
}
