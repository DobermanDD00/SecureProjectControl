package com.example.buysell.models.TaskPackage;

import com.example.buysell.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
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
    private LocalDateTime dateOfCreated = LocalDateTime.now();
    @Column(name = "history", columnDefinition = "text")
    private String history = "Задача создана, время создания " + dateOfCreated.toString()+"\n";
    //____________________
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
//            mappedBy = "task")
//    private List<Image> images = new ArrayList<>();
//    private Long previewImageId;
//________________________


//    @PrePersist
//    private void init() {
//        dateOfCreated = LocalDateTime.now();
//        history = "Задача создана, время создания " + dateOfCreated.toString()+"\n";
//    }
    public void addToHistory(String str){
        this.history = history + str;
    }

//    public void addImageToTask(Image image) {
//        image.setTask(this);
//        images.add(image);
//    }
}
