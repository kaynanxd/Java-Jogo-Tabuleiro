package com.example.jogo1;

import java.util.Scanner;

public class MainNormal extends BaseJogo {

    private Robo roboManual;
    public MainNormal() {
        super();
    }

    // Metodos Da Logica Do Jogo

    public void executarJogo(Scanner scanner) {

        System.out.print("\n=== Escolha a posição do alimento ===\n");
        System.out.print("Posição x: ");
        alimentoX = scanner.nextInt();
        System.out.print("Posição y: ");
        alimentoY = scanner.nextInt();
        scanner.nextLine();

        escolherPosAlimento(alimentoX, alimentoY);

        roboManual = new Robo("Azul");
        adicionarRobo(roboManual);

        while (!encontrouAlimento(roboManual)) {
            exibirTabuleiro();
            System.out.println("Posição atual do robô: (" + roboManual.getX() + "," + roboManual.getY() + ")");
            System.out.print("Digite o próximo movimento (1=Cima, 2=Baixo, 3=Direita, 4=Esquerda): ");

            int direcao;
            try {
                direcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número entre 1 e 4.");
                continue;
            }

            if (direcao < 1 || direcao > 4) {
                System.out.println("Número inválido! Use 1, 2, 3 ou 4.");
                continue;
            }

            try {
                roboManual.mover(direcao);
            } catch (MovimentoInvalidoException e) {
                System.out.println("Movimento inválido: " + e.getMessage());
            }
        }
        exibirTabuleiro();
        System.out.println("O robô encontrou o alimento!");
    }

    //Adaptacao dos metodos para usar a interface gráfica

    public void executarJogo(String corRobo1, int x, int y) {
        roboManual = new Robo(corRobo1);
        adicionarRobo(roboManual);
        escolherPosAlimento(x, y);
    }

    public boolean moverRobo(String input) {
        try {
            String imputEmMinisculo = input.toLowerCase();
            switch(imputEmMinisculo) {
                case "1": case "up":
                    roboManual.mover(1);  // Cima
                    break;
                case "2": case "down":
                    roboManual.mover(2);  // Baixo
                    break;
                case "3": case "right":
                    roboManual.mover(3);  // Direita
                    break;
                case "4": case "left":
                    roboManual.mover(4);  // Esquerda
                    break;
                default:
                    System.out.println("Entrada inválida!");
                    return false;
            }
        } catch (MovimentoInvalidoException e) {
            System.out.println("Erro de movimentação: " + e.getMessage());
            return false;
        }
        return encontrouAlimento(roboManual);
    }

    public Robo getRoboManual() {
        return roboManual;
    }

}


