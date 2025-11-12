package com.alexandersaul.Alexander.mapper;

import com.alexandersaul.Alexander.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {
}
