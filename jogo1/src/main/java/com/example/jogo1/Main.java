package com.example.jogo1;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;

public class Main extends Application {

    // Constantes
    private static final Color[] CORES_DISPONIVEIS = {
            Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.PURPLE, Color.ORANGE
    };

    private static final String ESTILO_FUNDO = "-fx-background-color: #FFD79E;";
    private static final String AUDIO_CONFIRM = "src/audios/confirm.mp3";
    private static final String AUDIO_CLIQUE = "src/audios/clique.mp3";
    private static final String FONTE_PADRAO = "Arial";

    private final Map<Color, String> COR_PARA_IMAGEM = Map.of(
            Color.RED, "src/imagens/avatar_red.png",
            Color.BLUE, "src/imagens/avatar_blue.png",
            Color.GREEN, "src/imagens/avatar_green.png",
            Color.YELLOW, "src/imagens/avatar_yellow.png",
            Color.PURPLE, "src/imagens/avatar_purple.png",
            Color.ORANGE, "src/imagens/avatar_orange.png"
    );

    // Componentes UI
    private Stage primaryStage;
    private Musica musica;
    private int numeroJogadoresSelecionado;
    private Dados dados = new Dados();
    private List<Jogador> jogadores = new ArrayList<>();
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
        // 2. Limpa todos os dados do jogo anterior
        jogadores.clear();
        coresUsadas.clear();
        corSelecionadaAtual = null;

        // 3. Reinicia o sistema de áudio
        musica = new Musica("src/audios/mus" +
                "ica.mp3");
        // 4. Prepara e mostra o menu principal
        exibirTelaSplash();
    }

    private void exibirMenuPrincipal() {
        Button btnJogar = criarBotaoPadronizado("Jogar", "src/imagens/dice.png",
                Color.STEELBLUE, this::exibirSelecionarJogadores); // Direto para seleção de jogadores

        Button btnCreditos = criarBotaoPadronizado("Créditos", "src/imagens/code.png",
                Color.MEDIUMPURPLE, this::exibirCreditos);

        Button btnSair = criarBotaoPadronizado("Sair", "src/imagens/exit.png",
                Color.SALMON, () -> System.exit(0));

        Button btnOpcoes = criarBotaoPadronizado("Opções", "src/imagens/options.png",
                Color.ORANGE, this::exibirOpcoes);

        HBox containerBotoes = new HBox(20, btnJogar, btnCreditos, btnOpcoes, btnSair);
        containerBotoes.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Corrida No Tabuleiro", containerBotoes), 700, 400));
        primaryStage.setTitle("Menu Principal");
    }

    private void exibirSelecionarJogadores() {
        List<Button> botoes = new ArrayList<>();
        jogadores.clear();
        coresUsadas.clear();

        // Cria botões para 2 a 6 jogadores
        for (int i = 2; i <= 6; i++) {
            int numJogadores = i;
            Button btn = criarBotaoPadronizado(i + " jogadores", "src/imagens/player.png",
                    Color.DARKCYAN, () -> {
                        numeroJogadoresSelecionado = numJogadores;
                        mostrarFormularioJogador(1);
                    });
            botoes.add(btn);
        }

        // Botão Voltar (será adicionado à segunda linha)
        Button btnVoltar = criarBotaoPadronizado("Voltar", "src/imagens/back.png",
                Color.GRAY, this::exibirMenuPrincipal);

        // Botão Debug
        Button btnDebug = criarBotaoComIcone("Debug OFF", "src/imagens/debug.png", Color.ORANGERED);
        btnDebug.setOnAction(criarAcaoComSom(() -> alternarModoDebug(btnDebug)));

        // Linha 1: 4 botões (2, 3, 4, 5 jogadores)
        HBox linha1 = new HBox(20, botoes.get(0), botoes.get(1), botoes.get(2), botoes.get(3));

        // Linha 2: 3 botões (6 jogadores, Debug, Voltar)
        HBox linha2 = new HBox(20, botoes.get(4), btnDebug, btnVoltar);

        linha1.setAlignment(Pos.CENTER);
        linha2.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Selecionar número de jogadores", linha1, linha2), 700, 500));
    }

    private void mostrarFormularioJogador(int indiceAtual) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #FFFF99;");

        StackPane containerPrincipal = new StackPane();
        containerPrincipal.setStyle("-fx-background-color: #6699FF; -fx-background-radius: 15;");
        containerPrincipal.setPrefSize(600, 400);
        containerPrincipal.setLayoutX(100);
        containerPrincipal.setLayoutY(50);

        // Configuração do conteúdo principal
        HBox conteudoPrincipal = new HBox(20);
        conteudoPrincipal.setAlignment(Pos.CENTER);
        conteudoPrincipal.setPadding(new Insets(20));

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

        // Componentes do formulário
        Label titulo = new Label("Jogador " + indiceAtual);
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label nomeLabel = criarLabel("Seu nome:");
        TextField nomeField = new TextField();
        nomeField.setPromptText("Digite seu nome");
        nomeField.setStyle("-fx-pref-width: 200px; -fx-font-size: 14px;");

        Label corLabel = criarLabel("Cor:");
        HBox seletorCores = criarSeletorCores(visualizadorImagem);

        Label tipoLabel = criarLabel("Tipo do Jogador:");
        HBox seletorTipo = criarSeletorTipoJogador();

        // Botão de ação
        Button botaoProximo = new Button((indiceAtual < numeroJogadoresSelecionado) ? "Próximo" : "Iniciar Jogo");
        botaoProximo.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        botaoProximo.setOnAction(e -> processarFormulario(primaryStage, indiceAtual, nomeField, seletorTipo));


        // Adicionando componentes ao formulário
        parteDireita.getChildren().addAll(
                titulo, nomeLabel, nomeField,
                corLabel, seletorCores,
                tipoLabel, seletorTipo,
                botaoProximo
        );

        // Montagem do layout principal
        conteudoPrincipal.getChildren().addAll(parteEsquerda, parteDireita);
        containerPrincipal.getChildren().add(conteudoPrincipal);
        root.getChildren().add(containerPrincipal);

        // Configuração da cena
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Configurar Jogadores");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processarFormulario(Stage stage, int indiceAtual, TextField campoNome, HBox seletorTipo) {
        String nome = campoNome.getText().trim();

        String tipo = "Normal"; // valor padrão
        for (Node node : seletorTipo.getChildren()) {
            if (node instanceof RadioButton) {
                RadioButton radio = (RadioButton) node;
                if (radio.isSelected()) {
                    tipo = radio.getUserData().toString();
                    break;
                }
            }
        }

        if (nome.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Preencha todos os campos.").show();
            return;
        }

        if (corSelecionadaAtual == null) {
            new Alert(Alert.AlertType.ERROR, "Selecione uma cor clicando em um dos quadrados coloridos.").show();
            return;
        }

        coresUsadas.add(corSelecionadaAtual);
        Jogador jogador = new Jogador(nome, tipo, null);
        jogador.setCor(corSelecionadaAtual);
        jogadores.add(jogador);

        if (indiceAtual < numeroJogadoresSelecionado) {
            // Continua para o próximo jogador
            mostrarFormularioJogador(indiceAtual + 1);
        } else {
            // Verifica se há pelo menos 2 tipos diferentes de jogadores
            if (temTiposDiferentesSuficientes()) {
                JogoTabuleiro jogo = new JogoTabuleiro(jogadores, dados.getModoJogo(), this, primaryStage);
                jogo.start(stage);
            } else {
                // Remove o último jogador adicionado para corrigir
                jogadores.remove(jogadores.size() - 1);
                coresUsadas.remove(corSelecionadaAtual);

                new Alert(Alert.AlertType.ERROR,
                        "Selecione pelo menos 2 tipos diferentes de jogadores (Normal, Sortudo e/ou Azarado).\n" +
                                "Atualmente todos os jogadores são do mesmo tipo: " + tipo)
                        .show();
            }
        }
        musica.tocarEfeito(AUDIO_CLIQUE);
    }
    private boolean temTiposDiferentesSuficientes() {

        String primeiroTipo = jogadores.get(0).getTipoJogador();

        // Verifica se existe algum jogador com tipo diferente
        for (Jogador j : jogadores) {
            if (!j.getTipoJogador().equals(primeiroTipo)) {
                return true; // Encontrou um tipo diferente
            }
        }

        return false; // Todos são do mesmo tipo
    }

    private HBox criarSeletorCores(ImageView visualizadorImagem) {
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

                botaoCor.setOnAction(e -> selecionarCor(cor, visualizadorImagem, estiloSelecionado));
            }
            container.getChildren().add(botaoCor);
        }

        return container;
    }

    private void selecionarCor(Color cor, ImageView visualizadorImagem, String estiloSelecionado) {
        musica.tocarEfeito(AUDIO_CLIQUE);
        corSelecionadaAtual = cor;

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

    private HBox criarSeletorTipoJogador() {
        HBox container = new HBox(10);
        ToggleGroup grupoTipo = new ToggleGroup();

        RadioButton opcaoSorte = new RadioButton("Sortudo");
        opcaoSorte.setToggleGroup(grupoTipo);
        opcaoSorte.setUserData("Sortudo");

        RadioButton opcaoNormal = new RadioButton("Normal");
        opcaoNormal.setToggleGroup(grupoTipo);
        opcaoNormal.setUserData("Normal");
        opcaoNormal.setSelected(true);

        RadioButton opcaoAzar = new RadioButton("Azarado");
        opcaoAzar.setToggleGroup(grupoTipo);
        opcaoAzar.setUserData("Azarado");

        container.getChildren().addAll(opcaoSorte, opcaoNormal, opcaoAzar);
        return container;
    }


    private void exibirCreditos() {
        Button btnDev1 = criarBotaoComIcone("KaynanSantos", "src/imagens/code.png", Color.PURPLE);
        Button btnDev2 = criarBotaoComIcone("Ana Beatriz", "src/imagens/code.png", Color.MEDIUMPURPLE);
        Button btnDev3 = criarBotaoComIcone("Luis Felipe", "src/imagens/code.png", Color.LIGHTPINK);
        Button btnVoltar = criarBotaoComIcone("Voltar", "src/imagens/back.png", Color.GRAY);

        btnVoltar.setOnAction(criarAcaoComSom(this::exibirMenuPrincipal));

        HBox botoes = new HBox(20, btnDev1, btnDev2, btnDev3, btnVoltar);
        botoes.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Créditos", botoes), 700, 400));
    }

    private void exibirOpcoes() {
        Button btnMusica = criarBotaoComIcone("Parar Música", "src/imagens/music.png", Color.GREEN);
        Button btnVoltar = criarBotaoComIcone("Voltar", "src/imagens/back.png", Color.GRAY);

        btnMusica.setOnAction(criarAcaoComSom(() -> alternarMusica(btnMusica)));
        btnVoltar.setOnAction(criarAcaoComSom(this::exibirMenuPrincipal));

        HBox botoes = new HBox(20, btnMusica, btnVoltar);
        botoes.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Opções", botoes), 700, 400));
    }

    private void alternarModoDebug(Button btnDebug) {
        if (dados.getModoJogo() == Dados.ModoJogo.DEBUG) {
            dados.setModoJogo(Dados.ModoJogo.NORMAL);
            btnDebug.setText("Debug OFF");
            btnDebug.setStyle("-fx-background-color: orangered; -fx-background-radius: 20;");
        } else {
            dados.setModoJogo(Dados.ModoJogo.DEBUG);
            btnDebug.setText("Debug ON");
            btnDebug.setStyle("-fx-background-color: green; -fx-background-radius: 20;");
        }
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
}