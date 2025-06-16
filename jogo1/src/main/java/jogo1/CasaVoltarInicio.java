package com.example.jogo1;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CasaVoltarInicio extends Casas {
    private Scanner scanner;
    public CasaVoltarInicio() {
        super("Voltar inicio");
        this.scanner = new Scanner(System.in);
    }

    public void acao(Jogador jogador, List<Jogador> jogadores) {
        // Prepara as opções para o popup
        List<String> opcoes = new ArrayList<>();
        for (int i = 0; i < jogadores.size(); i++) {
            opcoes.add((i + 1) + ". " + jogadores.get(i).nomeJogador);
        }

        Platform.runLater(() -> {
            Optional<String> escolha = PopupManager.showChoicePopup(
                    "🏠 Casa Especial - Voltar ao Início",
                    "Selecione o jogador que voltará:",
                    opcoes
            );

            escolha.ifPresent(sel -> {
                // Processa a seleção
                int index = Integer.parseInt(sel.substring(0, sel.indexOf("."))) - 1;
                Jogador selecionado = jogadores.get(index);
                selecionado.setPosicaoAtual(0);
                PopupManager.showInformationPopup(
                        "✅ Jogador Retornou",
                        selecionado.nomeJogador + " voltou ao início!"
                );
            });
        });
    }

}
