package com.duckyappstudio.visualizer2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mariuszgromada.math.mxparser.Function;
import org.mariuszgromada.math.mxparser.mXparser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bBisection;
    Button bSecant;
    Button bTangent;
    EditText etFuncion;
    EditText etIntervalA;
    EditText etIntervalB;
    EditText etPrecision;
    EditText etFunction;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bBisection = findViewById(R.id.bisection);
        bTangent = findViewById(R.id.tangent);
        bSecant = findViewById(R.id.secant);
       // etFuncion = findViewById(R.id.function);
        etIntervalA = findViewById(R.id.intervalA);
        etIntervalB = findViewById(R.id.intervalB);
        etPrecision = findViewById(R.id.precision);
        etFunction = findViewById(R.id.function);

        bBisection.setOnClickListener(MainActivity.this);
        bSecant.setOnClickListener(MainActivity.this);
        bTangent.setOnClickListener(MainActivity.this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bisection:
                mode = "bisection";
                start_activity();
                break;

            case R.id.secant:
                mode = "MathActivity";
                start_activity();
                break;

            case R.id.tangent:
                mode = "tangent";
                start_activity();
                break;
        }
    }

    private void start_activity() {
        Function f = new Function(String.valueOf(etFunction.getText()));
        f.checkSyntax();
        Toast.makeText(this, f.getErrorMessage(), Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, MathActivity.class);
        //pasar funcion, intervalo y precisión
        i.putExtra("function", (String.valueOf(etFunction.getText())));
        i.putExtra("precision", ParseDouble(String.valueOf(etPrecision.getText())));
        i.putExtra("intervalA", ParseDouble(String.valueOf(etIntervalA.getText())));
        i.putExtra("intervalB", ParseDouble(String.valueOf(etIntervalB.getText())));
        i.putExtra("mode", mode);
        startActivity(i);
        finish();
    }

    double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return -5005.34532;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else return 0;
    }
}
