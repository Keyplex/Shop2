package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PageBD extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear, btnPageMain, btnPageBD;
    EditText etMarka, etPrice;
    DBHelper dbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_bd);

        btnAdd = (Button) findViewById(R.id.Add);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);

        btnPageMain = (Button) findViewById(R.id.PageMain);
        btnPageMain.setOnClickListener(this);

        btnPageBD = (Button) findViewById(R.id.PageBD);
        btnPageBD.setOnClickListener(this);

        etMarka = (EditText) findViewById(R.id.Marka);
        etPrice = (EditText) findViewById(R.id.Price);

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
            TableLayout tb2 = findViewById(R.id.tableLayout4);
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

                Button DELETE = new Button(this);
                DELETE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View outRow = (View) view.getParent();
                        ViewGroup outBD = (ViewGroup) outRow.getParent();

                        outBD.removeView(outRow);
                        outBD.invalidate();

                        database.delete(DBHelper.TABLE_AVTOMOBIL, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf(view.getId())});
                        contentValues = new ContentValues();
                        Cursor cursorupd1 = database.query(DBHelper.TABLE_AVTOMOBIL, null,null,null, null, null, null);

                        if (cursorupd1.moveToFirst())
                        {
                            int id = cursorupd1.getColumnIndex(DBHelper.KEY_ID);
                            int nazvanie = cursorupd1.getColumnIndex(DBHelper.KEY_MARKA);
                            int price = cursorupd1.getColumnIndex(DBHelper.KEY_PRICE);
                            int realid = 1;
                            do {
                                if (cursorupd1.getInt(id) > realid)
                                {
                                    contentValues.put(DBHelper.KEY_ID, realid);
                                    contentValues.put(DBHelper.KEY_MARKA, cursorupd1.getString(nazvanie));
                                    contentValues.put(DBHelper.KEY_PRICE, cursorupd1.getString(price));
                                    database.replace(DBHelper.TABLE_AVTOMOBIL,null,contentValues);
                                }
                                realid++;
                            }
                            while (cursorupd1.moveToNext());
                            if (cursorupd1.moveToLast() && view.getId() != realid)
                            {
                                database.delete(DBHelper.TABLE_AVTOMOBIL,DBHelper.KEY_ID + " = ?", new String[]{cursorupd1.getString(id)});
                            }
                            UpdateTable();
                        }
                    }
                });
                params.weight = 1f;
                DELETE.setLayoutParams(params);
                DELETE.setText("Удалить запись");
                DELETE.setId(cursor.getInt(idIndex));
                tbOUT.addView(DELETE);
                tb2.addView(tbOUT);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        dbHelper = new DBHelper(this);
        String marka = etMarka.getText().toString();
        String price = etPrice.getText().toString();
        contentValues = new ContentValues();
        switch (v.getId()) {

            case R.id.Add:
                contentValues.put(DBHelper.KEY_MARKA, marka);
                contentValues.put(DBHelper.KEY_PRICE, price);
                database.insert(DBHelper.TABLE_AVTOMOBIL, null, contentValues);
                UpdateTable();
                etMarka.setText(null);
                etPrice.setText(null);
                break;

            case R.id.Clear:
                database.delete(DBHelper.TABLE_AVTOMOBIL, null, null);
                TableLayout dbOutput = findViewById(R.id.tableLayout4);
                dbOutput.removeAllViews();
                etMarka.setText(null);
                etPrice.setText(null);
                UpdateTable();
                break;

            case R.id.PageMain:
                Intent intent1 = new Intent(this, PageMagazin.class);
                startActivity(intent1);
                break;
            case R.id.PageBD:
                break;
        }
        dbHelper.close();
    }
}
