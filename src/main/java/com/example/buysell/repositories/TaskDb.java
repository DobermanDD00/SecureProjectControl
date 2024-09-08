package com.example.buysell.repositories;

import com.example.buysell.models.Security.Security;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.TaskPackage.TaskActive;
import com.example.buysell.models.TaskPackage.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.persistence.*;
import java.time.LocalDateTime;

@Component
@Entity
@Table(name = "tasksDb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDb {
    public static final int CURRENT_SECURITY = 1;
    public static final int SECURITY_OFF = 0;
    public static final int SECURITY_STANDARD = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")

    private byte[] title;
    @Lob
    @Column(name = "description")
    private byte[] description;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private TaskStatus status;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "active_id")
    private TaskActive active;
    @Column(name = "dateOfCreated")

    private LocalDateTime dateOfCreated;
    @Column(name = "history")
    @Lob
    private byte[] history;


    @SneakyThrows
    public TaskDb(Task task, SecretKey key) {

        if (CURRENT_SECURITY == SECURITY_OFF){
            this.id = task.getId();
            this.title = task.getTitle().getBytes();
            this.description = task.getDescription().getBytes();
            this.status = task.getStatus();
            this.active = task.getActive();
            this.dateOfCreated = task.getDateOfCreated();
            this.history = task.getHistory().getBytes();

        }else {
            this.id = task.getId();
            this.title = Security.cipherAes(task.getTitle().getBytes(), key, Cipher.ENCRYPT_MODE);
            this.description = Security.cipherAes(task.getDescription().getBytes(), key, Cipher.ENCRYPT_MODE);
            this.status = task.getStatus();
            this.active = task.getActive();
            this.dateOfCreated = task.getDateOfCreated();
            this.history = Security.cipherAes(task.getHistory().getBytes(), key, Cipher.ENCRYPT_MODE);
        }
    }

    @SneakyThrows
    public Task toTask(SecretKey key) {
        Task task = new Task();
        if (CURRENT_SECURITY == SECURITY_OFF) {
            task.setId(this.id);
            task.setTitle(new String(this.title));
            task.setDescription(new String(this.description));
            task.setStatus(this.status);
            task.setActive(this.active);
            task.setDateOfCreated(this.dateOfCreated);
            task.setHistory(new String(this.history));

        }else {
            task.setId(this.id);
            task.setTitle(new String(Security.cipherAes(this.title, key, Cipher.DECRYPT_MODE)));
            task.setDescription(new String(Security.cipherAes(this.description, key, Cipher.DECRYPT_MODE)));
            task.setStatus(this.status);
            task.setActive(this.active);
            task.setDateOfCreated(this.dateOfCreated);
            task.setHistory(new String(Security.cipherAes(this.history, key, Cipher.DECRYPT_MODE)));
        }

        return task;

    }



}


