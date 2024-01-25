package com.example.buysell.models.TaskPackage;

import com.example.buysell.models.inputException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreationDto {
    private Task task = new Task();
    private List<TaskAccess> Accesses = new ArrayList<>();
    private String info = "";
    public void addAccess(TaskAccess taskAccess){
        Accesses.add(taskAccess);
    }

    public void removeEmptyAccesses() {
        Accesses.removeIf(n -> n.getUser() == null || n.getRole() == null);
    }
    public void removeDuplicatesAccess() {
        Accesses = Accesses.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
    }

    /**
     * Метод для проверки введенной пользователем информации.
     * Убрать пустые и полупустые и дублирующиеся accesses и проверить оставшиеся
     *
     * @return true если все оК, false если есть ошибки
     * Данные об ошибках хранятся в info
     */
    public boolean isCorrectInput() {
        StringBuilder stringBuilder = new StringBuilder();
        if (task == null)
            stringBuilder.append("Ошибка, task == null\n");

        if (Accesses == null) {
            stringBuilder.append("Ошибка, accesses == null\n");
        } else {
            Accesses.forEach(n -> {
                if (n == null) stringBuilder.append("Ошибка, элемент accesses == null\n");
            });
        }

        if (stringBuilder.length() > 0) {
            info = stringBuilder.toString();
            return false;
        }

        removeDuplicatesAccess();
        removeEmptyAccesses();
        try {
            task.checkTask(Task.NEW_TASK_MODE);
        } catch (inputException e) {
            stringBuilder.append(e.getMessage());
        }
        if (Accesses.size() == 0) {
            stringBuilder.append("Ошибка, поля доступов пустые или полупустые\n");
        } else {
            Accesses.forEach(n -> {
                try {
                    n.checkAccess(TaskAccess.NEW_ACCESS_MODE);
                } catch (inputException e) {
                    stringBuilder.append(e.getMessage());
                }
            });
        }




        info = stringBuilder.toString();
        return info.length() == 0;
    }
}
