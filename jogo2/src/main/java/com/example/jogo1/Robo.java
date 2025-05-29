package com.example.jogo1;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Robo extends RoboBase {
    protected Robo(String cor) {
        super(cor);
    }

    @Override
    protected void moverIA() throws MovimentoInvalidoException {
        if (roboParalisado) {
            System.out.println("O robô " + cor + " está paralisado e não pode se mover.");
            throw new MovimentoInvalidoException("Robô paralisado");
        }
        while (true) {
            int sentido = random.nextInt(4) + 1;
            try {
                mover(sentido);
                break;
            } catch (MovimentoInvalidoException e) {
                System.out.println("Movimento inválido para o robô " + cor + ": " + e.getMessage());
            }
        }
    }
}
