package hu.nye.progtech.wumplusz.service.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.Hero;
import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.UserData;
import hu.nye.progtech.wumplusz.model.enums.GamePlayInstructions;
import hu.nye.progtech.wumplusz.model.enums.HeroDirection;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.throwable.DeathThrowable;
import hu.nye.progtech.wumplusz.service.throwable.ExitChoiceThrowable;
import hu.nye.progtech.wumplusz.service.throwable.VictoryThrowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GamePlayControllerTest {

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

    private static final List<GamePlayInstructions> GAME_PLAY_INSTRUCTIONS = List.of(
            GamePlayInstructions.LÉP,
            GamePlayInstructions.HALASZTÁS,
            GamePlayInstructions.FORDUL_BALRA
    );
    private static final String GAME_STARTED = "A játék elindult\n" +
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
            "A hős iránya: észak\n" +
            "Nyilak száma: 2\n" +
            "Kezdő pozíció: x: 2, y: 1\n" +
            "\n" +
            "Kilépés\n";
    private static final String DEATH = "  0 1 2 3 4 5 6 7 8 9 \n" +
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
            "A hős iránya: észak\n" +
            "Nyilak száma: 2\n" +
            "Kezdő pozíció: x: 2, y: 1\n" +
            "\n" +
            "Meghaltál!\n" +
            "\n";
    private static final String VICTORY = "  0 1 2 3 4 5 6 7 8 9 \n" +
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
            "A hős iránya: észak\n" +
            "Nyilak száma: 2\n" +
            "Kezdő pozíció: x: 2, y: 1\n" +
            "\n" +
            "Visszaértél a kezdő pozícióra, nyertél! Lépéseid száma: 11\n" +
            "\n";

    private MapVO mapVO;

    private GamePlayController underTest;

    private GamePlayStepController gamePlayStepController;

    private UserInteractionHandler userInteractionHandler;

    private GameStore gameStore;
    
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        this.mapVO = new MapVO(10, "B", 2, "N", MEDIUM_MAP);
        this.mapVO.getHero().setStepCount(10);
        this.mapVO.setStartingArrowCount();
        this.gamePlayStepController = Mockito.mock(GamePlayStepController.class);
        this.userInteractionHandler = Mockito.mock(UserInteractionHandler.class);
        this.gameStore = Mockito.mock(GameStore.class);
        this.underTest = new GamePlayController(gameStore, userInteractionHandler, gamePlayStepController);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testStartShouldStartGameAndExit() throws ExitChoiceThrowable {
        // given
        given(gameStore.getMapVO()).willReturn(mapVO).willReturn(mapVO);
        given(userInteractionHandler.getChosenGamePlayInstruction(GamePlayController.GAME_PLAY_INSTRUCTIONS_LIST)).willThrow(ExitChoiceThrowable.class);
        // when
        underTest.start();
        // then
        verify(gameStore, times(3)).getMapVO();
        assertEquals(GAME_STARTED ,outputStreamCaptor.toString());
    }

    @Test
    public void testGameStepShouldLEP() throws ExitChoiceThrowable, DeathThrowable, VictoryThrowable {
        // given
        given(gameStore.getMapVO()).willReturn(mapVO);
        given(userInteractionHandler.getChosenGamePlayInstruction(GamePlayController.GAME_PLAY_INSTRUCTIONS_LIST)).willReturn(GamePlayInstructions.LÉP).willThrow(ExitChoiceThrowable.class);
        given(gamePlayStepController.step(mapVO)).willReturn(mapVO);
        // when
        underTest.gameStep();
        // then
        verify(gamePlayStepController).step(mapVO);
    }

    @Test
    public void testGameStepShouldLEPAndDie() throws ExitChoiceThrowable, DeathThrowable, VictoryThrowable {
        // given
        given(gameStore.getMapVO()).willReturn(mapVO);
        given(userInteractionHandler.getChosenGamePlayInstruction(GamePlayController.GAME_PLAY_INSTRUCTIONS_LIST)).willReturn(GamePlayInstructions.LÉP).willThrow(ExitChoiceThrowable.class);
        given(gamePlayStepController.step(mapVO)).willThrow(DeathThrowable.class);
        // when
        underTest.gameStep();
        // then
        verify(gamePlayStepController).step(mapVO);
        assertEquals(DEATH, outputStreamCaptor.toString());
    }

    @Test
    public void testGameStepShouldLEPAndVictory() throws ExitChoiceThrowable, DeathThrowable, VictoryThrowable {
        // given
        UserData userData = Mockito.mock(UserData.class);
        given(gameStore.getMapVO()).willReturn(mapVO);
        given(userInteractionHandler.getChosenGamePlayInstruction(GamePlayController.GAME_PLAY_INSTRUCTIONS_LIST)).willReturn(GamePlayInstructions.LÉP).willThrow(ExitChoiceThrowable.class);
        given(gamePlayStepController.step(mapVO)).willThrow(VictoryThrowable.class);
        given(gameStore.getUserData()).willReturn(userData);
        given(userData.getWins()).willReturn(1);
        // when
        underTest.gameStep();
        // then
        verify(gamePlayStepController).step(mapVO);
        assertEquals(VICTORY, outputStreamCaptor.toString());
    }

    @Test
    public void testGameStepShouldFORDULBALRA() throws ExitChoiceThrowable {
        // given
        given(gameStore.getMapVO()).willReturn(mapVO);
        given(userInteractionHandler.getChosenGamePlayInstruction(GamePlayController.GAME_PLAY_INSTRUCTIONS_LIST)).willReturn(GamePlayInstructions.FORDUL_BALRA).willThrow(ExitChoiceThrowable.class);
        // when
        underTest.gameStep();
        // then
        verify(gamePlayStepController).turnLeft(mapVO);
    }

    @Test
    public void testGameStepShouldFORDULJOBBRA() throws ExitChoiceThrowable {
        // given
        given(gameStore.getMapVO()).willReturn(mapVO);
        given(userInteractionHandler.getChosenGamePlayInstruction(GamePlayController.GAME_PLAY_INSTRUCTIONS_LIST)).willReturn(GamePlayInstructions.FORDUL_JOBBRA).willThrow(ExitChoiceThrowable.class);
        // when
        underTest.gameStep();
        // then
        verify(gamePlayStepController).turnRight(mapVO);
    }

    @Test
    public void testGameStepShouldLO() throws ExitChoiceThrowable {
        // given
        given(gameStore.getMapVO()).willReturn(mapVO);
        given(userInteractionHandler.getChosenGamePlayInstruction(GamePlayController.GAME_PLAY_INSTRUCTIONS_LIST)).willReturn(GamePlayInstructions.LŐ).willThrow(ExitChoiceThrowable.class);
        // when
        underTest.gameStep();
        // then
        verify(gamePlayStepController).shoot(mapVO);
    }


    @Test
    public void testGameStepShouldFELSZED() throws ExitChoiceThrowable {
        // given
        given(gameStore.getMapVO()).willReturn(mapVO);
        given(userInteractionHandler.getChosenGamePlayInstruction(GamePlayController.GAME_PLAY_INSTRUCTIONS_LIST)).willReturn(GamePlayInstructions.ARANYAT_FELSZED).willThrow(ExitChoiceThrowable.class);
        // when
        underTest.gameStep();
        // then
        verify(gamePlayStepController).pickUp(mapVO);
    }
}
