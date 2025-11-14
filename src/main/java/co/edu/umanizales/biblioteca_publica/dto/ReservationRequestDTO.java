package co.edu.umanizales.biblioteca_publica.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {
    private String userId;
    private String bookId;
}
