package com.example.buysell.models.TaskPackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAccessCreationDto {
    private List<TaskAccess> Accesses = new ArrayList<>();

    public void addAccess(TaskAccess taskAccess){
        this.Accesses.add(taskAccess);
    }

}
