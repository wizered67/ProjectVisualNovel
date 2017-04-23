package com.wizered67.game.conversations.commands;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
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
                    end = character.getPosition().cpy();
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
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
    //todo allow camera per scene and multi scene? saving camera
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CameraCommand makeCommand(XmlReader.Element root) {
        XmlReader.Element positionElement = root.getChildByName("position");
        PositionComponent positionComponent = null;
        if (positionElement != null) {
            positionComponent = makePositionComponent(positionElement);
        }
        XmlReader.Element zoomElement = root.getChildByName("zoom");
        ZoomComponent zoomComponent = null;
        if (zoomElement != null) {
            zoomComponent = makeZoomComponent(zoomElement);
        }
        return new CameraCommand(positionComponent, zoomComponent);
    }

    private static PositionComponent makePositionComponent(XmlReader.Element element) {
        String xString = element.getAttribute("x", null);
        String yString = element.getAttribute("y", null);
        boolean relative = element.getBooleanAttribute("relative", false);
        float x = (xString != null) ? Float.parseFloat(xString) : 0;
        float y = (yString != null) ? Float.parseFloat(yString) : 0;
        String characterTarget = element.getAttribute("characterTarget", null);
        String interpolationType = element.getAttribute("interpolation", "linear");
        float time = element.getFloatAttribute("time", 0);
        boolean wait = element.getBooleanAttribute("wait", false);
        return new PositionComponent(new Vector2(x, y), xString != null, yString != null, characterTarget, interpolationType, time, wait, relative);
    }

    private static ZoomComponent makeZoomComponent(XmlReader.Element element) {
        float zoom = element.getFloatAttribute("amount");
        String type = element.getAttribute("interpolation", "linear");
        float time = element.getFloat("time", 0);
        boolean relative = element.getBooleanAttribute("relative", false);
        boolean wait = element.getBooleanAttribute("wait", false);
        return new ZoomComponent(zoom, type, time, wait, relative);
    }

    private static class PositionComponent {
        String characterTarget;
        Vector2 position;
        boolean xSpecified;
        boolean ySpecified;
        String type;
        float time;
        boolean wait;
        boolean relative;

        private PositionComponent(Vector2 position, boolean xSpecified, boolean ySpecified, String characterTarget,
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

    private static class ZoomComponent {
        float zoom;
        String type;
        float time;
        boolean wait;
        boolean relative;

        private ZoomComponent(float zoom, String type, float time, boolean wait, boolean relative) {
            this.zoom = zoom;
            this.type = type;
            this.time = time;
            this.wait = wait;
            this.relative = relative;
        }
    }
}
