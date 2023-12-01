package hu.nye.progtech.wumplusz.service.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.UserData;
import hu.nye.progtech.wumplusz.model.enums.Entity;
import hu.nye.progtech.wumplusz.repository.impl.JdbcGameRepository;
import hu.nye.progtech.wumplusz.repository.impl.TxtGameRepository;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.map.MapEditor;
import hu.nye.progtech.wumplusz.service.map.MapValidator;
import hu.nye.progtech.wumplusz.service.throwable.NoNameThrowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GameControllerTest {

    private static final String USER_NAME = "TestUser";

    private static final String NUMBER_BETWEEN_MESSAGE = "Kérlek 1-7 közötti számot adj meg!\n" +
            "Kiléptél!\n";
    private static final String MAP_ALREADY_DONE = "A pálya már készen van!\n" +
            "  0 1 2 3 4 5 6 7 8 9 \n" +
            "0 F F F F F F F F F F \n" +
            "1 F                 F \n" +
            "2 F H     G         F \n" +
            "3 F                 F \n" +
            "4 F                 F \n" +
            "5 F         U       F \n" +
            "6 F                 F \n" +
            "7 F           U     F \n" +
            "8 F                 F \n" +
            "9 F F F F F F F F F F \n" +
            "\n" +
            "Kiléptél!\n";

    private static final List<Entity> ENTITY_LIST = List.of(Entity.WUMPUSZ, Entity.FAL, Entity.HOS);

    private GameController underTest;

    private UserInteractionHandler interactionHandler;

    private GameStore gameStore;

    private MapEditor mapEditor;

    private TxtGameRepository txtGameRepository;

    private JdbcGameRepository jdbcGameRepository;

    private GamePlayController gamePlayController;


    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private static final Character[][] MEDIUM_MAP = new Character[][]{
            {'F','F','F','F','F','F','F','F','F','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ','G',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ','U',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ','U',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F','F','F','F','F','F','F','F','F','F'}
    };

    private MapVO mapVO;

    private MapVO invalidMapVO;

    private UserData userData;

    @BeforeEach
    public void setUp() {
        this.mapVO = new MapVO(10, "B", 2, "N", MEDIUM_MAP);
        this.invalidMapVO = new MapVO(23,  "B", 2, "N", MEDIUM_MAP);
        userData = new UserData(USER_NAME, 2);
        interactionHandler = Mockito.mock(UserInteractionHandler.class);
        gameStore = Mockito.mock(GameStore.class);
        mapEditor = Mockito.mock(MapEditor.class);
        txtGameRepository = Mockito.mock(TxtGameRepository.class);
        jdbcGameRepository = Mockito.mock(JdbcGameRepository.class);
        gamePlayController = Mockito.mock(GamePlayController.class);
        underTest = new GameController(interactionHandler, gameStore, mapEditor, txtGameRepository, jdbcGameRepository, gamePlayController);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testHandlePreStartShouldSetUserNameAndWins() {
        // given
        UserData userData = new UserData(USER_NAME, 0);
        given(interactionHandler.getUsername()).willReturn(USER_NAME);

        // when
        underTest.handlePreStart();

        // then
        verify(gameStore).setUserData(userData);
    }


    @Test
    public void testHandleMenuWithDoneMapShouldNotCallEdit() {
        given(interactionHandler.getMenuPoint()).willReturn(1).willReturn(7);
        given(gameStore.getAvailableEntities()).willReturn(null);
        given(gameStore.getMapVO()).willReturn(mapVO).willReturn(mapVO);
        // when
        underTest.handleMenu();

        // then
        verifyNoInteractions(mapEditor);
        assertEquals(MAP_ALREADY_DONE, outputStreamCaptor.toString());
    }

    @Test
    public void testHandleMenuWithNoMapShouldCallEdit() {
        MapVO mapVO = Mockito.mock(MapVO.class);
        given(interactionHandler.getMenuPoint()).willReturn(1).willReturn(7);
        given(gameStore.getAvailableEntities()).willReturn(null);
        given(gameStore.getMapVO()).willReturn(null).willReturn(null).willReturn(mapVO);
        given(mapVO.getSize()).willReturn(10);
        // when
        underTest.handleMenu();

        // then
        verify(mapEditor).handleTableSize();
        verify(mapEditor).edit();
        verify(gameStore).setAvailableEntities(anyList());
    }

    @Test
    public void testHandleMenuShouldCallEdit() {
        MapVO mapVO = Mockito.mock(MapVO.class);
        given(interactionHandler.getMenuPoint()).willReturn(1).willReturn(7);
        given(gameStore.getAvailableEntities()).willReturn(ENTITY_LIST);
        given(gameStore.getMapVO()).willReturn(mapVO).willReturn(mapVO).willReturn(mapVO);
        // when
        underTest.handleMenu();

        // then
        verify(mapEditor).edit();
        verifyNoMoreInteractions(mapEditor);
    }

    @Test
    public void testHandleMenuShouldCallTxtLoad() throws NoNameThrowable {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(2).willReturn(7);
        given(txtGameRepository.load(null)).willReturn(mapVO);

        // when
        underTest.handleMenu();

        // then
        verify(gameStore).setMapVO(mapVO);
        verify(txtGameRepository).load(null);
    }

    @Test
    public void testHandleMenuShouldNotCallTxtLoadAndExit() throws NoNameThrowable {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(2);
        given(txtGameRepository.load(null)).willReturn(invalidMapVO);

        // when
        underTest.handleMenu();

        // then
        verify(txtGameRepository).load(null);
        verifyNoInteractions(gameStore);
    }

    @Test
    public void testHandleMenuShouldCallJdbcLoad() throws NoNameThrowable {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(3).willReturn(7);
        given(interactionHandler.getUsername()).willReturn(USER_NAME);
        given(jdbcGameRepository.load(USER_NAME)).willReturn(userData);
        // when
        underTest.handleMenu();

        // then
        verify(jdbcGameRepository).load(USER_NAME);
        verify(gameStore).setUserData(userData);
        verifyNoMoreInteractions(gameStore);
        verifyNoMoreInteractions(jdbcGameRepository);
    }

    @Test
    public void testHandleMenuShouldCallJdbcSave() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(4).willReturn(7);

        // when
        underTest.handleMenu();

        // then
        verify(jdbcGameRepository).save();
    }

    @Test
    public void testHandleMenuShouldStart() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(5).willReturn(7);


        // when
        underTest.handleMenu();

        // then
        verify(gamePlayController).start();
    }

    @Test
    public void testHandleMenuWithWrongNumberShouldAskForMenuAgain() {
        // given
        given(interactionHandler.getMenuPoint()).willReturn(8).willReturn(4).willReturn(7);
        System.setOut(new PrintStream(outputStreamCaptor));

        // when
        underTest.handleMenu();

        // then
        verify(jdbcGameRepository).save();
        assertEquals(NUMBER_BETWEEN_MESSAGE, outputStreamCaptor.toString());
    }

}
