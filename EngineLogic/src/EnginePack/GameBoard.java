package EnginePack;

import java.io.Serializable;

public class GameBoard implements Serializable
{
    private int rows;
    private int columns;
    private GameTerritory[][] boardGame;

    public GameTerritory[][] getBoardGame() {
        return boardGame;
    }

    public GameBoard(int rows, int columns)
    {
        this.rows=rows;
        this.columns=columns;
        boardGame= new GameTerritory[rows][columns];
        allocateMatrix(rows, columns);
    }
/*
    GameBoard(GameBoard objectBoard)
    {
        this.rows = objectBoard.rows;
        this.columns = objectBoard.columns;
        boardGame = new GameTerritory[rows][columns];
        allocateMatrix(rows,columns);
        for(int i=0; i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                boardGame[i][j]=objectBoard.boardGame[i][j];
            }
        }

    }
*/
    private void allocateMatrix(int rows, int columns)
    {
        for(int i=0; i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                boardGame[i][j]=new GameTerritory();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
