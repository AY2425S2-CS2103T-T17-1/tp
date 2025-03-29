package seedu.address.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents the data model for the collection of previously entered user inputs.
 * Only the {@link #MAX_HISTORY_SIZE} most recently entered inputs are considered.
 * {@code InputHistory} objects with the same number and ordering of the last {@link #MAX_HISTORY_SIZE}
 * inputs are considered equal.
 */
public class InputHistory {

    /** Max number of input histories stored. Must be > 0. */
    static final int MAX_HISTORY_SIZE = 20;

    private final ObservableList<String> pastCommands;

    /**
     * Constructs an empty {@code InputHistory} object.
     */
    public InputHistory() {
        // Initialise an empty command history on load
        pastCommands = FXCollections.observableArrayList();
    }

    /**
     * Adds a new command input to the history.
     * If the history is full, the oldest command input is removed.
     *
     * @param commandInput The command input to be added.
     */
    public void addInput(String commandInput) {
        // skip adding this input if it is the same as the most recently added input.
        if (!pastCommands.isEmpty() && pastCommands.get(0).equals(commandInput)) {
            return;
        }

        if (pastCommands.size() == MAX_HISTORY_SIZE) {
            pastCommands.remove(MAX_HISTORY_SIZE - 1);
        }
        pastCommands.add(0, commandInput);
    }

    /**
     * Returns an unmodifiable list of previously entered commands.
     */
    public ObservableList<String> getPastCommands() {
        return FXCollections.unmodifiableObservableList(pastCommands);
    }

    @Override
    public int hashCode() {
        return pastCommands.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof InputHistory otherHistory)) {
            return false;
        }

        return this.pastCommands.equals(otherHistory.pastCommands);
    }

    @Override
    public String toString() {
        return this.pastCommands.toString();
    }
}
