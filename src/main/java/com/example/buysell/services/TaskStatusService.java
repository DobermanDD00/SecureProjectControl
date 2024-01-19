package com.example.buysell.services;

import com.example.buysell.models.TaskPackage.Status;
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
        Status taskStatus = new Status(1, "Отправлена на выполнение");
        taskStatusRepository.save(taskStatus);
        taskStatus = new Status(2, "Возвращена для корректировки");
        taskStatusRepository.save(taskStatus);
        taskStatus = new Status(3, "В процессе выполнения");
        taskStatusRepository.save(taskStatus);
        taskStatus = new Status(4, "Отправлено на проверку");
        taskStatusRepository.save(taskStatus);
        taskStatus = new Status(5, "Завершена");
        taskStatusRepository.save(taskStatus);
        taskStatus = new Status(6, "Возвращена на доработку");
        taskStatusRepository.save(taskStatus);
        taskStatus = new Status(7, "Прочее");
        taskStatusRepository.save(taskStatus);
        log.info("INITIALIZE STATUSES");


    }

}
