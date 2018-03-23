package com.filataxi.fila.repository;

import com.filataxi.fila.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface PositionRepository extends JpaRepository<Position, Integer>{
	 List<Position> findAllByOrderByIndexAsc();

	@Transactional
	@Modifying
	@Query("delete from Position p where p.driver.id = ?")
	 void deleteByDriverId(Integer id);
}
