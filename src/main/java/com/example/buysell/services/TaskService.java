package com.example.buysell.services;

import com.example.buysell.models.Image;
import com.example.buysell.models.Security.Security;
import com.example.buysell.models.TaskPackage.*;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.models.Exception.InputException;
import com.example.buysell.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepositoryCustom taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskRoleRepository taskRoleRepository;
    private final TaskAssesRepository taskAssesRepository;


    public void initialize() {

        if (taskStatusRepository.count() != 0) return;
        if (taskRoleRepository.count() != 0) return;

        List<TaskStatus> taskStatuses = new ArrayList<>();
        taskStatuses.add(new TaskStatus(1, "Отправлена на выполнение"));
        taskStatuses.add(new TaskStatus(2, "Возвращена для корректировки"));
        taskStatuses.add(new TaskStatus(3, "В процессе выполнения"));
        taskStatuses.add(new TaskStatus(4, "Отправлено на проверку"));
        taskStatuses.add(new TaskStatus(5, "Завершена"));
        taskStatuses.add(new TaskStatus(6, "Возвращена на доработку"));
        taskStatuses.add(new TaskStatus(7, "Прочее"));
        taskStatusRepository.saveAll(taskStatuses);
        log.info("INITIALIZE STATUSES TASK");

        List<TaskUserRole> taskUserRoles = new ArrayList<>();
        taskUserRoles.add(new TaskUserRole(1, "Руководитель"));
        taskUserRoles.add(new TaskUserRole(2, "Исполнитель"));
        taskUserRoles.add(new TaskUserRole(3, "Наблюдатель"));
        taskRoleRepository.saveAll(taskUserRoles);
        log.info("INITIALIZE ROLES TASK");


    }

    /**
     * Создание новой задачи в ДБ (taskDTO)
     */
    public void createTaskDto(TaskDto taskDto, User user) throws InputException {
        if (!taskDto.isCorrectInput()) {
            throw new InputException(taskDto.getInfo());
        }


        final SecretKey taskKey;
        try {
            taskKey = Security.generatedAesKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Task task = taskDto.getTask();
        task.setDateOfCreated(LocalDateTime.now());
        task.setHistory("Задача создана пользователем: " + user.getName() + "\n");
        task.addToHistory("Время создания " + task.getDateOfCreated().toString() + "\n");


        long idTask = saveTaskAndAccesses(task, taskDto.getAccesses(), taskKey);
        log.info("Сохранение задачи и доступов. idTask: {}", idTask);

    }

    /**
     * Обновить задачу в ДБ (taskDto)
     * @throws InputException
     */
    public void updateTaskDto(TaskDto newTaskDto, User user, PrivateKey privateKey) throws InputException {
        if (!newTaskDto.isCorrectInput()) {
            throw new InputException(newTaskDto.getInfo());
        }
        long idTask = newTaskDto.getTask().getId();
        TaskDto oldTaskDto = getTaskDTOById(idTask, user, privateKey);

        TaskAccess taskAccess = oldTaskDto.getAccesses().stream().filter(n-> n.getUser().getId() == user.getId()).findFirst().orElse(null);
        final SecretKey taskKey;
        try {
            taskKey = Security.decodedKeyAes(Security.cipherRSADecrypt(taskAccess.getTaskKey(), privateKey));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

        Task oldTask = oldTaskDto.getTask();
        Task newTask = newTaskDto.getTask();
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
        oldTask.addToHistory("Доступ обновлен:\n");
        newTaskDto.getAccesses().forEach(n->{
            oldTask.addToHistory(n.getRole().getName()+": "+n.getUser().getName()+"\n");
        });

        deleteAllAccessesByTaskId(idTask);
        saveTaskAndAccesses(oldTask, newTaskDto.getAccesses(), taskKey);
        log.info("Обновление задачи и доступов. idTask: {}", idTask);


    }



    public TaskDto getTaskDTOById(long idTask, User user, PrivateKey privateKey){
        List<TaskAccess> accesses = taskAssesRepository.findByTaskDbId(idTask);
        Task task = taskRepository.getTaskById(idTask, getSecretKeyToTask(taskAssesRepository.findFirstByTaskDbIdAndUserId(idTask, user.getId()), user, privateKey));
        return new TaskDto(task, accesses, "");
    }



    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }


    public List<Task> listTasksToUser(User user, PrivateKey privKey) {
        if (user == null) return null;
        if (user.getId() == 0) return null;

        List<TaskAccess> taskAccesses = taskAssesRepository.findByUserId(user.getId());
        Set<Task> tasksSet = new HashSet<>();
        taskAccesses.forEach(n -> {
            TaskDb taskDb = n.getTaskDb();
            tasksSet.add(taskDb.toTask(getSecretKeyToTask(n, user, privKey)));
        });

        return new ArrayList<>(tasksSet);
    }

    /**
     * Функция получения списка всех возможных статусов для задач
     */
    public List<TaskStatus> listAllStatusesTasks() {
        return taskStatusRepository.findAll();
    }

    /**
     * Функция получения списка всех возможных ролей (должностей) для пользователей в задачах
     */
    public List<TaskUserRole> listAllUserRolesToTask() {
        return taskRoleRepository.findAll();
    }

    public void deleteAllAccessesByTaskId(long idTask){
        taskAssesRepository.deleteAllByTaskDbId(idTask);
    }



    /**
     * Сохранение задачи и доступов к ней с проставлением доступов к ней, без проверок входных значений
     * @return idTaskDb
     */
    private long saveTaskAndAccesses(Task task, List<TaskAccess> accesses,  SecretKey taskKey){
        TaskDb taskDb = taskRepository.save(task, taskKey);

        accesses.forEach(n -> {
            n.setTaskDb(taskDb);
            try {
                n.setTaskKey(Security.cipherRSAEncrypt(Security.encodedAnyKey(taskKey), n.getUser().getPubKey()));
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                     IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException("Ошибка в шифровании ключа задачи");
            }
        });
        taskAssesRepository.saveAll(accesses);
        return  taskDb.getId();

    }

    /**
     * Получение секретного ключа к задаче
     */
    private SecretKey getSecretKeyToTask(TaskAccess taskAccess, User user, PrivateKey privKey) {
        if (taskAccess == null) return null;
        try {
            return Security.decodedKeyAes(Security.cipherRSADecrypt(taskAccess.getTaskKey(), privKey));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException("Ошибка при дешифровании ключа задачи");
        }

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
}
