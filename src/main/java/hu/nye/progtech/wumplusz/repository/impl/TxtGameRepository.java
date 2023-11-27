package hu.nye.progtech.wumplusz.repository.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.repository.GameRepository;
import hu.nye.progtech.wumplusz.service.throwable.NoNameThrowable;

/**
 * Komponens, amely betölti, elmenti a játékot txt fájlba.
 */
public class TxtGameRepository implements GameRepository<MapVO> {

    private static final String FILE_NAME = "wumpluszinput.txt";

    private Scanner scanner;

    private InputStream inputStream;

    /**
     * Elmenti az adott játékot.
     * Ezt nem használjuk.
     */
    @Override
    public void save() {

    }

    /**
     * Betölti az adott játékot.
     */
    @Override
    public MapVO load(String username) throws NoNameThrowable {
        return readFile();
    }

    /**
     * Beolvassa az adott txt fájlt.
     * Végigiterál a fájl sorain.
     * Az első sorból kiszedi a pálya méretét,
     * hős oszlopát, sorát, irányát.
     * A többi sor a pályát írja le.
     *
     * Ha nem tudja jól beolvasni, leállítja a programot.
     * Ha sikeres, visszaadja a MapVO-t és kiírja, hogy sikeres.
     */
    private MapVO readFile() {
        Integer mapSize = null;
        String column = null;
        Integer row = null;
        String heroDirection = null;
        String currentRow = null;
        Character[][] map = null;
        scanner = getScanner();
        int i = 0;
        try {
            while (scanner.hasNextLine()) {
                if (i == 0) {
                    String[] firstRow = scanner.nextLine().split(" ");
                    mapSize = Integer.valueOf(firstRow[0]);
                    column = firstRow[1];
                    row = Integer.valueOf(firstRow[2]);
                    heroDirection = firstRow[3];
                    map = new Character[mapSize][mapSize];
                } else {
                    currentRow = scanner.nextLine();
                    for (int j = 0; j < mapSize; j++) {
                        map[i - 1][j] = currentRow.charAt(j);
                    }
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("Hibás txt fájl! Javítsd ki, majd indítsd újra a játékot!");
            System.exit(0);
        }

        scanner.close();
        System.out.println("Sikeres beolvasás!");
        return new MapVO(mapSize, column, row - 1, heroDirection, map);
    }

    /**
     * Megnyitja a fájlt egy Scanner-rel.
     *
     * Ha nem létezik, nem találja a fájlt, leállítja a programot.
     */
    private Scanner getScanner() {
        inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        if (inputStream == null) {
            System.out.println("A fájl nem létezik! Tedd a helyére, majd indítsd újra a játékot!");
            System.exit(0);
        }
        return new Scanner(inputStream);
    }
}
