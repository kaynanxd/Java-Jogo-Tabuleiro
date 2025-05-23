package com.example.jogo1;
import java.util.List;
import java.util.Scanner;

public class MainMachine extends BaseJogo {

    private Robo robo1;
    private Robo robo2;

    public MainMachine() {
        super();
    }
    //metodos para funcionar no terminal
    public void executarJogo(Scanner scanner) {
        System.out.print("\n=== Escolha a posição do alimento ===\n");
        System.out.print("Posição x: ");
        alimentoX = scanner.nextInt();
        System.out.print("Posição y: ");
        alimentoY = scanner.nextInt();
        scanner.nextLine();

        escolherPosAlimento(alimentoX, alimentoY);

        boolean robo1Achou = false;
        boolean robo2Achou = false;

        Robo robo1 = new Robo("Azul");
        Robo robo2 = new Robo("Vermelho");
        adicionarRobo(robo1);
        adicionarRobo(robo2);

        while (!(robo1Achou || robo2Achou)) {
            for (int i = 0; i < robos.size(); i++) {
                Robo robo = robos.get(i);
                if (!encontrouAlimento(robo)) {
                    try {
                        robo.moverIA();
                    } catch (MovimentoInvalidoException e) {
                        System.out.println("Erro de movimentação: " + e.getMessage());
                    }
                }
            }

            exibirTabuleiro();

            robo1Achou = encontrouAlimento(robos.get(0));
            robo2Achou = encontrouAlimento(robos.get(1));
        }

        if (robo1Achou) {
            System.out.println("Robo " + robos.get(0).getCor() + " encontrou o alimento!");
        }
        if (robo2Achou && !robo1Achou) {
            System.out.println("Robo " + robos.get(1).getCor() + " encontrou o alimento!");
        }
    }
    //metodos para interface grafica
    public void executarJogo(String corRobo1,String corRobo2, int x, int y) {
        escolherPosAlimento(x, y);

        robo1 = new Robo(corRobo1);
        robo2 = new Robo(corRobo2);

        adicionarRobo(robo1);
        adicionarRobo(robo2);
    }

    public boolean atualizarMovimentoIA() {
        boolean algumAchou = false;

        for (Robo robo : robos) {
            if (!encontrouAlimento(robo)) {
                try {
                    robo.moverIA();
                } catch (MovimentoInvalidoException e) {
                    System.out.println("Erro de movimentação: " + e.getMessage());
                }
            }
        }

        // Checa se algum robô chegou ao alimento
        for (Robo robo : robos) {
            if (encontrouAlimento(robo)) {
                algumAchou = true;
            }
        }

        return algumAchou;
    }


    public List<Robo> getRobos() {
        return robos;
    }
}
