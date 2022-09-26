package com.fjar.app_crudsqlite;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class consultaRV extends AppCompatActivity {
    private RecyclerView recycler;
    private AdaptadorArticulos adaptadorArticulos;
    ConexionSQLite datos = new ConexionSQLite(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recycler = (RecyclerView) findViewById(R.id.rview);

        recycler.setHasFixedSize(true);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        Log.e("Separador", "------------------------------");
        Log.e("adaptadorartculos", datos.mostrarArticulos().toString());
        adaptadorArticulos = new AdaptadorArticulos(this, datos.mostrarArticulos());
        recycler.setAdapter(adaptadorArticulos);
    }
    public List<Dto> obtenerArticulos() {
        List<Dto> articulos = new ArrayList<>();
        articulos.add(new Dto(1, "", 0, 1));
        articulos.add(new Dto(2, "", 0, 1));
        articulos.add(new Dto(3, "", 0, 1));
        return articulos;
    }
}
