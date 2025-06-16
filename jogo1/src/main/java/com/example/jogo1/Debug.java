// Debug.java
package com.example.jogo1;

public class Debug extends ModoJogo {
    @Override
    public Dados.ModoJogo getModoJogo() {
        return Dados.ModoJogo.DEBUG; // Correto: Referencia o enum de Dados
    }

    @Override
    public void realizarJogada(Jogador jogadorAtual, JogoTabuleiro jogoTabuleiro) {
        // No modo DEBUG, o JogoTabuleiro é responsável por obter os valores dos dados do usuário.
        jogoTabuleiro.obterValoresDadosDebug(jogadorAtual);
    }
}