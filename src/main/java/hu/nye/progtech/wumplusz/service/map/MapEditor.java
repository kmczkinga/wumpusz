package hu.nye.progtech.wumplusz.service.map;

import java.util.ArrayList;
import java.util.List;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.Entity;
import hu.nye.progtech.wumplusz.service.input.InputReader;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.util.InstructionOutputWriter;

/**
 * Komponens, amely a pálya módosítására használatos.
 */
public class MapEditor {

    private final InputReader inputReader;

    private final GameStore gameStore;

    private final UserInteractionHandler interactionHandler;

    public MapEditor(InputReader inputReader, GameStore gameStore, UserInteractionHandler interactionHandler) {
        this.inputReader = inputReader;
        this.gameStore = gameStore;
        this.interactionHandler = interactionHandler;
    }


    /**
     * Ez vezényeli le a pályaszerkesztést.
     * - Először létrehozza az elérhető entitásokat, majd
     *   beállítja a gamestore-ban azokat.
     * - Bekéri a felhasználótól az entitást, x, y koordinátát,
     *   majd ha az az adott pontra letehető a pályán,
     *   akkor leteszi azt oda.
     * - Ha nem FAL, vagy VEREM entitás, akkor ki is törli azt
     *   az elérhető entitások listájából.
     * - Kiírja az egyes letevések után a pályát is.
     * - Ezt addig csinálja, amíg az elérhető entitások listájában
     *   már csak a FAL és VEREM marad, mivel ezekből bármennyit le
     *   tehet.
     */
    public void edit() {
        MapVO mapVO = gameStore.getMapVO();
        gameStore.setAvailableEntities(createAvailableEntityList(mapVO.getSize()));
        Entity chosenEntity;
        Integer x;
        Integer y;
        List<Entity> availableEntities;
        do {
            availableEntities = gameStore.getAvailableEntities();
            chosenEntity = interactionHandler.getChosenEntity(availableEntities);
            x = interactionHandler.getCoordinate(mapVO, InstructionOutputWriter.X_COORDINATE);
            y = interactionHandler.getCoordinate(mapVO, InstructionOutputWriter.Y_COORDINATE);
            if (mapVO.isEntityPlaceable(chosenEntity.getLabel(), x, y)) {
                mapVO.addEntity(chosenEntity, x, y);
                if (!Entity.FAL.equals(chosenEntity) && !Entity.VEREM.equals(chosenEntity)) {
                     gameStore.removeEntity(chosenEntity.name());
                }
            } else {
                System.out.println("Nem tehető ide ez az entitás, próbáld máshová!");
            }
            System.out.println(gameStore.getMapVO().toString());

        } while (availableEntities.size() > 2 && availableEntities.contains(Entity.VEREM) && availableEntities.contains(Entity.FAL));
    }

    /**
     * Lekéri, beállítja a tábla méretét, majd létrehozza a MapVO-t.
     * Ha nem 6-20 közötti számot kap meg, akkor addig bekéri,
     * amig nem olyat kap meg.
     * Majd elmenti a gamestore-ba a mapvo-t, kiírja a mapot.
     */
    public Integer handleTableSize() {
        System.out.print("A pálya méretét ");
        Integer tableSize = null;
        do {
            System.out.println("6-20-ig add meg!");
            tableSize = inputReader.readInteger();
        } while (!(tableSize >= 6 && tableSize <= 20));
        gameStore.setMapVO(new MapVO(tableSize));
        System.out.println(gameStore.getMapVO().toString());
        return tableSize;
    }

    /**
     * Létrehozza a letehető entitások listáját.
     * A wumpuszok száma attól függ, hogy mekkora a pálya mérete.
     * Pálya méret - wumpuszok száma
     *     6-8     -       1
     *     9-14     -      2
     *     14-20     -     3
     */
    public List<Entity> createAvailableEntityList(Integer mapSize) {
        List<Entity> result = new ArrayList<>();
        Integer wumpusCount = 0;
        if (mapSize <= 8) {
            wumpusCount = 1;
        } else if (mapSize >= 9 && mapSize <= 14) {
            wumpusCount = 2;
        } else {
            wumpusCount = 3;
        }
        for (int i = 0; i < wumpusCount; i++) {
            result.add(Entity.WUMPUSZ);
        }
        result.add(Entity.ARANY);
        result.add(Entity.HOS);
        result.add(Entity.FAL);
        result.add(Entity.VEREM);
        return result;
    }
}
