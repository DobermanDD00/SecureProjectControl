package com.example.buysell.models.TaskPackage;

import com.example.buysell.models.UserPackage.User;
import com.example.buysell.models.Exception.InputException;
import com.example.buysell.repositories.TaskDb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "task_asses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAccess {
    public static final int STANDARD_MODE = 0;
    public static final int NEW_ACCESS_MODE = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String str;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)//TODO 555 проверить удаление данных записай при удалении исходной задачи
    @JoinColumn(name = "task_id")
    private TaskDb taskDb;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private TaskUserRole role;
    @JoinColumn(name = "task_key")
    @Lob
    private byte[] taskKey;


    public boolean isCorrect(){
        return taskDb != null && user != null && role != null;

    }

    /**
     * Метод для проверки доступа, в случае ошибок порождает исключение с сообщением, где именно ошибки
     * @param mode STANDARD_MODE = 0 - стандартная полная проверка (id !=0 и taskKey != null),
     *             NEW_ACCESS_MODE = 1 - проверка для нового доступа (id мб == 0 и taskKey мб == null)
     */
    public void checkAccess(int mode) throws InputException {
        StringBuilder errorReport = new StringBuilder();

        if (mode == STANDARD_MODE && id == 0)
            errorReport.append("Ошибка, id доступа не может быть равно '0'\n");
        if (mode == STANDARD_MODE && taskDb == null)
            errorReport.append("Ошибка, задача не может пустой\n");
        if (user == null)
            errorReport.append("Ошибка, пользователь не может быть пустым\n");
        if (role == null)
            errorReport.append("Ошибка, роль не может быть пустой\n");
        if (mode == STANDARD_MODE && taskKey == null){
            errorReport.append("Ошибка, ключ задачи не может быть пустым");
        }

        if (errorReport.length() > 0){
            throw new InputException("Ошибка в доступе с id = "+id+"\n"+errorReport.toString());
        }


    }




}
