package com.wizered67.game.Saving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import com.wizered67.game.GUI.Conversations.scene.SceneCharacter;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.scene.SceneEntity;
import com.wizered67.game.GUI.Conversations.scene.SceneImage;
import com.wizered67.game.GUI.Conversations.scene.SceneManager;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.MusicManager;
import com.wizered67.game.Saving.Serializers.*;
import com.wizered67.game.Saving.Serializers.LuaSerializers.LuaBooleanSerializer;
import com.wizered67.game.Saving.Serializers.LuaSerializers.LuaDoubleSerializer;
import com.wizered67.game.Saving.Serializers.LuaSerializers.LuaIntegerSerializer;
import com.wizered67.game.Saving.Serializers.LuaSerializers.LuaStringSerializer;
import com.wizered67.game.Scripting.GameScript;
import com.wizered67.game.Scripting.ScriptManager;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaDouble;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaString;

import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Saves and loads serialized data using Kryo.
 * @author Adam Victor
 */
public class SaveManager {
    private static Kryo kryo = new Kryo();
    /** Adds all necessary serializers to Kryo for serializing important objects. */
    public static void init() {
        kryo.addDefaultSerializer(MusicManager.class, MusicManagerSerializer.class);
        kryo.addDefaultSerializer(SceneCharacter.class, CharacterSpriteSerializer.class);
        kryo.addDefaultSerializer(ConversationController.class, ConversationControllerSerializer.class);
        kryo.addDefaultSerializer(SceneManager.class, SceneManagerSerializer.class);
        kryo.addDefaultSerializer(LuaBoolean.class, LuaBooleanSerializer.class);
        kryo.addDefaultSerializer(LuaDouble.class, LuaDoubleSerializer.class);
        kryo.addDefaultSerializer(LuaInteger.class, LuaIntegerSerializer.class);
        kryo.addDefaultSerializer(LuaString.class, LuaStringSerializer.class);
        kryo.addDefaultSerializer(GameScript.class, GameScriptSerializer.class);
        kryo.addDefaultSerializer(ScriptManager.class, ScriptManagerSerializer.class);
        kryo.addDefaultSerializer(Conversation.class, ConversationSerializer.class);
        kryo.addDefaultSerializer(Sprite.class, SpriteSerializer.class);

        kryo.addDefaultSerializer(SceneEntity.class, SceneEntitySerializer.class);

        kryo.setReferences(true);
        Log.set(Log.LEVEL_TRACE);
        kryo.register(Color.class, new Serializer<Color>() {
            public Color read (Kryo kryo, Input input, Class<Color> type) {
                Color color = new Color();
                Color.rgba8888ToColor(color, input.readInt());
                return color;
            }

            public void write (Kryo kryo, Output output, Color color) {
                output.writeInt(Color.rgba8888(color));
            }
        });
        /*
        TestClass test = new TestClass();
        test.test = "Hello world!";
        Output o = new Output(Gdx.files.local("saveloadtest.sav").write(false));
        kryo.writeObject(o, test);
        o.close();
        */
        /*
        Input i = new Input(Gdx.files.local("saveloadtest.sav").read());
        SceneImage test = kryo.readObject(i, SceneImage.class);
        i.close();
        */
        /*
        Sprite sprite = new Sprite();
        sprite.setColor(Color.CYAN);
        sprite.setX(50);
        sprite.setY(100);
        sprite.setScale(2, 2);
        sprite.setRotation(90);
        SceneImage image = new SceneImage("Hello world instance id");
        image.setSprite(sprite);
        Output o = new Output(Gdx.files.local("saveloadtest.sav").write(false));
        kryo.writeObject(o, image);
        o.close();
        */

    }
    /** Saves all game data to the file FILEHANDLE. */
    public static void save(FileHandle fileHandle) {
        SaveData data = new SaveData();
        data.musicManager = GameManager.musicManager();
        ConversationController conversationController = GUIManager.conversationController();
        data.conversationController = conversationController;
        //LuaScriptManager sm = (LuaScriptManager) ConversationController.scriptManager("Lua");
        Map<String, ScriptManager> managers = ConversationController.allScriptManagers();
        for (String name : managers.keySet()) {
            data.scriptingVariables.put(name, managers.get(name).saveMap());
        }
        saveData(fileHandle, data);
    }
    /** Writes all data in SAVEDATA to the file FILEHANDLE. */
    private static void saveData(FileHandle fileHandle, SaveData saveData) {
        Output output = new Output(new DeflaterOutputStream(fileHandle.write(false)));
        kryo.writeObject(output, saveData);
        output.close();
    }
    /** Loads all game data from the file FILEHANDLE. */
    public static void load(FileHandle fileHandle) {
        SaveData saveData = loadData(fileHandle);
        for (String name : saveData.scriptingVariables.keySet()) {
            Map<String, Object> variables = saveData.scriptingVariables.get(name);
            ConversationController.scriptManager(name).reload(variables);
        }
    }
    /** Returns a SaveData object with the data from file FILEHANDLE. */
    public static SaveData loadData(FileHandle fileHandle) {
        Input input = new Input(new InflaterInputStream(fileHandle.read()));
        SaveData data = kryo.readObject(input, SaveData.class);
        input.close();
        return data;
    }
}
