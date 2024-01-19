package com.example.buysell.models.TaskPackage;

import com.example.buysell.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "task_asses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asses {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @JoinColumn(name = "role_id")
    private Long role;
    @JoinColumn(name = "task_key")
    private byte[] taskKey;





}
