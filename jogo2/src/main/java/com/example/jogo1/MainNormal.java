package com.example.jogo1;

import java.util.Scanner;

public class MainNormal extends BaseJogo {

    private Robo roboManual; //variavel para interface

    public MainNormal() {
        super();
    }
    // metodos para uso em terminal

    public void executarJogo(Scanner scanner) {
        System.out.println("\n=== Escolha a cor do Robô ===");
        System.out.println("Digite a cor do seu Robô: ");
        String corRobo = scanner.nextLine().trim();
        while (corRobo.isEmpty()) {
            System.out.println("Cor inválida! Por favor, digite novamente.");
            System.out.print("Digite a cor do seu robô: ");
            corRobo = scanner.nextLine().trim();
        }

        roboManual = new Robo(corRobo);
        adicionarRobo(roboManual);

        System.out.print("\n=== Escolha a posição do alimento ===\n");
        System.out.print("Posição x: ");
        alimentoX = scanner.nextInt();
        System.out.print("Posição y: ");
        alimentoY = scanner.nextInt();
        scanner.nextLine();
        escolherPosAlimento(alimentoX, alimentoY);

        while (!encontrouAlimento(roboManual)) {
            exibirTabuleiro();
            System.out.println("\nMovimente o Robô com:");
            System.out.println("Cima - 1 (Up)");
            System.out.println("Baixo - 2 (Down)");
            System.out.println("Direita - 3 (Right)");
            System.out.println("Esquerda - 4 (Left)");
            System.out.println("0 - Sair");
            System.out.print("Digite sua escolha: ");

            String input = scanner.nextLine().trim();

            if (input.equals("0") || input.equalsIgnoreCase("sair")) {
                System.out.println("\nPrograma encerrado. A posição final foi: " + roboManual);
                return;
            }

            try {
                if (input.matches("[1-4]")) {
                    roboManual.mover(Integer.parseInt(input));
                } else if (input.equalsIgnoreCase("up") ||
                        input.equalsIgnoreCase("down") ||
                        input.equalsIgnoreCase("right") ||
                        input.equalsIgnoreCase("left")) {
                    roboManual.mover(input.toLowerCase());
                } else {
                    System.out.println("Entrada inválida! Use 1-4 ou palavras-chave (up, down, right, left).");
                    continue;
                }
            } catch (MovimentoInvalidoException e) {
                System.out.println("Erro de movimentação: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }

        exibirTabuleiro();
        System.out.println("\n★ PARABÉNS! O ROBÔ " + roboManual.getCor().toUpperCase() + " encontrou o alimento! ★");
        System.out.println("Posição final: " + roboManual);
    }

    // ✅ Novos metodos baseado no de cima para uso com interface gráfica

    public void executarJogo(String corRobo1, int x, int y) {
        roboManual = new Robo(corRobo1);
        adicionarRobo(roboManual);
        escolherPosAlimento(x, y);
    }

    public boolean moverRobo(String input) {
        try {
            roboManual.mover(Integer.parseInt(input));
        } catch (MovimentoInvalidoException e) {
            System.out.println("Erro de movimentação: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            return false;
        }

        return encontrouAlimento(roboManual);
    }

    public Robo getRoboManual() {
        return roboManual;
    }

}


