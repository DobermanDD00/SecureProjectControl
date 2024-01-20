package com.example.buysell.models.TaskPackage;

import com.example.buysell.models.User;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String str;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)//TODO проверить удаление данных записай при удалении исходной задачи
    @JoinColumn(name = "task_id")
    private TaskDb taskDb;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private TaskUserRole role;
    @JoinColumn(name = "task_key")
    private byte[] taskKey;


    public boolean isEmpty(){
        if (id == 0 && str == "" && user == null && role == null && taskKey == null)
            return true;
        return false;
    }




}
