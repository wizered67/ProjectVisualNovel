package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.scene.CameraCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a CameraCommand from an XML element.
 * @author Adam Victor
 */
public class CameraCommandFactory implements ConversationCommandFactory<CameraCommand> {

    private final static CameraCommandFactory INSTANCE = new CameraCommandFactory();

    public static CameraCommandFactory getInstance() {
        return INSTANCE;
    }

    private CameraCommandFactory() {}

    @Override
    public CameraCommand makeCommand(ConversationLoader loader, XmlReader.Element root) {
        XmlReader.Element positionElement = root.getChildByName("position");
        CameraCommand.PositionComponent positionComponent = null;
        if (positionElement != null) {
            positionComponent = makePositionComponent(positionElement);
        }
        XmlReader.Element zoomElement = root.getChildByName("zoom");
        CameraCommand.ZoomComponent zoomComponent = null;
        if (zoomElement != null) {
            zoomComponent = makeZoomComponent(zoomElement);
        }
        return new CameraCommand(positionComponent, zoomComponent);
    }

    private CameraCommand.PositionComponent makePositionComponent(XmlReader.Element element) {
        String xString = element.getAttribute("x", null);
        String yString = element.getAttribute("y", null);
        boolean relative = element.getBooleanAttribute("relative", false);
        float x = (xString != null) ? Float.parseFloat(xString) : 0;
        float y = (yString != null) ? Float.parseFloat(yString) : 0;
        String characterTarget = element.getAttribute("characterTarget", null);
        String interpolationType = element.getAttribute("interpolation", "linear");
        float time = element.getFloatAttribute("time", 0);
        boolean wait = element.getBooleanAttribute("wait", false);
        return new CameraCommand.PositionComponent(new Vector2(x, y), xString != null, yString != null, characterTarget, interpolationType, time, wait, relative);
    }

    private CameraCommand.ZoomComponent makeZoomComponent(XmlReader.Element element) {
        float zoom = element.getFloatAttribute("amount");
        String type = element.getAttribute("interpolation", "linear");
        float time = element.getFloat("time", 0);
        boolean relative = element.getBooleanAttribute("relative", false);
        boolean wait = element.getBooleanAttribute("wait", false);
        return new CameraCommand.ZoomComponent(zoom, type, time, wait, relative);
    }
}
