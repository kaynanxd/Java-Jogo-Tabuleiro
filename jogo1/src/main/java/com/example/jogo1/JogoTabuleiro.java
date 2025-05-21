package com.example.jogo1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;
import java.util.ArrayList;

public class JogoTabuleiro {

    private static final String COR_NORMAL = "#dbffd9";
    private static final String COR_SURPRESA = "#59ffff";
    private static final String COR_SORTE = "#FFFF00";
    private static final String COR_PARALISIA = "#ffa1a1";
    private static final String COR_VOLTAR_INICIO = "#ffb485";
    private static final String COR_MAGICA = "#8aa5ff";

    private static final String AUDIO_CONFIRM = "src/audios/confirm.mp3";
    private static final String AUDIO_CLIQUE = "src/audios/clique.mp3";
    private static final String AUDIO_APLAUSOS = "src/audios/aplausos.mp3";

    private final Dados.ModoJogo modoJogo;  //armazena se esta em debug ou nao
    private final Musica musica;
    private final List<Jogador> jogadores;
    private final Tabuleiro tabuleiro;   //logica do tabuleiro
    private final GridPane grid = new GridPane();    // Layout que organiza as casas do tabuleiro
    private final StackPane[] casas = new StackPane[40]; // Array com todas as casas visuais
    private Main mainApp; // Referência para a aplicação principal para reiniciar o jogo

    private int indiceJogadorAtual = 0;
    private Button botaoJogar; //botao para rolar os dados
    private Label labelVez; //variavel para mostrar de quem e o turno

    public JogoTabuleiro(List<Jogador> jogadores, Dados.ModoJogo modoJogo, Main main, Stage primaryStage) {
        this.jogadores = jogadores;
        this.musica = new Musica(AUDIO_CLIQUE);
        this.tabuleiro = new Tabuleiro(jogadores);
        this.modoJogo = modoJogo;
        this.mainApp = main;

        for (Jogador jogador : jogadores) {
            jogador.tabuleiro = tabuleiro; // Associa o tabuleiro a cada jogador
            jogador.dados = new Dados(modoJogo); // Cria um objeto Dados para cada jogador
        }
    }

    public void start(Stage stage) {  //Inicia o jogo e configura a interface gráfica
        BorderPane root = new BorderPane();
        criarTabuleiroVisual();
        configurarInterfaceJogo(root);
        acaoCliqueBotaoDados();

        Scene scene = new Scene(root, 800, 700);
        stage.setScene(scene);
        stage.setTitle("Jogo de Tabuleiro");
        stage.show();
    }

    private void configurarInterfaceJogo(BorderPane root) {
        botaoJogar = criarBotaoJogar();
        labelVez = new Label();

        HBox barraInferior = new HBox(20, botaoJogar, labelVez);
        barraInferior.setAlignment(Pos.CENTER);
        barraInferior.setPadding(new Insets(15));

        root.setBottom(barraInferior); // Barra inferior com botão e label
        root.setCenter(grid);  // Tabuleiro no centro

        atualizarTabuleiroVisual(); //atualiza as informacoes iniciais
    }

    private Button criarBotaoJogar() {
        Button botao = new Button("🎲 Jogar Dados");
        botao.setStyle("""
            -fx-font-size: 20px;
            -fx-background-color: #2196F3;
            -fx-text-fill: white;
            -fx-padding: 15px 30px;
            -fx-background-radius: 10;
        """);

        // Efeito hover que aumenta o botao ao passar mouse
        botao.setOnMouseEntered(e -> {
            botao.setScaleX(1.1);
            botao.setScaleY(1.1);
            musica.tocarEfeito(AUDIO_CLIQUE);
        });

        botao.setOnMouseExited(e -> {
            botao.setScaleX(1.0);
            botao.setScaleY(1.0);
        });

        return botao;
    }

    private void acaoCliqueBotaoDados() {
        botaoJogar.setOnAction(e -> {
            musica.tocarEfeito(AUDIO_CONFIRM);
            realizarJogada();
        });
    }

    private void criarTabuleiroVisual() {
        grid.setHgap(0); //tira o espacamento entre os quadrados
        grid.setVgap(0);
        grid.setStyle("-fx-background-color: #b3f0ff; -fx-padding: 10;");

        for (int i = 0; i < 40; i++) { //cria as 40 casas
            StackPane casa = criarCasaTabuleiro(i); //cria uma casa individual
            casas[i] = casa; //armazena no array de casas

            int linha = (i / 10) * 2; //As linhas são multiplicadas por 2 para deixar espaço para os conectores e cada linha tem 10 casas
            int coluna = (linha / 2 % 2 == 0) ? i % 10 : 9 - (i % 10); // Alterna direção para conseguir fazer caminho do percurso em S

            grid.add(casa, coluna, linha); // adiciona a casa na posição calculada

            if (i == 9 || i == 19 || i == 29) { // Adiciona conectores nas viradas do tabuleiro,  inicio/fim de cada linha
                adicionarConector(linha, coluna);
            }
        }
    }

    private StackPane criarCasaTabuleiro(int numeroCasa) {
        StackPane casa = new StackPane(); // Container que permite sobrepor elementos
        casa.setPrefSize(80, 80); // Tamanho fixo para todas as casas
        casa.setStyle("-fx-border-color: black; -fx-background-color: " + obterCorCasa(numeroCasa) + ";");
        //adiciona o numero dentro da casa
        Label numero = new Label(String.valueOf(numeroCasa));
        casa.getChildren().add(numero);

        return casa;
    }

    private String obterCorCasa(int numeroCasa) {
        return switch (numeroCasa) {  //define a cor das casas especiais
            case 13 -> COR_SURPRESA;
            case 5, 15, 30 -> COR_SORTE;
            case 10, 25, 38 -> COR_PARALISIA;
            case 17, 27 -> COR_VOLTAR_INICIO;
            case 20, 35 -> COR_MAGICA;
            default -> COR_NORMAL;
        };
    }

    private void adicionarConector(int linha, int coluna) {   //adiciona a casa conector entre as linhas, a que tem a setinha
        StackPane conector = new StackPane();
        conector.setPrefSize(80, 80);
        conector.setStyle("-fx-background-color: " + COR_NORMAL + "; -fx-border-color: black;");

        Label seta = new Label("↓");
        conector.getChildren().add(seta);

        grid.add(conector, coluna, linha + 1); // Adiciona o conector na linha seguinte à casa
    }

    private void atualizarTabuleiroVisual() {
        Jogador atual = jogadores.get(indiceJogadorAtual);
        labelVez.setText("🎯 Vez de: " + atual.getNomeJogador() + " ( " + atual.getTipoJogador() + ")");

        String estilo = """
        -fx-background-color: %s;
        -fx-text-fill: black;
        -fx-font-weight: bold;
        -fx-font-size: 16px;
        -fx-padding: 8 16;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
    """.formatted(atual.getCorHex());
        labelVez.setStyle(estilo);

        for (int i = 0; i < 40; i++) { //pecorre todas as casas
            StackPane casa = casas[i];
            casa.getChildren().clear();  // Limpa o conteudo atual da casa

            VBox conteudo = new VBox(2); // //criar uma caixa para o conteudo de dentro do quadrado
            conteudo.setStyle("-fx-alignment: center;");
            conteudo.getChildren().add(new Label(String.valueOf(i))); //adiciona o numero a casa

            GridPane marcadores = criarMarcadoresJogadores(i); // Adiciona os marcadores dos jogadores que estão nesta casa
            conteudo.getChildren().add(marcadores);

            casa.getChildren().add(conteudo); //adiciona o novo conteudo definitivamente a casa
        }
    }

    private GridPane criarMarcadoresJogadores(int numeroCasa) {
        GridPane marcadores = new GridPane();
        marcadores.setHgap(2);
        marcadores.setVgap(2);
        marcadores.setAlignment(Pos.CENTER); //centraliza os marcadores

        int indice = 0;
        for (Jogador jogador : jogadores) { // Verifica quais jogadores estão nesta casa
            if (jogador.getPosicaoAtual() == numeroCasa) {
                Label marcador = criarMarcadorJogador(jogador); // Cria um marcador para o jogador
                int col = indice % 3; // Calcula posição dentro do quadrado (máximo 3 por linha para evitar esticamento do quadrado)
                int row = indice / 3;
                marcadores.add(marcador, col, row);
                indice++;
            }
        }

        return marcadores;
    }

    private Label criarMarcadorJogador(Jogador jogador) {
        Label marcador = new Label(jogador.getNomeJogador().substring(0, 1).toUpperCase()); // Cria label com a primeira letra do nome em maisculo
        marcador.setStyle(
                "-fx-background-color: " + jogador.getCorHex() + ";" +
                        "-fx-text-fill: black;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 4;" +
                        "-fx-min-width: 20px;" +
                        "-fx-alignment: center;" +
                        "-fx-border-radius: 3;"
        );
        return marcador;
    }

    private void realizarJogada() {

        Jogador jogadorAtual = jogadores.get(indiceJogadorAtual);  // Obtem o jogador atual

        if (jogadorAtual.rodadasParalisado > 0) { //verifica se o jogador esta paralisado e diminui 1
            jogadorAtual.setRodadasParaliso(jogadorAtual.rodadasParalisado - 1);
            mostrarMensagem(jogadorAtual.getNomeJogador() + " está paralisado e perde a rodada.");
            avancarJogador(); //chama metodo para avancar para o proximo jogador
            return;
        }

        if (modoJogo == Dados.ModoJogo.DEBUG) { //se estiver no debug ele pede os valores dos dados
            obterValoresDadosDebug(jogadorAtual);
        } else {
            jogadorAtual.dados.rolarDados(jogadorAtual.getTipoJogador()); //se estiver no modo normal e chamado rolar dados que esta na classe dados
        }
        mostrarResultadoDados(jogadorAtual);  //chama metodo que mostra o resultado dos dados
        iniciarAnimacaoMovimento(jogadorAtual); //chama animacao para fazer o toten do personagem pecorrer as casas
    }

    private void mostrarMensagem(String texto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(texto);
        alert.setHeaderText(null);
        alert.setTitle("Mensagem");
        alert.showAndWait();
    }

    private void avancarJogador() {
        Jogador jogadorAtual = jogadores.get(indiceJogadorAtual);

        if (jogadorAtual.dados.saoDadosIguais()) {
            Platform.runLater(() -> {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Jogue Novamente!");
                alerta.setHeaderText(null);
                alerta.setContentText("Você tirou dois dados iguais! Que sorte! Jogue novamente.");
                alerta.showAndWait();
            });
            // Não avança o jogador, apenas atualiza a interface
            atualizarTabuleiroVisual();
            return;
        }

        indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
        atualizarTabuleiroVisual();
    }

    private void mostrarResultadoDados(Jogador jogador) {
        mostrarMensagem(jogador.getNomeJogador() + " rolou " + jogador.dados.getSomaDados() + " (" + jogador.dados.dado1 + " + " + jogador.dados.dado2 + ")");
    }

    private void obterValoresDadosDebug(Jogador jogador) {
        String dado1 = PopupManager.showTextInputPopup("Modo Debug - Dado 1", "Digite o valor para o Dado 1", "1").orElse("1");
        String dado2 = PopupManager.showTextInputPopup("Modo Debug - Dado 2", "Digite o valor para o Dado 2", "1").orElse("1");

        try {
            jogador.dados.dado1 = Integer.parseInt(dado1);// converte para inteiros e armazena
            jogador.dados.dado2 = Integer.parseInt(dado2);
            jogador.dados.somaDados = jogador.dados.dado1 + jogador.dados.dado2;
        } catch (NumberFormatException e) { //caso o usuario digite letras ira dar erro e sera setado um valor padrao de 2
            PopupManager.showWarningPopup("Valor Inválido", "Digite apenas números inteiros!.");
            jogador.dados.dado1 = 1;
            jogador.dados.dado2 = 1;
            jogador.dados.somaDados = 2;
        }
    }

    private void iniciarAnimacaoMovimento(Jogador jogador) {
        int origem = jogador.getPosicaoAtual(); //pega posicao atual
        int destino = Math.min(origem + jogador.dados.getSomaDados(), 39); // calcula posicao final mas nao passa do final

        Timeline animacao = new Timeline(); // Cria uma linha do tempo para a animação usando uma blibioteca do javafx
        List<KeyFrame> movimentos = new ArrayList<>(); //cria uma lista para cada frame da animacao

        for (int i = origem + 1, passo = 1; i <= destino; i++, passo++) { //loop q cria um Frame para cada casa que o jogador deve percorrer
            int posicao = i;
            movimentos.add(new KeyFrame(Duration.seconds(passo * 0.3), e -> { // Cria um frame que será executado após um tempo progressivo
                jogador.setPosicaoAtual(posicao); // Atualiza posição
                atualizarTabuleiroVisual(); // Atualiza interface
                musica.tocarEfeito(AUDIO_CLIQUE); //adiciona efeito sonoro
            }));
        }
        // Adiciona todos os frames a animacao, chama o metodo finalizar jogada e dar play
        animacao.getKeyFrames().addAll(movimentos);
        animacao.setOnFinished(e -> finalizarJogada(jogador, destino));
        animacao.play();
    }

    private void finalizarJogada(Jogador jogador, int destino) {
        jogador.casasAndadas += jogador.dados.getSomaDados(); // Atualiza contador de casas andadas
        tabuleiro.verificarCasaEspecial(jogador); //usando o metodo da classe tabuleiro, verifica a casa especial

        if (jogador.getPosicaoAtual() >= 39) { //caso um jogador venca, ira aparecer um popup e mostrar as estatisticas
            Platform.runLater(() -> {
                musica.parar();
                musica.tocarEfeito(AUDIO_APLAUSOS);

                Alert alertVitoria = new Alert(Alert.AlertType.INFORMATION);
                alertVitoria.setTitle("Vitória!");
                alertVitoria.setHeaderText("🏆 " + jogador.getNomeJogador() + " venceu!");
                alertVitoria.setContentText("Clique para ver as estatísticas do jogo");

                ButtonType btnContinuar = new ButtonType("Ver Estatísticas", ButtonBar.ButtonData.OK_DONE);
                alertVitoria.getButtonTypes().setAll(btnContinuar);

                alertVitoria.showAndWait().ifPresent(response -> {
                    mostrarEstatisticasJogo(jogador); //chama o metodo q exibe o popup com todas estatisticas
                });
            });
        } else {
            avancarJogador(); //se o jogador nao venceu simplesmente avanca para o proximo jogador
        }
    }

    private void mostrarEstatisticasJogo(Jogador vencedor) {

        StringBuilder estatisticas = new StringBuilder();


        estatisticas.append("RESUMO DO JOGO\n\n"); // Cabeçalho do relatório
        estatisticas.append("🏆 Vencedor: ").append(vencedor.getNomeJogador()).append("\n\n");
        estatisticas.append("ESTATÍSTICAS DOS JOGADORES:\n");


        for (Jogador jogador : jogadores) {  // Loop através de todos os jogadores para pegar informacoes
            estatisticas.append("\n• ").append(jogador.getNomeJogador())
                    .append(" (").append(jogador.getTipoJogador()).append("):\n")
                    .append("   - Posição final: Casa ").append(jogador.getPosicaoAtual()).append("\n")
                    .append("   - Total de casas andadas: ").append(jogador.casasAndadas).append("\n")
                    .append("   - Cor: ").append(jogador.getCorHex()).append("\n");
        }

        Alert alertEstatisticas = new Alert(Alert.AlertType.INFORMATION);  // Cria um diálogo de alerta para mostrar as estatísticas
        alertEstatisticas.setTitle("Estatísticas do Jogo");
        alertEstatisticas.setHeaderText("Desempenho de Todos os Jogadores");

        TextArea textArea = new TextArea(estatisticas.toString()); // Cria uma área de texto para exibir as estatísticas
        textArea.setEditable(false);  // Impede edição
        textArea.setWrapText(true);   // Quebra de linha automática
        textArea.setPrefSize(400, 300);  // Tamanho

        alertEstatisticas.getDialogPane().setContent(textArea); // Substitui o conteúdo padrão do alerta pela TextArea

        ButtonType btnMenu = new ButtonType("Voltar ao Menu", ButtonBar.ButtonData.OK_DONE);   // Cria botão para voltar ao menu
        alertEstatisticas.getButtonTypes().setAll(btnMenu);  // Define como único botão

        alertEstatisticas.showAndWait().ifPresent(response -> {   // Ação executada quando o diálogo é fechado
            Stage stageAtual = (Stage) botaoJogar.getScene().getWindow(); // Obtém a janela atual através do botaoJogar
            stageAtual.close();  // Fecha a janela do jogo
            mainApp.ReiniciarJogo();   // reinicia o jogo
        });
    }
}