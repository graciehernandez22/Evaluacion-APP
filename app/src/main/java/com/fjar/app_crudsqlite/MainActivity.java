package com.fjar.app_crudsqlite;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.fjar.app_crudsqlite.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {
    private EditText et_codigo, et_descripcion, et_precio;
    private Button btn_guardar, btn_consultar1, btn_consultar2, btn_eliminar, btn_actualizar;
    private TextView tv_resultado;
    private Spinner spn;
    private DtoCategoria cat;

    boolean inputEt=false;
    boolean inputEd=false;
    boolean input1=false;
    int resultadoInsert=0;

    Modal ventanas = new Modal();
    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();
    AlertDialog.Builder dialogo;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_delete)
                    .setTitle("Warning")
                    .setMessage("Realmente deseas salir")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            finishAffinity();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck1 = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
            try {
                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                startActivity(intent);}
            catch (Exception ex){
               Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }



        } else {
            Log.i("Mensaje", "Se tiene permiso para leer y escribir!");
        }
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.attributes));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setTitleMargin(0,0,0,0);
        toolbar.setSubtitle("CRUD SQLite-2022");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.black));
        toolbar.setTitle("EVALUACIÓN SIS 21B");
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmacion();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanas.search(MainActivity.this);
            }
        });
        et_codigo = (EditText) findViewById(R.id.et_codigo);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        et_precio = (EditText) findViewById(R.id.et_precio);
        btn_guardar=(Button) findViewById(R.id.btn_guardar);
        btn_consultar1=(Button) findViewById(R.id.btn_consultar1);
        btn_consultar2=(Button) findViewById(R.id.btn_consultar2);
        btn_eliminar=(Button) findViewById(R.id.btn_eliminar);
        btn_actualizar=(Button) findViewById(R.id.btn_actualizar);
        spn = (Spinner) findViewById(R.id.spnCategoria);
        cat = new DtoCategoria();

        conexion.consultaCategoria();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                conexion.obtenerCategorias());
        spn.setAdapter(adaptador);



        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0){

                    cat.setIdCategoria(conexion.consultaCategoria().get(position - 1).getIdCategoria());

                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        String senal = "";
        String codigo = "";
        String descripcion = "";
        String precio = "";

        try{
            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            if(bundle !=null){
                senal = bundle.getString("flag");
                codigo = bundle.getString("codigo");
                if (senal != ""){
                    Toast.makeText(this, "El dato es" + senal + codigo, Toast.LENGTH_SHORT).show();
                }
                descripcion = bundle.getString("descripcion");
                precio = bundle.getString("precio");
                if(senal.equals("1")){
                    et_codigo.setText(codigo);
                    et_descripcion.setText(descripcion);
                    et_precio.setText(precio);
                }

            }
        }catch (Exception e){

        }

    }
    private void confirmacion(){
        String mensaje = "¿Realmente desea salir?";

        dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setIcon(R.drawable.ic_delete);
        dialogo.setTitle("Warning");
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                MainActivity.this.finish();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int id) {

            }
        });
        dialogo.show();
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Limpiar) {
            et_codigo.setText(null);
            et_descripcion.setText(null);
            et_precio.setText(null);
            return true;
        }else if(id==R.id.listaArticulos){
            Intent spinnerActivity = new Intent(MainActivity.this, Activity_consulta_spinner.class);
            startActivity(spinnerActivity);
            return true;
        }else if(id==R.id.listaArticulos1){
            Intent listViewActivity = new Intent(MainActivity.this, Activity_list_view_articulos.class);
            startActivity(listViewActivity);
            return true;
        }else if(id==R.id.listRecyclerView){
            Intent listViewActivity = new Intent(MainActivity.this, Recycler_View.class);
            startActivity(listViewActivity);
            return true;
        }else if(id==R.id.ListViewCat){
            Intent listViewActivity = new Intent(MainActivity.this, Activity_list_view_categorias.class);
            startActivity(listViewActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alta (View v){
    if(et_codigo.getText().toString().length()==0){
        et_codigo.setError("Campo Obligatorio");
        inputEt = false;
    }else{
        inputEt=true;
    }
        if(et_descripcion.getText().toString().length()==0){
            et_descripcion.setError("Campo Obligatorio");
            inputEd = false;
        }else{
            inputEd=true;
        }if(et_precio.getText().toString().length()==0){
            et_precio.setError("Campo Obligatorio");
            input1 = false;
        }else{
            input1=true;
        }
           if(inputEt && inputEd && input1){
               try{
                   datos.setCodigo(Integer.parseInt(et_codigo.getText().toString()));
                   datos.setDescripcion(et_descripcion.getText().toString());
                   datos.setPrecio(Double.parseDouble(et_precio.getText().toString()));
                   datos.setIdCategoria(cat.getIdCategoria()); //Se agrego este campo para el registro de idcategoria
                   if(conexion.InsertarTradicional(datos)){
                       Toast.makeText(this, "Registro agregado satisfactoriamente!", Toast.LENGTH_SHORT).show();
                       limpiarDatos();
                   }else{
                       Toast.makeText(getApplicationContext(), "Error. Ya exite un registro\n"+"Codigo: "+et_codigo.getText().toString(), Toast.LENGTH_LONG).show();
                       limpiarDatos();

                   }
               }catch (Exception e){
                   Toast.makeText(this, "ERROR. Ya existe. ", Toast.LENGTH_SHORT).show();

               }
           }
    }
    public void mensaje (String mensaje){
        Toast.makeText(this, ""+mensaje, Toast.LENGTH_SHORT).show();

    }
    public void limpiarDatos(){
        et_codigo.setText(null);
        et_descripcion.setText(null);
        et_precio.setText(null);
        et_codigo.requestFocus();
    }
    public void consultaporcodigo (View v){
        if(et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo Obligatorio");
            inputEt=false;
        }else{
            inputEt=true;
        }
        if(inputEt){
            String codigo = et_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(codigo));
            if(conexion.consultaArticulos(datos)){
                et_descripcion.setText(datos.getDescripcion());
                et_precio.setText(""+datos.getPrecio());
            }else{
                Toast.makeText(this, "No existe un articulo con dicho código", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }else{
            Toast.makeText(this, "Ingrese el código del articulo a buscar", Toast.LENGTH_SHORT).show();

        }
    }

    public void consultapordescripcion(View v){
        if(et_descripcion.getText().toString().length()==0){
            et_descripcion.setError("Campo Obligatorio");
            inputEd=false;
        }else{
            inputEd=true;
        }
        if(inputEd){
            String descripcion = et_descripcion.getText().toString();
            datos.setDescripcion(descripcion);
            if(conexion.consultarDescripcion(datos)){
                et_codigo.setText(""+datos.getCodigo());
                et_descripcion.setText(datos.getDescripcion());
                et_precio.setText(""+datos.getPrecio());
            }else{
                Toast.makeText(this, "No existe un articulo con dicha descripción", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }

    }else{
            Toast.makeText(this, "Ingrese la descripción del articulo a buscar", Toast.LENGTH_SHORT).show();

        }

    }
    public void bajaporcodigo(View v){
        if(et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo Obligatorio");
            inputEt=false;
        }else{
            inputEt=true;
        }
        if(inputEt){
            String cod = et_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(cod));
            if(conexion.bajaCodigo(MainActivity.this,datos)){
                limpiarDatos();
            }else{
                Toast.makeText(this, "No existe un articulo con dicho código", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }
    }
    public void modificacion(View v){
        if(et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo Obligatorio");
            inputEt=false;
        }else{
            inputEt=true;
        }
        if(inputEt){
            String cod = et_codigo.getText().toString();
            String descripcion = et_descripcion.getText().toString();
            double precio = Double.parseDouble(et_precio.getText().toString());

            datos.setCodigo(Integer.parseInt(cod));
            datos.setDescripcion(descripcion);
            datos.setPrecio(precio);
            if(conexion.modificar(datos)){
               Toast.makeText(this, "Registro Modificado Correctamente.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se han encontrado resultados para la busqueda especificada", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void CopiaBD(View v){
        try{
            final String inFileName = "/data/data/com.fjar.app_crudsqlite/databases/administracion.db";


            File dbFile = new File(inFileName);
            if(dbFile.exists()){
                Toast.makeText(this, "Existe la base de datos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No existe la base de datos", Toast.LENGTH_SHORT).show();
            }
            FileInputStream fis = new FileInputStream(dbFile);
            File directorio = new File(Environment.getExternalStorageDirectory() + "/CopiasDB");
            if(!directorio.exists()) {
                if (directorio.mkdirs()) {
                    Toast.makeText(this, "Se creo el directorio", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "No se creo el directorio", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Ya existe el direcotorio", Toast.LENGTH_SHORT).show();
            }
            String outFileName = Environment.getExternalStorageDirectory() + "/CopiasDB/administra_copy.db";
            File files = new File(outFileName);
            if(!files.exists()){
                files.createNewFile();
                if(files.exists()){
                    Toast.makeText(this, "El archivo copia existe", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this, "Exste la base de datos ", Toast.LENGTH_SHORT).show();
            }
            // Open the empty db as the output stream
            FileOutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[(int) dbFile.length()];

            int length;
            while ((length = fis.read(buffer))>0){
                output.write(buffer, 0, length);
            }
            // Close the streams
            output.flush();
            output.close();
            fis.close();


        }catch(Exception e){
            Log.e("error", e.toString());
        }

    }
}