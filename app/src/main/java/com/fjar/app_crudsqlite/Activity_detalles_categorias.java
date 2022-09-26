package com.fjar.app_crudsqlite;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_detalles_categorias extends AppCompatActivity {
    private TextView tv_id, tv_nombre, tv_estado, tv_fechaReg;
    private TextView tv_id1, tv_nombre1, tv_estado1, tv_fechaReg1, tv_fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_categorias);

        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_nombre = (TextView) findViewById(R.id.tv_name);
        tv_estado = (TextView) findViewById(R.id.tv_estado);
        tv_estado = (TextView) findViewById(R.id.tv_fechaReg);

        tv_id1 = (TextView) findViewById(R.id.tv_id1);
        tv_nombre1 = (TextView) findViewById(R.id.tv_nomnbre1);
        tv_estado1 = (TextView) findViewById(R.id.tv_estado1);
        tv_fechaReg1 = (TextView) findViewById(R.id.tv_fechaReg1);

        tv_fecha = (TextView) findViewById(R.id.tv_fecha);

        Bundle objeto = getIntent().getExtras();
        DtoCategoria dato = null;
        if(objeto != null){
            dato = (DtoCategoria) objeto.getSerializable("categoria");
            tv_id.setText(String.valueOf(dato.getIdCategoria()));
            tv_nombre.setText(dato.getNameCategoria());
            tv_estado.setText(String.valueOf(dato.getEstadoCategoria()));
            tv_fechaReg.setText(dato.getFecha().toString());

            tv_id1.setText(String.valueOf(dato.getIdCategoria()));
            tv_nombre1.setText(dato.getNameCategoria());
            tv_estado1.setText(String.valueOf(dato.getEstadoCategoria()));
            tv_fechaReg1.setText(dato.getFecha().toString());

            tv_fecha.setText("Fecha de creación" + getDateTime());
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.getDefault() );
        Date date = new Date();
        return dateFormat.format(date);
    }
}