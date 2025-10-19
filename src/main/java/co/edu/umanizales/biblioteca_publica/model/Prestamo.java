package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    private String id;
    private String usuarioId;
    private String libroId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEstimada;
    private LocalDate fechaDevolucionReal;
    private LoanStatus estado;
    private String observaciones;

    public Prestamo(String id, String usuarioId, String libroId, LocalDate fechaPrestamo, LocalDate fechaDevolucionEstimada) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEstimada = fechaDevolucionEstimada;
        this.estado = LoanStatus.ACTIVO;
        this.observaciones = "";
    }

    public boolean estaVencido() {
        return estado == LoanStatus.ACTIVO && LocalDate.now().isAfter(fechaDevolucionEstimada);
    }

    public void devolver() {
        this.fechaDevolucionReal = LocalDate.now();
        this.estado = LoanStatus.FINALIZADO;
    }

    public void marcarVencido() {
        if (estaVencido()) {
            this.estado = LoanStatus.VENCIDO;
        }
    }

    public long getDiasRetraso() {
        if (fechaDevolucionReal != null) {
            return fechaDevolucionEstimada.until(fechaDevolucionReal).getDays();
        } else if (estaVencido()) {
            return fechaDevolucionEstimada.until(LocalDate.now()).getDays();
        }
        return 0;
    }
}
