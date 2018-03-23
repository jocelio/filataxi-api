package com.filataxi.fila.repository;

import com.filataxi.fila.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer>{
	List<Driver> findByEnabledTrue();

}
