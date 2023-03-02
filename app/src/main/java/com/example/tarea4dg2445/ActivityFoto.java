package com.example.tarea4dg2445;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.tarea4dg2445.R;

import java.io.File;
public class ActivityFoto extends AppCompatActivity {
    private ImageView foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        foto=(ImageView) findViewById(R.id.img_foto);
        File pegar=new File(getIntent().getStringExtra("foto"));
        foto.setImageURI(Uri.fromFile(pegar));
    }
    public void retroceder(View view){
        Intent inicio=new Intent(this,ActivityLista.class);
        startActivity(inicio);
    }
}