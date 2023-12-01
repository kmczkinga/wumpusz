package hu.nye.progtech.wumplusz.service.map;

import static hu.nye.progtech.wumplusz.model.enums.Entity.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.Hero;
import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.Entity;
import hu.nye.progtech.wumplusz.model.enums.HeroDirection;
import hu.nye.progtech.wumplusz.service.input.InputReader;
import hu.nye.progtech.wumplusz.service.input.UserInteractionHandler;
import hu.nye.progtech.wumplusz.service.throwable.ExitChoiceThrowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MapEditorTest {

    private static final Integer SMALL_MAP_SIZE = 7;

    private static final Integer MEDIUM_MAP_SIZE = 12;

    private static final Integer BIG_MAP_SIZE = 15;



    private static final List<Entity> SMALL_MAP_ENTITIES1 = List.of(Entity.ARANY, Entity.HOS, FAL, Entity.VEREM);

    private static final List<Entity> SMALL_MAP_ENTITIES2 = List.of(Entity.HOS, FAL, Entity.VEREM);

    private static final List<Entity> SMALL_MAP_ENTITIES3 = List.of(FAL, Entity.VEREM);

    private static final List<Entity> SMALL_MAP_ENTITIES = List.of(Entity.WUMPUSZ,Entity.ARANY, Entity.HOS, FAL, Entity.VEREM);

    private static final List<Entity> MEDIUM_MAP_ENTITIES = List.of(Entity.WUMPUSZ, Entity.WUMPUSZ, Entity.ARANY, Entity.HOS, FAL,
            Entity.VEREM);

    private static final List<Entity> BIG_MAP_ENTITIES = List.of(Entity.WUMPUSZ, Entity.WUMPUSZ, Entity.WUMPUSZ, Entity.ARANY, Entity.HOS
            , FAL,
            Entity.VEREM);

    private static final String MAP_OUTPUT = "  0 1 2 3 4 5 6 7 \n" +
            "0 F F F F F F F F \n" +
            "1 F U           F \n" +
            "2 F             F \n" +
            "3 F             F \n" +
            "4 F             F \n" +
            "5 F             F \n" +
            "6 F             F \n" +
            "7 F F F F F F F F \n" +
            "\n" +
            "Nem tehető ide ez az entitás, próbáld máshová!\n" +
            "  0 1 2 3 4 5 6 7 \n" +
            "0 F F F F F F F F \n" +
            "1 F U           F \n" +
            "2 F             F \n" +
            "3 F             F \n" +
            "4 F             F \n" +
            "5 F             F \n" +
            "6 F             F \n" +
            "7 F F F F F F F F \n" +
            "\n" +
            "  0 1 2 3 4 5 6 7 \n" +
            "0 F F F F F F F F \n" +
            "1 F U           F \n" +
            "2 F             F \n" +
            "3 F     P       F \n" +
            "4 F             F \n" +
            "5 F             F \n" +
            "6 F             F \n" +
            "7 F F F F F F F F \n" +
            "\n" +
            "  0 1 2 3 4 5 6 7 \n" +
            "0 F F F F F F F F \n" +
            "1 F U           F \n" +
            "2 F             F \n" +
            "3 F     P       F \n" +
            "4 F       H     F \n" +
            "5 F             F \n" +
            "6 F             F \n" +
            "7 F F F F F F F F \n" +
            "\n" +
            "  0 1 2 3 4 5 6 7 \n" +
            "0 F F F F F F F F \n" +
            "1 F U           F \n" +
            "2 F             F \n" +
            "3 F     P       F \n" +
            "4 F       H     F \n" +
            "5 F         W   F \n" +
            "6 F             F \n" +
            "7 F F F F F F F F \n\n";

    private static final String EMPTY_MAP_OUTPUT = "A pálya méretét 6-20-ig add meg!\n" +
            "6-20-ig add meg!\n" +
            "  0 1 2 3 4 5 6 7 8 9 \n" +
            "0 F F F F F F F F F F \n" +
            "1 F                 F \n" +
            "2 F                 F \n" +
            "3 F                 F \n" +
            "4 F                 F \n" +
            "5 F                 F \n" +
            "6 F                 F \n" +
            "7 F                 F \n" +
            "8 F                 F \n" +
            "9 F F F F F F F F F F \n\n";

    private MapEditor underTest;

    private InputReader inputReader;

    private GameStore gameStore;

    private UserInteractionHandler interactionHandler;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @BeforeEach
    public void setUp() {
        inputReader = Mockito.mock(InputReader.class);
        gameStore = Mockito.mock(GameStore.class);
        interactionHandler = Mockito.mock(UserInteractionHandler.class);
        underTest = new MapEditor(inputReader, gameStore, interactionHandler);
    }

    @Test
    public void testEditShouldPlaceDownAnEntity() throws ExitChoiceThrowable {
        // given, when
        System.setOut(new PrintStream(outputStreamCaptor));
        MapVO mapVO = new MapVO(8);
        Hero hero = Mockito.mock(Hero.class);
        mapVO.setHero(hero);
        given(gameStore.getMapVO()).willReturn(mapVO).willReturn(mapVO);
        given(gameStore.getAvailableEntities())
                .willReturn(SMALL_MAP_ENTITIES)
                .willReturn(SMALL_MAP_ENTITIES1)
                .willReturn(SMALL_MAP_ENTITIES2)
                .willReturn(SMALL_MAP_ENTITIES3)
                .willReturn(SMALL_MAP_ENTITIES3)
                .willReturn(SMALL_MAP_ENTITIES3)
                .willReturn(SMALL_MAP_ENTITIES3);
        when(interactionHandler.getChosenEntity(SMALL_MAP_ENTITIES)).thenReturn(WUMPUSZ);
        when(interactionHandler.getChosenEntity(SMALL_MAP_ENTITIES1)).thenReturn(ARANY);
        when(interactionHandler.getChosenEntity(SMALL_MAP_ENTITIES2)).thenReturn(VEREM);
        when(interactionHandler.getChosenEntity(SMALL_MAP_ENTITIES3)).thenReturn(HOS).thenReturn(FAL).thenThrow(ExitChoiceThrowable.class);
        given(interactionHandler.getCoordinate(mapVO, "X"))
                .willReturn(1)
                .willReturn(1)
                .willReturn(3)
                .willReturn(4)
                .willReturn(5);
        given(interactionHandler.getCoordinate(mapVO, "Y"))
                .willReturn(1)
                .willReturn(1)
                .willReturn(3)
                .willReturn(4)
                .willReturn(5);
        given(interactionHandler.getHeroDirection()).willReturn(HeroDirection.N);
        underTest.edit();
        // then
        assertEquals(MAP_OUTPUT, outputStreamCaptor.toString());
    }

    @Test
    public void testHandleTableSizeShouldReturnTableSize() {
        // given
        System.setOut(new PrintStream(outputStreamCaptor));
        given(inputReader.readInteger()).willReturn(5).willReturn(10);
        // when
        when(gameStore.getMapVO()).thenReturn(new MapVO(10));
        underTest.handleTableSize();
        // then
        assertEquals(EMPTY_MAP_OUTPUT, outputStreamCaptor.toString());
    }


    @Test
    public void testCreateAvailableEntityListShouldCreateSmallList() {
        // given

        // when
        List<Entity> result = underTest.createAvailableEntityList(SMALL_MAP_SIZE);
        // then
        assertEquals(result, SMALL_MAP_ENTITIES);
    }

    @Test
    public void testCreateAvailableEntityListShouldCreateMediumList() {
        // given

        // when
        List<Entity> result = underTest.createAvailableEntityList(MEDIUM_MAP_SIZE);
        // then
        assertEquals(result, MEDIUM_MAP_ENTITIES);
    }

    @Test
    public void testCreateAvailableEntityListShouldCreateBigList() {
        // given

        // when
        List<Entity> result = underTest.createAvailableEntityList(20);
        // then
        assertEquals(result, BIG_MAP_ENTITIES);
    }

    @Test
    public void testCreateAvailableEntityListhouldReturnMiddleSizedEntityList() {
        // given

        // when
        List<Entity> result = underTest.createAvailableEntityList(10);
        // then
        assertEquals(result, MEDIUM_MAP_ENTITIES);
    }
}
