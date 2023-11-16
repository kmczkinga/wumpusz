package hu.nye.progtech.wumplusz.service.input;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.Entity;
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

    private static final String ENTITY_NAME = "FAL";

    private static final String WRONG_ENTITY_NAME = "Progtech";

    private static final Entity ENTITY = Entity.FAL;

    private static final List<Entity> ENTITY_LIST = List.of(Entity.WUMPUSZ, Entity.FAL, Entity.HOS);

    private static final String NO_ENTITY_WITH_NAME = "Válassz egy entitást a lehelyezéshez:\n" +
            "WUMPUSZ FAL HOS \n" +
            "Nem létező entitás, próbáld újra!\n" +
            "Válassz egy entitást a lehelyezéshez:\n" +
            "WUMPUSZ FAL HOS \n";

    private static final Integer CORRECT_COORDINATE = 5;

    private static final Integer WRONG_COORDINATE = 21;

    private UserInteractionHandler underTest;

    @Mock
    private InputReader inputReader;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        underTest = new UserInteractionHandler(inputReader);
    }

    @Test
    public void testGetUsernameShouldPrintMessage() {
        // given
        System.setOut(new PrintStream(outputStreamCaptor));
        given(inputReader.readString()).willReturn(USER_NAME);

        // when
        underTest.getUsername();

        // then
        verify(inputReader).readString();
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
        assertEquals(result, MENU_POINT);
    }

    @Test
    public void testGetChosenEntityShouldReturnEntity() {
        // given
        given(inputReader.readString()).willReturn(ENTITY_NAME);

        // when
        //Entity result = underTest.getChosenEntity(ENTITY_LIST);

        // then
        verify(inputReader).readString();
        //assertEquals(result, ENTITY);
    }

    @Test
    public void testGetChosenEntityShouldPrintWrongEntityAndReturnEntityForSecondTime() {
        // given
        given(inputReader.readString()).willReturn(WRONG_ENTITY_NAME).willReturn(ENTITY_NAME);
        System.setOut(new PrintStream(outputStreamCaptor));

        // when
       // Entity result = underTest.getChosenEntity(ENTITY_LIST);

        // then
        verify(inputReader, times(2)).readString();
        assertEquals(NO_ENTITY_WITH_NAME, outputStreamCaptor.toString());
        //assertEquals(result, ENTITY);
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
        assertEquals(result, CORRECT_COORDINATE);
    }
}
