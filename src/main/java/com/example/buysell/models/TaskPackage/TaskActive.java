package com.example.buysell.models.TaskPackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "task_actives")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskActive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
}
