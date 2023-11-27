package hu.nye.progtech.wumplusz.model;

/**
 * VO, amely a user adatait tárolja.
 * Ezek a neve, nyeréseinek száma.
 */
public class UserData {

    private String username;

    private Integer wins;

    public UserData(String username, Integer wins) {
        this.username = username;
        this.wins = wins;
    }

    /**
     * Visszaadja a nevét.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Visszaadja a nyeréseinek számát.
     */
    public Integer getWins() {
        return wins;
    }

    /**
     * Beállítja a nyeréseinek számát.
     */
    public void setWins(Integer wins) {
        this.wins = wins;
    }
}
