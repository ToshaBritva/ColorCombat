/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var stage = new Konva.Stage({
    container: 'container', // индификатор div контейнера
    width: 503,
    height: 503
});

// далее создаём слой
var layer = new Konva.Layer();

// создаём фигуру
var circle = new Konva.Circle({
    x: stage.width() / 2,
    y: stage.height() / 2,
    radius: 70,
    fill: 'red',
    stroke: 'black',
    strokeWidth: 4,
    id: 'face'
});

var rect = new Konva.Rect({
    x: 1,
    y: 1,
    width: stage.width() - 2,
    height: stage.height() - 2,
    fill: 'white',
    stroke: 'black',
    strokeWidth: 2
});
layer.add(rect);

var iie = 1;
var jje = 1;

for (var i = 1; i <= 500; i = i + 25) {
    for (var j = 1; j <= 500; j = j + 25) {
        var rect = new Konva.Rect({
            x: j + 1,
            y: i + 1,
            width: 25 - 2,
            height: 25 - 2,
            fill: 'white',
            stroke: 'black',
            id: jje.toString() + "_" + iie.toString(),
            name: jje.toString() + "_" + iie.toString(),
            strokeWidth: 2
        });
        iie++;
        layer.add(rect);
    }
    iie = 1;
    jje++;
}
layer.on('click', function (evt) {
    // get the shape that was clicked on
    var rect = evt.target;
    alert('you clicked on \"' + rect.getName() + '\"');
});


var tweens = [];
var shapes = layer.find('.5_5');


// apply transition to all nodes in the array
shapes.each(function (shape) {
    shape.fill('green');
});


// добавляем круг на слой
layer.add(circle);

// добавляем слой
stage.add(layer);
