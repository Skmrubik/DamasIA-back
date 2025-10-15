package com.example.DamasIA.controller;

import com.example.DamasIA.dto.Movimientos;
import com.example.DamasIA.dto.Scenary;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class IADamasController {

    private List<List<Integer>> scenaryBoard;

    public Movimientos sumAllPieces(List<List<Integer>> board) {
        int suma = 0;
        scenaryBoard = board;
        for (int i=0; i<board.size(); i++){
            for(int j=0; j<board.get(i).size(); j++) {
                suma += board.get(i).get(j);
                if (board.get(i).get(j)==0){
                    System.out.print("   ");
                } else {
                    System.out.print(" "+ board.get(i).get(j).toString()+ " ");
                }
            }
            System.out.println("");
        }
        List<Map<List<Integer>, List<Integer>>> movesPosibles = new ArrayList<>();
        /*
        for (int row=0; row<board.size(); row++){
            for(int col=0; col<board.get(row).size(); col++) {
                if (board.get(row).get(col)==2){
                    if (returnMoveLeftSimple(row,col)==0){
                        Map<List<Integer>, List<Integer>> move = new HashMap<>();

                    }
                }
            }
        }*/
        int row = 2;
        int col = 3;
        List<List<Integer>> movesPosible = new ArrayList<>();
        if (returnPiece(row+1, col-1)== 0){
            List<Integer> pos = new ArrayList<>();
            pos.add(row+1);
            pos.add(col-1);
            movesPosible.add(pos);
        }
        if (returnPiece(row+1, col+1)== 0){
            List<Integer> pos = new ArrayList<>();
            pos.add(row+1);
            pos.add(col+1);
            movesPosible.add(pos);
        }
        Random r= new Random();
        Movimientos movimientos = new Movimientos();

        // Generate random integers in range 0 to 999
        int r1 = r.nextInt(2);
        Map<List<Integer>, List<Integer>> move = new HashMap<>();
        move.put(movesPosible.get(r1), new ArrayList<>());
        movimientos.setMoves(move);
        List<Integer> piece = new ArrayList<>();
        piece.add(2);
        piece.add(3);
        movimientos.setPiece(piece);
        return movimientos;
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
}
