
var status;
var userName;
var userColor;
var currentPlayerColor;
var turn = 0;
var intervalTimer = 2000;
var isMyTurn = false;
var showScoreBoard;
var gameStarted = 0;
var gameTitle;
var firstMove=0;
var playTurnClick = 0;
var typeOfAttack=0;
window.onload = function()
{
    checkLoginStatus();
    var gameName = gameTitle;
    $.ajax
    ({
        url: 'gameControllerInfo',
        type: 'GET',
        data:gameName,
        success: createBoard
    });

    setInterval(updatePlayersDetails, intervalTimer);
    setInterval(gameStatus, intervalTimer);
};

function checkLoginStatus() {
    $.ajax
    ({
        url: 'userGameLoginInfo',
        type: 'GET',
        success: statusCallback
    });
}

function initializePage(json) {
    userName = getUser().m_Name;
    userColor=getUser().color;
    showScoreBoard = true;
    isMyTurn = false;
    status = 'inactive';
    loadWindowDetails(json);



}

function statusCallback(requestJson)
{
    if(requestJson.m_HasRequestSucceeded==false) {
        window.location.href = requestJson.m_RedirectURL;
    }
    else{
        $.ajax
        (
            {
                url: 'gameControllerInfo',
                type: 'GET',
                success: initializePage
            }
        )
    }
}

function gameStatus()
{
    var gameName = gameTitle;
    $.ajax
    (
        {
            async: false,
            url: 'gameControllerInfo',
            data:gameName,
            type: 'GET',
            success: handleStatus
        }
    )
}

function handleStatus(json) {

    newStatus = json.m_GameStatus;
    $('.currentMove').text(json.gameEngine.counterOfRounds);

    var colorPlayer = json.gameEngine.typeCurrentPlayer;
    //playerTurn = colorPlayer;

    switch (newStatus) {
        case 'Inactive':
            if (gameStarted == 1){
                gameStarted =0;
            }
            break;
        case 'Active': {
            if (gameStarted == 0) {
                gameStarted = 1;
            }
            $('.currentPlayerName')[0].innerHTML = colorPlayer;
            if (userColor === "None") {

            userColor = getUser().color;
            $('.ColorSpan').text(userColor);
            }

            currentPlayerColor = colorPlayer;
            break;
            }

        case "Finished":
            isMyTurn = false;
            if (showScoreBoard) {
                showWinnerDialog();
                //showEndGameDiaglog();
                showScoreBoard = false;
            }
            status = newStatus;
            break;
    }

    status = newStatus;

    $('.gameStatus').text('Game status: ' + status);
    if(status == "Active") {
        $('.gameStatus').css('color', 'red');
    }
}

function showWinnerDialog()
{
    $.ajax
    (
        {
            url: 'winnerServlet',
            type: 'GET',
            data:gameTitle,
            success: function(jsonWinner){showWinnerDialogCallBack(jsonWinner);}
        }
    )
}

function showWinnerDialogCallBack(jsonWinner) {
    var div = $('.winnerDialog')[0];
    div.style.display = "block";
    var messageWinner = "END GAME:\n";
    if(jsonWinner.toString() === "N") {
        messageWinner += "There is a TIE !\n";
    }
    else
    {
        messageWinner += "The winner is player - " + jsonWinner.toString()+" !!!\n";
    }


    $('.header').text(messageWinner);

}

function resetGame()
{
    var div = $('.dialogDiv')[0];
    div.style.display = "none";
    var gameName = gameTitle;
    //logout the users from that game
    window.location = "/Conquers/pages/Lobby/LobbyPage.html";
    $.ajax
    (
        {
            url: 'resetGame',
            type: 'GET',
            data: gameName,
            success: init
        }
    )
//init game

}

function init () {
console.log("hhhh");
}
function updatePlayersDetails()
{
    $.ajax
    (
        {
            url: 'gamePlayers',
            type: 'GET',
            success: updatePlayersDetailsCallback
        }
    )
}

function updatePlayersDetailsCallback(json) {
    $('.registeredPlayers').text(json.length);

    var playersNamesDiv = $('.playersNamesBody');
    var playersScoreDiv = $('.playersScoreBody');
    var playersTypeDiv = $('.playersTypesBody');

    playersNamesDiv.empty();
    playersTypeDiv.empty();
    playersScoreDiv.empty();
    for (i = 0; i < json.length; i++) {
        var playerContainerDiv = $(document.createElement('div'));
        playerContainerDiv.addClass('playerContainerDiv');
        playerContainerDiv.appendTo(playersNamesDiv);

        var playerDiv = $(document.createElement('div'));
        playerDiv.addClass('playerDiv');
        playerDiv.appendTo(playerContainerDiv);

        var scoreDiv = $(document.createElement('div'));
        scoreDiv.addClass('scoreDiv');
        scoreDiv.appendTo(playersScoreDiv);

        var typeDiv = $(document.createElement('div'));
        typeDiv.addClass('typeDiv');
        typeDiv.appendTo(playersTypeDiv);
    }

    var playerDivs = $('.playerDiv');
    var scoreDivs = $('.scoreDiv');
    var typeDivs = $('.typeDiv');
    for (i = 0; i < json.length; i++) {
        playerDivs[i].innerHTML = json[i].name;
        scoreDivs[i].innerHTML = json[i].turings;
        typeDivs[i].innerHTML = json[i].type;
        if (json[i].name === userName)
            $('.scoreSpan').text(json[i].turings);
    }
}

function loadWindowDetails(json)
{
    $('.userNameSpan').text('Hello, '+ userName + ", enjoy playing.");
    loadGameDetailsCallback(json);
}



function loadGameDetailsCallback(json)
{
    var creatorName = json.m_CreatorOfGame;
    var gameName = json.m_GameTitle;
    gameTitle = json.m_GameTitle;
    var boardSize = json.m_BoardGameRows + " X " + json.m_BoardGameCols;
    $('.creatorName').text("Game Creator: " + creatorName + ".");
    $('.gameName').text("Game Title: " + gameName);
    $('.boardSize').text("Board size: " + boardSize);
    $('.registeredPlayers').text(json.m_NumberOfRegisteredPlayers);
    $('.requiredPlayers').text(json.m_NumberOfPlayersNeededToStartGame);
   $('.totalMoves').text(json.m_NumberOfRounds);
    $('.currentMove').text(json.m_currentRound);
    $('.ColorSpan').text(userColor);

}

function createBoard(json)
{
    var board = $('.boardBody');
    board.contents().remove();
    var boardConquers = new BoardConquers(board,json);
}

function BoardConquers (selector,json) {

        this.ROWS = json.m_BoardGameRows;
        this.COLS = json.m_BoardGameCols;
        this.player = 'red';
        this.selector = selector;
        createGrid(this,json);
        /*this.setupEventListeners();*/
    }


function createGrid(boardConquers,json) {
    var $board = (boardConquers.selector);
    $board.empty();
    this.isGameOver = false;
    this.player = 'red';
    for (var row = 0; row < boardConquers.ROWS; row++) {
        var $row = $('<div>')
            .addClass('row');
        for (var col = 0; col < boardConquers.COLS; col++) {
            var $col = $('<button>')
                .addClass('col empty')
                .text(json.gameEngine.board.boardGame[row][col].id+'\n'+json.gameEngine.board.boardGame[row][col].profit+'\n'+json.gameEngine.board.boardGame[row][col].armyThreshold)
                .attr('data-col', col)
                .attr('data-row', row)
                .mousedown(function (event) {
                    var c = $(this).attr('data-col');
                    var r = $(this).attr('data-row');
                    switch (event.which) {
                        case 1:
                            onSquareClick(r,c);
                            break;
                        case 3:
                            onEnterTerritory(r,c);
                            break;
                    }});

            $row.append($col);

        }

        $board.append($row);
    }
    setInterval(printBoard,intervalTimer);
}
function  onLeaveTerritory() {
    console.log("shraga");
   // $('.dialogDiv1')[0].style.display = "none";
   // $('.dialogDiv1').find('.dialogContent').find('.details').find('.header').empty();
   // $('.dialogDiv1').find('.dialogContent').find('.details').find('.units').empty();
}

function onEnterTerritory(r,c)
{
    console.log("noam");
    var game = getGameEngine();
    var gameTerritory = game.board.boardGame[r][c];
    var user = getUser();
    if (gameTerritory.type === user.color) {

        var gameName = gameTitle;
        $.ajax
        (
            {
                url: 'ArmyInTerritory',
                type: 'GET',
                data: {
                    data: gameName,
                    r: r,
                    c: c
                },
                success: showDataTerritoryCallBack

            }
        )
    }
}

function showDataTerritoryCallBack(json)
{
    //var div = $('.dialogDiv1')[0];
   // div.style.display = "block";

  // $('.header').text(json);
   alert (json.toString());

}

function printBoard() {

    var gameName = gameTitle;

    $.ajax({
        url: 'gameControllerInfo',
        data:gameName,
        type: 'GET',
        success: updateBoard
    });
}

function updateBoard(json)
{
    for (var row = 0; row <  json.m_BoardGameRows; row++) {

        for (var col = 0; col < json.m_BoardGameCols; col++) {
            {
                if (json.gameEngine.board.boardGame[row][col].type != 'N') {
                    var color = json.gameEngine.board.boardGame[row][col].type.toString().toLocaleLowerCase();
                    $(`.col[data-row='${row}'][data-col='${col}']`).css("backgroundColor", color);
                }
                else
                {
                    $(`.col[data-row='${row}'][data-col='${col}']`).css("backgroundColor", 'white');
                }
            }
        }
    }
}

function checkProximity(r,c)
{
    var result = true;
    $.ajax
    ({
        async: false,
        url: 'checkProximity',
        type: 'POST',
        data:{
            r:r,
            c:c
        },

        success: function(jsonText){
            if(jsonText.m_Message === "invalid_choice")
            {
                result = false;
            }
            else
            {
                result =true;
            }
        }
    });
    return result;
}

function onPlayTurnClick() {

    if(gameStarted==1){
        if(userColor == currentPlayerColor && playTurnClick == 0) {

            playTurnClick = 1;
            typeOfAttack = 0;
            var gameName = gameTitle;
            

            $.ajax({
                url: 'UpdateDetailsBeforeTurn',
                data:gameName,
                type: 'GET',
                success: function(playerJson){playTurnCallBack(playerJson);}
            });
        }
        else
        {
            if(userColor != currentPlayerColor)
                alert("Hey! It's not your turn")
        }

    }
}

function playTurnCallBack(playerJson)
{

    $('.scoreSpan').text(playerJson.turings);
    printBoard();
}

function onSquareClick(r,c)
{
    if(gameStarted == 1) {

        if (userColor == currentPlayerColor) {

            if(playTurnClick === 1 && firstMove === 0) {
                firstMove =1 ;
                if (checkProximity(r, c) === true) {
                    var unitsToBuy = [];
                    var game = getGameEngine();
                    var gameTerritory = game.board.boardGame[r][c];

                    if (gameTerritory.type === 'N') {
                        console.log("enter1");

                        buyArmyShowDialog(game, gameTerritory, r, c);


                    }
                    else if (gameTerritory.type === currentPlayerColor) {
                        var div = $('.dialogDiv')[0];
                        div.style.display = "block";
                        var game = getGameEngine();

                        var temp;
                        var result = 'Choose your move: ' + '\n';

                        $('.header').text(result);
                        var $maintain = $('<button>')
                            .text("Maintain your army")
                            .click(function () {
                                onMaintainClick(gameTerritory, game, r, c);
                            });
                        var $addUnits = $('<button>')
                            .text("Add Units")
                            .click(function () {
                                onAddUnitsClick(gameTerritory, game, r, c);
                            });
                        $('.units').append($maintain);
                        $('.units').append($addUnits);

                    }
                    else {
                        var div = $('.dialogDiv')[0];
                        div.style.display = "block";
                        var game = getGameEngine();

                        var temp;
                        var result = 'Choose your Attack: ' + '\n';

                        $('.header').text(result);
                        var $maintain = $('<button>')
                            .text(" predictable Attack")
                            .click(function () {
                                typeOfAttack = 1;
                                manageAttack(gameTerritory, game, r, c );
                            });
                        var $addUnits = $('<button>')
                            .text("calculate Attack")
                            .click(function () {
                                typeOfAttack = 2;
                                manageAttack(gameTerritory, game, r, c);
                            });
                        $('.units').append($maintain);
                        $('.units').append($addUnits);
                    }
                }
                else
                {
                    firstMove =0;
                    alert("invalid choice! you need to choose another territory");
                }
            }

            else
            {
                if(firstMove === 1)
                {
                    alert("Your turn is over! press 'End Turn'");
                }
                else {
                    alert("You need to press on 'Play Turn' button!");
                }
            }
        }
        else {
                alert("It's not your turn!")
            }
        }

    else
    {
        alert("The game not start!")
    }
}

function manageAttack(gameTerritory, game, r, c)
{
    var div = $('.dialogDiv')[0];
    div.style.display = "none";
    $('.dialogDiv').find('.dialogContent').find('.details').find('.units').empty();
    buyArmyShowDialog(game,gameTerritory,r,c);
}

function buyArmyShowDialog(game,gameTerittory,r,c)
{

    var unitsToBuy = [];
    var div = $('.dialogDiv')[0];
    div.style.display = "block";

    var temp;
    var result = 'Number Of Unit To Buy';
    result=result+"\n";
    $('.header').text(result);

    for (var i = 0; i < game.numberOfTypesUnit; i++) {
        var $col = $('<input>')
            .attr('id', i);
        var temp= game.infoArmy[i].type;
        temp+="\n";
        $('.units').append(temp);
        $('.units').append($col);
        $('.units').append("\n");

    }

    var $done = $('<button>')
        .text("DONE")
        .click(function () {
            onDoneBuyingClick(gameTerittory, unitsToBuy, game, r, c);
        });

    $('.units').append($done);

}




function onMaintainClick(gameTerritory, game, r,c) {

    $('.dialogDiv')[0].style.display = "none";
    $('.dialogDiv').find('.dialogContent').find('.details').find('.units').empty();

    var gameName = gameTitle;
    $.ajax
    (
        {
            url: 'maintainArmy',
            type: 'GET',
            data: {
                data: gameName,
                r: r,
                c: c
            },
            success: function (responseJson) {
                maintainCallBack(responseJson);
            }
        }
    )
}

function maintainCallBack(responseJson)
{
    if(responseJson.m_Message === "maintain")
    {
        alert("The maintain successes")
    }
    else
    {
        alert("The maintain not successes! You don't have enough turings.")
    }
}

function onAddUnitsClick(gameTerritory, game, r,c){
    $('.dialogDiv')[0].style.display = "none";
    $('.dialogDiv').find('.dialogContent').find('.details').find('.units').empty();
    buyArmyShowDialog(game,gameTerritory,r,c);

}

function onDoneBuyingClick(gameTerritory, unitsToBuy,game,r,c) {

    var game = getGameEngine();
    for(var i=0;i<game.numberOfTypesUnit;i++)
    {
        if(document.getElementById(i).value=="")
            unitsToBuy.push("0")

        else
            if(isNaN(document.getElementById(i).value)) {
                alert("Must input number");
                return;
            }

            else
                unitsToBuy.push(document.getElementById(i).value)

    }

    $('.dialogDiv')[0].style.display = "none";
    $('.dialogDiv').find('.dialogContent').find('.details').find('.header').empty();
    $('.dialogDiv').find('.dialogContent').find('.details').find('.units').empty();
    var gameName = gameTitle;

    if ( typeOfAttack === 0) {
        $.ajax
        (
            {
                url: 'moneyToBuy',
                type: "POST",
                dataType: 'json',
                data: {
                    data: gameName,
                    json: unitsToBuy,
                },
                success: function (i_gameJson) {
                    haveMoneyToBuyCallback(gameTerritory, unitsToBuy, i_gameJson, r, c)
                }
            }
        )
    }
    else
    {$.ajax
        (
            {
                url: 'manageAttack',
                type: "POST",
                dataType: 'json',
                data: {
                    data: gameName,
                    r: r,
                    c: c,
                    typeOfAttack:typeOfAttack,
                    json: unitsToBuy,
                },
                success: function (json) {
                    attackCallBack(json)
                }
            }
        )
    }

}

function attackCallBack(json)
{
    var messageAttack;
    if(json.isAttackSuccess === true)
    {
        messageAttack = "The Attack Succeed!\n";
    }
    else
    {
        messageAttack = "The attack didn't succeed.\n"
    }

    messageAttack+="Enemy territory units details :\n" + json.unitsString;
    alert(messageAttack);
}

function haveMoneyToBuyCallback(gameTerritory,unitsToBuy,i_gameJson,r,c) {
    var gameName = gameTitle;
    if (i_gameJson.haveMoneyToBuy === true)
    {
        $.ajax
        (
            {
                url: 'buyArmy',
                type: 'GET',
                data: {
                    json: unitsToBuy,
                    data: gameName,
                    r:r,
                    c:c
                },
                success: function (GameJson) {
                    buyArmyCallback(gameTerritory, GameJson,r,c);
                }
            }
        )
    }
    else
        console.log("false");


}

function buyArmyCallback(gameTerritory,GameJson,r,c)
{
    if(GameJson.buyArmy==true)
    {
        alert("The purchase was successful! now the territory is yours!");
        var colorTer=currentPlayerColor.toString().toLocaleLowerCase();
        console.log(GameJson.board.boardGame[r][c]);
        $(`.col[data-row='${r}'][data-col='${c}']`).css("backgroundColor",colorTer);


    }
    else
    {
        alert("You Didn't Put Enough Army To This Territory!");
    }
}



function getUser() {
    var result;
    $.ajax
    ({
        async: false,
        url: 'lobbyUserInfo',
        type: 'GET',
        success: function(i_UserJson){
            result = i_UserJson;
        }
    });
    return result;
}

function getGameEngine()
{
    var result;
    $.ajax
    ({
        async: false,
        url: 'GameEngine',
        type: 'GET',
        data:gameTitle,
        success: function(i_GameJson){
            result = i_GameJson;
        }
    });
    return result;
}

function onLeaveGameClick()
{

    if (gameStarted == 1)  // if the game already started - retire
    {
        $.ajax
        ({
            async: false,
            url: 'retireGame',
            type: 'GET',
            success: function (json){retireCallBack(json);}
        });
    }
    else
    {
        $.ajax
        ({
            async: false,
            url: 'leaveGame',
            type: 'GET',
            success: function () {
                window.location = "/Conquers/pages/Lobby/LobbyPage.html";
            }
        });

    }
}

function retireCallBack(json)
{
    $('.currentPlayerName').text = json.gameEngine.typeCurrentPlayer;
    currentPlayerColor = json.gameEngine.typeCurrentPlayer;
    window.location = "/Conquers/pages/Lobby/LobbyPage.html";
    printBoard();
}



function onEndMoveClick() {

    if(gameStarted === 1 && playTurnClick === 1) {
        if(userColor == currentPlayerColor ) {

            turn = 0;
            var gameName = gameTitle;
            $.ajax
            (
                {
                    url: 'GameCurrentPlayerServlet',
                    data: {gameName: gameName},
                    type: 'GET',

                    success: function (json) {
                        changeTurn(json);
                    }

                }
            )
        }
        else
        {
            alert("Hey! It's not your turn")
        }
    }
    else
    {
        if(gameStarted === 1&&playTurnClick === 0)
        {
            if(userColor == currentPlayerColor ) {
                alert("You need to press on 'Play Turn' button!");
            }
            else
            {
                alert("Hey! It's not your turn")
            }
        }
    }

}

function changeTurn(json) {

   $('.currentPlayerName').text = json.color;
   $('.currentMove').text(json.currentRound);
    currentPlayerColor = json.color;
    playTurnClick = 0;
    firstMove = 0;
}






