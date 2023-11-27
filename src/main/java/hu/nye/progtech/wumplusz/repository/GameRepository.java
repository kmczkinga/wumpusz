package hu.nye.progtech.wumplusz.repository;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.service.throwable.NoNameThrowable;

/**
 * Interfész, amely a játék állást menti, visszatölti.
 */
public interface GameRepository<T> {

    /**
     * Elmenti az adott játékot.
     */
    void save();

    /**
     * Betölti az adott játékot.
     */
    T load(String username)  throws NoNameThrowable;
}
