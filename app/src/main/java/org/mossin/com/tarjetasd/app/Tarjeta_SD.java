package org.mossin.com.tarjetasd.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class Tarjeta_SD extends Activity  implements View.OnClickListener{
TextView resultado;
Button guardar,leer,nuevo;
EditText texto,nombreArchivo;
String nameArchivo,textoEscritoporUsuario;
File raiz,dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta__sd);

        resultado = (TextView)findViewById(R.id.textView1);
        guardar = (Button)findViewById(R.id.button);
        leer = (Button)findViewById(R.id.button2);
        nuevo = (Button)findViewById(R.id.button3);
        texto = (EditText)findViewById(R.id.editText2);
        nombreArchivo = (EditText)findViewById(R.id.editText);

       guardar.setEnabled(false);
       leer.setEnabled(false);

       guardar.setOnClickListener(this);
       leer.setOnClickListener(this);
       nuevo.setOnClickListener(this);

        //  Devuelve el estado actual del dispositivo de almacenamiento que proporciona la ruta dada.
        String estado = Environment.getExternalStorageState();
        resultado.append("\n"+estado);
        System.out.println("Mensaje: " + estado);

        // Devuelva el directorio primario de almacenamiento externo
        raiz = Environment.getExternalStorageDirectory();
        // creamos un nueva carpeta dentro de la directorio raiz
        dir = new File(raiz.getAbsolutePath()+"/Mossin");
        dir.mkdir();

        resultado.append("\nNuevo directorio: "+ dir);
        resultado.append("\nContenido del direcotorio raiz:");
        String [] listado = raiz.list();
        for(int i = 0; i< listado.length; i++){
            resultado.append("\n" + listado[i]);
        }

    }

    @Override
    public void onClick(View v) {
         nameArchivo = nombreArchivo.getText().toString().trim()+".txt";
         textoEscritoporUsuario = texto.getText().toString();

        switch (v.getId()){
          //Boton guardar
        case R.id.button:
            if(nameArchivo.equals("")){
                Toast.makeText(this,"Ingresa el nombre del archivo",Toast.LENGTH_LONG).show();

            }else if(textoEscritoporUsuario.equals("")){
                Toast.makeText(this,"Tu Archivo no tiene contenido",Toast.LENGTH_LONG).show();
            }else{
                //Escritura dentro de la Memoria SD
                File archivo = new File(dir,nameArchivo);
                try {
                    //Escribe byte dentro un archivo si No exite lo crea  y si existe otro lo remplaza
                    // o loagrega
                    FileOutputStream flujo = new FileOutputStream(archivo);
                    PrintWriter escribe = new PrintWriter(flujo);
                    escribe.println("\n"+textoEscritoporUsuario);
                    escribe.flush();
                    escribe.close();
                    Toast.makeText(this,"Archivo agregado Correctamente: "+nameArchivo+ "\ncon el contenido: "+textoEscritoporUsuario ,Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(this,"Error:\n"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
          break;
           //boton leer
          case R.id.button2:
              resultado.setText("");
              File archivo2 = new File(dir, nameArchivo);
              try {
                  FileInputStream flujoLetura = new FileInputStream(archivo2);
                  BufferedReader lector = new BufferedReader( new InputStreamReader(flujoLetura));

                  String cadena = lector.readLine();
                   while(cadena != null){
                       resultado.setText("\n"+cadena);
                       cadena = lector.readLine();
                   }

                  lector.close();
                  flujoLetura.close();
              }catch (Exception err) {
                  Toast.makeText(this, "Error: " + err.getMessage(),Toast.LENGTH_LONG).show();
              }

          break;
      //Boton Nuevo
          case R.id.button3:
              guardar.setEnabled(true);
              leer.setEnabled(true);

          break;
      }
    }
}
