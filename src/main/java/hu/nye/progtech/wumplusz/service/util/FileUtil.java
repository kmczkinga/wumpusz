package hu.nye.progtech.wumplusz.service.util;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Komponens, amely a fájl beolvasásához szükséges.
 */
public class FileUtil {

    private static final String FILE_NAME = "wumpluszinput.txt";

    private InputStream inputStream;

    public FileUtil() {
        this.inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        if (inputStream == null) {
            System.out.println("A fájl nem létezik! Tedd a helyére, majd indítsd újra a játékot!");
        }
    }

    /**
     * Megnyitja a fájlt egy Scanner-rel.
     *
     * Ha nem létezik, nem találja a fájlt, leállítja a programot.
     */

    public Scanner getScanner() {
        return new Scanner(inputStream);
    }
}
