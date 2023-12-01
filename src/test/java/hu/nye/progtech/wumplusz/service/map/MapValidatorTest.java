package hu.nye.progtech.wumplusz.service.map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hu.nye.progtech.wumplusz.model.MapVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapValidatorTest {

    private static final Character[][] SMALL_MAP = new Character[][]{
            {'F','F','F','F','F','F','F','F'},
            {'F',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ','G',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ','U',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ','F'},
            {'F','F','F','F','F','F','F','F'}
    };

    private static final Character[][] TOO_SMALL_MAP = new Character[][]{
            {'F','F','F','F','F','F','F'},
            {'F',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ','G',' ','F'},
            {'F',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ','U','F'},
            {'F',' ',' ',' ',' ',' ','F'},
            {'F','F','F','F','F','F','F'}
    };

    private static final Character[][] INVALID_MEDIUM_MAP = new Character[][]{
            {'F','F','F','F','F','F','F','F','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ','G',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ','U',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F','F','F','F','F','F','F','F','F'}
    };

    private static final Character[][] MEDIUM_MAP = new Character[][]{
            {'F','F','F','F','F','F','F','F','F','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ','G',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ','U',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ','U',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F','F','F','F','F','F','F','F','F','F'}
    };

    private static final Character[][] BIG_MAP = new Character[][]{
            {'F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ','G',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','U',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ','U',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','U',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F'}
    };

    private static final Character[][] TOO_BIG_MAP = new Character[][]{
            {'F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ','G',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','U',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ','U',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','U',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F'},
            {'F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F'}
    };

    private static MapValidator underTest;

    @BeforeEach
    public void setUp() {
        this.underTest = new MapValidator();
    }

    @Test
    public void testIsInvalidShouldReturnTrue() {
        // given
        MapVO mapVO = new MapVO(8, "B", 2, "N", SMALL_MAP);
        // when
        Boolean result = underTest.isValid(mapVO);
        // then
        assertTrue(result);
    }

    @Test
    public void testIsInvalidShouldWithMedMapReturnTrue() {
        // given
        MapVO mapVO = new MapVO(10, "B", 2, "N", MEDIUM_MAP);
        // when
        Boolean result = underTest.isValid(mapVO);
        // then
        assertTrue(result);
    }

    @Test
    public void testIsInvalidShouldWithBigReturnTrue() {
        // given
        MapVO mapVO = new MapVO(20, "B", 2, "N", BIG_MAP);
        // when
        Boolean result = underTest.isValid(mapVO);
        // then
        assertTrue(result);
    }

    @Test
    public void testIsInvalidWithSize9ShouldReturnFalse() {
        // given
        MapVO mapVO = new MapVO(9, "B", 2, "N", INVALID_MEDIUM_MAP);
        // when
        Boolean result = underTest.isValid(mapVO);
        // then
        assertFalse(result);
    }

    @Test
    public void testIsInvalidWithSize21ShouldReturnFalse() {
        // given
        MapVO mapVO = new MapVO(21, "B", 2, "N", TOO_BIG_MAP);
        // when
        Boolean result = underTest.isValid(mapVO);
        // then
        assertFalse(result);
    }

    @Test
    public void testIsInvalidWithSize5ShouldReturnFalse() {
        // given
        MapVO mapVO = new MapVO(5, "B", 2, "N", TOO_SMALL_MAP);
        // when
        Boolean result = underTest.isValid(mapVO);
        // then
        assertFalse(result);
    }
}
