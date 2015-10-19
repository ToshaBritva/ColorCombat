/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var cellsArr = [0, 5, 6, 7, 8];
var playersArr = [1, 2, 3, 4];
var playersColors = ["red", "aqua", "#FFFF00", "lime"];
var cellsColors = ["white", "#DB4D4D", "#33CCCC", "orange", "#00B800"];

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
initField();



//Функция инициализирует поле, добавляет клетки, игроков
function initField() {

    //рисуем квадраты
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
}

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

                //Если ее нет, то добавляем нового игрока
                if (!player) {
                    player = addNewPlayer(obj.i, obj.j, obj.number)
                } else {
                    player.setAbsolutePosition({x: getPlayerXCanvas(obj.j), y: getPlayerYCanvas(obj.i)});
                }
            }
        }

    });
    cellsLayer.draw();
    playersLayer.draw();
}

//Добавление нового игрока
function addNewPlayer(i, j, id) {
    var circle = new Konva.Circle({
        x: getPlayerXCanvas(j),
        y: getPlayerXCanvas(i),
        radius: cellWidth / 2 - 5,
        fill: playersColors[playersArr.indexOf(id)],
        stroke: 'black',
        strokeWidth: 2,
        id: "player" + id
    });
    playersLayer.add(circle);
    return circle;
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
    return cellsArr.indexOf(value) > -1;
}

//Функция возвращает true/false если указанное цифра соответствует игроку
function isPlayer(value) {
    return playersArr.indexOf(value) > -1;
}


//Обрабатывает нажатие клавиш на клавиаутре
$(document).keydown(function (event) {
    switch (event.keyCode) {
        case 38: //Вверх
        case 87: //W
            websocket.send("up");
            break;
        case 40: //Вниз
        case 83: //S
            websocket.send("down");
            break;
        case 37: //Лево
        case 65: //A
            websocket.send("left");
            break;
        case 39: //Право
        case 68: //D
            websocket.send("right");
            break;

    }
});


//Функция передвежения игрока и закраски клетки
function movePlayer(direction) {

    //Фигура соответсвующая управляемому игроку
    var player = playersLayer.findOne('#player' + currentPlayer.toString());
    var cellOld = cellsLayer.findOne('#' + getCellId(getPlayerYMatrix(player), getPlayerXMatrix(player)));
    var oldX = getPlayerXMatrix(player);
    var oldY = getPlayerYMatrix(player);
    var playerMoved = false;




    //Изменяем координаты фигуры игрока
    switch (direction) {
        case 37: //Лево
        case 65: //W
            if (canMoveLeft(player)) {
                player.move({x: -cellWidth, y: 0});
                playerMoved = true;
                sendPos(-cellWidth, 0);
            }
            break;
        case 38: //Вверх
        case 87: //S
            if (canMoveUp(player)) {
                player.move({x: 0, y: -cellWidth});
                playerMoved = true;
                sendPos(0, -cellWidth);
            }
            break;
        case 39: //Право
        case 68: //A
            if (canMoveRight(player)) {
                player.move({x: cellWidth, y: 0});
                playerMoved = true;
                sendPos(cellWidth, 0);
            }
            break;
        case 40: //Вниз
        case 83: //D
            if (canMoveDown(player)) {
                player.move({x: 0, y: cellWidth});
                playerMoved = true;
                sendPos(0, cellWidth);
            }
            break;
    }
    if (playerMoved) {

        //Передвигаем игрока
        playersLayer.draw();
        currentMatrix[getPlayerXMatrix(player)][getPlayerYMatrix(player)] = currentPlayer;

        //Красим клетку под игроком
        currentMatrix[oldX][oldY] = cellsArr[currentPlayer];
        cellOld.fill(cellsColors[currentPlayer]);
        cellOld.draw();

        var cell = cellsLayer.findOne('#' + getCellId(getPlayerYMatrix(player), getPlayerXMatrix(player)));
        cell.fill(cellsColors[currentPlayer]);
        cell.draw();

    }
}

function sendPos(x, y) {
    //  websocket.send("Hallo from " + currentPlayer.toString())
    var json = JSON.stringify({
        "id": currentPlayer,
        "coords": {
            "x": x,
            "y": y
        }
    });
    websocket.send(json);
}

//Проверяет может ли игрок сдвинуться в указанном направлении
function canMoveUp(player) {
    var playerX = getPlayerXMatrix(player);
    return playerX >= 1 && !isPlayer(currentMatrix[playerX - 1][getPlayerYMatrix(player)]);
}
function canMoveDown(player) {
    var playerX = getPlayerXMatrix(player);
    return playerX <= cellsCount - 2 && !isPlayer(currentMatrix[playerX + 1][getPlayerYMatrix(player)]);
}
function canMoveLeft(player) {
    var playerY = getPlayerYMatrix(player);
    return playerY >= 1 && !isPlayer(currentMatrix[getPlayerXMatrix(player)][playerY - 1]);
}
function canMoveRight(player) {
    var playerY = getPlayerYMatrix(player);
    return playerY <= cellsCount - 2 && !isPlayer(currentMatrix[getPlayerXMatrix(player)][playerY + 1]);
}

//Возвращает idшник прямоугольника на канвасе в формате X_Y
function getCellId(i, j) {
    var shapeX = i * cellWidth;
    var shapeY = j * cellWidth;
    return shapeX.toString() + '_' + shapeY.toString();
}


//Собтие смены игрока(Для тестирования базового функционала)
function change(evt) {
    var n = document.getElementById("Role").options.selectedIndex;
    var val = document.getElementById("Role").options[n].value;
    currentPlayer = parseInt(val, 10);
    document.getElementById("Role").blur();
}


//Отрисовка движения другого игрока
function drawOtherPlayerMove(data) {

    var json = JSON.parse(data);

    var player = playersLayer.findOne('#player' + json.id);
    var oldX = getPlayerXMatrix(player);
    var oldY = getPlayerYMatrix(player);

    //Передвигаем игрока
    player.move({x: json.coords.x, y: json.coords.y});
    playersLayer.draw();
    currentMatrix[getPlayerXMatrix(player)][getPlayerYMatrix(player)] = json.id;

    //Красим клетку
    currentMatrix[oldX][oldY] = cellsArr[json.id];
    var cell = cellsLayer.findOne('#' + getCellId(getPlayerYMatrix(player), getPlayerXMatrix(player)));
    cell.fill(cellsColors[json.id]);
    cell.draw();

    printMatrix(currentMatrix, cellsCount);
}


