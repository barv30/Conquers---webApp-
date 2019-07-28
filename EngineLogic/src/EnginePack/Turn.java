package EnginePack;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Turn {
    private eCurrentTurn m_CurrentTurn=eCurrentTurn.FIRST_PLAYER_TURN;
    private final StringProperty k_CurrentPlayerName = new SimpleStringProperty();
    public enum eCurrentTurn implements Serializable {

        FIRST_PLAYER_TURN(0),
        SECOND_PLAYER_TURN(1),
        THIRD_PLAYER_TURN(2),
        FOURTH_PLAYER_TURN(3),
        FIFTH_PLAYER_TURN(4),
        SIXTH_PLAYER_TURN(5);

        private int value;
        private static Map intToEnum = new HashMap<>();
        eCurrentTurn(int i_Value)
        {
            value=i_Value;
        }
        static{
            for(eCurrentTurn currentTurn: eCurrentTurn.values())
            {
                intToEnum.put(currentTurn.value,currentTurn);
            }
        }

        public static eCurrentTurn valueOf(int i_Value)
        {
            return (eCurrentTurn)intToEnum.get(i_Value);
        }
        public int getValue(){
            return value;
        }
    }

    public void changeTurn(int i_NumOfPlayers, String i_PlayerName)
    {
        setCurrentTurn(getNextTurn(i_NumOfPlayers),i_PlayerName);
    }
    public void setCurrentTurn(eCurrentTurn i_CurrTurn, String i_PlayerName)
    {
       m_CurrentTurn=i_CurrTurn;
       k_CurrentPlayerName.setValue(i_PlayerName);
    }
    public eCurrentTurn getCurrentTurn() {
        return m_CurrentTurn;
    }

    public eCurrentTurn getNextTurn(int i_NumOfPlayers)
    {
        final int NEXT_TURN=1;

        return eCurrentTurn.valueOf((m_CurrentTurn.getValue()+NEXT_TURN) % i_NumOfPlayers);
    }

    public String getCurrentPlayerName() {
        return k_CurrentPlayerName.get();
    }

    public StringProperty getCurrentPlayerNameProperty() {
        return k_CurrentPlayerName;
    }
}
