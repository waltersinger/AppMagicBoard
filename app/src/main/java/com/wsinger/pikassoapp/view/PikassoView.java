package com.wsinger.pikassoapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class PikassoView extends View {

    //Aqui construimos los elementos para dibujar en la pantalla
    public static final float TOUCH_TOLERANCE=10;

    private Bitmap bitmap; //imagen
    private Canvas bitmapCanvas; //el lienzo
    private Paint paintScreen; //
    private Paint paintLine;//lapiz
    private HashMap<Integer, Path> pathMap; //una linea que une dos o mas puntos
    private HashMap<Integer, Point> previousPointMap; //es un punto (basicamente un (x,y) en la pantalla)

    public PikassoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //Inicializo el area de dibujo, el lapiz, los puntos y las trazas del lapiz. Es como preparar un papel con el lapiz
        init();
    }

    void init(){
        paintScreen = new Paint();
        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.STROKE); //si no se lo define explicitamente, lo toma como FILL-AND_STROKE (esto es el relleno)
        paintLine.setStrokeWidth(7); //ancho de la punta
        paintLine.setStrokeCap(Paint.Cap.ROUND); //la terminacion de la linea, punta redonda

        pathMap = new HashMap<>();
        previousPointMap = new HashMap<>();
    }

    //Este evento captura lo que se ha dibujado en el lienzo (a traves del evento ontouch) y luego lo coloco en el area
    @Override
    protected void onDraw(Canvas canvas) {
        //Inicializa el area donde se va a dibujar
        canvas.drawBitmap(bitmap,0,0,paintScreen);
        //atrapa los trazos guardados en un hashmap y los dibuja.
        for (Integer key: pathMap.keySet() ) {
            canvas.drawPath(pathMap.get(key),paintLine);
        }
        //canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,78,paintLine);
    }
    //evento que captura el toque de la pantalla
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d("Screen touched!", event.toString());
        //Log.d("Screen touched!", String.valueOf(event.getActionMasked()));
        //Saco las coordenadas y el tipo de evento.
        int action = event.getActionMasked(); //tipo de evento (ACTION_UP,DOWN,ETC)
        int actionIndex = event.getActionIndex();// pointer (finger, mouse...)

        //fetch the finger and the movement.
        // Lo que busco aqui es obtener el punto inicial cuando se toca la pantalla y sin soltar mientras se arrastra
        // obtener los puntos y obtener los subsiguientes puntos.
        //Tambien cuando se suelta el dedo de la pantalla. Con esto busco trazar el recorrido
        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_UP){ //
            touchStarted(event.getX(actionIndex),
                    event.getY(actionIndex),//X and Y values where de pointer is
                    event.getPointerId(actionIndex));

            //Log.d("Test", String.valueOf(event.getPointerId(actionIndex)));
            Log.d("Test", "x="+String.valueOf(event.getX(actionIndex))+" y="+String.valueOf(event.getY(actionIndex)));
            Log.d("Test tipo de action", String.valueOf(action));

        }else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP){
            touchEnded(event.getPointerId(actionIndex));
            Log.d("Test tipo de action", "UP "+String.valueOf(action));
        }else{
            touchMoved(event);
        }

        invalidate(); //redraws the screen. refresca la pantalla, lo hace rapido, y fluido.

        return true;
    }

    private void touchMoved(MotionEvent event) {
        //una vez que el usuario dejo de mover, se llama a esta funcion. Va a tener todos los puntos de la pantalla por lo que el
        //dedo ha recorrido y vamos a intentar trazar el camino.

        for(int i=0; i<event.getPointerCount();i++){
            int pointerId = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerId);

            if(pathMap.containsKey(pointerId)){
                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);

                Path path = pathMap.get(pointerId);
                Point point = previousPointMap.get(pointerId);

                //calcular la distancia entre el punto actual y el ultimo punto anteriormente guardado.
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);

                //si la distancia es suficientemente significativa como para que se considere un movimiento entonces, trazar...
                if(deltaX>= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE){
                    path.quadTo(point.x,point.y,
                            (newX+point.x)/2, (newY + point.y)/2);

                    //almacenar las nuevas coordenadas.
                    point.x = (int) newX;
                    point.y = (int) newY;
                }
            }
        }
    }

    public void clear(){
        pathMap.clear(); //borrar todos las trazas (paths)
        previousPointMap.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate();//refrescar la pantalla

    }

    private void touchEnded(int pointerId) {
        //guardar el path
        Path path = pathMap.get(pointerId); //obtener el path o traza correspondiente
        bitmapCanvas.drawPath(path,paintLine); //dibujar en el canvas bitmap
        path.reset();
    }

    private void touchStarted(float x, float y, int pointerId) {
        //Todos los points se guardaran en path!! todos los puntos se guardan
        Path path;// store the path for given touch
        Point points;//store the last point in path

        if(pathMap.containsKey(pointerId)){ //si existe el objeto path en la posision pointerid, devuelvo los obj necesarios
            path = pathMap.get(pointerId);
            points = previousPointMap.get(pointerId);
        }else{//sino los guarda en sus respectivos almacenes.
            path = new Path();
            pathMap.put(pointerId,path);
            points = new Point();
            previousPointMap.put(pointerId,points);
        }

        //mover hacia la coordenada del touch
        path.moveTo(x,y);
        points.x = (int)x;
        points.y=(int)y;
    }
}
