package com.wizered67.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.wizered67.game.assets.parameters.ParametersLoader;
import com.wizered67.game.conversations.scene.SceneCharacter;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.conversations.xmlio.ConversationAssetLoader;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.GameManager;

import static com.badlogic.gdx.utils.XmlReader.Element;

import java.io.IOException;
import java.util.*;


/**
 * Manages resources by adapting an AssetManager and using identifiers, not filenames, for resources.
 * When created, loads identifier-filename mapping from RESOURCE_CONFIG_XML. Also used for loading animations,
 * asset groups, and characters.
 * @author Adam Victor
 */
public class Assets {
    /** XML Reader used for reading XML. */
    private XmlReader xmlReader;
    /** AssetManager used for loading and getting resources.
     * When its methods are called through this, first map identifier to filename. */
    private AssetManager assetManager;
    /** Mapping between asset identifiers and the filenames they can be accessed with. */
    private Map<String, AssetDescriptor> assetIdentifiers;
    /** Map between animation names and the Animation object that corresponds to it. */
    private Map<String, Animation<TextureRegion>> allAnimations;
    /** Map between names of asset groups and the set of assets they contain.
     * Used to load/unload many resources with one call. */
    private Map<String, Set<AssetDescriptor>> assetGroups;
    /** Used to load parameters from XML. */
    private ParametersLoader parametersLoader;

    private final String RESOURCE_CONFIG_XML = "Resources.xml";

    private final String RESOURCE_DIRECTORIES_FILE = "ResourceDirectories.xml";
    private final String CHARACTERS_FILE = "CharacterDefinitions.xml";
    private final String LOADING_FILE = "LoadingGroups.xml";


    private static final String ANIMATIONS_TAG = "animations";

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
        assetGroups = new HashMap<>();
        parametersLoader = new ParametersLoader();

        initResources();
        assetManager.setLoader(Conversation.class, new ConversationAssetLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(AnimationTextureAtlas.class, new TextureAtlasAnimationLoader(new InternalFileHandleResolver()));
    }

    /** Returns the parameters used to load an asset, also useful for getting data to use later. */
    public AssetLoaderParameters getParameters(String identifier) {
        return assetIdentifiers.get(identifier).params;
    }

    /** Called when an AnimationTextureAtlas is loaded to create all its animations and put into the allAnimations map. */
    public void loadAnimations(TextureAtlas atlas, List<AnimationData> animationsDataList) {
        for (AnimationData animationData : animationsDataList) {
            allAnimations.put(animationData.getAtlasAnimation(), animationData.createAnimation(atlas));
        }
    }
    /** Called when an AnimationTextureAtlas is disposed to remove all its animations. */
    public void unloadAnimations(List<AnimationData> animationDataList) {
        for (AnimationData animationData : animationDataList) {
            allAnimations.put(animationData.getAtlasAnimation(), null);
        }
    }
    /** Returns the Animation with identifier IDENTIFIER. */
    public Animation<TextureRegion> getAnimation(String identifier) {
        if (!allAnimations.containsKey(identifier)) {
            if (assetIdentifiers.containsKey(identifier)) { //actually a Texture
                Texture texture = get(identifier, Texture.class);
                Animation<TextureRegion> animation = new Animation<>(1, new TextureRegion(texture));
                allAnimations.put(identifier, animation);
                return animation;
            }
        }
        return allAnimations.get(identifier);
    }
    /** Maps all identifiers to Asset Descriptors, loads characters, and creates asset groups. */
    public void initResources() {
        xmlReader = new XmlReader();
        try {
            Element directoriesRoot = xmlReader.parse(Gdx.files.internal(RESOURCE_DIRECTORIES_FILE));
            loadAllResourceDefinitions(directoriesRoot);
            loadCharacters(xmlReader.parse(Gdx.files.internal(CHARACTERS_FILE)));
            createGroups(xmlReader.parse(Gdx.files.internal(LOADING_FILE)));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void loadAllResourceDefinitions(Element directoriesRoot) {
        for (int i = 0; i < directoriesRoot.getChildCount(); i++) {
            String classString = directoriesRoot.getChild(i).getAttribute("type", "java.lang.Object");
            String directoryName = directoriesRoot.getChild(i).getAttribute("name");
            try {
                Class clss = ClassReflection.forName(classString);
                Element resourcesRoot = xmlReader.parse(Gdx.files.internal(directoryName + "/" + RESOURCE_CONFIG_XML));
                loadResourceDefinitions(directoryName, resourcesRoot, clss);
            } catch (ReflectionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadResourceDefinitions(String directory, Element resourcesRoot, Class type) {
        for (int i = 0; i < resourcesRoot.getChildCount(); i++) {
            Element resourceElement = resourcesRoot.getChild(i);
            String name = resourceElement.getAttribute("name");
            String filename = directory + "/" + name;
            if (!Gdx.files.internal(filename).exists()) {
                GameManager.error("No such file '" + filename + "'.");
                continue;
            }
            String identifier = resourceElement.getAttribute("identifier", name);
            if (assetIdentifiers.containsKey(identifier)) {
                identifierError(identifier);
                continue;
            }
            AssetLoaderParameters parameters = parametersLoader.getParameters(type, resourceElement, identifier);
            assetIdentifiers.put(identifier, new AssetDescriptor(filename, type, parameters));
        }
    }

    /** Loads, creates, and adds to all SceneManager's map of all characters, the characters containing
     * in the child of ROOT named CHARACTERS_TAG. */
    private void loadCharacters(Element root) {
        for (int c = 0; c < root.getChildCount(); c += 1) {
            Element character = root.getChild(c);
            String id = character.getAttribute("id");
            String name = character.getAttribute("name");
            String sound = character.getAttribute("sound", SceneCharacter.DEFAULT_SPEAKING_SOUND);
            SceneManager.createCharacter(id, name, sound);
        }
    }

    /** Creates groups by mapping group names to the set of AssetDescriptors of resources in the group. */
    private void createGroups(XmlReader.Element groups) {
        for (int c = 0; c < groups.getChildCount(); c += 1) {
            XmlReader.Element group = groups.getChild(c);
            String groupName = group.getAttribute("name");
            Set<AssetDescriptor> assets = new HashSet<>();
            for (int a = 0; a < group.getChildCount(); a += 1) {
                XmlReader.Element asset = group.getChild(a);
                if (asset.getName().equals("load")) { //todo use constant for name?
                    String identifier = asset.getText();
                    AssetDescriptor descriptor = assetIdentifiers.get(identifier);
                    if (descriptor == null) {
                        GameManager.error("No such resource with name " + identifier);
                    }
                    assets.add(descriptor);
                }
            }
            assetGroups.put(groupName, assets);
        }
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
            unloadRaw(descriptor.fileName);
        }
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
        return assetManager.get(assetIdentifiers.get(identifier).fileName);
    }

    public synchronized <T> T get(String identifier, Class<T> type) {
        return assetManager.get(assetIdentifiers.get(identifier).fileName, type);
    }


    public synchronized void unload(String identifier) {
        assetManager.unload(assetIdentifiers.get(identifier).fileName);
    }


    public synchronized boolean isLoaded(String identifier) {
        return assetManager.isLoaded(assetIdentifiers.get(identifier).fileName);
    }


    public synchronized boolean isLoaded(String identifier, Class type) {
        return assetManager.isLoaded(assetIdentifiers.get(identifier).fileName, type);
    }

    public synchronized void load(String identifier) {
        assetManager.load(assetIdentifiers.get(identifier));
    }

    public synchronized <T> void load(String identifier, Class<T> type) {
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
