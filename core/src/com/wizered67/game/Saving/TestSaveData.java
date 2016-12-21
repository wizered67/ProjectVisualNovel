package com.wizered67.game.Saving;

import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GUI.Conversations.Commands.ChangeBranchCommand;
import com.wizered67.game.GUI.Conversations.Commands.ShowChoicesCommand;

import java.util.List;
import java.util.Map;

/**
 * Created by Adam on 12/20/2016.
 */
public class TestSaveData extends SaveData {
    public int num;
    public String string;
    public float decimal;
    public List<String> list;
    public Map<String, Integer> map;
    public ChangeBranchCommand cbCommand;
    public ShowChoicesCommand scCommand;
    public Vector2 vector;
}
