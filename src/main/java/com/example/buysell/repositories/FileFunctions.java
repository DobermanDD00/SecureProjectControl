package com.example.buysell.repositories;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileFunctions {
    public static void main(String[] args) {
        writeFile("dtyjedryjedx".getBytes(StandardCharsets.UTF_8), "AAAAAAAAAAAAAAAAAa.txt");
        System.out.println(new String(readFile("AAAAAAAAAAAAAAAAAa.txt"), StandardCharsets.UTF_8));
    }

    public static void writeFile(byte[] data, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);

            fos.write(data, 0, data.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static byte[] readFile(String path) {// Ниписать проверки для отсутствующих файлов
        try {
        Path path1 = Path.of(path);
            return Files.readAllBytes(path1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
