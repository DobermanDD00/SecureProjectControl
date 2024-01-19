package com.example.buysell.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepositoryDb extends JpaRepository<TaskDb, Long> {
    List<TaskDb> findByTitle(String title);



}
