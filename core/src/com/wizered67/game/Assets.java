package com.wizered67.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.wizered67.game.GUI.Conversations.scene.SceneCharacter;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationAssetLoader;
import com.wizered67.game.GUI.Conversations.scene.SceneManager;
import com.wizered67.game.GUI.Conversations.XmlIO.MixedXmlReader;

import static com.badlogic.gdx.utils.XmlReader.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Manages resources by adapting an AssetManager and using identifiers, not filenames, for resources.
 * When created, loads identifier-filename mapping from RESOURCE_XML.
 * @author Adam Victor
 */
public class Assets {
    /** XML Reader used for reading XML but separating text into its own elements. */
    private MixedXmlReader xmlReader;
    /** AssetManager used for loading and getting resources.
     * When its methods are called through this, first map identifier to filename. */
    private AssetManager assetManager;
    /** Mapping between asset identifiers and the filenames they can be accessed with. */
    private Map<String, String> assetIdentifiers;
    /** Map between animation names and the Animation object that corresponds to it. */
    private Map<String, Animation> allAnimations;
    /** The root Element of the loaded XML file. */
    private Element root;
    private final String RESOURCE_XML = "Resources.xml";

    private static final String ANIMATIONS_DIRECTORY = "Animations";
    private static final String BACKGROUNDS_DIRECTORY = "Backgrounds";
    private static final String MUSIC_DIRECTORY = "Music";
    private static final String SOUNDS_DIRECTORY = "Sounds";
    private static final String TEXTURES_DIRECTORY = "Textures";

    private static final String ANIMATION_FILES_TAG = "animation_files";
    private static final String ANIMATIONS_TAG = "animations";
    private static final String BACKGROUNDS_TAG = "backgrounds";
    private static final String MUSIC_TAG = "music";
    private static final String SOUNDS_TAG = "sounds";
    private static final String CHARACTERS_TAG = "characters";
    private static final String TEXTURES_TAG = "textures";

    private static final Pattern RESOURCE_PATTERN = Pattern.compile("\\s*(.+)\\s+(.+)\\s*");
    /** If no AssetManager is specified, make a new one and load to it. */
    public Assets() {
        this(new AssetManager());
    }
    /** Loads all resources, loading them into AssetManager MANAGER. */
    public Assets(AssetManager manager) {
        assetManager = manager;
        assetIdentifiers = new HashMap<>();
        allAnimations = new HashMap<>();
        loadResources();
        assetManager.setLoader(Conversation.class, new ConversationAssetLoader(new InternalFileHandleResolver()));
    }
    /** Returns the Animation with identifier IDENTIFIER. */
    public Animation getAnimation(String identifier) {
        return allAnimations.get(identifier);
    }
    /** Loads all resources, except animations themselves which must be loaded once the animation files finish. */
    public void loadResources() {
        xmlReader = new MixedXmlReader();
        try {
            root = xmlReader.parse(Gdx.files.internal(RESOURCE_XML));
            loadTextures(root);
            loadAnimationFiles(root);
            //loadAnimations(root);
            loadMusic(root);
            loadSounds(root);
            loadCharacters(root);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    /** Adds to assetIdentifiers the mapping between asset identifiers and filenames found
     * in the child of ROOT named TEXTURES_TAG. */
    @SuppressWarnings("unchecked")
    private void loadTextures(Element root) {
        Object[] data = getResources(root, TEXTURES_TAG, TEXTURES_DIRECTORY);
        Map<String, String> resources = (Map<String, String>) data[0];
        assetIdentifiers.putAll(resources);
        List<String> loadNow = (List<String>) data[1];
        for (String id : loadNow) {
            load(id, Texture.class);
        }
    }
    /** Adds to assetIdentifiers the mapping between asset identifiers and filenames found
     * in the child of ROOT named ANIMATION_FILES_TAG. */
    @SuppressWarnings("unchecked")
    private void loadAnimationFiles(Element root) {
        Object[] data = getResources(root, ANIMATION_FILES_TAG, ANIMATIONS_DIRECTORY);
        Map<String, String> resources = (Map<String, String>) data[0];
        assetIdentifiers.putAll(resources);
        List<String> loadNow = (List<String>) data[1];
        for (String id : loadNow) {
            load(id, TextureAtlas.class);
        }
    }

    /** Must be called once animation files are loaded. Goes through each one and creates Animation
     * objects with the data based off of the data from the children of the element named ANIMATIONS_TAG.
     */
    public void loadAnimations() {
        Element anims = root.getChildByName(ANIMATIONS_TAG);
        for (int c = 0; c < anims.getChildCount(); c += 1) {
            Element animationData = anims.getChild(c);
            String atlasAnimation = animationData.getAttribute("id");
            float duration = animationData.getFloatAttribute("frameDuration");
            String mode = animationData.getAttribute("playMode", "NORMAL");
            int index = atlasAnimation.indexOf('_');
            if (index <= 0) {
                GameManager.error("Malformed animation name '" + atlasAnimation + "'.");
            }
            String atlasName = atlasAnimation.substring(0, index);
            String animationName = atlasAnimation.substring(index + 1);
            TextureAtlas atlas = get(atlasName);
            Array<TextureAtlas.AtlasRegion> region = atlas.findRegions(animationName);
            Animation animation = new Animation(duration, region, Animation.PlayMode.valueOf(mode));
            allAnimations.put(atlasAnimation, animation);
        }
    }
    /** Adds to assetIdentifiers the mapping between asset identifiers and filenames found
     * in the child of ROOT named MUSIC_TAG. */
    @SuppressWarnings("unchecked")
    private void loadMusic(Element root) {
        Object[] data = getResources(root, MUSIC_TAG, MUSIC_DIRECTORY);
        Map<String, String> resources = (Map<String, String>) data[0];
        assetIdentifiers.putAll(resources);
        List<String> loadNow = (List<String>) data[1];
        for (String id : loadNow) {
            load(id, Music.class);
        }
    }
    /** Adds to assetIdentifiers the mapping between asset identifiers and filenames found
     * in the child of ROOT named SOUND_TAG. */
    @SuppressWarnings("unchecked")
    private void loadSounds(Element root) {
        Object[] data = getResources(root, SOUNDS_TAG, SOUNDS_DIRECTORY);
        Map<String, String> resources = (Map<String, String>) data[0];
        assetIdentifiers.putAll(resources);
        List<String> loadNow = (List<String>) data[1];
        for (String id : loadNow) {
            load(id, Sound.class);
        }
    }

    /** Loads, creates, and adds to all SceneManager's map of all characters, the characters containing
     * in the child of ROOT named CHARACTERS_TAG. */
    private void loadCharacters(Element root) {
        Element characters = root.getChildByName(CHARACTERS_TAG);
        for (int c = 0; c < characters.getChildCount(); c += 1) {
            Element character = characters.getChild(c);
            String id = character.getAttribute("id");
            String name = character.getAttribute("name");
            String sound = character.getAttribute("sound", SceneCharacter.DEFAULT_SPEAKING_SOUND);
            SceneManager.createCharacter(id, name, sound);
        }
    }

    /** Get all resources from the XMLElement ROOT's child with the name TYPE. Looks
     * for the resources in the directory DIRECTORY. Returns an array containing both
     * a map of all identifier, filename pairs found and a list of all resources to load
     * immediately.
     */
    private Object[] getResources(Element root, String type, String directory) {
        Map<String, String> resources = new HashMap<>();
        List<String> loadList = new ArrayList<>();
        Element files = root.getChildByName(type);
        for (int i = 0; i < files.getChildCount(); i += 1) {
            Element child = files.getChild(i);
            if (child.getName().equals("text")) {
                String text = child.getText();
                text = text.replaceAll("\\r", "");
                String[] lines = text.split("\\n");
                for (String line : lines) {
                    boolean loadNow = false;
                    line = line.trim();
                    Matcher matcher = RESOURCE_PATTERN.matcher(line);
                    String identifier, filename;
                    if (matcher.matches()) {
                        identifier = matcher.group(1);
                        if (identifier.startsWith("!")) {
                            identifier = identifier.substring(1);
                            loadNow = true;
                        }
                        filename = matcher.group(2);
                    } else {
                        if (line.startsWith("!")) {
                            line = line.substring(1);
                            loadNow = true;
                        }
                        identifier = line;
                        filename = line;
                    }
                    if (resources.containsKey(identifier)) {
                        identifierError(identifier);
                    }
                    String fullPath = directory + "/" + filename;
                    if (!Gdx.files.internal(fullPath).exists()) {
                        GameManager.error("No such file '" + fullPath + "'.");
                        continue;
                    }
                    if (loadNow) {
                        loadList.add(identifier);
                    }
                    //assetIdentifiers.put(identifier, directory + "/" + filename);
                    resources.put(identifier, fullPath);
                }
            }
        }
        return new Object[] { resources, loadList };
    }
    /** Display an error if the identifier is already in use. */
    private static void identifierError(String id) {
        GameManager.error("Identifier '" + id + "' is already in use.");
    }

    /** Tells the AssetManager to load the file of type TYPE with filename FILENAME. In effect, calls the
     * regular AssetManager load method without first mapping an identifier to a filename.
     */
    public synchronized <T> void loadRaw(String fileName, Class<T> type) {
        assetManager.load(fileName, type);
    }
    /** Tells the AssetManager to get the file with filename FILENAME. In effect, calls the
     * regular AssetManager get method without first mapping an identifier to a filename.
     */
    public synchronized <T> T getRaw(String fileName) {
        return assetManager.get(fileName);
    }


    /** Below are the regular methods of an AssetManager, except many have been changed so that they can be called with
     * a resource identifier, which is then mapped to the actual filename.
     */

    public synchronized <T> T get(String fileName) {
        return assetManager.get(assetIdentifiers.get(fileName));
    }

    public synchronized <T> T get(String fileName, Class<T> type) {
        return assetManager.get(assetIdentifiers.get(fileName), type);
    }


    public synchronized void unload(String fileName) {
        assetManager.unload(assetIdentifiers.get(fileName));
    }


    public synchronized boolean isLoaded(String fileName) {
        return assetManager.isLoaded(assetIdentifiers.get(fileName));
    }


    public synchronized boolean isLoaded(String fileName, Class type) {
        return assetManager.isLoaded(assetIdentifiers.get(fileName), type);
    }


    public synchronized <T> void load(String fileName, Class<T> type) {
        assetManager.load(assetIdentifiers.get(fileName), type);
    }

    public synchronized <T> void load(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
        assetManager.load(assetIdentifiers.get(fileName), type, parameter);
    }


    public void finishLoadingAsset(String fileName) {
        assetManager.finishLoadingAsset(assetIdentifiers.get(fileName));
    }


    public synchronized Class getAssetType(String fileName) {
        return assetManager.getAssetType(assetIdentifiers.get(fileName));
    }


    public void dispose() {
        assetManager.dispose();
    }

    public FileHandleResolver getFileHandleResolver() {
        return assetManager.getFileHandleResolver();
    }

    public <T> Array<T> getAll(Class<T> type, Array<T> out) {
        return assetManager.getAll(type, out);
    }

    public <T> T get(AssetDescriptor<T> assetDescriptor) {
        return assetManager.get(assetDescriptor);
    }

    public <T> boolean containsAsset(T asset) {
        return assetManager.containsAsset(asset);
    }

    public <T> String getAssetFileName(T asset) {
        return assetManager.getAssetFileName(asset);
    }

    public <T> AssetLoader getLoader(Class<T> type) {
        return assetManager.getLoader(type);
    }

    public <T> AssetLoader getLoader(Class<T> type, String fileName) {
        return assetManager.getLoader(type, fileName);
    }

    public void load(AssetDescriptor desc) {
        assetManager.load(desc);
    }

    public boolean update() {
        return assetManager.update();
    }

    public boolean update(int millis) {
        return assetManager.update(millis);
    }

    public void finishLoading() {
        assetManager.finishLoading();
    }

    public <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, AssetLoader<T, P> loader) {
        assetManager.setLoader(type, loader);
    }

    public <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, String suffix, AssetLoader<T, P> loader) {
        assetManager.setLoader(type, suffix, loader);
    }

    public int getLoadedAssets() {
        return assetManager.getLoadedAssets();
    }

    public int getQueuedAssets() {
        return assetManager.getQueuedAssets();
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public void setErrorListener(AssetErrorListener listener) {
        assetManager.setErrorListener(listener);
    }

    public void clear() {
        assetManager.clear();
    }

    public Logger getLogger() {
        return assetManager.getLogger();
    }

    public void setLogger(Logger logger) {
        assetManager.setLogger(logger);
    }

    public int getReferenceCount(String fileName) {
        return assetManager.getReferenceCount(fileName);
    }

    public void setReferenceCount(String fileName, int refCount) {
        assetManager.setReferenceCount(fileName, refCount);
    }

    public String getDiagnostics() {
        return assetManager.getDiagnostics();
    }

    public Array<String> getAssetNames() {
        return assetManager.getAssetNames();
    }

    public Array<String> getDependencies(String fileName) {
        return assetManager.getDependencies(assetIdentifiers.get(fileName));
    }

}
