package com.wizered67.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.wizered67.game.GameManager;

import java.util.List;

/**
 * Same behavior as regular TextureAtlases, but also unloads Animations when disposed.
 * @author Adam Victor
 */
public class AnimationTextureAtlas extends TextureAtlas {
    private List<AnimationData> animationDataList;
    public AnimationTextureAtlas(TextureAtlasData data, List<AnimationData> dataList) {
        super(data);
        animationDataList = dataList;
    }
    @Override
    public void dispose() {
        super.dispose();
        GameManager.assetManager().unloadAnimations(animationDataList);
    }
}
