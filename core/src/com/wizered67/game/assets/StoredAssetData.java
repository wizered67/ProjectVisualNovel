package com.wizered67.game.assets;

/**
 * Stores data for a loaded asset. Used for serialization.
 * @author Adam Victor
 */
public class StoredAssetData {
    /** The filename of this asset. */
    private String filename;
    /** The type of this asset. */
    private Class assetType;
    /** The reference count of this asset. */
    private int referenceCount;
    public StoredAssetData() {}
    public StoredAssetData(String file, Class type, int count) {
        filename = file;
        assetType = type;
        referenceCount = count;
    }
    public void reloadAsset(Assets assets) {
        assets.loadRaw(filename, assetType);
    }

    public void reloadReferenceCount(Assets assets) {
        assets.setReferenceCount(filename, referenceCount);
    }
}
