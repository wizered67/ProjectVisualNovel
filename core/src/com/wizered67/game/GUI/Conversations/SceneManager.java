package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
    private Set<CharacterSprite> characterSprites;
    /** Reference to the current ConversationController so that it can be alerted of
     * Animations being completed. */
    private ConversationController conversationController;
    /** SpriteBatch used to draw Sprites for each CharacterSprite. */
    private transient SpriteBatch batch;
    /** Maps character names to their corresponding CharacterSprite. */
    private static HashMap<String, CharacterSprite> allCharacters = new HashMap<String, CharacterSprite>();
    /** Texture to draw as background. */
    private transient Texture background;
    /** Identifier used for background texture. */
    private String backgroundIdentifier;
    /** List of identifiers of CharacterSprites to be removed from the scene at the end of the next update loop. */
    private List<String> removeList;
    private Map<String, Set<SceneImage>> imagesByTexture;
    private Map<String, SceneImage> imagesByInstance;
    private List<SceneImage> sortedImages;
    /** Dummy added at depth 0. When iterating it is skipped and instead characters are drawn at depth 0. */
    private final SceneImage zeroDummy = new SceneImage(0);
    /** No argument constructor. Needed for serialization.*/
    public SceneManager() {
        conversationController = null;
        characterSprites = null;
        batch = new SpriteBatch();
        allCharacters = null;
        background = null;
        removeList = new ArrayList<>();
        imagesByTexture = new HashMap<>();
        sortedImages = new ArrayList<>();
        imagesByInstance = new HashMap<>();
    }
    /** Creates a new SceneManager with ConversationController MW and no CharacterSprites. */
    public SceneManager(ConversationController mw) {
        conversationController = mw;
        characterSprites = new HashSet<>();
        batch = new SpriteBatch();
        backgroundIdentifier = "";
        removeList = new ArrayList<>();
        imagesByTexture = new HashMap<>();
        imagesByInstance = new HashMap<>();
        sortedImages = new ArrayList<>();
        sortedImages.add(zeroDummy);
    }
    /** Called each frame to draw the background, update the Animation of each CharacterSprite, and
     * then draw them. DELTA is the amount of time that has elapsed since the
     * last frame.
     */
    public void update(float delta) {
        if (Gdx.input.justTouched()) {
            SceneImage newImage = new SceneImage("test", "icon", 5);
            newImage.addToScene(this);
            newImage.setFade(2f);
            newImage.setPosition(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            SceneImage newImage = new SceneImage("test2", "icon2", -5);
            newImage.addToScene(this);
            newImage.setFade(1f);
            newImage.setPosition(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
        }
        batch.begin();
        if (background != null) {
            batch.draw(background, 0, 0);
        }

        Iterator<SceneImage> imageIterator = sortedImages.iterator();
        while (imageIterator.hasNext()) {
            SceneImage image = imageIterator.next();
            if (image == zeroDummy) {
                renderCharacters(delta);
            } else if (image.isRemoved()) {
                imageIterator.remove();
            } else {
                image.update(delta);
                image.draw(batch);
            }
        }
        batch.end();
    }
    /** Updates and renders all characters in the scene. */
    public void renderCharacters(float delta) {
        for (CharacterSprite sprite : characterSprites) {
            //System.out.println("Updating " + sprite.getKnownName());
            sprite.updateAnimation(delta);
            sprite.draw(batch);
        }
        for (String character : removeList) {
            characterSprites.remove(character);
        }
    }
   /** Adds the CharacterSprite with identifier IDENTIFIER to this scene. */
    public void addCharacter(String identifier) {
        characterSprites.add(allCharacters.get(identifier.toLowerCase()));
    }
    /** Removes the CharacterSprite with identifier IDENTIFIER from the scene. */
    public void removeCharacter(String identifier) {
        characterSprites.remove(allCharacters.get(identifier.toLowerCase()));
    }
    /** Removes all CharacterSprites from the scene and sets their visibility to false. */
    public void removeAllCharacters() {
        for (CharacterSprite characterSprite : characterSprites) {
            characterSprite.setFullVisible(false);
        }
        characterSprites.clear();
    }
    public static void createCharacter(String identifier, String name, String speakingSound) {
        if (!allCharacters.containsKey(identifier.toLowerCase())) {
            CharacterSprite newCharacter = new CharacterSprite(identifier.toLowerCase(), null, speakingSound);
            newCharacter.setKnownName(name);
            allCharacters.put(identifier.toLowerCase(), newCharacter);
        }
    }
    /** Returns the CharacterSprite with the identifier IDENTIFIER, or
     * outputs an error if no such character is in the scene.
     */
    public CharacterSprite getCharacterByIdentifier(String identifier) {
        CharacterSprite character = allCharacters.get(identifier.toLowerCase());
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

    public void addImage(SceneImage image) {
        Set<SceneImage> imagesOfTexture = imagesByTexture.get(image.getTextureName());
        if (imagesOfTexture == null) {
            imagesOfTexture = new HashSet<>();
            imagesByTexture.put(image.getTextureName(), imagesOfTexture);
        }
        imagesOfTexture.add(image);

        int newIndex = Collections.binarySearch(sortedImages, image);
        if (newIndex < 0) {
            newIndex = -(newIndex + 1);
        }
        sortedImages.add(newIndex, image);
    }

    public void removeImage(SceneImage image) {
        Set<SceneImage> imagesOfTexture = imagesByTexture.get(image.getTextureName());
        if (imagesOfTexture != null) {
            imagesOfTexture.remove(image);
        }
        imagesByInstance.remove(image.getInstanceIdentifier());
    }

    public SceneImage getImage(String instanceIdentifier) {
        return imagesByInstance.get(instanceIdentifier);
    }

    public Set<SceneImage> getImagesByTexture(String textureName) {
        return imagesByTexture.get(textureName);
    }

    /** Passes the complete event to the ConversationController to be passed to the last executed command. */
    public void complete(CompleteEvent event) {
        conversationController.complete(event);
    }
}
