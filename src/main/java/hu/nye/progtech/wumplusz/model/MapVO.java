package hu.nye.progtech.wumplusz.model;

import hu.nye.progtech.wumplusz.model.enums.Entity;


/**
 * VO, amely a játéktáblát jelképezi.
 * Tárolja a tábla méretét, és magát a táblát.
 */
public class MapVO {

    private final Integer size;

    private Character[][] map;

    public MapVO(Integer size) {
        this.size = size;
        this.map = new Character[size][size];
        createStartingMap();
    }

    /**
     * Hozzáad egy entitást a pálya megadott helyre.
     */
    public void addEntity(Entity entity, Integer i, Integer j) {
        this.map[i][j] = entity.getLabel();
    }

    /**
     * Megnézi, hogy letehető-e az adott entitás a megadott helyre.
     */
    public Boolean isEntityPlaceable(Character entityLabel, Integer i, Integer j) {
        return map[i][j] == entityLabel || map[i][j] == ' ';
    }

    /**
     * Megnézi, hogy a megadott koordináta a pályán belül van-e.
     */
    public Boolean isInsideMap(Integer coordinate) {
        return coordinate >= 0 && coordinate <= size - 1;
    }

    /**
     * Visszaadja a pálya méretét.
     */
    public Integer getSize() {
        return this.size;
    }


    /**
     * A pálya kiíratásához használatos.
     */

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("  ");
        if (size > 10) {
            result.append(" ");
        }
        for (int i = 0; i < size; i++) {
            result.append(i + " ");
        }
        result.append("\n");
        for (int i = 0; i < size; i++) {
            if (size < 11 || i > 9) {
                result.append(i + " ");
            } else {
                result.append(i + "  ");
            }
            for (int j = 0; j < size; j++) {
                if (j > 9) {
                    result.append(map[i][j] + "  ");
                } else {
                    result.append(map[i][j] + " ");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }


    /**
     * Létrehozza a kezdő pályát.
     * A pálya széleit fallal tölti fel, a többit szőközzel.
     * A szóköz jelképezi az üres helyet.
     */
    private void createStartingMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0 || j == 0 || i == size - 1 || j == size - 1) {
                    map[i][j] = 'F';
                } else {
                    map[i][j] = ' ';
                }
            }
        }
    }
}
