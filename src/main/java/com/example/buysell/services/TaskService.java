package com.example.buysell.services;

import com.example.buysell.models.Image;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.TaskPackage.Status;
import com.example.buysell.models.User;
import com.example.buysell.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
//    private final TaskRepository taskRepository;
    private final TaskRepositoryCustom taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;

    public List<Task> listTasks(String title) {
        if (title != null) return taskRepository.findByTitle(title);
        return taskRepository.findAll();
    }


    public List<Status> listStatuses() {
        return taskStatusRepository.findAll();
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }


    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void saveTask(Principal principal, Task task, MultipartFile file1) throws IOException {
//        task.setLead(getUserByPrincipal(principal));
        Image image1;

        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
//            task.addImageToTask(image1);
        }

        log.info("Saving new Task. Title: {}; Author email: {}", task.getTitle(), task.getLead().getId());
        Task taskFromDb = taskRepository.save(task);

    }

    public void taskUpdate(Task newTask, Task oldTask) {
        if (!newTask.getTitle().equals(oldTask.getTitle())){
            oldTask.addToHistory("Изменение названия с " +oldTask.getTitle()+" на " + newTask.getTitle()+"\n");
            oldTask.setTitle(newTask.getTitle());
        }
        if (!newTask.getDescription().equals(oldTask.getDescription())){
            oldTask.addToHistory("Изменение описания с " +oldTask.getDescription()+" на " + newTask.getDescription()+"\n");
            oldTask.setDescription(newTask.getDescription());
        }
        if (!newTask.getLead().equals(oldTask.getLead())){
            oldTask.addToHistory("Изменение руководителя с " +oldTask.getLead().getName()+" на " + newTask.getLead().getName()+"\n");
            oldTask.setLead(newTask.getLead());
        }
        if (!newTask.getPerformer().equals(oldTask.getPerformer())){
            oldTask.addToHistory("Изменение исполнителя с " +oldTask.getLead().getName()+" на " + newTask.getPerformer().getName()+"\n");
            oldTask.setPerformer(newTask.getPerformer());
        }
        if (!newTask.getStatus().equals(oldTask.getStatus())){
            oldTask.addToHistory("Изменение статуса с " +oldTask.getStatus().getTitle()+" на " + newTask.getStatus().getTitle()+"\n");
            oldTask.setStatus(newTask.getStatus());
        }


        taskRepository.save(oldTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id);
//        return taskRepository.findById(id).orElse(null);
    }
}
