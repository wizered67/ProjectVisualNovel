package com.wizered67.game.assets.parameters;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.wizered67.game.assets.AnimationData;
import com.wizered67.game.assets.AnimationTextureAtlas;
import com.wizered67.game.assets.TextureAtlasAnimationLoader;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.utils.XmlReader.Element;
/**
 * Creates AssetLoaderParameters of a certain type given a base element and its identifier.
 * @author Adam Victor
 */
public class ParametersLoader {
    public AssetLoaderParameters getParameters(Class type, Element resourceElement, String identifier) {
        if (type == AnimationTextureAtlas.class) {
            return makeAnimationParameters(resourceElement, identifier);
        } else if (type == Music.class) {
            return makeMusicParameters(resourceElement, identifier);
        } else if (type == Texture.class) {
            return makeTextureParameters(resourceElement, identifier);
        } else {
            return null;
        }
    }

    private AssetLoaderParameters makeAnimationParameters(Element resourceElement, String identifier) {
        if (resourceElement.getChildCount() <= 0) {
            return null;
        }
        List<AnimationData> dataList = new ArrayList<>();
        Element animationsElement = resourceElement.getChild(0);
        boolean flip = animationsElement.getBooleanAttribute("flip", false);
        for (int i = 0; i < animationsElement.getChildCount(); i++) {
            Element child = animationsElement.getChild(i);
            if (child.getName().equals("animation")) {
                String animationId = child.getAttribute("id");
                float frameDuration = child.getFloatAttribute("frameDuration", 0);
                String playMode = child.getAttribute("playMode", "NORMAL");
                dataList.add(new AnimationData(identifier, animationId, frameDuration, Animation.PlayMode.valueOf(playMode)));
            }
        }
        return new TextureAtlasAnimationLoader.AnimationTextureAtlasParameter(flip, dataList);
    }

    private AssetLoaderParameters makeMusicParameters(Element resourceElement, String identifier) {
        if (resourceElement.getChildCount() <= 0) {
            return null;
        }
        Element musicElement = resourceElement.getChild(0);
        float volume = musicElement.getFloatAttribute("volume", 1);
        return new MusicParameters(volume);
    }

    private AssetLoaderParameters makeTextureParameters(Element resourceElement, String identifier) {
        if (resourceElement.getChildCount() <= 0) {
            return null;
        }
        Element textureElement = resourceElement.getChild(0);
        boolean genMipMaps = textureElement.getBooleanAttribute("genMipMaps", false);
        String minFilterName = textureElement.getAttribute("minFilter", "Nearest");
        String magFilterName = textureElement.getAttribute("magFilter", "Nearest");
        String wrapUName = textureElement.getAttribute("wrapU", "ClampToEdge");
        String wrapVName = textureElement.getAttribute("wrapV", "ClampToEdge");
        TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
        parameter.genMipMaps = genMipMaps;
        parameter.minFilter = Texture.TextureFilter.valueOf(minFilterName);
        parameter.magFilter = Texture.TextureFilter.valueOf(magFilterName);
        parameter.wrapU = Texture.TextureWrap.valueOf(wrapUName);
        parameter.wrapV = Texture.TextureWrap.valueOf(wrapVName);
        return parameter;
    }
}
