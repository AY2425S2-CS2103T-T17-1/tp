package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InputHistoryTest {
    private InputHistory emptyInputHistory;

    @BeforeEach
    public void setUp() {
        emptyInputHistory = new InputHistory();
    }

    private InputHistory getInputHistoryOfSize(int size) {
        InputHistory inputHistory = new InputHistory();
        assertTrue(size > 0 && size <= InputHistory.MAX_HISTORY_SIZE);
        for (int i = 1; i <= size; i++) {
            inputHistory.addInput("input " + i);
        }
        return inputHistory;
    }

    @Test
    public void maxHistorySize_isPositiveInteger() {
        assertTrue(InputHistory.MAX_HISTORY_SIZE > 0);
    }

    @Test
    public void addInput_addInputWhenFull_success() {
        for (int i = 0; i < InputHistory.MAX_HISTORY_SIZE; i++) {
            emptyInputHistory.addInput("input " + i);
        }
        ObservableList<String> previousCommands = emptyInputHistory.getPastCommands();
        assertEquals(20, previousCommands.size());
        assertEquals("input 19", previousCommands.get(0));

        emptyInputHistory.addInput("input 20");
        assertEquals("input 20", previousCommands.get(0));
        assertEquals("input 1", previousCommands.get(19));
    }

    @Test
    public void addInput_duplicateInputsFullHistory_ignored() {
        final String duplicateMessage = "input 20";

        ObservableList<String> expectedInputList = FXCollections.observableArrayList();
        InputHistory actualFullInputHistory = new InputHistory();
        for (int i = 1; i <= InputHistory.MAX_HISTORY_SIZE; i++) {
            expectedInputList.add(0, "input " + i);
            actualFullInputHistory.addInput("input " + i);
        }
        // Adding into a full input history
        actualFullInputHistory.addInput(duplicateMessage);
        assertEquals(expectedInputList, actualFullInputHistory.getPastCommands());

        actualFullInputHistory.addInput(new String(duplicateMessage));
        assertEquals(expectedInputList, actualFullInputHistory.getPastCommands());
    }

    @Test
    public void addInput_duplicateInputsEmptyHistory_ignored() {
        final String duplicateMessage = "add 1 n/123";
        // Adding duplicates into an empty input history
        InputHistory expectedInputHistory = new InputHistory();
        expectedInputHistory.addInput(duplicateMessage);

        emptyInputHistory.addInput(duplicateMessage);

        // Add duplicates
        emptyInputHistory.addInput(duplicateMessage);
        assertEquals(expectedInputHistory, emptyInputHistory);
        // check that .equals() equality is actually maintained
        emptyInputHistory.addInput(new String(duplicateMessage));
        assertEquals(expectedInputHistory, emptyInputHistory);
    }

    @Test
    public void addInput_duplicateInputsNonEmptyNonFullHistory_ignored() {
        // Adding duplicates into a non-empty, non-full input history
        final String duplicateMessage = "input 5";
        final int nonEmptyNonFullInputSize = 5;

        InputHistory nonFullInputHistory = new InputHistory();
        InputHistory expectedNonFullInputHistory = new InputHistory();
        for (int i = 1; i <= nonEmptyNonFullInputSize; i++) {
            nonFullInputHistory.addInput("input " + i);
            expectedNonFullInputHistory.addInput("input " + i);
        }

        // Add duplicates
        nonFullInputHistory.addInput(duplicateMessage);
        assertEquals(expectedNonFullInputHistory, nonFullInputHistory);

        // check that .equals() equality is actually maintained
        nonFullInputHistory.addInput(new String(duplicateMessage));
        assertEquals(expectedNonFullInputHistory, nonFullInputHistory);
    }

    @Test
    public void equals() {
        InputHistory inputHistory = new InputHistory();
        // same values -> returns true
        assertTrue(inputHistory.equals(emptyInputHistory));

        // same object -> returns true
        assertTrue(inputHistory.equals(inputHistory));

        // null -> returns false
        assertFalse(inputHistory.equals(null));

        // different types -> returns false
        assertFalse(inputHistory.equals(5.0f));

        // different input containers
        inputHistory = getInputHistoryOfSize(3);
        assertFalse(inputHistory.equals(emptyInputHistory));

        InputHistory otherInputHistory = getInputHistoryOfSize(3);
        // same non-empty values
        assertTrue(inputHistory.equals(otherInputHistory));
    }

    @Test
    public void testHashCode() {
        InputHistory inputHistory = new InputHistory();
        // same values -> returns same hashcode
        assertEquals(inputHistory.hashCode(), emptyInputHistory.hashCode());

        // same object -> returns same hashcode
        assertEquals(inputHistory.hashCode(), inputHistory.hashCode());

        // different types -> returns false
        assertNotEquals(inputHistory.hashCode(), 5.0f);

        // different input containers
        inputHistory = getInputHistoryOfSize(3);
        assertNotEquals(inputHistory.hashCode(), emptyInputHistory.hashCode());

        InputHistory otherInputHistory = getInputHistoryOfSize(3);
        // same non-empty values
        assertEquals(inputHistory.hashCode(), otherInputHistory.hashCode());
    }

    @Test
    public void testToString() {
        // empty histories have the same string representation
        assertEquals(emptyInputHistory.toString(), new InputHistory().toString());

        // non-empty histories with the same inputs have the same string representations
        assertEquals(getInputHistoryOfSize(3), getInputHistoryOfSize(3));

        // non-empty histories with different histories have different string representations
        InputHistory inputHistory = getInputHistoryOfSize(3);
        InputHistory otherHistory = getInputHistoryOfSize(4);
        assertNotEquals(inputHistory.toString(), otherHistory.toString());

        // same full histories have same representations
        assertEquals(
                getInputHistoryOfSize(InputHistory.MAX_HISTORY_SIZE).toString(),
                getInputHistoryOfSize(InputHistory.MAX_HISTORY_SIZE).toString());
    }
}
