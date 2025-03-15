package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class SortCommandParserTest {
    private SortCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new SortCommandParser();
    }

    @Test
    public void parse_validNamePrefix_success() throws ParseException {
        SortCommand command = parser.parse("n/");
        assertEquals(CliSyntax.PREFIX_NAME.getPrefix(), command.getSortPrefix());
    }

    @Test
    public void parse_validPhonePrefix_success() throws ParseException {
        SortCommand command = parser.parse("p/");
        assertEquals(CliSyntax.PREFIX_PHONE.getPrefix(), command.getSortPrefix());
    }

    @Test
    public void parse_validEmailPrefix_success() throws ParseException {
        SortCommand command = parser.parse("e/");
        assertEquals(CliSyntax.PREFIX_EMAIL.getPrefix(), command.getSortPrefix());
    }

    @Test
    public void parse_validAddressPrefix_success() throws ParseException {
        SortCommand command = parser.parse("a/");
        assertEquals(CliSyntax.PREFIX_ADDRESS.getPrefix(), command.getSortPrefix());
    }

    @Test
    public void parse_validTagPrefix_success() throws ParseException {
        SortCommand command = parser.parse("t/");
        assertEquals(CliSyntax.PREFIX_TAG.getPrefix(), command.getSortPrefix());
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () -> {
            parser.parse("abc/");
        });
        assertEquals("Invalid prefix used.", exception.getMessage());
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () -> {
            parser.parse("");
        });
        assertEquals("Invalid prefix used.", exception.getMessage());
    }
}
