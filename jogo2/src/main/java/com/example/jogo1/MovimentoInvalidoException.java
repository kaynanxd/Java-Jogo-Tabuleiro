package com.example.jogo1;
public class MovimentoInvalidoException extends Exception{
    public MovimentoInvalidoException(String sentido){
        super("Movimento inválido:"+ sentido);
    }
    public MovimentoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
