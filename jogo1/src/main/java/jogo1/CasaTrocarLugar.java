package com.example.jogo1;
import java.util.List;

public class CasaTrocarLugar extends Casas {
    private Jogador menorPosJogador;
    private int menorPosicao;

    public CasaTrocarLugar() {
        super("Trocar lugar");
    }

    public void acao(Jogador jogadorAtual, List<Jogador> jogadores) {
        menorPosJogador = null;
        menorPosicao = Integer.MAX_VALUE;
        
        for (Jogador j : jogadores) {
            if (j != jogadorAtual && j.getPosicaoAtual() < jogadorAtual.getPosicaoAtual()) {
                if (j.getPosicaoAtual() < menorPosicao) {
                    menorPosicao = j.getPosicaoAtual();
                    menorPosJogador = j;
                }
            }
        }

        if (menorPosJogador != null) {
            int posTemp = jogadorAtual.getPosicaoAtual();
            jogadorAtual.setPosicaoAtual(menorPosJogador.getPosicaoAtual());
            menorPosJogador.setPosicaoAtual(posTemp);
            PopupManager.showInformationPopup("Troca de Posição", jogadorAtual.getNomeJogador() + " trocou de lugar com " + menorPosJogador.getNomeJogador());
        } else {
            PopupManager.showInformationPopup("Troca não realizada", "Nenhum jogador está atrás de " + jogadorAtual.getNomeJogador() + "\n\nVocê permanece na casa " + jogadorAtual.getPosicaoAtual());
        }
    }
}