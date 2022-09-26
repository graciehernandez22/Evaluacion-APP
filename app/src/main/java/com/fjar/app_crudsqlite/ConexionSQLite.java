package com.fjar.app_crudsqlite;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConexionSQLite extends SQLiteOpenHelper {

    boolean estadoDelete = true;

    ArrayList<String>listaArticulos;
    ArrayList<String> listaCategorias;

    ArrayList<Dto>articulosList;
    ArrayList<DtoCategoria> categoriaList;


    public ConexionSQLite(Context context) {super(context, "administracion.db", null, 3);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        Estas lineas falla pero desconozco porque,faltaria establecerles un limite al tipo de dato del campo??
        //db.execSQL("Create table categorias(idCategoria integer not null primary key, nameCategoria text not null, estadoCategoria integer not null, fechaRegistro text not null)");
        //db.execSQL("Create table articulos(codigo integer not null primary key, descripcion text, precio real, idCategoria integer )");
         */

        /*Definiendo la estrucutura de la base de datos*/
        db.execSQL("create table tb_categorias (idcategoria integer(11) not null primary key, nombrecategoria varchar(50) not null, estadocategoria integer(11) not null, fecharegistro datetime not null)");
        db.execSQL("create table articulos (codigo integer(11) not null primary key, descripcion varchar(100) not null, precio real not null, idcategoria integer(11) not null, FOREIGN KEY(idcategoria) REFERENCES tb_categorias(idcategoria))");

        //Almacenamiento de 3 categorias para efectos de prueba y ejemplo.
        db.execSQL("insert into tb_categorias values(1, 'Smartphone', 1, datetime('now','localtime')), (2, 'Tablets', 1, datetime('now','localtime')), (3, 'Computadora', 1, datetime('now','localtime')) ");

        /*
        db.execSQL("insert into categoria values(1, 'Electrodomesticos', 1, datetime('now','localtime'))");
        db.execSQL("insert into categoria values(2, 'Muebles', 1, datetime('now','localtime'))");
        db.execSQL("insert into categoria values(3, 'Celulares', 1, datetime('now','localtime'))");
        db.execSQL("insert into categoria values(4, 'Computadoras', 1, datetime('now','localtime'))");
        db.execSQL("insert into categoria values(5, 'Lamparas', 1, datetime('now','localtime'))");
        db.execSQL("insert into categoria values(6, 'Audio', 1, datetime('now','localtime'))");
        db.execSQL("insert into categoria values(7, 'Monitores', 1, datetime('now','localtime'))");
        db.execSQL("insert into categoria values(8, 'Laptops', 1, datetime('now','localtime'))");
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists articulos");
        db.execSQL("drop table if exists tb_categorias");
        onCreate(db);
    }

    public SQLiteDatabase db(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }

    //Método que inserta los datos en la tabla de forma tradicional
    public boolean InsertarTradicional (Dto datos){
        boolean estado = true;
        int resultado;

        try{
            int codigo = datos.getCodigo();
            String desc = datos.getDescripcion();
            double precio = datos.getPrecio();
            int idCategoria = datos.getIdCategoria();

            //Cursor fila = this.getWritableDatabase().rawQuery("Select codigo from articulos where codigo == " + codigo, null);
            Cursor fila = db().rawQuery("Select codigo from articulos Where codigo == " + codigo, null);
            if (fila.moveToFirst()){
                estado = false;

            }else {
                //estado = (Boolean) this.getWritableDatabase().insert("datos", "nombre, correo, telefono", registro);
                //resultado = (int) this.getWritableDatabase().insert("usuarios", "nombres, apellidos, usuario, clave, pregunta, respuesta", registro);
                String SQL = "Insert Into articulos \n" +
                            "(codigo, descripcion, precio, idCategoria) \n" +
                            "Values \n "+
                            "('" + String.valueOf(codigo) + "', '" + desc + "', '" + String.valueOf(precio) + "', '" + String.valueOf(idCategoria)  + "');";

                db().execSQL(SQL);
                db().close();
                /*
                this.getWritableDatabase().execSQL(SQL);
                this.getWritableDatabase().close();
                 */
                estado = true;
            }
        }catch (Exception e) {
            estado = false;
            Log.e("error", e.toString());
        }
        return estado;
    }

    public boolean insertardatos(Dto datos){
        boolean estado = true;
        int resultado;
        ContentValues registro = new ContentValues();
        try{
            registro.put("codigo",datos.getCodigo());
            registro.put("descripcion",datos.getDescripcion());
            registro.put("precio",datos.getPrecio());
            Cursor fila = db().rawQuery("select codigo from articulo where codigo ='"
                    +datos.getCodigo()+"'", null);
            if(fila.moveToFirst()==true){
                estado = false;
            }else {
                resultado = (int) db().insert("articulo", null, registro);
                if(resultado > 0) estado = true;
                else estado = false;
            }
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean InsertRegister(Dto datos){
        boolean estado = true;
        int resultado;
        try{
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            Cursor fila = db().rawQuery("select codigo from articulo where codigo='"
                    +datos.getCodigo()+"'", null);
            if(fila.moveToFirst()==true){
                estado=false;
            }else {
                String SQL = "INSERT INTO articulos \n" +
                        "(codigo,descripcion,precio)\n" +
                        "VALUES \n" +
                        "(?,?,?);";

                db().execSQL(SQL, new String[]{
                        String.valueOf(codigo),descripcion,String.valueOf(precio)
                });
                estado = true;
            }
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean consultaCodigo(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int codigo = datos.getCodigo();

            Cursor fila = db.rawQuery("select codigo, descripcion, precio from articulos where codigo = " + codigo, null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else{
                estado = false;
            }
            db.close();
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean consultaArticulos(Dto datos){
        boolean estado = true;
        int resultado;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String[]parametro = {String.valueOf(datos.getCodigo())};
            String[]campos = {"codigo","descripcion","precio"};
            Cursor fila = db.query("articulos", campos, "codigo=?",parametro,null,null,null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else{
                estado = false;
            }
            fila.close();
            db.close();
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean consultarDescripcion(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            String descripcion = datos.getDescripcion();
            Cursor fila = db.rawQuery("select codigo, descripcion, precio from articulos where descripcion = '" + descripcion + "'", null);

            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else {
                estado = false;
            }
            db.close();
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean bajaCodigo(final Context context, final Dto datos){

        estadoDelete = true;
        try {

            int codigo = datos.getCodigo();
            Cursor fila = db().rawQuery("select * from articulos where codigo=" + codigo, null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_delete);
                builder.setTitle("Warning");
                builder.setMessage("¿Esta seguro de borrar el registro? \n Código:" +
                        datos.getCodigo()+"\nDescripción: "+ datos.getDescripcion());
                builder.setCancelable(false);
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int codigo = datos.getCodigo();
                        int cant = db().delete("articulos","codigo = " + codigo, null);

                        if(cant > 0){
                            estadoDelete = true;
                            Toast.makeText(context, "Registro eliminado satisfactoriamente.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            estadoDelete = false;
                        }
                        db().close();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();;
            }else {
                Toast.makeText(context, "No hay resultados encontrados para la busqueda" +
                        "especifica.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            estadoDelete = false;
            Log.e("Error.", e.toString());
        }
        return estadoDelete;
    }

    public boolean modificar(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio",precio);

            int cant = (int) db.update("articulos", registro, "codigo="+ codigo,null);

            db.close();
            if(cant>0)estado = true;
            else estado = false;
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public ArrayList<Dto> consultaListaArticulos(){
        boolean estado = false;

        SQLiteDatabase db = this.getWritableDatabase();

        Dto articulos = null;
        articulosList = new ArrayList<Dto>();

        try {
            Cursor fila = db.rawQuery("select * from articulos",null);
            while (fila.moveToNext()){
                articulos = new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));

                articulosList.add(articulos);

                Log.i("codigo",String.valueOf(articulos.getCodigo()));
                Log.i("descripcion",articulos.getDescripcion().toString());
                Log.i("precio", String.valueOf(articulos.getPrecio()));
            }
            obtenerListaArticulos();
        }catch (Exception e){

        }
        return articulosList;
    }

    public ArrayList<DtoCategoria> consultaCategoria(){
        boolean estado = false;

        SQLiteDatabase db = this.getWritableDatabase();
        DtoCategoria cat = null;
        categoriaList = new ArrayList<DtoCategoria>();
        try{
            Cursor fila = db.rawQuery("select * from tb_categorias",null);
            while (fila.moveToNext()){
                cat = new DtoCategoria();
                cat.setIdCategoria(fila.getInt(0));
                cat.setNameCategoria(fila.getString(1));
                cat.setEstadoCategoria(fila.getInt(2));

                categoriaList.add(cat);

                Log.i("codigo",String.valueOf(cat.getIdCategoria()));
                Log.i("nombre categoria",cat.getNameCategoria());
                Log.i("Estado", String.valueOf(cat.getEstadoCategoria()));
            }
            obtenerCategorias();
        }catch (Exception e){

        }
        return categoriaList;
    }

    public ArrayList<String> obtenerListaArticulos(){
        listaArticulos = new ArrayList<String>();
        listaArticulos.add("Seleccione");

        for (int i=0; i<articulosList.size();i++){
            listaArticulos.add(articulosList.get(i).getCodigo()+"~"+
                    ""+articulosList.get(i).getDescripcion());
        }
        return listaArticulos;
    }

    public ArrayList<String> obtenerCategorias(){
        listaCategorias = new ArrayList<>();
        listaCategorias.add("Seleccione una categoria");

        for(int i = 0; i < categoriaList.size(); i++){
            listaCategorias.add(categoriaList.get(i).getIdCategoria() + " -> " +
                    categoriaList.get(i).getNameCategoria());
        }

        return listaCategorias;
    }

    public ArrayList<String> consultaListaArticulos1(){
        boolean estado = false;

        SQLiteDatabase db = this.getWritableDatabase();

        Dto articulos = null;
        articulosList = new ArrayList<Dto>();

        try {
            Cursor fila = db.rawQuery("select * from articulos", null);
            while (fila.moveToNext()){
                articulos = new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));

                articulosList.add(articulos);
            }

            listaArticulos = new ArrayList<String>();

            for (int i=0; i<= articulosList.size(); i++){
                listaArticulos.add(articulosList.get(i).getCodigo()+"~"+ articulosList.get(i).getDescripcion());
            }
        }catch (Exception e){

        }
        return listaArticulos;
    }

    public ArrayList<String> consultaListaCategorias(){
        boolean estado = false;

        SQLiteDatabase db = this.getWritableDatabase();

        DtoCategoria categoria = null;
        categoriaList = new ArrayList<DtoCategoria>();

        try {
            Cursor fila = db.rawQuery("select * from tb_categorias", null);
            while (fila.moveToNext()){
                categoria= new DtoCategoria();
                categoria.setIdCategoria(fila.getInt(0));
                categoria.setNameCategoria(fila.getString(1));
                categoria.setEstadoCategoria(fila.getInt(2));

                categoriaList.add(categoria);
            }

            listaCategorias = new ArrayList<String>();

            for (int i=0; i<= categoriaList.size(); i++){
                listaCategorias.add(categoriaList.get(i).getIdCategoria()+" -> "+ categoriaList.get(i).getNameCategoria());
            }
        }catch (Exception e){

        }
        return listaCategorias;
    }

    public List<Dto> mostrarArticulos() {
        SQLiteDatabase bd = this.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM articulos order by codigo desc", null);
        List<Dto> articulos = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                articulos.add(new Dto(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getInt(3)));
                Log.e("articulo",articulos.toString() );
            }while (cursor.moveToNext());
        }
        return articulos;

    }
}
