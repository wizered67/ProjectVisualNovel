package com.wizered67.game.Saving.Serializers;

/**
 * Stores states of GUI elements to be saved and loaded later.
 * @author Adam Victor
 */
public class GUIState {
    /** The current text of the speaker label. */
    public String speakerLabelText;
    /** The current text of the textbox label. */
    public String textboxLabelText;
    /** Whether the speaker label is visible. */
    public boolean speakerLabelVisible;
    /** Whether the textbox label is visible. */
    public boolean textboxLabelVisible;
    /** Array of the text on each choice button. */
    public String[] choiceButtonText;
}
