package EnginePack;

import java.io.Serializable;

public class GamePlayer implements Serializable
{
    String name;
    int id;
    private double turings;
    private int numberOfTerritories;
    private GameTerritory.TypeOfTerritory type;
    private String color;
    public double getTurings() {
        return turings;
    }

    public void setTurings(double turings) {
        this.turings = turings;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setNumberOfTerritories(int numberOfSurface) {
        this.numberOfTerritories = numberOfSurface;
    }

    public void setType(GameTerritory.TypeOfTerritory type) {
        this.type = type;
    }

    public int getNumberOfTerritories() {
        return numberOfTerritories;
    }

    public GameTerritory.TypeOfTerritory getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
