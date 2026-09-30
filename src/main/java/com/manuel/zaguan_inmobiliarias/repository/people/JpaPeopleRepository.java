package com.manuel.zaguan_inmobiliarias.repository.people;

import com.manuel.zaguan_inmobiliarias.entity.people.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPeopleRepository extends JpaRepository<People, Long> {
}
