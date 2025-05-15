package com.example.jogo1;
import java.util.List;

public abstract class Casas {
    protected String nomeCasa;

    public Casas (String nomeCasa) {
        this.nomeCasa = nomeCasa;
    }

    public String getNomeCasa() {
        return nomeCasa;
    }

    public abstract void acao(Jogador jogador, List<Jogador> jogadores);
}
