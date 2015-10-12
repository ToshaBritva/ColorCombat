/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var cellsArr = [0];
var playersArr = [];
var playersColors = ["red", "yellow", "lime", "aqua"];
var cellsColors = ["white", "lightcoral", "#F0F000", "#4DDB4D", "#00E6E6"];

var currentPlayer = 1;

var cellsCount = 10;

var currentMatrix = new Array(cellsCount);
for (i = 0; i < cellsCount; i++) {
    currentMatrix[i] = new Array(cellsCount);
    currentMatrix[i].fill(0);
}

var newMatrix = new Array(cellsCount);
for (i = 0; i < cellsCount; i++) {
    newMatrix[i] = new Array(cellsCount);
    newMatrix[i].fill(0);
}

newMatrix[0][0] = 1;
newMatrix[0][cellsCount - 1] = 2;
newMatrix[cellsCount - 1][0] = 3;
newMatrix[cellsCount - 1][cellsCount - 1] = 4;



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

initField();
printMatrix(currentMatrix, cellsCount);
reDrawField();



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

    if (newMatrix[0][0] !== 0)
        addNewPlayer(0, 0, 1);

    if (newMatrix[0][cellsCount - 1] !== 0)
        addNewPlayer(0, cellsCount - 1, 2);

    if (newMatrix[cellsCount - 1][0] !== 0)
        addNewPlayer(cellsCount - 1, 0, 3);

    if (newMatrix[cellsCount - 1][cellsCount - 1] !== 0)
        addNewPlayer(cellsCount - 1, cellsCount - 1, 4);

    currentMatrix = newMatrix;
    field.add(playersLayer);


}

//Добавление нового игрока
function addNewPlayer(i, j, id) {
    playersArr.push(id);
    cellsArr.push(id + 4);
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
}


//Возвращает координаты игрока в матрице
function getPlayerXMatrix(player) {
    return (player.y() - cellWidth / 2) / cellWidth;
}
function getPlayerYMatrix(player) {
    return (player.x() - cellWidth / 2) / cellWidth;
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
        case 37: //Лево
        case 65: //W
        case 38: //Вверх
        case 87: //S
        case 39: //Право
        case 68: //A
        case 40: //Вниз
        case 83: //D
            movePlayer(event.keyCode);
            printMatrix(currentMatrix, cellsCount);
            break;
    }
});


//Функция передвежения игрока и закраски клетки
function movePlayer(direction) {

    //Фигура соответсвующая управляемому игроку
    var player = playersLayer.findOne('#player' + currentPlayer.toString());
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

//Для отладки: выводит матрицу на страницу
function printMatrix(matrix, n) {
    var printThis = "";
    for (var i = 0; i < n; i++) {
        printThis += "<br>";
        for (var j = 0; j < n; j++)
            printThis += " " + matrix[i][j];
    }
    document.getElementById('currentTable').innerHTML = printThis;
}


function reDrawField() {
    for (var i = 0; i < cellsCount; i++)
        for (var j = 0; j < cellsCount; j++)
            if (currentMatrix[i][j] !== newMatrix[i][j]) {
                //если значения в матрицах различны, но при этом соответсвующие
                // фигугры имеют являются клетками, то перекрашиваем старую клетку
                if (isCell(currentMatrix[i][j]) && isCell(newMatrix[i][j])) {
                    var shape = cellsLayer.findOne('#' + getCellId(i, j));
                    shape.fill(cellsColors(newMatrix[i][j]));
                }
                else {
                    if (isPlayer(newMatrix[i][j])) {
                        var player = playersLayer.findOne('#player' + newMatrix[i][j].toString());
                        player.x = getPlayerXCanvas(j);
                        player.y = getPlayerYCanvas(i);
                        player.draw();
                    }
                }
                currentMatrix[i][j] = newMatrix[i][j];
            }
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


