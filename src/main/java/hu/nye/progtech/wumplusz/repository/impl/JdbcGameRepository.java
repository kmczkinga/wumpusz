package hu.nye.progtech.wumplusz.repository.impl;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.repository.GameRepository;

/**
 * Komponens, amely betölti, elmenti a játékot az adatbázisba.
 */
public class JdbcGameRepository implements GameRepository {

    /**
     * Elmenti az adott játékot.
     */
    @Override
    public void save() {

    }

    /**
     * Betölti az adott játékot.
     */
    @Override
    public MapVO load() {
        return null;
    }
}
