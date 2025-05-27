package com.example.jogo1;
import java.util.List;
import java.util.Scanner;

public class MainSurvivalBots extends BaseJogo {

    public MainSurvivalBots() { super(); }

    //Metodos Da Logica Do Jogo

    public void executarJogo(Scanner scanner) {

        Robo robo1 = new Robo("Azul");
        RoboInteligente robo2 = new RoboInteligente("Vermelho");
        adicionarRobo(robo1);
        adicionarRobo(robo2);

        escolherPosAlimento(alimentoX, alimentoY);

        boolean robo1AchouAlimento = false;
        boolean robo2AchouAlimento = false;

        System.out.print("\n=== Escolha a posição das bombas ===\n");
        System.out.print("Quantidades de bombas: ");
        int numBombas = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numBombas; i++) {
            System.out.print("Bomba " + (i + 1) + " - Posição x: ");
            int bombaX = scanner.nextInt();
            System.out.print("Bomba " + (i + 1) + " - Posição y: ");
            int bombaY = scanner.nextInt();
            scanner.nextLine();

            Bomba bomba = new Bomba(i + 1, bombaX, bombaY);
            bombas.add(bomba);
        }

        System.out.print("\n=== Escolha a posição das rochas ===\n");
        System.out.print("Quantidades de rochas: ");
        int numRochas = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numRochas; i++) {
            System.out.print("Rocha " + (i + 1) + " - Posição x: ");
            int rochaX = scanner.nextInt();
            System.out.print("Rocha " + (i + 1) + " - Posição y: ");
            int rochaY = scanner.nextInt();
            scanner.nextLine();

            Rocha rocha = new Rocha(i + 1, rochaX, rochaY);
            rochas.add(rocha);
        }

        while (!(robo1AchouAlimento || robo2AchouAlimento)) {
            for (int i = 0; i < robos.size(); i++) {
                Robo robo = robos.get(i);
                if (!encontrouAlimento(robo)) {
                    try {
                        robo.moverIA();
                        verificarColisoes(robo);
                    } catch (MovimentoInvalidoException e) {
                        System.out.println("Erro de movimentação: " + e.getMessage());
                    }
                }
            }

            exibirTabuleiro();

            robo1AchouAlimento = encontrouAlimento(robos.get(0));
            robo2AchouAlimento = encontrouAlimento(robos.get(1));
        }

        if (robo1AchouAlimento) {
            System.out.println("Robo " + robos.get(0).getCor() + " encontrou o alimento!");
            mostrarEstatisticas();
        }
        if (robo2AchouAlimento && !robo1AchouAlimento) {
            System.out.println("Robo " + robos.get(1).getCor() + " encontrou o alimento!");
            mostrarEstatisticas();
        }
    }

    public void mostrarEstatisticas(){
        for (int i = 0; i < robos.size(); i++) {
            System.out.print("\nRobô " + robos.get(i).getCor() + " - Movimentos válidos: " +
                    robos.get(i).numMovimentosValidos + " | Movimentos inválidos: " + robos.get(i).numMovimentosInvalidos);

        }
    }

    private void verificarColisoes(Robo robo) {
        for (int i = 0; i < bombas.size(); i++) {
            Bomba bomba = bombas.get(i);
            if (bomba.getX() == robo.getX() && bomba.getY() == robo.getY()) {
                bomba.bater(robo);
                if (!bomba.bombaAtivada()) {
                    removerBombaDaInterface(bomba.getX(), bomba.getY());
                }
                return;
            }
        }

        for (int i = 0; i < rochas.size(); i++) {
            Rocha rocha = rochas.get(i);
            if (rocha.getX() == robo.getX() && rocha.getY() == robo.getY()) {
                rocha.bater(robo);
                return;
            }
        }
    }

    //Adaptacao dos Metodos para interface grafica funcionar
    private List<int[]> posicoesBombasSelecionadas;

    public void setPosicoesBombasSelecionadas(List<int[]> posicoesBombasSelecionadas) {
        this.posicoesBombasSelecionadas = posicoesBombasSelecionadas;
    }

    public void executarJogo(String corRobo1,String corRobo2, int x, int y, List<int[]> bombasSelecionadas, List<int[]> pedrasSelecionadas) {
        Robo robo1 = new Robo(corRobo1);
        RoboInteligente robo2 = new RoboInteligente(corRobo2);
        adicionarRobo(robo1);
        adicionarRobo(robo2);

        escolherPosAlimento(x, y);

        int id = 1;
        for (int[] pos : bombasSelecionadas) {
            Bomba bomba = new Bomba(id++, pos[1], pos[0]);
            bombas.add(bomba);
        }

        id = 1;
        for (int[] pos : pedrasSelecionadas) {
            Rocha rocha = new Rocha(id++, pos[1], pos[0]);
            rochas.add(rocha);
        }
    }

    public boolean atualizarMovimentoIA() {
        for (Robo robo : robos) {
            if (!robo.isAtivo() || encontrouAlimento(robo)) continue;

            try {
                robo.moverIA();
                verificarColisoes(robo);
            } catch (MovimentoInvalidoException e) {
                System.out.println("Erro de movimentação: " + e.getMessage());
            }
        }

        for (Robo robo : robos) {
            if (robo.isAtivo() && encontrouAlimento(robo)) {
                mostrarEstatisticas();
                return true;
            }
        }

        boolean todosMortos = robos.stream().noneMatch(Robo::isAtivo);
        if(todosMortos==true){
            mostrarEstatisticas();
        }
        return todosMortos;
    }

    private void removerBombaDaInterface(int x, int y) {
        if (posicoesBombasSelecionadas != null) {
            posicoesBombasSelecionadas.removeIf(pos -> pos[0] == y && pos[1] == x);
        }
    }

    public List<Robo> getRobos() {
        return robos;
    }
}