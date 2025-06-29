// Debug.java
package com.example.jogo1;

public class Debug extends ModoJogo {
    public Dados.ModoJogo getModoJogo() {
        return Dados.ModoJogo.DEBUG; 
    }
    public void realizarJogada(Jogador jogadorAtual, JogoTabuleiro jogoTabuleiro) {
        jogoTabuleiro.obterValoresDadosDebug(jogadorAtual);
    }
}
