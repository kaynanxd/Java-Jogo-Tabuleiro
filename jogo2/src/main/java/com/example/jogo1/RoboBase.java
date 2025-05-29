package com.example.jogo1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class RoboBase {
    protected int x;
    protected int y;
    protected String cor;
    protected Random random;
    protected boolean roboParalisado = false;
    protected List<int[]> posicoesHistorico;
    protected int numMovimentosInvalidos = 0;
    protected int numMovimentosValidos = 0;

    protected RoboBase(String cor) {
        this.x = 0;
        this.y = 0;
        this.cor = cor;
        this.random = new Random();
        this.posicoesHistorico = new ArrayList<>();
        salvarPosicaoAtual(); // salva posição inicial
    }

    protected void salvarPosicaoAtual() {
        posicoesHistorico.add(new int[]{x, y});
    }

    protected void mover(int sentido) throws MovimentoInvalidoException {
        if (roboParalisado) {
            System.out.println("O robô " + cor + " foi explodido!");
            return;
        }

        int novoX = x, novoY = y;

        switch (sentido) {
            case 1 -> novoY--;
            case 2 -> novoY++;
            case 3 -> novoX++;
            case 4 -> novoX--;
            default -> throw new MovimentoInvalidoException("Direção inválida: " + sentido);
        }

        if (novoX < 0 || novoY < 0 || novoX > 4 || novoY > 4) {
            numMovimentosInvalidos++;
            throw new MovimentoInvalidoException(
                    "Movimento inválido! Posição resultante (" + novoX + ", " + novoY + ") fora do tabuleiro.");
        }

        numMovimentosValidos++;
        x = novoX;
        y = novoY;
        salvarPosicaoAtual();
        System.out.println(this);
    }

    public void explodir() {
        System.out.println("Robô " + cor + " explodiu e foi desativado!");
        roboParalisado = true;
    }

    public void voltarPosicaoAnterior() {
        if (posicoesHistorico.size() > 1) {
            posicoesHistorico.remove(posicoesHistorico.size() - 1);
            int[] posAnterior = posicoesHistorico.get(posicoesHistorico.size() - 1);
            this.x = posAnterior[0];
            this.y = posAnterior[1];
            System.out.println("Robô " + cor + " voltou para a posição (" + x + ", " + y + ")");
        }
    }

    protected int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }

    protected String getCor() {
        return cor;
    }

    protected boolean encontrouAlimento(int alimentoX, int alimentoY) {
        return x == alimentoX && y == alimentoY;
    }

    public boolean isAtivo() {
        return !roboParalisado;
    }

    public String toString() {
        return "Robo " + cor + " na posição (" + x + ", " + y + ")";
    }

    protected abstract void moverIA() throws MovimentoInvalidoException;
}
