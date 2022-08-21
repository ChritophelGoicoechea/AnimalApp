package com.example.animalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class Nivel6 extends AppCompatActivity {


    private TextView tv_nombre, tv_score;
    private ImageView iv_Auno, iv_Ados, iv_vidas, iv_signo;
    private EditText et_respuesta;
    private MediaPlayer mp, mp_great, mp_bad;

    int score, numAleatorio_uno, numAleatorio_dos, resultado, vidas = 3;
    String nombre_jugador, string_score, string_vidas;

    String numero [] = {"img", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel6);

        Toast.makeText(this, "Nivel 4 - Sumas y Restas", Toast.LENGTH_SHORT).show();

        tv_nombre = (TextView) findViewById(R.id.txt_jugador);
        tv_score = (TextView) findViewById(R.id.txt_score);
        iv_vidas = (ImageView) findViewById(R.id.iv_vidas);
        iv_Auno = (ImageView) findViewById(R.id.iv_num1);
        iv_Ados = (ImageView) findViewById(R.id.iv_num2);
        iv_signo = (ImageView)findViewById(R.id.imageview_signo);
        et_respuesta = (EditText) findViewById(R.id.txt_resultado);

        /*Aqui agregamos el nombre del jugador*/
        nombre_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText("Jugador: " + nombre_jugador);

        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: " + score);

        string_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(string_vidas);
        if(vidas == 3){
            iv_vidas.setImageResource(R.drawable.tresvidas);

        }
        if(vidas == 2){
            iv_vidas.setImageResource(R.drawable.dosvidas);

        }
        if(vidas == 1){
            iv_vidas.setImageResource(R.drawable.unavida);

        }








        /*Colocar Icono en la barra superior*/
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.image_logo);

        /*Reproducir musica al iniciar la app en loop*/
        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);

        /*Cargar Audios a utilizar*/
        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        NumAleatorio();

    }

    public void Comparar(View view){
        String respuesta = et_respuesta.getText().toString();

        /*Respuestas Correctas*/
        if(!respuesta.equals("")){
            int respuesta_jugador = Integer.parseInt(respuesta);

            if(resultado == respuesta_jugador){
                mp_great.start();
                score++;
                tv_score.setText("Score: " + score);
                et_respuesta.setText("");
                BaseDeDatos();


            }
            /*Respuestas Incorrectas*/
            else{

                mp_bad.start();
                vidas--;

                switch(vidas){
                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this,"¡Oh no!, te quedan solo 2 manzanas", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this,"¡Oh no!, te quedan solo 1 manzanas", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this,"¡Oh no!, Has perdido todas tus manzanas :c", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();
                        break;

                }

                et_respuesta.setText("");

            }

            NumAleatorio();

        }else{
            Toast.makeText(this,"Escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }

    }

    public void NumAleatorio(){
        if(score <= 100){

            numAleatorio_uno = (int) (Math.random() * 10);
            numAleatorio_dos = (int) (Math.random() * 10);

            if(numAleatorio_uno >= 0 && numAleatorio_uno <= 3){
                resultado = numAleatorio_uno + numAleatorio_dos;
                iv_signo.setImageResource(R.drawable.adicion);

            }else if(numAleatorio_uno >= 4 && numAleatorio_uno <= 7){
                resultado = numAleatorio_uno - numAleatorio_dos;
                iv_signo.setImageResource(R.drawable.resta);

            }
            else{
                resultado = numAleatorio_uno * numAleatorio_dos;
                iv_signo.setImageResource(R.drawable.multiplicacion);
            }

            if(resultado >= 0){

                /*Intercambio de Numeros para realizar la operación*/
                for(int i = 0; i < numero.length; i++){

                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());

                    if(numAleatorio_uno == i){
                        /*Intercambia valor de tipo string por una imagen*/iv_Auno.setImageResource(id);
                    }if(numAleatorio_dos == i){
                        iv_Ados.setImageResource(id);

                    }

                }

            } else {
                NumAleatorio();
            }





        }else{

            Intent intent = new Intent(this, MainActivity.class);

            Toast.makeText(this,"¡FELICIDADES!, has terminado el juego, Eres un genio/a", Toast.LENGTH_SHORT).show();

            startActivity(intent);
            finish();

            mp.stop();
            mp.release();






        }
    }

    public void BaseDeDatos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"BD",null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);
        /*Insertar el score mayor del juego en la bd y actualizarlo en el activity Main*/
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString( 0);
            String temp_score = consulta.getString(1);

            int bestScore = Integer.parseInt(temp_score);


            if(score > bestScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);

                BD.update("puntaje", modificacion, "score=" + bestScore, null);



            }
            BD.close();


        }else{
            ContentValues insertar = new ContentValues();
            insertar.put("nombre", nombre_jugador);
            insertar.put("score", score);

            BD.insert("puntaje", null, insertar);
            BD.close();



        }

    }

    @Override
    public void onBackPressed(){

    }
}