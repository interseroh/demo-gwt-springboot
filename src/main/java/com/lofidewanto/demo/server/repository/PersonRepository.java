package com.lofidewanto.demo.server.repository;

import org.springframework.data.repository.CrudRepository;

import com.lofidewanto.demo.server.domain.PersonImpl;

public interface PersonRepository extends CrudRepository<PersonImpl, Long> {

}
