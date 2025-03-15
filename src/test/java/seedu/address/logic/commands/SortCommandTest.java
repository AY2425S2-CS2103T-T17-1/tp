package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code SortCommand}.
 */
public class SortCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        // Create a new AddressBook and ModelManager instance for each test
        AddressBook addressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();
        model = new ModelManager(addressBook, userPrefs);
    }

    @Test
    public void execute_sortByName_success() throws DuplicatePersonException {
        model.addPerson(BENSON);
        model.addPerson(ALICE);
        model.addPerson(ELLE);
        model.addPerson(DANIEL);
        model.addPerson(CARL);
        SortCommand sortCommand = new SortCommand("n/");
        sortCommand.execute(model);

        // Check if the persons in AddressBook are sorted correctly by name
        assertEquals("Alice Pauline", model.getFilteredPersonList().get(0).getName().toString());
        assertEquals("Benson Meier", model.getFilteredPersonList().get(1).getName().toString());
        assertEquals("Carl Kurz", model.getFilteredPersonList().get(2).getName().toString());
        assertEquals("Daniel Meier", model.getFilteredPersonList().get(3).getName().toString());
        assertEquals("Elle Meyer", model.getFilteredPersonList().get(4).getName().toString());
    }

    @Test
    public void execute_sortByPhone_success() throws DuplicatePersonException {
        model.addPerson(BENSON);
        model.addPerson(ALICE);
        model.addPerson(ELLE);
        model.addPerson(DANIEL);
        model.addPerson(CARL);

        SortCommand sortCommand = new SortCommand("p/");
        sortCommand.execute(model);

        // Check if the persons in AddressBook are sorted correctly by phone
        assertEquals("9482224", model.getFilteredPersonList().get(0).getPhone().toString());
        assertEquals("87652533", model.getFilteredPersonList().get(1).getPhone().toString());
        assertEquals("94351253", model.getFilteredPersonList().get(2).getPhone().toString());
        assertEquals("95352563", model.getFilteredPersonList().get(3).getPhone().toString());
        assertEquals("98765432", model.getFilteredPersonList().get(4).getPhone().toString());
    }

    @Test
    public void execute_sortByEmail_success() throws DuplicatePersonException {
        model.addPerson(BENSON);
        model.addPerson(ALICE);
        model.addPerson(ELLE);
        model.addPerson(DANIEL);
        model.addPerson(CARL);

        SortCommand sortCommand = new SortCommand("e/");
        sortCommand.execute(model);

        // Check if the persons in AddressBook are sorted correctly by email
        assertEquals("alice@example.com", model.getFilteredPersonList().get(0).getEmail().toString());
        assertEquals("cornelia@example.com", model.getFilteredPersonList().get(1).getEmail().toString());
        assertEquals("heinz@example.com", model.getFilteredPersonList().get(2).getEmail().toString());
        assertEquals("johnd@example.com", model.getFilteredPersonList().get(3).getEmail().toString());
        assertEquals("werner@example.com", model.getFilteredPersonList().get(4).getEmail().toString());
    }

    @Test
    public void execute_sortByAddress_success() throws DuplicatePersonException {
        model.addPerson(BENSON);
        model.addPerson(ALICE);
        model.addPerson(ELLE);
        model.addPerson(DANIEL);
        model.addPerson(CARL);

        SortCommand sortCommand = new SortCommand("a/");
        sortCommand.execute(model);

        // Check if the persons in AddressBook are sorted correctly by address
        assertEquals("10th street", model.getFilteredPersonList().get(0).getAddress().toString());
        assertEquals("123, Jurong West Ave 6, #08-111", model.getFilteredPersonList().get(1).getAddress().toString());
        assertEquals("311, Clementi Ave 2, #02-25", model.getFilteredPersonList().get(2).getAddress().toString());
        assertEquals("michegan ave", model.getFilteredPersonList().get(3).getAddress().toString());
        assertEquals("wall street", model.getFilteredPersonList().get(4).getAddress().toString());
    }

    @Test
    public void execute_sortByTags_success() throws DuplicatePersonException {
        model.addPerson(BENSON);
        model.addPerson(ALICE);
        model.addPerson(ELLE);
        model.addPerson(DANIEL);
        model.addPerson(CARL);

        // Sort the persons by tags
        SortCommand sortCommand = new SortCommand("t/");
        sortCommand.execute(model);

        // Check if the persons are sorted correctly by tags (alphabetical order of tags)
        assertTrue(model.getFilteredPersonList().get(0).getTags().toString().contains("friends"));
        assertTrue(model.getFilteredPersonList().get(1).getTags().toString().contains("friends"));
        assertTrue(model.getFilteredPersonList().get(2).getTags().toString().contains("friends"));
        assertTrue(model.getFilteredPersonList().get(2).getTags().toString().contains("owesMoney"));
        assertTrue(model.getFilteredPersonList().get(3).getTags().isEmpty());
        assertTrue(model.getFilteredPersonList().get(4).getTags().isEmpty());
    }
}
