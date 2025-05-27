package com.example.jogo1;
import java.util.List;
import java.util.Scanner;

public class MainDualMission extends BaseJogo {


    public MainDualMission() {
        super();
    }

    @Override

    //Metodos Logica do jogo
    public void executarJogo(Scanner scanner) {

        Robo roboNormal = new Robo("Azul");
        RoboInteligente roboInteligente = new RoboInteligente("Vermelho");

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

        for (int i = 0; i < robos.size(); i++) {
            int numValidos = robos.get(i).numMovimentosValidos;
            int numInvalidos = robos.get(i).numMovimentosInvalidos;
            int numTotalMovimentos = numValidos + numInvalidos;

            System.out.print("\nRobô " + robos.get(i).getCor()
                    + " - Movimentos válidos: " + numValidos
                    + " | Movimentos inválidos: " + numInvalidos
                    + " | Total: " + numTotalMovimentos);
        }
    }
//Adaptacao dos metodos para a interface
    public void executarJogo(String corRobo1,String corRobo2, int x, int y){
        escolherPosAlimento(x, y);

        Robo roboNormal = new Robo(corRobo1);
        RoboInteligente roboInteligente = new RoboInteligente(corRobo2);

        adicionarRobo(roboNormal);
        adicionarRobo(roboInteligente);
    }

    public boolean atualizarMovimentoIA() {

        Robo roboNormal = robos.get(0);
        Robo roboInteligente = robos.get(1);

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

        if(roboNormalAchou==true && roboInteligenteAchou==true){
            for (int i = 0; i < robos.size(); i++) {
                int numValidos = robos.get(i).numMovimentosValidos;
                int numInvalidos = robos.get(i).numMovimentosInvalidos;
                int numTotalMovimentos = numValidos + numInvalidos;

                System.out.print("\nRobô " + robos.get(i).getCor()
                        + " - Movimentos válidos: " + numValidos
                        + " | Movimentos inválidos: " + numInvalidos
                        + " | Total: " + numTotalMovimentos);
            }
        }
        return encontrouAlimento(roboNormal) && encontrouAlimento(roboInteligente);
    }

    public List<Robo> getRobos() {
        return robos;
    }
}