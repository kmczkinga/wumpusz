package hu.nye.progtech.wumplusz.service.util;

import java.util.List;

import hu.nye.progtech.wumplusz.model.enums.Entity;

/**
 * Util osztály, amely az interakció előtti utasítások kiíratásáért felelős.
 */
public class InstructionOutputWriter {

    public static final String X_COORDINATE = "X";

    public static final String Y_COORDINATE = "Y";

    /**
     * Kiírja a kiválasztható menüpontokat.
     */
    public static void printMenu() {
        System.out.println("Válassz a menüpontokból:");
        System.out.println("1. Pályaszerkesztés");
        System.out.println("2. Beolvasás fájlból");
        System.out.println("3. Betöltés adatbázisból");
        System.out.println("4. Mentés adatbázisba");
        System.out.println("5. Játék");
        System.out.println("6. Kilépés");
    }

    /**
     * Kiírja a választható entitásokat, amik a listában megtalálhatóak.
     */
    public static void printEntity(List<Entity> availableEntities) {
        System.out.println("Válassz egy entitást a lehelyezéshez:");
        for (Entity entity : availableEntities) {
            System.out.print(entity.name() + " ");
        }
        System.out.println();
    }

    /**
     * Kiírja, hogy milyen koordinátát írjon be a felhasználó.
     */
    public static void printCoordinate(String coordinateType) {
        System.out.printf("Add meg a %s koordinátát: ", coordinateType);
    }
}
