package com.example.tarea4dg2445;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tarea4dg2445.R;
import com.example.tarea4dg2445.datos.conexion;
import com.example.tarea4dg2445.datos.consulta;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MainActivity extends AppCompatActivity {
    private EditText text_nombre,text_descripcion;
    private Button guardar;
    private ImageView imagen;
    private static final int REQUESTCODECAMARA=100;
    private static final int REQUESTTAKEFOTO=101;
    private String currentPhotoPath;
    private conexion conectar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_nombre=(EditText) findViewById(R.id.txt_nombre);
        text_descripcion=(EditText) findViewById(R.id.txt_descripcion);
        guardar=(Button) findViewById(R.id.bt_guardar);
        imagen=(ImageView) findViewById(R.id.img_imagen);
        conectar=new conexion(this, consulta.DataBase,null,1);
    }
    public void tomar_foto(View view){
        permisos();
    }
    public boolean validar(String dato, int numero){
        String opcion1="[A-Z,a-z,0-9,Á,É,Í,Ó,Ú,Ñ,ñ,.,_,' ',á,é,í,ó,ú,-]{1,300}";
        switch (numero){
            case 1:{return dato.matches(opcion1);}
            default:{return false;}
        }
    }
    public void crear(View view){
        if(validar(text_nombre.getText().toString().trim(),1)){
            if(validar(text_descripcion.getText().toString().trim(),1)){
                agregar();
            }
            else{
                Toast.makeText(this,"Descripcion no valida",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Nombre no valido",Toast.LENGTH_LONG).show();
        }
    }
    public void galleryAddPic(){
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f=new File(currentPhotoPath);
        Uri contenUri=Uri.fromFile(f);
        mediaScanIntent.setData(contenUri);
        this.sendBroadcast(mediaScanIntent);
    }
    public void dispatchTakePictureIntent()
    {
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile=null;
            try {
                photoFile=createImageFile();
            }
            catch (IOException ex){
            }
            if(photoFile!=null){
                Uri photoURI= FileProvider.getUriForFile(this,"com.example.tarea4dg2445.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent,REQUESTTAKEFOTO);
            }
        }
    }
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    //Agrega la foto al cuadro
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTTAKEFOTO && resultCode==RESULT_OK){
            File foto=new File(currentPhotoPath);
            imagen.setImageURI(Uri.fromFile(foto));
            guardar.setEnabled(true);
            galleryAddPic();

        }
    }
    public void permisos(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUESTCODECAMARA);
        }
        else{
            dispatchTakePictureIntent();
            //tomarFoto();
        }
    }

    private void tomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,REQUESTTAKEFOTO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUESTCODECAMARA){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else{
                Toast.makeText(getApplicationContext(),"Permiso Denegado",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void agregar(){
        SQLiteDatabase db=conectar.getWritableDatabase();
        ContentValues valores=new ContentValues();
        valores.put(consulta.nombre,text_nombre.getText().toString());
        valores.put(consulta.url,currentPhotoPath);
        valores.put(consulta.descripcion,text_descripcion.getText().toString());
        Long resultado=db.insert(consulta.persona,consulta.id,valores);
        Toast.makeText(getApplicationContext(),"Registro guardado",Toast.LENGTH_LONG).show();
        db.close();
        limpiar();
    }
    public void limpiar(){
        retroceder();
    }
    public void volver(View view){
        retroceder();
    }
    public void retroceder(){
        text_nombre.setText("");
        text_descripcion.setText("");
        currentPhotoPath="";
        File foto=new File(currentPhotoPath);
        imagen.setImageURI(Uri.fromFile(foto));
        guardar.setEnabled(false);
        Intent principal=new Intent(this,ActivityLista.class);
        startActivity(principal);
    }
}