package eu.pickride.model;

public class GenerateMonthlyPDFsDTO {
    private final int year;
    private final int month;

    public GenerateMonthlyPDFsDTO(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public void validate() {
        if (year == 0) throw new IllegalArgumentException("year is required");
        if (month == 0) throw new IllegalArgumentException("month is required");
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
}

