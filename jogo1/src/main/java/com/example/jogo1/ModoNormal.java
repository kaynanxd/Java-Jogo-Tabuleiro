// ModoNormal.java (Crie este novo arquivo)
package com.example.jogo1;

public class ModoNormal extends ModoJogo {
    @Override
    public Dados.ModoJogo getModoJogo() {
        return Dados.ModoJogo.NORMAL; 
    }

    @Override
    public void realizarJogada(Jogador jogadorAtual, JogoTabuleiro jogoTabuleiro) {
        jogadorAtual.dados.rolarDados();
    }
}
