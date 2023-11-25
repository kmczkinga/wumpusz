package hu.nye.progtech.wumplusz.service.game;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.HeroDirection;
import hu.nye.progtech.wumplusz.service.throwable.DeathThrowable;
import hu.nye.progtech.wumplusz.service.throwable.VictoryThrowable;

public class GamePlayStepController {

    public MapVO step(MapVO mapVO) throws DeathThrowable, VictoryThrowable {
        Integer heroRow = mapVO.getHero().getHeroRow();
        Integer heroColumn = mapVO.getHero().getHeroColumn();
        if (checkStep(mapVO)) {
            mapVO.deleteEntity(mapVO.getHero().getHeroColumn(), mapVO.getHero().getHeroRow());
            switch (mapVO.getHero().getHeroDirection()) {
                case N:
                    heroRow--;
                    break;
                case E:
                    heroColumn++;
                    break;
                case S:
                    heroRow++;
                    break;
                case W:
                    heroColumn--;
                    break;
            }
            mapVO.placeHero(heroColumn, heroRow);
            if (mapVO.getHero().isInStart() && mapVO.getHero().getHasGold()) {
                throw new VictoryThrowable();
            }
        }
        return mapVO;
    }

    public void turnRight(MapVO mapVO) {
        HeroDirection heroDirection = mapVO.getHero().getHeroDirection();
        switch (heroDirection) {
            case N:
                mapVO.getHero().setHeroDirection(HeroDirection.E);
                break;
            case E:
                mapVO.getHero().setHeroDirection(HeroDirection.S);
                break;
            case S:
                mapVO.getHero().setHeroDirection(HeroDirection.W);
                break;
            case W:
                mapVO.getHero().setHeroDirection(HeroDirection.N);
                break;
        }
    }

    public void turnLeft(MapVO mapVO) {
        HeroDirection heroDirection = mapVO.getHero().getHeroDirection();
        switch (heroDirection) {
            case N:
                mapVO.getHero().setHeroDirection(HeroDirection.W);
                break;
            case E:
                mapVO.getHero().setHeroDirection(HeroDirection.N);
                break;
            case S:
                mapVO.getHero().setHeroDirection(HeroDirection.E);
                break;
            case W:
                mapVO.getHero().setHeroDirection(HeroDirection.S);
                break;
        }
    }

    public void shoot(MapVO mapVO) {
        if (mapVO.getHero().getArrowCount() == 0) {
            System.out.println("Elfogytak a nyilaid!\n");
            return;
        }
        HeroDirection heroDirection = mapVO.getHero().getHeroDirection();
        Integer x = mapVO.getHero().getHeroColumn();
        Integer y = mapVO.getHero().getHeroRow();
        Character ch;
        while (true) {
            ch = mapVO.getEntity(x, y);
            if (ch == 'W') {
                mapVO.getHero().decreaseArrowCount();
                System.out.println("Falat találtál el! Egy nyilad elveszett.\n");
                return;
            } else if (ch == 'U') {
                mapVO.getHero().decreaseArrowCount();
                mapVO.deleteEntity(x,y);
                System.out.println("Eltaláltál egy wumpuszt!\n");
                return;
            }
            switch (heroDirection) {
                case N:
                    y--;
                    break;
                case E:
                    x++;
                    break;
                case S:
                    y++;
                    break;
                case W:
                    x--;
                    break;
            }
        }
    }

    public void pickUp(MapVO mapVO) {
        Integer x = mapVO.getHero().getHeroColumn();
        Integer y = mapVO.getHero().getHeroRow();
        Boolean hasGold = false;
        switch (mapVO.getHero().getHeroDirection()) {
            case N:
                if (mapVO.getEntity(x, --y) == 'G') {
                    hasGold = true;
                }
                break;
            case E:
                if (mapVO.getEntity(++x, y) == 'G') {
                    hasGold = true;
                }
                break;
            case S:
                if (mapVO.getEntity(x, ++y) == 'G') {
                    hasGold = true;
                }
                break;
            case W:
                if (mapVO.getEntity(--x, y) == 'G') {
                    hasGold = true;
                }
                break;
        }
        if (hasGold) {
            mapVO.getHero().setHasGold(true);
            mapVO.deleteEntity(x, y);
            System.out.println("Felvetted az aranyat!");
        } else {
            System.out.println("Nem vettél fel semmit!");
        }
    }

    private Boolean checkStep(MapVO mapVO) throws DeathThrowable {
        Character ch = Character.MIN_VALUE;
        Integer x = mapVO.getHero().getHeroColumn();
        Integer y = mapVO.getHero().getHeroRow();
        switch (mapVO.getHero().getHeroDirection()) {
            case N:
                ch = mapVO.getEntity(x, y - 1);
                break;
            case E:
                ch = mapVO.getEntity(x + 1, y);
                break;
            case S:
                ch = mapVO.getEntity(x, y + 1);
                break;
            case W:
                ch = mapVO.getEntity(x - 1, y );
                break;
        }
        switch (ch) {
            case 'W':
                System.out.println("Itt fal van!\n");
                return false;
            case 'P':
                System.out.println("Verembe léptél! Elvesztettél egy nyilat!\n");
                mapVO.getHero().decreaseArrowCount();
                return false;
            case 'U' :
                System.out.print("Wumpusszal találkoztál! ");
                throw new DeathThrowable();
            case 'G':
                System.out.println("Arany van előtted!\n");
                return false;
            default:
                return true;
        }
    }
}
