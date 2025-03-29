package br.com.bancodigital.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Transferencia
{
    private int id;
    private int idContaOrigem;
    private int idContaDestino;
    private double valor;
    private LocalDateTime dataTransferencia;
}
