package com.wizered67.game.GUI.Conversations.Commands.images;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.GUI.Conversations.Commands.CommandSequence;


/**
 * Created by Adam on 1/27/2017.
 */
public class ModifyImageCommandCreator {
    public static CommandSequence makeCommand(XmlReader.Element element) {
        CommandSequence result = new CommandSequence();
        String instance = element.getAttribute("instance", "");
        String group = element.getAttribute("group", "");
        for (int c = 0; c < element.getChildCount(); c += 1) {
            XmlReader.Element child = element.getChild(c);
            if (child.getName().equals("image")) {
                result.addCommand(ImageTextureCommand.makeCommand(instance, group, child));
            } else if (child.getName().equals("position")) {
                result.addCommand(ImagePositionCommand.makeCommand(instance, group, child));
            } else if (child.getName().equals("visibility")) {
                result.addCommand(ImageVisibilityCommand.makeCommand(instance, group, child));
            }
        }
        return result;
    }
}