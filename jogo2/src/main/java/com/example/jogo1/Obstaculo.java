package com.example.jogo1;
public abstract class Obstaculo {
    protected int idObstaculo;
    protected int posX;
    protected int posY;

    public Obstaculo(int idObstaculo, int posX, int posY) {
        this.idObstaculo = idObstaculo;
        this.posX = posX;
        this.posY = posY;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public abstract void bater(Robo robo);

    public boolean colisorObstaculo(int x, int y) {
        return this.posX == x && this.posY == y;
    }
}