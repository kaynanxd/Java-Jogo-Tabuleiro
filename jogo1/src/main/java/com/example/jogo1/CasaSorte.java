package com.example.jogo1;
import java.util.List;

public class CasaSorte extends Casas {
    public CasaSorte() {
        super("Sorte");
    }

    public void acao(Jogador jogador, List<Jogador> jogadores) {
        if (!jogador.tipoJogador.equals("Azarado")) {
            int posicaoSorte = jogador.getPosicaoAtual() + 3;
            jogador.setPosicaoAtual(posicaoSorte);
            PopupManager.showInformationPopup("Casa da Sorte",
                    jogador.getNomeJogador() + " esta com sorte e avançou 3 casas! Agora está na casa " + jogador.getPosicaoAtual());
        }
    }

}
