package com.example.kiemtragk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kiemtragk.SQLite.SQLite;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edtMSSV;
    EditText edtPass;
    Button btnLogin;
    String mMSSV = "";
    String mPass = "";
    SQLite sqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        sqLite = new SQLite( MainActivity.this );
        onInit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCheckNullEdt()){
                    Map<String,String> mMap = new HashMap<>();
                    mMap.put("user_name",mMSSV);
                    mMap.put("password",mPass);
                    new LoginAsyncTask(MainActivity.this,new IViewLogin() {
                        @Override
                        public void onLoginSuccess(String m) {
                            Toast.makeText(MainActivity.this,m,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,SubjectActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onLoginFail(String m) {
                            Toast.makeText(MainActivity.this,m,Toast.LENGTH_SHORT).show();
                        }
                    },mMap).execute("http://www.vidophp.tk/api/account/signin");
                }
            }
        });
    }

    private void onInit() {
        edtMSSV = findViewById( R.id.edtMSSV );
        edtPass = findViewById( R.id.edtPass );
        btnLogin = findViewById( R.id.btnLogin );
    }

    private boolean onCheckNullEdt(){
        mMSSV = edtMSSV.getText().toString();
        if (mMSSV.length() < 1){
            edtMSSV.setError("Username field cannot be blank");
            return false;
        }

        mPass = edtPass.getText().toString();
        if (mPass.length() < 1){
            edtPass.setError("Password field cannot be blank");
            return  false;
        }
        return true;
    }
}
