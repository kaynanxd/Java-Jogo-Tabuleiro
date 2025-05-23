/*import java.util.Scanner;

public class Main {
    public static void main (String args[]) {
        Scanner scanner = new Scanner(System.in);
        int escolhaModoJogo = 0;

        System.out.println("=== Jogo dos Robôs ===");
        System.out.println("Escolha o modo de jogo:");
        System.out.println("1 - Modo Normal");
        System.out.println("2 - Modo Machine");
        System.out.println("3 - Modo Dual Mission");
        System.out.println("4 - Modo Survival Bots");
        System.out.print("Digite sua escolha: ");
        escolhaModoJogo = scanner.nextInt();

        switch(escolhaModoJogo) {
            case 1:
                MainNormal jogoNormal = new MainNormal();
                jogoNormal.executarJogo(scanner);
                break;
            
            case 2: 
                MainMachine jogoMachine = new MainMachine();
                jogoMachine.executarJogo(scanner);
                break;

            case 3: 
                MainDualMission jogoDualMission = new MainDualMission();
                jogoDualMission.executarJogo(scanner);
                break;
            
            case 4:
                MainSurvivalBots jogoSurvivalBots = new MainSurvivalBots();
                jogoSurvivalBots.executarJogo(scanner);
                break;
        }

        scanner.close();

    }
} */
