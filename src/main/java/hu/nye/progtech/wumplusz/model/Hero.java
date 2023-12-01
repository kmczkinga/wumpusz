package hu.nye.progtech.wumplusz.model;

import hu.nye.progtech.wumplusz.model.enums.HeroDirection;

/**
 * VO, amely a hőst jelképezi.
 * Tárolja annak irányát, nyilak számát,
 * lépéseinek számát, jelenlegi és kezdő pozícióját.
 */
public class Hero {

    private HeroDirection heroDirection;

    private Integer arrowCount;

    private Integer stepCount;

    private Integer heroRow;

    private Integer heroColumn;

    private Integer startingColumn;

    private Integer startingRow;

    private Boolean hasGold;

    public Hero(HeroDirection heroDirection, Integer heroRow, Integer heroColumn, Boolean hasGold) {
        this.heroDirection = heroDirection;
        this.heroRow = heroRow;
        this.heroColumn = heroColumn;
        this.startingColumn = heroColumn;
        this.startingRow = heroRow;
        this.hasGold = hasGold;
    }

    /**
     * Visszaadja a lépések számát.
     */
    public Integer getStepCount() {
        return stepCount;
    }

    /**
     * Beállítja a lépések számát.
     */
    public void setStepCount(Integer stepCount) {
        this.stepCount = stepCount;
    }

    /**
     * Egyel megnöveli a nyilak számát.
     */
    public void increaseStepCount() {
        this.stepCount++;
    }

    /**
     * Visszaadja a hős irányát.
     */
    public HeroDirection getHeroDirection() {
        return heroDirection;
    }

    /**
     * Beállítja a hős irányát.
     */
    public void setHeroDirection(HeroDirection heroDirection) {
        this.heroDirection = heroDirection;
    }

    /**
     * Visszaadja a nyilak számát.
     */
    public Integer getArrowCount() {
        return arrowCount;
    }

    /**
     * Beállítja a nyilak számát.
     */
    public void setArrowCount(Integer arrowCount) {
        this.arrowCount = arrowCount;
    }

    /**
     * Csökkenti a nyilak számát.
     */
    public void decreaseArrowCount() {
        this.arrowCount--;
    }

    /**
     * Visszaadja a hős jelenlegi oszlopát.
     */
    public Integer getHeroColumn() {
        return heroColumn;
    }

    /**
     * Beállítja a hős jelenlegi oszlopát.
     */
    public void setHeroColumn(Integer heroColumn) {
        this.heroColumn = heroColumn;
    }

    /**
     * Visszaadja a hős jelenlegi sorát.
     */
    public Integer getHeroRow() {
        return heroRow;
    }

    /**
     * Beállítja a hős jelenlegi sorát.
     */
    public void setHeroRow(Integer heroRow) {
        this.heroRow = heroRow;
    }

    /**
     * Visszaadja, hogy birtokában van-e az arany.
     */
    public Boolean getHasGold() {
        return hasGold;
    }

    /**
     * Beállítja, hogy nála van az arany, vagy sem.
     */
    public void setHasGold(Boolean hasGold) {
        this.hasGold = hasGold;
    }

    /**
     * Megnézi, hogy a hős a kezdő pozícióban van-e.
     */
    public Boolean isInStart() {
        return (startingColumn == heroColumn) && (startingRow == heroRow);
    }

    /**
     * Visszaadja a hős kezdő oszlopát.
     */
    public Integer getStartingColumn() {
        return startingColumn;
    }

    /**
     * Visszaadja a hős kezdő sorát.
     */
    public Integer getStartingRow() {
        return startingRow;
    }

    /**
     * Beállítja a hős kezdő oszlopát.
     */
    public void setStartingColumn(Integer startingColumn) {
        this.startingColumn = startingColumn;
    }

    /**
     * Beállítja a hős kezdő sorát.
     */
    public void setStartingRow(Integer startingRow) {
        this.startingRow = startingRow;
    }
}
