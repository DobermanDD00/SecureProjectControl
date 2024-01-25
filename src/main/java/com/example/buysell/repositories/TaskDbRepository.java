package com.example.buysell.repositories;

import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskDbRepository extends JpaRepository<TaskDb, Long> {
    List<TaskDb> findByTitle(String title);

    TaskDb findTopByOrderByIdDesc();







}
