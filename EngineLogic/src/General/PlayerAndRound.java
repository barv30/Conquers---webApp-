package General;

import EnginePack.GamePlayer;

public class PlayerAndRound {

    private GamePlayer player;
    private int currentRound;

    public PlayerAndRound(GamePlayer currPlayer, int round)
    {
        this.player = currPlayer;
        this.currentRound = round;
    }
}
