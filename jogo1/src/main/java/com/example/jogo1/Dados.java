package com.example.jogo1;

import java.util.Random;
import java.util.Scanner;

public class Dados {
    private DadosProduct dadosProduct = new DadosProduct();
	protected int dado1 = 0, dado2 = 0, somaDados = 0;
    private Random random;

    public enum ModoJogo {
        DEBUG, NORMAL
    }

    public Dados() {
        this(ModoJogo.NORMAL);
    }

    public Dados(ModoJogo modo) {
        dadosProduct.setModoJogo2(modo);
        this.random = new Random();
        if (modo == ModoJogo.DEBUG) {
            dadosProduct.setSc(new Scanner(System.in));
        }
    }

    public ModoJogo getModoJogo() {
        return dadosProduct.getModoJogo();
    }

    public void setModoJogo(ModoJogo modo) {
        dadosProduct.setModoJogo(modo);
    }

    public void rolarDados() {
    	dado1 = random.nextInt(6) + 1;
        dado2 = random.nextInt(6) + 1;
        somaDados = dado1 + dado2;          
    }
    
    public void setDadosDebug(int d1, int d2) {
        this.dado1 = d1;
        this.dado2 = d2;
        this.somaDados = d1 + d2;
    }

    public int getSomaDados() {
        return somaDados;
    }

    public boolean saoDadosIguais() {
        return dado1 == dado2;
    }

    public void exibirDados() {
        System.out.print("Dado 1: " + dado1 + " | Dado 2: " + dado2 + " | Soma dos dados: " + somaDados + ".\n");
    }
}