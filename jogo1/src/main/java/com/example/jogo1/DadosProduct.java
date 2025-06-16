package com.example.jogo1;


import com.example.jogo1.Dados.ModoJogo;
import java.util.Scanner;

public class DadosProduct {
	private ModoJogo modoJogo;
	private Scanner sc;

	public ModoJogo getModoJogo() {
		return modoJogo;
	}

	public void setModoJogo2(ModoJogo modoJogo) {
		this.modoJogo = modoJogo;
	}

	public void setSc(Scanner sc) {
		this.sc = sc;
	}

	public void setModoJogo(ModoJogo modo) {
		this.modoJogo = modo;
		if (modo == ModoJogo.DEBUG && sc == null) {
			this.sc = new Scanner(System.in);
		}
	}
}