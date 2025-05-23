package com.example.jogo1;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.util.*;

import javafx.scene.Node;

public class Main extends Application {

    // Constantes
    private static final Color[] CORES_DISPONIVEIS = {
            Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.PURPLE, Color.ORANGE
    };

    private static final String ESTILO_FUNDO = "-fx-background-color: #e0ffff;";
    private static final String AUDIO_CONFIRM = "src/audios/confirm.mp3";
    private static final String AUDIO_CLIQUE = "src/audios/clique.mp3";
    private static final String AUDIO_APLAUSO = "src/audios/aplausos.mp3";
    private static final String AUDIO_DERROTA = "src/audios/derrota.mp3";
    private static final String FONTE_PADRAO = "Arial";
    private String modoDeJogoSelecionado;

    // Componentes UI
    private Stage primaryStage;
    private Musica musica;
    private int numeroJogadoresSelecionado;
    private List<Color> coresUsadas = new ArrayList<>();
    private Color corSelecionadaAtual;
    private Button[] botoesCores;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.musica = new Musica("src/audios/musica.mp3");
        exibirTelaSplash();
    }

    private void exibirTelaSplash() {
        musica.tocar();
        Image imagemSplash = new Image("file:src/imagens/Splashart.png");
        ImageView visualizadorImagem = new ImageView(imagemSplash);
        visualizadorImagem.setFitWidth(800);
        visualizadorImagem.setFitHeight(600);
        visualizadorImagem.setPreserveRatio(false);

        Label mensagem = new Label("Pressione qualquer tecla para continuar");
        mensagem.setFont(Font.font(FONTE_PADRAO, 16));
        mensagem.setTextFill(Color.WHITE);
        mensagem.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10;");

        StackPane root = new StackPane(visualizadorImagem, mensagem);
        StackPane.setAlignment(mensagem, Pos.BOTTOM_CENTER);

        Scene cenaSplash = new Scene(root, 800, 600);
        cenaSplash.setOnKeyPressed((KeyEvent e) -> {
            exibirMenuPrincipal();
            musica.tocarEfeito(AUDIO_CONFIRM);
        });

        primaryStage.setScene(cenaSplash);
        primaryStage.setTitle("Splash");
        primaryStage.show();
    }

    public void ReiniciarJogo() {
        musica.parar();
        musica = new Musica("src/audios/musica.mp3");
        corDosRobos.clear();
        exibirTelaSplash();
    }

    private void exibirMenuPrincipal() {
        Button btnJogar = criarBotaoPadronizado("Jogar", "src/imagens/dice.png",
                Color.LIGHTSKYBLUE, this::exibirSelecionarModoJogo); // Direto para seleção de jogadores

        Button btnCreditos = criarBotaoPadronizado("Créditos", "src/imagens/code.png",
                Color.LIGHTSKYBLUE, this::exibirCreditos);

        Button btnSair = criarBotaoPadronizado("Sair", "src/imagens/exit.png",
                Color.LIGHTSKYBLUE, () -> System.exit(0));

        Button btnOpcoes = criarBotaoPadronizado("Opções", "src/imagens/options.png",
                Color.LIGHTSKYBLUE, this::exibirOpcoes);

        HBox containerBotoes = new HBox(20, btnJogar, btnCreditos, btnOpcoes, btnSair);
        containerBotoes.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Robo Puzzle", containerBotoes), 700, 400));
        primaryStage.setTitle("Menu Principal");
    }

    private void exibirSelecionarModoJogo() {
        List<Button> botoes = new ArrayList<>();

        Button btnNormal = criarBotaoPadronizado(" Normal", "src/imagens/robot2.png", Color.LIGHTSKYBLUE, () -> {
            mostrarFormularioJogador(1);
            modoDeJogoSelecionado = "NORMAL";// Modo normal - 1 jogador
        });

        Button btnMachine = criarBotaoPadronizado(" Machine", "src/imagens/robot3.png", Color.LIGHTSKYBLUE, () -> {
            mostrarFormularioJogador(2); // Modo machine - 2 jogadores
            modoDeJogoSelecionado = "MACHINE";
        });

        Button btnDualMission = criarBotaoPadronizado(" DualMission", "src/imagens/robot4.png", Color.LIGHTSKYBLUE, () -> {
            mostrarFormularioJogador(2); // Ajuste conforme necessário
            modoDeJogoSelecionado = "DUAL_MISSION";
        });

        Button btnSurvivalBots = criarBotaoPadronizado(" SurvivalBots", "src/imagens/dice.png", Color.LIGHTSKYBLUE, () -> {
            mostrarFormularioJogador(2); // Modo survival - 2 jogadores
            modoDeJogoSelecionado = "SURVIVALBOTS";
        });

        Button btnVoltar = criarBotaoPadronizado("Voltar", "src/imagens/back.png",
                Color.LIGHTSKYBLUE, this::exibirMenuPrincipal);

        HBox linha1 = new HBox(20, btnNormal, btnMachine, btnDualMission);
        HBox linha2 = new HBox(20, btnSurvivalBots, btnVoltar);

        linha1.setAlignment(Pos.CENTER);
        linha2.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Selecionar Modo De Jogo", linha1, linha2), 700, 500));
    }

    private void mostrarFormularioJogador(int quantidadeJogadores) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #e0ffff;");

        VBox containerFormularios = new VBox(20);
        containerFormularios.setAlignment(Pos.CENTER);
        containerFormularios.setLayoutX(50);
        containerFormularios.setLayoutY(20);

        // Criar formulário para o jogador 1
        StackPane formularioJogador1 = criarFormularioIndividual("Robô 1");
        containerFormularios.getChildren().add(formularioJogador1);

        if (quantidadeJogadores > 1) {
            StackPane formularioJogador2 = criarFormularioIndividual("Robô 2");
            containerFormularios.getChildren().add(formularioJogador2);
        }

        Button botaoProximo = new Button("Iniciar Jogo");
        botaoProximo.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        botaoProximo.setOnAction(e -> {
            Color corRobo1 = coresPorJogador.get("Robô 1");
            Color corRobo2 = coresPorJogador.get("Robô 1");
            if (corRobo1 == null || corRobo2 == null ) {
                new Alert(Alert.AlertType.WARNING, "Selecione uma cor para os robos antes de iniciar o jogo.").show();
                return;
            }

            musica.tocarEfeito(AUDIO_CONFIRM);
            mostrarSelecionarPosicaoAlimento();
        });

        containerFormularios.getChildren().add(botaoProximo);
        root.getChildren().add(containerFormularios);

        // Configuração da cena
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

        // Configuração do visualizador de imagem
        ImageView visualizadorImagem = new ImageView();
        visualizadorImagem.setFitWidth(200);
        visualizadorImagem.setFitHeight(200);
        VBox parteEsquerda = new VBox(visualizadorImagem);
        parteEsquerda.setAlignment(Pos.CENTER);

        // Configuração do formulário
        VBox parteDireita = new VBox(15);
        parteDireita.setAlignment(Pos.CENTER_LEFT);
        parteDireita.setPadding(new Insets(20));

        Label titulo = new Label(tituloJogador);
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label corLabel = criarLabel("Cor:");
        HBox seletorCores = criarSeletorCores(visualizadorImagem,tituloJogador);

        // Adicionando componentes ao formulário
        parteDireita.getChildren().addAll(
                titulo,
                corLabel, seletorCores
        );

        // Montagem do layout principal
        conteudo.getChildren().addAll(parteEsquerda, parteDireita);
        container.getChildren().add(conteudo);

        return container;
    }

    private HBox criarSeletorCores(ImageView visualizadorImagem, String nomeJogador) {
        HBox container = new HBox(10);
        botoesCores = new Button[CORES_DISPONIVEIS.length];

        // Encontra a primeira cor disponível
        Color primeiraCorDisponivel = null;
        for (Color cor : CORES_DISPONIVEIS) {
            if (!coresUsadas.contains(cor)) {
                primeiraCorDisponivel = cor;
                break;
            }
        }
        // Configura a imagem inicial se houver cor disponível
        if (primeiraCorDisponivel != null) {
            corSelecionadaAtual = primeiraCorDisponivel;
            carregarImagemAvatar(visualizadorImagem, primeiraCorDisponivel);
        }

        // Cria botões para cada cor disponível
        for (int i = 0; i < CORES_DISPONIVEIS.length; i++) {
            Color cor = CORES_DISPONIVEIS[i];
            Button botaoCor = new Button();
            botoesCores[i] = botaoCor;
            botaoCor.setUserData(cor);

            if (coresUsadas.contains(cor)) {
                // Cor já usada - desativa
                botaoCor.setStyle("-fx-background-color: black; -fx-min-width: 30px; -fx-min-height: 30px;");
                botaoCor.setDisable(true);
            } else {
                String estiloNormal = "-fx-background-color: " + toHexString(cor) +
                        "; -fx-min-width: 30px; -fx-min-height: 30px;";
                String estiloSelecionado = estiloNormal +
                        "-fx-border-color: white; -fx-border-width: 3px;";

                // Configura estilo inicial
                botaoCor.setStyle(cor.equals(primeiraCorDisponivel) ? estiloSelecionado : estiloNormal);

                botaoCor.setOnAction(e -> selecionarCor(cor, visualizadorImagem, estiloSelecionado, nomeJogador));
            }
            container.getChildren().add(botaoCor);
        }

        return container;
    }

    private Map<String, Color> coresPorJogador = new HashMap<>();

    private void selecionarCor(Color cor, ImageView visualizadorImagem, String estiloSelecionado, String nomeJogador) {
        musica.tocarEfeito(AUDIO_CLIQUE);
        corSelecionadaAtual = cor;
        coresPorJogador.put(nomeJogador, cor);

        // Remove seleção de todos os botões
        for (Button btn : botoesCores) {
            if (btn != null && !btn.isDisabled()) {
                Color btnColor = (Color) btn.getUserData();
                btn.setStyle("-fx-background-color: " + toHexString(btnColor) +
                        "; -fx-min-width: 30px; -fx-min-height: 30px;");
            }
        }
        // Seleciona o botão clicado
        ((Button) visualizadorImagem.getScene().getFocusOwner()).setStyle(estiloSelecionado);
        carregarImagemAvatar(visualizadorImagem, cor);
    }

    private void carregarImagemAvatar(ImageView visualizadorImagem, Color cor) {
        try {
            String caminhoImagem = COR_PARA_IMAGEM.get(cor);
            Image image = new Image(new File(caminhoImagem).toURI().toString());
            visualizadorImagem.setImage(image);
        } catch (Exception ex) {
            System.err.println("Erro ao carregar imagem: " + ex.getMessage());
        }
    }

    private int[] posicaoAlimentoSelecionada;

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

        GridPane matriz = criarMatrizVisual(); // cria matriz

        // Adiciona lógica de clique para inserção do alimento
        for (Node node : matriz.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle celula = (Rectangle) node;

                celula.setOnMouseClicked(e -> {
                    musica.tocarEfeito(AUDIO_CLIQUE);

                    // Limpa seleção anterior
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

            System.out.println("Alimento posicionado em: [" + posicaoAlimentoSelecionada[0] +
                    ", " + posicaoAlimentoSelecionada[1] + "]");

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
    private final List<int[]> posicoesBombasSelecionadas = new ArrayList<>();

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

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Selecionar Bombas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private final List<int[]> posicoesPedrasSelecionadas = new ArrayList<>();

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

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Selecionar Pedras");
        primaryStage.setScene(scene);
        primaryStage.show();
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
                celula.setUserData(new int[]{linha, coluna}); // [row, column]
                celula.setId("celula-" + linha + "-" + coluna);

                matriz.add(celula, coluna, linha);
            }
        }

        return matriz;
    }

    private void exibirElementosNaMatriz(GridPane matriz) {
        // Exibe alimento
        if (posicaoAlimentoSelecionada != null) {
            int linha = posicaoAlimentoSelecionada[0];
            int coluna = posicaoAlimentoSelecionada[1];

            ImageView alimentoImg = new ImageView(new Image("file:src/imagens/alimento.png"));
            alimentoImg.setFitWidth(70);
            alimentoImg.setFitHeight(60);
            matriz.add(alimentoImg, coluna, linha);
        }

        // Exibe bombas
        for (int[] pos : posicoesBombasSelecionadas) {
            int linha = pos[0];
            int coluna = pos[1];

            ImageView bombaImg = new ImageView(new Image("file:src/imagens/bomba.png"));
            bombaImg.setFitWidth(70);
            bombaImg.setFitHeight(60);
            matriz.add(bombaImg, coluna, linha);
        }

        // Exibe pedras
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
        // Limpa imagens anteriores
        matriz.getChildren().removeIf(node ->
                node instanceof ImageView && !roboViews.containsValue(node)
        );

        // Mostra o alimento na posição correta
        exibirElementosNaMatriz(matriz);

        if (jogo instanceof MainNormal) {
            Robo robo = ((MainNormal) jogo).getRoboManual();
                adicionarRoboNaMatriz(matriz, robo);
        }
        else if (jogo instanceof MainMachine) {
            MainMachine machine = (MainMachine) jogo;
            for (Robo robo : machine.getRobos()) {
                adicionarRoboNaMatriz(matriz, robo);
            }
        }
        else if (jogo instanceof MainDualMission) {
            MainDualMission dualMission = (MainDualMission) jogo;
            for (Robo robo : dualMission.getRobos()) {
                adicionarRoboNaMatriz(matriz, robo);
            }
        }
        else if (jogo instanceof MainSurvivalBots) {
            MainSurvivalBots survivalBots = (MainSurvivalBots) jogo;
            for (Robo robo : survivalBots.getRobos()) {
                adicionarRoboNaMatriz(matriz, robo);
            }
        }
    }

    private Map<Robo, ImageView> roboViews = new HashMap<>();


    private void adicionarRoboNaMatriz(GridPane matriz, Robo robo) {
        if (!robo.isAtivo()) {
            ImageView img = roboViews.get(robo);
            if (img != null) matriz.getChildren().remove(img);
            roboViews.remove(robo);
            return;
        }

        ImageView roboImg = roboViews.get(robo);

        if (roboImg == null) {
            Color corRobo = corDosRobos.get(robo);
            if (corRobo == null) {
                System.out.println("corRobo está nulo para o robô: " + robo);
                corRobo = Color.BLUE;
            }
            String caminhoImagem = COR_PARA_IMAGEM.get(corRobo);

            if (caminhoImagem == null) {
                caminhoImagem = "src/imagens/robo.png"; // Fallback
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

            // 🔊 Tocar som manualmente antes de iniciar a animação
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
    private Map<Robo, Color> corDosRobos = new HashMap<>();

    private void iniciarJogoNormal() {
        int[] pos = posicaoAlimentoSelecionada;
        Color corRobo1 = coresPorJogador.get("Robô 1");  // Pega a cor salva
        String nomeCor = converterColorParaString(corRobo1);

        MainNormal jogo = new MainNormal();
        Robo robo = jogo.getRoboManual();

        jogo.executarJogo(nomeCor, pos[0], pos[1]);
        corDosRobos.put(robo, corRobo1);

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
        Color cor1 = coresPorJogador.get("Robô 1");
        Color cor2 = coresPorJogador.get("Robô 2");
        String nomecor1=converterColorParaString(cor1);
        String nomecor2=converterColorParaString(cor2);

        MainMachine jogo = new MainMachine();
        List<Robo> robos = jogo.getRobos();
        jogo.executarJogo(nomecor1, nomecor2, pos[0], pos[1]);
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
                        .map(Robo::getCor)
                        .findFirst()
                        .orElse("IA");

                botaoVitoria(vencedor);
                loopIA[0].stop(); // acessa corretamente agora
            }
        }));
        loopIA[0].setCycleCount(Animation.INDEFINITE);
        loopIA[0].play();
    }

    private void iniciarJogoDualMission() {
        int[] pos = posicaoAlimentoSelecionada;
        Color cor1 = coresPorJogador.get("Robô 1");
        Color cor2 = coresPorJogador.get("Robô 2");
        String nomecor1=converterColorParaString(cor1);
        String nomecor2=converterColorParaString(cor2);

        MainDualMission jogo = new MainDualMission();
        List<Robo> robos = jogo.getRobos();

        jogo.executarJogo(nomecor1,nomecor2, pos[0], pos[1]);
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
                botaoVitoria("Ambos os robôs encontraram o alimento!");
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
        Color cor1 = coresPorJogador.get("Robô 1");
        Color cor2 = coresPorJogador.get("Robô 2");
        String nomecor1=converterColorParaString(cor1);
        String nomecor2=converterColorParaString(cor2);

        MainSurvivalBots jogo = new MainSurvivalBots();
        List<Robo> robos = jogo.getRobos();
        jogo.setPosicoesBombasSelecionadas(pos1);
        jogo.executarJogo(nomecor1,nomecor2, pos[0], pos[1], pos1, pos2);
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
                        .map(Robo::getCor)
                        .findFirst();

                loopIA[0].stop();

                if (vencedor.isPresent()) {
                    botaoVitoria(vencedor.get());
                } else {
                    botaoDerrota();
                }
            }
        }));
        loopIA[0].setCycleCount(Animation.INDEFINITE);
        loopIA[0].play();
    }



    private Pane criarControlesMovimento(MainNormal jogo, GridPane matriz) {
        // Cria um container principal para os controles
        VBox containerControles = new VBox(5);
        containerControles.setAlignment(Pos.CENTER);

        // Configuração dos botões
        Button btnCima = new Button("↑");
        Button btnBaixo = new Button("↓");
        Button btnDireita = new Button("→");
        Button btnEsquerda = new Button("←");

        // Estilização dos botões
        String estiloBotao = "-fx-font-size: 20px; -fx-min-width: 60px; -fx-min-height: 60px;";
        btnCima.setStyle(estiloBotao);
        btnBaixo.setStyle(estiloBotao);
        btnDireita.setStyle(estiloBotao);
        btnEsquerda.setStyle(estiloBotao);

        // Ações dos botões
        btnCima.setOnAction(e -> {
            musica.tocarEfeito(AUDIO_CLIQUE);
            boolean resultado=jogo.moverRobo("1");
            atualizarMatriz(matriz, jogo);
            if(resultado){botaoVitoria(jogo.getRoboManual().getCor());}
        });
        btnBaixo.setOnAction(e -> {
            musica.tocarEfeito(AUDIO_CLIQUE);
            boolean resultado=jogo.moverRobo("2");
            atualizarMatriz(matriz, jogo);
            if(resultado){botaoVitoria(jogo.getRoboManual().getCor());}
        });
        btnDireita.setOnAction(e -> {
            musica.tocarEfeito(AUDIO_CLIQUE);
            boolean resultado=jogo.moverRobo("3");
            atualizarMatriz(matriz, jogo);
            if(resultado){botaoVitoria(jogo.getRoboManual().getCor());}
        });
        btnEsquerda.setOnAction(e -> {
            musica.tocarEfeito(AUDIO_CLIQUE);
            boolean resultado=jogo.moverRobo("4");
            atualizarMatriz(matriz, jogo);
            if(resultado){botaoVitoria(jogo.getRoboManual().getCor());}
        });

        // Linha de cima (apenas botão Cima)
        HBox linhaCima = new HBox();
        linhaCima.setAlignment(Pos.CENTER);
        linhaCima.getChildren().add(btnCima);

        // Linha de baixo (Esquerda, Baixo, Direita)
        HBox linhaBaixo = new HBox(10);
        linhaBaixo.setAlignment(Pos.CENTER);
        linhaBaixo.getChildren().addAll(btnEsquerda, btnBaixo, btnDireita);

        // Adiciona as linhas ao container principal
        containerControles.getChildren().addAll(linhaCima, linhaBaixo);

        // Posicionamento do container
        containerControles.setLayoutX(200);
        containerControles.setLayoutY(500);

        return containerControles;
    }

    public void botaoVitoria(String corRobo) {
        Platform.runLater(() -> {
            musica.tocarEfeito(AUDIO_APLAUSO);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Parabéns!");
            alert.setHeaderText("Vitória do Robô " + corRobo.toUpperCase());
            alert.setContentText("O robô encontrou o alimento com sucesso!");

            // Configuração simplificada do botão
            alert.getButtonTypes().clear();
            ButtonType voltarMenuButton = new ButtonType("Voltar ao Menu", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().add(voltarMenuButton);

            // Estilo inline básico (opcional)
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-background-color: #f0f0f0; -fx-font-size: 14px;");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == voltarMenuButton) {
                ReiniciarJogo();
            }
        });
    }

    public void botaoDerrota() {
        Platform.runLater(() -> {
            musica.tocarEfeito(AUDIO_DERROTA);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Todos Perderam!");
            alert.setHeaderText("Derrotas de todos robos ");
            alert.setContentText("infelizmente ninguem venceu");

            alert.getButtonTypes().clear();
            ButtonType voltarMenuButton = new ButtonType("Voltar ao Menu", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().add(voltarMenuButton);

            // Estilo inline básico (opcional)
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-background-color: #f0f0f0; -fx-font-size: 14px;");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == voltarMenuButton) {
                ReiniciarJogo();
            }
        });
    }




    private void exibirCreditos() {
        Button btnDev1 = criarBotaoComIcone("KaynanSantos", "src/imagens/code.png", Color.LIGHTSKYBLUE);
        Button btnDev2 = criarBotaoComIcone("Ana Beatriz", "src/imagens/code.png", Color.LIGHTSKYBLUE);
        Button btnDev3 = criarBotaoComIcone("Luis Felipe", "src/imagens/code.png", Color.LIGHTSKYBLUE);
        Button btnVoltar = criarBotaoComIcone("Voltar", "src/imagens/back.png", Color.LIGHTSKYBLUE);

        btnVoltar.setOnAction(criarAcaoComSom(this::exibirMenuPrincipal));

        HBox botoes = new HBox(20, btnDev1, btnDev2, btnDev3, btnVoltar);
        botoes.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Créditos", botoes), 700, 400));
    }

    private void exibirOpcoes() {
        Button btnMusica = criarBotaoComIcone("Parar Música", "src/imagens/music.png", Color.GREEN);
        Button btnVoltar = criarBotaoComIcone("Voltar", "src/imagens/back.png", Color.LIGHTSKYBLUE);

        btnMusica.setOnAction(criarAcaoComSom(() -> alternarMusica(btnMusica)));
        btnVoltar.setOnAction(criarAcaoComSom(this::exibirMenuPrincipal));

        HBox botoes = new HBox(20, btnMusica, btnVoltar);
        botoes.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Opções", botoes), 700, 400));
    }

    private void alternarMusica(Button botao) {
        if (musica.isMusicaTocando()) {
            musica.parar();
            botao.setStyle("-fx-background-radius: 20; -fx-background-color: red;");
        } else {
            musica.tocar();
            botao.setStyle("-fx-background-radius: 20; -fx-background-color: green;");
        }
    }

    private Button criarBotaoPadronizado(String texto, String icone, Color cor, Runnable acao) {
        Button btn = criarBotaoComIcone(texto, icone, cor);
        btn.setOnAction(criarAcaoComSom(acao));
        return btn;
    }

    private EventHandler<ActionEvent> criarAcaoComSom(Runnable acao) {
        return e -> {
            musica.tocarEfeito(AUDIO_CONFIRM);
            acao.run();
        };
    }

    private Button criarBotaoComIcone(String texto, String caminhoImagem, Color corFundo) {
        ImageView icone = new ImageView(new Image("file:" + caminhoImagem));
        icone.setFitWidth(50);
        icone.setFitHeight(50);

        Button botao = new Button(texto, icone);
        botao.setFont(Font.font(FONTE_PADRAO, 16));
        botao.setTextFill(Color.BLACK);
        botao.setPrefSize(130, 160);
        botao.setContentDisplay(ContentDisplay.TOP);
        botao.setStyle("-fx-background-radius: 20; -fx-background-color: " + toRgb(corFundo) + ";");

        configurarEfeitoHover(botao);
        return botao;
    }

    private void configurarEfeitoHover(Button botao) {
        ScaleTransition transicao = new ScaleTransition(Duration.millis(150), botao);
        transicao.setToX(1.1);
        transicao.setToY(1.1);

        botao.setOnMouseEntered(e -> {
            transicao.stop();
            transicao.playFromStart();
            musica.tocarEfeito(AUDIO_CLIQUE);
        });

        botao.setOnMouseExited(e -> {
            transicao.stop();
            botao.setScaleX(1.0);
            botao.setScaleY(1.0);
        });
    }

    private VBox criarLayoutPadrao(String tituloTexto, Node... conteudo) {
        Label titulo = new Label(tituloTexto);
        titulo.setFont(Font.font(FONTE_PADRAO, 20));

        // Criar o VBox com espaçamento primeiro
        VBox layout = new VBox(30); // 30 é o espaçamento entre os nós

        // Adicionar o título e os conteúdos
        layout.getChildren().add(titulo);
        layout.getChildren().addAll(conteudo);

        layout.setAlignment(Pos.CENTER);
        layout.setStyle(ESTILO_FUNDO);
        return layout;
    }

    private Label criarLabel(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        return label;
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    private String toRgb(Color c) {
        return String.format("rgb(%d,%d,%d)",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255));
    }

    private String converterColorParaString(Color cor) {
        if (cor.equals(Color.RED)) return "vermelho";
        if (cor.equals(Color.BLUE)) return "azul";
        if (cor.equals(Color.GREEN)) return "verde";
        if (cor.equals(Color.YELLOW)) return "amarelo";
        if (cor.equals(Color.PURPLE)) return "roxo";
        if (cor.equals(Color.ORANGE)) return "laranja";
        return "azul"; // default
    }

    private final Map<Color, String> COR_PARA_IMAGEM = Map.of(
            Color.RED, "src/imagens/avatar_red.png",
            Color.BLUE, "src/imagens/avatar_blue.png",
            Color.GREEN, "src/imagens/avatar_green.png",
            Color.YELLOW, "src/imagens/avatar_yellow.png",
            Color.PURPLE, "src/imagens/avatar_purple.png",
            Color.ORANGE, "src/imagens/avatar_orange.png"
    );
}