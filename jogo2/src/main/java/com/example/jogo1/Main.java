package com.example.jogo1;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.util.Duration;
import java.util.*;

import javafx.scene.Node;

public class Main extends Application {

    private static final String ESTILO_FUNDO = "-fx-background-color: #e0ffff;";
    private static final String AUDIO_CONFIRM = "src/audios/confirm.mp3";
    private static final String AUDIO_CLIQUE = "src/audios/clique.mp3";
    private static final String FONTE_PADRAO = "Arial";

    private Stage primaryStage;
    private Musica musica;
    private InterfaceJogo interfaceJogo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.musica = new Musica("src/audios/musica.mp3");
        this.interfaceJogo = new InterfaceJogo(primaryStage, musica,this);
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
        interfaceJogo.reiniciarJogo();
        exibirTelaSplash();
    }

    private void exibirMenuPrincipal() {
        Button btnJogar = criarBotaoPadronizado("Jogar", "src/imagens/dice.png",
                Color.LIGHTSKYBLUE, this::exibirSelecionarModoJogo);

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

        Button btnNormal = criarBotaoPadronizado(" Normal", "src/imagens/robot2.png", Color.LIGHTSKYBLUE, () -> {
            interfaceJogo.mostrarFormularioJogador(1);
            interfaceJogo.setModoDeJogoSelecionado("NORMAL");
        });

        Button btnMachine = criarBotaoPadronizado(" Machine", "src/imagens/robot3.png", Color.LIGHTSKYBLUE, () -> {
            interfaceJogo.mostrarFormularioJogador(2);
            interfaceJogo.setModoDeJogoSelecionado("MACHINE");
        });

        Button btnDualMission = criarBotaoPadronizado(" DualMission", "src/imagens/robot4.png", Color.LIGHTSKYBLUE, () -> {
            interfaceJogo.mostrarFormularioJogador(2);
            interfaceJogo.setModoDeJogoSelecionado("DUAL_MISSION");
        });

        Button btnSurvivalBots = criarBotaoPadronizado(" SurvivalBots", "src/imagens/dice.png", Color.LIGHTSKYBLUE, () -> {
            interfaceJogo.mostrarFormularioJogador(2);
            interfaceJogo.setModoDeJogoSelecionado("SURVIVALBOTS");
        });

        Button btnVoltar = criarBotaoPadronizado("Voltar", "src/imagens/back.png",
                Color.LIGHTSKYBLUE, this::exibirMenuPrincipal);

        HBox linha1 = new HBox(20, btnNormal, btnMachine, btnDualMission);
        HBox linha2 = new HBox(20, btnSurvivalBots,btnVoltar);

        linha1.setAlignment(Pos.CENTER);
        linha2.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(
                criarLayoutPadrao("Selecionar Modo De Jogo", linha1, linha2), 700, 500));
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

        VBox layout = new VBox(30);
        layout.getChildren().add(titulo);
        layout.getChildren().addAll(conteudo);

        layout.setAlignment(Pos.CENTER);
        layout.setStyle(ESTILO_FUNDO);
        return layout;
    }

    private String toRgb(Color c) {
        return String.format("rgb(%d,%d,%d)",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255));
    }
}