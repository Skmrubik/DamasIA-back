package com.example.DamasIA.controller;

import com.example.DamasIA.dto.Movimientos;
import com.example.DamasIA.dto.Piece;
import com.example.DamasIA.dto.Scenary;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class IADamasController {

    private List<List<Integer>> scenaryBoard;

    public Movimientos getRandomMovs(List<List<Integer>> board) {
        int suma = 0;
        scenaryBoard = board;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                suma += board.get(i).get(j);
                if (board.get(i).get(j) == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + board.get(i).get(j).toString() + " ");
                }
            }
            System.out.println("");
        }
        List<Map<Piece, Piece>> movesPosibles = new ArrayList<>();
        List<Map<Piece, Piece>> movesSkipPosibles = new ArrayList<>();
        List<Piece> pieceOrigen = new ArrayList<>();
        List<Piece> pieceOrigenSkip = new ArrayList<>();
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                if (board.get(row).get(col) == 2) {
                    if (returnMoveLeftSimple(row, col) == 0) {
                        Map<Piece, Piece> move = new HashMap<>();
                        Piece piece1 = new Piece(row + 1, col - 1);
                        Piece piece2 = new Piece();
                        move.put(piece1, piece2);
                        movesPosibles.add(move);
                        pieceOrigen.add(new Piece(row, col));
                    }
                    if (returnMoveRightSimple(row, col) == 0) {
                        Map<Piece, Piece> move = new HashMap<>();
                        Piece piece1 = new Piece(row + 1, col + 1);
                        Piece piece2 = new Piece();
                        move.put(piece1, piece2);
                        movesPosibles.add(move);
                        pieceOrigen.add(new Piece(row, col));
                    }
                    if (returnMoveLeftSkipped(row, col) == 0) {
                        Map<Piece, Piece> move = new HashMap<>();
                        Piece piece1 = new Piece(row + 2, col - 2);
                        Piece piece2 = new Piece(row + 1, col - 1);
                        move.put(piece1, piece2);
                        movesSkipPosibles.add(move);
                        pieceOrigenSkip.add(new Piece(row, col));
                    }
                    if (returnMoveRightSkipped(row, col) == 0) {
                        Map<Piece, Piece> move = new HashMap<>();
                        Piece piece1 = new Piece(row + 2, col + 2);
                        Piece piece2 = new Piece(row + 1, col + 1);
                        move.put(piece1, piece2);
                        movesSkipPosibles.add(move);
                        pieceOrigenSkip.add(new Piece(row, col));
                    }
                }
            }
        }

        Random r = new Random();
        Movimientos movimientos = new Movimientos();
        if (!movesSkipPosibles.isEmpty()) {
            int r1 = r.nextInt(movesSkipPosibles.size());
            Map<List<Integer>, List<Integer>> moveReturn = new HashMap<>();
            Map<Piece, Piece> move = movesSkipPosibles.get(r1);
            for (Map.Entry<Piece, Piece> entry : move.entrySet()) {
                // Aquí se obtiene la clave:
                List<Integer> movPiece = entry.getKey().getPiece();
                List<Integer> pieceSkipped = entry.getValue().getPiece();
                moveReturn.put(movPiece, pieceSkipped);
            }
            movimientos.setMoves(moveReturn);
            movimientos.setPiece(pieceOrigenSkip.get(r1).getPiece());
            return movimientos;
        }
        else{
            r = new Random();
            // Generate random integers in range 0 to 999
            int r2 = r.nextInt(movesPosibles.size());
            Map<List<Integer>, List<Integer>> moveReturn = new HashMap<>();
            Map<Piece, Piece> move = movesPosibles.get(r2);
            for (Map.Entry<Piece, Piece> entry : move.entrySet()) {
                // Aquí se obtiene la clave:
                List<Integer> movSimple = entry.getKey().getPiece();
                moveReturn.put(movSimple, new ArrayList<>());
            }
            movimientos.setMoves(moveReturn);
            movimientos.setPiece(pieceOrigen.get(r2).getPiece());
            return movimientos;
        }
    }

    private Integer returnPiece(int row, int col){
        return scenaryBoard.get(row).get(col);
    }

    private Integer returnMoveLeftSimple(int row, int col){
        if (col>0)
            return scenaryBoard.get(row+1).get(col-1);
        else
            return -1;
    }
    private Integer returnMoveRightSimple(int row, int col){
        if (col<7)
            return scenaryBoard.get(row+1).get(col+1);
        else
            return -1;
    }
    private Integer returnMoveLeftSkipped(int row, int col){
        if (col>1 && scenaryBoard.get(row+1).get(col-1)==1){
            return scenaryBoard.get(row+2).get(col-2);
        } else {
            return -1;
        }
    }
    private Integer returnMoveRightSkipped(int row, int col){
        if (col<6 && scenaryBoard.get(row+1).get(col+1)==1){
            return scenaryBoard.get(row+2).get(col+2);
        } else {
            return -1;
        }
    }
}
