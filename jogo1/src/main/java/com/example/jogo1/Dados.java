package com.example.jogo1;

import java.util.Random;
import java.util.Scanner;


public class Dados {
    protected int dado1 = 0, dado2 = 0, somaDados = 0;
    protected ModoJogo modoJogo;
    private Scanner sc;
    private Random random;

    public enum ModoJogo {
        DEBUG, NORMAL
    }

    public Dados() {
        this(ModoJogo.NORMAL);
    }

    public Dados(ModoJogo modo) {
        this.modoJogo = modo;
        this.random = new Random();
        if (modo == ModoJogo.DEBUG) {
            this.sc = new Scanner(System.in);
        }
    }

    public ModoJogo getModoJogo() {
        return modoJogo;
    }

    public void setModoJogo(ModoJogo modo) {
        this.modoJogo = modo;
        if (modo == ModoJogo.DEBUG && sc == null) {
            this.sc = new Scanner(System.in);
        }
    }

    public void rolarDados(String tipoJogador) {
        switch (modoJogo) {
            case NORMAL:
                int tentativas = 0;
                do {
                    // Rola os dados (valores de 1 a 6)
                    dado1 = random.nextInt(6) + 1;
                    dado2 = random.nextInt(6) + 1;
                    somaDados = dado1 + dado2;
                    tentativas++;

                    // Verifica condições especiais do jogador
                    boolean condicaoSatisfeita = true;

                    if (tipoJogador.equals("Sortudo")) {
                        condicaoSatisfeita = (somaDados >= 7);
                    } else if (tipoJogador.equals("Azarado")) {
                        condicaoSatisfeita = (somaDados <= 6);
                    }
                    // Verifica se os dados são iguais (exceto em DEBUG)
                    boolean dadosIguais = (dado1 == dado2);
                    boolean deveRolarNovamente = modoJogo != ModoJogo.DEBUG && dadosIguais;

                    if (condicaoSatisfeita && !deveRolarNovamente) {
                        break;
                    }
                } while (true);
                break;

            case DEBUG:
                // Implementação do modo debug
                System.out.println("Modo DEBUG ativado. Insira os valores dos dados:");
                dado1 = sc.nextInt();
                dado2 = sc.nextInt();
                somaDados = dado1 + dado2;
                break;
        }
    }

    public int getSomaDados() {
        return somaDados;
    }

    public void exibirDados() {
        System.out.print("Dado 1: " + dado1 + " | Dado 2: " + dado2 + " | Soma dos dados: " + somaDados + ".\n");
    }
}
