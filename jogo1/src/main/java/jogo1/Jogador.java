package com.example.jogo1;
import javafx.scene.paint.Color;
public class Jogador {
    protected String nomeJogador;
    protected String tipoJogador;
    protected int posicaoAtual;
    protected Dados dados;
    protected boolean jogadorParalisado = false;
    protected int rodadasParalisado = 0;
    protected int casasAndadas;
    protected Tabuleiro tabuleiro;
    protected Color cor;

    public Jogador(String nomeJogador, String tipoJogador, Tabuleiro tabuleiro) {
        this.nomeJogador = nomeJogador;
        this.tipoJogador = tipoJogador;
        this.posicaoAtual = 0;
        this.casasAndadas = 0;
        this.dados = new Dados();
        this.tabuleiro = tabuleiro;
    }

    public String getCorHex() {
        return String.format("#%02X%02X%02X",
                (int) (cor.getRed() * 255),
                (int) (cor.getGreen() * 255),
                (int) (cor.getBlue() * 255));
    }

    public Color getCor() {return cor;}

    public void setCor(Color cor) {this.cor = cor;}

    public String getNomeJogador() {
        return nomeJogador;
    }

    public String getTipoJogador() {
        return tipoJogador;
    }

    public void setTipoJogador(String tipo) {
        this.tipoJogador = tipo;
    }

    public int getPosicaoAtual() {
        return posicaoAtual;
    }

    public void setPosicaoAtual(int posicao) {
        this.posicaoAtual = posicao;
    }

    public void setRodadasParaliso(int rodadas) {
        this.rodadasParalisado = rodadas;
        this.jogadorParalisado = rodadas > 0;
    }
}