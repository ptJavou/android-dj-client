package com.example.ramoncosta.android_dj_beta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Cria uma nova thread para enviar a mensagem para o servidor via socket
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                String action = intent.getAction();
                String type = intent.getType();
                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type)) {
                        handleSendText(intent); // Handle text being sent
                    } else {
                        // Handle other intents, such as being started from the home screen
                    }
                }

            }
        }).start();
    }

    /**
     * Recebe o link via intent do app do youtube
     * @param intent intent com o link do video compartilhado
     */
    public void handleSendText(final Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (sharedText != null) {
            try{
                String serverIp = getServerIp();
                try (Socket socket = new SocketClient(serverIp).getSocket()) {
                    try(PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)){
                        writer.println(sharedText);
                    }
                }
            }catch (IOException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Endereço ip não especificado!", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }finally{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Enviado!", Toast.LENGTH_LONG).show();
                    }
                });
                finish();
            }
        }
    }

    /**
     * Lê o arquivo que contém o endereço ip do servidor.
     * @return o endereço do servidor
     * @throws IOException caso o arquivo não for encontrado.
     */
    private String getServerIp() throws IOException{
        try(FileInputStream fileInputStream = openFileInput("serverIp.txt")){
            try(Scanner input = new Scanner(fileInputStream);){
                StringBuilder serverIp = new StringBuilder();
                while(input.hasNextLine()) {
                    String line = input.nextLine();
                    serverIp.append(line);
                }
                return serverIp.toString();
            }
        }
    }

    /**
     * exibe a opção de configuração na barra de menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Redireciona para a activity com base na opção selecionada no menu.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, ServerConfigActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
