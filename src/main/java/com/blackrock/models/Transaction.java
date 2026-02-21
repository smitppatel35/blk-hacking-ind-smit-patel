package com.blackrock.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Transaction {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private double amount;
    private double ceiling;
    private double remanent;

    // Constructed after /parse endpoint processing
    public Transaction(LocalDateTime date, double amount, double ceiling, double remanent) {
        this.date = date;
        this.amount = amount;
        this.ceiling = ceiling;
        this.remanent = remanent;
    }
}