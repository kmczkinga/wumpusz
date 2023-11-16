package hu.nye.progtech.wumplusz.repository;

import hu.nye.progtech.wumplusz.model.MapVO;

/**
 * Interfész, amely a játék állást menti, visszatölti.
 */
public interface GameRepository {

    /**
     * Elmenti az adott játékot.
     */
    public void save();

    /**
     * Betölti az adott játékot.
     */
    public MapVO load();
}
