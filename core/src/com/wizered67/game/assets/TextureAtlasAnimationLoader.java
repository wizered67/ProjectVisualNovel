package com.wizered67.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.utils.Array;
import com.wizered67.game.GameManager;


public class TextureAtlasAnimationLoader extends SynchronousAssetLoader<AnimationTextureAtlas, TextureAtlasAnimationLoader.AnimationTextureAtlasParameter> {
    public TextureAtlasAnimationLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    private TextureAtlasData data;

    @Override
    public AnimationTextureAtlas load (AssetManager assetManager, String fileName, FileHandle file, AnimationTextureAtlasParameter parameter) {
        for (Page page : data.getPages()) {
            page.texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
        }

        AnimationTextureAtlas atlas = new AnimationTextureAtlas(data);
        atlas.setFilepath(fileName);
        GameManager.assetManager().loadAnimation(fileName, atlas);
        return atlas;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle atlasFile, AnimationTextureAtlasParameter parameter) {
        FileHandle imgDir = atlasFile.parent();

        if (parameter != null)
            data = new TextureAtlasData(atlasFile, imgDir, parameter.flip);
        else {
            data = new TextureAtlasData(atlasFile, imgDir, false);
        }

        Array<AssetDescriptor> dependencies = new Array();
        for (Page page : data.getPages()) {
            TextureParameter params = new TextureParameter();
            params.format = page.format;
            params.genMipMaps = page.useMipMaps;
            params.minFilter = page.minFilter;
            params.magFilter = page.magFilter;
            dependencies.add(new AssetDescriptor(page.textureFile, Texture.class, params));
        }
        return dependencies;
    }

    static public class AnimationTextureAtlasParameter extends AssetLoaderParameters<AnimationTextureAtlas> {
        /** whether to flip the texture atlas vertically **/
        public boolean flip = false;

        public AnimationTextureAtlasParameter () {
        }

        public AnimationTextureAtlasParameter (boolean flip) {
            this.flip = flip;
        }
    }
}
