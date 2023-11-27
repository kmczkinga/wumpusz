package hu.nye.progtech.wumplusz.repository.impl;

import static org.junit.jupiter.api.Assertions.assertNull;

import hu.nye.progtech.wumplusz.repository.impl.JdbcGameRepository;
import hu.nye.progtech.wumplusz.repository.impl.TxtGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TxtGameRepositoryTest {

    private TxtGameRepository underTest;

    @BeforeEach
    public void setUp() {
        underTest = new TxtGameRepository();
    }

    @Test
    public void testSaveShouldSaveMapToTxt() {
        // given

        // when
        underTest.save();

        // then
    }

    @Test
    public void testLoadShouldLoadMapFromTxt() {
        // given

        // when
        //String result = underTest.load();

        // then
       // assertNull(result);
    }
}
