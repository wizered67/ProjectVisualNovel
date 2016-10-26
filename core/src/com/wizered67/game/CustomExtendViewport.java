package com.wizered67.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CustomExtendViewport extends Viewport {
	private float minWorldWidth, minWorldHeight;
	private float maxWorldWidth, maxWorldHeight;
	private float scale = 0;
	/** Creates a new viewport using a new {@link OrthographicCamera} with no maximum world size. */
	public CustomExtendViewport(float minWorldWidth, float minWorldHeight) {
		this(minWorldWidth, minWorldHeight, 0, 0, new OrthographicCamera());
	}

	/** Creates a new viewport with no maximum world size. */
	public CustomExtendViewport(float minWorldWidth, float minWorldHeight, Camera camera) {
		this(minWorldWidth, minWorldHeight, 0, 0, camera);
	}

	/** Creates a new viewport using a new {@link OrthographicCamera} and a maximum world size.
	 *
	 * */
	public CustomExtendViewport(float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight) {
		this(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, new OrthographicCamera());
	}

	/** Creates a new viewport with a maximum world size.
	 * @param maxWorldWidth User 0 for no maximum width.
	 * @param maxWorldHeight User 0 for no maximum height. */
	public CustomExtendViewport(float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight, Camera camera) {
		this.minWorldWidth = minWorldWidth;
		this.minWorldHeight = minWorldHeight;
		this.maxWorldWidth = maxWorldWidth;
		this.maxWorldHeight = maxWorldHeight;
		setCamera(camera);
	}

	@Override
	public void update (int screenWidth, int screenHeight, boolean centerCamera) {
		// Fit min size to the screen.
		float worldWidth = minWorldWidth;
		float worldHeight = minWorldHeight;
		Vector2 scaled = Scaling.fit.apply(worldWidth, worldHeight, screenWidth, screenHeight);
		scale = (float) Math.round(scaled.x / worldWidth);
		//System.out.println(scale);
		scaled.x = worldWidth * scale;
		scaled.y = worldHeight * scale;
		// Extend in the short direction.
		int viewportWidth = Math.round(scaled.x);
		int viewportHeight = Math.round(scaled.y);
		boolean wellScaled = false;
		while (!wellScaled){
			if (viewportWidth < screenWidth) {
				float toViewportSpace = viewportHeight / worldHeight;
				float toWorldSpace = worldHeight / viewportHeight;
				float lengthen = (screenWidth - viewportWidth) * toWorldSpace;
				if (maxWorldWidth > 0) lengthen = Math.min(lengthen, maxWorldWidth - minWorldWidth);
				worldWidth += lengthen;
				viewportWidth += Math.round(lengthen * toViewportSpace);
			}
			if (viewportHeight < screenHeight) {
				float toViewportSpace = viewportWidth / worldWidth;
				float toWorldSpace = worldWidth / viewportWidth;
				float lengthen = (screenHeight - viewportHeight) * toWorldSpace;
				if (maxWorldHeight > 0) lengthen = Math.min(lengthen, maxWorldHeight - minWorldHeight);
				worldHeight += lengthen;
				viewportHeight += Math.round(lengthen * toViewportSpace);
			}
			setWorldSize(worldWidth, worldHeight);
			if (viewportWidth < screenWidth || viewportHeight < screenHeight){
				scale += 1;
				//System.out.println("Bad Scale, new scale is: " + scale);
				scaled.x = minWorldWidth * scale;
				scaled.y = minWorldHeight * scale;
				worldWidth = minWorldWidth;
				worldHeight = minWorldHeight;
				// Extend in the short direction.
				viewportWidth = Math.round(scaled.x);
				viewportHeight = Math.round(scaled.y);
			}
			else{
				wellScaled = true;
			}
		}
		
		
		// Center.
		int oldScreenWidth = screenWidth;
		int oldScreenHeight = screenHeight;
		
		setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);
		/*
		if (getScreenX() - worldWidth / 2 < 0 || getScreenX() + worldWidth / 2 > maxWorldWidth){
			scale += 1;
			scaled.x = minWorldWidth * scale;
			scaled.y = minWorldHeight * scale;
			// Extend in the short direction.
			viewportWidth = Math.round(scaled.x);
			viewportHeight = Math.round(scaled.y);
			setScreenBounds((oldScreenWidth - viewportWidth) / 2, (oldScreenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);
		}
		*/
		//System.out.println(this.getScreenX());
		//System.out.println(this.getScreenY());
		apply(centerCamera);
	}

	public float getMinWorldWidth () {
		return minWorldWidth;
	}

	public void setMinWorldWidth (float minWorldWidth) {
		this.minWorldWidth = minWorldWidth;
	}

	public float getMinWorldHeight () {
		return minWorldHeight;
	}

	public void setMinWorldHeight (float minWorldHeight) {
		this.minWorldHeight = minWorldHeight;
	}

	public float getMaxWorldWidth () {
		return maxWorldWidth;
	}

	public void setMaxWorldWidth (float maxWorldWidth) {
		this.maxWorldWidth = maxWorldWidth;
	}

	public float getMaxWorldHeight () {
		return maxWorldHeight;
	}

	public void setMaxWorldHeight (float maxWorldHeight) {
		this.maxWorldHeight = maxWorldHeight;
	}
	
	public float getScale(){
		return scale;
	}
}