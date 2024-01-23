package com.example.buysell.services;

import com.example.buysell.models.Image;
import com.example.buysell.models.Security.Security;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.TaskPackage.TaskAccess;
import com.example.buysell.models.TaskPackage.TaskStatus;
import com.example.buysell.models.TaskPackage.TaskUserRole;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

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


//        try {
//            Files.readAllBytes(Path.of("path"));
//        } catch (IOException e) {
//            log.error("Ошибка 1 ");
//            e.printStackTrace();//TODO Спросить Олега
//        }


    }

    @SneakyThrows
    public boolean createTaskAndAccesses(Task task, List<TaskAccess> accesses, User user) {
        if (task == null || accesses == null || user == null) return false;

        initializeTask(task, user);
        return saveTaskAndAccesses(task, accesses, user);

    }


        public void updateTaskAndAccesses(Task newTask, List<TaskAccess> newAccesses, User user, Object privKey) {
        newTask = updateTask(newTask, newAccesses, user, privKey);

        taskAssesRepository.deleteByTaskDbId(newTask.getId());
        saveTaskAndAccesses(newTask, newAccesses, user);

    }

    private Task updateTask(Task newTask, List<TaskAccess> accesses, User user, Object privKey) {
        SecretKey taskKey = getSecretKeyToTask(newTask.getId(), user, privKey);
        Task oldTask = taskRepository.getTaskById(newTask.getId(), taskKey);
        if (!newTask.getTitle().equals(oldTask.getTitle())) {
            oldTask.addToHistory("Изменение названия с " + oldTask.getTitle() + " на " + newTask.getTitle() + "\n");
            oldTask.setTitle(newTask.getTitle());
        }
        if (!newTask.getDescription().equals(oldTask.getDescription())) {
            oldTask.addToHistory("Изменение описания с " + oldTask.getDescription() + " на " + newTask.getDescription() + "\n");
            oldTask.setDescription(newTask.getDescription());
        }

        if (!newTask.getStatus().equals(oldTask.getStatus())) {
            oldTask.addToHistory("Изменение статуса на " + newTask.getStatus().getTitle() + "\n");
            oldTask.setStatus(newTask.getStatus());
        }

        return oldTask;
    }


    @SneakyThrows
    private boolean saveTaskAndAccesses(Task task, List<TaskAccess> accesses, User user) {
        SecretKey taskKey = Security.generatedAesKey();
        long id = taskRepository.save(task, taskKey);
        TaskDb taskDb = taskRepository.findTaskDbById(id);

        accesses.forEach(n -> {
            //TODO 000 Взять из дб список пользователей, по accesses и проставить в accesses зашифрованные ключи задач
            n.setTaskKey(Security.encodedAnyKey(taskKey));
            n.setTaskDb(taskDb);
        });
        saveFilteredAccesses(accesses);
        return false;//************
    }

    private Task initializeTask(Task task, User user) {
        task.setDateOfCreated(LocalDateTime.now());
        task.setHistory("Задача создана пользователем: " + user.getName() + "\n");
        task.addToHistory("Время создания " + task.getDateOfCreated().toString() + "\n");

        return task;
    }

    private SecretKey getSecretKeyToTask(long idTask, User user, Object privKey) {
        List<TaskAccess> taskAccesses = taskAssesRepository.findByTaskDbIdAndUserId(idTask, user.getId());
        if (taskAccesses == null) return null;
        //TODO ************8999 На будущее беру из списка только 1 значение
        // Переделать и из доступа дешифровать ключ задачи
        return Security.decodedKeyAes(taskAccesses.get(0).getTaskKey());

    }


    public void saveFilteredAccesses(List<TaskAccess> accesses) {
        accesses.removeIf(n -> (!n.isCorrect()));
        taskAssesRepository.saveAll(accesses);
    }

    public List<TaskAccess> getAccessesToTaskById(long taskId) {
        return taskAssesRepository.findByTaskDbId(taskId);
    }


    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @SneakyThrows
    public Task getTask(TaskDb taskDb, User user, Object privKey) {
        return taskDb.toTask(getSecretKeyToTask(taskDb.getId(), user, privKey));
    }


    @SneakyThrows
    public Task getTaskById(long idTask, User user, Object privKey) {
        List<TaskAccess> taskAccesses = taskAssesRepository.findByTaskDbIdAndUserId(idTask, user.getId());

        Task task = taskRepository.getTaskById(idTask, getSecretKeyToTask(idTask, user, privKey));

        return task;
    }

    /**
     * Функция находит задачи по названию или возвращает все задачи
     */
    //TODO 444 Сломано, починить когда понадобиться
    public List<Task> tasksByTitleOrAll(String title) {
        if (title != null) return taskRepository.findByTitle(title);
        return null;
//        return taskRepository.findAll();
    }

    public List<Task> tasksToUser(User user, Object privKey) {
        if (user == null) return null;
        if(user.getId() == null) return null;

        List<TaskAccess> taskAccesses = taskAssesRepository.findByUserId(user.getId());
        Set<Task> tasksSet = new HashSet<>();
        taskAccesses.forEach(n -> {
            TaskDb taskDb = n.getTaskDb();
            tasksSet.add(taskDb.toTask(getSecretKeyToTask(taskDb.getId(), user, privKey)));
        });


        return new ArrayList<>(tasksSet);
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
//        taskRepository.save(task);

    }
}
