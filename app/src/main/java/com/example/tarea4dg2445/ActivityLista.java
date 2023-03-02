package com.example.tarea4dg2445;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tarea4dg2445.R;
import com.example.tarea4dg2445.datos.conexion;
import com.example.tarea4dg2445.datos.consulta;
import com.example.tarea4dg2445.datos.informacion;

import java.util.ArrayList;
public class ActivityLista extends AppCompatActivity {
    private ListView lista;
    private conexion conectar;
    private ArrayList<informacion> listapersona;
    private ArrayList<String> arreglopersona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        lista=(ListView) findViewById(R.id.txt_lista);
        conectar=new conexion(this, consulta.DataBase,null,1);
        ObtenerLista();
        ArrayAdapter adp=new ArrayAdapter(this, android.R.layout.simple_list_item_1,arreglopersona);
        lista.setAdapter(adp);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mostrar=new Intent(getApplicationContext(),ActivityFoto.class);
                mostrar.putExtra("foto",listapersona.get(i).getUrl());
                startActivity(mostrar);
            }
        });
    }
    private void ObtenerLista() {
        SQLiteDatabase db=conectar.getReadableDatabase();
        informacion persona=null;
        listapersona=new ArrayList<informacion>();
        String envio="SELECT * FROM "+consulta.persona+" WHERE 1";
        Cursor cursor=db.rawQuery(envio,null);
        while (cursor.moveToNext()){
            persona=new informacion();
            persona.setId(cursor.getInt(0));
            persona.setNombre(cursor.getString(1));
            persona.setUrl(cursor.getString(2));
            persona.setDescripcion(cursor.getString(3));
            listapersona.add(persona);
        }
        cursor.close();
        fllList();
    }
    private void fllList() {
        arreglopersona=new ArrayList<String>();
        for(int i=0;i<listapersona.size();i++){
            arreglopersona.add(
                    "Nombre: "+listapersona.get(i).getNombre()+"\n"+
                            "Descripcion: "+listapersona.get(i).getDescripcion()
            );
        }
    }
    public void atras(View view){
        Intent crear=new Intent(this,MainActivity.class);
        startActivity(crear);
    }
}