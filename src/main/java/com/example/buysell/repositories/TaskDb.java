package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.Active;
import com.example.buysell.models.TaskPackage.Status;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    private Status status;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "active_id")
    private Active active;
    @Column(name = "history", columnDefinition = "text")
    private byte[] history;
    private LocalDateTime dateOfCreated;




    public TaskDb(Task task, User user) {// TODO шифрование
        this.id = task.getId();
        this.title = task.getTitle().getBytes();
        this.description = task.getDescription().getBytes();
        this.lead = task.getLead();
        this.performer = task.getPerformer();
        this.status = task.getStatus();
        this.active = task.getActive();

        this.history = task.getHistory().getBytes();
    }

    public Task toTask(User user) {// TODO дешифрование
        Task task = new Task();
        task.setId(this.id);
        task.setTitle(new String(this.title));
        task.setDescription(new String(this.description));
        task.setLead(this.lead);
        task.setPerformer(this.performer);
        task.setStatus(this.status);
        task.setActive(this.active);
        task.setHistory(new String(this.history));
        return task;

    }

    public static List<TaskDb> toTaskDb(List<Task> tasks, User user) {
        if (tasks == null) return null;
        List<TaskDb> tasksDb = new ArrayList<>();
        for (Task task : tasks) {
            tasksDb.add(new TaskDb(task, user));
        }
        return tasksDb;

    }

    public static List<Task> toTask(List<TaskDb> tasksDb, User user){
        if (tasksDb == null) return null;
        List<Task> tasks = new ArrayList<>();
        for(TaskDb taskDb: tasksDb){
            tasks.add(taskDb.toTask(user));
        }
        return tasks;

    }

}

