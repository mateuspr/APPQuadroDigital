package com.parse.starter.util;

import java.util.HashMap;

public class ParseErros {
    private HashMap<Integer,String> erros;
    public ParseErros(){
        this.erros = new HashMap<>();
        this.erros.put(201,"Senha não preenchida!");
        this.erros.put(202,"Usuário já cadastrado!, escolha outro nome de usuário");
        this.erros.put(125,"Endereço de e-mail inválido!");

    }

    public String getErro(int codErro){
        return  this.erros.get(codErro);
    }
}
