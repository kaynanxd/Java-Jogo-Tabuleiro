package com.example.jogo1;
import javafx.scene.paint.Color;
import java.util.List; // Necessário para o método acao das Casas, mas não para rolarMeusDados()

public class Jogador {
    protected String nomeJogador;
    protected String tipoJogador;
    protected int posicaoAtual;
    protected Dados dados;
    protected boolean jogadorParalisado = false;
    protected int rodadasParalisado = 0;
    protected int casasAndadas;
    protected Tabuleiro tabuleiro;
    protected Color cor;

    public Jogador(String nomeJogador, String tipoJogador, Tabuleiro tabuleiro) {
        this.nomeJogador = nomeJogador;
        this.tipoJogador = tipoJogador;
        this.posicaoAtual = 0;
        this.casasAndadas = 0;
        this.dados = new Dados(); // Instancia Dados aqui
        this.tabuleiro = tabuleiro;
    }

    public String getCorHex() {
        return String.format("#%02X%02X%02X",
                (int) (cor.getRed() * 255),
                (int) (cor.getGreen() * 255),
                (int) (cor.getBlue() * 255));
    }

    public Color getCor() {return cor;}

    public void setCor(Color cor) {this.cor = cor;}

    public String getNomeJogador() {
        return nomeJogador;
    }

    public String getTipoJogador() {
        return tipoJogador;
    }

    public void setTipoJogador(String tipo) {
        this.tipoJogador = tipo;
    }

    public int getPosicaoAtual() {
        return posicaoAtual;
    }

    public void setPosicaoAtual(int posicao) {
        this.posicaoAtual = posicao;
    }

    public void setRodadasParaliso(int rodadas) {
        this.rodadasParalisado = rodadas;
        this.jogadorParalisado = rodadas > 0;
    }

    // Novo método para o jogador rolar seus próprios dados, aplicando a lógica de seu tipo
    public void rolarMeusDados() {
        if (this.dados.getModoJogo() == Dados.ModoJogo.DEBUG) {
            // Em modo DEBUG, a rolagem é definida externamente (pelo JogoTabuleiro)
            // mas o Jogador ainda precisa ter o método para ser chamado.
            // A responsabilidade de obter os inputs de debug ainda estaria no JogoTabuleiro.
            // Para simplificar, vou manter a chamada direta em JogoTabuleiro,
            // mas conceitualmente, o jogador "rolaria" os dados dele.
            // Para manter a consistência, o JogoTabuleiro chamaria dados.setDadosDebug() diretamente.
            // Então, este método `rolarMeusDados()` será para o modo NORMAL.
             // Não faz nada aqui para o modo debug, pois JogoTabuleiro vai setar os valores diretamente.
            // A lógica para obter valores de debug será tratada no JogoTabuleiro.
            return;
        }

        int tentativas = 0;
        do {
            this.dados.rolarDados(); // Rola os dados (agora sem tipo de jogador)
            this.casasAndadas += this.dados.getSomaDados(); // Já adiciona aqui, ou pode mover para JogoTabuleiro

            boolean condicaoSatisfeita = true;

            if (this.tipoJogador.equals("Sortudo")) {
                condicaoSatisfeita = (this.dados.getSomaDados() >= 7);
            } else if (this.tipoJogador.equals("Azarado")) {
                condicaoSatisfeita = (this.dados.getSomaDados() <= 6);
            }

            if (condicaoSatisfeita) {
                break; // Sai do loop se a condição for satisfeita
            }
            tentativas++;
            // Opcional: Adicionar um popup informando sobre a re-rolagem, como em Dados original
        } while (true);
    }
}