package com.example.jogo1;
import java.util.List;
import java.util.Scanner;

public class MainMachine extends BaseJogo {

    private RoboBase robo1;
    private RoboBase robo2;

    public MainMachine() {
        super();
    }

    // Métodos Da Lógica Do Jogo
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

        // Instanciação dos robôs
        robo1 = new Robo("Azul");
        robo2 = new Robo("Vermelho");
        adicionarRobo(robo1);
        adicionarRobo(robo2);

        while (!(robo1Achou || robo2Achou)) {
            for (int i = 0; i < robos.size(); i++) {
                RoboBase robo = robos.get(i);
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

        // Exibição de mensagens de vitória
        if (robo1Achou) {
            System.out.println("Robo " + robos.get(0).getCor() + " encontrou o alimento!");
        }
        if (robo2Achou && !robo1Achou) {
            System.out.println("Robo " + robos.get(1).getCor() + " encontrou o alimento!");
        }

        // Exibição das estatísticas dos robôs
        for (int i = 0; i < robos.size(); i++) {
            RoboBase robo = robos.get(i);
            System.out.print(
                    "\nRobô " + robo.getCor() + " - Movimentos válidos: " + robo.numMovimentosValidos +
                            " | Movimentos inválidos: " + robo.numMovimentosInvalidos);
        }
    }

    // Adaptação dos métodos para funcionar na interface
    public void executarJogo(String corRobo1, String corRobo2, int x, int y) {
        escolherPosAlimento(x, y);

        // Instanciação dos robôs
        robo1 = new Robo(corRobo1);
        robo2 = new Robo(corRobo2);

        adicionarRobo(robo1);
        adicionarRobo(robo2);
    }

    public boolean atualizarMovimentoIA() {
        boolean algumAchou = false;

        // Movimentação dos robôs
        for (RoboBase robo : robos) {
            if (!encontrouAlimento(robo)) {
                try {
                    robo.moverIA();
                } catch (MovimentoInvalidoException e) {
                    System.out.println("Erro de movimentação: " + e.getMessage());
                }
            }
        }

        // Checa se algum robô chegou ao alimento
        for (RoboBase robo : robos) {
            if (encontrouAlimento(robo)) {
                algumAchou = true;
            }
        }

        // Exibição das estatísticas se algum robô encontrou o alimento
        if (algumAchou) {
            for (int i = 0; i < robos.size(); i++) {
                RoboBase robo = robos.get(i);
                System.out.print(
                        "\nRobô " + robo.getCor() + " - Movimentos válidos: " + robo.numMovimentosValidos +
                                " | Movimentos inválidos: " + robo.numMovimentosInvalidos);
            }
        }

        return algumAchou;
    }

    public List<RoboBase> getRobos() {
        return robos;
    }
}
