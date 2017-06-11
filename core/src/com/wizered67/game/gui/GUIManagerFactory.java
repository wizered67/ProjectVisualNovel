package com.wizered67.game.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.ConversationController;

/**
 * Class for creating default GUIManagers.
 * @author Adam Victor
 */
public class GUIManagerFactory {
    public static GUIManager makeConversationGUIManager(Stage stage, ConversationController conversationController) {
        GUIManager guiManager = new GUIManager(stage, GameManager.skins().getConversationSkin());
        guiManager.addUIComponent(new DialogueElementsUI(guiManager, guiManager.getSkin(), conversationController));
        guiManager.addUIComponent(new TextInputUI(guiManager, guiManager.getSkin()));
        guiManager.addUIComponent(new TranscriptUI(guiManager, guiManager.getSkin(), conversationController));
        guiManager.addUIComponent(new DebugMenuUI(guiManager, guiManager.getSkin(), conversationController));
        return guiManager;
    }
}
