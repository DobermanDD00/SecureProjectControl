package com.example.buysell.services;

import com.example.buysell.models.TaskStatus;
import com.example.buysell.models.User;
import com.example.buysell.models.enums.Role;
import com.example.buysell.repositories.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    public void initialize(){
        TaskStatus taskStatus = new TaskStatus(1, "Отправлена на выполнение");
        taskStatusRepository.save(taskStatus);
        taskStatus = new TaskStatus(2, "Возвращена для корректировки");
        taskStatusRepository.save(taskStatus);
        taskStatus = new TaskStatus(3, "В процессе выполнения");
        taskStatusRepository.save(taskStatus);
        taskStatus = new TaskStatus(4, "Отправлено на проверку");
        taskStatusRepository.save(taskStatus);
        taskStatus = new TaskStatus(5, "Завершена");
        taskStatusRepository.save(taskStatus);
        taskStatus = new TaskStatus(6, "Возвращена на доработку");
        taskStatusRepository.save(taskStatus);
        taskStatus = new TaskStatus(7, "Прочее");
        taskStatusRepository.save(taskStatus);
        log.info("INITIALIZE STATUSES");


    }

}
