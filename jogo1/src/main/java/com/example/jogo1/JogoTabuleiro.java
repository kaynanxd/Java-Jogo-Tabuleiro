
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

    private JogoTabuleiroProduct jogoTabuleiroProduct;
    private static final String COR_NORMAL = "#dbffd9";
    private static final String COR_SURPRESA = "#59ffff";
    private static final String COR_SORTE = "#FFFF00";
    private static final String COR_PARALISIA = "#ffa1a1";
    private static final String COR_VOLTAR_INICIO = "#ffb485";
    private static final String COR_MAGICA = "#8aa5ff";

    private static final String AUDIO_CONFIRM = "src/audios/confirm.mp3";
    private static final String AUDIO_CLIQUE = "src/audios/clique.mp3";
    private static final String AUDIO_APLAUSOS = "src/audios/aplausos.mp3";
    private final MarcadorFactory marcadorFactory = new MarcadorFactory();

    private final ModoJogo modoJogo; 
    private final Musica musica;
    private final Tabuleiro tabuleiro;
    private final GridPane grid = new GridPane();
    private final StackPane[] casas = new StackPane[40];
    private Main mainApp;

    private Button botaoJogar;
    private Label labelVez;

    public JogoTabuleiro(List<Jogador> jogadores, Dados.ModoJogo modoJogoEnum, Main main, Stage primaryStage) {
        this.jogoTabuleiroProduct = new JogoTabuleiroProduct(jogadores);
        this.musica = new Musica(AUDIO_CLIQUE);
        this.tabuleiro = new Tabuleiro(jogadores);

        if (modoJogoEnum == Dados.ModoJogo.DEBUG) {
            this.modoJogo = new Debug();
        } else {
            this.modoJogo = new ModoNormal();
        }
        this.mainApp = main;

        for (Jogador jogador : jogadores) {
            jogador.tabuleiro = tabuleiro;
            jogador.dados = new Dados(modoJogoEnum);
        }
    }

    public void start(Stage stage) {
        BorderPane root = stageRoot();
		Scene scene = new Scene(root, 800, 700);
        stage.setScene(scene);
        stage.setTitle("Jogo de Tabuleiro");
        stage.show();
    }

	private BorderPane stageRoot() {
		BorderPane root = new BorderPane();
		criarTabuleiroVisual();
		configurarInterfaceJogo(root);
		acaoCliqueBotaoDados();
		return root;
	}

    private void configurarInterfaceJogo(BorderPane root) {
        HBox barraInferior = barraInferior();
		barraInferior.setPadding(new Insets(15));

        root.setBottom(barraInferior);
        root.setCenter(grid);
    }

	private HBox barraInferior() {
		botaoJogar = criarBotaoJogar();
		labelVBarraInferior();
		HBox barraInferior = new HBox(20, botaoJogar, labelVez);
		barraInferior.setAlignment(Pos.CENTER);
		return barraInferior;
	}

	private void labelVBarraInferior() {
		labelVez = new Label();
		atualizarTabuleiroVisual();
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
        gridConfig();
		for (int i = 0; i < 40; i++) {
            StackPane casa = criarCasaTabuleiro(i);
            casas[i] = casa;

            int linha = (i / 10) * 2;
            int coluna = (linha / 2 % 2 == 0) ? i % 10 : 9 - (i % 10);

            grid.add(casa, coluna, linha);

            if (i == 9 || i == 19 || i == 29) {
                adicionarConector(linha, coluna);
            }
        }
    }

	private void gridConfig() {
		grid.setHgap(0);
		grid.setVgap(0);
		grid.setStyle("-fx-background-color: #b3f0ff; -fx-padding: 10;");
	}

    private StackPane criarCasaTabuleiro(int numeroCasa) {
        StackPane casa = new StackPane();
        casa.setPrefSize(80, 80);
        casa.setStyle("-fx-border-color: black; -fx-background-color: " + obterCorCasa(numeroCasa) + ";");
        Label numero = new Label(String.valueOf(numeroCasa));
        casa.getChildren().add(numero);

        return casa;
    }

    private String obterCorCasa(int numeroCasa) {
        return switch (numeroCasa) {
            case 13 -> COR_SURPRESA;
            case 5, 15, 30 -> COR_SORTE;
            case 10, 25, 38 -> COR_PARALISIA;
            case 17, 27 -> COR_VOLTAR_INICIO;
            case 20, 35 -> COR_MAGICA;
            default -> COR_NORMAL;
        };
    }

    private void adicionarConector(int linha, int coluna) {
        StackPane conector = conector();
		Label seta = new Label("↓");
        conector.getChildren().add(seta);

        grid.add(conector, coluna, linha + 1);
    }

	private StackPane conector() {
		StackPane conector = new StackPane();
		conector.setPrefSize(80, 80);
		conector.setStyle("-fx-background-color: " + COR_NORMAL + "; -fx-border-color: black;");
		return conector;
	}

    public void atualizarTabuleiroVisual() {
        labelVez();
		for (int i = 0; i < 40; i++) {
            StackPane casa = casas[i];
            casa.getChildren().clear();

            VBox conteudo = new VBox(2);
            conteudo.setStyle("-fx-alignment: center;");
            conteudo.getChildren().add(new Label(String.valueOf(i)));

            GridPane marcadores = criarMarcadoresJogadores(i);
            conteudo.getChildren().add(marcadores);

            casa.getChildren().add(conteudo);
        }
    }

	private void labelVez() {
		Jogador atual = jogoTabuleiroProduct.getJogadores().get(jogoTabuleiroProduct.getIndiceJogadorAtual());
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
	}

    private GridPane criarMarcadoresJogadores(int numeroCasa) {
        GridPane marcadores = new GridPane();
        marcadores.setHgap(2);
        marcadores.setVgap(2);
        marcadores.setAlignment(Pos.CENTER);

        int indice = 0;
        for (Jogador jogador : jogoTabuleiroProduct.getJogadores()) {
            if (jogador.getPosicaoAtual() == numeroCasa) {
                Label marcador = marcadorFactory.criarPara(jogador);
                int col = indice % 3;
                int row = indice / 3;
                marcadores.add(marcador, col, row);
                indice++;
            }
        }

        return marcadores;
    }

    private void realizarJogada() {
        Jogador jogadorAtual = jogoTabuleiroProduct.getJogadores().get(jogoTabuleiroProduct.getIndiceJogadorAtual());

        if (jogadorAtual.rodadasParalisado > 0) {
            jogadorAtual.setRodadasParaliso(jogadorAtual.rodadasParalisado - 1);
            mostrarMensagem(jogadorAtual.getNomeJogador() + " está paralisado e perde a rodada.");
            jogoTabuleiroProduct.avancarJogador(this);
            return;
        }

        modoJogo.realizarJogada(jogadorAtual, this);
        mostrarResultadoDados(jogadorAtual);
        iniciarAnimacaoMovimento(jogadorAtual);
    }

    private void mostrarMensagem(String texto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(texto);
        alert.setHeaderText(null);
        alert.setTitle("Mensagem");
        alert.showAndWait();
    }

    private void mostrarResultadoDados(Jogador jogador) {
        mostrarMensagem(jogador.getNomeJogador() + " rolou " + jogador.dados.getSomaDados() + " (" + jogador.dados.dado1 + " + " + jogador.dados.dado2 + ")");
    }

    public void obterValoresDadosDebug(Jogador jogador) {
        String dado1 = PopupManager.showTextInputPopup("Modo Debug - Dado 1", "Digite o valor para o Dado 1", "1").orElse("1");
        String dado2 = PopupManager.showTextInputPopup("Modo Debug - Dado 2", "Digite o valor para o Dado 2", "1").orElse("1");

        try {
            int d1 = Integer.parseInt(dado1);
            int d2 = Integer.parseInt(dado2);
            jogador.dados.setDadosDebug(d1, d2);
        } catch (NumberFormatException e) {
            PopupManager.showWarningPopup("Valor Inválido", "Digite apenas números inteiros!.");
            jogador.dados.setDadosDebug(1, 1);
        }
    }

    private void iniciarAnimacaoMovimento(Jogador jogador) {
        int origem = jogador.getPosicaoAtual();
        int destino = Math.min(origem + jogador.dados.getSomaDados(), 39);

        Timeline animacao = new Timeline();
        List<KeyFrame> movimentos = new ArrayList<>();

        for (int i = origem + 1, passo = 1; i <= destino; i++, passo++) {
            int posicao = i;
            movimentos.add(new KeyFrame(Duration.seconds(passo * 0.3), e -> {
                jogador.setPosicaoAtual(posicao);
                atualizarTabuleiroVisual();
                musica.tocarEfeito(AUDIO_CLIQUE);
            }));
        }
        animacao.getKeyFrames().addAll(movimentos);
        animacao.setOnFinished(e -> finalizarJogada(jogador, destino));
        animacao.play();
    }

    private void finalizarJogada(Jogador jogador, int destino) {
        tabuleiro.verificarCasaEspecial(jogador);

        if (jogador.getPosicaoAtual() >= 39) {
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
                    mostrarEstatisticasJogo(jogador);
                });
            });
        } else {
            jogoTabuleiroProduct.avancarJogador(this);
        }
    }

    private void mostrarEstatisticasJogo(Jogador vencedor) {
        TextArea textArea = textAreaEstatisticas(vencedor);
		Alert alertEstatisticas = new Alert(Alert.AlertType.INFORMATION);
        alertEstatisticas.setTitle("Estatísticas do Jogo");
        alertEstatisticas.setHeaderText("Desempenho de Todos os Jogadores");

        textArea.setEditable(false);
        textArea.setWrapText(true);
        alertEstatisticas.getDialogPane().setContent(textArea);

        ButtonType btnMenu = new ButtonType("Voltar ao Menu", ButtonBar.ButtonData.OK_DONE);
        alertEstatisticas.getButtonTypes().setAll(btnMenu);

        alertEstatisticas.showAndWait().ifPresent(response -> {
            Stage stageAtual = (Stage) botaoJogar.getScene().getWindow();
            stageAtual.close();
            mainApp.ReiniciarJogo();
        });
    }

	private TextArea textAreaEstatisticas(Jogador vencedor) {
		StringBuilder estatisticas = estatisticasVencedor(vencedor);
		TextArea textArea = new TextArea(estatisticas.toString());
		textArea.setPrefSize(400, 300);
		return textArea;
	}

	private StringBuilder estatisticasVencedor(Jogador vencedor) {
		StringBuilder estatisticas = new StringBuilder();
		estatisticas.append("RESUMO DO JOGO\n\n");
		estatisticas.append("🏆 Vencedor: ").append(vencedor.getNomeJogador()).append("\n\n");
		estatisticas.append("ESTATÍSTICAS DOS JOGADORES:\n");
		for (Jogador jogador : jogoTabuleiroProduct.getJogadores()) {
			estatisticas.append("\n• ").append(jogador.getNomeJogador()).append(" (").append(jogador.getTipoJogador())
					.append("):\n").append("   - Posição final: Casa ").append(jogador.getPosicaoAtual()).append("\n")
					.append("   - Total de casas andadas: ").append(jogador.casasAndadas).append("\n")
					.append("   - Cor: ").append(jogador.getCorHex()).append("\n");
		}
		return estatisticas;
	}

    public Dados.ModoJogo getModoJogo() {
        return modoJogo.getModoJogo();
    }
}
