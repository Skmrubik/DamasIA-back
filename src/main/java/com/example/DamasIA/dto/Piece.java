package com.example.DamasIA.dto;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    private List<Integer> xy;

    public Piece(){
        xy= new ArrayList<>();
    }
    public Piece(Integer x, Integer y){
        xy = new ArrayList<>();
        xy.add(x);
        xy.add(y);
    }
    public Integer getX(){
        return xy.get(0);
    }
    public Integer getY(){
        return xy.get(1);
    }
    public List<Integer> getPiece(){
        return xy;
    }
    public String to_string(){
        if (xy.isEmpty()) {
            return "()";
        } else {
            return "("+xy.get(0) + "," + xy.get(1)+")";
        }

    }
}
