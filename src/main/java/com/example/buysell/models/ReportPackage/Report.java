package com.example.buysell.models.ReportPackage;

import com.example.buysell.models.UserPackage.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private long id;
    private String title;
    private String description;
    private ReportStatus status;
    private User user;
    private LocalDateTime dateOfCreated;
    private String history;

    public void addToHistory(String str) {
        this.history = history + str;
    }


}
