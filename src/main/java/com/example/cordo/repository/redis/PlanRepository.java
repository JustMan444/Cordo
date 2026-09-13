package com.example.cordo.repository.redis;

import com.example.cordo.entity.Subscribe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends CrudRepository<Subscribe, String> {
}
