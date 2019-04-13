package com.duckyappstudio.visualizer2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import org.mariuszgromada.math.mxparser.*;

public class MathActivity extends AppCompatActivity implements View.OnClickListener {

    Double precision;
    Double intervalA;
    Double intervalB;
    GraphView graph;
    String mode;
    String function;
    LineGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> intervaloA;
    LineGraphSeries<DataPoint> intervaloB;
    LineGraphSeries<DataPoint> linea;
    LineGraphSeries<DataPoint> linea2;
    LineGraphSeries<DataPoint> lineaTangent;

    Function f;
    Expression ex;


    Button bIterate;

    int counter = 0;
    double a,b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secant);

        Intent intent = getIntent();
        precision = intent.getDoubleExtra("precision", 0.1);
        if (precision<=0) precision = 0.1;
        intervalA = intent.getDoubleExtra("intervalA", -5);
        if (intervalA == -5005.34532) intervalA = -5.0;
        intervalB = intent.getDoubleExtra("intervalB", 5);
        if (intervalB == -5005.34532) intervalB = 5.0;
        mode = intent.getStringExtra("mode");
        function = intent.getStringExtra ("function");
        f = new Function(function);
        StringBuilder sb = new StringBuilder(function);
        for (int i=0; i<7; ++i){
            sb.deleteCharAt(0);
        }

        String resultString = sb.toString();
        Log.d("EXPRESION", resultString);
        ex = new Expression(resultString);


        bIterate = findViewById(R.id.iterate);
        graph = (GraphView) findViewById(R.id.graph);


        add_zooming_scrolling();

        create_graph(intervalA, intervalB);

        graph.addSeries(intervaloA);
        intervaloA.setColor(R.color.colorAccent);
        if (!mode.equals("tangent"))graph.addSeries(intervaloB);
        intervaloB.setColor(R.color.colorAccent);
        graph.addSeries(series);


        bIterate.setOnClickListener(MathActivity.this);
        a = intervalA;
        b = intervalB;
    }

    private void add_zooming_scrolling() {
        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
    }

    private void create_graph(Double intervalA, Double intervalB) {



        double y,x;
        x = intervalA - 5.0;

        series = new LineGraphSeries<DataPoint>();
        for(int i =0; i<2*(intervalB*10-intervalA*10)+50; i++) {
            x = x + 0.1;
            y = f.calculate(x);
            series.appendData(new DataPoint(x, y), true, 50000);
        }


         intervaloA = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(intervalA, -100),
                new DataPoint(intervalA, 100),
        });

            intervaloB = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(intervalB, -100),
                    new DataPoint(intervalB, 100),
            });


    }

    @Override
    public void onClick(View v) {
        ++counter;
        if (mode.equals("MathActivity"))
        secant_method();

        else if (mode.equals("bisection")) bisection_method();

        else tangent_method();
    }

    private void tangent_method() {
        double fa = f.calculate(a);
        Log.d("sedsdvsdv = ", "svdsdv");

        Expression pendiente  = new Expression("der(" + ex.getExpressionString() + ", x, " + String.valueOf(a) + ")" );

        Log.d("EXPRESION",  ex.getExpressionString());
        Log.d ("DERIVADA",  pendiente.getExpressionString());
        Log.d ("PENDIENTE" , String.valueOf(pendiente.calculate()));
        b = a - (fa/pendiente.calculate());
        draw_perp_line(b);
        if (a<=b)
        lineaTangent = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(a, fa),
                new DataPoint(b, 0),
        });

        else
            lineaTangent = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(b, 0),
                    new DataPoint(a, fa),
            });
        graph.addSeries(lineaTangent);
        lineaTangent.setColor(R.color.colorPrimary);

        Toast.makeText(this, "Iteration " + Integer.toString(counter) + ". Estimated root: " + Double.toString(b), Toast.LENGTH_LONG).show();
        if ((Math.abs(b-a)<= precision)) startResultActivity(b);

        a = b;
    }

    private void bisection_method() {
        double fa = f.calculate(a);

        //SET C;
        double x = (a+b)/2;
        if ((fa*f.calculate(x))< 0){
            this.b = x;
            draw_perp_line(x);
        }
        else {
            this.a = x;
            draw_perp_line(x);
        }
        Toast.makeText(this, "Iteration " + Integer.toString(counter) + ". Estimated root: " + Double.toString(x), Toast.LENGTH_LONG).show();
        if ((Math.abs(b-a)<= precision)) startResultActivity(x);
    }

    private void secant_method() {
        double fa = f.calculate(a);
        double fb = f.calculate(b);
        double m = (fb-fa)/(b-a);

        linea = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(a, fa),
                new DataPoint(b, fb),
        });
        graph.addSeries(linea);
        linea.setColor(R.color.colorPrimary);

        //SET C;
        double x = ((-fa + m*a)/m);
        if ((fa*f.calculate(x))< 0){
            this.b = x;
            draw_perp_line(x);
        }
        else {
            this.a = x;
            draw_perp_line(x);
        }

        Toast.makeText(this, "Iteration " + Integer.toString(counter) + ". Estimated root: " + Double.toString(x), Toast.LENGTH_LONG).show();
        if (((f.calculate(x)) <= precision) && (Math.abs(b-a)<= precision)) startResultActivity(x);

    }


    private void startResultActivity(double x) {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("precision", precision);
        i.putExtra("resultado", x);
        startActivity(i);
        finish();
    }

    private void draw_perp_line(double b) {
        //draw nxt perp line
        linea2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(b, -100),
                new DataPoint(b, 100),
        });
        graph.addSeries(linea2);
        linea2.setColor(R.color.colorAccent);
    }
}
