package hu.nye.progtech.wumplusz;

import java.util.Scanner;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.repository.impl.JdbcGameRepository;
import hu.nye.progtech.wumplusz.repository.impl.TxtGameRepository;
import hu.nye.progtech.wumplusz.service.game.GameController;
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
        final InputReader inputReader = new InputReader(new Scanner(System.in));
        final UserInteractionHandler interactionHandler = new UserInteractionHandler(inputReader);
        final GameStore gameStore = new GameStore();
        final MapEditor mapEditor = new MapEditor(inputReader, gameStore, interactionHandler);
        final TxtGameRepository txtGameRepository = new TxtGameRepository();
        final JdbcGameRepository jdbcGameRepository = new JdbcGameRepository();
        final GameController gameController = new GameController(interactionHandler, gameStore, mapEditor,
                txtGameRepository, jdbcGameRepository);
        gameController.handlePreStart();
        gameController.handleMenu();
    }
}
