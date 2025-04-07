---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* Notarius is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org/).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-T17-1/tp/blob/4923ae7bb48c7905383bca81cd029679f89ad40e/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-T17-1/tp/blob/4923ae7bb48c7905383bca81cd029679f89ad40e/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-T17-1/tp/blob/3f2520c27a52b761b7c2310023814f52366f640c/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, 
`PersonDetailPanel`, `StatusBarFooter` and `HelpWindow`. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-T17-1/tp/blob/3f2520c27a52b761b7c2310023814f52366f640c/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-T17-1/tp/blob/3f2520c27a52b761b7c2310023814f52366f640c/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-T17-1/tp/blob/3f2520c27a52b761b7c2310023814f52366f640c/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-T17-1/tp/blob/3f2520c27a52b761b7c2310023814f52366f640c/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores states of the model in the ModelState in order to undo/redo the states.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-T17-1/tp/blob/3f2520c27a52b761b7c2310023814f52366f640c/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### List feature

The `list` command enables users to view all existing contacts from Notarius.

<p align="center">
  <img src="images/ListCommandSequenceDiagram.png" alt="Ui" />
</p>

#### Implementation Details

1. The user inputs the command to list all contacts.
2. A `LogicManager` object invokes the `execute` method of a `ListCommand` object.
3. The `execute` method of the `ListCommand` object invokes the `updateFilteredPersonList` and
`commit` method of its `Model` to update and show all contacts.
4. The `execute` method of the `ListCommand` object returns a `CommandResult` object which stores the data regarding
the completion of the `list` command.

### Help feature

The `help` command allows users to view general application usage instructions or specific details about a command.

<p align="center">
  <img src="images/HelpCommandSequenceDiagram.png" alt="Ui" />
</p>
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `HelpCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of the diagram.

</div>

#### Implementation Details

1. The user inputs the command to request help, either as help for general help or help COMMAND_NAME for details on a specific command. 
2. A `HelpCommandParser` object invokes its `parse` method, which parses the user input:
   1. If no argument is provided, it returns a `HelpCommand` object for general help. 
   2. If a valid command name is provided, it returns a `HelpCommand` object with that command name. 
3. A `LogicManager` object invokes the `execute` method of the `HelpCommand` object. 
4. The execute method of the HelpCommand object checks if a command name was specified:
   1. If no command name is provided, it returns a `CommandResult` containing general help instructions. 
   2. If a valid command name is provided, it retrieves the corresponding help message from a predefined command-help mapping (`COMMAND_HELP`). 
   3. If an invalid command name is provided, it returns an error message stating that the command is unknown. 
5. The `execute` method of the `HelpCommand` object returns a `CommandResult` object that stores the help message or error message. 
6. The application displays the help message to the user, either in a popup window (for general help) or in the main interface (for specific commands).

### Clear feature

The `clear` command enables users to remove all existing contacts from Notarius.

<p align="center">
  <img src="images/ClearCommandSequenceDiagram.png" alt="Ui" />
</p>

#### Implementation Details

1. The user inputs the command to clear all contacts.
2. A `LogicManager` object invokes the `execute` method of a `ClearCommand` object.
3. The `execute` method of the `ClearCommand` object invokes the `setAddressBook` and `commit` method 
of its `Model` argument with a new `AddressBook` object which contains an empty `UniquePersonList` property.
4. The `execute` method of the `ClearCommand` object returns a `CommandResult` object which stores the data regarding 
the completion of the `clear` command.

### Sort feature

The `sort` command enables users to sort contacts in Notarius by prefix in lexicographical order.

<p align="center">
  <img src="images/SortCommandSequenceDiagram.png" alt="Ui" />
</p>
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `SortCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of the diagram.

</div>

#### Implementation Details

1. The user inputs the command to sort contacts with the specified prefix.
2. A `SortCommandParser` object invokes its `parse` method which parses the user input.
3. The `SortCommand` object is created with the parsed prefix.
4. A `LogicManager` object invokes the `execute` method of the `SortCommand` object.
5. The `execute` method of the `SortCommand` object invokes the `updateSortedPersonList`, 
`updateSortedFilteredPersonList` and `commit` methods of its `Model` argument to update and sort 
all contacts by the target prefix.
6. The `execute` method of the `SortCommand` object returns a `CommandResult` object which stores the data regarding 
the completion of the `sort` command.

#### Design Considerations
**Aspect: Determining the level of multi-attributes sorting for contacts**
* **Alternative 1 (current choice):** Support multi-level sorting for up to two attributes, with tags as first attribute
  * Pros: It is easier for users to understand and use. Sorting by tags first allows contacts to be grouped into respective clusters, making it easier to find related contacts.
  * Cons: For users with complex needs, limiting sorting to two attributes may be restrictive. They may need the ability to sort by more attributes to meet their requirements.

* **Alternative 2:** Support multi-level sorting for more than two attributes
  * Pros: Advanced users with specific needs (e.g., sorting by tag, then name, then phone number) can get more control and precision in how they organize their contacts.
  * Cons: It might slow down the system, especially if the dataset is large or if sorting requires a lot of computation. 

### Find feature

The `find` command allows users to search for contacts in Notarius based on specified fields: 
name, phone, email, address, and tags. The search is case-insensitive and supports multiple keywords. 
Additionally, for name, email, and address fields, the search is tolerant of minor typos, allowing matches within a Levenshtein distance of 2.

<p align="center">
  <img src="images/FindCommandSequenceDiagram.png" alt="Ui" />
</p>
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `FindCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of the diagram.

</div>

#### Implementation Details

1. The user enters the `find` command with the desired search criteria. 
2. The `LogicManager` invokes the parseCommand method of AddressBookParser to identify the command type. 
3. If the command is recognized as `find`, the `FindCommandParser` is instantiated. 
4. The `FindCommandParser` extracts the search parameters and keywords, ensuring correct parsing of multi-word inputs enclosed in double quotes (""). 
5. A new `FindCommand` object is created using the parsed search fields and keywords. 
6. The `LogicManager` executes the `FindCommand` object, which:
   1. Calls `updateFilteredPersonList` and `commit` method in `Model` to filter contacts based on the search fields and keywords. 
   2. Uses a case-insensitive check for all fields. 
   3. Applies Levenshtein distance ≤ 2 matching for `name`, `email`, and `address`. 
7. The `FindCommand` returns a `CommandResult`, displaying the filtered list of contacts matching the search criteria.

#### Design Considerations

**Aspect: Lenient Matching Using Levenshtein Distance**
* **Alternative 1 (Current choice):** Apply typo-tolerant search (Levenshtein distance) to selected fields (`name`, `email`, `address`)<br>
  * Pros: 
    * Enhances usability by allowing users to find contacts even with small typing mistakes. 
    * Applied only to fields where user errors are common, maintaining accuracy for fields like `phone`.
  * Cons:
    * May return unexpected matches (e.g. searching `find 10` returning `David Li`).
    * Slightly increases computational complexity of the search.<br>
 
* **Alternative 2:** Enforce strict, exact-match search for all fields<br>
  * Pros: 
    * Predictable and unambiguous results.
    * Simpler and faster to implement.
  * Cons:
    * Less forgiving for users who make typos.
    * May frustrate users if exact spelling is unknown.<br><br>


**Aspect: Default Field Matching When No Prefix is Provided**
* **Alternative 1 (Current choice):** Assume `name` search when no prefix is specified<br>
  * Pros: 
    * Streamlines the common use case, since most users search by `name`. 
    * Reduces typing effort and improves convenience.
  * Cons: 
    * May confuse users who expect exact prefix-based behavior.<br>
    
* **Alternative 2:** Require prefix for all searches (e.g. `find n/"Al"`)
  * Pros: 
    * Unambiguous behavior, clear field targeting. 
    * Reduces risk of misinterpreted input.
  * Cons:
    * Increases command verbosity. 
    * Less convenient for users performing quick name searches.

### Add feature

The `add` command allows the user to add a new client contact into Notarius.

The sequence diagram below models the different components of the application involved when the user
executes the `add` command.
![AddCommandSequenceDiagram](images/AddCommandSequenceDiagram.png)
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `AddCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of the diagram.

</div>

#### Implementation Details

1. The user executes a command to add a contact by specifying the contact's details.
2. The `LogicManager` receives the command and calls `AddressBookParser#parseCommand` to parse the command.
3. The `AddressBookParser` creates an `AddCommandParser` object, and calls its `parse` method.
4. Within the `AddCommandParser#parse` method, the command format is checked to ensure that the required prefixes are present and valid.
5. The `parse` method of `AddCommandParser` will then create and return a new `AddCommand` object with the parsed details of the new contact to be added.
6. `LogicManager` calls the `execute` method of the `AddCommand` object.
7. The `AddCommand` object calls the `Model#addPerson` method to add the new contact to the list of persons in Notarius.

#### Usage Examples

1. The user starts Notarius
2. The user enters the input `add n/Notarius Law p/98765432 e/email@example.com a/Blk 123 #01-01`
3. If the email does not already exist, the client contact is created with the name `Notarius Law`, and reflected in the list of client contacts in Notarius.

### Delete feature

The `delete` command allows the user to delete specified client contact(s) from Notarius.

The sequence diagram below models the different components of the application that are involved
when the user executes the `delete` command.
![DeleteSequenceDiagram](images/DeleteSequenceDiagram.png)
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of the diagram.

</div>

#### Implementation Details

1. The user executes a command to delete a contact by specifying the contact's index.
2. The `LogicManager` receives the command and calls `AddressBookParser#parseCommand` to parse the command.
3. The `AddressBookParser` creates a `DeleteCommandParser` object, which creates a `MassOpsIndexParser` object for parsing multiple indexes.
4. The `AddressBookParser` then calls the `parse` method of the newly created `DeleteCommandParser` object.
5. If the delete method specifies the `i/` prefix with the corresponding value being either a ranged format (`startIndex-endIndex`) or spaced format (`index1 index2 ... indexN`), the `parse` method of `MassOpsIndexParser` will be called to parse the indexes.
6. A new `DeleteCommand` object is created with the returned set of unique indexes from the parser, and gets returned back to the `LogicManager`.
7. The `LogicManager` calls the `execute` method of the `DeleteCommand` object.
8. The `DeleteCommand` object calls the `Model#deletePerson` method for each of the set of unique indexes in decreasing order of the index's `zeroBasedIndex`.
9. After all `indexes` specified have been deleted, `Model#commit` is called on the `Model` argument to save the changes made to the list of persons in the addressbook.

#### Usage Examples

1. User starts Notarius
2. User executes `delete i/1-3`
3. The client contacts with indexes 1, 2 and 3 will be deleted from Notarius. This change should be reflected in the client contact list.


#### Design Considerations

**Aspect: How to implement the delete command**

* **Alternative 1 (current choice):** Support deletion of multiple client contacts with intuitive delete formats
    * Pros: Intuitive to use, and improved user-friendliness. Users do not have to repeatedly type the same command to delete clients one-by-one.
    * Cons: More complicated to implement, due to the need of parsing multiple indexes, ignoring duplicates, and ensuring valid parsing according to the multiple specified formats.

* **Alternative 2:** Support deletion of only one client contact at a time using a single format
    * Pros: Simpler to implement, as the command will only need to parse one index.
    * Cons: Less user-friendly, as users will have to spend more time and trouble to repeatedly type the same command to delete potentially many clients one-by-one.


### Edit feature

The `edit` command allows users to update one or more fields of a contact in Notarius.

<p align="center"> <img src="images/EditSequenceDiagram.png" alt="EditCommand Sequence Diagram" /> </p> <div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `EditCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.</div>

#### Implementation Details

1. The user inputs the `edit` command along with the index of the contact to edit, followed by any number of fields to update.
2. The `LogicManager` receives the command and calls `AddressBookParser#parseCommand`.
3. The `AddressBookParser` creates an `EditCommandParser` object and calls its `parse` method.
4. The `EditCommandParser` parses the index and input fields, then creates an `EditCommand` with an `EditPersonDescriptor` holding the new field values.
5. The `LogicManager` executes the `EditCommand`, which:
    - Retrieves the list of filtered persons via `Model#getFilteredPersonList()`.
    - Validates the provided index.
    - Creates an updated `Person` using `EditPersonDescriptor#createEditedPerson()`.
    - Replaces the existing person with the edited one via `Model#setPerson()`.
    - Calls `Model#updateFilteredPersonList()` and `Model#commit()` to apply and save the changes.
6. A `CommandResult` is returned to the `LogicManager`, which then displays a confirmation message.

#### Usage Examples

1. `edit 1 p/91234567 e/john@example.com`  
   _Updates the phone number and email of the first contact._

2. `edit 3 n/Alex Tan t/client`  
   _Updates the name and tags of the third contact to “Alex Tan” and “client”._

3. `edit 2`  
   _Invalid: At least one field must be provided._

#### Design Considerations

**Aspect: How to represent editability of contact fields**

* **Alternative 1 (current choice):** Use `EditPersonDescriptor` to hold only fields to be updated
    * Pros: Efficient – only changed fields are considered; avoids unnecessary data rewriting
    * Cons: Slightly more complex to manage null values and partial updates

* **Alternative 2:** Replace entire `Person` object with a new one using all fields
    * Pros: Simpler code, consistent object recreation
    * Cons: Less efficient; error-prone if unnecessary data is overwritten

### Pin feature

The `pin` command allows users to mark a contact as important. Pinned contacts are prioritized for easier access.

<p align="center">
  <img src="images/PinSequenceDiagram.png" alt="PinCommand Sequence Diagram" />
</p>

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `PinCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.</div>

#### Implementation Details

1. The user inputs the `pin` command followed by the index of the contact to pin.
2. The `LogicManager` receives the command and calls `AddressBookParser#parseCommand`.
3. The `AddressBookParser` creates a `PinCommandParser` and calls its `parse` method.
4. The `PinCommandParser` parses the index and creates a `PinCommand` object.
5. The `LogicManager` then executes the `PinCommand`, which:
    - Retrieves the list of filtered persons via `Model#getFilteredPersonList()`.
    - Validates the provided index and retrieves the target contact.
    - Creates an updated `Person` instance with the `pin` flag set to `true`.
    - Replaces the old contact with the updated one using `Model#setPerson()`.
    - Calls `Model#pinPerson()` and `Model#commit()` to persist the change.
6. A `CommandResult` is returned to the `LogicManager`, which displays a success message.

#### Usage Examples

1. `pin 1`  
   _Pins the first contact in the list._

2. `pin 3`  
   _Pins the third contact._

3. `pin`  
   _Invalid: No index provided._

4. `pin 10`  
   _Invalid: Index exceeds size of contact list._

#### Design Considerations

**Aspect: How to represent the "pinned" state of a contact**

* **Alternative 1 (current choice):** Add a `Pin` attribute to the `Person` class
    * Pros: Easy to check pin status directly from the `Person` object
    * Cons: Requires updating and cloning `Person` objects on pin/unpin

* **Alternative 2:** Maintain a separate list of pinned person IDs
    * Pros: Avoids modifying `Person` objects
    * Cons: Adds complexity for syncing across model updates and filtering

**Aspect: Preventing redundant pins**

* The `PinCommand` checks if a contact is already pinned. If so, it does not change the state and returns a different message (`Already pinned.`)


### Command history

The command history allows the user to re-access previously entered commands quickly.


The following sequence diagram models the interaction within the `Model` component called by `LogicManager#execute`
when the user executes a command to **save command inputs**.
![AddCommandHistorySequenceDiagram](images/AddCommandHistorySequenceDiagram.png)

#### Implementation Details: Saving commands to command history

1. When a user enters a non-empty command, the `ModelManager#addPastCommandInput` method will be called when the command text in `LogicManager` component is non-empty.
2. The `ModelManager` calls `CommandHistory#addInput` and passes the non-empty command text as argument.
3. If the command history's most recently added command is `equal` to the command text argument, the command text is not re-added again.
4. Otherwise, if the command history is currently full, with `MAX_HISTORY_SIZE` entries (set to 20), the earliest added command entry will be removed.
5. The `CommandHistory` object will add the command to `pastCommands`.

Note that `pastCommands` is an `ObservableList` sorted from most to least recent at the tail of the list.

<br>

The following sequence diagram models the main components involved when a user moves up the command history selection
using the `Ctrl + Up` key combinations on Windows (or `Ctrl + Opt + Up` on macOS), to **re-access previous commands**.
![CommandHistorySequenceDiagram](images/CommandHistorySequenceDiagram.png)

#### Implementation Details: Re-accessing command history

1. When the user presses `Ctrl + Up`, the subscribed for the `EventHandler<KeyEvent>` JavaFX listener will be called.
2. The listener then calls `handleMovementUp` method of the `CommandHistoryMenu` in the Ui module.
3. The `CommandHistoryMenu` object calls the `moveUp` method of an internal `CommandHistoryMenuController` object to handle the logic
   of decrementing the index.
4. In `moveUp` then sets the current input of the `CommandBox` object through a call to `CommandBox#setCommandTextField` with the selected previous command.
5. The `CommandHistoryMenu` object then gets a result of this selection update by calling `CommandHistoryMenuController#getCommandSelectionIndex`
6. If the index is present within this result, the `CommandHistoryMenu` object gets the `SelectionModel` for rendering the given list cell of `ListView`
7. The `select` method of the `SelectionModel` object is then called with the index passed as argument.


#### Usage Examples
1. User launches Notarius
2. User enters a non-empty command.
3. User re-accesses the previously entered command using the command history shortcuts:
    * Windows/Linux: `Ctrl + Up`/`Ctrl + Down` to move up/down the selection.
    * macOS: `Ctrl + Opt + Up`/ `Ctrl + Opt + Down` similarly.


#### Design Considerations

**Aspect: Types of input to save in the command history**

* **Alternative 1 (current choice):** Support addition of both invalid and valid commands, with duplicate handling for consecutively entered inputs
    * Pros: More user-friendliness, allows users to re-access and quickly re-edit past commands, if they have typed them wrong by accident, without having to retype the entire command.
    * Cons: May slightly clutter the command history with invalid commands if the user spams **different** invalid commands intentionally, which is not the intended behaviour.

* **Alternative 2:** Support addition of only valid commands
    * Pros: Allows users to find previously entered valid commands that they want to repeat.
    * Cons: Less user-friendly, since users will not be able to re-access previously entered commands that they have typed incorrectly, and will have to retype the entire command again.


### Note feature
The note feature allows the user to change and view notes. \
The user changes notes via the `note` command and the user views notes via the `viewnote` command.

#### Implementation Details

This is how a user changes their note via the `note` command:
![NoteCommandSequenceDiagram](images/NoteCommandSequenceDiagram.png)
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `NoteCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of the diagram.
</div>
1. The user inputs the command to change their note of a contact of a specific index.
2. A `NoteCommandParser` object invokes its `parse` method which parses the user input.
3. The `NoteCommand` object is created with the parsed prefix and specified index.
4. A `LogicManager` object invokes the `execute` method of the `NoteCommand` object.
5. The `execute` method of the `NoteCommand` object invokes the getFilteredPersonList of the `model`
and gets the person with the specified index. It then modifies the specified client contact with 
a new note invokes the `setPerson`,
and `commit` methods of its `Model` argument to change the note of the person.
6. The `execute` method of the `NoteCommand` object returns a `CommandResult` object which stores the data regarding
   the completion of the `note` command.

This is how a user views their note via the `viewnote` command:

![ViewNoteCommandSequenceDiagram](images/ViewNoteCommandSequenceDiagram.png)
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ViewNoteCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of the diagram.
</div>
1. The user inputs the command to view a note of a contact of a specific index.
2. A `ViewNoteCommandParser` object invokes its `parse` method which parses the user input.
3. The `ViewNoteCommand` object is created with the specified index.
4. A `LogicManager` object invokes the `execute` method of the `ViewNoteCommand` object.
5. The `execute` method of the `ViewNoteCommand` object invokes the getFilteredPersonList of the `Model` and gets the note of the client contact at the specified index and
   does the `commit` method of the `Model` as well.
6. The `execute` method of the `ViewNoteCommand` object returns a `CommandResult` object which stores the data regarding
   the completion of the `viewnote` command, if successful includes the note of the client contact at the specified index.

#### Design Considerations

**Aspect: How notes are shown to the user:**
- **Alternative 1 (current choice):** The user changes notes via the command line and views notes via the command output.
    - Pros: Easy to implement. User may prefer this if they prefer solely typing in a command line without using external keyboard shortcuts.
    - Cons: Feature may be less useful due to limited command box size and command input box size if the user types a long note.
- **Alternative 2:** View/change notes via a separate window that the user can open by entering a command. The user can done use shortcuts when they are done viewing/editing the note to close the window.
    - Pros: Better support for longer notes and better formatting.
    - Cons: Users that prefer solely typing on a command line may not like using external keyboard shortcuts. 

### Undo/redo feature
The `undo` and `redo` commands undoes and redoes other commands respectively. 

#### Implementation Details

* The proposed undo/redo mechanism is facilitated by `ModelState`. 
* The `ModelState` is a save state of the `Model`.
* The `ModelState` saves the `ReadOnlyAddressBook` and `Predicate<Person>` of the `Model`.
* 'ModelState' objects are stored in the `Model` as `stateHistory` which is an `ArrayList<ModelState>`.
* Undo/redo is facilitated by a `currentStatePointer` which points to the current `ModelState` in the `stateHistory`.

Additionally, the `Model` implements and exposes the following operations to facilitate the undo/redo process:
* `Model#commit()` — Saves the current `ModelState` in the `stateHistory`.
* `Model#undo()` — Restores the previous `ModelState` from its `stateHistory`.
* `Model#redo()` — Restores a previously undone `ModelState` from its `stateHistory`.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `stateHistory` will be initialised with the initial `ModelState` of the newly initialised `Model`, and the `currentStatePointer` pointing to that single `ModelState`.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book of the `Model`. The `delete` command calls `Model#commit()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `stateHistory` as a `ModelState`, and the `currentStatePointer` is shifted to the newly inserted `ModelState`.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commit()`, causing another modified address book state in the newly created `ModelState` to be saved into the `stateHistory`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commit()`, so the `ModelState` will not be saved into the `stateHistory`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undo()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous `ModelState`, and restores the `Model` to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0 pointing to the initial `ModelState`, then there are no previous `ModelState` states to restore. The `undo` command uses `Model#hasUndo()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redo()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone `ModelState`, and restores the `Model` to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `stateHistory.size() - 1`, pointing to the latest `ModelState` state, then there are no undone `ModelState` to restore. The `redo` command uses `Model#hasRedo()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user executes `clear`, which calls `Model#commit()`. Since the `currentStatePointer` is not pointing at the end of the `stateHistory`, all `ModelState` states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState4](images/UndoRedoState4.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design Considerations

**Aspect: How undo & redo executes:**
- **Alternative 1 (current choice):** Saves the state of the model.
    - Pros: Easy to implement. 
    - Cons: May have performance issues in terms of memory usage.
- **Alternative 2:** Individual command knows how to undo/redo by itself.
    - Pros: Will use less memory (e.g. for `delete`, just save the person(s) being deleted).
    - Cons: We must ensure that the implementation of each individual command is correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: Lawyers

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage contacts faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                                           | I want to …​                                                      | So that I can…​                                                                             |
|----------|-------------------------------------------------------------------|-------------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| `* * *`  | lawyer                                                            | list all contacts with their relevant details                     | easily peruse them                                                                          |
| `* * *`  | lawyer                                                            | add a contact's name                                              | identify the individual easily                                                              |
| `* * *`  | lawyer                                                            | add a contact's phone number                                      | contact them by phone                                                                       |
| `* * *`  | lawyer                                                            | add a contact's email                                             | contact them by email                                                                       |
| `* * *`  | lawyer                                                            | add a contact's address                                           | know where to mail legal documents physically to                                            |
| `* * *`  | lawyer                                                            | save my contacts to my _local system_                             | access them later after reopening the application                                           |
| `* * *`  | lawyer with many clients                                          | search for contacts                                               | easily find out their contact information                                                   |
| `* * *`  | lawyer                                                            | delete contacts                                                   | remove client contacts that I no longer need                                                |
| `* * *`  | lawyer                                                            | access my contacts without internet                               | so that I can continue to work effectively in places with limited to no internet connection |
| `* *`    | busy lawyer who is working with multiple clients at the same time | add notes to contacts                                             | keep track of their relevant case-related details, preferences, and personal details        |
| `* *`    | busy lawyer who is working with multiple clients at the same time | view a contact's notes                                            | see their relevant case-related details, preferences, and personal details                  |
| `* *`    | lawyer                                                            | sort the contacts                                                 | improve the organisation of my client contacts                                              |
| `* *`    | forgetful lawyer                                                  | pin important clients                                             | look up their information faster                                                            |
| `* *`    | lawyer                                                            | unpin currently pinned clients                                    | keep my pinned list of clients up to date                                                   |
| `* *`    | lawyer who can type fast                                          | re-access previously entered commands quickly                     | save time by not typing them again                                                          |
| `* *`    | lawyer with many clients                                          | delete multiple client contacts at once                           | clear clients from past cases faster and more conveniently                                  |
| `* *`    | forgetful lawyer                                                  | view a quick help summary for certain commands                    | quickly relearn how to perform certain commands in case I forgot how to use them            |
| `* *`    | lawyer                                                            | edit client contact details                                       | keep client contact information up to date                                                  |
| `* *`    | lawyer trying the application for the first time                  | clear all the client contact data                                 | start fresh from a clean state                                                              |
| `* *`    | careless lawyer                                                   | undo my previous commands                                         | easily revert my mistakes                                                                   |
| `* *`    | careless lawyer                                                   | redo my previous commands                                         | easily revert my mistakes from undoing                                                      |
| `* *`    | lawyer                                                            | tag contacts (e.g., "Plaintiff", "Defendant", "Under Review")     | remember their roles easily                                                                 |
| `* *`    | lawyer                                                            | set reminders to follow up with contacts                          | make sure to not miss an important check-in                                                 |
| `* *`    | lawyer                                                            | tag multiple contacts at once                                     | categorise them for my needs more efficiently and conveniently                              |
| `* *`    | lawyer                                                            | add multiple tags to a contact                                    | organise my contacts neatly and not have to keep tagging the same contact multiple times    |
| `* *`    | impatient lawyer                                                  | get the output of my commands quickly and responsively            | save time without having to wait long for the command output                                |
| `* *`    | lawyer                                                            | have a user-friendly and uncluttered interface to navigate around | focus on my legal work without getting distracted by complicated or cluttered visuals       |
| `*`      | lawyer                                                            | have my notes be automatically time-stamped                       | keep track of when notes were created                                                       |
| `*`      | busy lawyer                                                       | use command aliases to enter commands faster                      | save time and improve efficiency                                                            |
| `*`      | lawyer                                                            | add general post-it notes                                         | store information that is not related to any case or contact                                |
| `*`      | lawyer                                                            | assign contacts to specific cases                                 | keep track of all clients involved in a legal matter                                        |
| `*`      | busy lawyer                                                       | view all contacts of specific cases                               | quickly access their case details when needed                                               |
| `*`      | lawyer                                                            | unlink contacts related from cases                                | update case information when necessary                                                      |
| `*`      | lawyer                                                            | add notes to cases                                                | view case information when needed                                                           |

### Use cases

(For all use cases below, the **System** is `Notarius` and the **Actor** is the `User`, unless specified otherwise)

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC01 - Deleting client contacts`

**Guarantees**: `If MSS reaches step 4, the requested client contact(s) will be deleted`

**MSS**:
1. User requests to delete specific contacts in the list.
2. Notarius deletes the contacts and confirms that the contacts have been deleted.

   Use case ends.

**Extensions**:

* 1a. Notarius is unable to find some of the specified contact(s).
  * 1a1. Notarius alerts the user about the error.
  * 1a2. User retypes the command.
  * Steps 1a-1a2 are repeated until the specified contact(s) exist.
  * Use case resumes from step 2.

* 1b. Notarius uncovers an invalid contact identifier.
  * 1b1. Notarius alerts the user about the issue.
  * 1b2. User retypes the command with a valid contact identifier format.
  * Steps 1b-1b2 are repeated until the contact identifier is valid.
  * Use case resumes from step 2.

* 1c. Notarius uncovers an empty specified prefix.
  * 1c1. Notarius alerts the user about the issue.
  * 1c2. User retypes the command with a non-empty value for the specified prefix.
  * Steps 1c-1c2 are repeated until the prefix is no longer empty.
  * Use case resumes from step 2.


**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC02 - Adding client contacts`

**Guarantees**: `If MSS reaches step 2, the client contact is successfully added to Notarius.`

**MSS**:
1. User requests to add a client contact.
2. Notarius adds the client contact and confirms the addition.

   Use case ends.

**Extensions**:

* 1a. User omits one or more required fields in the command.
  * 1a1. Notarius alerts the user about the missing fields.
  * 1a2. User retypes the command with all required fields.
  * Steps 1a-1a2 are repeated until the all the required fields are specified.
  * Use case resumes from step 2.

* 1b. User enters a contact with an email that already exists in the system.
  * 1b1. Notarius alerts the user that a duplicate contact exists.
  * 1b2. User may choose to modify the email or cancel the action.
  * Steps 1b-1b2 are repeated until the email entered is not a duplicate.
  * Use case resumes from step 2.

* 1c. User enters an invalid field.
  * 1c1. Notarius alerts the user about the incorrect format.
  * 1c2. User retypes the command with valid formats.
  * Steps 1c-1c2 are repeated until the fields are valid.
  * Use case resumes from step 2.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC03 - Editing a contact`

**Guarantees**: `If MSS reaches step 2, the contact will have the specified field updated correctly to the new value`

**MSS**:
1. User requests to edit field of a contact given by their id.
2. Notarius updates that field to the new value and confirms the contact has been edited.

   Use case ends.

**Extensions**:
* 1a. Notarius is unable to find the specified contact.
  * 1a1. Notarius alerts the user about the error.
  * 1a2. User retypes the command.
  * Steps 1a-1a2 are repeated until the contact specified exists.
  * Use case resumes from step 2.

* 1b. Notarius uncovers an empty field description.
  * 1b1. Notarius alerts the user about the issue.
  * 1b2. User retypes the command with a non-empty value for the specified field.
  * Steps 1b-1b2 are repeated until the field is no longer empty.
  * Use case resumes from step 2.

* 1c. Notarius uncovers that the format of the field the user entered is invalid.
  * 1c1. Notarius alerts the user about the error.
  * 1c2. User retypes the command with a valid field format.
  * Steps 1c-1c2 are repeated until the field format is valid.
  * Use case resumes from step 2.

* 1d. Notarius uncovers an invalid contact identifier.
  * 1d1. Notarius alerts the user about the issue.
  * 1d2. User retypes the command with a valid contact identifier format.
  * Steps 1d-1d2 are repeated until the contact identifier is valid.
  * Use case resumes from step 2.

**System**: `Notarius`

**Use Case**: `UC04 - Changing a Note of a Client Contact`

**Actor**: `User`

**Guarantees**: `If MSS reaches step 2, the note of the client contact is changed.`

**MSS**:

1. User requests to change a note to a contact.
2. Notarius changes the note of the client contact and confirms the successful change of the note.

   Use case ends.

**Extensions**:

* 1a. Notarius is unable to find the specified client contact.
  * 1a1. Notarius alerts the user about the error.
  * 1a2. User retypes the command.
  * Steps 1a-1a2 are repeated until the client contact specified exists.
  * Use case resumes from step 2.

* 1b. Notarius uncovers an invalid contact identifier.
  * 1b1. Notarius alerts the user about the error.
  * 1b2. User retypes the command with a valid contact identifier format.
  * Steps 1b-1b2 are repeated until the contact identifier is valid.
  * Use case resumes from step 2.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC05 - Viewing a Note of a client contact`

**Guarantees**: `If MSS reaches step 2, the note of the client contact is displayed.`

**MSS**:

1. User requests for the notes of a contact.
2. Notarius displays all notes belonging to the contact.

   Use case ends.

**Extensions**:

* 1a. Notarius is unable to find the specified client contact.
  * 1a1. Notarius alerts the user about the error.
  * 1a2. User retypes the command.
  * Steps 1a-1a2 are repeated until the client contact specified exists.
  * Use case resumes from step 2.

* 1b. Notarius uncovers an invalid contact identifier.
  * 1b1. Notarius alerts the user about the error.
  * 1b2. User retypes the command with a valid contact identifier format.
  * Steps 1b-1b2 are repeated until the contact identifier is valid.
  * Use case resumes from step 2.
 
**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC06 - Accessing an input from the command history`

**Preconditions**: `Command history is open.`

**MSS**:

1. User selects a command input from the command history.
2. Notarius prepares the selected command to be entered.
3. User enters the command.
4. Notarius executes the command and closes the command history.

   Use case ends.

**Extensions**

* 1a. Notarius is unable to find any command in the history.
    * 1a1. Notarius alerts the user with a message.
    * 1a2. User enters a new command.
    * Use case resumes from step 4.

* 3a. User requests to edit the command input with a new value.
    * 3a1. Notarius updates the selected command input with the new value.
    * Steps 3a-3a1 are repeated until the user enters the command.
    * Use case resumes from step 4.

* *a. At any time, user requests to close the command history.
    * *a1. Notarius closes the command history.
    * Use case ends.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC07 - Sorting the contacts list`

**Guarantees**: `If MSS reaches step 3, the user has successfully sorted the contacts list by a specified prefix.`

**MSS**:

1. User requests to sort Notarius by a specified prefix.
2. Notarius updates the contacts list in the sorted order.
3. Notarius confirms that the contacts list has been successfully sorted.

   Use case ends.

**Extensions**:

* 1a. Notarius detects a missing prefix in the entered input.
  * 1a1. Notarius displays the error message.
  * 1a2. User re-enters a new command with a specified prefix.
  * Steps 1a1 - 1a2 are repeated until a valid prefix is input by the User.
  * Use case resumes from step 2.

* 1b. Notarius detects an invalid prefix in the entered input.
  * 1b1. Notarius displays the error message.
  * 1b2. User re-enters a new command with a specified prefix.
  * Steps 1b1 - 1b2 are repeated until a valid field is input by the User.
  * Use case resumes from step 2.

* 1c. User enters extra spaces or invalid format in the entered input.
  * 1c1. Notarius displays an error message.
  * 1c2. User re-enters a new command with properly formatted command.
  * Steps 1c1 - 1c2 are repeated until a valid command is input by the User.
  * Use case resumes from step 2.

* 1d. User enters duplicate prefixes in the entered input.
  * 1d1. Notarius displays an error message.
  * 1d2. User re-enters a new command with no duplicate prefixes.
  * Steps 1d1 - 1d2 are repeated until a valid command with no duplicate prefixes is input by the User.
  * Use case resumes from step 2.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC08 - Clearing the contacts list`

**Guarantees**: `If MSS reaches step 3, the user has successfully cleared the contacts list.`

**MSS**:

1. User requests to clear the data in the contacts list.
2. Notarius updates the data in the contacts list.
3. Notarius confirms that the data in the contacts list has been cleared.

   Use case ends.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC09 - Listing all contacts`

**Guarantees**: `If MSS reaches step 3, the user has successfully listed all the contacts.`

**MSS**:

1. User requests to list all contacts.
2. Notarius displays all relevant contacts.
3. Notarius confirms that all relevant contacts has been successfully listed.

   Use case ends.


**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC10 - Displaying Help Information`

**Guarantees**: `If MSS reaches step 2, the requested help information will be displayed.`

**MSS**:

1. User requests help by entering the help command. 
2. Notarius displays a help window with general usage instructions.

   Use case ends.

**Extensions**:

* 1a. User requests help for a specific command. 
  * 1a1. Notarius displays detailed usage information for the specified command.<br>
    Use case ends.

* 1b.  User enters an invalid command name. 
  * 1b1. Notarius alerts the user with an error message: "Unknown command! Use help to see available commands."
  * 1b2. User retypes the command with a valid usgae.<br>
    Steps 1b1-1b2 are repeated until a valid command is entered.<br>
    Use case resumes from step 1a1.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC11 - Finding a Contact`

**Guarantees**: `If MSS reaches step 3, a list of matching contacts will be displayed.`

**MSS**:

1. User requests to find contacts by entering the find command with specified fields and keywords. 
2. Notarius searches for contacts whose fields contain any of the given keywords, 
allowing minor typos (up to a Levenshtein distance of 2) in the name, email, and address fields only. 
3. Notarius displays a list of matching contacts with index numbers.

   Use case ends.


**Extensions**:

* 1a. User enters an invalid search format. 
  * 1a1. Notarius alerts the user with an error message about the incorrect format. 
  * 1a2. User retypes the command following the correct format.<br>
    Steps 1a1-1a2 are repeated until the command format is valid.<br>
    Use case resumes from step 2.
* 1b. User enters multiple search fields. 
  * 1b1. Notarius searches for contacts that match any of the 
  specified fields (name, phone, email, address, or tags).<br>
    Use case resumes from step 3.
* 1c. User enters a keyword with minor typos in the phone or tag fields. 
  * 1c1. Notarius does not apply typo correction for phone numbers or tags. 
  * 1c2. If an exact match is not found, Notarius displays a message indicating no results were found.<br>
    Use case ends.
* 1d. User enters a keyword with minor typos in the name, email, or address fields. 
  * 1d1. Notarius applies fuzzy matching (Levenshtein distance of up to 2) for name, email, and address fields.<br>
    Use case resumes from step 3.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC12 - Undoing a Command`

**Guarantees**: `If MSS reaches step 2, contacts list would have been returned to the state
before the previous command was executed`

**MSS**:

1. User requests to undo the last command.
2. Notarius restores the contacts list to the state before the last command was executed.

   Use case ends.

**Extensions**:

* 1a. Notarius sees that there is no previous state of the contacts list to undo to
  * 1a1. Notarius alerts the user with an error message.

  Use case ends.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC13 - Redoing a Command`

**Guarantees**: `If MSS reaches step 2, contacts list would have been returned to the state
before the previous undo was executed`

**MSS**:

1. User requests to redo last undone command.
2. Notarius restores the contacts list to the state before the last undone command was executed.

   Use case ends.

**Extensions**:

* 1a. Notarius sees that there is no last undone state of the contacts list to redo to
  * 1a1. Notarius alerts the user with an error message.

  Use case ends.

**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC14 - Pinning a client contact`

**Guarantees**: `If MSS reaches step 3, the client contact is marked as pinned.`

**MSS**:

1. User requests to pin a contact in the current contact list.
2. User enters the `pin` command followed by the index of the contact to pin.
3. Notarius marks the contact as pinned and confirms the action with a success message.

   Use case ends.

**Extensions**:

* 2a. User omits the index in the pin command.
  * 2a1. Notarius alerts the user that an index must be provided.
  * 2a2. User retypes the command with a valid index.
  * Use case resumes from step 3.

* 2b. User enters an index that is out of range.
  * 2b1. Notarius alerts the user that the index is invalid.
  * 2b2. User retypes the command with a valid index.
  * Use case resumes from step 3.

* 3a. The contact is already pinned.
  * 3a1. Notarius notifies the user that the contact is already pinned.
  * Use case ends.


**System**: `Notarius`

**Actor**: `User`

**Use Case**: `UC15 - Unpinning a client contact`

**Guarantees**: `If MSS reaches step 3, the client contact is marked as unpinned.`

**MSS**:

1. User requests to unpin a contact in the current contact list.
2. User enters the `unpin` command followed by the index of the contact to unpin.
3. Notarius marks the contact as unpinned and confirms the action with a success message.

   Use case ends.

**Extensions**:

* 2a. User omits the index in the unpin command.
  * 2a1. Notarius alerts the user that an index must be provided.
  * 2a2. User retypes the command with a valid index.
  * Use case resumes from step 3.

* 2b. User enters an index that is out of range.
  * 2b1. Notarius alerts the user that the index is invalid.
  * 2b2. User retypes the command with a valid index.
  * Use case resumes from step 3.

* 3a. The contact is already unpinned.
  * 3a1. Notarius notifies the user that the contact is already unpinned.
  * Use case ends.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 contacts without a noticeable lag in user requests.
3. A user with _above average typing speed_ for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Data should be stored locally as human editable file.
5. Software should work without an installer.
6. GUI should work well with standard screen resolutions 1920x1080 and higher, and for screen scales 100% and 125%.
7. GUI should be usable for resolutions 1280x720 and higher, and for screen scales 150%.
8. Application should come packaged as a single jar file.
9. The `.jar/.zip` file should not exceed 100 MB.
10. Documents packaged with the application should each not exceed 15 MB per file.
11. The application should respond within 5 seconds to any user request.

### Glossary

| **Terms**                  | **Meaning**                                                                                                                                            |
|----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| Mainstream OS              | Windows, Linux, Unix, MacOS.                                                                                                                           |
| Above-average typing speed | ≥ 60 words per minute.                                                                                                                                 |
| Crucial client information | Important client information such as name, phone number, email, address, tags, as well as notes stored that could contain legal information for cases. |
| Local system               | The user's computer that they are using to run the application.                                                                                        |
| Cases (in law)             | The legal disputes that lawyers work on that involve many parties.                                                                                     |
| User Request               | The commands the user gives to Notarius via the command line interface.                                                                                |
| MSS                        | Main Success Scenario.                                                                                                                                 |
| API                        | Application Programming Interface.                                                                                                                     |
| GUI                        | Graphic User Interface.                                                                                                                                |
| CLI                        | Command Line Interface.                                                                                                                                |
| JAR                        | A packed file format used in Java that contains compiled Java code to enable easy distribution, portability, and execution.                            |
| JSON                       | JavaScript Object Notation, a lightweight data format widely used for storing and exchanging structured data.                                          |
| Contact Identifier         | Refers to the index shown in the contacts list                                                                                                         |

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding client contacts

1. Adding a client contact in the contact list

    1. Prerequisites: The email specified in each of the `add` commands in the test cases does not exist in the client contacts list.
    2. Test case: `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` <br>
       Expected: Adds a client contact with the name "John Doe" into the contact list and a message showing that the client contact has been added is displayed.

### Editing client contacts
1. Editing a client contact in the contact list

    1. Prerequisites:  The email specified in each of the `edit` commands in the test cases does not exist in the client contacts list. 
There should be at least 1 client contact shown in the contacts list. Otherwise, use the `add` command to add more client contacts.
    2. Test case: `edit 1 n/Johnny Doe p/91234567 e/johndoe@example.com a/John street, block 123, #01-01 t/Corporate Law t/Defendant`<br>
       Expected: For the 1st client contact from the top of the list, the name of the contact is edited to "Johnny Doe", the phone number of the contact is edited to "91234567"
, the email of the contact is edited to "johndoe@example.com", the address of the contact is edited to "John street, block 123, #01-01"
and there will be two tags on the contact which are "Corporate Law" and "Defendant". A message showing that the person has been edited is also shown.
    3. Test case `edit 1 t/`<br>
       Expected: There will be no tags for the 1st client contact from the top of the list. A message showing that the person has been edited is also shown.

### Deleting client contacts

Each test case in this feature section (labelled "Test case") should be independent.

1. Deleting a single client contact while all persons are being shown

   1. Prerequisites for each test case: List all persons using the `list` command. There should be **at least** 6 contacts in the list. Otherwise, use the `add` command to add more client contacts. Take note that duplicate emails (ignoring letter casing) are not allowed in Notarius.

   2. Test case: `delete 1`<br>
      Expected: First client contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.
   
2. Deleting consecutive client contacts while all persons are being shown

   1. Prerequisites for each test case: List all persons using the `list` command, ensuring that there are **exactly** 6 contacts in the list. Otherwise, use the `add` command to add more client contacts. Take note that duplicate emails (ignoring letter casing) are not allowed in Notarius.

   1. Test case: `delete i/1-3`<br>
      Expected: First three client contacts are deleted from the list. Details of the deleted contacts shown in the status message. Timestamp in the status bar is updated.
   
   1. Test case: `delete  i/1-1`<br>
      Expected: First client contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete i/0-2`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.
   
   1. Test case: `delete i/1-9`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Test case: `delete i/6-1`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

3. Deleting the first and last client contact in the contact list while all client contacts are shown.

   1. Prerequisites for each test case: List all persons using the `list` command, ensuring that there are **exactly** 6 contacts in the list. Otherwise, use the `add` command to add more client contacts. Take note that duplicate emails (ignoring letter casing) are not allowed in Notarius.

   1. Test case: `delete i/1 6`<br>
      Expected: First and last client contacts are deleted from the list. Details of the deleted contacts shown in the status message. Timestamp in the status bar is updated.
   
   1. Test case: `delete i/1 1 6 6 6`<br>
      Expected: First and last client contacts are deleted from the list. Details of the deleted contacts shown in the status message. Timestamp in the status bar is updated.
   
   1. Test case: `delete i/0 7`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

### Pinning a contact

Each test case in this feature section (labelled “Test case”) should be independent.

#### Pinning a single contact

- **Prerequisites**:
  - List all contacts using the `list` command. There should be at least 2 contacts. Otherwise, use the `add` command to add more.
  - Ensure that the contact to be pinned is currently **unpinned**.

- **Test case**: `pin 1`  
  **Expected**: The first contact is pinned. The status message confirms the successful pinning. The timestamp in the status bar is updated.

- **Test case**: `pin 0`  
  **Expected**: No contact is pinned. Error message displayed in the status message. Status bar remains the same.

- **Test case**: `pin`, `pin x` (where x > list size)  
  **Expected**: No contact is pinned. Error message displayed. Status bar remains the same.

#### Pinning an already pinned contact

- **Test case**: `pin 1` (run twice in a row on the same contact)  
  **Expected**: Second time, an error message is shown indicating that the contact is already pinned. No change is made. Status bar remains the same.

---

### Unpinning a contact

Each test case in this feature section (labelled “Test case”) should be independent.

#### Unpinning a single contact

- **Prerequisites**:
  - List all contacts using the `list` command. There should be at least 2 contacts.
  - Ensure the selected contact is **pinned** beforehand.

- **Test case**: `unpin 1`  
  **Expected**: The first contact is unpinned. The status message confirms successful unpinning. Timestamp in the status bar is updated.

- **Test case**: `unpin 0`  
  **Expected**: No contact is unpinned. Error message shown in the status message. Status bar remains the same.

- **Test case**: `unpin`, `unpin x` (where x > list size)  
  **Expected**: No contact is unpinned. Error message displayed. Status bar remains the same.

#### Unpinning an already unpinned contact

- **Test case**: `unpin 1` (run twice in a row on the same contact)  
  **Expected**: Second time, an error message is shown indicating that the contact is already unpinned. No change is made. Status bar remains the same.


### Command history

Each test case in this feature section (labelled "Test case") should be independent of each other.<br>
**Important**: For the key combinations specified, macOS users should use `Ctrl + Opt + Up`/`Ctrl + Opt + Down` respectively instead of `Ctrl + Up`/`Ctrl + Down`.

1. Saving command history

   1. Prerequisites for each test case: 
        * No command should be entered into the command box yet (and thus the command history should be empty). Otherwise, relaunch the application.
        * There should be **exactly** 6 contacts in the list. Otherwise, use the `add` command to add more client contacts. Take note that duplicate emails (ignoring letter casing) are not allowed in Notarius.
        * **None** of the contacts should have an email equal to `test@email.com`, **ignoring** letter casing. Otherwise, delete that contact using `delete` and add a new contact that does not have a duplicate email.

   1. Test case: `delete 1`<br>
      Expected: The command history is updated with the command text "delete 1". The command history is displayed when the user presses `Ctrl + Up` or `Ctrl + Down`.

   1. Test case: `delete 1` followed by `add n/notarius p/1231 e/test@email.com a/blk 123 abc` <br>
      Expected: The command history is updated with the command texts `add n/notarius p/1231 e/test@email.com a/blk 123 abc` at the top of the command history list and `delete 1` below it.

   1. Test case: `list` followed by `list` <br>
      Expected: The command history is updated with the command text "list", but it will be shown with no **consecutive** duplicates when open.
    
   1. Test case: `delete 1` followed by: `adddd n/typo p/123 e/test@email.com a/test blk` <br>
      Expected: The command history is updated with the invalid command: `adddd n/typo p/123 e/test@email.com a/test blk` at the top of the command history and the valid command: `delete 1` below it.

### Finding a person

1. Finding a person while all persons are being shown

   1. Prerequisites: List all persons using `list` command. Multiple persons in the list.
   2. Test case: `find n/"Alice"` <br>
      Expected: Displays all contacts with names that match "Alice" (case-insensitive) or have a name with levenshtein distance <= 2 to "ALICE".
   3. Test case: `find n/"Alice" "Bob"` <br>
      Expected: Displays all contacts with names containing either "Alice" or "Bob".
   4. Test case: `find n/"Alice" e/"alice@email.com" a/"Bedok Central"` <br>
      Expected: Displays all contacts where any of the fields match or have a levenshtein distance <= 2 from each keyword.
   5. Test case: `find t/"client" p/"12345678"` <br>
      Expected: Displays contacts that exactly match either tag "client" or phone "12345678".
   6. Test case: `find n/"NonExistentName"` <br>
      Expected: "0 persons listed!" message is displayed.

### Displaying help

1. Showing help in Notarius

   1. Prerequisites: Notarius is open and running
   2. Test case: `help` <br>
      Expected: A help window appears displaying instructions for using the application. The status message confirms that the help window has been opened.
   3. Test case: `help find` <br>
      Expected: The application displays detailed instructions for the find command, including expected parameters and format. 
   4. Test case: `help delete` <br>
      Expected: The application displays detailed instructions for the delete command, including expected parameters and format.
   5. Test case: `help meeee` (invalid command) <br>
      Expected: Error message: "Unknown command! Use 'help' to see available commands."



### Sorting contacts list

1. Sorting contacts list in Notarius
   1. Prerequisites: Notarius contains a list of contacts.
   2. Test case: `sort n/`<br>
   Expected: Displays all contacts sorted by names in ascending order.
   3. Test case: `sort p/`<br>
   Expected: Displays all contacts sorted by phone numbers in ascending order.
   4. Test case: `sort e/`<br>
   Expected: Displays all contacts sorted by email addresses in ascending order.
   5. Test case: `sort a/`<br>
   Expected: Displays all contacts sorted by addresses in ascending order.
   6. Test case: `sort t/`<br>
   Expected: Displays all contacts sorted by tags in ascending order.
   7. Test case: `sort t/ n/`<br>
   Expected: Displays all contacts sorted by tags, followed by names in ascending order.
   8. Test case: `sort t/ p/`<br>
   Expected: Displays all contacts sorted by tags, followed by phone numbers in ascending order.
   9. Test case: `sort t/ e/`<br>
   Expected: Displays all contacts sorted by tags, followed by email addresses in ascending order.
   10. Test case: `sort t/ a/`<br>
   Expected: Displays all contacts sorted by tags, followed by addresses in ascending order.

### Listing all contacts

1. List all contacts

   1. Test case: `list`<br>
      Expected: Displays the whole contacts list.

### Clearing all contacts

1. Clear all contacts 

  1. Test case: `clear`<br>
     Expected: Clears the whole contacts list.

### Changing a note

1. Changing a note in Notarius

    1. Prerequisite for test case: There should be at least 1 client contact shown in the contacts list. Otherwise, use
    the `add` command to add more client contacts.
    2. Test case: `note 1 nt/overseas for a while` <br>
    Expected: The 1st client contact from the top of the contacts list has the note "overseas for a while".
    This can be checked using the `viewnote` command. Specifically the command `viewnote 1`.

### Viewing notes
1. Viewing a note in Notarius

    1. Prerequisite for test case: There should be at least 1 client contact shown in the contacts list. Otherwise, use
       the `add` command to add more client contacts. <br>
Change note of the 1st client contact from the top of the list to "overseas for a while" using the `note` command. <br>
The full command for this is `note 1 nt/overseas for a while`.
    2. Test case: `viewnote 1`<br>
       Expected: The note of the 1st client contact from the top of the list which is "overseas for a while" is displayed.

### Undoing a command
1. Undoing a command in Notarius

    1. Prerequisite for test case: There should have been a previous command that was executed that is not `exit`.
    2. Test case: `undo`<br>
       Expected: Returns the state of the contacts list to before the previous command was executed.

### Redoing an undone command
1. Redoing an undone command in Notarius

    1. Prerequisite for test case: There should have been a previous `undo` command that was
executed with no other command executed after that `undo` command.
    2. Test case: `redo`<br>
       Expected: Returns the state of the contacts list to before the `undo` command was executed.

## **Appendix: Planned Enhancements**:

### 1. Allow for international phone number format to be entered

#### Current:
In phone input fields, user only can enter a number. This restricts the user from being able to enter country codes.
This would reduce the application's usefulness as an address book application due to its limitation of storing
international client contacts. 

#### Planned:
To enhance usability and global compatibility. We should allow the user to enter the country code along with the phone number 
in phone input fields.

### 2. Bulk tag deletion capability

#### Current:
Via the edit command, all tags can be deleted or tags can be replaced with new ones. 
However, If a singular client contact has many tags and the 
user wants to delete a few tags from them, this would be decently cumbersome as the user has to retype in the tags
they do not want to replace.

#### Planned:
Add a command to delete multiple tags from a client contact.

### 3. Support for longer notes.

#### Current:
Notes are entered through a command line and are designed to write short and simple notes. However, users may 
want to write longer notes with more formatting options. Longer notes may be cumbersome experience to view in the
command output.

#### Planned:
If we want to stick fully to a CLI, we can add longer multi-line notes by using a prompt-based input technique where 
the user can enter line after line by using the `note` command. When the user wants to stop entering lines of the note
they can type the command `donenote`. This can be enhanced by allowing formatting options such as bolding. Long notes
can also be displayed better by making the command output collapsible.

### 4. More specific error messages for `delete` command.

#### Current:
When the delete command fails to delete specified contact(s) due to an error in the command parsing phase,
the error message shows "Invalid command format!" followed by the delete command usage, instead of providing
useful information on why the command failed, which may be problematic since the command could fail for multiple reasons
besides having invalid index(s) (i.e. `INDEX` specified is not a positive integer between 1 and 2147483647 inclusive), which
is already a known issue.

The additional reasons include exceeding the maximum number of unique indexes that can be deleted of 100, or when `START_INDEX` > `END_INDEX` for the range-format.

#### Planned:
The application should provide more informative and specific error messages that contain the reason for failing to delete contact(s),
such as when the number of unique indexes to be deleted exceeds 100 (e.g., "Maximum number of contacts exceeded").

It should also display relevant error messages when the indexes specified in the format do not comply with the constraints
specified in the user guide for their respective formats. For example, displaying that the start and end index of a ranged-delete
is out-of-order when `START_INDEX` is greater than `END_INDEX`, or displaying which specified index was invalid if multiple indexes are provided.


### 5. Selecting a client contact in contact list

#### Current:
When a new command is called, the selected contact in the contact list will be unselected by the application.

#### Planned:
The client contact will still be selected even though a new command is called. In addition, users can have the option to select or unselect the client contact.


### 6. Undo/redo displays what commands were undone, redone 

#### Current:
When a command is undone/redone, the user may not know what command was undone/redone as it is not visible to them.
This might make it confusing for the user as they might not know which command they undid/redid.

#### Planned:
When a command is undone/redone, the command that has been undone/redone is shown in the command box.

### 7. Better Command history GUI

#### Current:
The command history wrap-around scrolling might be confusing for the user especially given the current
result output height is very small, making it inconvenient for users to navigate to past commands,
since it may also be hard to see multiple previous input commands at once.

#### Planned:
Add new settings to the user preference that allow the user to adjust the scroll speed and number of
commands displayed in the command history window.

### 8. Option to save command history to disk

#### Current:
Currently, the command history contains only the commands that were executed during the current session.
If users accidentally close the program, then all command history is lost, which is especially problematic
if they had entered useful commands prior.

#### Planned:
Support an option for users to save the command history to disk, so that users can access their previous commands even after closing the program.
