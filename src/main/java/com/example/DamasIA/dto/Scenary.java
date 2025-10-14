package com.example.DamasIA.dto;

import java.util.List;

public class Scenary {
    List<List<Integer>> board;

    public Scenary(List<List<Integer>> board) {
        this.board = board;
    }

    public List<List<Integer>> getBoard() {
        return board;
    }

    public void setBoard(List<List<Integer>> board) {
        this.board = board;
    }

}
