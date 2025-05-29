package com.example.jogo1;
import java.util.List;
import java.util.Scanner;

public class MainDualMission extends BaseJogo {

    public MainDualMission() {
        super();
    }

    @Override
    // Métodos Lógica do jogo
    public void executarJogo(Scanner scanner) {
        RoboBase roboNormal = new Robo("Azul");
        RoboBase roboInteligente = new RoboInteligente("Vermelho");

        adicionarRobo(roboNormal);
        adicionarRobo(roboInteligente);

        System.out.print("\n=== Escolha a posição do alimento ===\n");
        System.out.print("Posição x: ");
        int x = scanner.nextInt();
        System.out.print("Posição y: ");
        int y = scanner.nextInt();
        scanner.nextLine();

        escolherPosAlimento(x, y);

        boolean roboNormalAchou = false;
        boolean roboInteligenteAchou = false;

        while (!(roboNormalAchou && roboInteligenteAchou)) {
            exibirTabuleiro();

            if (!roboNormalAchou) {
                try {
                    roboNormal.moverIA();
                    if (encontrouAlimento(roboNormal)) {
                        roboNormalAchou = true;
                        System.out.println("Robô Azul encontrou o alimento!");
                    }
                } catch (MovimentoInvalidoException e) {
                    System.out.println("Erro de movimentação: " + e.getMessage());
                }
            }

            if (!roboInteligenteAchou) {
                try {
                    roboInteligente.moverIA();
                    if (encontrouAlimento(roboInteligente)) {
                        roboInteligenteAchou = true;
                        System.out.println("Robô Vermelho encontrou o alimento!");
                    }
                } catch (MovimentoInvalidoException e) {
                    System.out.println("Erro de movimentação: " + e.getMessage());
                }
            }

            // Mensagem para o outro robô se já encontrou
            if (roboNormalAchou && !roboInteligenteAchou) {
                System.out.println("Robô Azul já encontrou o alimento!");
            } else if (!roboNormalAchou && roboInteligenteAchou) {
                System.out.println("Robô Vermelho já encontrou o alimento!");
            }
        }

        System.out.println("\nOs dois robôs encontraram o alimento!");

        // Exibição das estatísticas dos robôs
        for (int i = 0; i < robos.size(); i++) {
            RoboBase robo = robos.get(i);
            int numValidos = robo.numMovimentosValidos;
            int numInvalidos = robo.numMovimentosInvalidos;
            int numTotalMovimentos = numValidos + numInvalidos;

            System.out.print("\nRobô " + robo.getCor()
                    + " - Movimentos válidos: " + numValidos
                    + " | Movimentos inválidos: " + numInvalidos
                    + " | Total: " + numTotalMovimentos);
        }
    }

    // Adaptação dos métodos para a interface
    public void executarJogo(String corRobo1, String corRobo2, int x, int y) {
        escolherPosAlimento(x, y);

        RoboBase roboNormal = new Robo(corRobo1);
        RoboBase roboInteligente = new RoboInteligente(corRobo2);

        adicionarRobo(roboNormal);
        adicionarRobo(roboInteligente);
    }

    public boolean atualizarMovimentoIA() {
        RoboBase roboNormal = robos.get(0);
        RoboBase roboInteligente = robos.get(1);

        boolean roboNormalAchou = encontrouAlimento(roboNormal);
        boolean roboInteligenteAchou = encontrouAlimento(roboInteligente);

        if (!roboNormalAchou) {
            try {
                roboNormal.moverIA();
                if (encontrouAlimento(roboNormal)) {
                    roboNormalAchou = true;
                    System.out.println("Robô Azul encontrou o alimento!");
                }
            } catch (MovimentoInvalidoException e) {
                System.out.println("Erro de movimentação: " + e.getMessage());
            }
        }

        if (!roboInteligenteAchou) {
            try {
                roboInteligente.moverIA();
                if (encontrouAlimento(roboInteligente)) {
                    roboInteligenteAchou = true;
                    System.out.println("Robô Vermelho encontrou o alimento!");
                }
            } catch (MovimentoInvalidoException e) {
                System.out.println("Erro de movimentação: " + e.getMessage());
            }
        }

        if (roboNormalAchou && roboInteligenteAchou) {
            // Exibir as estatísticas após ambos os robôs encontrarem o alimento
            for (int i = 0; i < robos.size(); i++) {
                RoboBase robo = robos.get(i);
                int numValidos = robo.numMovimentosValidos;
                int numInvalidos = robo.numMovimentosInvalidos;
                int numTotalMovimentos = numValidos + numInvalidos;

                System.out.print("\nRobô " + robo.getCor()
                        + " - Movimentos válidos: " + numValidos
                        + " | Movimentos inválidos: " + numInvalidos
                        + " | Total: " + numTotalMovimentos);
            }
        }

        return encontrouAlimento(roboNormal) && encontrouAlimento(roboInteligente);
    }

    public List<RoboBase> getRobos() {
        return robos;
    }
}
