package hu.nye.progtech.wumplusz.repository;

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
    public String load();
}
