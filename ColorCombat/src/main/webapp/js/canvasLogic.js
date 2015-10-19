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

