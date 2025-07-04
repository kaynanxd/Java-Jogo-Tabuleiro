package com.example.jogo1;
public class Rocha extends Obstaculo {

    public Rocha(int idObstaculo, int posX, int posY) {
        super(idObstaculo, posX, posY);
    }

    @Override
    public void bater(RoboBase robo) {
        System.out.println("O robô " + robo.getCor() + " bateu em uma rocha na posição (" + posX + "," + posY + ")");
        robo.voltarPosicaoAnterior();
    }
}
