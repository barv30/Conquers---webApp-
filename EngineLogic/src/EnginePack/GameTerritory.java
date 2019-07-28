package EnginePack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameTerritory implements Serializable
{
    public enum TypeOfTerritory
    {
        GREEN,RED,ORANGE,GRAY,N
    }

    private int id;
    private int profit;
    private int armyThreshold;
    private GameTerritory.TypeOfTerritory type;
    private int currentPower;
    private List<ArmyUnit> unitInTerritory;


    public GameTerritory()
    {
        type= GameTerritory.TypeOfTerritory.N;
        currentPower=0;
        unitInTerritory = new ArrayList<ArmyUnit>();

    }

    public void setType(TypeOfTerritory type) {
        this.type = type;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }
    public List<ArmyUnit> getUnitInTerritory() {
        return unitInTerritory;
    }

    public void setUnitInTeritory(List<ArmyUnit> unitInTeritory) {
        this.unitInTerritory = unitInTeritory;
    }

    public GameTerritory.TypeOfTerritory getType() {
        return type;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public void setArmyThreshold(int armyThreshold) {
        this.armyThreshold = armyThreshold;
    }

    public int getId() {
        return id;
    }

    public int getArmyThreshold() {
        return armyThreshold;
    }

    public int getProfit() {
        return profit;
    }
}
