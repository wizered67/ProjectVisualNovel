package com.wizered67.game.GUI.Conversations.XmlIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.wizered67.game.GUI.Conversations.Commands.*;
import com.wizered67.game.GUI.Conversations.Conversation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * A class used to load and parse XML files into Conversations with
 * the commands in the XML file.
 * @author Adam Victor
 */
public class ConversationLoader {
    /** Used to parse Xml. The MixedXmlReader separates text into its own elements to maintain order. */
    private MixedXmlReader xml = new MixedXmlReader();
    /** Mapping of xml element names to corresponding ConversationCommand class. */
    private static Map<String, Class> classMapping = new HashMap<String, Class>();

    static {
        classMapping.put("assign", AssignCommand.class);
        classMapping.put("bg", BackgroundSetCommand.class);
        classMapping.put("changebranch", ChangeBranchCommand.class);
        classMapping.put("changeconv", ChangeConversationCommand.class);
        classMapping.put("character", CharacterAddCommand.class);
        classMapping.put("animation", CharacterAnimationCommand.class);
        classMapping.put("direction", CharacterDirectionCommand.class);
        classMapping.put("name", CharacterNameCommand.class);
        classMapping.put("position", CharacterPositionCommand.class);
        classMapping.put("visible", CharacterVisibleCommand.class);
        classMapping.put("sequence", CommandSequence.class);
        classMapping.put("debug", DebugCommand.class);
        classMapping.put("delay", DelayCommand.class);
        classMapping.put("script", ExecuteScriptCommand.class);
        classMapping.put("text", MessageCommand.class);
        classMapping.put("music", PlayMusicCommand.class);
        classMapping.put("sound", PlaySoundCommand.class);
        classMapping.put("preload", PreloadCommand.class);
        classMapping.put("choices", ShowChoicesCommand.class);
        classMapping.put("textspeed", TextSpeedCommand.class);
        classMapping.put("if", VariableConditionCommand.class);
        classMapping.put("init", VariableInitializeCommand.class);
    }

    /** Returns the Conversation created by parsing the XML file
     * with the name NAME. */
    public Conversation loadConversation(String name) {
        FileHandle file = Gdx.files.internal("Conversations/" + name);
        return loadConversation(file);
    }

    /** Returns the Conversation created by parsing the XML file with FileHandle FILE. */
    public Conversation loadConversation(FileHandle file) {
        if (file == null) {
            return null;
        }
        try {
            Conversation conversation = new Conversation(file.name());
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
            throw new GdxRuntimeException("Couldn't load Conversation " + file.name(), e);
        }
    }

    /** Adds the branch contained in the XML Element ROOT to CONVERSATION by
     * parsing each command's XML tag.
     */
    private void addBranch(Conversation conversation, Element root) {
        String name = root.getAttribute("name");
        LinkedList<ConversationCommand> commands = new LinkedList<ConversationCommand>();
        for (int i = 0; i < root.getChildCount(); i += 1) {
            commands.add(getCommand(root.getChild(i)));
        }
        conversation.addBranch(name, commands);
    }

    /** Static method to add a new mapping between element name NAME
     * and ConversationCommand class type. */
    public static void addClassMapping(String name, Class type) {
        classMapping.put(name, type);
    }

    /** Static method to return a ConversationCommand given the XML Element ROOT.
     * Passes in the current CONVERSATION for commands to reference as necessary.
     * Uses reflection to invoke the makeCommand method of the corresponding class
     * based on class mapping.
     */
    public static ConversationCommand getCommand(Element root) {
        String name = root.getName();
        Class clazz = classMapping.get(name);
        ConversationCommand command = null;
        if (clazz == null) {
            Gdx.app.error("Commands", "No class mapped to name: " + name);
        }
        try {
            Method m = ClassReflection.getMethod(clazz, "makeCommand", Element.class);
            command = (ConversationCommand) m.invoke(null, root);
        } catch (ReflectionException re) {
            re.printStackTrace();
            throw new RuntimeException();
        }
        return command;
    }
}
