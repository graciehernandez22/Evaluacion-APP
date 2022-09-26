package com.fjar.app_crudsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_detalles_articulos extends AppCompatActivity {
    private TextView tv_codigo, tv_descripcion, tv_precio;
    private TextView tv_codigo1, tv_descripcion1, tv_precio1, tv_fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_articulos);

        tv_codigo = (TextView) findViewById(R.id.tv_codigo);
        tv_descripcion = (TextView) findViewById(R.id.tv_descripcion);
        tv_precio = (TextView) findViewById(R.id.tv_precio);

        tv_codigo1 = (TextView) findViewById(R.id.tv_codigo1);
        tv_descripcion1 = (TextView) findViewById(R.id.tv_descripcion1);
        tv_precio1 = (TextView) findViewById(R.id.tv_precio1);
        tv_fecha = (TextView) findViewById(R.id.tv_fecha);

        Bundle objeto = getIntent().getExtras();
        Dto dato = null;
        if(objeto != null){
            dato = (Dto) objeto.getSerializable("articulo");
            tv_codigo.setText("" + dato.getCodigo());
            tv_descripcion.setText(dato.getDescripcion());
            tv_precio.setText(String.valueOf(dato.getPrecio()));

            tv_codigo1.setText("" + dato.getCodigo());
            tv_descripcion1.setText(dato.getDescripcion());
            tv_precio1.setText(String.valueOf(dato.getPrecio()));
            tv_fecha.setText("Fecha de creación" + getDateTime());
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.getDefault() );
        Date date = new Date();
        return dateFormat.format(date);
    }
}