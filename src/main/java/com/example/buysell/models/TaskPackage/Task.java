package com.example.buysell.models.TaskPackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private long id;

    private String title;
    private String description;
    private TaskStatus status;
    private TaskActive active;
    private LocalDateTime dateOfCreated = LocalDateTime.now();
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
