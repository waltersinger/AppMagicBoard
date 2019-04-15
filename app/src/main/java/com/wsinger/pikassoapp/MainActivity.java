package com.wsinger.pikassoapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.wsinger.pikassoapp.view.PikassoView;


public class MainActivity extends AppCompatActivity {

    private PikassoView myView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    ImageView imageView;

    //Dialogo acho linea
    Bitmap bitmap;
    Canvas canvas;
    Paint p;

    //Dialogo de color
    private SeekBar seekBarBlue;
    private SeekBar seekBarRed;
    private SeekBar seekBarGreen;
    private SeekBar seekBarAlpha;
    private View viewColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = findViewById(R.id.magic_board_view);

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        //Aqui pondremos el grupo de sentencias que van a guardar el dibujo una vez que la pantalla rote (por definicion, cuando se produce
//        //una rotacion de pantalla, todos los widget se destruyen y se vuelven a crear, luego en onCreate colocaremos para cargar la img guardada
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId() ){
            case R.id.share_item_m:
                Snackbar.make(myView,"Compartir",Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.color_item_m:
                showColorDialog();
                break;
            case R.id.stroke_item_m:
                showLineWitdhDialog();
                break;
            case R.id.eraser_item_m:
                break;

            case R.id.borrar_item_m:
                myView.clear();
                break;
            case R.id.guardar_item_m:
                myView.saveImageToStore();
                break;
            case R.id.cargar_imagen_m:
                break;
        }

        return true;
    }

    private void showLineWitdhDialog(){

        alertDialogBuilder = new AlertDialog.Builder(this);
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_linewidth,null);
        imageView = viewDialog.findViewById(R.id.image_view_dialog);


        p  = new Paint();
        p.setColor(PikassoView.getColor());
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(myView.getStrokePencil());

        bitmap = Bitmap.createBitmap(350,100,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawLine(30,50,320,50,p);
        imageView.setImageBitmap(bitmap);


        final SeekBar seekBar = viewDialog.findViewById(R.id.seekbar_linewidth);
        Button button = viewDialog.findViewById(R.id.button_alert_strokew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(myView,"Trazo",Snackbar.LENGTH_SHORT).show();
                myView.setStrokePencil(seekBar.getProgress());
                alertDialog.dismiss();
                alertDialogBuilder = null;
            }
        });

        seekBar.setProgress( myView.getStrokePencil());

        seekBar.setOnSeekBarChangeListener(seekBarChange);
        alertDialogBuilder.setTitle(getResources().getString(R.string.title_dialog_width));
        alertDialogBuilder.setView(viewDialog);
        alertDialog =  alertDialogBuilder.create();
        alertDialog.show();

    }
    //Lo que quiero crear aqui es mostrar una barra para cambiar el grosor del lapiz
    private SeekBar.OnSeekBarChangeListener seekBarChange = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //Dibujo la figura en el alertdialog del grosor, para elegir el mismo

            p.setStrokeWidth(progress);

            bitmap.eraseColor(Color.WHITE);
            canvas.drawLine(30,50,320,50,p);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
//  CUADRO COLOR

    private void showColorDialog(){

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dalog_color,null);

        viewColor = view.findViewById(R.id.view_color);
        seekBarAlpha = view.findViewById(R.id.seek_bar_alpha);
        seekBarBlue = view.findViewById(R.id.seek_bar_blue);
        seekBarRed = view.findViewById(R.id.seek_bar_red);
        seekBarGreen = view.findViewById(R.id.seek_bar_green);

        //Registrar los listener de los seekbar
        seekBarAlpha.setOnSeekBarChangeListener(seekBarColorChange);
        seekBarBlue.setOnSeekBarChangeListener(seekBarColorChange);
        seekBarRed.setOnSeekBarChangeListener(seekBarColorChange);
        seekBarGreen.setOnSeekBarChangeListener(seekBarColorChange);

        int color = myView.getColor();
        seekBarAlpha.setProgress(Color.alpha(color));
        seekBarBlue.setProgress(Color.blue(color));
        seekBarGreen.setProgress(Color.green(color));
        seekBarRed.setProgress(Color.red(color));

        viewColor.setBackgroundColor(Color.argb(seekBarAlpha.getProgress(),seekBarRed.getProgress(),seekBarGreen.getProgress(),seekBarBlue.getProgress()));

        Button button = view.findViewById(R.id.button_alert_color);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myView.setDrawingColor(Color.argb(seekBarAlpha.getProgress(),
                        seekBarRed.getProgress(),
                        seekBarGreen.getProgress(),
                        seekBarBlue.getProgress()));
                    alertDialog.dismiss();
            }
        });

        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(R.string.title_dialog_color);
        alertDialog.show();
    }

    private SeekBar.OnSeekBarChangeListener seekBarColorChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            myView.setBackgroundColor(Color.argb(seekBarAlpha.getProgress(),
                    seekBarRed.getProgress(),
                    seekBarGreen.getProgress(),
                    seekBarBlue.getProgress()));

            viewColor.setBackgroundColor(Color.argb(seekBarAlpha.getProgress(),
                    seekBarRed.getProgress(),
                    seekBarGreen.getProgress(),
                    seekBarBlue.getProgress()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

}
