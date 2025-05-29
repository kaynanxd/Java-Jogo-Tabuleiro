package com.example.jogo1;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.geometry.Pos;
import java.util.*;
import java.io.File;

public class InterfaceJogo {
    private Stage primaryStage;
    private Musica musica;
    private String modoDeJogoSelecionado;
    private int[] posicaoAlimentoSelecionada;
    private List<int[]> posicoesBombasSelecionadas = new ArrayList<>();
    private List<int[]> posicoesPedrasSelecionadas = new ArrayList<>();
    private Map<RoboBase, Color> corDosRobos = new HashMap<>(); //mapa com cor das imagems do robo
    private Map<String, Color> coresPorJogador = new HashMap<>(); //mapa com as cores dos robos
    private Map<RoboBase, ImageView> roboViews = new HashMap<>();
    private Color corSelecionadaAtual;
    private Main mainApp;

    private static final Color[] CORES_DISPONIVEIS = {
            Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.PURPLE, Color.ORANGE
    };

    private static final String AUDIO_CONFIRM = "src/audios/confirm.mp3";
    private static final String AUDIO_CLIQUE = "src/audios/clique.mp3";
    private static final String AUDIO_APLAUSO = "src/audios/aplausos.mp3";
    private static final String AUDIO_DERROTA = "src/audios/derrota.mp3";
    private static final String AUDIO_EXPLOSAO = "src/audios/explosao.mp3";

    private final Map<Color, String> COR_PARA_IMAGEM = Map.of(
            Color.RED, "src/imagens/avatar_red.png",
            Color.BLUE, "src/imagens/avatar_blue.png",
            Color.GREEN, "src/imagens/avatar_green.png",
            Color.YELLOW, "src/imagens/avatar_yellow.png",
            Color.PURPLE, "src/imagens/avatar_purple.png",
            Color.ORANGE, "src/imagens/avatar_orange.png"
    );

    public InterfaceJogo(Stage primaryStage, Musica musica,Main mainApp) {
        this.primaryStage = primaryStage;
        this.musica = musica;
        this.mainApp = mainApp;
    }

    public void setModoDeJogoSelecionado(String modo) {
        this.modoDeJogoSelecionado = modo;
    }

    public void reiniciarJogo() {
        corDosRobos.clear();
        posicoesPedrasSelecionadas.clear();
        posicoesBombasSelecionadas.clear();
        coresPorJogador.clear();
        roboViews.clear();
    }

    public void mostrarFormularioJogador(int quantidadeJogadores) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #e0ffff;");

        VBox containerFormularios = new VBox(20);
        containerFormularios.setAlignment(Pos.CENTER);
        containerFormularios.setLayoutX(50);
        containerFormularios.setLayoutY(20);

        StackPane formularioJogador1 = criarFormularioIndividual("Robo 1");
        containerFormularios.getChildren().add(formularioJogador1);

        if (quantidadeJogadores > 1) {
            StackPane formularioJogador2 = criarFormularioIndividual("Robo 2");
            containerFormularios.getChildren().add(formularioJogador2);
        }

        Button botaoProximo = new Button("Iniciar Jogo");
        botaoProximo.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        botaoProximo.setOnAction(e -> {
            //verifica se os robos nao estao com a mesma cor e vai pra tela de selecao do alimento
            Color corRobo1 = coresPorJogador.get("Robo 1");
            Color corRobo2 = coresPorJogador.get("Robo 2");
            if (corRobo1 == corRobo2 ) {
                new Alert(Alert.AlertType.WARNING, "Selecione uma cor Diferente para algum Robo.").show();
                return;
            }
            musica.tocarEfeito(AUDIO_CONFIRM);
            mostrarSelecionarPosicaoAlimento();
        });

        containerFormularios.getChildren().add(botaoProximo);
        root.getChildren().add(containerFormularios);

        Scene scene = new Scene(root, 650, quantidadeJogadores > 1 ? 600 : 350);
        primaryStage.setTitle("Configurar Jogadores");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane criarFormularioIndividual(String tituloJogador) {
        StackPane container = new StackPane();
        container.setStyle("-fx-background-color: #6699FF; -fx-background-radius: 15;");
        container.setPrefSize(400, 250);

        HBox conteudo = new HBox(20);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setPadding(new Insets(20));

        ImageView visualizadorImagem = new ImageView();
        visualizadorImagem.setFitWidth(200);
        visualizadorImagem.setFitHeight(200);
        VBox parteEsquerda = new VBox(visualizadorImagem);
        parteEsquerda.setAlignment(Pos.CENTER);

        VBox parteDireita = new VBox(15);
        parteDireita.setAlignment(Pos.CENTER_LEFT);
        parteDireita.setPadding(new Insets(20));

        Label titulo = new Label(tituloJogador);
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label corLabel = new Label("Cor:");
        corLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        HBox seletorCores = criarSeletorCores(visualizadorImagem, tituloJogador);

        parteDireita.getChildren().addAll(titulo, corLabel, seletorCores);
        conteudo.getChildren().addAll(parteEsquerda, parteDireita);
        container.getChildren().add(conteudo);

        return container;
    }

    private HBox criarSeletorCores(ImageView visualizadorImagem, String nomeJogador) {
        HBox container = new HBox(10);
        Button[] botoesCoresLocal = new Button[CORES_DISPONIVEIS.length];

        Color corInicial=CORES_DISPONIVEIS[0];  //seta as cores padrao dos robos
        if (nomeJogador.equalsIgnoreCase("robo 1")) {
            corInicial = Color.RED;
        } else if (nomeJogador.equalsIgnoreCase("robo 2")) {
            corInicial = Color.BLUE;
        }
        corSelecionadaAtual = corInicial;
        coresPorJogador.put(nomeJogador, corSelecionadaAtual); //coloca a cor na lista com as cores dos jogadores
        carregarImagemAvatar(visualizadorImagem, corSelecionadaAtual); //carrega a imagem da cor do robo

        for (int i = 0; i < CORES_DISPONIVEIS.length; i++) {
            Color cor = CORES_DISPONIVEIS[i];
            Button botaoCor = new Button();
            botoesCoresLocal[i] = botaoCor;
            botaoCor.setUserData(cor);

            botaoCor.setStyle("-fx-background-color: " + toHexString(cor) +
                    "; -fx-min-width: 30px; -fx-min-height: 30px;");

            botaoCor.setOnAction(e -> {
                musica.tocarEfeito(AUDIO_CLIQUE);
                corSelecionadaAtual = cor;
                coresPorJogador.put(nomeJogador, cor);

                // Remove bordas de todos os botões deste seletor
                for (Button btn : botoesCoresLocal) {
                    if (btn != null) {
                        Color btnColor = (Color) btn.getUserData();
                        btn.setStyle("-fx-background-color: " + toHexString(btnColor) +
                                "; -fx-min-width: 30px; -fx-min-height: 30px;");
                    }
                }

                // Adiciona borda no botão clicado
                botaoCor.setStyle("-fx-background-color: " + toHexString(cor) +
                        "; -fx-min-width: 30px; -fx-min-height: 30px;" +
                        " -fx-border-color: black; -fx-border-width: 3px;");

                carregarImagemAvatar(visualizadorImagem, cor);
            });

            container.getChildren().add(botaoCor);
        }
            // coloca no botao selecionado uma borda preta ao criar o seletor
        for (Button btn : botoesCoresLocal) {
            if (btn != null && btn.getUserData().equals(corSelecionadaAtual)) {
                btn.setStyle("-fx-background-color: " + toHexString(corSelecionadaAtual) +
                        "; -fx-min-width: 30px; -fx-min-height: 30px;" +
                        " -fx-border-color: black; -fx-border-width: 3px;");
                break;
            }
        }

        return container;
    }

    private void carregarImagemAvatar(ImageView visualizadorImagem, Color cor) {
            String caminhoImagem = COR_PARA_IMAGEM.get(cor);
            Image image = new Image(new File(caminhoImagem).toURI().toString());
            visualizadorImagem.setImage(image);
    }

    private void mostrarSelecionarPosicaoAlimento() {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #e0ffff;");

        VBox containerPrincipal = new VBox(20);
        containerPrincipal.setAlignment(Pos.CENTER);
        containerPrincipal.setLayoutX(50);
        containerPrincipal.setLayoutY(20);

        Label titulo = new Label("Escolha o local do alimento");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        containerPrincipal.getChildren().add(titulo);

        GridPane matriz = criarMatrizVisual();

        for (Node node : matriz.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle celula = (Rectangle) node;

                celula.setOnMouseClicked(e -> {
                    musica.tocarEfeito(AUDIO_CLIQUE);

                    matriz.getChildren().forEach(n -> {
                        if (n instanceof Rectangle) {
                            ((Rectangle) n).setFill(Color.WHITE);
                        }
                    });

                    matriz.getChildren().removeIf(n -> n instanceof ImageView);

                    celula.setFill(Color.LIGHTGREEN);
                    posicaoAlimentoSelecionada = (int[]) celula.getUserData();

                    ImageView alimentoImg = new ImageView(new Image("file:src/imagens/alimento.png"));
                    alimentoImg.setFitWidth(70);
                    alimentoImg.setFitHeight(60);
                    matriz.add(alimentoImg, posicaoAlimentoSelecionada[1], posicaoAlimentoSelecionada[0]);
                });
            }
        }

        containerPrincipal.getChildren().add(matriz);

        Button btnConfirmar = new Button("Confirmar");
        btnConfirmar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnConfirmar.setOnAction(e -> {
            if (posicaoAlimentoSelecionada == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Atenção");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, selecione uma posição para o alimento!");
                alert.showAndWait();
                return;
            }

            System.out.println("Alimento posicionado em: [" + posicaoAlimentoSelecionada[1] +
                    ", " + posicaoAlimentoSelecionada[0] + "]");

            musica.tocarEfeito(AUDIO_CONFIRM);
            if ("SURVIVALBOTS".equals(modoDeJogoSelecionado)) {
                mostrarSelecionarBombas();
            } else {
                iniciarJogoCompleto();
            }
        });

        containerPrincipal.getChildren().add(btnConfirmar);
        root.getChildren().add(containerPrincipal);

        Scene scene = new Scene(root, 500, 550);
        primaryStage.setTitle("Posicionar Alimento");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarSelecionarBombas() {
        Pane root = new Pane();
        VBox containerPrincipal = new VBox(20);
        containerPrincipal.setAlignment(Pos.CENTER);
        containerPrincipal.setLayoutX(50);
        containerPrincipal.setLayoutY(20);

        Label titulo = new Label("Escolha as posições das bombas");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #8b0000;");
        containerPrincipal.getChildren().add(titulo);

        GridPane matriz = criarMatrizVisual();

        for (Node node : matriz.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle celula = (Rectangle) node;
                celula.setOnMouseClicked(e -> {
                    musica.tocarEfeito(AUDIO_CLIQUE);

                    int[] pos = (int[]) celula.getUserData();

                    // Verifica se a posição já tem o alimento
                    if (posicaoAlimentoSelecionada != null &&
                            Arrays.equals(pos, posicaoAlimentoSelecionada)) {
                        mostrarAlertaPosicaoOcupada("alimento");
                        return;
                    }
                    if (pos[0] == 0 && pos[1] == 0) {
                        mostrarAlertaPosicaoOcupada("Robo");
                        return;
                    }
                    // Verifica se já tem uma pedra
                    boolean temPedra = posicoesPedrasSelecionadas.stream()
                            .anyMatch(p -> Arrays.equals(p, pos));

                    if (temPedra) {
                        mostrarAlertaPosicaoOcupada("pedra");
                        return;
                    }

                    boolean jaSelecionada = posicoesBombasSelecionadas.stream()
                            .anyMatch(p -> Arrays.equals(p, pos));
                    if (jaSelecionada) {
                        posicoesBombasSelecionadas.removeIf(p -> Arrays.equals(p, pos));
                        celula.setFill(Color.WHITE);
                        matriz.getChildren().removeIf(n -> n instanceof ImageView &&
                                GridPane.getColumnIndex(n) == pos[1] &&
                                GridPane.getRowIndex(n) == pos[0]);
                    } else {
                        posicoesBombasSelecionadas.add(pos);
                        celula.setFill(Color.RED);
                        ImageView bombaImg = new ImageView(new Image("file:src/imagens/bomba.png"));
                        bombaImg.setFitWidth(70);
                        bombaImg.setFitHeight(60);
                        matriz.add(bombaImg, pos[1], pos[0]);
                    }
                });
            }
        }

        containerPrincipal.getChildren().add(matriz);

        Button btnConfirmar = new Button("Confirmar Bombas");
        btnConfirmar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnConfirmar.setOnAction(e -> mostrarSelecionarPedras());

        containerPrincipal.getChildren().add(btnConfirmar);
        root.getChildren().add(containerPrincipal);

        Scene scene = new Scene(root, 550, 600);
        primaryStage.setTitle("Selecionar Bombas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarSelecionarPedras() {
        Pane root = new Pane();
        VBox containerPrincipal = new VBox(20);
        containerPrincipal.setAlignment(Pos.CENTER);
        containerPrincipal.setLayoutX(50);
        containerPrincipal.setLayoutY(20);

        Label titulo = new Label("Escolha as posições das pedras");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #555;");
        containerPrincipal.getChildren().add(titulo);

        GridPane matriz = criarMatrizVisual();

        for (Node node : matriz.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle celula = (Rectangle) node;
                celula.setOnMouseClicked(e -> {
                    musica.tocarEfeito(AUDIO_CLIQUE);

                    int[] pos = (int[]) celula.getUserData();

                    // Verifica se a posição já tem o alimento
                    if (posicaoAlimentoSelecionada != null &&
                            Arrays.equals(pos, posicaoAlimentoSelecionada)) {
                        mostrarAlertaPosicaoOcupada("alimento");
                        return;
                    }

                    if (pos[0] == 0 && pos[1] == 0) {
                        mostrarAlertaPosicaoOcupada("Robo");
                        return;
                    }

                    // Verifica se já tem uma bomba nesta posição
                    boolean temBomba = posicoesBombasSelecionadas.stream()
                            .anyMatch(p -> Arrays.equals(p, pos));

                    if (temBomba) {
                        mostrarAlertaPosicaoOcupada("bomba");
                        return;
                    }

                    boolean jaSelecionada = posicoesPedrasSelecionadas.stream()
                            .anyMatch(p -> Arrays.equals(p, pos));
                    if (jaSelecionada) {
                        posicoesPedrasSelecionadas.removeIf(p -> Arrays.equals(p, pos));
                        celula.setFill(Color.WHITE);
                        matriz.getChildren().removeIf(n -> n instanceof ImageView &&
                                GridPane.getColumnIndex(n) == pos[1] &&
                                GridPane.getRowIndex(n) == pos[0]);
                    } else {
                        posicoesPedrasSelecionadas.add(pos);
                        celula.setFill(Color.LIGHTGREEN);
                        ImageView pedraImg = new ImageView(new Image("file:src/imagens/pedra.png"));
                        pedraImg.setFitWidth(70);
                        pedraImg.setFitHeight(60);
                        matriz.add(pedraImg, pos[1], pos[0]);
                    }
                });
            }
        }

        containerPrincipal.getChildren().add(matriz);

        Button btnConfirmar = new Button("Iniciar Jogo");
        btnConfirmar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnConfirmar.setOnAction(e -> iniciarJogoCompleto());

        containerPrincipal.getChildren().add(btnConfirmar);
        root.getChildren().add(containerPrincipal);

        Scene scene = new Scene(root, 550, 600);
        primaryStage.setTitle("Selecionar Pedras");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarAlertaPosicaoOcupada(String elemento) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Posição ocupada");
        alert.setHeaderText(null);
        alert.setContentText("Esta posição já contém " + elemento + "! Escolha outra posição.");
        alert.showAndWait();
    }

    private GridPane criarMatrizVisual() {
        GridPane matriz = new GridPane();
        matriz.setHgap(0);
        matriz.setVgap(0);
        matriz.setAlignment(Pos.CENTER);
        matriz.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        for (int linha = 0; linha < 5; linha++) {
            for (int coluna = 0; coluna < 5; coluna++) {
                Rectangle celula = new Rectangle(80, 80);
                celula.setFill(Color.WHITE);
                celula.setStroke(Color.BLACK);
                celula.setStrokeWidth(1);
                celula.setUserData(new int[]{linha, coluna});
                celula.setId("celula-" + linha + "-" + coluna);

                matriz.add(celula, coluna, linha);
            }
        }
        return matriz;
    }

    private void exibirElementosNaMatriz(GridPane matriz) {
        if (posicaoAlimentoSelecionada != null) {
            int linha = posicaoAlimentoSelecionada[0];
            int coluna = posicaoAlimentoSelecionada[1];

            ImageView alimentoImg = new ImageView(new Image("file:src/imagens/alimento.png"));
            alimentoImg.setFitWidth(70);
            alimentoImg.setFitHeight(60);
            matriz.add(alimentoImg, coluna, linha);
        }

        for (int[] pos : posicoesBombasSelecionadas) {
            int linha = pos[0];
            int coluna = pos[1];

            ImageView bombaImg = new ImageView(new Image("file:src/imagens/bomba.png"));
            bombaImg.setFitWidth(70);
            bombaImg.setFitHeight(60);
            matriz.add(bombaImg, coluna, linha);
        }

        for (int[] pos : posicoesPedrasSelecionadas) {
            int linha = pos[0];
            int coluna = pos[1];

            ImageView pedraImg = new ImageView(new Image("file:src/imagens/pedra.png"));
            pedraImg.setFitWidth(70);
            pedraImg.setFitHeight(60);
            matriz.add(pedraImg, coluna, linha);
        }
    }

    private void atualizarMatriz(GridPane matriz, BaseJogo jogo) {
        matriz.getChildren().removeIf(node ->
                node instanceof ImageView && !roboViews.containsValue(node)
        );

        exibirElementosNaMatriz(matriz);

        if (jogo instanceof MainNormal) {
            RoboBase robo = ((MainNormal) jogo).getRoboManual();
            adicionarRoboNaMatriz(matriz, robo);
        }
        else if (jogo instanceof MainMachine) {
            MainMachine machine = (MainMachine) jogo;
            for (RoboBase robo : machine.getRobos()) {
                adicionarRoboNaMatriz(matriz, robo);
            }
        }
        else if (jogo instanceof MainDualMission) {
            MainDualMission dualMission = (MainDualMission) jogo;
            for (RoboBase robo : dualMission.getRobos()) {
                adicionarRoboNaMatriz(matriz, robo);
            }
        }
        else if (jogo instanceof MainSurvivalBots) {
            MainSurvivalBots survivalBots = (MainSurvivalBots) jogo;
            for (RoboBase robo : survivalBots.getRobos()) {
                adicionarRoboNaMatriz(matriz, robo);
            }
        }
    }

    private void adicionarRoboNaMatriz(GridPane matriz, RoboBase robo) {
        if (!robo.isAtivo()) {
            ImageView img = roboViews.get(robo);
            if (img != null) {

                // Mostra alerta
                Platform.runLater(() -> {
                    musica.tocarEfeito(AUDIO_EXPLOSAO);

                    Stage alertStage = new Stage();
                    alertStage.initModality(Modality.NONE);
                    alertStage.setTitle("Robô Explodiu!");

                    Label message = new Label("O robô " + robo.getCor() + " foi destruído por uma bomba!");
                    VBox content = new VBox(message);
                    content.setAlignment(Pos.CENTER);
                    content.setPadding(new Insets(15));

                    Scene scene = new Scene(content, 300, 100);
                    alertStage.setScene(scene);

                    Window mainWindow = matriz.getScene().getWindow();
                    alertStage.setX(mainWindow.getX() + mainWindow.getWidth() - 850);
                    alertStage.setY(mainWindow.getY() + 100);

                    alertStage.show();

                    // Fecha automaticamente após 3 segundos
                    PauseTransition delay = new PauseTransition(Duration.seconds(3));
                    delay.setOnFinished(e -> alertStage.close());
                    delay.play();
                });
                // Remove a imagem do robô
                matriz.getChildren().remove(img);
            }
            roboViews.remove(robo);
            return;
        }

        ImageView roboImg = roboViews.get(robo);

        if (roboImg == null) {

            String corRoboStr = robo.getCor().toLowerCase();
            Color corRobo = converterStringParaColor(corRoboStr);

            String caminhoImagem = COR_PARA_IMAGEM.get(corRobo);
            if (caminhoImagem == null) {
                caminhoImagem = "src/imagens/robo.png";
            }

            roboImg = new ImageView(new Image("file:" + caminhoImagem));
            roboImg.setFitWidth(70);
            roboImg.setFitHeight(60);
            roboViews.put(robo, roboImg);
            matriz.add(roboImg, robo.getX(), robo.getY());
        } else {

            int colunaAtual = Optional.ofNullable(GridPane.getColumnIndex(roboImg)).orElse(0);
            int linhaAtual = Optional.ofNullable(GridPane.getRowIndex(roboImg)).orElse(0);

            double dx = (robo.getX() - colunaAtual) * 70;
            double dy = (robo.getY() - linhaAtual) * 60;

            TranslateTransition transicao = new TranslateTransition(Duration.millis(300), roboImg);
            transicao.setByX(dx);
            transicao.setByY(dy);

            final ImageView imgFinal = roboImg;
            transicao.setOnFinished(e -> {
                GridPane.setColumnIndex(imgFinal, robo.getX());
                GridPane.setRowIndex(imgFinal, robo.getY());
                imgFinal.setTranslateX(0);
                imgFinal.setTranslateY(0);
            });

            musica.tocarEfeito(AUDIO_CLIQUE);
            transicao.play();
        }
    }

    private void iniciarJogoCompleto() {

        switch (modoDeJogoSelecionado) {
            case "NORMAL":
                iniciarJogoNormal();
                break;
            case "MACHINE":
                iniciarJogoMachine();
                break;
            case "DUAL_MISSION":
                iniciarJogoDualMission();
                break;
            case "SURVIVALBOTS":
                iniciarJogoSurvivalBots();
                break;
            default:
                System.out.println("Modo de jogo não reconhecido.");
        }
    }

    private void iniciarJogoNormal() {
        int[] pos = posicaoAlimentoSelecionada;
        Color corRobo1 = coresPorJogador.get("Robo 1");
        String nomeCor = converterColorParaString(corRobo1);

        MainNormal jogo = new MainNormal();
        jogo.executarJogo(nomeCor, pos[1], pos[0]);

        Pane root = new Pane();
        root.setStyle("-fx-background-color: #ffffff;");

        Label titulo = new Label("Modo Normal - Jogo Iniciado");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titulo.setLayoutX(200);
        titulo.setLayoutY(20);

        GridPane matriz = criarMatrizVisual();
        matriz.setLayoutX(100);
        matriz.setLayoutY(80);

        exibirElementosNaMatriz(matriz);
        atualizarMatriz(matriz, jogo);

        // Adiciona os controles de movimento
        Pane controles = criarControlesMovimento(jogo, matriz);

        root.getChildren().addAll(titulo, matriz, controles);

        Scene scene = new Scene(root, 600, 650);
        primaryStage.setTitle("Jogo Normal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void iniciarJogoMachine() {
        int[] pos = posicaoAlimentoSelecionada;
        Color cor1 = coresPorJogador.get("Robo 1");
        Color cor2 = coresPorJogador.get("Robo 2");
        String nomecor1=converterColorParaString(cor1);
        String nomecor2=converterColorParaString(cor2);

        MainMachine jogo = new MainMachine();
        List<RoboBase> robos = jogo.getRobos();
        jogo.executarJogo(nomecor1, nomecor2, pos[1], pos[0]);
        corDosRobos.put(robos.get(0), cor1);
        corDosRobos.put(robos.get(1), cor2);

        Pane root = new Pane();
        root.setStyle("-fx-background-color: #ffffff;");

        Label titulo = new Label("Modo Machine - Jogo Iniciado");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titulo.setLayoutX(200);
        titulo.setLayoutY(20);

        GridPane matriz = criarMatrizVisual();
        matriz.setLayoutX(100);
        matriz.setLayoutY(80);

        exibirElementosNaMatriz(matriz);
        atualizarMatriz(matriz, jogo);

        root.getChildren().addAll(titulo, matriz);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Modo Machine");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Corrigido: wrapper para acessar o loop dentro do lambda
        final Timeline[] loopIA = new Timeline[1];
        loopIA[0] = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            boolean achou = jogo.atualizarMovimentoIA();
            atualizarMatriz(matriz, jogo);

            if (achou) {
                String vencedor = jogo.getRobos().stream()
                        .filter(robo -> jogo.encontrouAlimento(robo))
                        .map(RoboBase::getCor)
                        .findFirst()
                        .orElse("IA");

                botaoVitoria(vencedor, jogo.getRobos());
                loopIA[0].stop(); // acessa corretamente agora
            }
        }));
        loopIA[0].setCycleCount(Animation.INDEFINITE);
        loopIA[0].play();
    }

    private void iniciarJogoDualMission() {
        int[] pos = posicaoAlimentoSelecionada;
        Color cor1 = coresPorJogador.get("Robo 1");
        Color cor2 = coresPorJogador.get("Robo 2");
        String nomecor1=converterColorParaString(cor1);
        String nomecor2=converterColorParaString(cor2);

        MainDualMission jogo = new MainDualMission();
        List<RoboBase> robos = jogo.getRobos();

        jogo.executarJogo(nomecor1,nomecor2, pos[1], pos[0]);
        corDosRobos.put(robos.get(0), cor1);
        corDosRobos.put(robos.get(1), cor2);

        Pane root = new Pane();
        root.setStyle("-fx-background-color: #ffffff;");

        Label titulo = new Label("Modo Dual Mission - Jogo Iniciado");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titulo.setLayoutX(200);
        titulo.setLayoutY(20);

        GridPane matriz = criarMatrizVisual();
        matriz.setLayoutX(100);
        matriz.setLayoutY(80);

        exibirElementosNaMatriz(matriz);
        atualizarMatriz(matriz, jogo);

        root.getChildren().addAll(titulo, matriz);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Modo Dual Mission");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Corrigido: wrapper para acessar o loop dentro do lambda
        final Timeline[] loopIA = new Timeline[1];
        loopIA[0] = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            boolean achou = jogo.atualizarMovimentoIA();
            atualizarMatriz(matriz, jogo);

            if (achou) {
                botaoVitoria("Ambos os robos encontraram o alimento!", jogo.getRobos());
                loopIA[0].stop();
            }
        }));
        loopIA[0].setCycleCount(Animation.INDEFINITE);
        loopIA[0].play();
    }

    private void iniciarJogoSurvivalBots() {
        mostrarSelecionarBombas();
        mostrarSelecionarPedras();
        int[] pos = posicaoAlimentoSelecionada;
        List<int[]> pos1 = posicoesBombasSelecionadas;
        List<int[]> pos2 = posicoesPedrasSelecionadas;
        Color cor1 = coresPorJogador.get("Robo 1");
        Color cor2 = coresPorJogador.get("Robo 2");
        String nomecor1=converterColorParaString(cor1);
        String nomecor2=converterColorParaString(cor2);

        MainSurvivalBots jogo = new MainSurvivalBots();
        List<RoboBase> robos = jogo.getRobos();
        jogo.setPosicoesBombasSelecionadas(pos1);
        jogo.executarJogo(nomecor1,nomecor2, pos[1], pos[0], pos1, pos2);
        corDosRobos.put(robos.get(0), cor1);
        corDosRobos.put(robos.get(1), cor2);

        Pane root = new Pane();
        root.setStyle("-fx-background-color: #ffffff;");

        Label titulo = new Label("Modo Survival Bots - Jogo Iniciado");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titulo.setLayoutX(200);
        titulo.setLayoutY(20);

        GridPane matriz = criarMatrizVisual();
        matriz.setLayoutX(100);
        matriz.setLayoutY(80);

        exibirElementosNaMatriz(matriz);
        atualizarMatriz(matriz, jogo);

        root.getChildren().addAll(titulo, matriz);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Modo Survival Bots");
        primaryStage.setScene(scene);
        primaryStage.show();

        final Timeline[] loopIA = new Timeline[1];
        loopIA[0] = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            boolean achou = jogo.atualizarMovimentoIA();
            atualizarMatriz(matriz, jogo);

            if (achou) {
                Optional<String> vencedor = jogo.getRobos().stream()
                        .filter(robo -> jogo.encontrouAlimento(robo))
                        .map(RoboBase::getCor)
                        .findFirst();

                loopIA[0].stop();

                if (vencedor.isPresent()) {
                    botaoVitoria(vencedor.get(), jogo.getRobos());
                } else {
                    botaoDerrota(jogo.getRobos());
                }
            }
        }));
        loopIA[0].setCycleCount(Animation.INDEFINITE);
        loopIA[0].play();
    }

    private Pane criarControlesMovimento(MainNormal jogo, GridPane matriz) {

        VBox containerControles = new VBox(20);
        containerControles.setAlignment(Pos.CENTER);

        Label instrucao = new Label("Digite os comandos:\n1/Up    2/down    3/Right    4/Left");
        instrucao.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");
        instrucao.setAlignment(Pos.CENTER);

        TextField campoInput = new TextField();
        campoInput.setStyle("-fx-font-size: 18px; -fx-max-width: 200px; -fx-alignment: center;");
        campoInput.setPromptText("Escreva Aqui");

        campoInput.setOnAction(e -> {
            String input = campoInput.getText().trim();

            if (!input.isEmpty()) {
                musica.tocarEfeito(AUDIO_CLIQUE);
                boolean resultado = jogo.moverRobo(input);
                atualizarMatriz(matriz, jogo);

                // Limpa o campo após o envio
                campoInput.clear();

                if(resultado) {
                    botaoVitoria(jogo.getRoboManual().getCor(), Arrays.asList(jogo.getRoboManual()));
                }
            }
        });

        containerControles.getChildren().addAll(instrucao, campoInput);

        containerControles.setLayoutX(200);
        containerControles.setLayoutY(500);

        return containerControles;
    }

    public void botaoVitoria(String corRobo, List<RoboBase> robos) {
        Platform.runLater(() -> {
            musica.tocarEfeito(AUDIO_APLAUSO);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Parabéns!");
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setResizable(false);
            popupStage.setOnCloseRequest(e -> e.consume());

            VBox content = new VBox(20);
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: #c4ffb0;");

            Label header = new Label("Vitória do Robo " + corRobo.toUpperCase());
            header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            Label message = new Label("O robo encontrou o alimento com sucesso!");

            Label estatisticasLabel = new Label(mostrarEstatisticas(robos));
            estatisticasLabel.setStyle("-fx-font-size: 12px;");
            estatisticasLabel.setWrapText(true);

            Button voltarMenuButton = new Button("Voltar ao Menu");
            voltarMenuButton.setStyle("-fx-font-size: 14px; -fx-padding: 5 15;");
            voltarMenuButton.setOnAction(e -> {
                popupStage.close();
                mainApp.ReiniciarJogo();
            });

            content.getChildren().addAll(header, message, estatisticasLabel, voltarMenuButton);

            Scene scene = new Scene(content, 600, 250);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        });
    }


    public void botaoDerrota( List<RoboBase> robos) {
        Platform.runLater(() -> {
            musica.tocarEfeito(AUDIO_DERROTA);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Todos Perderam!");
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setResizable(false);

            // Remove o botão de fechar (X)
            popupStage.setOnCloseRequest(e -> e.consume());

            VBox content = new VBox(20);
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: #ffb0bd;");

            Label header = new Label("Derrota de todos os robôs");
            header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            Label estatisticasLabel = new Label(mostrarEstatisticas(robos));
            estatisticasLabel.setStyle("-fx-font-size: 12px;");
            estatisticasLabel.setWrapText(true);

            Label message = new Label("Infelizmente ninguém venceu");

            Button voltarMenuButton = new Button("Voltar ao Menu");
            voltarMenuButton.setStyle("-fx-font-size: 14px; -fx-padding: 5 15;");
            voltarMenuButton.setOnAction(e -> {
                popupStage.close();
                mainApp.ReiniciarJogo();
            });

            content.getChildren().addAll(header, message,estatisticasLabel, voltarMenuButton);

            Scene scene = new Scene(content, 600, 250);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        });
    }

    public String mostrarEstatisticas(List<RoboBase> robos) {
        StringBuilder sb = new StringBuilder();
        for (RoboBase robo : robos) {
            int movimentosTotais = robo.numMovimentosValidos;
            int movimentosValidosReais = movimentosTotais - robo.numMovimentosInvalidos;

            sb.append("Robô ").append(robo.getCor())
                    .append(" - Movimentos válidos: ").append(movimentosValidosReais)
                    .append("       Movimentos inválidos: ").append(robo.numMovimentosInvalidos)
                    .append("       Movimentos totais: ").append(movimentosTotais)
                    .append("\n");
        }
        return sb.toString();
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    private String converterColorParaString(Color cor) {
        if (cor.equals(Color.RED)) return "vermelho";
        if (cor.equals(Color.BLUE)) return "azul";
        if (cor.equals(Color.GREEN)) return "verde";
        if (cor.equals(Color.YELLOW)) return "amarelo";
        if (cor.equals(Color.PURPLE)) return "roxo";
        if (cor.equals(Color.ORANGE)) return "laranja";
        return "azul";
    }
    private Color converterStringParaColor(String corStr) {
        switch (corStr.toLowerCase()) {
            case "vermelho": return Color.RED;
            case "azul": return Color.BLUE;
            case "verde": return Color.GREEN;
            case "amarelo": return Color.YELLOW;
            case "roxo": return Color.PURPLE;
            case "laranja": return Color.ORANGE;
            default: return Color.BLUE;
        }
    }
}