package hu.nye.progtech.wumplusz.model;

import java.util.Arrays;
import java.util.Objects;

import hu.nye.progtech.wumplusz.model.enums.Entity;
import hu.nye.progtech.wumplusz.model.enums.HeroDirection;


/**
 * VO, amely a játéktáblát jelképezi.
 * Tárolja a tábla méretét, és magát a táblát.
 */
public class MapVO {

    private final Integer size;

    private Hero hero;

    private Character[][] map;

    public MapVO(Integer size) {
        this.size = size;
        this.map = new Character[size][size];
        createStartingMap();
    }

    public MapVO(Integer size, String heroColumn, Integer heroRow, String heroDirection, Character[][] map) {
        this.size = size;
        this.map = map;
        this.hero = new Hero(HeroDirection.valueOf(heroDirection), heroRow, columnStringToInt(heroColumn), false);
        placeHeroWithString(heroColumn, heroRow);
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
     * Visszaadja az entitást az adott koordinátáról.
     */
    public Character getEntity(int x, int y) {
        return map[y][x];
    }

    /**
     * Megnézi, hogy a megadott koordináta a pályán belül van-e.
     */
    public Boolean isInsideMap(Integer coordinate) {
        return coordinate >= 0 && coordinate <= size - 1;
    }

    /**
     * Megnézi, hogy a pálya tele van-e.
     */
    public Boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Beállítja a hős kezdő nyilainak számát a wumpusz számok alapján.
     */
    public void setStartingArrowCount() {
        Integer wumpusCount = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getEntity(i, j).equals('U')) {
                    wumpusCount++;
                }
            }
        }
        this.hero.setArrowCount(wumpusCount);
    }

    /**
     * Beállítja a hero-t.
     */
    public void setHero(Hero hero) {
        this.hero = hero;
    }

    /**
     * Visszaadja a pálya méretét.
     */
    public Integer getSize() {
        return this.size;
    }

    /**
     * Kitörli az entitást az adott koordinátáról.
     */
    public void deleteEntity(Integer column, Integer row) {
        this.map[row][column] = '_';
    }

    /**
     * Visszaadja a hőst.
     */
    public Hero getHero() {
        return hero;
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

    /**
        Betű oszlop alapján leteszi a hőst.
     */
    public void placeHeroWithString(String columnString, Integer row) {
        Integer column = columnStringToInt(columnString);
        placeHero(column, row);
    }

    /**
     * Visszaadja a mapot.
     */
    public Character[][] getMap() {
        return map;
    }

    /**
     * Az ASCII táblázat alapján átalakítja a betű koordinátát számmá.
     */
    private Integer columnStringToInt(String columnString) {
        return columnString.charAt(0) - 'A';
    }

    /**
     * Leteszi a hőst.
     */
    public void placeHero(Integer column, Integer row) {
        this.map[row][column] = 'H';
        this.hero.setHeroRow(row);
        this.hero.setHeroColumn(column);
    }

    /**
     * Beállítja a mapot.
     */
    public void setMap(Character[][] map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapVO mapVO = (MapVO) o;
        return Objects.equals(size, mapVO.size) && Objects.equals(hero, mapVO.hero) && Arrays.equals(map, mapVO.map);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size, hero);
        result = 31 * result + Arrays.hashCode(map);
        return result;
    }
}
