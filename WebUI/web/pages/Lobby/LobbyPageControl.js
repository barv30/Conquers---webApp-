
window.onload = function ()
{
    getLoginStatusFromServer();
    refreshUserList();
    refreshGamesList();
    setInterval(refreshUserList, 2000);
    setInterval(refreshGamesList, 2000);
    setInterval(getLoginStatusFromServer, 2000);
};


/*window.onunload = function ()
{
    $.ajax
    ({
        url: 'logout',
        type: 'GET',
    });
};*/

function getUserName() {
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
    return result.m_Name;
}


function getLoginStatusFromServer() {
    $.ajax
    ({
        url: 'lobbyUserInfo',
        type: 'GET',
        success: showUserDetailsCallback
    });
}

function showUserDetailsCallback(i_UserJson) {
    $('.userNameSpan').text("Hello " + i_UserJson.m_Name);


}
function refreshUserList() {
    $.ajax(
        {
            url: 'lobbyUserList',
            type: 'GET',
            success: refreshUserListCallback
        }
    );
}

function refreshUserListCallback(userListJson) {
    var usersTable = $('.usersTable tbody');
    usersTable.empty();

    userListJson.forEach(function (user) {
        var tr = $(document.createElement('tr'));

        var td = $(document.createElement('td')).text(user.m_Name);

        td.appendTo(tr);

        tr.appendTo(usersTable);

    });
}



function refreshGamesList() {
    $.ajax
    (
        {
            url: 'gameslist',
            type: 'GET',
            success: refreshGamesListCallback
        }
    )
}




function removeTerritoriesDialog() {

    $('.dialogDiv')[0].style.display = "none";
}





function unitDataClicked(event)
{
    var nameOfGame = event.target.id;
    $.ajax
    (
        {
            url: 'unitData',
            type: 'GET',
            data: {
                name: nameOfGame
            },
            success: unitDataClickedCallBack
        }
    );
}


function unitDataClickedCallBack(json) {

    var div = $('.dialogDiv')[0];
    div.style.display = "block";
    var game = json;
    var spaces=0;
    var elementB;
    game.gameEngine.infoArmy.forEach(function(element) {
        if(element.type.length>=spaces)
        {
            spaces=element.type.length;
            elementB=element;
        }

    });
    var result="Type"
    for(var i=0;i<spaces;i++)
    {
        result=result+" ";
    }

    result=result+"Purchase     Max fire\n";


    game.gameEngine.infoArmy.forEach(function(element) {
        result = result +element.type;
        for(var i=0;i<spaces;i++)
        {
            result=result+" ";
        }

        if(element!=elementB)
        {
            for(var i=0;i<(spaces-element.type.length);i++)
            {
                result=result+" ";
            }
        }

        result=result+element.purchase+"             "+element.maxFirePower+"\n";
    });

    $('.stringBoard').text(result);


}

function joinGameClicked(event)
{
    var nameOfGame = event.target.id;
    $.ajax
    (
        {
            url: 'joinGame',
            type: 'GET',
            data: {
                name: nameOfGame
            },
            success: joinGameClickedCallback
        }
    );
}
function refreshGamesListCallback(json) {
    var gamesTable = $('.gamesTable tbody');
    gamesTable.empty();
    var gamesList = json;

    gamesList.forEach(function (game) {
        var tr = $(document.createElement('tr'));
        var tdGameName = $(document.createElement('td')).text(game.m_GameTitle);
        var tdCreatorName = $(document.createElement('td')).text(game.m_CreatorOfGame);
        var tdBoardSize = $(document.createElement('td')).text(game.m_BoardGameRows + " X " + game.m_BoardGameCols);
        var tdDataTerrirtoy = $(document.createElement('td')).text(game.m_Target);
        var tdUnitType = $(document.createElement('td')).text(game.m_GameVariant);
        var tdPlayerNumber = $(document.createElement('td')).text(game.m_NumberOfRegisteredPlayers + " / " + game.m_NumberOfPlayersNeededToStartGame);
        var tdGameStatus = $(document.createElement('td')).text(game.m_GameStatus);
        tdGameName.appendTo(tr);
        tdCreatorName.appendTo(tr);
        tdBoardSize.appendTo(tr);
        tdDataTerrirtoy.appendTo(tr);
        var TerritoriesButton = $(document.createElement("button")).text("Territories Data");
        TerritoriesButton[0]["id"]=game.m_GameTitle;
        TerritoriesButton[0].addEventListener("click",territoriesDataClicked);
        TerritoriesButton.appendTo(tdDataTerrirtoy);
        tdUnitType.appendTo(tr);
        var unitDataButton = $(document.createElement("button")).text("Units Data");
        unitDataButton[0]["id"]=game.m_GameTitle;
        unitDataButton[0].addEventListener("click",unitDataClicked);
        unitDataButton.appendTo(tdUnitType);
        tdPlayerNumber.appendTo(tr);
        tdGameStatus.appendTo(tr);


        var myJoinButton = $(document.createElement("button")).text("Join Game");
        myJoinButton[0]["id"]=game.m_GameTitle;
        myJoinButton[0].addEventListener("click",joinGameClicked);
        myJoinButton.appendTo(tr);

        if(tdGameStatus[0].innerHTML == 'Active' || tdGameStatus[0].innerHTML=='Finished')
        {
            myJoinButton[0].disabled=true
        }
        /*
        if(game.m_NumberOfRegisteredPlayers === game.m_NumberOfPlayersNeededToStartGame)
        {
            myJoinButton[0].disabled=true
        }
        */
        tr.appendTo(gamesTable);

    });
}


function territoriesDataClicked(event) {

    var nameOfGame = event.target.id;
    $.ajax
    (
        {
            url: 'unitData',
            type: 'GET',
            data: {
                name: nameOfGame
            },
            success: territoriesDataClickedCallBack
        }
    );
}

function territoriesDataClickedCallBack(json) {
    var div = $('.dialogDiv')[0];
    div.style.display = "block";
    var game = json;
    var rows = game.m_BoardGameRows;
    var cols = game.m_BoardGameCols;

    var temp;
    var result ='Board:'+
        "\n";
    for (var j = 0; j < rows ; j++)
    {

        for (var k = 0; k < cols ; k++) {
            result=result+"---------";
        }

        result=result+"-"+"\n";

        //if(j+1<10)
        //  result=result+" "+(j + 1).toString();
        // else
        //   result=result+(j + 1).toString();

        for (var l = 0; l < cols  ; l++) {
            //GameTerritory cell = game.getBoard().getBoardGame()[j][l];

            if ((cols  * j + l) < 10 )
            {
                result=result+ "|00" +game.gameEngine.board.boardGame[j][l].id+"  ";
            }
            else if((cols  * j + l) >= 10 && (cols  * j + l) < 100)
                result=result+ "|0" + game.gameEngine.board.boardGame[j][l].id + "  ";
            else
                result=result+ "|" + game.gameEngine.board.boardGame[j][l].id + "  ";
        }

        result=result+ "| \n";

        for (var t = 0; t <cols  ; t++) {
            //GameTerritory cell = game.getBoard().getBoardGame()[j][t];
            var numberOfDigit=game.gameEngine.board.boardGame[j][t].profit.toString().length;
            result=result+"|"+game.gameEngine.board.boardGame[j][t].profit;
            for(var i=0;i<6-numberOfDigit;i++)
            {
                result=result+" ";
            }
            if(game.gameEngine.board.boardGame[j][t].profit==0)
            {
                result=result+" ";
            }
        }

        result=result+"| \n";

        for (var t = 0; t <cols  ; t++) {

            //GameTerritory cell = game.getBoard().getBoardGame()[j][t];
            var numberOfDigit=game.gameEngine.board.boardGame[j][t].armyThreshold.toString().length;
            result=result+ "|"+game.gameEngine.board.boardGame[j][t].armyThreshold;
            for(var i=0;i<6-numberOfDigit;i++)
            {
                result=result+" ";
            }
            if(game.gameEngine.board.boardGame[j][t].armyThreshold==0)
            {
                result=result+" ";
            }
        }

        result=result+"| \n";
    }

    for (var k = 0; k < cols ; k++) {
        result=result+"---------";
    }

    result=result+"-";

    $('.stringBoard').text(result);

}


function clearFileInput() {
    document.getElementById("fileInput").value = "";
}


var wasFileSelected;
var loadGameFile;

function loadGameClicked(event) {
    loadGameFile = event.target.files[0];
    wasFileSelected = true;
}

function UploadGameClicked(event){
    if(wasFileSelected===true) {
            var reader = new FileReader();
            var creatorName = getUserName();

            reader.onload = function () {
                var content = reader.result;
                $.ajax(
                    {
                        url: 'loadgame',
                        data: {
                            file: content,
                            creator: creatorName
                        },
                        type: 'POST',
                        success: UploadGameCallBack
                    }
                );
            };
            reader.readAsText(loadGameFile);

        }
    else{
        alert('No File Selected!');
    }
    wasFileSelected=false;
    loadGameFile=undefined;
}

function UploadGameCallBack(json) {
    if (json==="Success") {
        alert("Load game Success !!");
        refreshGamesList();
        clearFileInput();
    }
    else {
        clearFileInput();
        alert(json);
    }
}

function joinGameClickedCallback(requestJson) {
    if (requestJson.m_HasRequestSucceeded == true) {
        window.location.href = requestJson.m_RedirectURL;
    } else {
        alert(requestJson.m_Message);
    }
}

