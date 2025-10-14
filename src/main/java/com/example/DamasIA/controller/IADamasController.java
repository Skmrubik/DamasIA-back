package com.example.DamasIA.controller;

import com.example.DamasIA.dto.Scenary;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class IADamasController {
    public Integer sumAllPieces(Scenary scenary) {
        int suma = 0;
        List<List<Integer>> board = scenary.getBoard();
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
        System.out.println("Pieza: ["+ scenary.getPiece().get(0)+","+scenary.getPiece().get(1)+"]");
        return suma;
    }
}
