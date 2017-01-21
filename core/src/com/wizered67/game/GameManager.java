package com.wizered67.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.wizered67.game.Inputs.MyInputProcessor;

import java.util.*;

public class GameManager {
	public static MainGame game;
	private static MyInputProcessor inputProcessor;
	public static void init(MainGame g){
		game = g;
		/* //TODO remove this entirely
		GroovyTest groovy = new GroovyTest();
		groovy.init();
		*/

	}

	public static void setMainInputProcessor(MyInputProcessor ip) {
		inputProcessor = ip;
	}

	public static MyInputProcessor getMainInputProcessor() {
		return inputProcessor;
	}

	public static Assets assetManager() {
		return game.assetManager;
	}

	public static void error(String error) {
		Gdx.app.error("ERROR", error);
	}

    public static MusicManager musicManager() {
        return game.musicManager;
    }

    /*
	public static void loadDefault() {
		AssetManager assetManager = assetManager();
		assetManager.load("Sounds/talksoundmale.wav", Sound.class);
		assetManager.load("Sounds/talksoundfemale.wav", Sound.class);
		//assetManager.load("Sounds/sfxblipmale.wav", Sound.class);
		//assetManager.load("Sounds/intense.wav", Sound.class);
		//assetManager.load("Music/crossexamination.mp3", Music.class);
		assetManager.load("Animations/Edgeworth.pack", TextureAtlas.class);
		assetManager.load("Backgrounds/testbackground.png", Texture.class);
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
	*/
}
