package hu.nye.progtech.wumplusz.repository.impl;

import static org.junit.jupiter.api.Assertions.assertNull;

import hu.nye.progtech.wumplusz.repository.impl.JdbcGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JdbcGameRepositoryTest {

    private JdbcGameRepository underTest;

    @BeforeEach
    public void setUp() {
        underTest = new JdbcGameRepository();
    }

    @Test
    public void testSaveShouldSaveMapToJdbc() {
        // given

        // when
        underTest.save();

        // then
    }

    @Test
    public void testLoadShouldLoadMapFromJdbc() {
        // given

        // when
        String result = underTest.load();

        // then
        assertNull(result);
    }
}
