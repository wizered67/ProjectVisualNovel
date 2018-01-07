package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.scene.ScreenFadeCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;
import com.wizered67.game.conversations.xmlio.ConversationParsingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating a ScreenFadeCommand from an XML element.
 *
 * @author Adam Victor
 */
public class ScreenFadeCommandFactory implements ConversationCommandFactory<ScreenFadeCommand> {
    private final static ScreenFadeCommandFactory INSTANCE = new ScreenFadeCommandFactory();

    public static ScreenFadeCommandFactory getInstance() {
        return INSTANCE;
    }

    private ScreenFadeCommandFactory() {
    }

    @Override
    public ScreenFadeCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String colorName = element.getAttribute("color", "BLACK");
        Color color = Colors.get(colorName);
        if (color == null) {
            GameManager.error("No such color: " + colorName);
            color = Color.WHITE;
        }
        float exitTime = element.getFloatAttribute("exitTime");
        float enterTime = element.getFloatAttribute("enterTime");
        boolean wait = element.getBooleanAttribute("wait", true);
        String exitType = element.getAttribute("exitType", "linear");
        String enterType = element.getAttribute("enterType", "linear");
        List<ConversationCommand> commands = new ArrayList<>();
        for (int c = 0; c < element.getChildCount(); c += 1) {
            XmlReader.Element child = element.getChild(c);
            try {
                commands.add(loader.getCommand(child));
            } catch (ConversationParsingException e) {
                e.printStackTrace();
            }

        }
        return new ScreenFadeCommand(commands, color, exitTime, exitType, enterTime, enterType, wait);
    }
}