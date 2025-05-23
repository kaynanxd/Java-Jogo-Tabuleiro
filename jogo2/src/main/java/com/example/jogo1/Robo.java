package com.example.jogo1;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Robo {
    private int x;
    private int y;
    private String cor;
    private Random random;
    private boolean roboParalisado = false;
    protected List<int[]> posicoesHistorico;

    protected Robo(String cor) {
        this.x = 0;
        this.y = 0;
        this.cor = cor;
        this.random = new Random();
        this.posicoesHistorico = new ArrayList<>();
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

    protected void salvarPosicaoAtual() {
        posicoesHistorico.add(new int[]{x, y});
    }

    protected void mover(String sentido) throws MovimentoInvalidoException {
        if (roboParalisado) {
            System.out.print("O robô foi explodido!");
            return;
        }
        int novoX = x, novoY = y;
        switch (sentido.toLowerCase()) {
            case "up":
                y++;
                break;
            case "down":
                y--;
                break;
            case "right":
                x++;
                break;
            case "left":
                x--;
                break;
            default:
                System.out.println("Direção inválida. Posição atual: (" + x + ", " + y + ")");
        }

        if (x < 0 || y < 0) {
            throw new MovimentoInvalidoException(
                    "Movimento inválido! A posição do robô não pode ser negativa. Tentativa: (" + x + ", " + y + ")");
        }

        salvarPosicaoAtual();
        x = novoX;
        y = novoY;
        System.out.println(this);
    }

    protected void mover(int sentido) throws MovimentoInvalidoException {
        if (roboParalisado) {
            System.out.print("O robô foi explodido!");
            return;
        }
        int novoX = x, novoY = y;

        switch (sentido) {
            case 1:
                novoY--;
                break;
            case 2:
                novoY++;
                break;
            case 3:
                novoX++;
                break;
            case 4:
                novoX--;
                break;
            default:
                throw new MovimentoInvalidoException("Direção inválida: " + sentido);
        }

        if (novoX < 0 || novoY < 0 || novoX > 4 || novoY > 4) {
            throw new MovimentoInvalidoException(
                    "Movimento inválido! Posição resultante (" + novoX + ", " + novoY + ") fora do tabuleiro.");
        }

        salvarPosicaoAtual();
        x = novoX;
        y = novoY;
        System.out.println(this);
    }

    protected void moverIA() throws MovimentoInvalidoException {
        while (true) {
            if (roboParalisado) {
                System.out.println("O robô " + cor + " está paralisado e não pode se mover.");
                throw new MovimentoInvalidoException("Robô paralisado");
            }
            int sentido = random.nextInt(4) + 1; // 1 a 4

            try {
                mover(sentido);
                break;
            } catch (MovimentoInvalidoException e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }

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
        return getX() == alimentoX && getY() == alimentoY;

    }
    public boolean isAtivo() {
        return !roboParalisado;
    }

    public String toString() {
        return "Robo " + cor + " na posição (" + y + ", " + x + ")";
    }
}
