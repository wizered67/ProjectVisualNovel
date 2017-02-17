package com.wizered67.game.Saving.Serializers;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.wizered67.game.Assets;
import com.wizered67.game.GameManager;
import com.wizered67.game.StoredAssetData;

import java.util.ArrayList;
import java.util.List;

/**
 * Serializes Assets to ensure loaded assets stay loaded.
 * @author Adam Victor
 */
public class AssetsSerializer extends Serializer<Assets> {
    /**
     * Writes the bytes for the object to the output.
     * <p>
     * This method should not be called directly, instead this serializer can be passed to {@link Kryo} write methods that accept a
     * serialier.
     */
    @Override
    public void write(Kryo kryo, Output output, Assets object) {
        List<StoredAssetData> loadedAssets = new ArrayList<>();
        Array<String> filenames = object.getAssetNames();
        for (String file : filenames) {
            Class type = object.getAssetTypeRaw(file);
            int count = object.getReferenceCount(file);
            StoredAssetData data = new StoredAssetData(file, type, count);
            loadedAssets.add(data);
        }
        kryo.writeObject(output, loadedAssets);
    }

    /**
     * Reads bytes and returns a new object of the specified concrete type.
     * <p>
     * Before Kryo can be used to read child objects, {@link Kryo#reference(Object)} must be called with the parent object to
     * ensure it can be referenced by the child objects. Any serializer that uses {@link Kryo} to read a child object may need to
     * be reentrant.
     * <p>
     * This method should not be called directly, instead this serializer can be passed to {@link Kryo} read methods that accept a
     * serialier.
     */
    @Override
    public Assets read(Kryo kryo, Input input, Class<Assets> type) {
        Assets assets = GameManager.assetManager();
        List<StoredAssetData> loadedAssets = kryo.readObject(input, ArrayList.class);
        for (StoredAssetData data : loadedAssets) {
            data.reloadAsset(assets);
        }
        GameManager.assetManager().finishLoading();
        for (StoredAssetData data : loadedAssets) {
            data.reloadReferenceCount(assets);
        }
        return assets;
    }

}
