package com.example.buysell.models.TaskPackage;

import com.example.buysell.models.Exception.InputException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    public static final int STANDARD_MODE = 0;
    public static final int NEW_TASK_MODE = 1;

    private long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskActive active;
    private LocalDateTime dateOfCreated;
    private String history;


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
    public void addToHistory(String str) {
        this.history = history + str;
    }

//    public void addImageToTask(Image image) {
//        image.setTask(this);
//        images.add(image);
//    }

    /**
     * Метод для проверки задачи, в случае ошибок порождает исключение с сообщением, где именно ошибки
     *
     * @param mode STANDARD_MODE = 0 - стандартная полная проверка (id !=0),
     *             NEW_TASK_MODE = 1 - проверка для новой задачи (id мб == 0)
     */
    public void checkTask(int mode) throws InputException {
        StringBuilder errorReport = new StringBuilder();

        if (mode == STANDARD_MODE && id == 0)
            errorReport.append("Ошибка, id задачи не может быть равно '0'\n");

        if (title == null || title.length() == 0) {
            errorReport.append("Ошибка, название задачи не может быть пустым\n");
            //Потом дописать проверки, что название не состоит из одних пробелов
        }
        if (description == null || description.length() == 0) {
            errorReport.append("Ошибка, описание задачи не может быть пустым\n");
            //Потом дописать проверки, что описание не состоит из одних пробелов
        }
        if (status == null)
            errorReport.append("Ошибка, статус задачи не может быть пустым\n");
        if (mode == STANDARD_MODE && active == null)
            errorReport.append("Ошибка, активность задачи не может быть пустой\n");
        if (mode == STANDARD_MODE && dateOfCreated == null)
            errorReport.append("Ошибка, дата создания не может быть пустой\n");
        if (mode == STANDARD_MODE && dateOfCreated.isAfter(LocalDateTime.now()))
            errorReport.append("Ошибка, дата создания не может быть из будущего\n");
        if (mode == STANDARD_MODE && history.length() == 0)
            errorReport.append("Ошибка, история не может быть пустой\n");

        if (errorReport.length() > 0) {
//            throw new imputException("Ошибка в задаче с id = "+id+" Название "+title+"\n"+errorReport.toString());
            throw new InputException(errorReport.toString());
        }
    }



}
