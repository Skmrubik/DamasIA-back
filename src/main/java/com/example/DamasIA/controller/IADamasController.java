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
        List<List<Piece>> paths = new ArrayList<>();
        List<Piece> pathInit = new ArrayList<>();
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
                    List<Piece> path = new ArrayList<>();
                    explora(true, true, false, false, piece, piece, path, paths,pathInit, false);
                    //pathInit.add(piece);
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
        System.out.println("PATH "+paths.size());
        System.out.println("PATH INIT "+pathInit.size());
        for(int i=0; i<pathInit.size(); i++){
            List<Piece> p = paths.get(i);
            Piece pieceInit = pathInit.get(i);
            System.out.print(pieceInit.to_string()+ ": ");
            for (Piece piece1 : p){
                System.out.print(piece1.to_string()+ " ");
            }
            System.out.println(" ");
        }

        /*
        for (int i=0; i<movimientosPosibles.size(); i++){
            movimientosPosibles.get(i).toStringMovs();
        }*/
        Movimientos movimientosIter = new Movimientos();
        movimientosIter.setPiece(pathInit.get(0).getPiece());
        Map<List<Integer>, List<Integer>> moves = new HashMap<>();
        for (int i=0; i<paths.get(0).size(); i+=2){
            moves.put(paths.get(0).get(i).getPiece(), paths.get(0).get(i+1).getPiece());
        }
        movimientosIter.setMoves(moves);
        movimientosPosibles.add(movimientosIter);

        return movimientosPosibles.get(0);
    }

    public Integer explora(boolean izq,boolean inicio, boolean saltoAnterior, boolean yaSaltado, Piece posInicial,
                        Piece posActual, List<Piece> path, List<List<Piece>> paths, List<Piece> pathInit, boolean pathInitAdd){
        if (!yaSaltado && inicio && posActual.getX()<7) {
            int left = returnMoveLeftSimple(posInicial.getX(), posInicial.getY());
            int right = returnMoveRightSimple(posInicial.getX(), posInicial.getY());
            Piece pieceLeft = new Piece(posInicial.getX() + 1, posInicial.getY() - 1);
            Piece pieceRight = new Piece(posInicial.getX() + 1, posInicial.getY() + 1);
            if (left == 0) {
                path.add(pieceLeft);
                path.add(new Piece());
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                path.clear();
            } else if (left == 1) {
                explora(true, false, true, false, posInicial, pieceLeft, path, paths, pathInit, false);
            }
            if (right == 0) {
                path.add(pieceRight);
                path.add(new Piece());
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                path.clear();
            } else if (right == 1) {
                explora(false, false, true, false, posInicial, pieceRight,  path, paths, pathInit, false);
            }
            return 0;
        } else if (saltoAnterior && posActual.getX()<7){
            Piece pieceSkipped = new Piece(posActual.getX(), posActual.getY());
            if (izq){
                if (returnMoveLeftSimple(posActual.getX(), posActual.getY()) == 0) {
                    Piece piece = new Piece(posActual.getX() + 1, posActual.getY() - 1);
                    path.add(piece);
                    path.add(pieceSkipped);
                    explora(izq, true, false, true, posInicial, piece,path, paths, pathInit, true);
                }
                else{
                    return 0;
                }
            } else {
                if (returnMoveRightSimple(posActual.getX(), posActual.getY()) == 0) {
                    Piece piece = new Piece(posActual.getX() + 1, posActual.getY() + 1);
                    path.add(piece);
                    path.add(pieceSkipped);
                    explora(izq, true, false, true, posInicial, piece, path, paths, pathInit, true);
                }
                else{
                    return 0;
                }
            }
        } else if (yaSaltado && inicio){
            if (returnMoveLeftSimple(posActual.getX(), posActual.getY()) == 1) {
                Piece piece = new Piece(posActual.getX() + 1, posActual.getY() - 1);
                explora(true, false, true, true, posInicial, piece,  path, paths, pathInit, pathInitAdd);
            } else {
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                int ini = path.size() - 2; // En este caso: 5 - 2 = 3
                int fin = path.size();
                path.subList(ini, fin).clear();
                return 0;
            }
            if (returnMoveRightSimple(posActual.getX(), posActual.getY()) == 1) {
                Piece piece = new Piece(posActual.getX() + 1, posActual.getY() + 1);
                explora(false, false, true, true, posInicial, piece,  path, paths, pathInit,pathInitAdd);
            } else {
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                int ini = path.size() - 2; // En este caso: 5 - 2 = 3
                int fin = path.size();
                path.subList(ini, fin).clear();
                return 0;
            }
        }
        return 0;
    }
    private Integer returnPiece(int row, int col){
        return scenaryBoard.get(row).get(col);
    }

    private Integer returnMoveLeftSimple(int row, int col){
        if (col>0 && row<7)
            return scenaryBoard.get(row+1).get(col-1);
        else
            return -1;
    }
    private Integer returnMoveRightSimple(int row, int col){
        if (col<7 && row<7)
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
