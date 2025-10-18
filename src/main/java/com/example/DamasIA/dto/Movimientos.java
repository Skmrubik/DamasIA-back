package com.example.DamasIA.dto;

import java.util.*;

public class Movimientos {
    public List<Integer> piece;
    public Map<List<Integer>, List<Integer>> moves;

    public Movimientos() {
        piece = new ArrayList<>();
        moves = new LinkedHashMap<>();
    }

    public List<Integer> getPiece() {
        return piece;
    }

    public void setPiece(List<Integer> piece) {
        this.piece = piece;
    }

    public Map<List<Integer>, List<Integer>> getMoves() {
        return moves;
    }

    public void setMoves(Map<List<Integer>, List<Integer>> moves) {
        this.moves = moves;
    }

    public void toStringMovs(){
        System.out.println("Pieza: ["+piece.get(0)+","+piece.get(1)+"]");
        System.out.println("Movimientos: "+ moves);
    }
}
