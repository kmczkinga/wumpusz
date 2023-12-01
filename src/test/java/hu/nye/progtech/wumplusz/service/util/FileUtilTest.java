package hu.nye.progtech.wumplusz.service.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileUtilTest {

    private FileUtil underTest;

    @BeforeEach
    public void setUp() {
        underTest = new FileUtil();
    }

    @Test
    public void testGetScannerShouldReturnScanner() {
        // given

        // when
        Scanner result = underTest.getScanner();

        // then
        assertNotNull(result);
    }
}
