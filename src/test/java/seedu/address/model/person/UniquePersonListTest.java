package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.ISABELLE;
import static seedu.address.testutil.TypicalPersons.RACHEL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.PersonBuilder;

public class UniquePersonListTest {

    private final UniquePersonList uniquePersonList = new UniquePersonList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        assertTrue(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniquePersonList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.add(ALICE));
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPerson(null, ALICE));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPerson(ALICE, null));
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.setPerson(ALICE, ALICE));
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(ALICE);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasSameIdentity_success() {
        uniquePersonList.add(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniquePersonList.setPerson(ALICE, editedAlice);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(editedAlice);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, BOB);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.setPerson(ALICE, BOB));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.remove(ALICE));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniquePersonList.add(ALICE);
        uniquePersonList.remove(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPersons((UniquePersonList) null));
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniquePersonList.add(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        uniquePersonList.setPersons(expectedUniquePersonList);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPersons((List<Person>) null));
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniquePersonList.add(ALICE);
        List<Person> personList = Collections.singletonList(BOB);
        uniquePersonList.setPersons(personList);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Person> listWithDuplicatePersons = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.setPersons(listWithDuplicatePersons));
    }

    @Test
    public void pinPerson_existingPerson_movesToFirstPosition() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        uniquePersonList.pinPerson(BOB);
        assertEquals(BOB, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void pinPerson_nonExistentPerson_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.pinPerson(ALICE));
    }

    @Test
    public void unpinPerson_existingPinnedPerson_success() {
        Person pinnedBob = new PersonBuilder(BOB).withPin(true).build();

        uniquePersonList.add(ALICE);
        uniquePersonList.add(pinnedBob);

        uniquePersonList.unpinPerson(pinnedBob);
        uniquePersonList.sortBy("n/");
        assertEquals(ALICE, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void unpinPerson_personNotInList_throwsPersonNotFoundException() {
        Person notAdded = new PersonBuilder(BOB).withPin(false).build();
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.unpinPerson(notAdded));
    }

    @Test
    public void prioritisePins_mixedPins_reordersCorrectly() {
        Person unpinnedBob = new PersonBuilder(BOB).withPin(false).build();
        Person pinnedBob = new PersonBuilder(BOB).withPin(true).build();
        Person unpinnedAlice = new PersonBuilder(ALICE).withPin(false).build();

        uniquePersonList.add(unpinnedAlice);
        uniquePersonList.add(unpinnedBob);
        uniquePersonList.pinPerson(unpinnedBob);

        uniquePersonList.prioritisePins();

        assertEquals(pinnedBob, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniquePersonList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniquePersonList.asUnmodifiableObservableList().toString(), uniquePersonList.toString());
    }

    @Test
    public void sortBy_namePrefix_sortsByName() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        uniquePersonList.sortBy("n/");
        assertEquals(ALICE, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void sortBy_phonePrefix_sortsByPhone() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        uniquePersonList.sortBy("p/");
        assertEquals(BOB, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void sortBy_phonePrefix_sortsByLongPhoneNumber() {
        uniquePersonList.add(RACHEL);
        uniquePersonList.add(ISABELLE);
        uniquePersonList.sortBy("p/");
        assertEquals(ISABELLE, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void sortBy_emailPrefix_sortsByEmail() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        uniquePersonList.sortBy("e/");
        assertEquals(ALICE, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void sortBy_addressPrefix_sortsByAddress() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        uniquePersonList.sortBy("a/");
        assertEquals(ALICE, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void sortBy_tagsPrefix_sortsByTags() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        uniquePersonList.sortBy("t/");
        assertEquals(BOB, uniquePersonList.asUnmodifiableObservableList().get(0));
    }

    @Test
    public void sortBy_invalidPrefix_doesNothing() {
        uniquePersonList.add(BOB);
        uniquePersonList.add(ALICE);
        uniquePersonList.sortBy("invalidPrefix");
        assertEquals(BOB, uniquePersonList.asUnmodifiableObservableList().get(0));
    }
}
