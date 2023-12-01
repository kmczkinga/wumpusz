package hu.nye.progtech.wumplusz.service.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.HeroDirection;
import hu.nye.progtech.wumplusz.service.throwable.DeathThrowable;
import hu.nye.progtech.wumplusz.service.throwable.VictoryThrowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GamePlayStepControllerTest {

    private static final Character[][] MAP_NORTH = new Character[][]{
            {'W','W','W', 'W', 'W'},
            {'W','_','H', '_', 'W'},
            {'W','_','_', '_', 'W'},
            {'W','_','_', '_', 'W'},
            {'W','W','W', 'W', 'W'}
    };

    private static final Character[][] MAP_WEST = new Character[][]{
            {'W','W','W', 'W', 'W'},
            {'W','_','_', '_', 'W'},
            {'W','H','_', '_', 'W'},
            {'W','_','_', '_', 'W'},
            {'W','W','W', 'W', 'W'}
    };

    private static final Character[][] MAP_SOUTH = new Character[][]{
            {'W','W','W', 'W', 'W'},
            {'W','_','_', '_', 'W'},
            {'W','_','_', '_', 'W'},
            {'W','_','H', '_', 'W'},
            {'W','W','W', 'W', 'W'}
    };

    private static final Character[][] MAP_EAST = new Character[][]{
            {'W','W','W', 'W', 'W'},
            {'W','_','_', '_', 'W'},
            {'W','_','_', 'H', 'W'},
            {'W','_','_', '_', 'W'},
            {'W','W','W', 'W', 'W'}
    };
    private static final String NO_ARROW = "Elfogytak a nyilaid!\n" +
            "\n";
    private static final String WALL_SHOOT = "Falat találtál el! Egy nyilad elveszett.\n" +
            "\n";

    private static final String PICKED_UP_GOLD = "Felvetted az aranyat!\n";

    private static final String NO_PICKED_UP_GOLD = "Nem vettél fel semmit!\n";

    private static final String WUMPUSZ_SHOOT = "Eltaláltál egy wumpuszt!\n" +
            "\n";


    private GamePlayStepController underTest;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        this.underTest = new GamePlayStepController();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testStepWithNShouldPlayAStep() throws DeathThrowable, VictoryThrowable {
        // given
        MapVO mapVO = createMapVO("N");

        // when
        MapVO result = underTest.step(mapVO);

        // then
        assertTrue(isMapEquals(result.getMap(), MAP_NORTH));
    }

    @Test
    public void testStepWithEShouldPlayAStep() throws DeathThrowable, VictoryThrowable {
        // given
        MapVO mapVO = createMapVO("E");

        // when
        MapVO result = underTest.step(mapVO);

        // then
        assertTrue(isMapEquals(result.getMap(), MAP_EAST));
    }

    @Test
    public void testStepWithSShouldPlayAStep() throws DeathThrowable, VictoryThrowable {
        // given
        MapVO mapVO = createMapVO("S");

        // when
        MapVO result = underTest.step(mapVO);

        // then
        assertTrue(isMapEquals(result.getMap(), MAP_SOUTH));
    }

    @Test
    public void testStepWithWShouldPlayAStep() throws DeathThrowable, VictoryThrowable {
        // given
        MapVO mapVO = createMapVO("W");

        // when
        MapVO result = underTest.step(mapVO);

        // then
        assertTrue(isMapEquals(result.getMap(), MAP_WEST));
    }

    @Test
    public void testStepNotPlaceable() throws DeathThrowable, VictoryThrowable {
        // given
        MapVO mapVO = new MapVO(5, "B", 1, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setStepCount(10);
        mapVO.setStartingArrowCount();
        System.out.println(mapVO);

        // when
        MapVO result = underTest.step(mapVO);

        // then
        assertTrue(isMapEquals(result.getMap(), mapVO.getMap()));
    }

    @Test
    public void testStepWithWShouldVictory() throws DeathThrowable, VictoryThrowable {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setStepCount(10);
        mapVO.getHero().setStartingRow(1);
        mapVO.getHero().setStartingColumn(2);
        mapVO.getHero().setHasGold(true);
        mapVO.setStartingArrowCount();

        // when
        VictoryThrowable result = assertThrows(VictoryThrowable.class, () -> underTest.step(mapVO), "");

        // then
        assertNotNull(result);
    }

    @Test
    public void testCheckStepWithP() throws DeathThrowable, VictoryThrowable {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','P', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setArrowCount(1);
        // when
        Boolean result = underTest.checkStep(mapVO);

        // then
        assertFalse(result);
        assertEquals(mapVO.getHero().getArrowCount(), 0);
    }

    @Test
    public void testCheckStepWithU() throws DeathThrowable, VictoryThrowable {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','U', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        DeathThrowable result = assertThrows(DeathThrowable.class, () -> underTest.checkStep(mapVO), "");

        // then
        assertNotNull(result);
    }

    @Test
    public void testCheckStepWithG() throws DeathThrowable {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','G', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        Boolean result = underTest.checkStep(mapVO);

        // then
        assertFalse(result);
    }

    @Test
    public void checkTurnRightShouldTurnRightN() {
        // given
        MapVO mapVO = createMapVO("N");

        // when
        underTest.turnRight(mapVO);

        // then
        assertEquals(mapVO.getHero().getHeroDirection(), HeroDirection.E);
    }

    @Test
    public void checkTurnRightShouldTurnRightE() {
        // given
        MapVO mapVO = createMapVO("E");

        // when
        underTest.turnRight(mapVO);

        // then
        assertEquals(mapVO.getHero().getHeroDirection(), HeroDirection.S);
    }

    @Test
    public void checkTurnRightShouldTurnRightS() {
        // given
        MapVO mapVO = createMapVO("S");

        // when
        underTest.turnRight(mapVO);

        // then
        assertEquals(mapVO.getHero().getHeroDirection(), HeroDirection.W);
    }

    @Test
    public void checkTurnRightShouldTurnRightW() {
        // given
        MapVO mapVO = createMapVO("W");

        // when
        underTest.turnRight(mapVO);

        // then
        assertEquals(mapVO.getHero().getHeroDirection(), HeroDirection.N);
    }

    @Test
    public void checkTurnRightShouldTurnLeftN() {
        // given
        MapVO mapVO = createMapVO("N");

        // when
        underTest.turnLeft(mapVO);

        // then
        assertEquals(mapVO.getHero().getHeroDirection(), HeroDirection.W);
    }

    @Test
    public void checkTurnRightShouldTurnLeftE() {
        // given
        MapVO mapVO = createMapVO("E");

        // when
        underTest.turnLeft(mapVO);

        // then
        assertEquals(mapVO.getHero().getHeroDirection(), HeroDirection.N);
    }

    @Test
    public void checkTurnRightShouldTurnLeftS() {
        // given
        MapVO mapVO = createMapVO("S");

        // when
        underTest.turnLeft(mapVO);

        // then
        assertEquals(mapVO.getHero().getHeroDirection(), HeroDirection.E);
    }

    @Test
    public void checkTurnRightShouldTurnLeftW() {
        // given
        MapVO mapVO = createMapVO("W");

        // when
        underTest.turnLeft(mapVO);

        // then
        assertEquals(mapVO.getHero().getHeroDirection(), HeroDirection.S);
    }

    @Test
    public void testShootWithNoArrow() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','G', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setArrowCount(0);
        // when
        underTest.shoot(mapVO);

        // then
        assertEquals(NO_ARROW, outputStreamCaptor.toString());
    }

    @Test
    public void testShootWithEast() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "E", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setArrowCount(1);
        // when
        underTest.shoot(mapVO);

        // then
        assertEquals(WALL_SHOOT, outputStreamCaptor.toString());
    }

    @Test
    public void testShootWithWest() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "W", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setArrowCount(1);
        // when
        underTest.shoot(mapVO);

        // then
        assertEquals(WALL_SHOOT, outputStreamCaptor.toString());
    }

    @Test
    public void testShootWithNorth() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setArrowCount(1);
        // when
        underTest.shoot(mapVO);

        // then
        assertEquals(WALL_SHOOT, outputStreamCaptor.toString());
    }

    @Test
    public void testShootWithSouth() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "S", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setArrowCount(1);
        // when
        underTest.shoot(mapVO);

        // then
        assertEquals(WALL_SHOOT, outputStreamCaptor.toString());
    }

    @Test
    public void testShootWithU() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','U', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setArrowCount(1);
        // when
        underTest.shoot(mapVO);

        // then
        assertEquals(WUMPUSZ_SHOOT, outputStreamCaptor.toString());
    }

    @Test
    public void testPickUpWithNorth() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','G', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        underTest.pickUp(mapVO);

        // then
        assertEquals(PICKED_UP_GOLD, outputStreamCaptor.toString());
    }

    @Test
    public void testPickUpWithWest() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "W", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','G','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        underTest.pickUp(mapVO);

        // then
        assertEquals(PICKED_UP_GOLD, outputStreamCaptor.toString());
    }

    @Test
    public void testPickUpWithSouth() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "S", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','G', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        underTest.pickUp(mapVO);

        // then
        assertEquals(PICKED_UP_GOLD, outputStreamCaptor.toString());
    }

    @Test
    public void testPickUpWithEast() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "E", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', 'G', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        underTest.pickUp(mapVO);

        // then
        assertEquals(PICKED_UP_GOLD, outputStreamCaptor.toString());
    }

    @Test
    public void testPickUpWithNorthNoGold() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "N", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        underTest.pickUp(mapVO);

        // then
        assertEquals(NO_PICKED_UP_GOLD, outputStreamCaptor.toString());
    }

    @Test
    public void testPickUpWithWestNoGold() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "W", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        underTest.pickUp(mapVO);

        // then
        assertEquals(NO_PICKED_UP_GOLD, outputStreamCaptor.toString());
    }

    @Test
    public void testPickUpWithSouthNoGold() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "S", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        underTest.pickUp(mapVO);

        // then
        assertEquals(NO_PICKED_UP_GOLD, outputStreamCaptor.toString());
    }

    @Test
    public void testPickUpWithEastNoGold() {
        // given
        MapVO mapVO = new MapVO(5, "C", 2, "E", new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        // when
        underTest.pickUp(mapVO);

        // then
        assertEquals(NO_PICKED_UP_GOLD, outputStreamCaptor.toString());
    }


    private MapVO createMapVO(String heroDirection) {
        MapVO mapVO = new MapVO(5, "C", 2, heroDirection, new Character[][]{
                {'W','W','W', 'W', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','_','_', '_', 'W'},
                {'W','W','W', 'W', 'W'}
        });
        mapVO.getHero().setStepCount(10);
        mapVO.setStartingArrowCount();
        return mapVO;
    }

    private boolean isMapEquals(Character[][] map1, Character[][] map2) {
        for (int i = 0; i < map1.length; i++) {
            for (int j = 0; j < map1.length; j++) {
                if (map1[i][j] != map2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
