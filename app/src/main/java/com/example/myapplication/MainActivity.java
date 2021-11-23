package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnEntry, btnRegister;
    EditText etLogin, etPassword, etAdminLogin, etAdminPassword;

    DBEntryHelper dbEntryHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEntry = (Button) findViewById(R.id.Entry);
        btnEntry.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.Register);
        btnRegister.setOnClickListener(this);

        etLogin = (EditText) findViewById(R.id.Login);
        etPassword = (EditText) findViewById(R.id.Password);

        dbEntryHelper = new DBEntryHelper(this);
        database= dbEntryHelper.getWritableDatabase();



        etAdminLogin = (EditText) findViewById(R.id.Login);
        etAdminPassword = (EditText) findViewById(R.id.Password);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Entry:
                Cursor logCursor = database.query(DBEntryHelper.TABLE_USERS, null,null,null, null, null, null);

                boolean logged = false;
                if(logCursor.moveToFirst())
                {
                    int LoginIndex = logCursor.getColumnIndex(DBEntryHelper.KEY_LOGIN);
                    int PasswordIndex = logCursor.getColumnIndex(DBEntryHelper.KEY_PASSWORD);
                    do{
                        if(etLogin.getText().toString().equals(logCursor.getString(LoginIndex)) && etPassword.getText().toString().equals(logCursor.getString(PasswordIndex)))
                        {
                            if(etLogin.getText().toString().equals("admin") && etPassword.getText().toString().equals("admin"))
                            {
                                Toast.makeText(this,"Вы вошли, как админ", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this,PageBD.class));
                                logged=true;
                                break;
                            }
                            else {
                                startActivity(new Intent(this, PageMagazin.class));
                                logged = true;
                                break;
                            }
                        }

                    }while(logCursor.moveToNext());
                }
                logCursor.close();
                if (!logged) Toast.makeText(this,"Введенная комбинация логина и пароля не была найдена", Toast.LENGTH_LONG).show();
                break;

            case R.id.Register:
                Cursor RegisterCursor = database.query(DBEntryHelper.TABLE_USERS, null,null,null, null, null, null);

                boolean finded = false;
                if(RegisterCursor.moveToFirst())
                {
                    int LoginIndex = RegisterCursor.getColumnIndex(DBEntryHelper.KEY_LOGIN);
                    do{
                        if(etLogin.getText().toString().equals(RegisterCursor.getString(LoginIndex)))
                        {
                            Toast.makeText(this,"Введенный логин уже зарегестрирован", Toast.LENGTH_LONG).show();
                            finded=true;
                            break;
                        }
                    }while(RegisterCursor.moveToNext());
                }
                if (!finded)
                {
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(DBEntryHelper.KEY_LOGIN, etLogin.getText().toString());
                    contentValues.put(DBEntryHelper.KEY_PASSWORD, etPassword.getText().toString());
                    database.replace(DBEntryHelper.TABLE_USERS,null,contentValues);
                    Toast.makeText(this,"Вы успешно зарегестрировались", Toast.LENGTH_LONG).show();
                }
                RegisterCursor.close();
                break;
        }
    }
}
