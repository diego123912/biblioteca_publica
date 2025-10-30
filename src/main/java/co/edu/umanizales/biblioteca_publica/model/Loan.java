package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private String id;
    private String userId;
    private String bookId;
    private LocalDate loanDate;
    private LocalDate estimatedReturnDate;
    private LocalDate actualReturnDate;
    private LoanStatus status;
    private String observations;

    public Loan(String id, String userId, String bookId, LocalDate loanDate, LocalDate estimatedReturnDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.estimatedReturnDate = estimatedReturnDate;
        this.status = LoanStatus.ACTIVE;
        this.observations = "";
    }

    public boolean isOverdue() {
        return status == LoanStatus.ACTIVE && LocalDate.now().isAfter(estimatedReturnDate);
    }

    public void returnBook() {
        this.actualReturnDate = LocalDate.now();
        this.status = LoanStatus.COMPLETED;
    }

    public void markOverdue() {
        if (isOverdue()) {
            this.status = LoanStatus.OVERDUE;
        }
    }

    public long getDelayDays() {
        if (actualReturnDate != null) {
            return estimatedReturnDate.until(actualReturnDate).getDays();
        } else if (isOverdue()) {
            return estimatedReturnDate.until(LocalDate.now()).getDays();
        }
        return 0;
    }
}
