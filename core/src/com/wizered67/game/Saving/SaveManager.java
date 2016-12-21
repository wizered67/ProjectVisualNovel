package com.wizered67.game.Saving;

import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.SceneManager;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;

import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;


/**
 * Created by Adam on 12/20/2016.
 */
public class SaveManager {
    private static Kryo kryo = new Kryo();

    public static void save(FileHandle fileHandle) {
        SaveData data = new SaveData();
        GameManager.musicManager().save(data);
        ConversationController conversationController = GUIManager.conversationController();
        conversationController.save(data);
        conversationController.sceneManager().save(data);
        saveData(fileHandle, data);
    }

    public static void saveData(FileHandle fileHandle, SaveData saveData) {
        Output output = new Output(fileHandle.write(false));
        kryo.writeObject(output, saveData);
        output.close();
    }

    public static void load(FileHandle fileHandle) {
        SaveData saveData = loadData(fileHandle);
        saveData.musicManager.reload();
        ConversationController conversationController = GUIManager.conversationController();
        conversationController.reload(saveData);
        conversationController.sceneManager().reload(saveData);
    }

    public static SaveData loadData(FileHandle fileHandle) {
        Input input = new Input(fileHandle.read());
        SaveData data = kryo.readObject(input, SaveData.class);
        input.close();
        return data;
    }
}
