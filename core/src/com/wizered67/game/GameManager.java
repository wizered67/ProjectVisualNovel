package com.wizered67.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.wizered67.game.GUI.Conversations.Commands.*;
import com.wizered67.game.Inputs.MyInputProcessor;
import com.wizered67.game.Saving.SaveData;
import com.wizered67.game.Saving.TestSaveData;
import com.wizered67.game.Saving.SaveManager;
import com.wizered67.game.Scripting.GroovyTest;

import java.util.*;

public class GameManager {
	public static MainGame game;
	private static MyInputProcessor inputProcessor;
	public static void init(MainGame g){
		game = g;
		/* //TODO remove this entirely
		LuaExecutor lua = new LuaExecutor();
		lua.init();
		*/
		GroovyTest groovy = new GroovyTest();
		groovy.init();
		//TODO remove this test
        /*
		TestSaveData data = new TestSaveData();
		data.decimal = 4f;
		data.num = 1;
		data.string = "hola";
		data.list = new ArrayList<String>();
		data.list.add("Aloha, ");
		data.list.add("World.");
		data.map = new HashMap<String, Integer>();
		data.map.put("One", 1);
		data.map.put("Two", 2);
		data.map.put("Three", 3);
        data.cbCommand = new ChangeBranchCommand("main branch");
        data.vector = new Vector2(5, 3);
        String[] text = new String[] {"Choice One", "Choice Two", "Choice Three"};
        List<ConversationCommand>[] commands = new ArrayList[3];
        List<ConversationCommand> c0 = new ArrayList<ConversationCommand>();
        c0.add(new CharacterAddCommand("Adam", "Adam", "Sound"));
        List<ConversationCommand> c1 = new ArrayList<ConversationCommand>();
        c1.add(new CharacterAnimationCommand("Adam", "Idle", true));
        List<ConversationCommand> c2 = new ArrayList<ConversationCommand>();
        c2.add(new PlayMusicCommand("Music", false));
        commands[0] = c0;
        commands[1] = c1;
        commands[2] = c2;
        VariableConditionCommand[] cond = new VariableConditionCommand[3];
        data.scCommand = new ShowChoicesCommand(text, commands, cond);
		SaveManager.saveData(Gdx.files.local("Saves/test1.bin"), data);
		SaveData test = SaveManager.loadData(Gdx.files.local("Saves/test1.bin"));
		*/
	}

	public static void setMainInputProcessor(MyInputProcessor ip) {
		inputProcessor = ip;
	}

	public static MyInputProcessor getMainInputProcessor() {
		return inputProcessor;
	}

	public static AssetManager assetManager() {
		return game.assetManager;
	}

    public static Map<String, Map<String, Animation>> loadedAnimations() {
        return game.loadedAnimations;
    }

	public static void error(String error) {
		Gdx.app.error("ERROR", error);
	}

    public static MusicManager musicManager() {
        return game.musicManager;
    }

	public static void loadDefault() {
		AssetManager assetManager = assetManager();
		assetManager.load("Sounds/talksoundmale.wav", Sound.class);
		assetManager.load("Sounds/talksoundfemale.wav", Sound.class);
		assetManager.load("Sounds/sfxblipmale.wav", Sound.class);
		assetManager.load("Sounds/intense.wav", Sound.class);
		assetManager.load("Music/crossexamination.mp3", Music.class);
		assetManager.load("Animations/Edgeworth.pack", TextureAtlas.class);
	}

	public static void loadAnimations() {
		AssetManager assetManager = assetManager();
		TextureAtlas atlas = assetManager.get("Animations/Edgeworth.pack", TextureAtlas.class);
		Array<TextureAtlas.AtlasRegion> think = atlas.findRegions("Edgeworth");
		Array<TextureAtlas.AtlasRegion> idle = atlas.findRegions("Static");
		Array<TextureAtlas.AtlasRegion> confront = atlas.findRegions("Confront");
		Array<TextureAtlas.AtlasRegion> point = atlas.findRegions("Point");
		Array<TextureAtlas.AtlasRegion> accuse = atlas.findRegions("Accuse");
		Animation thinkAnim = new Animation(0.3f, think);
		Animation idleAnim = new Animation(1, idle);
		Animation confrontAnim = new Animation(1, confront);
		Animation pointAnim = new Animation(1, point);
		Animation accuseAnim = new Animation(0.05f, accuse);
		HashMap<String, Animation> edgeworthAnimations = new HashMap<String, Animation>();
		edgeworthAnimations.put("Think", thinkAnim);
		edgeworthAnimations.put("Idle", idleAnim);
		edgeworthAnimations.put("Confront", confrontAnim);
		edgeworthAnimations.put("Point", pointAnim);
		edgeworthAnimations.put("Accuse", accuseAnim);
		loadedAnimations().put("Edgeworth", edgeworthAnimations);
	}
}
