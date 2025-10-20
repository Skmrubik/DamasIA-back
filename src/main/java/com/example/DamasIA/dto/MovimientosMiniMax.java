package com.example.DamasIA.dto;

import java.util.ArrayList;
import java.util.List;

public class MovimientosMiniMax {
    Integer minimax;
    List<Piece> pathsFinal;
    Piece pathInitFinal;

    public MovimientosMiniMax() {
        pathsFinal = new ArrayList<>();
    }

    public MovimientosMiniMax(Integer minimax, List<Piece> pathsFinal, Piece pathInitFinal) {
        this.minimax = minimax;
        this.pathsFinal = pathsFinal;
        this.pathInitFinal = pathInitFinal;
    }

    public MovimientosMiniMax(Integer minimax) {
        this.minimax = minimax;
        pathsFinal = new ArrayList<>();
    }

    public Integer getMinimax() {
        return minimax;
    }

    public List<Piece> getPathsFinal() {
        return pathsFinal;
    }

    public Piece getPathInitFinal() {
        return pathInitFinal;
    }
}
