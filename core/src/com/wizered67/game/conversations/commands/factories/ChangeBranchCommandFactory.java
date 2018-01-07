package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.base.ChangeBranchCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a ChangeBranchCommand from an XML element.
 * @author Adam Victor
 */
public class ChangeBranchCommandFactory implements ConversationCommandFactory<ChangeBranchCommand> {

    private final static ChangeBranchCommandFactory INSTANCE = new ChangeBranchCommandFactory();

    public static ChangeBranchCommandFactory getInstance() {
        return INSTANCE;
    }

    private ChangeBranchCommandFactory() {}

    @Override
    public ChangeBranchCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String newBranch = element.getAttribute("branch", null);
        if (newBranch == null) {
            throw new GdxRuntimeException("No new branch specified for new branch command.");
        }
        return new ChangeBranchCommand(newBranch);
    }
}
