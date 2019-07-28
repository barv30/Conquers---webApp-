package EnginePack;


import Users.User;
import generate.GameDescriptor;
import generate.Player;
import javafx.scene.control.Alert;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameEngine implements Serializable {
    protected GameBoard board;
    protected int initialFunds;
    private String [] colors={"GREEN" ,"RED","ORANGE","GRAY"};
    protected List<GameTerritory> territories;
    protected int totalCycles;
    private int defaultProfit;
    private int defaultArmyThreshold;
    private List<ArmyUnit> infoArmy;
    private int counterOfRounds;
    private int numberOfPlayers;
    private int counterPlayers;
    private List<GamePlayer> players;
    public boolean gameStarted = false;
    private GameTerritory.TypeOfTerritory typeCurrentPlayer;
    private int currentPlayerNumber;
    public generate.GameDescriptor gameDescriptor;
    private int numberOfTypesUnit;
    private boolean haveMoneyToBuy;
    private boolean buyArmy;


    public int getNumberOfTypesUnit() {
        return numberOfTypesUnit;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public int getDefaultArmyThreshold() {
        return defaultArmyThreshold;
    }

    public int getDefaultProfit() {
        return defaultProfit;
    }

    public List<GameTerritory> getTerritories() {
        return territories;
    }

    public void initGameEngine() {
        board = new GameBoard(gameDescriptor.getGame().getBoard().getRows().intValue(), gameDescriptor.getGame().getBoard().getColumns().intValue());
        initialFunds = gameDescriptor.getGame().getInitialFunds().intValue();
        territories = new ArrayList<GameTerritory>(gameDescriptor.getGame().getTerritories().getTeritory().size());
        initTerritoryList();
        totalCycles = gameDescriptor.getGame().getTotalCycles().intValue();
        defaultProfit = gameDescriptor.getGame().getTerritories().getDefaultProfit().intValue();
        defaultArmyThreshold = gameDescriptor.getGame().getTerritories().getDefaultArmyThreshold().intValue();
        counterOfRounds = 1;
        numberOfPlayers = gameDescriptor.getDynamicPlayers().getTotalPlayers().intValue();
        counterPlayers = numberOfPlayers;
        players = new ArrayList<GamePlayer>(numberOfPlayers);
        currentPlayerNumber = 0;
        typeCurrentPlayer = GameTerritory.TypeOfTerritory.GREEN;
        infoArmy = new ArrayList<ArmyUnit>(gameDescriptor.getGame().getArmy().getUnit().size());
        initUnitsList();
        numberOfTypesUnit = infoArmy.size();
        fillDatainBoard();
    }

    public void setPlayerNextTurn(GamePlayer player)
    {
        int index = getLocationInPlayerList(player);
        if( index+1 == players.size())
        {
            currentPlayerNumber = 0;
            typeCurrentPlayer = players.get(0).getType();
        }
        else // change to next player in list
        {
            currentPlayerNumber = index;
            typeCurrentPlayer = players.get(index+1).getType();

        }
    }

    private int getLocationInPlayerList(GamePlayer player)
    {
        for(int i=0;i<players.size();i++)
        {
            if(players.get(i).getName().equals(player.getName()))
            {
                return i;
            }
        }
        return -1;
    }

    public void removePlayer(String nameToDelete)
    {
        GamePlayer toRemove = findPlayerByName(nameToDelete);
        players.remove(toRemove);
    }

    public GamePlayer findPlayerByName(String name)
    {
        for(GamePlayer player : players)
        {
            if(player.getName().equals(name))
            {
                return player;
            }
        }
        return null;
    }
        public String changeTurnAndInitRound()
    {
        String stringRound = null;
        ++this.currentPlayerNumber;
        counterPlayers = currentPlayerNumber+1;
        if (this.currentPlayerNumber == this.players.size()) {
            counterOfRounds ++;
            if(counterOfRounds == totalCycles+1) {
                stringRound = "FINISH";
            }
            else {
                stringRound = "NEW_ROUND";
                this.counterPlayers = 0;
            }
            this.currentPlayerNumber = 0;
        }

        typeCurrentPlayer = players.get(currentPlayerNumber).getType();
        return stringRound;

    }

    public String[] getColors() {
        return colors;
    }

    public void setTypeFunction(GamePlayer p)
    {
        String color = p.getColor();

        switch(color)
        {
            case "GREEN":
            {
                p.setType(GameTerritory.TypeOfTerritory.GREEN);
                break;
            }

            case "RED":
            {
                p.setType(GameTerritory.TypeOfTerritory.RED);
                break;
            }

            case "ORANGE":
            {
                p.setType(GameTerritory.TypeOfTerritory.ORANGE);
                break;
            }

            case "GRAY":
            {
                p.setType(GameTerritory.TypeOfTerritory.GRAY);
                break;
            }

        }
    }

    /*
        public void UpdatePlayersList()
        {
            setCounterPlayers(getCounterPlayers() - 1);
            ArrayList<GamePlayer> newList = new ArrayList<GamePlayer>();
            GamePlayer currentPlayer = getCurrentPlayer();
            for(int i=0 ;i<players.size();i++)
            {
                if(players.get(i).getType() != currentPlayer.getType())
                {
                    newList.add(players.get(i));
                }
                else
                {
                    if(i!=players.size()-1)
                        setTypeCurrentPlayer(players.get(i+1).getType());
                    else
                        setTypeCurrentPlayer(players.get(0).getType());

                }

            }

            players = newList;

        }
        public void writeToFile(String myFileName) throws IOException {

            File myFile = new File(myFileName);
            myFile.createNewFile();
            try (ObjectOutputStream out =
                         new ObjectOutputStream(
                                 new FileOutputStream(myFile))) {
                out.writeObject(this);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static GameEngine readFromFile(String myFileName) throws IOException, ClassNotFoundException {
            GameEngine gameFromFile = new GameEngine();
            try (ObjectInputStream in =
                         new ObjectInputStream(
                                 new FileInputStream(myFileName))) {

                gameFromFile = (GameEngine) in.readObject();

            }
            return gameFromFile;
        }
    */
    private void initPlayersList() {
        for (int i = 0; i < gameDescriptor.getDynamicPlayers().getTotalPlayers().intValue(); i++) {
            players.add(new GamePlayer());
            //players.get(i).setName(gameDescriptor.getPlayers().getPlayer().get(i).getName());
            //players.get(i).setId(gameDescriptor.getPlayers().getPlayer().get(i).getId().intValue());
            //players.get(i).setTurings(gameDescriptor.getGame().getInitialFunds().intValue());
            players.get(i).setNumberOfTerritories(0);
            if (i == 0) {
                players.get(i).setType(GameTerritory.TypeOfTerritory.GREEN);
            } else if(i==1) {
                players.get(i).setType(GameTerritory.TypeOfTerritory.RED);
            }
            else if(i==2){
                players.get(i).setType(GameTerritory.TypeOfTerritory.ORANGE);
            }
            else{
                players.get(i).setType(GameTerritory.TypeOfTerritory.GRAY);

            }
        }
    }

    private void initUnitsList() {
        for (int i = 0; i < gameDescriptor.getGame().getArmy().getUnit().size(); i++) {
            infoArmy.add(new ArmyUnit());
            infoArmy.get(i).setCompetenceReduction(gameDescriptor.getGame().getArmy().getUnit().get(i).getCompetenceReduction().intValue());
            infoArmy.get(i).setMaxFirePower(gameDescriptor.getGame().getArmy().getUnit().get(i).getMaxFirePower().intValue());
            infoArmy.get(i).setRank(gameDescriptor.getGame().getArmy().getUnit().get(i).getRank());
            infoArmy.get(i).setPurchase(gameDescriptor.getGame().getArmy().getUnit().get(i).getPurchase().doubleValue());
            infoArmy.get(i).setType(gameDescriptor.getGame().getArmy().getUnit().get(i).getType());
            infoArmy.get(i).setCounterUnits(0);
            infoArmy.get(i).setCostOfMaintainArmy(infoArmy.get(i).getPurchase()/(double)infoArmy.get(i).getMaxFirePower());



        }
    }


    private void initTerritoryList() {
        for (int i = 0; i < gameDescriptor.getGame().getTerritories().getTeritory().size(); i++) {
            territories.add(new GameTerritory());
            territories.get(i).setId(gameDescriptor.getGame().getTerritories().getTeritory().get(i).getId().intValue());
            territories.get(i).setProfit(gameDescriptor.getGame().getTerritories().getTeritory().get(i).getProfit().intValue());
            territories.get(i).setArmyThreshold(gameDescriptor.getGame().getTerritories().getTeritory().get(i).getArmyThreshold().intValue());
        }
    }

    public void calculateTuringsForPlayer(GamePlayer currentPlayer) {
        GameTerritory.TypeOfTerritory typeOfPlayer = currentPlayer.getType();
        for (int i = 0; i < getBoard().getRows(); i++) {
            for (int j = 0; j < getBoard().getColumns(); j++) {
                if (getBoard().getBoardGame()[i][j].getType() == typeOfPlayer) {
                    currentPlayer.setTurings(getBoard().getBoardGame()[i][j].getProfit() + currentPlayer.getTurings());
                }
            }
        }
    }

    public ArrayList<GameTerritory> calculateLostPowerAndTerritories(GamePlayer currentPlayer) {
        GameTerritory.TypeOfTerritory typeOfPlayer = currentPlayer.getType();
        List<ArmyUnit> needsToRemove = new ArrayList<ArmyUnit>();
        ArrayList<GameTerritory> lostTerritory = new ArrayList<GameTerritory>();
        for (int i = 0; i < getBoard().getRows(); i++) {
            for (int j = 0; j < getBoard().getColumns(); j++) {
                GameTerritory currentTerritory = getBoard().getBoardGame()[i][j];
                if (currentTerritory.getType() == typeOfPlayer) {
                    for (int k = 0; k < currentTerritory.getUnitInTerritory().size(); k++) {
                        manageUnitToRemoveAfterLostPower(currentTerritory, needsToRemove, k);
                    }

                    for (int k = 0; k < needsToRemove.size(); k++) {
                        currentTerritory.getUnitInTerritory().remove(needsToRemove.get(k));
                    }
                    if (currentTerritory.getCurrentPower() < currentTerritory.getArmyThreshold()) {
                        manageLostTerritoryForPlayer(currentPlayer, currentTerritory);
                        lostTerritory.add(currentTerritory);
                    }
                }
            }
        }
        return lostTerritory;
    }

    private void manageUnitToRemoveAfterLostPower(GameTerritory currentTerritory, List<ArmyUnit> needsToRemove, int k) {
        currentTerritory.getUnitInTerritory().get(k).setCurrentPower(currentTerritory.getUnitInTerritory().get(k).getCurrentPower() - currentTerritory.getUnitInTerritory().get(k).getCompetenceReduction());
        currentTerritory.setCurrentPower(currentTerritory.getCurrentPower() - currentTerritory.getUnitInTerritory().get(k).getCompetenceReduction());

        if (currentTerritory.getUnitInTerritory().get(k).getCurrentPower() <= 0) {
            needsToRemove.add(currentTerritory.getUnitInTerritory().get(k));
        }
    }

    private void manageLostTerritoryForPlayer(GamePlayer currentPlayer, GameTerritory currentTerritory) {
        currentPlayer.setNumberOfTerritories(currentPlayer.getNumberOfTerritories() - 1);
        currentTerritory.setCurrentPower(0);
        currentTerritory.setUnitInTeritory(new ArrayList<ArmyUnit>());
        currentTerritory.setType(GameTerritory.TypeOfTerritory.N);
        currentPlayer.setTurings(currentPlayer.getTurings() - currentTerritory.getProfit());
    }

    public double calculateTuringToGetMaxFirePower(GameTerritory territory) {
        double result = 0;
        int amount;

        for (int k = 0; k < territory.getUnitInTerritory().size(); k++) {
            amount= territory.getUnitInTerritory().get(k).getMaxFirePower() - territory.getUnitInTerritory().get(k).getCurrentPower();
            result+= amount * territory.getUnitInTerritory().get(k).getCostOfMaintainArmy();
        }

        return result ;
    }

    public void passTurn() {
            setCounterPlayers(getCounterPlayers() - 1);
            for(int i=0;i<getPlayers().size();i++)
                if(i!= (getPlayers().size()-1))
                {
                    if (getTypeCurrentPlayer() == players.get(i).getType())
                    {
                        setTypeCurrentPlayer(players.get(i + 1).getType());
                        break;
                    }
                }
                else
                    {
                        setTypeCurrentPlayer(players.get(0).getType());
                    }
        }

    public boolean maintainArmy(GameTerritory territory) {

        double number = calculateTuringToGetMaxFirePower(territory);
        GamePlayer currentPlayer = getCurrentPlayer();
        int addPower;
        boolean result;
        if (number <= currentPlayer.getTurings()) {
            for (int i = 0; i < territory.getUnitInTerritory().size(); i++) {
                addPower = territory.getUnitInTerritory().get(i).getMaxFirePower() - territory.getUnitInTerritory().get(i).getCurrentPower();
                territory.getUnitInTerritory().get(i).setCurrentPower(territory.getUnitInTerritory().get(i).getCurrentPower() + addPower);
            }

            result = true;
            currentPlayer.setTurings(currentPlayer.getTurings() - number);
        } else {
            result = false;
        }

        return result;
    }

    public GamePlayer getCurrentPlayer() {
        GamePlayer currentPlayer= null;

        for(int i=0;i<players.size();i++)
        {
            if(players.get(i).getType() == getTypeCurrentPlayer()) {
                currentPlayer = players.get(i);
                break;
            }

        }
        return currentPlayer;
    }

    public boolean checkProximity(int[] rowAndCol) {
        GameTerritory[][] board = getBoard().getBoardGame();
        GameTerritory.TypeOfTerritory type = getCurrentPlayer().getType();
        int row = rowAndCol[0];
        int col = rowAndCol[1];

        //corners

        //row = 0 , col = 0
        if (rowAndCol[0] == 0 && rowAndCol[1] == 0) {
            if (board[row + 1][col].getType() == type || board[row][col + 1].getType() == type || board[0][0].getType() == type)
                return true;
        }

        //row = rows  , col= columns
        else if (rowAndCol[0] == getBoard().getRows() - 1 && rowAndCol[1] == getBoard().getColumns() - 1) {
            if (board[row - 1][col].getType() == type || board[row][col - 1].getType() == type || board[row][col].getType() == type)
                return true;
        }

        //row = rows ,col=0
        else if (rowAndCol[0] == getBoard().getRows() - 1 && rowAndCol[1] == 0) {
            if (board[row - 1][col].getType() == type || board[row][col + 1].getType() == type || board[row][col].getType() == type)
                return true;
        }

        //row=0, col=columns
        else if (rowAndCol[1] == getBoard().getColumns() - 1 && rowAndCol[0] == 0) {
            if (board[row + 1][col].getType() == type || board[row][col - 1].getType() == type || board[row][col].getType() == type)
                return true;
        }

        //col=0
        else if (rowAndCol[1] == 0) {
            if (board[row + 1][col].getType() == type || board[row - 1][col].getType() == type || board[row][col + 1].getType() == type || board[row][col].getType() == type)
                return true;
        }

        //row=0
        else if (rowAndCol[0] == 0) {
            if (board[row][col + 1].getType() == type || board[row][col - 1].getType() == type || board[row + 1][col].getType() == type || board[row][col].getType() == type)
                return true;
        }

        //col= columns
        else if (rowAndCol[1] == getBoard().getColumns() - 1) {
            if (board[row][col - 1].getType() == type || board[row + 1][col].getType() == type || board[row - 1][col].getType() == type || board[row][col].getType() == type)
                return true;
        }

        //row=rows
        else if (rowAndCol[0] == getBoard().getRows() - 1) {
            if (board[row - 1][col].getType() == type || board[row][col + 1].getType() == type || board[row][col - 1].getType() == type || board[row][col].getType() == type)
                return true;
        } else if (board[row - 1][col].getType() == type || board[row + 1][col].getType() == type || board[row][col + 1].getType() == type || board[row][col - 1].getType() == type || board[row][col].getType() == type) {
            return true;
        }

        return false;
    }

    public static generate.GameDescriptor fromXmlFileToObject(StringReader fileContent) {

        generate.GameDescriptor game = null;
        try {
            //File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(generate.GameDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            game = (generate.GameDescriptor) jaxbUnmarshaller.unmarshal(fileContent);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return game;
    }

    public boolean manageCalculateRiskAttack(GameTerritory currentTer, ArrayList<UnitToBuy> unitList, int row,int col, ArrayList<UnitToBuy> listOfUnitInTerritory) {
        int powerCurrentPlayer=0;
        GamePlayer playerOfTer = getPlayer(currentTer.getType());
        if(!checkIfPlayerHaveTuringsToBuy(getCurrentPlayer(),unitList))
        {
            return false;
        }
        insertUnitOfTerritoryToList(currentTer,listOfUnitInTerritory);
        for(int i=0;i<unitList.size();i++) {
            ArmyUnit unit = searchUnitInList(unitList.get(i).getType());
            if(unit != null)
                powerCurrentPlayer+= unit.getMaxFirePower() * unitList.get(i).getAmount();
        }

        int powerCurrentTerritory = currentTer.getCurrentPower();
        boolean winnerCurrentTer = false;
        boolean strongCurrentTer = false;
        boolean attackSucc = false;

        if (currentTer.getArmyThreshold() < powerCurrentPlayer) {
            double sum = powerCurrentPlayer + powerCurrentTerritory;
            double checkProb1 = powerCurrentPlayer / sum;
            double checkProb2 = powerCurrentTerritory / sum;
            double number = Math.random();
            if (powerCurrentPlayer > powerCurrentTerritory) //current player have more power
            {
                strongCurrentTer = true;
                if (number < checkProb2) {
                    winnerCurrentTer = true;
                }
            } else // player of territory have more power
                {
                if (number < checkProb2) {
                    winnerCurrentTer = true;
                }
            }

            if (winnerCurrentTer == true) {
                attackSucc = false;
                manageWinnerCurrentPlayerTerritory(currentTer, powerCurrentPlayer, strongCurrentTer, powerCurrentTerritory, row,col);
            } else {
                playerOfTer.setNumberOfTerritories(playerOfTer.getNumberOfTerritories()-1);
               attackSucc= manageWinnerCurrentPlayer(unitList,strongCurrentTer, currentTer, powerCurrentTerritory, powerCurrentPlayer,row,col);

            }
        }

        return attackSucc;
    }

    private boolean manageWinnerCurrentPlayer(ArrayList<UnitToBuy> unitList, boolean strongCurrent, GameTerritory currentTer, int powerCurrentTerritory, int powerCurrentPlayer,int row , int col) {

        getCurrentPlayer().setNumberOfTerritories(getCurrentPlayer().getNumberOfTerritories()+1);
        ArmyUnit unit;

        updateCounterOfUnit(currentTer);
        currentTer.setUnitInTeritory(new ArrayList<>());
        getBoard().getBoardGame()[row][col].setType(getCurrentPlayer().getType());
        addUnitToTerritory(currentTer,unitList);
        currentTer.setCurrentPower(0);
        for (int i = 0; i < currentTer.getUnitInTerritory().size(); i++) {
            if (strongCurrent == false) {
                currentTer.getUnitInTerritory().get(i).setCurrentPower((int) Math.ceil(powerCurrentTerritory / powerCurrentPlayer) * currentTer.getUnitInTerritory().get(i).getMaxFirePower());
            }
            else {
                currentTer.getUnitInTerritory().get(i).setCurrentPower((int) Math.ceil(0.5 * currentTer.getUnitInTerritory().get(i).getMaxFirePower()));
            }

            currentTer.setCurrentPower(currentTer.getCurrentPower() + currentTer.getUnitInTerritory().get(i).getCurrentPower());

        }


        if (currentTer.getCurrentPower() < currentTer.getArmyThreshold()) {
            getCurrentPlayer().setNumberOfTerritories(getCurrentPlayer().getNumberOfTerritories()-1);
            for(int i=0;i<currentTer.getUnitInTerritory().size();i++)
            {
                unit = currentTer.getUnitInTerritory().get(i);
                unit.setCounterUnits(unit.getCounterUnits()-1);
            }
            board.getBoardGame()[row][col].setType(GameTerritory.TypeOfTerritory.N);
            manageMakeTerritoryNeutral(currentTer);
            return false;
        }
        return true;
    }

    private void manageWinnerCurrentPlayerTerritory(GameTerritory currentTer, int powerCurrentPlayer, boolean strongCurrent, int powerCurrentTerritory, int row, int col) {
                    GamePlayer playerOfTer = getPlayer(currentTer.getType());
                    currentTer.setCurrentPower(0);
                    for (int j = 0; j < currentTer.getUnitInTerritory().size();j++)
                    {
                        if (strongCurrent == true) {
                            double prob = (double) (powerCurrentTerritory) / (double) (powerCurrentPlayer);
                            currentTer.getUnitInTerritory().get(j).setCurrentPower((int) Math.ceil(prob) * currentTer.getUnitInTerritory().get(j).getMaxFirePower());
                        }
                    else {
                            currentTer.getUnitInTerritory().get(j).setCurrentPower((int) Math.ceil(0.5 * currentTer.getUnitInTerritory().get(j).getMaxFirePower()));
                        }
                        currentTer.setCurrentPower(currentTer.getCurrentPower() + currentTer.getUnitInTerritory().get(j).getCurrentPower());
                    }

                    if(currentTer.getCurrentPower() < currentTer.getArmyThreshold()) // become neutral
                    {
                        ArmyUnit unit;
                        for(int i=0;i<currentTer.getUnitInTerritory().size();i++)
                        {
                            unit = currentTer.getUnitInTerritory().get(i);
                            unit.setCounterUnits(unit.getCounterUnits()-1);
                        }

                        playerOfTer.setNumberOfTerritories(playerOfTer.getNumberOfTerritories()-1);
                        board.getBoardGame()[row][col].setType(GameTerritory.TypeOfTerritory.N);
                        manageMakeTerritoryNeutral(currentTer);

                    }


                }

    private void insertUnitOfTerritoryToList(GameTerritory ter, ArrayList<UnitToBuy> list)
    {
        int amount=0;
        for(int i=0;i<infoArmy.size();i++)
        {
            amount = calculateAmountOfUnitInTer(ter,infoArmy.get(i).getType());
            list.add(new UnitToBuy(infoArmy.get(i).getType(),amount));

        }
    }

    public void manageMakeTerritoryNeutral(GameTerritory currentTer) {
        currentTer.setUnitInTeritory(new ArrayList<ArmyUnit>());
        currentTer.setCurrentPower(0);
        currentTer.setType(GameTerritory.TypeOfTerritory.N);
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public int getTotalCycles() {
        return totalCycles;
    }

    public void setCounterPlayers(int counterPlayers) {
        this.counterPlayers = counterPlayers;
    }

    public int getCounterPlayers() {
        return counterPlayers;
    }

    public void setCounterOfRounds(int counterOfRounds) {
        this.counterOfRounds = counterOfRounds;
    }

    public void setTypeCurrentPlayer(GameTerritory.TypeOfTerritory typeCurrentPlayer) {
        this.typeCurrentPlayer = typeCurrentPlayer;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public GameTerritory.TypeOfTerritory getTypeCurrentPlayer() {
        return typeCurrentPlayer;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public GameBoard getBoard() {
        return board;
    }

    public List<ArmyUnit> getInfoArmy() {
        return infoArmy;
    }

    public int getCounterOfRounds() {
        return counterOfRounds;
    }

    public GameTerritory.TypeOfTerritory checkTheWinner() {
        if(numberOfPlayers==1)
        {
            return typeCurrentPlayer;
        }

        int sumPlayerGreen = 0;
        int sumPlayerRed = 0;
        int sumPlayerOrange = 0;
        int sumPlayerGray = 0;

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                GameTerritory curretTer = board.getBoardGame()[i][j];
                if (curretTer.getType() == GameTerritory.TypeOfTerritory.GREEN) {
                    sumPlayerGreen += curretTer.getProfit();
                } else if (curretTer.getType() == GameTerritory.TypeOfTerritory.RED) {
                    sumPlayerRed += curretTer.getProfit();
                } else if (curretTer.getType() == GameTerritory.TypeOfTerritory.ORANGE) {
                    sumPlayerOrange += curretTer.getProfit();
                } else if (curretTer.getType() == GameTerritory.TypeOfTerritory.GRAY) {
                    sumPlayerGray += curretTer.getProfit();
                }
            }
        }

        if (sumPlayerGreen > sumPlayerRed && sumPlayerGreen > sumPlayerOrange && sumPlayerGreen > sumPlayerGray) {
            return GameTerritory.TypeOfTerritory.GREEN;
        } else if (sumPlayerRed > sumPlayerGreen && sumPlayerRed > sumPlayerOrange && sumPlayerRed > sumPlayerGray) {
            return GameTerritory.TypeOfTerritory.RED;
        } else if (sumPlayerOrange > sumPlayerGreen && sumPlayerOrange > sumPlayerRed && sumPlayerOrange > sumPlayerGray) {
            return GameTerritory.TypeOfTerritory.ORANGE;
        } else if (sumPlayerGray > sumPlayerGreen && sumPlayerGray > sumPlayerRed && sumPlayerGray > sumPlayerOrange) {
            return GameTerritory.TypeOfTerritory.GRAY;

        }
        return GameTerritory.TypeOfTerritory.N;
    }

        private void fillDatainBoard() {
        int numberCell;
        int counter = 0;
        int id = territories.get(counter).getId();

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                numberCell = board.getBoardGame()[0].length * i + j;
                if (territories.size() > counter) {
                    id = territories.get(counter).getId();
                }

                if ((numberCell + 1) == id) {
                    int profit = territories.get(counter).getProfit();
                    int armyThreshold = territories.get(counter).getArmyThreshold();
                    board.getBoardGame()[i][j].setId(id);
                    board.getBoardGame()[i][j].setProfit(profit);
                    board.getBoardGame()[i][j].setArmyThreshold(armyThreshold);
                    counter++;

                } else {
                    board.getBoardGame()[i][j].setId(numberCell+1);
                    board.getBoardGame()[i][j].setProfit(defaultProfit);
                    board.getBoardGame()[i][j].setArmyThreshold(defaultArmyThreshold);
                }
            }
        }
    }


    public String checkXml(String fileContent) {
        StringReader readString=new StringReader(fileContent);
        generate.GameDescriptor gameXml = fromXmlFileToObject(readString);
        String res = null;
        BigInteger rowsN = gameXml.getGame().getBoard().getRows();
        int valueR = rowsN.intValue();
        BigInteger columnsN = gameXml.getGame().getBoard().getColumns();
        int valueC = columnsN.intValue();
        int numberOfPlayers = gameXml.getDynamicPlayers().getTotalPlayers().intValue();
        if ((valueR < 2) || (valueR > 30)) {
            res = "Error: number of rows is invalid.it needs to be number 2-30";
        }
        else if (numberOfPlayers < 2 || numberOfPlayers >4)
        {
            res = "Error: number of players is invalid. it needs to be 2-4 players";
        }
         else if ((valueC < 3) || (valueC > 30))
        {
            res = "Error: number of columns is invalid. it needs to be number 3-30";
        }

        else if(checkValidUnits(gameXml))
        {
            res = "Error: there is a 2 unit with the same type or rank";
        }
         else if(checkValidRank(gameXml))
        {
            res = "Error: invalid rank in army units";
        }

         else{
            List<generate.Teritory> territories = gameXml.getGame().getTerritories().getTeritory();
            for (int i = 0; i < territories.size(); i++) {
                if (territories.get(i).getId().intValue() > (valueC * valueR)) {
                    res = "Error: you have territory's id invalid";
                    i = territories.size() - 1;
                } else {
                    for (int j = 0; j < territories.size(); j++) {
                        if (territories.get(j).getId().equals(territories.get(i).getId()) && (i != j)) {
                            res = "Error: you have 2 equals territory's id";
                            j = territories.size() - 1;
                            i = j;
                        }
                    }
                }

            }
            if (res == null) {
                if (gameXml.getGame().getTerritories().getDefaultArmyThreshold() == null) {
                    gameXml.getGame().getTerritories().setDefaultArmyThreshold(BigInteger.valueOf(100));
                }

                if (gameXml.getGame().getTerritories().getDefaultProfit() == null) {
                    gameXml.getGame().getTerritories().setDefaultProfit(BigInteger.valueOf(10));
                }

                gameDescriptor = gameXml;
                initGameEngine();
                res="Read file successfully";
            }
        }

         return res;
    }

    private boolean checkSameIdPlayers(GameDescriptor gameXml)
    {
        List<Player> listPlayer =gameXml.getPlayers().getPlayer();
        for(int i=0; i<listPlayer.size();i++)
        {
            int id = listPlayer.get(i).getId().intValue();
            for(int j=0;j<listPlayer.size();j++)
            {
                if(id == listPlayer.get(j).getId().intValue() && i!=j)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkValidRank(GameDescriptor gameXml)
    {
        List<generate.Unit> listUnits = gameXml.getGame().getArmy().getUnit();
        for(int i=0;i<listUnits.size();i++)
        {
            if(listUnits.get(i).getRank() != (byte)(i+1))
            {
                return true;
            }
        }
        return false;
    }

    private boolean checkValidUnits(GameDescriptor gameXml)
    {
        List<generate.Unit> listUnits = gameXml.getGame().getArmy().getUnit();
        for(int i=0;i<listUnits.size();i++)
        {
            byte rank = listUnits.get(i).getRank();
            String type = listUnits.get(i).getType();
            for(int j=0;j<listUnits.size();j++)
            {
                if((listUnits.get(j).getRank() == rank && i!=j) ||
                listUnits.get(j).getType().equals(type) &&i!=j)
                {
                    return true;
                }
            }
        }
        return false;

    }

    public boolean buyArmy(ArrayList<UnitToBuy> listUnit, GameTerritory ter,GamePlayer currentPlayer,int row,int col)
    {
        double turingsToBuy=0;
        int amountOfPower=0;
        for(int i=0;i<listUnit.size();i++)
        {
            ArmyUnit unit = searchUnitInList(listUnit.get(i).getType());
            if(unit != null)
            {
            turingsToBuy += listUnit.get(i).getAmount() * unit.getPurchase();
            amountOfPower+=listUnit.get(i).getAmount() * unit.getMaxFirePower();

            }
        }

        if(turingsToBuy > currentPlayer.getTurings())
        {
            buyArmy=false;
            return false;
        }

        if(amountOfPower < ter.getArmyThreshold())
        {
            buyArmy=false;
            return false;
        }

        addUnitToTerritory(ter,listUnit);
        currentPlayer.setTurings(currentPlayer.getTurings() - turingsToBuy);
        currentPlayer.setNumberOfTerritories(currentPlayer.getNumberOfTerritories()+1);
        ter.setType(currentPlayer.getType());
        board.getBoardGame()[row][col].setType(currentPlayer.getType());
        buyArmy=true;
        return true;
    }

    private void addUnitToTerritory(GameTerritory ter ,ArrayList<UnitToBuy> listUnit )
    {
        for(int i=0;i<listUnit.size();i++) {
            ArmyUnit unit = searchUnitInList(listUnit.get(i).getType());
            for(int j=0;j<listUnit.get(i).getAmount();j++)
            {
                ter.getUnitInTerritory().add(new ArmyUnit(unit.getPurchase(),unit.getMaxFirePower(),
                        unit.getCompetenceReduction(),unit.getRank(),unit.getType()));
                ter.setCurrentPower((ter.getCurrentPower() + unit.getMaxFirePower()));
            }

            infoArmy.get(unit.getRank()-1).setCounterUnits(unit.getCounterUnits() + listUnit.get(i).getAmount());
        }
    }

    public ArmyUnit searchUnitInList(String type)
    {
        for(int i=0;i<infoArmy.size();i++)
        {
            if(type.equals(infoArmy.get(i).getType()))
                return infoArmy.get(i);
        }
        return null;
    }

    public boolean managePredictedAttack(GameTerritory currentTer, ArrayList<UnitToBuy> list, int row, int col, ArrayList<UnitToBuy> listOfUnitInTerritory)
    {
        boolean attackSucc = false;
        GamePlayer playerOfTer=getPlayer(currentTer.getType());
        if(!checkIfPlayerHaveTuringsToBuy(getCurrentPlayer(),list))
        {
            return false;
        }
        insertUnitOfTerritoryToList(currentTer,listOfUnitInTerritory);
        ArrayList<UnitToBuy> tempListAttack=new ArrayList<>();
        ArrayList<UnitToBuy> tempListTer=new ArrayList<>();
        int amountUnit;
        int amountCurrent;
        updateCounterOfUnit(currentTer);

        for(int i=0;i<infoArmy.size();i++)
        {
            amountUnit = calculateAmountOfUnitInTer(currentTer,infoArmy.get(i).getType());
            tempListTer.add(new UnitToBuy(infoArmy.get(i).getType(),amountUnit));
            amountUnit = getAmountOfUnit(infoArmy.get(i).getType(),list);
            tempListAttack.add(new UnitToBuy(infoArmy.get(i).getType(),amountUnit));
        }

        for(int i=0;i<infoArmy.size();i++)
        {
            amountCurrent=Math.max(tempListAttack.get(i).getAmount(),tempListTer.get(i).getAmount())-Math.min(tempListAttack.get(i).getAmount(),tempListTer.get(i).getAmount());
            if(tempListAttack.get(i).getAmount()>tempListTer.get(i).getAmount())
            {
                tempListAttack.get(i).setAmount(amountCurrent);
                tempListTer.get(i).setAmount(0);
            }

            else
            {
                tempListTer.get(i).setAmount(amountCurrent);
                tempListAttack.get(i).setAmount(0);
            }
        }
        if(checkIfEmptyList(tempListAttack) && checkIfEmptyList(tempListTer))
        {
            playerOfTer.setNumberOfTerritories(playerOfTer.getNumberOfTerritories() -1);
            board.getBoardGame()[row][col].setType(GameTerritory.TypeOfTerritory.N);
            manageMakeTerritoryNeutral(currentTer);
            attackSucc = false;
        }

        else if(findMaxRank(tempListAttack)>findMaxRank(tempListTer)) // attack succeed
        {
            playerOfTer.setNumberOfTerritories(playerOfTer.getNumberOfTerritories() -1);
           attackSucc = updateUnitTerritories(currentTer, tempListAttack, row , col);
        }

        else  // attack fail - calculate new amount of units in territory
        {
            attackSucc = false;
            if(checkIfNeedToBecomeNeutralTerritory(tempListTer,currentTer))
            {
                playerOfTer.setNumberOfTerritories(playerOfTer.getNumberOfTerritories() -1);
                board.getBoardGame()[row][col].setType(GameTerritory.TypeOfTerritory.N);
                manageMakeTerritoryNeutral(currentTer);
            }
           else
            {
                updateNewArmyInTer(currentTer,tempListTer);
            }

        }

        return attackSucc;
    }

    private boolean checkIfNeedToBecomeNeutralTerritory(ArrayList<UnitToBuy> list, GameTerritory ter)
    {
        ArmyUnit unit;
        int power=0;
        for(int i=0;i<list.size();i++)
        {
            unit = searchUnitInList(list.get(i).getType());
            power+= unit.getMaxFirePower() * list.get(i).getAmount();

        }

        if ( power < ter.getArmyThreshold()) // need to become neutral
            return true;
        return false;
    }

     private void updateNewArmyInTer(GameTerritory currentTer, ArrayList<UnitToBuy> list)
    {
        currentTer.setUnitInTeritory(new ArrayList<ArmyUnit>());
        currentTer.setCurrentPower(0);
        ArmyUnit unit;
        for(int i=0;i<list.size();i++)
        {
            unit = searchUnitInList(list.get(i).getType());
            unit.setCounterUnits(unit.getCounterUnits()+list.get(i).getAmount());
            for(int j=0;j<list.get(i).getAmount();j++)
            {
                ArmyUnit newUnit = new ArmyUnit(unit.getPurchase(),unit.getMaxFirePower(),unit.getCompetenceReduction()
                        ,unit.getRank(),unit.getType());
                currentTer.getUnitInTerritory().add(newUnit);
                currentTer.setCurrentPower(currentTer.getCurrentPower() +newUnit.getCurrentPower() );
            }
        }
    }

    public GamePlayer getPlayer(GameTerritory.TypeOfTerritory type)
    {
        for(int i=0;i<players.size();i++)
        {
            if(players.get(i).getType().equals(type))
            {
                return players.get(i);
            }
        }
        return null;
    }

    private boolean checkIfEmptyList(ArrayList<UnitToBuy> list)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getAmount() !=0)
                return false;
        }
        return true;
    }

    private int getAmountOfUnit(String type, ArrayList<UnitToBuy> list)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getType().equals(type))
            {
                return list.get(i).getAmount();
            }
        }
        return 0;
    }

    private void updateCounterOfUnit(GameTerritory ter)
    {
        ArmyUnit unit;
        for(int i=0;i<ter.getUnitInTerritory().size();i++)
        {
            unit = searchUnitInList(ter.getUnitInTerritory().get(i).getType());
            unit.setCounterUnits(unit.getCounterUnits() - 1);
        }
    }

    private boolean updateUnitTerritories(GameTerritory currentTer, ArrayList<UnitToBuy> listCurrentUnits, int row , int col)
    {
        if(checkIfNeedToBecomeNeutralTerritory(listCurrentUnits,currentTer)){
            board.getBoardGame()[row][col].setType(GameTerritory.TypeOfTerritory.N);
            manageMakeTerritoryNeutral(currentTer);
            return false;
        }

        else {
            getCurrentPlayer().setNumberOfTerritories(getCurrentPlayer().getNumberOfTerritories() + 1);
            currentTer.setType(getCurrentPlayer().getType());
            updateNewArmyInTer(currentTer, listCurrentUnits);
        }
        return true;

    }

    private int calculateAmountOfUnitInTer(GameTerritory currentTer, String typeUnit)
    {
        int counterUnit=0;
        for(int i=0;i<currentTer.getUnitInTerritory().size();i++)
        {
            String type = currentTer.getUnitInTerritory().get(i).getType();
            if(type.equals(typeUnit))
            {
                counterUnit++;
            }
        }

        return counterUnit;
    }

    private int findMaxRank(ArrayList<UnitToBuy> units)
    {
        int maxRank=0;
        for(int i=0;i<units.size();i++)
        {
            if(units.get(i).getAmount() == 0)
                continue;

            String unit=units.get(i).getType();
            ArmyUnit u=searchUnitInList(unit);
            if(u.getRank()>maxRank)
            {
                maxRank = u.getRank();
            }
        }

        return maxRank;
    }

    public boolean checkIfPlayerHaveTuringsToBuy(GamePlayer player , ArrayList<UnitToBuy> list)
    {
        int turingsToBuy=0;
        for(int i=0;i<list.size();i++)
        {
            ArmyUnit unit = searchUnitInList(list.get(i).getType());
            if(unit != null)
            {
                turingsToBuy += list.get(i).getAmount() * unit.getPurchase();
            }
        }
        if(turingsToBuy > getCurrentPlayer().getTurings())
        {
            haveMoneyToBuy=false;
            return false;

        }
        else
        {
            player.setTurings(player.getTurings() - turingsToBuy);
            haveMoneyToBuy=true;
        }

        return true;
    }

    public String createStringWithTuringsPlayer()
    {
        String res = "Amount Turings:\n";
         for(int i=0;i<players.size();i++)
        {
            res+=players.get(i).getName()+","+players.get(i).getType()+":"+players.get(i).getTurings()+" turings\n";
        }
         return res;
    }

}

