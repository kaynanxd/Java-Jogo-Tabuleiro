package com.example.jogo1;
import java.util.List;
import java.util.Scanner;

public class MainDualMission extends BaseJogo {


    public MainDualMission() {
        super();
    }
    //metodos para terminal
    public void executarJogo(Scanner scanner) {

        Robo roboNormal = new Robo("Azul");
        RoboInteligente roboInteligente = new RoboInteligente("Vermelho");

        adicionarRobo(roboNormal);
        adicionarRobo(roboInteligente);

        System.out.print("\n=== Escolha a posição do alimento ===\n");
        System.out.print("Posição x: ");
        int alimentoX = scanner.nextInt();
        System.out.print("Posição y: ");
        int alimentoY = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        escolherPosAlimento(alimentoX, alimentoY);

        boolean roboNormalAchou = false;
        boolean roboInteligenteAchou = false;

        while (!(roboNormalAchou && roboInteligenteAchou)) {
            exibirTabuleiro();
            if (!roboNormalAchou) {
                try {
                    roboNormal.moverIA();
                    // Verificar se encontrou o alimento
                    if (roboNormal.encontrouAlimento(alimentoX, alimentoY)) {
                        roboNormalAchou = true;
                        System.out.println("Robô Azul encontrou o alimento!");
                    }
                    if(!roboNormalAchou && roboInteligenteAchou) {
                        System.out.println("Robô Vermelho já encontrou o alimento!");
                    }
                } catch (MovimentoInvalidoException e) {
                    System.out.println("Erro de movimentação: " + e.getMessage());
                }
            }

            if (!roboInteligenteAchou) {
                try {
                    roboInteligente.moverIA();
                    // Verificar se encontrou o alimento
                    if (roboInteligente.encontrouAlimento(alimentoX, alimentoY)) {
                        roboInteligenteAchou = true;
                        System.out.println("Robô Vermelho encontrou o alimento!");
                    }

                    if(roboNormalAchou && !roboInteligenteAchou) {
                        System.out.println("Robô Azul já encontrou o alimento!");
                    }
                } catch (MovimentoInvalidoException e) {
                    System.out.println("Erro de movimentação: " + e.getMessage());
                }
            }
        }

        System.out.println("\nOs dois robôs encontraram o alimento!");
    }
//metodos para interface
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
            } catch (MovimentoInvalidoException e) {
                System.out.println("Erro de movimentação: " + e.getMessage());
            }
        }

        if (!roboInteligenteAchou) {
            try {
                roboInteligente.moverIA();
            } catch (MovimentoInvalidoException e) {
                System.out.println("Erro de movimentação: " + e.getMessage());
            }
        }

        return encontrouAlimento(roboNormal) && encontrouAlimento(roboInteligente);
    }

    public List<Robo> getRobos() {
        return robos;
    }
}