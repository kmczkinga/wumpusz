package hu.nye.progtech.wumplusz;

import java.util.Scanner;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.repository.impl.JdbcGameRepository;
import hu.nye.progtech.wumplusz.repository.impl.TxtGameRepository;
import hu.nye.progtech.wumplusz.service.game.GameController;
import hu.nye.progtech.wumplusz.service.game.GamePlayController;
import hu.nye.progtech.wumplusz.service.game.GamePlayStepController;
import hu.nye.progtech.wumplusz.service.input.InputReader;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.map.MapEditor;

/**
 * Belépési pont a Wumplusz játékba.
 */
public class Main {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok
     */
    public static void main(String[] args) {
        InputReader inputReader = new InputReader(new Scanner(System.in));
        UserInteractionHandler interactionHandler = new UserInteractionHandler(inputReader);
        GameStore gameStore = new GameStore();
        MapEditor mapEditor = new MapEditor(inputReader, gameStore, interactionHandler);
        TxtGameRepository txtGameRepository = new TxtGameRepository();
        JdbcGameRepository jdbcGameRepository = new JdbcGameRepository();
        GamePlayStepController gamePlayStepController = new GamePlayStepController();
        GamePlayController gamePlayController = new GamePlayController(gameStore, interactionHandler, gamePlayStepController);
        GameController gameController = new GameController(interactionHandler, gameStore, mapEditor,
                txtGameRepository, jdbcGameRepository, gamePlayController);
        gameController.handlePreStart();
        gameController.handleMenu();
    }
}
