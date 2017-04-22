package com.wizered67.game.assets;

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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.scene.SceneCharacter;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.conversations.xmlio.ConversationAssetLoader;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.xmlio.MixedXmlReader;
import com.wizered67.game.GameManager;

import static com.badlogic.gdx.utils.XmlReader.Element;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Manages resources by adapting an AssetManager and using identifiers, not filenames, for resources.
 * When created, loads identifier-filename mapping from RESOURCE_XML. Also used for loading animations,
 * asset groups, and characters.
 * @author Adam Victor
 */
public class Assets {
    /** XML Reader used for reading XML but separating text into its own elements. */
    private MixedXmlReader xmlReader;
    /** AssetManager used for loading and getting resources.
     * When its methods are called through this, first map identifier to filename. */
    private AssetManager assetManager;
    /** Mapping between asset identifiers and the filenames they can be accessed with. */
    private Map<String, AssetDescriptor> assetIdentifiers;
    /** Mapping between XML tag and Class of resources in that tag. */
    private Map<String, Class> tagToClass;
    /** Mapping between animation file path and set of AnimationData associated with that atlas. */
    private Map<String, Set<AnimationData>> atlasFileToAnimationData;
    /** Map between animation names and the Animation object that corresponds to it. */
    private Map<String, Animation<TextureRegion>> allAnimations;
    /** Map between names of asset groups and the set of assets they contain.
     * Used to load/unload many resources with one call. */
    private Map<String, Set<AssetDescriptor>> assetGroups;
    /** The root Element of the loaded XML file. */
    private Element root;
    private final String RESOURCE_XML = "Resources.xml";

    private static final String ANIMATIONS_DIRECTORY = "Animations";
    private static final String MUSIC_DIRECTORY = "Music";
    private static final String SOUNDS_DIRECTORY = "Sounds";
    private static final String TEXTURES_DIRECTORY = "Textures";
    private static final String CONVERSATION_DIRECTORY = "Conversations";
    private static final String CONVERSATION_EXTENSION = ".conv";

    private static final String ANIMATION_FILES_TAG = "animation_files";
    private static final String ANIMATIONS_TAG = "animations";
    private static final String MUSIC_TAG = "music";
    private static final String SOUNDS_TAG = "sounds";
    private static final String CHARACTERS_TAG = "characters";
    private static final String TEXTURES_TAG = "textures";
    private static final String GROUPS_TAG = "groups";
    private static final Pattern RESOURCE_PATTERN = Pattern.compile("\\s*(.+)\\s+\"(.+)\"\\s*");
    /** If no AssetManager is specified, make a new one and load to it. */
    public Assets() {
        this(new AssetManager());
    }
    /** Loads all resources, loading them into AssetManager MANAGER. */
    public Assets(AssetManager manager) {
        assetManager = manager;
        assetManager.getLogger().setLevel(Logger.DEBUG);

        assetIdentifiers = new HashMap<>();
        allAnimations = new HashMap<>();
        tagToClass = new HashMap<>();
        assetGroups = new HashMap<>();
        atlasFileToAnimationData = new HashMap<>();
        initTagsToClass();
        initResources();
        assetManager.setLoader(Conversation.class, new ConversationAssetLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(AnimationTextureAtlas.class, new TextureAtlasAnimationLoader(new InternalFileHandleResolver()));
    }
    /** Sets all mappings between tags and classes needed for loading. */
    private void initTagsToClass() {
        tagToClass.put(ANIMATION_FILES_TAG, AnimationTextureAtlas.class);
        tagToClass.put(MUSIC_TAG, Music.class);
        tagToClass.put(SOUNDS_TAG, Sound.class);
        tagToClass.put(TEXTURES_TAG, Texture.class);
    }
    public void loadAnimation(String atlasFilename) {
        Set<AnimationData> allData = atlasFileToAnimationData.get(atlasFilename);
        if (allData == null) {
            return;
        }
        for (AnimationData data : allData) {
            allAnimations.put(data.getAtlasAnimation(), data.createAnimation());
        }
    }
    public void loadAnimation(String atlasFilename, TextureAtlas atlas) {
        Set<AnimationData> allData = atlasFileToAnimationData.get(atlasFilename);
        if (allData == null) {
            return;
        }
        for (AnimationData data : allData) {
            if (allAnimations.get(data.getAtlasAnimation()) == null) {
                allAnimations.put(data.getAtlasAnimation(), data.createAnimation(atlas));
            }
        }
    }
    public void unloadAnimations(String atlasFilename) {
        Set<AnimationData> allData = atlasFileToAnimationData.get(atlasFilename);
        if (allData == null) {
            return;
        }
        for (AnimationData data : allData) {
            allAnimations.put(data.getAtlasAnimation(), null);
        }
    }
    /** Returns the Animation with identifier IDENTIFIER. */
    public Animation<TextureRegion> getAnimation(String identifier) {
        return allAnimations.get(identifier);
    }
    /** Maps all identifiers to Asset Descriptors, loads characters, and creates asset groups. */
    public void initResources() {
        xmlReader = new MixedXmlReader();
        try {
            root = xmlReader.parse(Gdx.files.internal(RESOURCE_XML));
            mapTextures(root);
            mapAnimationFiles(root);
            createAnimationData(root);
            mapMusic(root);
            mapSounds(root);
            loadCharacters(root);
            createGroups(root);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    /** Adds to assetIdentifiers the mapping between asset identifiers and filenames found
     * in the child of ROOT named TEXTURES_TAG. */
    @SuppressWarnings("unchecked")
    private void mapTextures(Element root) {
        Map<String, AssetDescriptor> resources = getResources(root, TEXTURES_TAG, TEXTURES_DIRECTORY);
        assetIdentifiers.putAll(resources);
    }
    /** Adds to assetIdentifiers the mapping between asset identifiers and filenames found
     * in the child of ROOT named ANIMATION_FILES_TAG. */
    @SuppressWarnings("unchecked")
    private void mapAnimationFiles(Element root) {
        Map<String, AssetDescriptor> resources = getResources(root, ANIMATION_FILES_TAG, ANIMATIONS_DIRECTORY);
        assetIdentifiers.putAll(resources);
    }

    /** Must be called once animation files are loaded. Goes through each one and creates Animation
     * objects with the data based off of the data from the children of the element named ANIMATIONS_TAG.
     */
    public void createAnimationData(Element root) {
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
            String atlasFilename = assetIdentifiers.get(atlasName).fileName;
            Set<AnimationData> dataSet = atlasFileToAnimationData.get(atlasFilename);
            if (dataSet == null) {
                dataSet = new HashSet<>();
                atlasFileToAnimationData.put(atlasFilename, dataSet);
            }
            dataSet.add(new AnimationData(atlasName, animationName, duration, Animation.PlayMode.valueOf(mode)));
            //TextureAtlas atlas = get(atlasName);
            //Array<TextureAtlas.AtlasRegion> region = atlas.findRegions(animationName);
            //Animation animation = new Animation(duration, region, Animation.PlayMode.valueOf(mode));
            //allAnimations.put(atlasAnimation, animation);
        }
    }
    /** Adds to assetIdentifiers the mapping between asset identifiers and filenames found
     * in the child of ROOT named MUSIC_TAG. */
    @SuppressWarnings("unchecked")
    private void mapMusic(Element root) {
        Map<String, AssetDescriptor> resources = getResources(root, MUSIC_TAG, MUSIC_DIRECTORY);
        assetIdentifiers.putAll(resources);
    }
    /** Adds to assetIdentifiers the mapping between asset identifiers and filenames found
     * in the child of ROOT named SOUND_TAG. */
    @SuppressWarnings("unchecked")
    private void mapSounds(Element root) {
        Map<String, AssetDescriptor> resources = getResources(root, SOUNDS_TAG, SOUNDS_DIRECTORY);
        assetIdentifiers.putAll(resources);
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

    /** Creates groups by mapping group names to the set of AssetDescriptors of resources in the group. */
    private void createGroups(XmlReader.Element root) {
        XmlReader.Element groups = root.getChildByName(GROUPS_TAG);
        for (int c = 0; c < groups.getChildCount(); c += 1) {
            XmlReader.Element group = groups.getChild(c);
            String groupName = group.getAttribute("name");
            Set<AssetDescriptor> assets = new HashSet<>();
            for (int a = 0; a < group.getChildCount(); a += 1) {
                XmlReader.Element asset = group.getChild(a);
                if (asset.getName().equals("load")) { //todo use constant for name?
                    //has to get text element because using Mixed XML Reader
                    String identifier = asset.getChildByName("text").getText();
                    AssetDescriptor descriptor = assetIdentifiers.get(identifier);
                    if (identifier.endsWith(CONVERSATION_EXTENSION)) {
                        descriptor = new AssetDescriptor(CONVERSATION_DIRECTORY + "/" + identifier, Conversation.class);
                    }

                    if (descriptor == null) {
                        GameManager.error("No such resource with name " + identifier);
                    }
                    assets.add(descriptor);
                }
            }
            assetGroups.put(groupName, assets);
        }
    }

    /** Get all resources from the XMLElement ROOT's child with the name TYPE. Looks
     * for the resources in the directory DIRECTORY. Returns an array containing both
     * a map of all identifier, filename pairs found and a list of all resources to load
     * immediately.
     */
    private Map<String, AssetDescriptor> getResources(Element root, String type, String directory) {
        Map<String, AssetDescriptor> resources = new HashMap<>();
        Element files = root.getChildByName(type);
        for (int i = 0; i < files.getChildCount(); i += 1) {
            Element child = files.getChild(i);
            if (child.getName().equals("text")) {
                String text = child.getText();
                text = text.replaceAll("\\r", "");
                String[] lines = text.split("\\n");
                for (String line : lines) {
                    line = line.trim();
                    Matcher matcher = RESOURCE_PATTERN.matcher(line);
                    String identifier, filename;
                    if (matcher.matches()) {
                        identifier = matcher.group(1);
                        filename = matcher.group(2);
                    } else {
                        line = line.replaceAll("\"", "");
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
                    //assetIdentifiers.put(identifier, directory + "/" + filename);
                    resources.put(identifier, new AssetDescriptor(fullPath, tagToClass.get(type)));
                }
            }
        }
        return resources;
    }
    /** Display an error if the identifier is already in use. */
    private static void identifierError(String id) {
        GameManager.error("Identifier '" + id + "' is already in use.");
    }

    /** Loads all resources in the asset group with name GROUP. */
    public void loadGroup(String groupName) {
        Set<AssetDescriptor> group = assetGroups.get(groupName);
        if (group == null) {
            GameManager.error("No such group with name " + groupName);
            return;
        }
        for (AssetDescriptor descriptor : group) {
            load(descriptor);
        }
    }

    /** Unloads all resources in the asset group with name GROUP. */
    public void unloadGroup(String groupName) {
        Set<AssetDescriptor> group = assetGroups.get(groupName);
        if (group == null) {
            GameManager.error("No such group with name " + groupName);
            return;
        }
        for (AssetDescriptor descriptor : group) {
            unload(descriptor.fileName);
        }
    }

    /** Load and unload Conversations through the asset manager. */
    public synchronized void loadConversation(String conversation) {
        assetManager.load(CONVERSATION_DIRECTORY + "/" + conversation, Conversation.class);
    }

    public synchronized void unloadConversation(String conversation) {
        assetManager.unload(CONVERSATION_DIRECTORY + "/" + conversation);
    }

    public synchronized Conversation getConversation(String conversation) {
        return assetManager.get(CONVERSATION_DIRECTORY + "/" + conversation, Conversation.class);
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

    /** Tells the AssetManager to unload the file with filename FILENAME. In effect, calls the
     * regular AssetManager load method without first mapping an identifier to a filename. */
    public synchronized <T> void unloadRaw(String fileName) {
        assetManager.unload(fileName);
    }

    /** Below are the regular methods of an AssetManager, except many have been changed so that they can be called with
     * a resource identifier, which is then mapped to the actual filename.
     */

    public synchronized <T> T get(String identifier) {
        if (identifier.endsWith(".conv")) {
            return (T) getConversation(identifier);
        }
        return assetManager.get(assetIdentifiers.get(identifier).fileName);
    }

    public synchronized <T> T get(String identifier, Class<T> type) {
        return assetManager.get(assetIdentifiers.get(identifier).fileName, type);
    }


    public synchronized void unload(String identifier) {
        if (identifier.endsWith(".conv")) {
            unloadConversation(identifier);
        }
        assetManager.unload(assetIdentifiers.get(identifier).fileName);
    }


    public synchronized boolean isLoaded(String identifier) {
        return assetManager.isLoaded(assetIdentifiers.get(identifier).fileName);
    }


    public synchronized boolean isLoaded(String identifier, Class type) {
        return assetManager.isLoaded(assetIdentifiers.get(identifier).fileName, type);
    }

    public synchronized void load(String identifier) {
        if (identifier.endsWith(".conv")) {
            loadConversation(identifier);
        }
        assetManager.load(assetIdentifiers.get(identifier));
    }

    public synchronized <T> void load(String identifier, Class<T> type) {
        if (type == Conversation.class) {
            loadConversation(identifier);
        }
        assetManager.load(assetIdentifiers.get(identifier).fileName, type);
    }

    public synchronized <T> void load(String identifier, Class<T> type, AssetLoaderParameters<T> parameter) {
        assetManager.load(assetIdentifiers.get(identifier).fileName, type, parameter);
    }


    public void finishLoadingAsset(String identifier) {
        assetManager.finishLoadingAsset(assetIdentifiers.get(identifier).fileName);
    }


    public synchronized Class getAssetTypeRaw(String filename) {
        return assetManager.getAssetType(filename);
    }

    public synchronized Class getAssetType(String identifier) {
        return assetManager.getAssetType(assetIdentifiers.get(identifier).fileName);
    }


    public synchronized void dispose() {
        assetManager.dispose();
    }

    public FileHandleResolver getFileHandleResolver() {
        return assetManager.getFileHandleResolver();
    }

    public synchronized <T> Array<T> getAll(Class<T> type, Array<T> out) {
        return assetManager.getAll(type, out);
    }

    public synchronized <T> T get(AssetDescriptor<T> assetDescriptor) {
        return assetManager.get(assetDescriptor);
    }

    public synchronized  <T> boolean containsAsset(T asset) {
        return assetManager.containsAsset(asset);
    }

    public synchronized <T> String getAssetFileName(T asset) {
        return assetManager.getAssetFileName(asset);
    }

    public <T> AssetLoader getLoader(Class<T> type) {
        return assetManager.getLoader(type);
    }

    public <T> AssetLoader getLoader(Class<T> type, String fileName) {
        return assetManager.getLoader(type, fileName);
    }

    public synchronized void load(AssetDescriptor desc) {
        assetManager.load(desc);
    }

    public synchronized boolean update() {
        return assetManager.update();
    }

    public boolean update(int millis) {
        return assetManager.update(millis);
    }

    public void finishLoading() {
        assetManager.finishLoading();
    }

    public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, AssetLoader<T, P> loader) {
        assetManager.setLoader(type, loader);
    }

    public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, String suffix, AssetLoader<T, P> loader) {
        assetManager.setLoader(type, suffix, loader);
    }

    public synchronized int getLoadedAssets() {
        return assetManager.getLoadedAssets();
    }

    public synchronized int getQueuedAssets() {
        return assetManager.getQueuedAssets();
    }

    public synchronized float getProgress() {
        return assetManager.getProgress();
    }

    public synchronized void setErrorListener(AssetErrorListener listener) {
        assetManager.setErrorListener(listener);
    }

    public synchronized void clear() {
        assetManager.clear();
    }

    public Logger getLogger() {
        return assetManager.getLogger();
    }

    public void setLogger(Logger logger) {
        assetManager.setLogger(logger);
    }

    public synchronized int getReferenceCount(String fileName) {
        return assetManager.getReferenceCount(fileName);
    }

    public synchronized void setReferenceCount(String fileName, int refCount) {
        assetManager.setReferenceCount(fileName, refCount);
    }

    public synchronized String getDiagnostics() {
        return assetManager.getDiagnostics();
    }

    public synchronized Array<String> getAssetNames() {
        return assetManager.getAssetNames();
    }

    public synchronized Array<String> getDependencies(String fileName) {
        return assetManager.getDependencies(assetIdentifiers.get(fileName).fileName);
    }

}
