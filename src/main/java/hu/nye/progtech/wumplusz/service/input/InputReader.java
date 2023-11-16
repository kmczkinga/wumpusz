package hu.nye.progtech.wumplusz.service.input;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Komponens, ami beolvassa a felhasználó bevitelt konzolról.
 */

public class InputReader {

    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Beolvassa az inputot konzolról, majd Stringként visszaadja.
     * Ha a Scanner nem talál sort, vagy le van zárva, akkor null-lal tér vissza.
     *
     * @return a felhasználó neve Stringként.
     */

    public String readString() {
        String input = null;
        try {
            input = scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            System.err.println("Hiba a beolvasás közben");
        }
        return input;
    }

    /**
     * Beolvassa a következő integert az inputról.
     * Ha nem számot adott meg, újra bekéri.
     */
    public Integer readInteger() {
        Integer input;
        while (true) {
            try {
                input = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Számot adj meg!");
            }
        }
        return input;
    }
}
