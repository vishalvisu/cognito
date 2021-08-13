package com.hackerearth.fullstack.backend.repository;

import com.hackerearth.fullstack.backend.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event,Long> {
	
}
