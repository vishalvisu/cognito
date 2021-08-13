package com.hackerearth.fullstack.backend.repository;

import com.hackerearth.fullstack.backend.model.Repo;
import org.springframework.data.repository.CrudRepository;

public interface RepoRepository extends CrudRepository<Repo,Long> {

}
