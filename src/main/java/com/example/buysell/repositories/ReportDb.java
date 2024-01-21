package com.example.buysell.repositories;

import com.example.buysell.models.ReportPackage.Report;
import com.example.buysell.models.ReportPackage.ReportStatus;
import com.example.buysell.models.Security.Security;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.UserPackage.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportDb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDb {
    public static final int CURRENT_SECURITY = 1;
    public static final int SECURITY_OFF = 0;
    public static final int SECURITY_STANDARD = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private byte[] title;
    @Column(name = "description")

    private byte[] description;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private ReportStatus status;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "dateOfCreated")

    private LocalDateTime dateOfCreated;
    @Column(name = "history")

    private byte[] history;

    @SneakyThrows
    public ReportDb(Report report, SecretKey key) {

        if (CURRENT_SECURITY == SECURITY_OFF){
            this.id = report.getId();
            this.title = report.getTitle().getBytes();
            this.description = report.getDescription().getBytes();
            this.status = report.getStatus();
            this.user = report.getUser();
            this.dateOfCreated = report.getDateOfCreated();
            this.history = report.getHistory().getBytes();

        }else {
            this.id = report.getId();
            this.title = Security.cipherAes(report.getTitle().getBytes(), key, Cipher.ENCRYPT_MODE);
            this.description = Security.cipherAes(report.getDescription().getBytes(), key, Cipher.ENCRYPT_MODE);
            this.status = report.getStatus();
            this.user = report.getUser();
            this.dateOfCreated = report.getDateOfCreated();
            this.history = Security.cipherAes(report.getHistory().getBytes(), key, Cipher.ENCRYPT_MODE);
        }
    }
    @SneakyThrows
    public Report toReport(SecretKey key) {
        Report report = new Report();
        if (CURRENT_SECURITY == SECURITY_OFF) {
            report.setId(this.id);
            report.setTitle(new String(this.title));
            report.setDescription(new String(this.description));
            report.setStatus(this.status);
            report.setUser(this.user);
            report.setDateOfCreated(this.dateOfCreated);
            report.setHistory(new String(this.history));

        }else {
            report.setId(this.id);
            report.setTitle(new String(Security.cipherAes(this.title, key, Cipher.DECRYPT_MODE)));
            report.setDescription(new String(Security.cipherAes(this.description, key, Cipher.DECRYPT_MODE)));
            report.setStatus(this.status);
            report.setUser(this.user);
            report.setDateOfCreated(this.dateOfCreated);
            report.setHistory(new String(Security.cipherAes(this.history, key, Cipher.DECRYPT_MODE)));
        }

        return report;

    }





}
