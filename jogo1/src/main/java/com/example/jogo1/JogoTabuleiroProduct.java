package com.example.jogo1;


import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class JogoTabuleiroProduct {
	private final List<Jogador> jogadores;
	private int indiceJogadorAtual = 0;

	public JogoTabuleiroProduct(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public int getIndiceJogadorAtual() {
		return indiceJogadorAtual;
	}

	public void avancarJogador(JogoTabuleiro jogoTabuleiro) {
		Jogador jogadorAtual = jogadores.get(indiceJogadorAtual);
		if (jogadorAtual.dados.saoDadosIguais()) {
			Platform.runLater(() -> {
				Alert alerta = new Alert(Alert.AlertType.INFORMATION);
				alerta.setTitle("Jogue Novamente!");
				alerta.setHeaderText(null);
				alerta.setContentText("Você tirou dois dados iguais! Que sorte! Jogue novamente.");
				alerta.showAndWait();
			});
			jogoTabuleiro.atualizarTabuleiroVisual();
			return;
		}
		indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
		jogoTabuleiro.atualizarTabuleiroVisual();
	}
}