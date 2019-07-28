package Games;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GamesManager {

    private Map<String, GameController> m_Games;

    public GamesManager()
    {
        m_Games = new HashMap<>();
    }


    public synchronized void addGame(String i_GameContent, String i_GameCreator) {
        GameController newGame = new GameController();
        newGame.initGame(i_GameContent, i_GameCreator);
        if (isGameNameExists(newGame.getGameTitle())) {
            throw new IllegalArgumentException("Game title is already in use.");
        }

        m_Games.put(newGame.getGameTitle(), newGame);
    }

    public synchronized  GameController findGameController(String i_GameTitle)
    {
        return m_Games.get(i_GameTitle);
    }

    private boolean isGameNameExists(String i_GameTitle) {
       return  m_Games.containsKey(i_GameTitle);
    }

    public Collection<GameController> getGames() {
        return m_Games.values();
    }

    public GameController getGameByUserName(String userName) {
        GameController[] result = new GameController[1];
        this.m_Games.forEach((key, game) -> {
            if (game.hasPlayerWithName(userName)) {
                result[0] = game;
            }

        });
        return result[0];
    }



}
