package com.parse.starter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.R;
import com.parse.starter.util.ParseErros;

public class CadastroActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtSenha;
    private EditText txtNome;
    private Button btnCadastrar;
    private TextView lblFacaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtNome = (EditText) findViewById(R.id.txtNome);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        btnCadastrar =(Button) findViewById(R.id.btnCadastrar);
        lblFacaLogin = (TextView) findViewById(R.id.lblFacaLogin);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });

        lblFacaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLoginUsuario();
            }
        });

    }
    private void cadastrarUsuario(){
        //Cria objeto usuario
        ParseUser usuario = new ParseUser();
        usuario.setUsername(txtUsuario.getText().toString().toLowerCase());
        usuario.setPassword(txtSenha.getText().toString());
        usuario.put("Nome",txtNome.getText().toString());
        //usuario.setEmail(txtEmail.getText().toString().toLowerCase());

        Log.i("cadastrarUsuario", "Usuario: " + txtUsuario.getText().toString().toLowerCase());
        Log.i("cadastrarUsuario", "Senha: "  + txtSenha.getText().toString());
        Log.i("cadastrarUsuario","Nome :" + txtNome.getText().toString());

        usuario.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){//Sucesso ao salvar
                    Log.i("cadastrarUsuario","Sucesso!");
                    Toast.makeText(CadastroActivity.this,"Salvo com sucesso!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CadastroActivity.this,LoginActivity.class);
                    startActivity(intent);
                    //abrirLoginUsuario();
                }else{//Erro ao salvar
                    Log.i("cadastrarUsuario","Erro!" + e.getCode());
                    ParseErros parseErros = new ParseErros();
                    String erro = parseErros.getErro(e.getCode());
                    if (erro == null){
                        Toast.makeText(CadastroActivity.this,"Erro não cadastrado na tabela Hash!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CadastroActivity.this,erro,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
