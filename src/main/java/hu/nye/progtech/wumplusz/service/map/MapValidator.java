package hu.nye.progtech.wumplusz.service.map;

import hu.nye.progtech.wumplusz.model.MapVO;

public class MapValidator {

    public static Boolean isValid(MapVO mapVO) {
        return isSizeValid(mapVO) && isEntityCountValid(mapVO);
    }

     private static Boolean isSizeValid(MapVO mapVO) {
        return mapVO.getSize() >= 6 && mapVO.getSize() <= 20;
    }

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
                }else if (mapVO.getEntity(i, j).equals('H')) {
                    heroCount++;
                }
            }
        }
        Boolean correctWumpusCount = ((mapVO.getSize() >= 6 && mapVO.getSize() <= 8 && wumpusCount == 1)
        || (mapVO.getSize() >= 9 && mapVO.getSize() <= 14 && wumpusCount == 2)
        || (mapVO.getSize() >= 15 && mapVO.getSize() <= 20 && wumpusCount == 1));
        return correctWumpusCount && goldCount == 1 && heroCount == 1;
    }
}
