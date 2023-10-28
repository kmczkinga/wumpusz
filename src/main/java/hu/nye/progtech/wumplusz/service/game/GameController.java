package hu.nye.progtech.wumplusz.service.game;

import java.util.InputMismatchException;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.repository.impl.JdbcGameRepository;
import hu.nye.progtech.wumplusz.repository.impl.TxtGameRepository;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.map.MapEditor;

/**
 * Komponens, ami a játék menetét vezérli.
 */

public class GameController {

    private final UserInteractionHandler interactionHandler;

    private final GameStore gameStore;

    private final MapEditor mapEditor;

    private final TxtGameRepository txtGameRepository;

    private final JdbcGameRepository jdbcGameRepository;


    public GameController(UserInteractionHandler interactionHandler, GameStore gameStore, MapEditor mapEditor,
                          TxtGameRepository txtGameRepository, JdbcGameRepository jdbcGameRepository) {
        this.interactionHandler = interactionHandler;
        this.gameStore = gameStore;
        this.mapEditor = mapEditor;
        this.txtGameRepository = txtGameRepository;
        this.jdbcGameRepository = jdbcGameRepository;
    }

    /**
     * A játék előtti folyamatot vezérli.
     * Ez a játékosnév bekérése.
     */
    public void handlePreStart() {
        final String userName = interactionHandler.getUsername();
        gameStore.setUserName(userName);
    }

    /**
     * Lekéri a kiválasztott menüpontot, átadja a feladatot az adott komponenseknek.
     */
    public void handleMenu() {
        Integer chosenMenu = interactionHandler.getMenuPoint();
        switch (chosenMenu) {
            case 1:
                mapEditor.handleTableSize();
                mapEditor.edit();
                break;
            case 2:
                txtGameRepository.load();
                break;
            case 3:
                jdbcGameRepository.load();
                break;
            case 4:
                jdbcGameRepository.save();
                break;
            case 5:
                start();
                break;
            case 6:
                System.exit(0);
                break;
            default:
                System.out.println("Kérlek 1-6 közötti számot adj meg!");
                handleMenu();
                break;
        }
    }

    /**
     * Elindítja a játékciklust.
     */
    public void start() {
        System.out.println("A játék elindult");
    }
}
