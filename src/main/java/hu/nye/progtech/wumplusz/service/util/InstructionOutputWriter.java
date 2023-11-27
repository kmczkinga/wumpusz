package hu.nye.progtech.wumplusz.service.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import hu.nye.progtech.wumplusz.model.UserData;
import hu.nye.progtech.wumplusz.model.enums.Entity;
import hu.nye.progtech.wumplusz.model.enums.GamePlayInstructions;

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
        System.out.println("6. High score táblázat");
        System.out.println("7. Kilépés");
    }

    /**
     * Kiírja a választható entitásokat, amik a listában megtalálhatóak.
     */
    public static void printEntity(List<Entity> availableEntities) {
        System.out.println("Válassz egy entitást a lehelyezéshez. Kilépéshez: 'KILÉPÉS'");
        for (Entity entity : availableEntities) {
            System.out.print(entity.name() + " (" + entity.getLabel() + ") ");
        }
        System.out.println();
    }

    /**
     * Kiírja, hogy milyen koordinátát írjon be a felhasználó.
     */
    public static void printCoordinate(String coordinateType) {
        System.out.printf("Add meg a %s koordinátát: ", coordinateType);
    }

    public static void printGetHeroDirection() {
        System.out.println("Adja meg a hős kezdezi irányát (N/W/S/E)");
    }

    public static void printGamePlayChoices(List<GamePlayInstructions> gamePlayInstructionsList) {
        System.out.println("Válasz egy akciót:");
        for (GamePlayInstructions gamePlayInstructions : gamePlayInstructionsList) {
            System.out.print(gamePlayInstructions + " ");
        }
    }

    public static void printLoadUsername() {
        System.out.println("Adj meg egy nevet, amit be szerentél tölteni!");
    }

    public static void printHighScore(List<UserData> userDatas) {
        System.out.println("Highscore: ");
        List<UserData> sorted =
                userDatas.stream().sorted(Comparator.comparing(UserData::getWins)).collect(Collectors.toList());
        for(int i = sorted.size() - 1; i >= 0; i--) {
            System.out.println(sorted.get(i).getUsername() + ": " + sorted.get(i).getWins() + " nyerés");
        }
        System.out.println();
    }
}
