/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var cellsNumbers = [0, 5, 6, 7, 8]; //Цифры соответсвующие клеткам
var playersNumbers = [1, 2, 3, 4]; //Цифры соответсвующие игрокам
var playersColors = ["red", "aqua", "#FFFF00", "lime"]; //Цвета игроков
var cellsColors = ["white", "#DB4D4D", "#33CCCC", "orange", "#00B800"]; //Цвета клеток
var players = new Array(); //Игроки с их очками 

var cellsCount = 10;

//Инициализируем канвас
var field = new Konva.Stage({
    container: 'gameField', // индификатор div контейнера
    width: 400,
    height: 400
});

cellWidth = field.height() / 10; //ширина=высота клетки
cellBorderWidth = 3; // ширина границы клетки

// далее создаём слой для ячеек и игроков
var cellsLayer = new Konva.Layer();
var playersLayer = new Konva.Layer();
var bonusLayer = new Konva.Layer();

//Отрисовываем клетки
for (var i = 0; i < field.height(); i = i + cellWidth)
    for (var j = 0; j < field.width(); j = j + cellWidth) {
        var rect = new Konva.Rect({
            x: i,
            y: j,
            width: cellWidth,
            height: cellWidth,
            fill: 'white',
            stroke: 'black',
            id: i.toString() + "_" + j.toString(),
            strokeWidth: cellBorderWidth
        });
        cellsLayer.add(rect);
    }
field.add(cellsLayer);
field.add(playersLayer);
field.add(bonusLayer);


//******************************ФУНКЦИИ**************************************
//*************************************************************************
function drawChanges(changes) {

    var json = JSON.parse(changes);

    json.forEach(function draw(obj) {
        //Если это клетка, то перекрашиваем ее
        if (isCell(obj.number)) {
            var cell = cellsLayer.findOne('#' + getCellId(obj.j, obj.i));
            cell.fill(cellsColors[obj.number - 4]);
        } else {
            //Если это игрок
            if (isPlayer(obj.number)) {

                //Пытаемся получить фигуру соответсвующую игроку
                var player = playersLayer.findOne('#player' + obj.number.toString());

                //Если ее нет
                if (!player) {

                    // Добавляем нового игрока
                    player = addNewPlayer(obj.i, obj.j, obj.number)
                    players.push(obj);

                } else {

                    player.fill(playersColors[playersNumbers.indexOf(obj.paintingNumber)])
                    //Передвигаем игрока
                    player.setAbsolutePosition({x: getPlayerXCanvas(obj.j), y: getPlayerYCanvas(obj.i)});

                    //Изменяем его очки
                    var currentPlayerIndex = findIndexByKeyValue(players, obj.number)
                    players[currentPlayerIndex].score = obj.score;
                }

            } else {
                spawnBonus(obj);
            }
        }

    });
    fillScoreTable();
    cellsLayer.draw();
    playersLayer.draw();

}

function fillScoreTable() {

    //Сортируем игроков по убыванию
    players.sort(function (a, b) {
        return b.score - a.score;
    });

    //Очищаем таблицу
    var scoreTable = $("#ScoreTable");
    $("#ScoreTable tr").remove();

    //Заполняем заново
    for (var i = 0; i < players.length; i++) {
        var tr = $('<tr bgcolor = \'' + playersColors[players[i].number - 1] + '\'>').appendTo(scoreTable);
        tr.append('<th id=\'' + players[i].nickname + '\'> ' + players[i].nickname + ' </th>');
        tr.append('<th id=\'' + players[i].nickname + 'Score\'> ' + players[i].score + ' </th>');
    }
}

//Добавление нового игрока
function addNewPlayer(i, j, id) {
    var circle = new Konva.Circle({
        x: getPlayerXCanvas(j),
        y: getPlayerXCanvas(i),
        radius: cellWidth / 2 - 3,
        fill: playersColors[playersNumbers.indexOf(id)],
        stroke: 'black',
        strokeWidth: 2,
        id: "player" + id
    });
    playersLayer.add(circle);
    return circle;
}


//Ищем индекс элемента в массиве с указаным number
function findIndexByKeyValue(array, number)
{
    for (var i = 0; i < array.length; i++) {
        if (array[i].number == number) {
            return i;
        }
    }
    return null;
}

//Возвращает координаты игрока на канвасе
function getPlayerXCanvas(playerYMatrix) {
    return playerYMatrix * cellWidth + cellWidth / 2;
}
function getPlayerYCanvas(playerXMatrix) {
    return playerXMatrix * cellWidth + cellWidth / 2;
}

function getLeftCellCornerX(cellYMatrix) {
    return cellYMatrix * cellWidth + cellBorderWidth / 2;
}

function getLeftCellCornerY(cellXMatrix) {
    return cellXMatrix * cellWidth + cellBorderWidth / 2;
}

//Функция возвращает true/false если указанное цифра соответсвует клетке
function isCell(value) {
    return cellsNumbers.indexOf(value) > -1;
}


//Функция возвращает true/false если указанное цифра соответствует игроку
function isPlayer(value) {
    return playersNumbers.indexOf(value) > -1;
}


//Обрабатывает нажатие клавиш на клавиаутре
$(document).keydown(function (event) {
    switch (event.keyCode) {
        case 38: //Вверх
        case 87: //W
            websocket.send("{\"target\":\"movePlayer\",\"value\":\"up\"}");
            break;
        case 40: //Вниз
        case 83: //S
            websocket.send("{\"target\":\"movePlayer\",\"value\":\"down\"}");
            break;
        case 37: //Лево
        case 65: //A
            websocket.send("{\"target\":\"movePlayer\",\"value\":\"left\"}");
            break;
        case 39: //Право
        case 68: //D
            websocket.send("{\"target\":\"movePlayer\",\"value\":\"right\"}");
            break;

    }
});

//Возвращает idшник прямоугольника на канвасе в формате X_Y
function getCellId(i, j) {
    var shapeX = i * cellWidth;
    var shapeY = j * cellWidth;
    return shapeX.toString() + '_' + shapeY.toString();
}

//Игра закончена
function gameOver(winner) {
    var player = JSON.parse(winner);
    var t = "Игра окончена!!!\nПобедил " + player.nickname + " со счетом " + player.score;
    ShowEndMSG(t);
}

//Устанавливаем время
function setTime(t) {
    $('.timerDiv').html(JSON.parse(t));
    $("#timer").html(JSON.parse(t));
}

function spawnBonus(bonus) {
    var cell = cellsLayer.findOne('#' + getCellId(bonus.j, bonus.i));
    cell.fill(cellsColors[0]);
    cellsLayer.draw();

    var imageObj = new Image();
    imageObj.onload = function () {
        var bonusImg = new Konva.Image({
            x: getLeftCellCornerX(bonus.j),
            y: getLeftCellCornerY(bonus.i),
            image: imageObj,
            width: cellWidth - cellBorderWidth,
            height: cellWidth - cellBorderWidth * 1.25,
            id: bonus.name + "_" + bonus.i + "_" + bonus.j
        });
        bonusLayer.add(bonusImg);
        bonusImg.draw();
    };

    switch (bonus.number) {
        case 10:
            imageObj.src = '../Images/Game/Bonus/cross.png';
            break;
        case 11:
            imageObj.src = '../Images/Game/Bonus/speedUp.png';
            break;
        case 12:
            imageObj.src = '../Images/Game/Bonus/freeze.png';
            break;
        case 13:
            imageObj.src = '../Images/Game/Bonus/reversePainting.png';
            break;
    }
}

//Убирает бонусы
function removeBonus(bonusJSON) {

    var bonus = JSON.parse(bonusJSON);

    var bonusObj = bonusLayer.findOne('#' + bonus.name + "_" + bonus.i + "_" + bonus.j);

    bonusObj.remove();

    bonusLayer.draw();

}

//Изменяет статус игры
function changeStatus(statusJSON) {
    $("#Status h3").text(JSON.parse(statusJSON));
    if (JSON.parse(statusJSON) == "В процессе") {
        ShowMSG("FIGHT", 1000);
    }
}

function countdown(seconds) {
    ShowMSG(JSON.parse(seconds), 600);
}