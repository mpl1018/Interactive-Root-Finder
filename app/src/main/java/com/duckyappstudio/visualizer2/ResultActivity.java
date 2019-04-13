package com.duckyappstudio.visualizer2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.decimal4j.util.DoubleRounder;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvResult;
    Button bDeNuevo;
    Double precision;
    Double resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        precision = intent.getDoubleExtra("precision", 0.1);
        resultado = intent.getDoubleExtra("resultado", 0.1);




        tvResult = findViewById(R.id.tvresultado);
        bDeNuevo = findViewById(R.id.bDenuevo);

        //PONEMOS EL NUMERO DE DECIMALES CORRECTOS
        String text = Double.toString(Math.abs(precision));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;


        tvResult.setText(String.valueOf(DoubleRounder.round(resultado, decimalPlaces+1)) + " +- " + String.valueOf(precision));

        bDeNuevo.setOnClickListener(ResultActivity.this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
