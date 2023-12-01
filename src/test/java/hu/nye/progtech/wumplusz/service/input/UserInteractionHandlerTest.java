package hu.nye.progtech.wumplusz.service.input;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.Entity;
import hu.nye.progtech.wumplusz.model.enums.GamePlayInstructions;
import hu.nye.progtech.wumplusz.model.enums.HeroDirection;
import hu.nye.progtech.wumplusz.service.throwable.ExitChoiceThrowable;
import hu.nye.progtech.wumplusz.service.util.InstructionOutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserInteractionHandlerTest {

    private static final String USER_NAME = "TestUser";

    private static final String INPUT_USER_NAME_MESSAGE = "Add meg a neved: ";

    private static final Integer MENU_POINT = 4;

    private static final String FAL_ENTITY = "FAL";

    private static final String WRONG_ENTITY_NAME = "Progtech";

    private static final List<Entity> ENTITY_LIST = List.of(Entity.WUMPUSZ, Entity.FAL, Entity.HOS);


    private static final String NO_ENTITY_WITH_NAME = "Válassz egy entitást a lehelyezéshez. Kilépéshez: 'KILÉPÉS'\n" +
            "WUMPUSZ (U) FAL (W) HOS (H) \n" +
            "Nem létező entitás, próbáld újra!\n" +
            "Válassz egy entitást a lehelyezéshez. Kilépéshez: 'KILÉPÉS'\n" +
            "WUMPUSZ (U) FAL (W) HOS (H) \n";

    private static final Integer CORRECT_COORDINATE = 5;

    private static final Integer WRONG_COORDINATE = 21;
    private static final String EXIT_STRING = "KILÉPÉS";
    private static final String PICK_AN_ENTITY = "Válassz egy entitást a lehelyezéshez. Kilépéshez: 'KILÉPÉS'\n" +
            "WUMPUSZ (U) FAL (W) HOS (H) \n";

    private static final List<GamePlayInstructions> GAME_PLAY_INSTRUCTIONS = List.of(
            GamePlayInstructions.LÉP,
            GamePlayInstructions.HALASZTÁS,
            GamePlayInstructions.FORDUL_BALRA
    );
    private static final String NOT_EXISTING_INSTUCTION_TWO_TIMES = "Válasz egy akciót:\n" +
            "LÉP HALASZTÁS FORDUL_BALRA \n" +
            "Nem létező instrukció, próbáld újra!\n" +
            "Válasz egy akciót:\n" +
            "LÉP HALASZTÁS FORDUL_BALRA ";
    private static final String NOT_EXISTING_INSTUCTION = "Válasz egy akciót:\n" +
            "LÉP HALASZTÁS FORDUL_BALRA ";


    private UserInteractionHandler underTest;

    @Mock
    private InputReader inputReader;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        underTest = new UserInteractionHandler(inputReader);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testGetUsernameShouldPrintMessage() {
        // given
        given(inputReader.readString()).willReturn(USER_NAME);

        // when
        underTest.getUsername();

        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(INPUT_USER_NAME_MESSAGE, outputStreamCaptor.toString());
    }

    @Test
    public void testGetMenuPointShouldReturnMenuPoint() {
        // given
        given(inputReader.readInteger()).willReturn(MENU_POINT);

        // when
        Integer result = underTest.getMenuPoint();

        // then
        verify(inputReader).readInteger();
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, MENU_POINT);
    }

    @Test
    public void testGetChosenEntityShouldReturnEntity() throws ExitChoiceThrowable {
        // given
        given(inputReader.readString()).willReturn(FAL_ENTITY);

        // when
        Entity result = underTest.getChosenEntity(ENTITY_LIST);

        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, Entity.FAL);
    }

    @Test
    public void testGetChosenEntityShouldPrintWrongEntityAndReturnEntityForSecondTime() throws ExitChoiceThrowable {
        // given
        given(inputReader.readString()).willReturn(WRONG_ENTITY_NAME).willReturn(FAL_ENTITY);
        

        // when
        Entity result = underTest.getChosenEntity(ENTITY_LIST);

        // then
        verify(inputReader, times(2)).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(NO_ENTITY_WITH_NAME, outputStreamCaptor.toString());
        assertEquals(result, Entity.FAL);
    }

    @Test
    public void testGetChosenEntityWithKilepesShouldThrowExitChoiceThrowable() {
        // given
        given(inputReader.readString()).willReturn(EXIT_STRING);

        // when
        ExitChoiceThrowable exitChoiceThrowable = assertThrows(ExitChoiceThrowable.class, () -> underTest.getChosenEntity(ENTITY_LIST),
                "");
        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(PICK_AN_ENTITY, outputStreamCaptor.toString());
        assertNotNull(exitChoiceThrowable);
    }

    @Test
    public void testGetChosenEntityWithLetterKShouldThrowExitChoiceThrowable() {
        // given
        given(inputReader.readString()).willReturn("K");

        // when
        ExitChoiceThrowable exitChoiceThrowable = assertThrows(ExitChoiceThrowable.class, () -> underTest.getChosenEntity(ENTITY_LIST),
                "");
        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(PICK_AN_ENTITY, outputStreamCaptor.toString());
        assertNotNull(exitChoiceThrowable);
    }

    @Test
    public void testGetChosenEntityWithLetterUShouldReturnWumpusz() throws ExitChoiceThrowable {
        // given
        given(inputReader.readString()).willReturn("U");

        // when
        Entity result = underTest.getChosenEntity(ENTITY_LIST);

        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, Entity.WUMPUSZ);
    }

    @Test
    public void testGetCoordinateShouldReturnCoordinateSecondTime() {
        // given
        MapVO mapVO = Mockito.mock(MapVO.class);
        given(inputReader.readInteger()).willReturn(WRONG_COORDINATE).willReturn(CORRECT_COORDINATE);
        given(mapVO.isInsideMap(WRONG_COORDINATE)).willReturn(false);
        given(mapVO.isInsideMap(CORRECT_COORDINATE)).willReturn(true);
        // when
        Integer result = underTest.getCoordinate(mapVO, "X");

        // then
        verify(inputReader, times(2)).readInteger();
        verify(mapVO).isInsideMap(WRONG_COORDINATE);
        verify(mapVO).isInsideMap(CORRECT_COORDINATE);
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, CORRECT_COORDINATE);
    }

    @Test
    public void testGetHeroDirectionWithLetterNShouldReturnHeroDirection(){
        // given
        given(inputReader.readString()).willReturn("N");

        // when
        HeroDirection result = underTest.getHeroDirection();

        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, HeroDirection.N);
    }

    @Test
    public void testGetHeroDirectionWithNotExistingDirectionAndLetterSShouldReturnHeroDirectionAfterTheSecondTime(){
        // given
        given(inputReader.readString()).willReturn("P").willReturn("S");

        // when
        HeroDirection result = underTest.getHeroDirection();

        // then
        verify(inputReader, times(2)).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, HeroDirection.S);
    }

    @Test
    public void testGetChosenGamePlayInstructionShouldReturnInstruction() throws ExitChoiceThrowable {
        // given
        given(inputReader.readString()).willReturn("LÉP");

        // when
        GamePlayInstructions result = underTest.getChosenGamePlayInstruction(GAME_PLAY_INSTRUCTIONS);

        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, GamePlayInstructions.LÉP);
    }

    @Test
    public void testGetChosenGamePlayInstructionWithShortenedShouldReturnInstruction() throws ExitChoiceThrowable {
        // given
        given(inputReader.readString()).willReturn("bal");

        // when
        GamePlayInstructions result = underTest.getChosenGamePlayInstruction(GAME_PLAY_INSTRUCTIONS);

        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, GamePlayInstructions.FORDUL_BALRA);
    }

    @Test
    public void testGetChosenGamePlayInstructionWithWrongInstructionAndFELADShouldThrowExitChoiceThrowableForSecondTime() throws ExitChoiceThrowable {
        // given
        given(inputReader.readString()).willReturn("test").willReturn("FELAD");

        // when
        ExitChoiceThrowable exitChoiceThrowable = assertThrows(ExitChoiceThrowable.class, () -> underTest.getChosenGamePlayInstruction(GAME_PLAY_INSTRUCTIONS),
                "");

        // then
        verify(inputReader, times(2)).readString();
        verifyNoMoreInteractions(inputReader);
        assertNotNull(exitChoiceThrowable);
        assertEquals(NOT_EXISTING_INSTUCTION_TWO_TIMES, outputStreamCaptor.toString());
    }

    @Test
    public void testGetChosenGamePlayInstructionWithfeladShouldThrowExitChoiceThrowable() throws ExitChoiceThrowable {
        // given
        given(inputReader.readString()).willReturn("felad");

        // when
        ExitChoiceThrowable exitChoiceThrowable = assertThrows(ExitChoiceThrowable.class, () -> underTest.getChosenGamePlayInstruction(GAME_PLAY_INSTRUCTIONS),
                "");

        // then
        verify(inputReader).readString();
        verifyNoMoreInteractions(inputReader);
        assertNotNull(exitChoiceThrowable);
        assertEquals(NOT_EXISTING_INSTUCTION, outputStreamCaptor.toString());
    }

    @Test
    public void testGetChosenGamePlayInstructionWithWrongInstructionShouldReturnInstuctionForSecondTime() throws ExitChoiceThrowable {
        // given
        given(inputReader.readString()).willReturn("test").willReturn("LÉP");

        // when
        GamePlayInstructions result = underTest.getChosenGamePlayInstruction(GAME_PLAY_INSTRUCTIONS);


        // then
        verify(inputReader, times(2)).readString();
        verifyNoMoreInteractions(inputReader);
        assertEquals(result, GamePlayInstructions.LÉP);
    }
}
