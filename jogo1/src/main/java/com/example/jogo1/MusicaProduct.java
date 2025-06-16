package com.example.jogo1;


import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import java.io.File;

public class MusicaProduct {
	private MediaPlayer playerEfeitoAtual;

	public void tocarEfeito(String caminhoEfeito) {
		try {
			playerEfeitoAtual(caminhoEfeito);
		} catch (MediaException e) {
			System.err.println("Falha ao carregar efeito sonoro: " + e.getMessage());
		}
	}

	private void playerEfeitoAtual(String caminhoEfeito) throws MediaException {
		if (playerEfeitoAtual != null) {
			playerEfeitoAtual.stop();
		}
		Media efeito = criarMedia(caminhoEfeito);
		playerEfeitoAtual = new MediaPlayer(efeito);
		playerEfeitoAtual
				.setOnError(() -> System.err.println("Erro no efeito: " + playerEfeitoAtual.getError().getMessage()));
		playerEfeitoAtual.play();
	}

	public Media criarMedia(String caminho) throws MediaException {
		String caminhoCompleto = new File(caminho).toURI().toString();
		return new Media(caminhoCompleto);
	}

	public Media criarMediaSeguro(String caminho) {
		try {
			return criarMedia(caminho);
		} catch (MediaException e) {
			System.err.println("Erro ao carregar arquivo de música: " + e.getMessage());
			return null;
		}
	}
}