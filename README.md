# AppMagicBoard
Pizarra Mágica para dibujar a mano alzada. Tiene opciones basicas para cambiar color,borrar,grosor del lapiz. This is a Magic Board style app that has just basic functions like change color, stroke, and so on.
<p>
Es una aplicación simple y sencilla, con ello quisiera demostrar lo sencillo que es hacerlo. Primeramente se crea un CUSTOM VIEW que va
a tener un Lienzo (CANVAS) en donde se plasma un BITMA en el cual se va a dibujar. Tambien se van a definir los lapices, pinceles, colores.
<p>
Si hacemos una analogía con un diseño artistico a mano podría decirse que en CUSTOM VIEW es un Escritorio o mesa de trabajo en donde se va a poner las herramientas
como el lienzo, la hoja, los lapices o pinceles con sus distintos estilos, las temperas, las pinturas, los colores. Ademas del infaltable borrador.
<p>
Una vez que preparamos el "area de trabajo", diremos a la aplicacion que al iniciarse, levante esta area (CUSTOM VIEW). Este area de trabajo lo vamos
a plasmar en el XML principal del activity.
<p>
Al extender de un VIEW, se heredan ciertos eventos o metodos predefinidos de la clase padre que nos va a servir para definir todas
las herramientas que necesitamos. Por ejemplo dispondremos del metodo OnDraw, OnTouch etc...
<p>
Además de que tenemos de eventos que comunican la pantalla con nuestra aplicacion, es decir, al tocar la pantalla se dispararán ciertos
eventos que nos van a ser de ayuda para marcar la traza de la linea que queremos dibujar asi como tambien las caracteristicas de color, espesor, etc...
Es como si nuestro dedo fuese el lápiz.
<p>
Esta pequeña app contendra solo ciertas herramientas de dibujo, solo para pasar un buen momento.
