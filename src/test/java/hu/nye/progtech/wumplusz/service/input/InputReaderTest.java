package hu.nye.progtech.wumplusz.service.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class InputReaderTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private static final String INPUT_STRING = "Progtech";

    private static final Integer INPUT_INTEGER = 4;

    private static final String INPUT_INTEGER_STRING = "4";

    private static final String READ_ERROR_MESSAGE = "Hiba a beolvasás közben\n";

    private static final String ADD_NUMBER_MESSAGE = "Számot adj meg!\n";

    private static final String NO_LINE_MESSAGE = "No line found";

    private static final String SCANNER_CLOSED = "Scanner closed";

    private InputReader underTest;

    private Scanner scanner;

    @Test
    void testReadStringShouldReturnInputString() {
        // given
        scanner = new Scanner(INPUT_STRING);
        underTest = new InputReader(scanner);

        // when
        String input = underTest.readString();

        // then
        assertEquals(INPUT_STRING, input);
    }

    @Test
    void testReadStringShouldMakeError() {
        // given
        scanner = new Scanner(INPUT_STRING);
        scanner.close();
        underTest = new InputReader(scanner);
        System.setErr(new PrintStream(outputStreamCaptor));

        // when
        underTest.readString();

        // then
        assertEquals(READ_ERROR_MESSAGE, outputStreamCaptor.toString());
    }

    @Test
    void testReadIntegerShouldReturnInputInteger() {
        // given
        scanner = new Scanner(INPUT_INTEGER_STRING);
        underTest = new InputReader(scanner);

        // when
        Integer input = underTest.readInteger();

        // then
        assertEquals(INPUT_INTEGER, input);
    }

    @Test
    void testReadIntegerShouldMakeError() {
        // given
        scanner = new Scanner(INPUT_STRING);
        scanner.close();
        underTest = new InputReader(scanner);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> underTest.readInteger());

        // then
        assertEquals(SCANNER_CLOSED, exception.getMessage());
    }


    @Test
    void testReadIntegerShouldWriteOutAddNumber() {
        // given
        Scanner scanner = new Scanner(INPUT_STRING);
        underTest = new InputReader(scanner);
        System.setOut(new PrintStream(outputStreamCaptor));


        // when
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> underTest.readInteger());

        // then
        assertEquals(NO_LINE_MESSAGE, exception.getMessage());
        assertEquals(ADD_NUMBER_MESSAGE, outputStreamCaptor.toString());
    }

}
