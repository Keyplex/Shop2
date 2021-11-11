package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PageBD extends AppCompatActivity implements View.OnClickListener {

    Button btnArrange, btnPageMain, btnPageBD;
    TextView sumshopBag;
    DBHelper dbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_bd);


        btnArrange = (Button) findViewById(R.id.Arrange);
        btnArrange.setOnClickListener(this);

        btnPageMain = (Button) findViewById(R.id.PageMain);
        btnPageMain.setOnClickListener(this);

        btnPageBD = (Button) findViewById(R.id.PageBD);
        btnPageBD.setOnClickListener(this);

        sumshopBag = (TextView) findViewById(R.id.sumshopBag);

        dbHelper = new DBHelper(this);
        database= dbHelper.getWritableDatabase();

        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_AVTOMOBIL, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int markaIndex = cursor.getColumnIndex(DBHelper.KEY_MARKA);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout tb2 = findViewById(R.id.tableLayout2);
            tb2.removeAllViews();
            do {
                TableRow tbOUT = new TableRow(this);
                tbOUT.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView ID = new TextView(this);
                params.weight = 1.0f;
                ID.setLayoutParams(params);
                ID.setText(cursor.getString(idIndex));
                tbOUT.addView(ID);

                TextView MARKA = new TextView(this);
                params.weight = 1.0f;
                MARKA.setLayoutParams(params);
                MARKA.setText(cursor.getString(markaIndex));
                tbOUT.addView(MARKA);

                TextView PRICE = new TextView(this);
                params.weight = 1.0f;
                PRICE.setLayoutParams(params);
                PRICE.setText(cursor.getString(priceIndex));
                tbOUT.addView(PRICE);

                Button BUY = new Button(this);
                BUY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View outRow = (View) view.getParent();
                        ViewGroup outBD = (ViewGroup) outRow.getParent();
                        outBD.removeView(outRow);
                        outBD.invalidate();
                        //String k = "id = ?";
                        Cursor cursorsum = database.query(DBHelper.TABLE_AVTOMOBIL, null, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf(view.getId())}, null, null, null);
                        double sum = Float.valueOf(sumshopBag.getText().toString());
                        double sum2 = 0;
                        if (cursorsum != null)
                        {
                            Log.d("mLog","Курсор фулл ");
                            if (cursorsum.moveToFirst())
                            {
                                int Price = cursorsum.getColumnIndex(DBHelper.KEY_PRICE);
                                do {
                                    sum2 = cursorsum.getDouble(Price);
                                    Log.d("mLog", " "+sum2);
                                }
                                while (cursorsum.moveToNext());
                                UpdateTable();
                            }
                            cursorsum.close();
                        }
                        else
                            Log.d("mLog", "Курсор равен нулю ");
                        sum = sum + sum2;
                        sumshopBag.setText(String.valueOf(sum));
                    }
                });
                params.weight = 1f;
                BUY.setLayoutParams(params);
                BUY.setText("Купить товар ");
                BUY.setId(cursor.getInt(idIndex));
                tbOUT.addView(BUY);
                tb2.addView(tbOUT);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {
        dbHelper = new DBHelper(this);
        contentValues = new ContentValues();
        switch (v.getId()) {


            case R.id.Arrange:
                Toast toast = Toast.makeText(getApplicationContext(),"Сумма заказа = "+ sumshopBag.getText(),Toast.LENGTH_SHORT);
                toast.show();
                sumshopBag.setText(" 0");
                break;
            case R.id.PageMain:
                break;
            case R.id.PageBD:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        dbHelper.close();
    }
}