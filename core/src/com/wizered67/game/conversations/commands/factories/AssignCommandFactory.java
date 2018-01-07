package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.base.AssignCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;
import com.wizered67.game.conversations.xmlio.ConversationParsingException;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating an AssignCommand from an XML element.
 * @author Adam Victor
 */
public class AssignCommandFactory implements ConversationCommandFactory<AssignCommand> {

    private final static AssignCommandFactory INSTANCE = new AssignCommandFactory();

    public static AssignCommandFactory getInstance() {
        return INSTANCE;
    }

    private AssignCommandFactory() {}

    @Override
    public AssignCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String name = "";
        Map<String, ConversationCommand> map = new HashMap<String, ConversationCommand>();
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element child = element.getChild(i);
            if (i % 2 == 0) {
                name = child.getText().trim();
            } else {
                try {
                    map.put(name, loader.getCommand(child));
                } catch (ConversationParsingException e) {
                    //If the command isn't valid print and ignore.
                    e.printStackTrace();
                }
            }
        }
        return new AssignCommand(map);
    }
}
