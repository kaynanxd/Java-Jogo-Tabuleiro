// MarcadorFactory.java (NOVO ARQUIVO)
package com.example.jogo1;

import javafx.scene.control.Label;

public class MarcadorFactory {

    private static final String ESTILO_MARCADOR = """
        -fx-background-color: %s;
        -fx-text-fill: black;
        -fx-font-weight: bold;
        -fx-padding: 4;
        -fx-min-width: 20px;
        -fx-alignment: center;
        -fx-border-radius: 3;
    """;

    public Label criarPara(Jogador jogador) {
        if (jogador == null) {
            return new Label();
        }
        Label marcador = new Label(jogador.getNomeJogador().substring(0, 1).toUpperCase());
        String estiloFinal = String.format(ESTILO_MARCADOR, jogador.getCorHex());
        marcador.setStyle(estiloFinal);
        return marcador;
    }
}