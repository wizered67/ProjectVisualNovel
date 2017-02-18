package com.wizered67.game.conversations.commands.images;

import com.wizered67.game.conversations.scene.SceneImage;

/**
 * Represents an action to be applied on an image. The apply method
 * will be called on one image at a time, but may be called on entire
 * groups of images.
 * @author Adam Victor
 */
public interface ImageAction {
    void apply(SceneImage image);
}
