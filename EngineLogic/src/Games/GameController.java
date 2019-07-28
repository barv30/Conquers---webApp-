package Games;

import EnginePack.GameEngine;
import EnginePack.GamePlayer;
import EnginePack.GameTerritory;
import Users.User;
import generate.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameController {

    private GameEngine gameEngine;
    private List<User> m_UsersRegistered;
    private String m_CreatorOfGame;
    private String m_GameTitle;
    private int m_NumberOfRounds;
    private int m_currentRound;
    private int m_NumberOfRegisteredPlayers=0;
    private int m_BoardGameRows;
    private int m_BoardGameCols;
    private String m_GameStatus = "Inactive";
    private int m_turingsStart;
    private String m_GameContent;
    private int m_NumberOfPlayersNeededToStartGame;

    public String getM_CreatorOfGame() {
        return m_CreatorOfGame;
    }

    public String getM_GameContent() {
        return m_GameContent;
    }

    public void setM_currentRound(int m_currentRound) {
        this.m_currentRound = m_currentRound;
    }

    public void setM_GameStatus(String m_GameStatus) {
        this.m_GameStatus = m_GameStatus;
    }

    public String getM_GameStatus() {
        return m_GameStatus;
    }

    public List<User> getM_UsersRegistered() {
        return m_UsersRegistered;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public void initGame(String i_GameContent, String i_GameCreator) {
        gameEngine= new GameEngine();
        m_UsersRegistered = new ArrayList<>();
        String xmlReadOutCome = gameEngine.checkXml(i_GameContent);


        if (xmlReadOutCome.equals("Read file successfully") == false) {
            throw new IllegalArgumentException(xmlReadOutCome);
        }
        else{
            m_NumberOfRegisteredPlayers=0;
            m_GameContent = i_GameContent;
            m_GameStatus = "Inactive";
            m_CreatorOfGame = i_GameCreator;
            m_GameTitle=gameEngine.gameDescriptor.getDynamicPlayers().getGameTitle();
            m_NumberOfPlayersNeededToStartGame=gameEngine.getNumberOfPlayers();
            m_NumberOfRounds = gameEngine.getTotalCycles();
            m_currentRound = 1;
            m_BoardGameCols= gameEngine.getBoard().getColumns();
            m_BoardGameRows= gameEngine.getBoard().getRows();
            m_turingsStart = gameEngine.gameDescriptor.getGame().getInitialFunds().intValue();
        }
    }


    public String getGameTitle() {
        return m_GameTitle;
    }

    public String getCreatorOfGameName() {
        return m_CreatorOfGame;
    }

    public List<GamePlayer> getPlayerListOfGame()
    {
        return gameEngine.getPlayers();
    }

    public synchronized boolean addUser(User i_UserToAdd) {
        if(m_NumberOfRegisteredPlayers< m_NumberOfPlayersNeededToStartGame && m_GameStatus != "Active") {

            m_UsersRegistered.add(i_UserToAdd);
            GamePlayer tempPlayer=new GamePlayer();
            tempPlayer.setColor("None");
            tempPlayer.setName(i_UserToAdd.getName());
            tempPlayer.setNumberOfTerritories(0);
            tempPlayer.setTurings(m_turingsStart);
            gameEngine.getPlayers().add(tempPlayer);
            m_NumberOfRegisteredPlayers++;
            if(m_NumberOfRegisteredPlayers== m_NumberOfPlayersNeededToStartGame)
            {
                for(int i=0 ;i<m_UsersRegistered.size();i++)
                {
                    gameEngine.getPlayers().get(i).setColor(gameEngine.getColors()[i]);
                    m_UsersRegistered.get(i).setColor(gameEngine.getColors()[i]);
                    gameEngine.setTypeFunction( gameEngine.getPlayers().get(i));


                }

                m_GameStatus="Active";
            }
            return true;
        }

        return false;
    }

    public boolean hasPlayerWithName(String name) {
        Iterator var2 = this.getGameEngine().getPlayers().iterator();

        GamePlayer player;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            player = (GamePlayer)var2.next();
        } while(!player.getName().equals(name));

        return true;
    }

    public synchronized void removePlayerFromGame(User userToRemove){
            gameEngine.removePlayer(userToRemove.getName());

            //User toRemove = null;
            for (User user : m_UsersRegistered) {

                if (user.getName().equals(userToRemove.getName())) {
                    m_UsersRegistered.remove(user);
                    break;
                }
            }



            --m_NumberOfRegisteredPlayers;
            m_GameStatus = "inactive";

    }

    public void retirePlayer(User userToRetire)
    {
        GamePlayer player = gameEngine.findPlayerByName(userToRetire.getName());
        gameEngine.setPlayerNextTurn(player);
        for(int i=0; i<m_BoardGameRows; i++)
        {
            for(int j=0;j<m_BoardGameCols;j++)
            {
                GameTerritory territory = gameEngine.getBoard().getBoardGame()[i][j];
                if(territory.getType() == player.getType())
                {
                    gameEngine.manageMakeTerritoryNeutral(territory);
                }
            }
        }

        removePlayerFromGame(userToRetire);
        m_GameStatus = "Active";
        gameEngine.setNumberOfPlayers(gameEngine.getNumberOfPlayers()-1);
        if(gameEngine.getPlayers().size() == 1)
        {
            m_GameStatus="Finished";
        }
    }
}
