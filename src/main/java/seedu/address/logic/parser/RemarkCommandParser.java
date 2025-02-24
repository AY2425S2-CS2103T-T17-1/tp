package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

/**
 * Class for the parser of the {@code RemarkCommand}.
 */
public class RemarkCommandParser implements Parser {

    static final Prefix PREFIX_REMARK = new Prefix("r/");

    /**
     * Parses {@code args} into a command and returns it.
     *
     * @param args The arguments of the command.
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    @Override
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE), ive);
        }

        String remarkString = argMultimap.getValue(PREFIX_REMARK).orElse("");

        Remark remark = new Remark(remarkString);

        return new RemarkCommand(index, remark);
    }
}
