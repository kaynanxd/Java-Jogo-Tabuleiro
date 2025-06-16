package com.example.jogo1;
import java.util.List;
import java.util.Random;

public class CasaTrocarTipo extends Casas {
    public CasaTrocarTipo() {
        super("Trocar Tipo");
    }

    public void acao(Jogador jogador, List<Jogador> jogadores) {
        String tipoJogadorAtual = jogador.getTipoJogador();
        String[] tipos = {"Sortudo", "Azarado", "Normal"};
        String novoTipoJogador = tipoJogadorAtual;

        while (novoTipoJogador.equals(tipoJogadorAtual)) {
            novoTipoJogador = tipos[new Random().nextInt(tipos.length)];
        }

        jogador.setTipoJogador(novoTipoJogador);
        PopupManager.showInformationPopup("troca tipo",  jogador.getNomeJogador() + " trocou de tipo: " + tipoJogadorAtual + " -> " + novoTipoJogador);

    }
}