package com.wizered67.game.GUI.Conversations;

/**
 * Created by Adam on 10/27/2016.
 */
public class CompleteEvent {
    public Object data;
    public Type type;


    public CompleteEvent(Type t) {
        this(t, null);
    }

    public CompleteEvent(Type t, Object d) {
        type = t;
        data = d;
    }

    public enum Type {
        INPUT, CHOICE, ANIMATION_END
    }
}
