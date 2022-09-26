package com.fjar.app_crudsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity_list_view_articulos extends AppCompatActivity {
    private ListView listViewPersonas;
    private ArrayAdapter adaptador;
    private SearchView searchView;
    private ListView listView;
    private ArrayList<String> list;
    ArrayAdapter adapter;
    String[] version = {"Aestro", "Blender", "CupCake", "Donut", "Eclair", "Froyo", "GingerBread", "HoneyComb", "IceCream Sandwich",
            "Jelly Bean", "KitKat", "Lolipop", "Marshmallow", "Nought", "Oreo"};
    private ConexionSQLite conexion = new ConexionSQLite(this);
    private Dto datos = new Dto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_articulos);

        listViewPersonas = (ListView) findViewById(R.id.listViewPersonas);
        searchView =(SearchView) findViewById(R.id.searchView);

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, conexion.consultaListaArticulos1());
        listViewPersonas.setAdapter(adaptador);
        /*
        list = new ArrayList<>();
        for (int i = 0; i < version.length; i++){
            list.add(version[i]);
        }
        adapter = new ArrayAdapter(Activity_list_view_articulos.this, android.R.layout.simple_list_item_1, list);
        listViewPersonas.setAdapter(adapter);
        */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                /*
                if(conexion.consultaListaArticulos1().contain(s)){
                    if (list.contains(s)){
                        adapter.getFilter().filter(s);
                    }
                    return true;
                }
                */
                String text = s;
                adaptador.getFilter().filter(text);
                return false;
            }
        });

        listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String informacion = "Código: " + conexion.consultaListaArticulos().get(position).getCodigo() + "\n";
                informacion += "Descripción: " + conexion.consultaListaArticulos().get(position).getDescripcion() + "\n";
                informacion += "Precio: " + conexion.consultaListaArticulos().get(position).getPrecio() + "\n";

                Dto articulos = conexion.consultaListaArticulos().get(position);
                Intent intent = new Intent(Activity_list_view_articulos.this, Activity_detalles_articulos.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("articulo", (Serializable) articulos);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}