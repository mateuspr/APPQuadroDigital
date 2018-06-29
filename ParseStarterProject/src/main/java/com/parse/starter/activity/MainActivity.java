/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;//importado manualmente
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.starter.R;
import com.parse.starter.adapter.TabsAdapter;
import com.parse.starter.fragments.HomeFragment;
import com.parse.starter.util.SlidingTabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;


public class MainActivity extends AppCompatActivity {


  private Toolbar toolbarPrincipal;
    private ViewPager viewPager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //CONFIGURAR TOOLBAR
    toolbarPrincipal= (Toolbar) findViewById(R.id.toolbarPrincipal);
    setSupportActionBar(toolbarPrincipal);
    //toolbarPrincipal.setLogo(R.drawable.instagramlogo);

    //CONFIGURAR ABAS
      SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab_main);
    viewPager = (ViewPager) findViewById(R.id.view_pager_main);

    //CONFIGURANDO O ADAPTER
      TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(),this);
      viewPager.setAdapter(tabsAdapter);
      slidingTabLayout.setCustomTabView(R.layout.tabs_view,R.id.text_item_tab);
      slidingTabLayout.setDistributeEvenly(true); //Distrui as abas no layout inteiro
      slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.cinzaEscuro));//COR DA ABA SELECIONADA
      slidingTabLayout.setViewPager(viewPager);
  }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      //Verifica o objeto selecionado no menu
      switch (item.getItemId()){
          case R.id.action_sair:
              deslogarUsuario();
            return true;
          case R.id.action_configuracoes:
              return true;
          case R.id.action_compartilhar:
              compartilharFoto();
              return true;
          default:
              return super.onOptionsItemSelected(item);
      }
    }
    private void compartilharFoto(){
        //Abrir Galeria de foto
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Recupera a FOTO ESCOLHIDA
        startActivityForResult(intent,1);
    }

    @Override
    //Método após selecionar a foto (REQUEST CODE 1 )
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TESTA O PROCESSO DE RETORNO DE DADOS
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            //Recupera diretorio da imagem
            Uri localIMGSel = data.getData();


            try{
                //Recupera a imagem do local que foi selecionado
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localIMGSel);

                //Comprimir no formato PNG
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.PNG,75,stream); //IMAGEM -> depois de converter -> STREAM

                //Cria um arraY  de bytes da imagem
                byte[] byteArray = stream.toByteArray(); //converte o STREAM para ARRAY DE bytes formato utilizado pelo parse

                //Cria um arquivo com formato próprio do parse
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddmmaaaahhmm");
                String nomeIMG = dateFormat.format(new Date());
                ParseFile arquivoBase = new ParseFile(nomeIMG+"imagem.png",byteArray);

                //Cria a classe IMAGEM no Parse
                ParseObject parseObject = new ParseObject("Imagem"); //Cria a classe Imagem
                parseObject.put("username",ParseUser.getCurrentUser().getUsername()); //Coluna Username usuário logado
                parseObject.put("imagem",arquivoBase); //Coluna Imagem

                //Salvar objeto
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){//Sucesso
                            Toast.makeText(getApplicationContext(),"Sua imagem foi postada",Toast.LENGTH_LONG).show();

                            //Atualiza a listagem de itens do Fragmento Home Fragment
                            TabsAdapter adapterNovo=  (TabsAdapter) viewPager.getAdapter();
                            HomeFragment homeFragmentNovo = (HomeFragment) adapterNovo.getFragment(0);
                            homeFragmentNovo.atualizaPostagens();

                        }else{
                            Toast.makeText(getApplicationContext(),"Erro " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });




            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void deslogarUsuario(){
        ParseUser.logOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
