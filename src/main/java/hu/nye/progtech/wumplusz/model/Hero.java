package hu.nye.progtech.wumplusz.model;

import hu.nye.progtech.wumplusz.model.enums.HeroDirection;

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

    public Integer getStepCount() {
        return stepCount;
    }

    public void setStepCount(Integer stepCount) {
        this.stepCount = stepCount;
    }

    public void increaseStepCount() {
        this.stepCount++;
    }

    public HeroDirection getHeroDirection() {
        return heroDirection;
    }

    public void setHeroDirection(HeroDirection heroDirection) {
        this.heroDirection = heroDirection;
    }

    public Integer getArrowCount() {
        return arrowCount;
    }

    public void setArrowCount(Integer arrowCount) {
        this.arrowCount = arrowCount;
    }


    public void decreaseArrowCount() {
        this.arrowCount--;
    }

    public Integer getHeroColumn() {
        return heroColumn;
    }

    public void setHeroColumn(Integer heroColumn) {
        this.heroColumn = heroColumn;
    }

    public Integer getHeroRow() {
        return heroRow;
    }

    public void setHeroRow(Integer heroRow) {
        this.heroRow = heroRow;
    }

    public Boolean getHasGold() {
        return hasGold;
    }

    public void setHasGold(Boolean hasGold) {
        this.hasGold = hasGold;
    }

    public Boolean isInStart() {
        return (startingColumn == heroColumn) && (startingRow == heroRow);
    }

    public Integer getStartingColumn() {
        return startingColumn;
    }

    public Integer getStartingRow() {
        return startingRow;
    }
}
