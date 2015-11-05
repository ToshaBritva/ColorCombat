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

                    //Передвигаем игрока
                    player.setAbsolutePosition({x: getPlayerXCanvas(obj.j), y: getPlayerYCanvas(obj.i)});

                    //Изменяем его очки
                    var currentPlayerIndex = findIndexByKeyValue(players, obj.number)
                    players[currentPlayerIndex].score = obj.score;
                }

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
function gameOver(winer) {
    var t = "Игра окончена!!!\nПобедил " + winer.nickname + " со счетом " + winer.score;
    ShowMSG(t);
    //$("#timer").html(t);
}

//Начинаем игру
function  startGame() {
    clearField();
    websocket.send("{\"target\":\"startGame\"}");
}

//Устанавливаем время
function  setTime(t) {
    $('.timerDiv').html(t);
        
    $("#timer").html(t);
}

//Очистка поля (красит все клетки в белый цвет)
function clearField() {
    for (var i = 0; i < cellsLayer.children.length; i++)
        cellsLayer.children[i].fill(cellsColors[0]);
}