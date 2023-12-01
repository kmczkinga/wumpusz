package hu.nye.progtech.wumplusz.service.game;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.HeroDirection;
import hu.nye.progtech.wumplusz.service.throwable.DeathThrowable;
import hu.nye.progtech.wumplusz.service.throwable.VictoryThrowable;

/**
 * Komponens, amely elvégzi a játékbeli lépéseket.
 */
public class GamePlayStepController {

    /**
     * Egy játékos lépést végez el.
     * Bekéri a hős sorát, oszlopát.
     * Ha elvégezhető a lépés, akkor kitörli az adott entitást az adott helyről.
     * Majd a hős irányának megfelelően arrébbteszi a hőst.
     * Ha a lépésben a kezdő pozícióra tér vissza, dob egy VictoryThrowablet.
     * Ha a lépésben meghal, dob egy DeathThrowable-t.
     * Ha sikeres a lépés, visszaadja a módosított MapVO-t.
     */
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
                default:
                    break;
            }
            mapVO.placeHero(heroColumn, heroRow);
            if (mapVO.getHero().isInStart() && mapVO.getHero().getHasGold()) {
                throw new VictoryThrowable();
            }
        }
        return mapVO;
    }

    /**
     * Jobbra forgarja a hőst a megfelelő irányoktól jobbra lévő irányba.
     */
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
            default:
                break;
        }
    }

    /**
     * Balra fordítja a hőst az adott irány balra megfelelő irányba.
     */
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
            default:
                break;
        }
    }

    /**
     * Elvégzi a lövést.
     * Ellenőrzi, hogy van-e nyila a hősnek.
     * Majd ciklikusan arrébbteszi a nyilat
     * (nem teszi le, csak a koordinátái változnak).
     * Ha épp falon áll a nyíl, elvesz egy nyilat.
     * Ha wumpuszon áll a nyíl, megöli azt.
     * Ha semmin nem áll, arrébbteszi a következő helyre.
     */
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
                mapVO.deleteEntity(x, y);
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
                default:
                    break;
            }
        }
    }

    /**
     * Elvégzi az arany felvevést.
     * Ezt a hős megfelelő irányától
     * 1 koordinátával arrébb ellenőrzi.
     * Ha talált aranyat, felveszi azt,
     * ha nem, akkor nem.
     * Ezeket ki is írja a usernek.
     */
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
            default:
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

    /**
     * Egy lépést ellenőriz le, hogy elvégezhető-e.
     * A különböző irányokat figyelembe véve.
     * Ha falat, vermet, wumpuszt, aranyat talál, jelzi.
     * Falnál semmi nem történik, veremben csökkenti a nyilak számát,
     * wumpusznál dob egy DeathThrowablet,
     * aranynál csak jelzi azt.
     */
    public Boolean checkStep(MapVO mapVO) throws DeathThrowable {
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
                ch = mapVO.getEntity(x - 1, y);
                break;
            default:
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
            case 'U':
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
