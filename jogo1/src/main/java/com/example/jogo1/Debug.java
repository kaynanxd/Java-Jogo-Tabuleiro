// Debug.java
package com.example.jogo1;

public class Debug extends ModoJogo {
    @Override
    public Dados.ModoJogo getModoJogo() {
        return Dados.ModoJogo.DEBUG; 
    }

    @Override
    public void realizarJogada(Jogador jogadorAtual, JogoTabuleiro jogoTabuleiro) {
        jogoTabuleiro.obterValoresDadosDebug(jogadorAtual);
    }
}
