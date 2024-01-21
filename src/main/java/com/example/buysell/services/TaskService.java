package com.example.buysell.services;

import com.example.buysell.models.Image;
import com.example.buysell.models.TaskPackage.TaskAccess;
import com.example.buysell.models.TaskPackage.TaskUserRole;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.TaskPackage.TaskStatus;
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
    private final UserRepository userRepository;
    private final TaskRepositoryCustom taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskRoleRepository taskRoleRepository;
    private final TaskAssesRepository taskAssesRepository;


    public void initialize() {
        TaskStatus taskStatus;
        taskStatus = new TaskStatus(1, "Отправлена на выполнение");
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
        log.info("INITIALIZE STATUSES TASK");

        TaskUserRole taskRole;
        taskRole = new TaskUserRole(1, "Руководитель");
        taskRoleRepository.save(taskRole);
        taskRole = new TaskUserRole(2, "Исполнитель");
        taskRoleRepository.save(taskRole);
        taskRole = new TaskUserRole(3, "Наблюдатель");
        taskRoleRepository.save(taskRole);
        log.info("INITIALIZE ROLES TASK");


    }

    public void saveTaskAndAccesses(Task task, List<TaskAccess> accesses) {
        long id = taskRepository.save(task);
        TaskDb taskDb = taskRepository.findTaskDbById(id);
        for (TaskAccess access : accesses) {
            access.setTaskDb(taskDb);
        }
        saveFilteredAccesses(accesses);
    }

    public void updateTaskAndAccesses(Task newTask, List<TaskAccess> newAccesses) {
        updateTask(newTask);

        List<TaskAccess> oldAccesses = taskAssesRepository.findByTaskDbId(newTask.getId());
        taskAssesRepository.deleteByTaskDbId(newTask.getId());
        //TODO Совет Олег, сравнение и обновление доступов как это делать нормально,
        // сейчас удаляются старые и записываются новые (если ничего не менялось, то все перезаписывается на тоже самое)

        newAccesses.forEach(s -> s.setTaskDb(taskRepository.findTaskDbById(newTask.getId())));
        saveFilteredAccesses(newAccesses);

    }

    public void updateTask(Task newTask) {
        Task oldTask = taskRepository.findTaskById(newTask.getId());
        if (!newTask.getTitle().equals(oldTask.getTitle())) {
            oldTask.addToHistory("Изменение названия с " + oldTask.getTitle() + " на " + newTask.getTitle() + "\n");
            oldTask.setTitle(newTask.getTitle());
        }
        if (!newTask.getDescription().equals(oldTask.getDescription())) {
            oldTask.addToHistory("Изменение описания с " + oldTask.getDescription() + " на " + newTask.getDescription() + "\n");
            oldTask.setDescription(newTask.getDescription());
        }

        if (!newTask.getStatus().equals(oldTask.getStatus())) {
            oldTask.addToHistory("Изменение статуса с " + oldTask.getStatus().getTitle() + " на " + newTask.getStatus().getTitle() + "\n");
            oldTask.setStatus(newTask.getStatus());
        }
        taskRepository.save(oldTask);
    }


    public void saveFilteredAccesses(List<TaskAccess> accesses) {
        accesses.removeIf(n -> (!n.isCorrect()));
        accesses.forEach(n->taskAssesRepository.save(n));
    }

    public List<TaskAccess> getAccessesToTaskById(long taskId) {
        return taskAssesRepository.findByTaskDbId(taskId);
    }


    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findTaskById(id);

    }

    /**
     * Функция находит задачи по названию или возвращает все задачи
     */
    public List<Task> tasksByTitleOrAll(String title) {
        if (title != null) return taskRepository.findByTitle(title);
        return taskRepository.findAll();
    }

    /**
     * Функция получения списка всех возможных статусов для задач
     */
    public List<TaskStatus> listStatusesTasks() {
        return taskStatusRepository.findAll();
    }

    /**
     * Функция получения списка всех возможных ролей (должностей) для пользователей в задачах
     */
    public List<TaskUserRole> listUserRolesToTask() {
        return taskRoleRepository.findAll();
    }
    //__________________________________________________________________________

    //TODO 11 Переместить в UserService
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    //TODO 22 Проверить, ф-ия для изображения
    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }


    //TODO 22 Обновить, Principal связано с пользователем, разобраться
    public void saveTask(Principal principal, Task task, MultipartFile file1) throws IOException {
//        task.setLead(getUserByPrincipal(principal));
        Image image1;

        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
//            task.addImageToTask(image1);
        }

        log.info("Saving new Task. Title: {}; ", task.getTitle());
        taskRepository.save(task);

    }
}
