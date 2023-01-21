package org.puretripp.vassal.types.ranks;

import java.util.HashMap;

public enum TownRanks {
    LEADER(11),
    NOBLE(10),
    COUNCIL(9),
    TREASURER(8),
    MILITIA(7),
    POLICE(6),
    CITIZEN(5);

    public final int rankVal;

    public static TownRanks intToEnum(int x) {
        switch(x) {
            case 5:
                return CITIZEN;
            case 6:
                return POLICE;
            case 7:
                return MILITIA;
            case 8:
                return TREASURER;
            case 9:
                return COUNCIL;
            case 10:
                return NOBLE;
            case 11:
                return LEADER;
        }
        return null;
    }


    TownRanks(int rankVal) {
        this.rankVal = rankVal;
    }

    public int getValue(){
        return rankVal;
    }


}
