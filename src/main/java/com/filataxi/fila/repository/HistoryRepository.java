package com.filataxi.fila.repository;

import com.filataxi.fila.model.HistoryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface HistoryRepository extends JpaRepository<HistoryData, Integer> {

}
