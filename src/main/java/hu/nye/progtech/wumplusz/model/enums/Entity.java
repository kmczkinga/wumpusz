package hu.nye.progtech.wumplusz.model.enums;

/**
 * Entitás típus.
 */
public enum Entity {
    FAL('F'),
    VEREM('V'),
    WUMPUSZ('W'),
    ARANY('A'),
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
}
