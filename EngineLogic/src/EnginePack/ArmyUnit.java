package EnginePack;

import java.io.Serializable;

public class ArmyUnit implements Serializable
{

    private double purchase;
    private int maxFirePower;
    private int competenceReduction;
    private byte rank;
    private String type;
    private int currentPower;
    private int counterUnits;
    double costOfMaintainArmy;

    public void setPurchase(double purchase) {
        this.purchase = purchase;
    }

    public ArmyUnit(double purchase,int maxFirePower, int competenceReduction,byte rank, String type)
    {
        this.purchase=purchase;
        this.maxFirePower=maxFirePower;
        this.competenceReduction=competenceReduction;
        this.rank=rank;
        this.type=type;
        currentPower=maxFirePower;
        costOfMaintainArmy =(purchase/(double)maxFirePower);

    }

    public void setCostOfMaintainArmy(double costOfMaintainArmy) {
        this.costOfMaintainArmy = costOfMaintainArmy;
    }

    public double getCostOfMaintainArmy() {
        return costOfMaintainArmy;
    }

    public int getCounterUnits() {
        return counterUnits;
    }

    public void setCounterUnits(int counterUnits) {
        this.counterUnits = counterUnits;
    }

    public byte getRank() {
        return rank;
    }

    public String getType() {
        return type;
    }

    public double getPurchase() {
        return purchase;
    }

    public int getMaxFirePower() {
        return maxFirePower;
    }

    public void setMaxFirePower(int maxFirePower) {
        this.maxFirePower = maxFirePower;
    }

    public void setCompetenceReduction(int competenceReduction) {
        this.competenceReduction = competenceReduction;
    }

    public void setRank(byte rank) {
        this.rank = rank;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCompetenceReduction() {
        return competenceReduction;
    }

    public ArmyUnit()
    {
        currentPower= maxFirePower;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }

}