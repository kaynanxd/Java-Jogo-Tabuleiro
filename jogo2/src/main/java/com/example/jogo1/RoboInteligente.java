package com.example.jogo1;
import java.util.ArrayList;
import java.util.Random;

public class RoboInteligente extends Robo {
    protected ArrayList<Integer>[][] movimentosInvalidos;
    private Random random;

    public RoboInteligente(String cor) {
        super(cor);
        this.random = new Random();
        this.movimentosInvalidos = new ArrayList[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                movimentosInvalidos[i][j] = new ArrayList<>();
            }
        }
    }

    @Override
    protected void mover(int sentido) throws MovimentoInvalidoException {
        int novoX = getX(), novoY = getY();

        switch (sentido) {
            case 1: // Cima
                novoY++;
                break;
            case 2: // Baixo
                novoY--;
                break;
            case 3: // Direita
                novoX++;
                break;
            case 4: // Esquerda
                novoX--;
                break;
            default:
                throw new MovimentoInvalidoException("Direção inválida: " + sentido);
        }

        // Verifica limites do tabuleiro
        if (novoX < 0 || novoY < 0 || novoX > 4 || novoY > 4) {
            movimentosInvalidos[getX()][getY()].add(sentido);
            throw new MovimentoInvalidoException("Movimento inválido na posição (" + getX() + "," + getY() + ")");
        }

        setX(novoX);
        setY(novoY);
        System.out.println("Robô " + getCor() + " moveu para (" + getX() + "," + getY() + ")");
    }

    @Override
    protected void moverIA() throws MovimentoInvalidoException {
        ArrayList<Integer> movimentosPossiveis = new ArrayList<>();
        for (int sentido = 1; sentido <= 4; sentido++) {
            if (!movimentosInvalidos[getX()][getY()].contains(sentido)) {
                movimentosPossiveis.add(sentido);
            }
        }

        if (movimentosPossiveis.isEmpty()) {
            throw new MovimentoInvalidoException("Nenhum movimento válido disponível na posição (" +
                    getX() + "," + getY() + ")");
        }

        int sentidoEscolhido = movimentosPossiveis.get(random.nextInt(movimentosPossiveis.size()));

        try {
            mover(sentidoEscolhido);
        } catch (MovimentoInvalidoException e) {
            movimentosInvalidos[getX()][getY()].add(sentidoEscolhido);
            throw e;
        }
    }
}