package com.example.jogo1;


public abstract class ModoJogo {
	public abstract Dados.ModoJogo getModoJogo();

	public abstract void realizarJogada(Jogador jogadorAtual, JogoTabuleiro jogoTabuleiro);
}