package com.parse.starter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.util.ParseErros;

public class LoginActivity extends AppCompatActivity {

    private EditText txtLoginUsuario;
    private EditText txtLoginSenha;
    private Button btnLoginEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLoginUsuario = (EditText) findViewById(R.id.txtLoginUsuario);
        txtLoginSenha = (EditText) findViewById(R.id.txtLoginSenha);
        btnLoginEntrar = (Button) findViewById(R.id.btnLoginEntrar);

        ParseUser.logOut();
        //Verificar se o usuário está logado
        verificarUsuarioLogado();

        btnLoginEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = txtLoginUsuario.getText().toString().toLowerCase();
                String senha = txtLoginSenha.getText().toString().toLowerCase();

                verificaLogin(usuario,senha);
            }
        });
    }

    private void verificaLogin(String usuario, String senha){
        ParseUser.logInInBackground(usuario, senha, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){//Sucesso ao salvar
                    Log.i("loginUsuario","Sucesso!");
                    Toast.makeText(LoginActivity.this,"Login realizado com sucesso!",Toast.LENGTH_SHORT).show();
                    abrirAreaPrincipal();

                }else{//Erro ao salvar
                    Log.i("loginUsuario","Erro!" + e.getCode());
                    ParseErros parseErros = new ParseErros();
                    String erro = parseErros.getErro(e.getCode());
                    if (erro == null){
                        Toast.makeText(LoginActivity.this,"Erro não cadastrado na tabela Hash!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this,erro,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public  void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this,CadastroActivity.class);
        startActivity(intent);
    }
    private void verificarUsuarioLogado(){

        if (ParseUser.getCurrentUser() != null){
            //Envia usuário para tela principal do APP
            abrirAreaPrincipal();
        }
    }

    private void abrirAreaPrincipal(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
