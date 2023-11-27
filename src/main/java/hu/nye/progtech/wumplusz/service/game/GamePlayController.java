package hu.nye.progtech.wumplusz.service.game;

import java.util.List;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.GamePlayInstructions;
import hu.nye.progtech.wumplusz.service.throwable.DeathThrowable;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.throwable.ExitChoiceThrowable;
import hu.nye.progtech.wumplusz.service.throwable.VictoryThrowable;

public class GamePlayController {

    private static final List<GamePlayInstructions> GAME_PLAY_INSTRUCTIONS_LIST = List.of(GamePlayInstructions.LÉP,
            GamePlayInstructions.FORDUL_JOBBRA, GamePlayInstructions.FORDUL_BALRA, GamePlayInstructions.LŐ,
            GamePlayInstructions.ARANYAT_FELSZED, GamePlayInstructions.FELAD);

    private final GameStore gameStore;

    private final UserInteractionHandler interactionHandler;

    private final GamePlayStepController gamePlayStepController;

    public GamePlayController(GameStore gameStore, UserInteractionHandler interactionHandler, GamePlayStepController gamePlayStepController) {
        this.gameStore = gameStore;
        this.interactionHandler = interactionHandler;
        this.gamePlayStepController = gamePlayStepController;
    }

    /**
     * Elindítja a játékciklust.
     */
    public void start() {
        System.out.println("A játék elindult");
        gameStore.getMapVO().setStartingArrowCount();
        gameStore.getMapVO().getHero().setStepCount(0);
        gameStep();
    }

    public void gameStep() {
        MapVO mapVO = gameStore.getMapVO();
        while(true) {
            System.out.println(mapVO.toString());

            System.out.println("A hős iránya: " + mapVO.getHero().getHeroDirection().getLabel());
            System.out.println("Nyilak száma: " + mapVO.getHero().getArrowCount());
            System.out.println("Kezdő pozíció: x: " + mapVO.getHero().getStartingRow() + ", y: " + mapVO.getHero().getStartingColumn() + "\n");
            GamePlayInstructions gamePlayInstructions;
            try {
                gamePlayInstructions = interactionHandler.getChosenGamePlayInstruction(GAME_PLAY_INSTRUCTIONS_LIST);
            } catch (ExitChoiceThrowable e) {
                System.out.println("Kilépés");
                return;
            }
            switch (gamePlayInstructions) {
                case LÉP:
                    try {
                        mapVO = gamePlayStepController.step(mapVO);
                    } catch (DeathThrowable deathThrowable) {
                        System.out.println("Meghaltál!\n");
                        return;
                    } catch (VictoryThrowable victoryThrowable) {
                        System.out.println("Visszaértél a kezdő pozícióra, nyertél! Lépéseid száma: " + (gameStore.getMapVO().getHero().getStepCount() + 1) + "\n");
                        gameStore.getUserData().setWins(gameStore.getUserData().getWins() + 1);
                        return;
                    }
                    break;
                case FORDUL_JOBBRA:
                    gamePlayStepController.turnRight(mapVO);
                    break;
                case FORDUL_BALRA:
                    gamePlayStepController.turnLeft(mapVO);
                    break;
                case LŐ:
                    gamePlayStepController.shoot(mapVO);
                    break;
                case ARANYAT_FELSZED:
                    gamePlayStepController.pickUp(mapVO);
                    break;
                case FELAD:
                    return;
            }
            mapVO.getHero().increaseStepCount();
        }

    }




}
