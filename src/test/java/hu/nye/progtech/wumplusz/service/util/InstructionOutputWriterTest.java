package hu.nye.progtech.wumplusz.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import hu.nye.progtech.wumplusz.model.UserData;
import hu.nye.progtech.wumplusz.model.enums.Entity;
import hu.nye.progtech.wumplusz.model.enums.GamePlayInstructions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InstructionOutputWriterTest {

    private static final String HIGH_SCORE = "Highscore: \n" +
            "testuser2: 5 nyerés\n" +
            "testuser1: 3 nyerés\n" +
            "testuser3: 2 nyerés\n" +
            "\n";
    private static final String GAMEPLAY_INSTRUCTIONS = "Válasz egy akciót:\n" +
            "LÉP HALASZTÁS FORDUL_JOBBRA ";
    private static final String GET_HERO_DIRECTION = "Adja meg a hős kezdezi irányát (N/W/S/E)\n";
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private static InstructionOutputWriter underTest;

    private static final String MENU_STRING = "Válassz a menüpontokból:\n1. Pályaszerkesztés\n2. Beolvasás fájlból" +
            "\n3. Betöltés adatbázisból\n4. Mentés adatbázisba\n5. Játék\n6. High score táblázat\n7. Kilépés\n";

    private static final String ENTITY_STRING = "Válassz egy entitást a lehelyezéshez. Kilépéshez: 'KILÉPÉS'\nWUMPUSZ (U) WUMPUSZ (U) HOS" +
            " (H) ARANY (G) " +
            "VEREM " +
            "(P)" + " FAL (W) \n";

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

    @Test
    public void testPrintGetHeroDirectionShouldPrintGetHeroDirection () {
        // given

        // when
        underTest.printGetHeroDirection();

        // then
        assertEquals(GET_HERO_DIRECTION, outputStreamCaptor.toString());
    }

    @Test
    public void testPrintGamePlayChoicesShouldPrintChoices () {
        // given
        List<GamePlayInstructions> gamePlayInstructionsList = List.of(
                GamePlayInstructions.LÉP,
                GamePlayInstructions.HALASZTÁS,
                GamePlayInstructions.FORDUL_JOBBRA
        );

        // when
        underTest.printGamePlayChoices(gamePlayInstructionsList);

        // then
        assertEquals(GAMEPLAY_INSTRUCTIONS, outputStreamCaptor.toString());
    }

    @Test
    public void testPrintHighScoreShouldPrintHighScoreInReverseOrder () {
        // given
        List<UserData> userDatas = List.of(
                new UserData("testuser1", 3),
                new UserData("testuser2", 5),
                new UserData("testuser3", 2)
        );

        // when
        underTest.printHighScore(userDatas);

        // then
        assertEquals(HIGH_SCORE, outputStreamCaptor.toString());
    }
}
