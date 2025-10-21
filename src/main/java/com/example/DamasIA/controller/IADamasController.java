package com.example.DamasIA.controller;

import com.example.DamasIA.dto.Movimientos;
import com.example.DamasIA.dto.MovimientosMiniMax;
import com.example.DamasIA.dto.Piece;
import com.example.DamasIA.dto.Scenary;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class IADamasController {

    private List<List<Integer>> scenaryBoard;

    public Movimientos getRandomMovs(List<List<Integer>> board) {
        Scenary scenary = new Scenary(board);
        for (int i = 0; i < scenary.getBoard().size(); i++) {
            for (int j = 0; j < scenary.getBoard().get(i).size(); j++) {
                if (scenary.getBoard().get(i).get(j) == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + scenary.getBoard().get(i).get(j).toString() + " ");
                }
            }
            System.out.println("");
        }
        List<List<Piece>> paths = new ArrayList<>();
        List<Piece> pathInit = new ArrayList<>();
        List<Map<List<Integer>, List<Integer>>> movesPosibles = new ArrayList<>();
        List<Movimientos> jugadasPosibles = new ArrayList<>();
        // Movimientos movimientos = new Movimientos();

        genMovimientosPosibles(scenary, "IA", movesPosibles, jugadasPosibles, paths, pathInit);

        //Para cada movimiento generamos su valor heurisitico

        List<Integer> listHeuristics = new ArrayList<>();
        for(int i=0; i<pathInit.size(); i++){
            List<Piece> p = paths.get(i);
            Piece pieceInit = pathInit.get(i);
            System.out.print(pieceInit.to_string()+ ": ");
            for (Piece piece1 : p){
                System.out.print(piece1.to_string()+ " ");
            }
            /*
            Movimientos movimientosIter = new Movimientos();
            movimientosIter.setPiece(pathInit.get(i).getPiece());
            Map<List<Integer>, List<Integer>> moves = new LinkedHashMap<>();
            for (int j=0; j<paths.get(i).size(); j+=2){
                moves.put(paths.get(i).get(j).getPiece(), paths.get(i).get(j+1).getPiece());
            }
            movimientosIter.setMoves(moves);
            List<List<Integer>> boardTest = board.stream()
                    // 1. Mapea cada sublista a una nueva ArrayList, copiando sus elementos.
                    .map(ArrayList::new)
                    // 2. Colecta todas esas nuevas sublistas en una nueva lista exterior.
                    .collect(Collectors.toList());
            Scenary scenary4 = new Scenary(boardTest);
            doMove(scenary4, movimientosIter);
            int heuristic = heuristicScenary(scenary4);
            listHeuristics.add(heuristic);
            System.out.print(" - "+ heuristic);
            System.out.println(" ");*/
        }

        List<Scenary> listaScenarios = genEscenariosPosibles(scenary, paths, pathInit);
        System.out.println("ESCENARIOS: "+ listaScenarios.size());
        for (int i=1; i<=listaScenarios.size(); i++){
            System.out.println("--- "+i+" ---");
            printScenary(listaScenarios.get(i-1));
        }
        //Calculamos el indice de mayor valor heuristico
        /*
        int indexHeuristic=0;
        int maxHeuristic=0;
        for (int i=0 ; i<listHeuristics.size(); i++){
            if (listHeuristics.get(i)>maxHeuristic){
                indexHeuristic= i;
                maxHeuristic = listHeuristics.get(i);
            }
        }*/
        List<List<Piece>> pathsAux = new ArrayList<>();
        List<Piece> pathsFinal = new ArrayList<>();
        Piece pathInitFinal = new Piece();
        MovimientosMiniMax movesMinimax = minimax(0, scenary, pathsAux);

        pathsFinal = movesMinimax.getPathsFinal();
        pathInitFinal = movesMinimax.getPathInitFinal();
        Movimientos movimientos = new Movimientos();
        movimientos.setPiece(pathInitFinal.getPiece());
        Map<List<Integer>, List<Integer>> moves = new LinkedHashMap<>();
        for (int i=0; i<pathsFinal.size(); i+=2){
            moves.put(pathsFinal.get(i).getPiece(), pathsFinal.get(i+1).getPiece());
        }
        //Realizamos los movimientos
        movimientos.setMoves(moves);
        doMove(scenary, movimientos);
        System.out.println("ESCENARIO TRAS MOVIMIENTO IA");

        for (int i = 0; i < scenary.getBoard().size(); i++) {
            for (int j = 0; j < scenary.getBoard().get(i).size(); j++) {
                if (scenary.getBoard().get(i).get(j) == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + scenary.getBoard().get(i).get(j).toString() + " ");
                }
            }
            System.out.println("");
        }

        return movimientos;
    }

    private MovimientosMiniMax minimax(int prof, Scenary escenario, List<List<Piece>> paths){
        if (prof==1){
            List<Map<List<Integer>, List<Integer>>> movesPosibles = new ArrayList<>();
            List<Movimientos> jugadasPosibles = new ArrayList<>();
            List<List<Piece>> pathsAux = new ArrayList<>();
            List<Piece> pathInit = new ArrayList<>();
            genMovimientosPosibles(escenario, "JUG", movesPosibles, jugadasPosibles, pathsAux, pathInit);
            System.out.println("MOVIMIENTO");
            for (int i = 0; i < escenario.getBoard().size(); i++) {
                for (int j = 0; j < escenario.getBoard().get(i).size(); j++) {
                    if (escenario.getBoard().get(i).get(j) == 0) {
                        System.out.print("   ");
                    } else {
                        System.out.print(" " + escenario.getBoard().get(i).get(j).toString() + " ");
                    }
                }
                System.out.println("");
            }
            for(int i=0; i<pathsAux.size(); i++){
                List<Piece> p = pathsAux.get(i);
                Piece pieceInit = pathInit.get(i);
                System.out.print(pieceInit.to_string()+ ": ");
                for (Piece piece1 : p){
                    System.out.print(piece1.to_string()+ " ");
                }
                System.out.println("");
            }
            System.out.println("Heuristica: "+ heuristicScenary(escenario,pathsAux));
            return new MovimientosMiniMax(heuristicScenary(escenario,pathsAux));
        } else {
            String turno;
            if (prof%2== 0){
                turno = "IA";
            } else {
                turno = "JUG";
            }
            List<Scenary> escenariosGen = new ArrayList<>();
            List<Map<List<Integer>, List<Integer>>> movesPosibles = new ArrayList<>();
            List<Movimientos> jugadasPosibles = new ArrayList<>();
            List<List<Piece>> pathsAux = new ArrayList<>();
            List<Piece> pathInit = new ArrayList<>();
            List<Piece> pathsFinal = new ArrayList<>();
            Piece pathInitFinal = new Piece();
            genMovimientosPosibles(escenario, turno, movesPosibles, jugadasPosibles, pathsAux, pathInit);
            escenariosGen = genEscenariosPosibles(escenario,pathsAux, pathInit);
            int min = 1000000;
            int max = -1000000;
            for (int i=0; i<escenariosGen.size(); i++){
                MovimientosMiniMax heuristic = minimax(prof+1, escenariosGen.get(i), pathsAux);
                if (turno == "JUG"){
                    if (heuristic.getMinimax()<min){
                        min = heuristic.getMinimax();
                        pathsFinal = pathsAux.get(i);
                        pathInitFinal = pathInit.get(i);
                    }
                } else {
                    if (heuristic.getMinimax()>max){
                        max = heuristic.getMinimax();
                        pathsFinal = pathsAux.get(i);
                        pathInitFinal = pathInit.get(i);
                    }
                }
            }
            if (turno == "JUG"){
                return new MovimientosMiniMax(min, pathsFinal, pathInitFinal);
            } else {
                return new MovimientosMiniMax(max, pathsFinal, pathInitFinal);
            }
        }
    }

    private Integer calculateMinList(List<Integer> heuristics){
        int min = 1000000;
        for(Integer heur: heuristics){
            if (heur<min){
                min = heur;
            }
        }
        return min;
    }
    private Integer calculateMaxList(List<Integer> heuristics){
        int max = -10000000;
        for(Integer heur: heuristics){
            if (heur>max){
                max = heur;
            }
        }
        return max;
    }
    private void genMovimientosPosibles(Scenary scenary, String jugador, List<Map<List<Integer>, List<Integer>>> movesPosibles,
                                        List<Movimientos> jugadasPosibles, List<List<Piece>> paths, List<Piece> pathInit) {
        for (int row = 0; row < scenary.getBoard().size(); row++) {
            for (int col = 0; col < scenary.getBoard().get(row).size(); col++) {
                if (jugador.equals("IA") && scenary.getBoard().get(row).get(col) == 2) {
                    Map<List<Integer>, List<Integer>> moves = new LinkedHashMap<>();
                    Piece piece = new Piece(row, col);
                    List<Piece> path = new ArrayList<>();
                    exploraIA(scenary, true, true, false, false, piece, piece, path, paths,pathInit, false);
                    Movimientos movimientosIter = new Movimientos();
                    movimientosIter.setPiece(piece.getPiece());
                    movimientosIter.setMoves(moves);
                    if (!moves.isEmpty()){
                        movesPosibles.add(moves);
                        jugadasPosibles.add(movimientosIter);
                    }
                } else if (jugador.equals("JUG") && scenary.getBoard().get(row).get(col) == 1){
                    Map<List<Integer>, List<Integer>> moves = new LinkedHashMap<>();
                    Piece piece = new Piece(row, col);
                    List<Piece> path = new ArrayList<>();
                    exploraJugador(scenary, true, true, false, false, piece, piece, path, paths,pathInit, false);
                    Movimientos movimientosIter = new Movimientos();
                    movimientosIter.setPiece(piece.getPiece());
                    movimientosIter.setMoves(moves);
                    if (!moves.isEmpty()){
                        movesPosibles.add(moves);
                        jugadasPosibles.add(movimientosIter);
                    }
                }
            }
        }
    }
    private void printScenary(Scenary scenary){
        for (int i = 0; i < scenary.getBoard().size(); i++) {
            for (int j = 0; j < scenary.getBoard().get(i).size(); j++) {
                if (scenary.getBoard().get(i).get(j) == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + scenary.getBoard().get(i).get(j).toString() + " ");
                }
            }
            System.out.println("");
        }

    }
    private List<Scenary> genEscenariosPosibles(Scenary scenary, List<List<Piece>> paths, List<Piece> pathInit) {
        List<Scenary> escenariosPosibles = new ArrayList<>();
        for(int i=0; i<paths.size(); i++){
            Movimientos movimientosIter = new Movimientos();
            movimientosIter.setPiece(pathInit.get(i).getPiece());
            Map<List<Integer>, List<Integer>> moves = new LinkedHashMap<>();
            for (int j=0; j<paths.get(i).size(); j+=2){
                moves.put(paths.get(i).get(j).getPiece(), paths.get(i).get(j+1).getPiece());
            }
            movimientosIter.setMoves(moves);
            List<List<Integer>> boardTest = scenary.getBoard().stream()
                    // 1. Mapea cada sublista a una nueva ArrayList, copiando sus elementos.
                    .map(ArrayList::new)
                    // 2. Colecta todas esas nuevas sublistas en una nueva lista exterior.
                    .collect(Collectors.toList());
            Scenary scenaryTest = new Scenary(boardTest);
            doMove(scenaryTest, movimientosIter);
            escenariosPosibles.add(scenaryTest);
        }
        return escenariosPosibles;
    }
    public Integer exploraIA(Scenary scenary, boolean izq,boolean inicio, boolean saltoAnterior, boolean yaSaltado, Piece posInicial,
                        Piece posActual, List<Piece> path, List<List<Piece>> paths, List<Piece> pathInit, boolean pathInitAdd){
        if (!yaSaltado && inicio && posActual.getX()<7) {
            int left = returnMoveLeftSimple(scenary, posInicial.getX(), posInicial.getY());
            int right = returnMoveRightSimple(scenary, posInicial.getX(), posInicial.getY());
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
                exploraIA(scenary,true, false, true, false, posInicial, pieceLeft, path, paths, pathInit, false);
            }
            if (right == 0) {
                path.add(pieceRight);
                path.add(new Piece());
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                path.clear();
            } else if (right == 1) {
                exploraIA(scenary,false, false, true, false, posInicial, pieceRight,  path, paths, pathInit, false);
            }
            return 0;
        } else if (saltoAnterior && posActual.getX()<7){
            Piece pieceSkipped = new Piece(posActual.getX(), posActual.getY());
            if (izq){
                if (returnMoveLeftSimple(scenary, posActual.getX(), posActual.getY()) == 0) {
                    Piece piece = new Piece(posActual.getX() + 1, posActual.getY() - 1);
                    path.add(piece);
                    path.add(pieceSkipped);
                    exploraIA(scenary,izq, true, false, true, posInicial, piece,path, paths, pathInit, true);
                }
                else{
                    return 0;
                }
            } else {
                if (returnMoveRightSimple(scenary, posActual.getX(), posActual.getY()) == 0) {
                    Piece piece = new Piece(posActual.getX() + 1, posActual.getY() + 1);
                    path.add(piece);
                    path.add(pieceSkipped);
                    exploraIA(scenary,izq, true, false, true, posInicial, piece, path, paths, pathInit, true);
                }
                else{
                    return 0;
                }
            }
        } else if (yaSaltado && inicio){
            int tamPath = path.size();
            if (returnMoveLeftSimple(scenary, posActual.getX(), posActual.getY()) == 1) {
                Piece piece = new Piece(posActual.getX() + 1, posActual.getY() - 1);
                exploraIA(scenary,true, false, true, true, posInicial, piece,  path, paths, pathInit, pathInitAdd);
            } else {
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                int ini = path.size() - 2; // En este caso: 5 - 2 = 3
                int fin = path.size();
                path.subList(ini, fin).clear();
                return 0;
            }
            if (returnMoveRightSimple(scenary, posActual.getX(), posActual.getY()) == 1) {
                Piece piece = new Piece(posActual.getX() + 1, posActual.getY() + 1);
                exploraIA(scenary,false, false, true, true, posInicial, piece,  path, paths, pathInit,pathInitAdd);
            } else {
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                int ini = path.size() - 2; // En este caso: 5 - 2 = 3
                int fin = path.size();
                path.subList(ini, fin).clear();
                return 0;
            }
            if (tamPath == path.size()){
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                path.clear();
            }
        }
        return 0;
    }

    public Integer exploraJugador(Scenary scenary, boolean izq,boolean inicio, boolean saltoAnterior, boolean yaSaltado, Piece posInicial,
                             Piece posActual, List<Piece> path, List<List<Piece>> paths, List<Piece> pathInit, boolean pathInitAdd){
        if (!yaSaltado && inicio && posActual.getX()>0) {
            int left = returnMoveLeftSimpleJugador(scenary, posInicial.getX(), posInicial.getY());
            int right = returnMoveRightSimpleJugador(scenary, posInicial.getX(), posInicial.getY());
            Piece pieceLeft = new Piece(posInicial.getX() - 1, posInicial.getY() - 1);
            Piece pieceRight = new Piece(posInicial.getX() - 1, posInicial.getY() + 1);
            if (left == 0) {
                path.add(pieceLeft);
                path.add(new Piece());
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                path.clear();
            } else if (left == 2) {
                exploraJugador(scenary,true, false, true, false, posInicial, pieceLeft, path, paths, pathInit, false);
            }
            if (right == 0) {
                path.add(pieceRight);
                path.add(new Piece());
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                path.clear();
            } else if (right == 2) {
                exploraJugador(scenary,false, false, true, false, posInicial, pieceRight,  path, paths, pathInit, false);
            }
            return 0;
        } else if (saltoAnterior && posActual.getX()>0){
            Piece pieceSkipped = new Piece(posActual.getX(), posActual.getY());
            if (izq){
                if (returnMoveLeftSimpleJugador(scenary, posActual.getX(), posActual.getY()) == 0) {
                    Piece piece = new Piece(posActual.getX() - 1, posActual.getY() - 1);
                    path.add(piece);
                    path.add(pieceSkipped);
                    exploraJugador(scenary,izq, true, false, true, posInicial, piece,path, paths, pathInit, true);
                }
                else{
                    return 0;
                }
            } else {
                if (returnMoveRightSimpleJugador(scenary, posActual.getX(), posActual.getY()) == 0) {
                    Piece piece = new Piece(posActual.getX() - 1, posActual.getY() + 1);
                    path.add(piece);
                    path.add(pieceSkipped);
                    exploraJugador(scenary,izq, true, false, true, posInicial, piece, path, paths, pathInit, true);
                }
                else{
                    return 0;
                }
            }
        } else if (yaSaltado && inicio){
            int tamPath = path.size();
            if (returnMoveLeftSimpleJugador(scenary, posActual.getX(), posActual.getY()) == 2) {
                Piece piece = new Piece(posActual.getX() - 1, posActual.getY() - 1);
                exploraJugador(scenary,true, false, true, true, posInicial, piece,  path, paths, pathInit, pathInitAdd);
            } else {
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                int ini = path.size() - 2; // En este caso: 5 - 2 = 3
                int fin = path.size();
                path.subList(ini, fin).clear();
                return 0;
            }
            if (returnMoveRightSimpleJugador(scenary, posActual.getX(), posActual.getY()) == 2) {
                Piece piece = new Piece(posActual.getX() - 1, posActual.getY() + 1);
                exploraJugador(scenary,false, false, true, true, posInicial, piece,  path, paths, pathInit,pathInitAdd);
            } else {
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                int ini = path.size() - 2; // En este caso: 5 - 2 = 3
                int fin = path.size();
                path.subList(ini, fin).clear();
                return 0;
            }
            if (tamPath == path.size()){
                List<Piece> pathCopy = new ArrayList<>(path);
                paths.add(pathCopy);
                pathInit.add(posInicial);
                path.clear();
            }
        }
        return 0;
    }

    private Integer returnPiece(int row, int col){
        return scenaryBoard.get(row).get(col);
    }

    private Integer heuristicScenary(Scenary scenary, List<List<Piece>> pathsJugador){
        int fichasJugador = 0;
        int fichasIA = 0;
        for (int i = 0; i < scenary.getBoard().size(); i++) {
            for (int j = 0; j < scenary.getBoard().get(i).size(); j++) {
                if (scenary.getBoard().get(i).get(j) == 1) {
                    fichasJugador++;
                } else if (scenary.getBoard().get(i).get(j) == 2){
                    fichasIA++;
                }
            }
        }
        fichasIA*=10;
        fichasJugador*=10;
        int sumaPosiblesMovs=0;
        for (List<Piece> mov: pathsJugador) {
            if (mov.size() <3){
                if(mov.get(1).getPiece().isEmpty())
                    sumaPosiblesMovs+=1;
                else
                    sumaPosiblesMovs+=3;
            }
            else if (mov.size() <7){
                sumaPosiblesMovs+=6;
            } else {
                sumaPosiblesMovs+=10;
            }

        }
        return (fichasIA*50) - (fichasJugador*50) - sumaPosiblesMovs;
    }
    private void doMove(Scenary scenary, Movimientos movimientos){
        List<Integer> piece = movimientos.getPiece();
        List<List<Integer>> clavesOrdenadas = new ArrayList<>(movimientos.getMoves().keySet());
        List<Integer> pieceFinal = clavesOrdenadas.get(clavesOrdenadas.size() - 1);
        scenary.getBoard().get(piece.get(0)).set(piece.get(1), 0);
        scenary.getBoard().get(pieceFinal.get(0)).set(pieceFinal.get(1), 2);
        for (List<Integer> pieceSkipped : movimientos.getMoves().values()) {
            if (!pieceSkipped.isEmpty()){
                scenary.getBoard().get(pieceSkipped.get(0)).set(pieceSkipped.get(1), 0);
            }
        }
    }
    private Integer returnMoveLeftSimple(Scenary scenary, int row, int col){
        if (col>0 && row<7)
            return scenary.getBoard().get(row+1).get(col-1);
        else
            return -1;
    }
    private Integer returnMoveRightSimple(Scenary scenary, int row, int col){
        if (col<7 && row<7)
            return scenary.getBoard().get(row+1).get(col+1);
        else
            return -1;
    }
    private Integer returnMoveLeftSimpleJugador(Scenary scenary, int row, int col){
        if (col>0 && row>0)
            return scenary.getBoard().get(row-1).get(col-1);
        else
            return -1;
    }
    private Integer returnMoveRightSimpleJugador(Scenary scenary, int row, int col){
        if (col<7 && row>0)
            return scenary.getBoard().get(row-1).get(col+1);
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
