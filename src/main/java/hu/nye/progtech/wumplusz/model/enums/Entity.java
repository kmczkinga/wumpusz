package hu.nye.progtech.wumplusz.model.enums;

/**
 * Entitás típus.
 */
public enum Entity {
    FAL('W'),
    VEREM('P'),
    WUMPUSZ('U'),
    ARANY('G'),
    HOS('H');

    private final Character label;

    private Entity(Character label) {
        this.label = label;
    }

    /**
     * Visszaadja az entitás címkéjét.
     */
    public Character getLabel() {
        return this.label;
    }

    /**
     * Megkeresi az adott típust a hozzá tartozó label alapján.
     */
    public static Entity valueOfLabel(Character label) {
        for (Entity e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
