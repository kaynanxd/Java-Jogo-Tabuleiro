package com.example.jogo1;
import java.util.List;

public class CasaParalisado extends Casas {
    
    public CasaParalisado() {
        super("Paralizado");
    }

    public void acao(Jogador jogador, List<Jogador> jogadores) {
        PopupManager.showWarningPopup(
                "⏸️ Ação Paralisada", "O jogador " + jogador.getNomeJogador() + " (" + jogador.getTipoJogador() + ")" +
                        "\n\nFoi paralisado e perderá a próxima rodada!");
        jogador.setRodadasParaliso(1);
    }
}