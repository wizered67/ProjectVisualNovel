package com.wizered67.game.GUI.Conversations;

/** Contains information to be passed to the current ConversationCommand
 * about an event that has occurred that could lead to its completion.
 * Contains a TYPE enum and some DATA, if extra information is necessary,
 * such as what choice a player selected.
 * @author Adam Victor
 */
public class CompleteEvent {
    /** The data specific to this CompleteEvent, providing additional information. */
    public Object data;
    /** The type of event that has occurred. */
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
