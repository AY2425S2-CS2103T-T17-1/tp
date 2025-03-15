package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.logging.Logger;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments to create a SortCommand and parsing logic identifies the sorting prefix.
 *
 */
public class SortCommandParser implements Parser<SortCommand> {
    private static final Logger logger = Logger.getLogger(SortCommandParser.class.getName());

    /**
     * Parses the given prefix string and returns a SortCommand.
     *
     * @param args The arguments string provided by the user.
     * @return SortCommand object based on the specified sorting attribute.
     * @throws ParseException Prefix is invalid or the arguments are not recognized.
     */
    public SortCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        switch (trimmedArgs) {
        case "n/":
            logger.info("Sorting by name.");
            return new SortCommand(PREFIX_NAME.getPrefix());

        case "p/":
            logger.info("Sorting by phone.");
            return new SortCommand(PREFIX_PHONE.getPrefix());

        case "e/":
            logger.info("Sorting by email.");
            return new SortCommand(PREFIX_EMAIL.getPrefix());

        case "a/":
            logger.info("Sorting by address.");
            return new SortCommand(PREFIX_ADDRESS.getPrefix());

        case "t/":
            logger.info("Sorting by tag.");
            return new SortCommand(PREFIX_TAG.getPrefix());

        default:
            logger.warning("Invalid sorting prefix provided: " + trimmedArgs);
            throw new ParseException("Invalid prefix used.");
        }
    }
}
