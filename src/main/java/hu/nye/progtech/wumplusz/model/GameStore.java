package hu.nye.progtech.wumplusz.model;

import java.util.List;

import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.enums.Entity;

/**
 * Komponens, ami a játékkal kapcsolatos információkat tárolja.
 */

public class GameStore {


    private UserData userData;

    private MapVO mapVO;

    private List<Entity> availableEntities;


    /**
     * Visszaadja a MapVO-t.
     */
    public MapVO getMapVO() {
        return mapVO;
    }


    /**
     * Beállítja a MapVO-t.
     */
    public void setMapVO(MapVO mapVO) {
        this.mapVO = mapVO;
    }

    /**
     * Beállítja az elérhető entitásokat.
     */
    public void setAvailableEntities(List<Entity> entities) {
        this.availableEntities = entities;
    }

    /**
     * Visszaadja az elérhető entitásokat.
     */
    public List<Entity> getAvailableEntities() {
        return this.availableEntities;
    }

    /**
     * Kitörli az adott nevű entitást az elérhető entitások listájából.
     * Ezt az elérhető entitások végigloopolásával teszi, majd kitörli a megtalált entitást.
     * Csak akkor törli ki, ha megtalálta.
     */
    public void removeEntity(String entityName) {
        Entity removableEntity = null;
        for (Entity entity : availableEntities) {
            if (entity.name().equals(entityName)) {
                removableEntity = entity;
            }
        }
        if (removableEntity != null) {
            availableEntities.remove(removableEntity);
        }
    }

    /**
     * Visszaadja a userdata-t.
     */

    public UserData getUserData() {
        return userData;
    }

    /**
     * Beállítja a userdata-t.
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * Stringként visszaadja ezt az objektumot.
     */
    @Override
    public String toString() {
        return "GameStore{" +
                "userName='" + userData.toString() + '\'' +
                ", mapVO=" + mapVO +
                '}';
    }
}
