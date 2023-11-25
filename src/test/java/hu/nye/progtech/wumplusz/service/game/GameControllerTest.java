package hu.nye.progtech.wumplusz.service.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.enums.Entity;
import hu.nye.progtech.wumplusz.repository.impl.JdbcGameRepository;
import hu.nye.progtech.wumplusz.repository.impl.TxtGameRepository;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.map.MapEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GameControllerTest {

    private static final String USER_NAME = "TestUser";

    private static final String NUMBER_BETWEEN_MESSAGE = "Kérlek 1-6 közötti számot adj meg!\n";

    private static final String GAME_STARTED = "A játék elindult\n";

    private GameController underTest;

    private UserInteractionHandler interactionHandler;

    private GameStore gameStore;

    private MapEditor mapEditor;

    private TxtGameRepository txtGameRepository;

    private JdbcGameRepository jdbcGameRepository;

    private GamePlayController gamePlayController;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        interactionHandler = Mockito.mock(UserInteractionHandler.class);
        gameStore = Mockito.mock(GameStore.class);
        mapEditor = Mockito.mock(MapEditor.class);
        txtGameRepository = Mockito.mock(TxtGameRepository.class);
        jdbcGameRepository = Mockito.mock(JdbcGameRepository.class);
        gamePlayController = Mockito.mock(GamePlayController.class);
        underTest = new GameController(interactionHandler, gameStore, mapEditor, txtGameRepository, jdbcGameRepository, gamePlayController);
    }

    @Test
    public void testHandlePreStartShouldSetUserName() {
        // given
        given(interactionHandler.getUsername()).willReturn(USER_NAME);

        // when
        underTest.handlePreStart();

        // then
        verify(gameStore).setUserName(USER_NAME);
    }

    @Test
    public void testHandleMenuShouldCallEdit() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(1);
        given(mapEditor.handleTableSize()).willReturn(10);

        // when
        underTest.handleMenu();

        // then
        verify(mapEditor).handleTableSize();
        verify(mapEditor).edit();
    }

    @Test
    public void testHandleMenuShouldCallTxtLoad() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(2);

        // when
        underTest.handleMenu();

        // then
        verify(txtGameRepository).load();
    }

    @Test
    public void testHandleMenuShouldCallJdbcLoad() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(3);

        // when
        underTest.handleMenu();

        // then
        verify(jdbcGameRepository).load();
    }

    @Test
    public void testHandleMenuShouldCallJdbcSave() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(4);

        // when
        underTest.handleMenu();

        // then
        verify(jdbcGameRepository).save();
    }

    @Test
    public void testHandleMenuShouldStart() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(5);
        System.setOut(new PrintStream(outputStreamCaptor));

        // when
        underTest.handleMenu();

        // then
        verify(gamePlayController).start();
        assertEquals(GAME_STARTED, outputStreamCaptor.toString());
    }

    @Test
    public void testHandleMenuWithWrongNumberShouldAskForMenuAgain() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(7).willReturn(1);
        System.setOut(new PrintStream(outputStreamCaptor));

        // when
        underTest.handleMenu();

        // then
        verify(mapEditor).edit();
        assertEquals(NUMBER_BETWEEN_MESSAGE, outputStreamCaptor.toString());
    }
}
