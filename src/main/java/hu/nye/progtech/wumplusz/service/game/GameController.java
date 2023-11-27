package hu.nye.progtech.wumplusz.service.game;

import java.util.InputMismatchException;
import java.util.List;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.UserData;
import hu.nye.progtech.wumplusz.repository.impl.JdbcGameRepository;
import hu.nye.progtech.wumplusz.repository.impl.TxtGameRepository;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.map.MapEditor;
import hu.nye.progtech.wumplusz.service.map.MapValidator;
import hu.nye.progtech.wumplusz.service.throwable.NoNameThrowable;
import hu.nye.progtech.wumplusz.service.util.InstructionOutputWriter;

/**
 * Komponens, ami a játék menetét vezérli.
 */

public class GameController {

    private final UserInteractionHandler interactionHandler;

    private final GameStore gameStore;

    private final MapEditor mapEditor;

    private final TxtGameRepository txtGameRepository;

    private final JdbcGameRepository jdbcGameRepository;

    private final GamePlayController gamePlayController;


    public GameController(UserInteractionHandler interactionHandler, GameStore gameStore, MapEditor mapEditor,
                          TxtGameRepository txtGameRepository, JdbcGameRepository
                                  jdbcGameRepository, GamePlayController gamePlayController) {
        this.interactionHandler = interactionHandler;
        this.gameStore = gameStore;
        this.mapEditor = mapEditor;
        this.txtGameRepository = txtGameRepository;
        this.jdbcGameRepository = jdbcGameRepository;
        this.gamePlayController = gamePlayController;
    }

    /**
     * A játék előtti folyamatot vezérli.
     * Ez a játékosnév bekérése.
     */
    public void handlePreStart() {
        String userName = interactionHandler.getUsername();
        UserData userData = new UserData(userName, 0);
        gameStore.setUserData(userData);
    }

    /**
     * Lekéri a kiválasztott menüpontot, átadja a feladatot az adott komponenseknek.
     */
    public void handleMenu() {
        Integer chosenMenu = interactionHandler.getMenuPoint();
        switch (chosenMenu) {
            case 1:
                if (gameStore.getAvailableEntities() == null) {
                    System.out.println("A pálya már készen van!");
                    System.out.println(gameStore.getMapVO().toString());
                    handleMenu();
                }
                if (gameStore.getMapVO() == null) {
                    mapEditor.handleTableSize();
                    gameStore.setAvailableEntities(mapEditor.createAvailableEntityList(gameStore.getMapVO().getSize()));
                } else {
                    System.out.println(gameStore.getMapVO().toString());
                }
                mapEditor.edit();
                handleMenu();
                break;
            case 2:
                try {
                    MapVO mapVO = txtGameRepository.load(null);
                    if (MapValidator.isValid(mapVO)) {
                        gameStore.setMapVO(mapVO);
                    } else {
                        System.out.println("Hibás txt fájl! Az entitás számok nem megfelelőek! Javítsd ki, majd indítsd újra a játékot!");
                        System.exit(0);
                    }
                } catch (NoNameThrowable noNameThrowable) {
                    // Ezt nem használjuk
                }
                handleMenu();
                break;
            case 3:
                String username = interactionHandler.getUsername();
                UserData userData = null;
                try {
                    userData = jdbcGameRepository.load(username);
                    gameStore.setUserData(userData);
                    handleMenu();
                } catch (NoNameThrowable noNameThrowable) {
                    handleMenu();
                }

                break;
            case 4:
                jdbcGameRepository.save();
                handleMenu();
                break;
            case 5:
                gamePlayController.start();
                handleMenu();
                break;
            case 6:
                List<UserData> users = jdbcGameRepository.readAllUsers();
                InstructionOutputWriter.printHighScore(users);
                handleMenu();
                break;
            case 7:
                System.exit(0);
                break;
            default:
                System.out.println("Kérlek 1-6 közötti számot adj meg!");
                handleMenu();
                break;
        }
    }
}
