package com.wizered67.game.conversations.commands.impl.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.SceneCharacter;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;
import com.wizered67.game.conversations.scene.interpolations.PositionInterpolation;

/**
 * ConversationCommand for changing position and zoom of the camera, possibly using interpolations.
 * @author Adam Victor
 */
public class CameraCommand implements ConversationCommand {
    private PositionComponent positionComponent;
    private ZoomComponent zoomComponent;
    private boolean positionDone;
    private boolean zoomDone;

    public CameraCommand() {

    }

    public CameraCommand(PositionComponent positionComponent, ZoomComponent zoomComponent) {
        this.positionComponent = positionComponent;
        this.zoomComponent = zoomComponent;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        OrthographicCamera camera = conversationController.currentSceneManager().getCamera();
        if (positionComponent != null) {
            positionDone = !positionComponent.wait;
            Vector2 cameraPosition = new Vector2(camera.position.x, camera.position.y);
            Vector2 end = new Vector2(cameraPosition.x, cameraPosition.y);
            if (positionComponent.xSpecified) {
                if (positionComponent.relative) {
                    end.x += positionComponent.position.x;
                } else {
                    end.x = positionComponent.position.x;
                }
            }
            if (positionComponent.ySpecified) {
                if (positionComponent.relative) {
                    end.y += positionComponent.position.y;
                } else {
                    end.y = positionComponent.position.y;
                }
            }
            if (positionComponent.characterTarget != null) {
                SceneCharacter character = conversationController.currentSceneManager().getCharacterByIdentifier(positionComponent.characterTarget);
                if (character != null) {
                    Vector2 spriteOffset = new Vector2(0, 0);
                    if (character.getSprite() != null) {
                        spriteOffset.x = character.getSprite().getWidth() / 2;
                        spriteOffset.y = character.getSprite().getHeight() / 2;
                    }
                    end = character.getPosition().cpy().add(spriteOffset);
                    //Use x and y specified as offset from centering on character position.
                    if (positionComponent.xSpecified) {
                        end.x += positionComponent.position.x;
                    }
                    if (positionComponent.ySpecified) {
                        end.y += positionComponent.position.y;
                    }
                } else {
                    GameManager.error("Character " + positionComponent.characterTarget + " is not in the scene currently.");
                }
            }
            PositionInterpolation positionInterpolation =
                    new PositionInterpolation(positionComponent.type, cameraPosition, end, positionComponent.time);
            conversationController.currentSceneManager().setCameraPositionInterpolation(positionInterpolation);
        } else {
            positionDone = true;
        }
        if (zoomComponent != null) {
            zoomDone = !zoomComponent.wait;
            float cameraZoom = camera.zoom;
            float endZoom = cameraZoom;
            if (zoomComponent.relative) {
                endZoom += zoomComponent.zoom;
            } else {
                endZoom = zoomComponent.zoom;
            }
            FloatInterpolation zoomInterpolation = new FloatInterpolation(zoomComponent.type, cameraZoom, endZoom, zoomComponent.time);
            conversationController.currentSceneManager().setCameraZoomInterpolation(zoomInterpolation);
        } else {
            zoomDone = true;
        }
    }

    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return !zoomDone || !positionDone;
    }

    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.INTERPOLATION) {
            Object[] data = (Object[]) c.data;
            if (data[2] == CompleteEvent.InterpolationEventType.POSITION) {
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                if (manager.getCamera() == entity) {
                    positionDone = true;
                }
            }
            if (data[2] == CompleteEvent.InterpolationEventType.ZOOM) {
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                if (manager.getCamera() == entity) {
                    zoomDone = true;
                }
            }
        }
    }//todo allow camera per scene and multi scene? saving camera

    public static class PositionComponent {
        String characterTarget;
        Vector2 position;
        boolean xSpecified;
        boolean ySpecified;
        String type;
        float time;
        boolean wait;
        boolean relative;

        public PositionComponent(Vector2 position, boolean xSpecified, boolean ySpecified, String characterTarget,
                                  String type, float time, boolean wait, boolean relative) {
            this.position = position;
            this.xSpecified = xSpecified;
            this.ySpecified = ySpecified;
            this.characterTarget = characterTarget;
            this.type = type;
            this.time = time;
            this.wait = wait;
            this.relative = relative;
        }
    }

    public static class ZoomComponent {
        float zoom;
        String type;
        float time;
        boolean wait;
        boolean relative;

        public ZoomComponent(float zoom, String type, float time, boolean wait, boolean relative) {
            this.zoom = zoom;
            this.type = type;
            this.time = time;
            this.wait = wait;
            this.relative = relative;
        }
    }
}
