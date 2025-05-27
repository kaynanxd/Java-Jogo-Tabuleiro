package com.example.jogo1;
public class Bomba extends Obstaculo {
    private boolean bombaAtiva;

    public Bomba(int idObstaculo, int posX, int posY) {
        super(idObstaculo, posX, posY);
        this.bombaAtiva = true;
    }

    public boolean bombaAtivada() {
        return bombaAtiva;
    }

    @Override
    public void bater(Robo robo) {
        if (bombaAtiva) {
            System.out.println(
                    "O robô " + robo.getCor() + " foi eliminado pela bomba na posição (" + posX + "," + posY + ")");
            robo.explodir();
            this.bombaAtiva = false;
        }
    }
}