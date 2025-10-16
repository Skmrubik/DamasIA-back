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
        //List<Map<Piece, Piece>> movesPosibles = new ArrayList<>();
        //List<Map<Piece, Piece>> movesSkipPosibles = new ArrayList<>();
        List<Map<List<Integer>, List<Integer>>> movesPosibles = new ArrayList<>();
        //List<Piece> pieceOrigen = new ArrayList<>();
        //List<Piece> pieceOrigenSkip = new ArrayList<>();
        List<Movimientos> movimientosPosibles = new ArrayList<>();
        Movimientos movimientos = new Movimientos();
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                if (board.get(row).get(col) == 2) {
                    Map<List<Integer>, List<Integer>> moves = new HashMap<>();
                    Piece piece = new Piece(row, col);
                    explora(true, true, false, false, piece, piece,moves);
                    Movimientos movimientosIter = new Movimientos();
                    movimientosIter.setPiece(piece.getPiece());
                    movimientosIter.setMoves(moves);
                    if (moves.size()>0){
                        movesPosibles.add(moves);
                        movimientosPosibles.add(movimientosIter);
                    }
                }
            }
        }
        for (int i=0; i<movimientosPosibles.size(); i++){
            movimientosPosibles.get(i).toStringMovs();
        }

        return movimientosPosibles.get(0);
    }

    public Map<List<Integer>, List<Integer>> explora(boolean izq,boolean inicio, boolean saltoAnterior, boolean yaSaltado, Piece posInicial,
                        Piece posActual, Map<List<Integer>, List<Integer>> moves){
        if (!yaSaltado && inicio && posActual.getX()<7) {
            if (returnMoveLeftSimple(posInicial.getX(), posInicial.getY()) == 0) {
                Piece piece = new Piece(posInicial.getX() + 1, posInicial.getY() - 1);
                List<Integer> movSimple = piece.getPiece();
                List<Integer> aux = new ArrayList<>();
                moves.put(movSimple, aux);
            }
            if (returnMoveRightSimple(posInicial.getX(), posInicial.getY()) == 0) {
                Piece piece = new Piece(posInicial.getX() + 1, posInicial.getY() + 1);
                List<Integer> movSimple = piece.getPiece();
                List<Integer> aux = new ArrayList<>();
                moves.put(movSimple, aux);
            }
            if (returnMoveLeftSimple(posInicial.getX(), posInicial.getY()) == 1) {
                Piece piece = new Piece(posInicial.getX() + 1, posInicial.getY() - 1);
                explora(true, false, true, false, posInicial, piece, moves);
            }
            if (returnMoveRightSimple(posInicial.getX(), posInicial.getY()) == 1) {
                Piece piece = new Piece(posInicial.getX() + 1, posInicial.getY() + 1);
                explora(false, false, true, false, posInicial, piece, moves);
            }
            return moves;
        } else if (saltoAnterior && posActual.getX()<7){
            if (izq){
                if (returnMoveLeftSimple(posActual.getX(), posActual.getY()) == 0) {
                    Piece pieceSkipped = new Piece(posActual.getX(), posActual.getY());
                    Piece piece = new Piece(posActual.getX() + 1, posActual.getY() - 1);
                    List<Integer> piecePropia = piece.getPiece();
                    List<Integer> pieceSaltada = pieceSkipped.getPiece();
                    moves.put(piecePropia, pieceSaltada);
                    explora(izq, true, false, true, posInicial, piece, moves);
                }
                return moves;
            } else {
                if (returnMoveRightSimple(posActual.getX(), posActual.getY()) == 0) {
                    Piece pieceSkipped = new Piece(posActual.getX(), posActual.getY());
                    Piece piece = new Piece(posActual.getX() + 1, posActual.getY() + 1);
                    List<Integer> piecePropia = piece.getPiece();
                    List<Integer> pieceSaltada = pieceSkipped.getPiece();
                    moves.put(piecePropia, pieceSaltada);
                    explora(izq, true, false, true, posInicial, piece, moves);
                }
                return moves;
            }
        } else if (yaSaltado && inicio && posActual.getX()<7){
            if (returnMoveLeftSimple(posActual.getX(), posActual.getY()) == 1) {
                Piece piece = new Piece(posActual.getX() + 1, posActual.getY() - 1);
                explora(true, false, true, true, posInicial, piece, moves);
            }
            if (returnMoveRightSimple(posActual.getX(), posActual.getY()) == 1) {
                Piece piece = new Piece(posActual.getX() + 1, posActual.getY() + 1);
                explora(false, false, true, true, posInicial, piece, moves);
            }
            return moves;
        }
        return moves;
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
