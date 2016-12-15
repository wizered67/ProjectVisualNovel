package com.wizered67.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisTable;


/**
 * Created by Adam on 11/17/2016.
 */
public class Editor extends ApplicationAdapter {
    private Stage stage;
    private VisTable uiRoot;
    private Viewport stageViewport;

    @Override
    public void create() {
        VisUI.load();
        //VisUI.setDefaultTitleAlign(Align.center);
        stage = createStage();
        Gdx.input.setInputProcessor(stage);

        uiRoot = new VisTable();
        uiRoot.setFillParent(true);

        stage.addActor(uiRoot);

        createUI();

    }

    private Stage createStage () {
        stageViewport = new ScreenViewport();
        Stage stage = new Stage(stageViewport);
        return stage;
    }

    private void createUI() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Test Menu");
        MenuItem menuItem = new MenuItem("Test Item");
        MenuItem item2 = new MenuItem("Test Item 2");
        Menu subMenu = new Menu("Submenu");
        subMenu.addItem(new MenuItem("Test"));
        item2.setSubMenu(subMenu);
        menu.addItem(menuItem);
        menu.addSeparator();
        menu.addItem(item2);
        menuBar.addMenu(menu);
        uiRoot.add(menuBar.getTable()).fillX().expandX().row();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }
}
