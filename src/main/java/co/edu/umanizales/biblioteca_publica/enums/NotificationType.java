package co.edu.umanizales.biblioteca_publica.enums;

public enum NotificationType {
    LOAN("Loan"),
    RETURN("Return"),
    OVERDUE("Overdue"),
    GENERAL("General");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
