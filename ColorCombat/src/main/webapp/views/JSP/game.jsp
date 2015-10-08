<%-- 
    Document   : game
    Created on : 08.10.2015, 21:58:53
    Author     : boris
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="<c:url value="/js/socketJS.js" />"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
        <script>

            var cellWidth = 40;//ширина клетки
            var leftX = 0; //координаты левого верхнего угла квадрата в котором щас кружок
            var leftY = 0; //координаты правого верхнего угла квадрата в котором щас кружок
            var radius = 15;

            function DrawCanvas() {
                var canvas = document.getElementById("canvas");
                var context = canvas.getContext("2d");

                context.beginPath();
                context.arc(cellWidth / 2, cellWidth / 2, radius, 0, 2 * Math.PI, false);
                context.fillStyle = 'green';
                context.fill();
                context.lineWidth = 2;
                context.strokeStyle = '#003300';
                context.stroke();
            }


            $(document).keydown(function (event) {
                switch (event.keyCode) {
                    case 37: //Лево
                    case 38: //Вверх
                    case 39: //Право
                    case 40: //Вниз
                        MoveFigure(event.keyCode);
                        break;
                }
            });


            function MoveFigure(direction) {
                var canvas = document.getElementById("canvas");
                switch (direction) {
                    case 37: //Лево
                        shapeReDraw(leftX - 40, leftY);
                        break;
                    case 38: //Вверх
                        shapeReDraw(leftX, leftY - 40);
                        break;
                    case 39: //Право
                        shapeReDraw(leftX + 40, leftY);
                        break;
                    case 40: //Вниз
                        shapeReDraw(leftX, leftY + 40);
                        break;
                }
            }


            function shapeReDraw(x, y) {
                var canvas = document.getElementById("canvas");
                var context = canvas.getContext("2d");
                context.beginPath();
                context.rect(leftX + 3, leftY + 3, cellWidth - 7, cellWidth - 7);
                context.fillStyle = 'green';
                context.fill();
                context.closePath();

                leftX = x;
                leftY = y;

                context.beginPath();
                context.arc(leftX + cellWidth / 2, leftY + cellWidth / 2, radius, 0, 2 * Math.PI, false);
                context.fillStyle = 'green';
                context.fill();
                context.lineWidth = 2;
                context.strokeStyle = '#003300';
                context.stroke();
            }




        </script>
    </head>
    <body onload="DrawCanvas();">
        <h1>Hello World!</h1>
        <canvas id="canvas" width="420px" height="420px" style="background: #fff;     magrin:20px;"></canvas> 
        <script type="text/javascript" language="javascript">
            var bw = 400;
            var bh = 400;
            var p = 0;

            var canvas = document.getElementById("canvas");
            var context = canvas.getContext("2d");
            function drawBoard() {
                for (var x = 0; x <= bw; x += 40) {
                    context.moveTo(0.5 + x + p, p);
                    context.lineTo(0.5 + x + p, bh + p);
                }


                for (var x = 0; x <= bh; x += 40) {
                    context.moveTo(p, 0.5 + x + p);
                    context.lineTo(bw + p, 0.5 + x + p);
                }

                context.strokeStyle = "black";
                context.lineWidth = 2;
                context.stroke();
            }

            drawBoard();
        </script>
    </body>
</html>

