package com.unir.loans.controller;

import com.unir.loans.model.pojo.Loan;
import com.unir.loans.model.request.CreateLoanRequest;
import com.unir.loans.service.LoanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Loans Controller", description = "Microservicio encargado de manejar operaciones para el prestamo de libros en la biblioteca.")
public class LoansController {

    @Autowired
    private LoanServiceImpl service;

    @Operation(
            operationId = "Obtener prestamos",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todos los prestamos almacenados en la base de datos.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el prestamo con el identificador indicado.")
    @ApiResponse(
            responseCode = "500",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Error interno del servidor interno.")
    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getLoans(
            @Parameter(name = "idBook", description = "Id del libro prestado. Debe ser exacto", example = "1", required = false)
            @RequestParam(required = false) Integer idBook,
            @Parameter(name = "idClient", description = "Id del usuario que solicito el prestamo. Debe ser exacto", example = "10", required = false)
            @RequestParam(required = false) String idClient,
            @Parameter(name = "status", description = "Estado del prestamo. Debe ser exacto", example = "returned", required = false)
            @RequestParam(required = false) String status){

        List<Loan> loans = service.getLoans(idBook, idClient, status);
        if (loans != null) {
            return ResponseEntity.ok(loans);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/loans")
    @Operation(
            operationId = "Insertar un prestamo",
            description = "Operacion de escritura",
            summary = "Se crea un prestamo a partir de sus datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del prestamo a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateLoanRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "500",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Error interno del servidor interno.")
    public ResponseEntity<Loan> addLoan(@RequestBody CreateLoanRequest request) {

        Loan createdLoan = service.addLoan(request);
        if (createdLoan != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }



}
