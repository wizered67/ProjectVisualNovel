package com.wizered67.game.gui.conversations.commands;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.gui.conversations.CompleteEvent;
import com.wizered67.game.gui.conversations.ConversationController;
import com.wizered67.game.gui.conversations.xmlio.ConversationLoader;
import com.wizered67.game.gui.conversations.scene.Fade;
import com.wizered67.game.gui.conversations.scene.SceneManager;
import com.wizered67.game.GameManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ConversationCommand for fading out the screen and then fading back in. Allows setting color, time
 * for each, interpolation type for each, and whether to wait before proceeding. Once the fade out is complete,
 * commands nested in the fade element are executed. Once those are done, the fade in begins. This allows, for example,
 * removing all characters while the screen is fully colored so that the player does not see the characters dissapear.
 * @author Adam Victor
 */
public class ScreenFadeCommand implements ConversationCommand {
    private Color color;
    private float exitTime;
    private String exitType;
    private float enterTime;
    private String enterType;
    private boolean wait;
    private boolean done;
    private List<ConversationCommand> commands;

    public ScreenFadeCommand(List<ConversationCommand> commands, Color color, float exitTime, String exitType, float enterTime, String enterType, boolean wait) {
        this.commands = commands;
        this.commands.add(new FadeInCommand());
        this.color = color.cpy();
        this.exitTime = exitTime;
        this.exitType = exitType;
        this.enterTime = enterTime;
        this.enterType = enterType;
        this.wait = wait;
        done = !wait;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        done = !wait;
        conversationController.sceneManager().setFade(new Fade(exitType, 0, exitTime, 1), color);
    }

    private void addEnterCommands(ConversationController conversationController) {
        conversationController.insertCommands(commands);
    }

    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return !done;
    }

    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.FADE_END) {
            Object[] data = (Object[]) c.data;
            SceneManager manager = (SceneManager) data[0];
            Object entity = data[1];
            if (manager == entity) {
                done = true;
                addEnterCommands(manager.conversationController());
            }
        }
    }

    public static ScreenFadeCommand makeCommand(XmlReader.Element element) {
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
            commands.add(ConversationLoader.getCommand(child));
        }
        return new ScreenFadeCommand(commands, color, exitTime, exitType, enterTime, enterType, wait);
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }

    private class FadeInCommand implements ConversationCommand {
        private boolean isDone;
        private FadeInCommand() {

        }

        @Override
        public void execute(ConversationController conversationController) {
            isDone = !wait;
            conversationController.sceneManager().setFade(new Fade(enterType, 1, enterTime, -1), color);
        }

        @Override
        public boolean waitToProceed() {
            return !isDone;
        }

        @Override
        public void complete(CompleteEvent c) {
            if (c.type == CompleteEvent.Type.FADE_END) {
                Object[] data = (Object[]) c.data;
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                if (manager == entity) {
                    isDone = true;
                }
            }
        }

        @Override
        public void writeXml(XmlWriter xmlWriter) {

        }
    }
}
