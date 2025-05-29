package com.example.jogo1;
import java.util.ArrayList;
import java.util.Random;

public class RoboInteligente extends RoboBase {
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
        salvarPosicaoAtual();
    }

    @Override
    protected void moverIA() throws MovimentoInvalidoException {
        if (!isAtivo()) {
            System.out.println("O robô " + getCor() + " está paralisado e não pode se mover.");
            throw new MovimentoInvalidoException("Robô paralisado");
        }

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

        boolean movimentoRealizado = false;
        while (!movimentoRealizado) {
            int sentidoEscolhido = movimentosPossiveis.get(random.nextInt(movimentosPossiveis.size()));
            try {
                mover(sentidoEscolhido);
                movimentoRealizado = true;
            } catch (MovimentoInvalidoException e) {
                movimentosInvalidos[getX()][getY()].add(sentidoEscolhido);
                movimentosPossiveis.remove((Integer) sentidoEscolhido);
                if (movimentosPossiveis.isEmpty()) {
                    throw new MovimentoInvalidoException(
                            "Nenhum movimento válido disponível após tentativas na posição (" +
                                    getX() + "," + getY() + ")");
                }
            }
        }
    }
}