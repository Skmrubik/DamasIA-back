package com.example.DamasIA.service;

import com.example.DamasIA.controller.IADamasController;
import com.example.DamasIA.dto.Movimientos;
import com.example.DamasIA.dto.Scenary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RestController
@RequestMapping("/api")
public class IADamasRest {

    @Autowired
    IADamasController iaDamasController;

    @PostMapping("/nextMove")
    public ResponseEntity<Movimientos> nextMove(@RequestBody Scenary scenary) {
        try {
            System.out.println("nextMove");
            Movimientos movimientos = iaDamasController.sumAllPieces(scenary.getBoard());

            return new ResponseEntity<>(movimientos, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/nextMove2")
    public String getNextMove() {
        return "El siguiente movimiento es...";
    }
}
