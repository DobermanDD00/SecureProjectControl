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
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @SneakyThrows
    public void createTaskAndAccesses(Task task, List<TaskAccess> accesses, User user) {
        //TODO Спросить Олега где фильтровать accesses
        initializeTask(task, user);
        saveTaskAndAccesses(task, accesses, user);
    }


    public void updateTaskAndAccesses(Task newTask, List<TaskAccess> newAccesses, User user) {
        newTask = updateTask(newTask, newAccesses, user);

        taskAssesRepository.deleteByTaskDbId(newTask.getId());
        //TODO Совет Олег, сравнение и обновление доступов как это делать нормально,
        // сейчас удаляются старые и записываются новые (если ничего не менялось, то все перезаписывается на тоже самое)

        saveTaskAndAccesses(newTask, newAccesses, user);

    }

    private Task updateTask(Task newTask, List<TaskAccess> accesses, User user) {
        SecretKey taskKey = getSecretKeyToTask(newTask.getId(), user);
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
            oldTask.addToHistory("Изменение статуса с " + oldTask.getStatus().getTitle() + " на " + newTask.getStatus().getTitle() + "\n");
            oldTask.setStatus(newTask.getStatus());
        }
        //TODO Спросить Олега как лучше добавлять в историю изменения доступов

        return oldTask;
    }


    @SneakyThrows
    private void saveTaskAndAccesses(Task task, List<TaskAccess> accesses, User user) {
        //TODO Спросить Олега где фильтровать accesses
        SecretKey taskKey = Security.generatedAesKey();
        long id = taskRepository.save(task, taskKey);
        TaskDb taskDb = taskRepository.findTaskDbById(id);

        accesses.forEach(n -> {
            //TODO 333 Когда у пользователей появиться pub and pri key:
            // Сохранять зашифрованный ключ задачи, сейчас в отк виде, публ ключ брать из ДБ
            n.setTaskKey(Security.encodedAnyKey(taskKey));
            n.setTaskDb(taskDb);
        });
        saveFilteredAccesses(accesses);
    }

    private Task initializeTask(Task task, User user) {
        task.setDateOfCreated(LocalDateTime.now());
        task.setHistory("Задача создана пользователем: " + user.getName() + "\n");
        task.addToHistory("Время создания " + task.getDateOfCreated().toString() + "\n");

        return task;
    }

    private SecretKey getSecretKeyToTask(long idTask, User user) {
        List<TaskAccess> taskAccesses = taskAssesRepository.findByTaskDbIdAndUserId(idTask, user.getId());
        if (taskAccesses == null) return null;
        //TODO 999 На будущее беру из списка только 1 значение
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
    public Task getTask(TaskDb taskDb, User user) {
        return taskDb.toTask(getSecretKeyToTask(taskDb.getId(), user));
    }

    public List<Task> getTasks(List<TaskDb> tasksDb, User user) {
        List<Task> tasks = new ArrayList<>();
        tasksDb.forEach(n -> {
            tasks.add(getTask(n, user));
        });
        return tasks;
    }

    @SneakyThrows
    public Task getTaskById(long idTask, User user) {
        //TODO Спросить Олега про поиск по примеру
        List<TaskAccess> taskAccesses = taskAssesRepository.findByTaskDbIdAndUserId(idTask, user.getId());

        Task task = taskRepository.getTaskById(idTask, getSecretKeyToTask(idTask, user));

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

    public List<Task> tasksToUser(User user) {
        if (user == null) return null;
        if(user.getId() == null) return null;//TODO Почему?

        List<TaskAccess> taskAccesses = taskAssesRepository.findByUserId(user.getId());
        List<Task> tasks = new ArrayList<>();
        taskAccesses.forEach(n -> tasks.add(getTask(n.getTaskDb(), user)));


        return tasks;
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
