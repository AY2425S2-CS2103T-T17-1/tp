package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Comparator;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;


/**
 * Parses input arguments and creates a new FindCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new SortCommand(parseComparatorForKeywords(trimmedArgs));
    }


    private Comparator<Person> parseComparatorForKeywords(String keyword) throws ParseException {
        switch (keyword) {
        case "n":
            return new Comparator<Person>() {
                @Override
                public int compare(Person person1, Person person2) {
                    return person1.getName().toString().compareTo(person2.getName().toString());
                }
            };
        case "p":
            return new Comparator<Person>() {
                @Override
                public int compare(Person person1, Person person2) {
                    return person1.getPhone().toString().compareTo(person2.getPhone().toString());
                }
            };
        default:
            throw new ParseException(SortCommand.MESSAGE_UNIDENTIFIED_KEYWORD);
        }
    }
}

