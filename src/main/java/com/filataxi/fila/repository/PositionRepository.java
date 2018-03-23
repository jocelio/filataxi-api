package com.filataxi.fila.repository;

import com.filataxi.fila.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PositionRepository extends JpaRepository<Position, Integer>{
	 List<Position> findAllByOrderByIndexAsc();

	@Query("delete from Position p where p.driver.id = ?")
	 void deleteByDriverId(Integer id);
}
