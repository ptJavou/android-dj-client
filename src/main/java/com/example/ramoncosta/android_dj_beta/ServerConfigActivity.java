package com.example.ramoncosta.android_dj_beta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerConfigActivity extends AppCompatActivity {

    private EditText serverIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.serverIp = (EditText) findViewById(R.id.serverIp);
    }

    /**
     * Salva o endereço ip especificado pelo usuário em um arquivo de texto.
     * @param v
     */
    public void saveServerIp(View v){
        FileOutputStream fileStream = null;
        PrintWriter pw = null;
        try{
           fileStream = openFileOutput("serverIp.txt", MODE_PRIVATE);
           pw = new PrintWriter(fileStream, true);
            pw.println(serverIp.getText().toString());

            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
            serverIp.setText(" ");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally{
            try{
                fileStream.close();
                pw.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
