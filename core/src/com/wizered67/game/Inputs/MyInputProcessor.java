package com.wizered67.game.Inputs;

import com.badlogic.gdx.InputProcessor;
//import com.wizered67.game.Entities.Entity;

import java.util.*;


public class MyInputProcessor implements InputProcessor {
	
    private Map<Integer,InputInfo> touches = new HashMap<Integer,InputInfo>();
    private Map<Integer,Integer> touchToAction = new HashMap<Integer,Integer>();
    private Map<Integer, InputInfo> keys = new HashMap<Integer, InputInfo>();
	//private ArrayList<Entity> controllableEntities = new ArrayList<Entity>();
    private Set<Controllable> registeredControllableObjects = new HashSet<Controllable>();
    private boolean screenTouched = false;
    public MyInputProcessor(){
    	for(int i = 0; i < 5; i++){
            touches.put(i, new InputInfo());
            touchToAction.put(i, null);
        }
    }

    public void register(Controllable c) {
        if (!registeredControllableObjects.contains(c)) {
            registeredControllableObjects.add(c);
        }
    }
    /*
    public void addControllableEntity(Entity e){
		controllableEntities.add(e);
	}
	*/
    
    public void update(){
    	for (InputInfo input : keys.values()){
    		if (input != null){
    			input.justTouched = false;
    		}
    	}
    	for (InputInfo input : touches.values()){
    		if (input != null){
    			input.justTouched = false;
    		}
    	}
    }

	private void setInput(int index, InputInfo result){
		/*
		for (Entity e : controllableEntities){
			if (e != null && !e.getDestroyed()) {e.setInput(index, result); }
		}
		*/
	}

    @Override
	public boolean keyDown(int keycode) {
    	System.out.println("Pressed: " + keycode);
		if (!keys.containsKey(keycode) || keys.get(keycode) == null){
			InputInfo input = new InputInfo();
			input.justTouched = true;
			input.touched = true;
			keys.put(keycode, input);
			//System.out.println("New Key");
		}
		setInput(keycode, keys.get(keycode));
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		//System.out.println("Released: " + keycode);
		if (keys.containsKey(keycode) && keys.get(keycode) != null){
			keys.get(keycode).touched = false;
			keys.get(keycode).justTouched = false;
			keys.put(keycode, null);
			setInput(keycode, null);
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean justPressed = !screenTouched;
        screenTouched = true;
        for (Controllable c : registeredControllableObjects) {
            c.touchDown(screenX, screenY, pointer, button, justPressed);
        }
		/*
		if(pointer < 5){
			InputInfo touch = touches.get(pointer);
            touch.touchX = screenX;
            touch.touchY = screenY;
            if (!touch.touched){
            	touch.justTouched = true;
            }
            touch.touched = true;
            
            if (screenX < Gdx.graphics.getWidth() / 2 && screenY > Gdx.graphics.getHeight() / 2){
            	setInput(Input.Keys.LEFT, touch);
            	touchToAction.put(pointer, Input.Keys.LEFT);
            }
            if (screenX > Gdx.graphics.getWidth() / 2 && screenY > Gdx.graphics.getHeight() / 2){
            	setInput(Input.Keys.RIGHT, touch);
            	touchToAction.put(pointer, Input.Keys.RIGHT);
            }
            
            if (screenY < Gdx.graphics.getHeight() / 2){
            	setInput(Input.Keys.UP, touch);
            	touchToAction.put(pointer, Input.Keys.UP);
            }
            
        }
		*/
        return true;

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenTouched = false;
		/*
		if(pointer < 5){
			InputInfo touch = touches.get(pointer);
            touch.touchX = 0;
            touch.touchY = 0;
            touch.touched = false;
            touch.justTouched = false;
            
            if (touchToAction.get(pointer) != null){
            	setInput(touchToAction.get(pointer), null);
            }
        }
        */
        return true;
	}
	

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
