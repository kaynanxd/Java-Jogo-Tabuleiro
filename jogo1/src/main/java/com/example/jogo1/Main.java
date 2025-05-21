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

    private static final String ESTILO_FUNDO = "-fx-background-color: #c2ffd2;";
    private static final String AUDIO_CONFIRM = "src/audios/confirm.mp3";
    private static final String AUDIO_CLIQUE = "src/audios/clique.mp3";
    private static final String FONTE_PADRAO = "Arial";
    private Stage primaryStage;
    private Musica musica;
    private int numeroJogadoresSelecionado;
    private Dados dados = new Dados();
    private List<Jogador> jogadores = new ArrayList<>();
    private List<Color> coresUsadas = new ArrayList<>();
    private Color corSelecionadaAtual;
    private Button[] botoesCores;

    private final Map<Color, String> COR_PARA_IMAGEM = Map.of(
            Color.RED, "src/imagens/avatar_red.png",
            Color.BLUE, "src/imagens/avatar_blue.png",
            Color.GREEN, "src/imagens/avatar_green.png",
            Color.YELLOW, "src/imagens/avatar_yellow.png",
            Color.PURPLE, "src/imagens/avatar_purple.png",
            Color.ORANGE, "src/imagens/avatar_orange.png"   );

    private static final Color[] CORES_DISPONIVEIS = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE, Color.ORANGE  };

    //metodo herdado da classe Applicativo, inicializa o JavaFX.Depois chama automaticamente o metodo start(Stage primaryStage) onde e definido ainterface grafica
    public static void main(String[] args) {
        launch(args);
    }

    //@Override Metodo que inicia a interface grafica e musica
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.musica = new Musica("src/audios/musica.mp3");
        exibirTelaSplash();
    }

    public void ReiniciarJogo() {   //Limpa todos os dados do jogo anterior para poder reiniciar
        musica.parar();
        jogadores.clear();
        coresUsadas.clear();
        corSelecionadaAtual = null;
        musica = new Musica("src/audios/musica.mp3");
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
            musica.tocarEfeito(AUDIO_CONFIRM);     });
        //Configura a janela principal
        primaryStage.setScene(cenaSplash);
        primaryStage.setTitle("Splash");
        primaryStage.show();
    }

    private void exibirMenuPrincipal() {
        Button btnJogar = criarBotaoPadronizado("Jogar", "src/imagens/dice.png", Color.MEDIUMSEAGREEN, this::exibirSelecionarJogadores);
        Button btnCreditos = criarBotaoPadronizado("Créditos", "src/imagens/code.png", Color.MEDIUMSEAGREEN, this::exibirCreditos);
        Button btnSair = criarBotaoPadronizado("Sair", "src/imagens/exit.png", Color.SALMON, () -> System.exit(0));
        Button btnOpcoes = criarBotaoPadronizado("Opções", "src/imagens/options.png", Color.MEDIUMSEAGREEN, this::exibirOpcoes);

        HBox containerBotoes = new HBox(20, btnJogar, btnCreditos, btnOpcoes, btnSair); // Organiza os botões horizontalmente com espaçamento e centraliza
        containerBotoes.setAlignment(Pos.CENTER);
            // Cria a janela do menu principal
        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Corrida No Tabuleiro", containerBotoes), 700, 400));
        primaryStage.setTitle("Menu Principal");
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

    private void exibirSelecionarJogadores() {
             //cria uma lista para armazenar os botoes e cria eles
        List<Button> botoes = new ArrayList<>();
        for (int i = 2; i <= 6; i++) {
            int numJogadores = i;
            Button btn = criarBotaoPadronizado(i + " jogadores", "src/imagens/player.png", Color.MEDIUMSEAGREEN, () -> {
                        numeroJogadoresSelecionado = numJogadores;
                        mostrarFormularioJogador(1);  //metodo onde sera mostrado a interface do formulario
                    });
            botoes.add(btn); //adiciona o botao a lista
        }
        Button btnVoltar = criarBotaoPadronizado("Voltar", "src/imagens/back.png", Color.DARKSEAGREEN, this::exibirMenuPrincipal);
        Button btnDebug = criarBotaoComIcone("Debug OFF", "src/imagens/debug.png", Color.SALMON);
        btnDebug.setOnAction(criarAcaoComSom(() -> alternarModoDebug(btnDebug)));  //chama metodo para ativar e desativar modo debug

        // define que a linha 1 tera 4 botoes e a linha 2 tem
        HBox linha1 = new HBox(20, botoes.get(0), botoes.get(1), botoes.get(2), botoes.get(3));
        HBox linha2 = new HBox(20, botoes.get(4), btnDebug, btnVoltar);
        linha1.setAlignment(Pos.CENTER);
        linha2.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Selecionar número de jogadores", linha1, linha2), 700, 500));
    }

    private void mostrarFormularioJogador(int indiceAtual) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #c2ffd2;");

        // Cria um container principal na cor azul
        StackPane containerPrincipal = new StackPane();
        containerPrincipal.setStyle("-fx-background-color: #6699FF; -fx-background-radius: 15;");
        containerPrincipal.setPrefSize(600, 400);
        containerPrincipal.setLayoutX(100);
        containerPrincipal.setLayoutY(50);

        // Cria um layout horizontal para dividir a tela
        HBox conteudoPrincipal = new HBox(20);
        conteudoPrincipal.setAlignment(Pos.CENTER);
        conteudoPrincipal.setPadding(new Insets(20));

        // Configuração da imagem Que fica na esquerda
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
        //Botao de Confirmar
        Button botaoProximo = new Button((indiceAtual < numeroJogadoresSelecionado) ? "Próximo" : "Iniciar Jogo");
        botaoProximo.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        botaoProximo.setOnAction(e -> processarFormulario(primaryStage, indiceAtual, nomeField, seletorTipo));

        // Adiciona os componentes ao formulário
        parteDireita.getChildren().addAll(
                titulo, nomeLabel, nomeField,
                corLabel, seletorCores,
                tipoLabel, seletorTipo,
                botaoProximo
        );
        //Monta o layout principal
        conteudoPrincipal.getChildren().addAll(parteEsquerda, parteDireita);
        containerPrincipal.getChildren().add(conteudoPrincipal);
        root.getChildren().add(containerPrincipal);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Configurar Jogadores");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processarFormulario(Stage stage, int indiceAtual, TextField campoNome, HBox seletorTipo) {
        String nome = campoNome.getText().trim();
        String tipo = "Normal";
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
                        "Selecione pelo menos 2 tipos diferentes de jogadores (Normal, Sortudo e/ou Azarado).\n" + "Atualmente todos os jogadores são do mesmo tipo: " + tipo).show();
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
        botoesCores = new Button[CORES_DISPONIVEIS.length];  //inicializa array com os botao com a quantidade de cores disponiveis
        //Encontra a primeira cor disponível
        Color primeiraCorDisponivel = null;
        for (Color cor : CORES_DISPONIVEIS) {
            if (!coresUsadas.contains(cor)) { //verifica se a cor ja foi usada
                primeiraCorDisponivel = cor;
                break;
            }
        }
        // seta a primeira cor para carregar a imagem
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
                //se a cor ja foi usada o botao e desativado
                botaoCor.setStyle("-fx-background-color: black; -fx-min-width: 30px; -fx-min-height: 30px;");
                botaoCor.setDisable(true);
            } else {  //configura layout do botao, se for selecionado ele fica com contorno branco
                String estiloNormal = "-fx-background-color: " + toHexString(cor) +
                        "; -fx-min-width: 30px; -fx-min-height: 30px;";
                String estiloSelecionado = estiloNormal +
                        "-fx-border-color: white; -fx-border-width: 3px;";
                // Aplica estilo inicial,se for a primeira cor disponível, já aparece selecionada,e a imagem e carregada
                botaoCor.setStyle(cor.equals(primeiraCorDisponivel) ? estiloSelecionado : estiloNormal);
                botaoCor.setOnAction(e -> selecionarCor(cor, visualizadorImagem, estiloSelecionado));
            }
            container.getChildren().add(botaoCor); // Adiciona cada botão ao HBox
        }
        return container;
    }

    private void selecionarCor(Color cor, ImageView visualizadorImagem, String estiloSelecionado) {
        musica.tocarEfeito(AUDIO_CLIQUE);
        corSelecionadaAtual = cor;
        // Remove o contorno branco de todos os botões
        for (Button btn : botoesCores) {
            if (btn != null && !btn.isDisabled()) {
                Color btnColor = (Color) btn.getUserData();
                btn.setStyle("-fx-background-color: " + toHexString(btnColor) +
                        "; -fx-min-width: 30px; -fx-min-height: 30px;");
            }
        }
        // Seleciona o botão clicado e carrega a nova imagem
        ((Button) visualizadorImagem.getScene().getFocusOwner()).setStyle(estiloSelecionado);
        carregarImagemAvatar(visualizadorImagem, cor);
    }

    private void carregarImagemAvatar(ImageView visualizadorImagem, Color cor) {
            String caminhoImagem = COR_PARA_IMAGEM.get(cor); // Obtem o caminho da imagem do mapa de cores
            Image image = new Image(new File(caminhoImagem).toURI().toString());
            visualizadorImagem.setImage(image);
    }

    private HBox criarSeletorTipoJogador() {
        HBox container = new HBox(10);  //cria container horizontal com espaçamento 10
        ToggleGroup grupoTipo = new ToggleGroup();  // cria Grupo para os radio buttons

        RadioButton opcaoSorte = new RadioButton("Sortudo");
        opcaoSorte.setToggleGroup(grupoTipo);
        opcaoSorte.setUserData("Sortudo");

        RadioButton opcaoNormal = new RadioButton("Normal");
        opcaoNormal.setToggleGroup(grupoTipo);
        opcaoNormal.setUserData("Normal");
        opcaoNormal.setSelected(true); //deixa pre selecionado

        RadioButton opcaoAzar = new RadioButton("Azarado");
        opcaoAzar.setToggleGroup(grupoTipo);
        opcaoAzar.setUserData("Azarado");

        container.getChildren().addAll(opcaoSorte, opcaoNormal, opcaoAzar); // Adiciona os radio buttons ao container
        return container;
    }

    private void exibirCreditos() {
        Button btnDev1 = criarBotaoComIcone("KaynanSantos", "src/imagens/player2.png", Color.MEDIUMSEAGREEN);
        Button btnDev2 = criarBotaoComIcone("Ana Beatriz", "src/imagens/player2.png", Color.MEDIUMSEAGREEN);
        Button btnDev3 = criarBotaoComIcone("Luis Felipe", "src/imagens/player2.png", Color.MEDIUMSEAGREEN);
        Button btnVoltar = criarBotaoComIcone("Voltar", "src/imagens/back.png", Color.DARKSEAGREEN);
        btnVoltar.setOnAction(criarAcaoComSom(this::exibirMenuPrincipal)); //manda de volta pra menuprincipal

        HBox botoes = new HBox(20, btnDev1, btnDev2, btnDev3, btnVoltar); //coloca os botoes em uma linha com espacamento 20
        botoes.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Créditos", botoes), 700, 400));
    }

    private void exibirOpcoes() {
        Button btnMusica = criarBotaoComIcone("Parar Música", "src/imagens/music.png", Color.GREEN);
        Button btnVoltar = criarBotaoComIcone("Voltar", "src/imagens/back.png", Color.DARKSEAGREEN);

        btnMusica.setOnAction(criarAcaoComSom(() -> alternarMusica(btnMusica)));
        btnVoltar.setOnAction(criarAcaoComSom(this::exibirMenuPrincipal));

        HBox botoes = new HBox(20, btnMusica, btnVoltar);
        botoes.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Opções", botoes), 700, 400));
    }

    private void alternarModoDebug(Button btnDebug) {
        if (dados.getModoJogo() == Dados.ModoJogo.DEBUG) { //muda para modo normal do debug
            dados.setModoJogo(Dados.ModoJogo.NORMAL);
            btnDebug.setText("Debug OFF");
            btnDebug.setStyle("-fx-background-color: SALMON; -fx-background-radius: 20;");
        } else {  //liga modo debug
            dados.setModoJogo(Dados.ModoJogo.DEBUG);
            btnDebug.setText("Debug ON");
            btnDebug.setStyle("-fx-background-color: green; -fx-background-radius: 20;");
        }
    }

    private void alternarMusica(Button botao) {
        if (musica.isMusicaTocando()) {  //se musica tiver tocando ela para
            musica.parar();
            botao.setStyle("-fx-background-radius: 20; -fx-background-color: red;");
        } else {   //dar play na musica
            musica.tocar();
            botao.setStyle("-fx-background-radius: 20; -fx-background-color: green;");
        }
    }
    //Cria um Evento que toca efeito sonoro antes de executar a ação
    private EventHandler<ActionEvent> criarAcaoComSom(Runnable acao) {
        return e -> {
            musica.tocarEfeito(AUDIO_CONFIRM);
            acao.run();
        };
    }
    //cria os botoes do menu com icone cor e uma acao
    private Button criarBotaoPadronizado(String texto, String icone, Color cor, Runnable acao) {
        Button btn = criarBotaoComIcone(texto, icone, cor);
        btn.setOnAction(criarAcaoComSom(acao));
        return btn;
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

    private void configurarEfeitoHover(Button botao) {      //efeito de botoes aumentarem ao passar o cursor
        ScaleTransition transicao = new ScaleTransition(Duration.millis(150), botao);  //cria animacao que dura 150ms
        transicao.setToX(1.1); //aumenta a escala em 1.1
        transicao.setToY(1.1);

        botao.setOnMouseEntered(e -> {
            transicao.stop(); //para qualquer animacao em andamento
            transicao.playFromStart();
            musica.tocarEfeito(AUDIO_CLIQUE);
        });

        botao.setOnMouseExited(e -> {  //ao tirar cursor animacao para e botao volta ao normal
            transicao.stop();
            botao.setScaleX(1.0);
            botao.setScaleY(1.0);
        });
    }
    //Converte uma cor JavaFX para formato hexadecimal
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }
    //Converte uma cor JavaFX para RGB
    private String toRgb(Color c) {
        return String.format("rgb(%d,%d,%d)",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255));
    }
}