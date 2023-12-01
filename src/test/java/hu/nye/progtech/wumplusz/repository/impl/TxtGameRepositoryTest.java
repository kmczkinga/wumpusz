package hu.nye.progtech.wumplusz.repository.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.util.Scanner;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.service.throwable.NoNameThrowable;
import hu.nye.progtech.wumplusz.service.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TxtGameRepositoryTest {

    private TxtGameRepository underTest;

    private static final String TXT_FILE_CONTENT = "9 B 5 E\n" +
            "WWWWWWWWW\n" +
            "W___P___W\n" +
            "WUGP____W\n" +
            "W_______W\n" +
            "W__P____W\n" +
            "W_______W\n" +
            "W__U____W\n" +
            "W_______W\n" +
            "WWWWWWWWW";

    private static final Character[][] MAP = {
            {'W','W','W','W','W','W','W','W','W'},
            {'W','_','_','_','P','_','_','_','W'},
            {'W','U','G','P','_','_','_','_','W'},
            {'W','_','_','_','_','_','_','_','W'},
            {'W','_','_','P','_','_','_','_','W'},
            {'W','_','_','_','_','_','_','_','W'},
            {'W','_','_','U','_','_','_','_','W'},
            {'W','_','_','_','_','_','_','_','W'},
            {'W','W','W','W','W','W','W','W','W'}
    };

    private static final MapVO MAP_VO = new MapVO(9, "B", 4, "E", MAP);

    FileUtil fileUtil;

    @BeforeEach
    public void setUp() {
        fileUtil = Mockito.mock(FileUtil.class);
        underTest = new TxtGameRepository(fileUtil);
    }

    @Test
    public void testSaveShouldSaveMapToTxt() {
        // given

        // when
        underTest.save();

        // then
    }

    @Test
    public void testLoadShouldLoadMapFromTxt() throws NoNameThrowable {
        // given
        Scanner scanner = new Scanner(TXT_FILE_CONTENT);
        given(fileUtil.getScanner()).willReturn(scanner);
        // when
        MapVO result = underTest.load(null);

        // then
        assertEquals(MAP_VO.getSize(), result.getSize());
        for (int i = 0; i < MAP_VO.getSize(); i++) {
            for (int j = 0; j < MAP_VO.getSize(); j++) {
                assertEquals(MAP_VO.getMap()[i][j], result.getMap()[i][j]);
            }
        }
    }
}
