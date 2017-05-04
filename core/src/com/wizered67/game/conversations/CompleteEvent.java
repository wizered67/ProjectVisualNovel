package com.wizered67.game.conversations;

import com.wizered67.game.conversations.scene.SceneManager;

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

    /** A CompleteEvent for when input is given. */
    private static final CompleteEvent INPUT_COMPLETE_EVENT = new CompleteEvent(Type.INPUT);
    /** A CompleteEvent for when a choice is made. */
    private static final CompleteEvent CHOICE_COMPLETE_EVENT = new CompleteEvent(Type.CHOICE);
    /** A CompleteEvent for when an animation ends. */
    private static final CompleteEvent ANIMATION_END_EVENT = new CompleteEvent(Type.ANIMATION_END);
    /** A CompleteEvent for when all current text has been displayed. */
    private static final CompleteEvent TEXT_END_EVENT = new CompleteEvent(Type.TEXT);
    /** A CompleteEvent for when any Interpolation ends. */
    private static final CompleteEvent INTERPOLATION_END_EVENT = new CompleteEvent(Type.INTERPOLATION);

    public CompleteEvent(Type t) {
        this(t, null);
    }

    public CompleteEvent(Type t, Object d) {
        type = t;
        data = d;
    }

    public static CompleteEvent input() {
        return INPUT_COMPLETE_EVENT;
    }

    public static CompleteEvent choice() {
        return CHOICE_COMPLETE_EVENT;
    }

    public static CompleteEvent animationEnd(String name) {
        ANIMATION_END_EVENT.data = name;
        return ANIMATION_END_EVENT;
    }

    public static CompleteEvent text() {
        return TEXT_END_EVENT;
    }

    public static CompleteEvent interpolation(SceneManager manager, Object entity, InterpolationEventType interpolationEventType) {
        INTERPOLATION_END_EVENT.data = new Object[] { manager, entity, interpolationEventType };
        return INTERPOLATION_END_EVENT;
    }

    public enum Type {
        INPUT, CHOICE, ANIMATION_END, TEXT, INTERPOLATION
    }

    public enum InterpolationEventType {
        POSITION, ZOOM, FADE, SHAKE
    }
}
