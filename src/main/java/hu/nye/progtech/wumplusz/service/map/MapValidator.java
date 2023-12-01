package hu.nye.progtech.wumplusz.service.map;

import hu.nye.progtech.wumplusz.model.MapVO;

/**
 * Komponens, amely a pálya helyességét ellenőrzi.
 */
public class MapValidator {

    /**
     * Megnézi, hogy a pálya helyes-e.
     */
    public static Boolean isValid(MapVO mapVO) {
        return isSizeValid(mapVO) && isEntityCountValid(mapVO);
    }

     /**
     * A pálya méretét ellenőrzi le.
     * 6 és 20 között.
     */
     private static Boolean isSizeValid(MapVO mapVO) {
        return mapVO.getSize() >= 6 && mapVO.getSize() <= 20;
    }

    /**
     * Megnézi, hogy az
     * - arany szám = 1,
     * - a hős szám = 1,
     * - wumpusz szám megegyezik-e a pálya méretével.
     */
    private static Boolean isEntityCountValid(MapVO mapVO) {
        Integer wumpusCount = 0;
        Integer goldCount = 0;
        Integer heroCount = 0;
        for (int i = 0; i < mapVO.getSize(); i++) {
            for (int j = 0; j < mapVO.getSize(); j++) {
                if (mapVO.getEntity(i, j).equals('U')) {
                    wumpusCount++;
                } else if (mapVO.getEntity(i, j).equals('G')) {
                    goldCount++;
                } else if (mapVO.getEntity(i, j).equals('H')) {
                    heroCount++;
                }
            }
        }
        Boolean correctWumpusCount = ((mapVO.getSize() >= 6 && mapVO.getSize() <= 8 && wumpusCount == 1)
        || (mapVO.getSize() >= 9 && mapVO.getSize() <= 14 && wumpusCount == 2)
        || (mapVO.getSize() >= 15 && mapVO.getSize() <= 20 && wumpusCount == 3));
        return correctWumpusCount && goldCount == 1 && heroCount == 1;
    }
}
