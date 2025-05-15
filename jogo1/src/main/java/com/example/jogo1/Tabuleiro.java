package com.example.jogo1;
import java.util.List;

public class Tabuleiro {
    private Casas[] casas;
    private List<Jogador> jogadores;

    public Tabuleiro(List<Jogador> jogadores) {
        this.jogadores = jogadores;
        casas = new Casas[40];
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        int[] posicoesParalisado = { 10, 25, 38 };
        for (int i = 0; i < posicoesParalisado.length; i++) {
            casas[posicoesParalisado[i]] = new CasaParalisado();
        }

        casas[13] = new CasaTrocarTipo();

        int[] posicoesSorte = { 5, 15, 30 };
        for (int i = 0; i < posicoesSorte.length; i++) {
            casas[posicoesSorte[i]] = new CasaSorte();
        }

        int[] posicoesVoltarInicio = { 17, 27 };
        for (int i = 0; i < posicoesVoltarInicio.length; i++) {
            casas[posicoesVoltarInicio[i]] = new CasaVoltarInicio();
        }

        int[] posicoesTrocarLugar = { 20, 35 };
        for (int i = 0; i < posicoesTrocarLugar.length; i++) {
            casas[posicoesTrocarLugar[i]] = new CasaTrocarLugar();
        }

    }

    public void verificarCasaEspecial(Jogador jogador) {
        int posicao = jogador.getPosicaoAtual();
        if (casas[posicao] != null) {
            casas[posicao].acao(jogador, jogadores);
        }
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }
}