package com.example.animalapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_BestScore;
    private MediaPlayer mp;

    /*Colocar Imagenes Aleatorias en Inicio*/
    int num_aleatorio = (int) (Math.random() * 10);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = (EditText) findViewById(R.id.txt_nombre);
        iv_personaje = (ImageView) findViewById(R.id.iv_personaje);
        tv_BestScore = (TextView) findViewById(R.id.txt_BestScore);

        /*Colocar Icono en la barra superior*/
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.image_logo);

        /*Colocar Imagenes Aleatorias en Inicio*/
        int id;
        if (num_aleatorio == 0 || num_aleatorio == 10){
            id = getResources().getIdentifier("caballo", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }
        else if (num_aleatorio == 1 || num_aleatorio == 9){
            id = getResources().getIdentifier("cerdo", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }
        else if (num_aleatorio == 2 || num_aleatorio == 8){
            id = getResources().getIdentifier("conejo", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }
        else if (num_aleatorio == 3 || num_aleatorio == 7){
            id = getResources().getIdentifier("elefante", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }
        else if (num_aleatorio == 4 || num_aleatorio == 5 || num_aleatorio == 6){
            id = getResources().getIdentifier("vaca", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }
        /*Conexión a la base de datos*/
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"BD",null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        /*Consultamos los datos y los mostramos*/
        Cursor consulta = BD.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)", null);

        if(consulta.moveToFirst()){
              String temp_nombre = consulta.getString( 0);
              String temp_score = consulta.getString(1);
              tv_BestScore.setText("Record: " + temp_score + " de " + temp_nombre);
              BD.close();
          } else {
              BD.close();
          }

        /*Reproducir musica al iniciar la app en loop*/
        mp = MediaPlayer.create(this, R.raw.song);
        mp.start();
        mp.setLooping(true);

    }

    public void Jugar(View view){
        String nombre = et_nombre.getText().toString();

        if (!nombre.equals("")){
            mp.stop();
            mp.release();

            Intent intent = new Intent(this, Nivel1.class);
            intent.putExtra("jugador", nombre);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this,"Primero debes escribir tu nombre", Toast.LENGTH_SHORT).show();

            /*Se abre el teclado automaticamente para que escribas tu nombre */
            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);
        }


    }

    @Override
    public void onBackPressed(){

    }
}