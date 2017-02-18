package com.wizered67.game.gui.conversations.commands.images;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.gui.conversations.commands.CommandSequence;


/**
 * This class is mapped to the <image> command so that the makeCommand
 * method is called for any image related command. Inside the image tag,
 * there are a number of different elements for modifying properties of images.
 * The makeCommand method will automatically create the commands necessary
 * to make the changes. The result of makeCommand is a CommandSequence which
 * contains all the necessary commands so that they can be executed in sequence.
 * @author Adam Victor
 */
public class ModifyImageCommandCreator {
    /** Extracts all information from the <image> element and uses it to create
     * the subcommands necessary to modify its state as specified.
     */
    public static CommandSequence makeCommand(XmlReader.Element element) {
        CommandSequence result = new CommandSequence();
        String instance = element.getAttribute("instance", "");
        String group = element.getAttribute("group", "");
        if (!instance.isEmpty()) {
            result.addCommand(new ImageCreateCommand(instance));
        }
        for (int c = 0; c < element.getChildCount(); c += 1) {
            XmlReader.Element child = element.getChild(c);
            if (child.getName().equals("texture")) {
                result.addCommand(ImageTextureCommand.makeCommand(instance, group, child));
            } else if (child.getName().equals("position")) {
                result.addCommand(ImagePositionCommand.makeCommand(instance, group, child));
            } else if (child.getName().equals("visibility")) {
                result.addCommand(ImageVisibilityCommand.makeCommand(instance, group, child));
            } else if (child.getName().equals("group")) {
                result.addCommand(ImageGroupChangeCommand.makeCommand(instance, group, child));
            }
        }
        return result;
    }
}