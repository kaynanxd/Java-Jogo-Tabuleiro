// ModoNormal.java (Crie este novo arquivo)
package com.example.jogo1;

public class ModoNormal extends ModoJogo {
    @Override
    public Dados.ModoJogo getModoJogo() {
        return Dados.ModoJogo.NORMAL; // Correto: Retorna o enum NORMAL de Dados
    }

    @Override
    public void realizarJogada(Jogador jogadorAtual, JogoTabuleiro jogoTabuleiro) {
        // No modo NORMAL, o jogador simplesmente rola os dados.
        jogadorAtual.dados.rolarDados(); // Chama o método rolarDados() da instância Dados do jogador
    }
}
