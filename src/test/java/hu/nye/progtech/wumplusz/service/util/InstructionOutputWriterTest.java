package hu.nye.progtech.wumplusz.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import hu.nye.progtech.wumplusz.model.enums.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InstructionOutputWriterTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private static InstructionOutputWriter underTest;

    private static final String MENU_STRING = "Válassz a menüpontokból:\n1. Pályaszerkesztés\n2. Beolvasás fájlból" +
            "\n3. Betöltés adatbázisból\n4. Mentés adatbázisba\n5. Játék\n6. Kilépés\n";

    private static final String ENTITY_STRING = "Válassz egy entitást a lehelyezéshez:\nWUMPUSZ WUMPUSZ HOS ARANY VEREM FAL \n";

    private static final String COORDINATE_STRING = "Add meg a X koordinátát: ";

    @BeforeEach
    public void setUp() {
        underTest = new InstructionOutputWriter();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testPrintMenuShouldPrintMenuPoints() {
        // given
        // when
        underTest.printMenu();

        // then
        assertEquals(MENU_STRING, outputStreamCaptor.toString());
    }

    @Test
    public void testPrintEntityShouldPrintAvailableEntities() {
        // given
        List<Entity> availableEntities = List.of(
                Entity.WUMPUSZ,
                Entity.WUMPUSZ,
                Entity.HOS,
                Entity.ARANY,
                Entity.VEREM,
                Entity.FAL
        );

        // when
        underTest.printEntity(availableEntities);

        // then
        assertEquals(ENTITY_STRING, outputStreamCaptor.toString());
    }

    @Test
    public void testPrintCoordinateShouldPrintCoordinate() {
        // given
        String coordinateType = "X";

        // when
        underTest.printCoordinate(coordinateType);

        // then
        assertEquals(COORDINATE_STRING, outputStreamCaptor.toString());
    }
}
