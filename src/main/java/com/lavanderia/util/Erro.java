package com.lavanderia.util;

import java.util.HashMap;

public class Erro {

    private HashMap<String, String> erros;

    public Erro() {
        this.erros = new HashMap();
    }

    public HashMap<String, String> getMensagens() {
        return this.erros;
    }

    public void add(String atributo, String mensagemErro) {
        if (this.erros.containsKey(atributo)) {
            this.erros.put(atributo, this.erros.get(atributo) + "<br /><span class=\"error\">" + mensagemErro + "</span>");
        } else {
            this.erros.put(atributo, "<span class=\"error\">" + mensagemErro + "</span>");
        }
    }

    public boolean issetAttribute(String attribute) {
        return this.erros.containsKey(attribute);
    }
}
