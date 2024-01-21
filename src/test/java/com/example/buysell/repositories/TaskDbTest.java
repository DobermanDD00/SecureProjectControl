package com.example.buysell.repositories;

import com.example.buysell.models.Security.Security;
import com.example.buysell.models.TaskPackage.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskDbTest {

    @Test
    void toTask() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Task task = new Task(12, "234", "sldjf", null, null, LocalDateTime.now(), "dls");
        SecretKey secretKey = Security.generatedAesKey();

        TaskDb taskDb = new TaskDb(task, secretKey);
        Task task1 = taskDb.toTask(secretKey);

        Assertions.assertTrue(task.equals(task1));


    }
}