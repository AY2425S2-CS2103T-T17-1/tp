package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's note in the address book.
 * Guarantees: immutable
 */
public class Note {

    public static final String MESSAGE_CONSTRAINTS = "Notes can take any values, and it should not be blank";

    public static final String BLANK_NOTE_VALUE = "<Blank Note>";

    public static final String DEFAULT_NOTE_VALUE = "";

    public final String value;

    /**
     * Constructs a {@code Note}.
     *
     * @param note A valid note.
     */
    public Note(String note) {
        requireNonNull(note);
        value = note;
    }

    /**
     * Returns true if the given note is blank,
     * which means its string value only consists of white strings or is empty.
     */
    public boolean isBlank() {
        return value.isBlank();
    }

    @Override
    public String toString() {
        // 'this' is used here to make it clear its calling on this note.
        if (this.isBlank()) {
            return BLANK_NOTE_VALUE;
        }

        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Note)) {
            return false;
        }

        Note otherNote = (Note) other;
        return value.equals(otherNote.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static Note createDefaultNote() {
        return new Note(DEFAULT_NOTE_VALUE);
    }

}
