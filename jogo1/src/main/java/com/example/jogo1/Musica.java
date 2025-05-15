package com.example.jogo1;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaException;
import java.io.File;

public class Musica {
    private final MediaPlayer playerMusica;
    private MediaPlayer playerEfeitoAtual;

    public Musica(String caminhoMusica) {
        Media media = criarMediaSeguro(caminhoMusica);
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
        try {
            if (playerEfeitoAtual != null) {
                playerEfeitoAtual.stop();
            }

            Media efeito = criarMedia(caminhoEfeito);
            playerEfeitoAtual = new MediaPlayer(efeito);
            playerEfeitoAtual.setOnError(() ->
                    System.err.println("Erro no efeito: " + playerEfeitoAtual.getError().getMessage()));
            playerEfeitoAtual.play();
        } catch (MediaException e) {
            System.err.println("Falha ao carregar efeito sonoro: " + e.getMessage());
        }
    }

    private Media criarMedia(String caminho) throws MediaException {
        String caminhoCompleto = new File(caminho).toURI().toString();
        return new Media(caminhoCompleto);
    }

    private Media criarMediaSeguro(String caminho) {
        try {
            return criarMedia(caminho);
        } catch (MediaException e) {
            System.err.println("Erro ao carregar arquivo de música: " + e.getMessage());
            return null;
        }
    }
}