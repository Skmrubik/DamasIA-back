package com.example.DamasIA.dto;

import java.util.List;

public class Scenary {
    List<List<Integer>> board;
    List<Integer> piece;

    public Scenary(List<List<Integer>> board, List<Integer> piece) {
        this.board = board;
        this.piece = piece;
    }

    public List<List<Integer>> getBoard() {
        return board;
    }

    public void setBoard(List<List<Integer>> board) {
        this.board = board;
    }

    public List<Integer> getPiece() {
        return piece;
    }

    public void setPiece(List<Integer> piece) {
        this.piece = piece;
    }
}
