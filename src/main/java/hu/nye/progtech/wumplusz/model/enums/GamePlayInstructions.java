package hu.nye.progtech.wumplusz.model.enums;

public enum GamePlayInstructions {
    LÉP ("lép"),
    FORDUL_JOBBRA ("jobb"),
    FORDUL_BALRA ("bal"),
    LŐ ("lő"),
    ARANYAT_FELSZED ("felszed"),
    FELAD ("felad"),
    HALASZTÁS ("halaszt");

    private final String label;

    private GamePlayInstructions(String label) {
        this.label = label;
    }

    /**
     * Visszaadja az instrukció címkéjét.
     */
    public String getLabel() {
        return this.label;
    }

    public static GamePlayInstructions valueOfLabel(String label) {
        for (GamePlayInstructions g: values()) {
            if (g.label.equals(label)) {
                return g;
            }
        }
        return null;
    }
}
