package com.example.jogo1;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaException;
import java.io.File;

public class Musica {
    private MusicaProduct musicaProduct = new MusicaProduct();
	private final MediaPlayer playerMusica;
    public Musica(String caminhoMusica) {
        Media media = musicaProduct.criarMediaSeguro(caminhoMusica);
        this.playerMusica = media != null ? new MediaPlayer(media) : null;

        if (this.playerMusica != null) {
            this.playerMusica.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    public void tocar() {
        if (playerMusica != null) {
            playerMusica.play();
        }
    }

    public void parar() {
        if (playerMusica != null) {
            playerMusica.stop();
        }
    }

    public boolean isMusicaTocando() {
        return playerMusica != null &&
                playerMusica.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void tocarEfeito(String caminhoEfeito) {
        musicaProduct.tocarEfeito(caminhoEfeito);
    }

	public void alternarMusica(Button botao) {
		if (isMusicaTocando()) {
			parar();
			botao.setStyle("-fx-background-radius: 20; -fx-background-color: red;");
		} else {
			tocar();
			botao.setStyle("-fx-background-radius: 20; -fx-background-color: green;");
		}
	}
}