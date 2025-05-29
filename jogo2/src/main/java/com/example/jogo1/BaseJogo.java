package com.example.jogo1;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class BaseJogo {
    protected String[][] tabuleiro;
    protected int tamanhoTabuleiro = 5;
    protected ArrayList<RoboBase> robos;
    protected ArrayList<Bomba> bombas;
    protected ArrayList<Rocha> rochas;

    protected int alimentoX = 0, alimentoY = 0;

    public BaseJogo() {
        this.tabuleiro = new String[tamanhoTabuleiro][tamanhoTabuleiro];
        this.robos = new ArrayList<>();
        this.bombas = new ArrayList<>();
        this.rochas = new ArrayList<>();
        inicializarTabuleiro();
    }

    public void inicializarTabuleiro() {
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            for (int j = 0; j < tamanhoTabuleiro; j++) {
                tabuleiro[i][j] = "[ ]";
            }
        }
    }

    public void escolherPosAlimento(int x, int y) {
        alimentoX = x;
        alimentoY = y;
    }

    protected void adicionarRobo(RoboBase robo) {
        robos.add(robo);
    }

    protected void exibirTabuleiro() {
        inicializarTabuleiro();

        tabuleiro[alimentoX][alimentoY] = "[A]";

        for (int i = 0; i < bombas.size(); i++) { //marca bombas
            Bomba bomba = bombas.get(i);
            if (bomba.bombaAtivada()) {
                tabuleiro[bomba.getX()][bomba.getY()] = "[B]";
            }
        }

        for (int i = 0; i < rochas.size(); i++) { //marca pedras
            Rocha rocha = rochas.get(i);
            tabuleiro[rocha.getX()][rocha.getY()] = "[R]";
        }


        for (int i = 0; i < robos.size(); i++) { //marca robos
            RoboBase robo = robos.get(i);
            tabuleiro[robo.getX()][robo.getY()] = "[R" + robo.getCor().substring(0, 1).toUpperCase() + "]";
        }

        System.out.print("\n=== Tabuleiro ===\n");
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            for (int j = 0; j < tamanhoTabuleiro; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
    }

    protected boolean encontrouAlimento(RoboBase robo) {
        return robo.getX() == alimentoX && robo.getY() == alimentoY;
    }

    public abstract void executarJogo(Scanner scanner);
}