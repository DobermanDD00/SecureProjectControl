package com.example.buysell.repositories;

import com.example.buysell.models.TaskActive;
import com.example.buysell.models.TaskStatus;
import com.example.buysell.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "tasksDb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDb {
//    public static final int CURRENT_SECURITY = 0;
//    public static final int SECURITY_OFF = 0;
//    public static final int SECURITY_STANDARD = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private byte[] title;
    @Column(name = "description", columnDefinition = "text")
    private byte[] description;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "lead_id")
    private User lead;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "performer_id")
    private User performer;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private TaskStatus status;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "active_id")
    private TaskActive active;
    @Column(name = "history", columnDefinition = "text")
    private byte[] history;
    private LocalDateTime dateOfCreated;

//    public TaskDb(Task task){
//        this.id = task.getId();
//        this.title = task.getTitle().getBytes();
//        this.description = task.getDescription().getBytes();
//        this.lead = task.getLead();
//        this.performer = task.getPerformer();
//        this.status = task.getStatus();
//        this.active = task.getActive();
//        this.history = task.getHistory().getBytes();
//    }

}

