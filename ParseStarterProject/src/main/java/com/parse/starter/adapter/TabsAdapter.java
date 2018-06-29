package com.parse.starter.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.ViewGroup;

import com.parse.starter.R;
import com.parse.starter.fragments.HomeFragment;
import com.parse.starter.fragments.UsuariosFragment;

import java.util.HashMap;

public class TabsAdapter extends FragmentStatePagerAdapter {

    private Context context;
    //private String[] abas = new String[]{"HOME","USUARIOS"};
    private int[] icones = new int[]{R.drawable.ic_action_home,R.drawable.ic_people};
    private int tamanhoIcone;
    private HashMap<Integer,Fragment> fragmentosUtilizados;



    public TabsAdapter(FragmentManager fm,Context c) {
        super(fm);
        this.context = c;
        this.fragmentosUtilizados = new HashMap<>();
        //CALCULAR O TAMANHO DO ICONE DE ACORDO COM A RESOLUÇÃO
        double escala = this.context.getResources().getDisplayMetrics().density;
        tamanhoIcone = (int) (36*escala);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new HomeFragment();
                fragmentosUtilizados.put(position,fragment);
                break;
            case 1:
                fragment = new UsuariosFragment();
                //fragmentosUtilizados.put(position,fragment);
                break;

        }
        return fragment;
    }


    public void destroyItem(ViewGroup container,int position,Object object ){
        super.destroyItem(container,position,object);
        fragmentosUtilizados.remove(position);
    }

    public Fragment getFragment (Integer indice){
        return  fragmentosUtilizados.get(indice);
    }

    @Override
    public int getCount() {
        return icones.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //RECUPERAR O ICONE DE ACORDO COM A POSIÇÃO
        Drawable drawable = ContextCompat.getDrawable(this.context,icones[position]);
        //DESENHAR O DRAWABLE
        drawable.setBounds(0,0,tamanhoIcone,tamanhoIcone);

        //PERMITE COLOCAR UMA IMAGEM DENTRO DE UM TEXTO
        ImageSpan imageSpan = new ImageSpan(drawable);

        //Classe utilizado para retornar  CharSequence
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//somente imagem/pode mesclar com texto

        return  spannableString;

    }
}
