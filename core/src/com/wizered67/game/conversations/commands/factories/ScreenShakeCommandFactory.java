package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.scene.ScreenShakeCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a ScreenShakeCommand from an XML element.
 *
 * @author Adam Victor
 */
public class ScreenShakeCommandFactory implements ConversationCommandFactory<ScreenShakeCommand> {
    private final static ScreenShakeCommandFactory INSTANCE = new ScreenShakeCommandFactory();

    public static ScreenShakeCommandFactory getInstance() {
        return INSTANCE;
    }

    private ScreenShakeCommandFactory() {
    }

    @Override
    public ScreenShakeCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        float time = element.getFloatAttribute("time");
        String interpolation = element.getAttribute("interpolation", "linear");
        boolean wait = element.getBooleanAttribute("wait", false);
        float magnitudeX = element.getFloatAttribute("xMagnitude", 0);
        float magnitudeY = element.getFloatAttribute("yMagnitude", 0);
        return new ScreenShakeCommand(interpolation, new Vector2(magnitudeX, magnitudeY), time, wait);
    }
}