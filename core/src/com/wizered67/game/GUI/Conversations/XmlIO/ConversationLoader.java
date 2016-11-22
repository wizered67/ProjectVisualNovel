package com.wizered67.game.GUI.Conversations.XmlIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.*;
import com.wizered67.game.GUI.Conversations.Conversation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;

/**
 * Created by Adam on 11/15/2016.
 */
public class ConversationLoader {
    private XmlReader xml = new XmlReader();

    public Conversation loadConversation(String name) {
        try {
            Conversation conversation = new Conversation();
            FileHandle file = Gdx.files.internal("Conversations/" + name);
            Element root = xml.parse(file);
            for (int i = 0; i < root.getChildCount(); i += 1) {
                Element c = root.getChild(i);
                String n = c.getName();
                if (n.equals("branch")) {
                    addBranch(conversation, c);
                }
            }
            return conversation;
        } catch (IOException e) {
            throw new GdxRuntimeException("Couldn't load Conversation " + name, e);
        }
    }

    private void addBranch(Conversation conversation, Element root) {
        String name = root.getAttribute("name");
        LinkedList<ConversationCommand> commands = new LinkedList<ConversationCommand>();
        for (int i = 0; i < root.getChildCount(); i += 1) {
            commands.add(getCommand(conversation, root.getChild(i)));
        }
        conversation.addBranch(name, commands);
    }

    public static ConversationCommand getCommand(Conversation conversation, Element root) {
        String name = root.getName();
        ConversationCommand command = null;
        if (name.equals("changebranch")) {
            command = ChangeBranchCommand.makeCommand(conversation, root);
        } else if (name.equals("addcharacter")) {
            command = CharacterAddCommand.makeCommand(conversation, root);
        } else if (name.equals("setanimation")) {
            command = CharacterAnimationCommand.makeCommand(conversation, root);
        } else if (name.equals("setdirection")) {
            command = CharacterDirectionCommand.makeCommand(conversation, root);
        } else if (name.equals("setname")) {
            command = CharacterNameCommand.makeCommand(conversation, root);
        } else if (name.equals("setposition")) {
            command = CharacterPositionCommand.makeCommand(conversation, root);
        } else if (name.equals("setvisible")) {
            command = CharacterVisibleCommand.makeCommand(conversation, root);
        } else if (name.equals("sequence")) {
            command = CommandSequence.makeCommand(conversation, root);
        } else if (name.equals("debug")) {
            command = DebugCommand.makeCommand(conversation, root);
        } else if (name.equals("message")) {
            command = MessageCommand.makeCommand(conversation, root);
        } else if (name.equals("playmusic")) {
            command = PlayMusicCommand.makeCommand(conversation, root);
        } else if (name.equals("pausemusic")) {
            command = new PlayMusicCommand(1);
        } else if (name.equals("resumemusic")) {
            command = new PlayMusicCommand(2);
        } else if (name.equals("playsound")) {
            command = PlaySoundCommand.makeCommand(conversation, root);
        } else if (name.equals("preload")) {
            command = null; //TODO
        } else if (name.equals("choices")) {
            command = ShowChoicesCommand.makeCommand(conversation, root);
        }

        return command;
    }
}
