package com.example.jogo1;
public class Rocha extends Obstaculo {

    public Rocha(int idObstaculo, int posX, int posY) {
        super(idObstaculo, posX, posY);
    }

    @Override
    public void bater(Robo robo) {
        System.out.println("O robô " + robo.getCor() + " bateu em uma rocha na posição (" + posY + "," + posX + ")");
        robo.voltarPosicaoAnterior();
    }
}
