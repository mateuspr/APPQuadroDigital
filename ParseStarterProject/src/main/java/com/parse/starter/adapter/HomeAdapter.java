package com.parse.starter.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.parse.ParseObject;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends ArrayAdapter<ParseObject> {
    private Context context;
    private ArrayList<ParseObject> postagens;

    public HomeAdapter(Context c,ArrayList<ParseObject> objects){
        super(c,0,objects);
        this.context = c;
        this.postagens = objects;
    }

    public View getView(int position, View covertView , ViewGroup parent){
        View view = covertView;
        /*  Verifica se não existe o  objeto view criado
            pois a view utilizada é armazenado no cache do android  e fica na variavel convertView*/
        if (view == null){
            //Inicializa objeto para a montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta a View apartir do XML
            view = inflater.inflate(R.layout.lista_postagem, parent,false);
        }

        //Verifica se existe postagens
        if (postagens.size() > 0){
            //Recupera componentes da tela
            ImageView imagePostagem =(ImageView) view.findViewById(R.id.image_lista_postagem);

            ParseObject parseObject = postagens.get(position);

            //parseObject.get("NOME COLUNA") RECUPERA O CAMPO

            //PICASSO BAIXA IMAGEM E CRIA CACHE
            Picasso.get()
                    .load(parseObject.getParseFile("imagem").getUrl())
                    .into(imagePostagem);


        }

        return  view;
    }
}

