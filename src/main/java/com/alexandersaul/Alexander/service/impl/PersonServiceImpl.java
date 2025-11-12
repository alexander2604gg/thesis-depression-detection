package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.dto.person.PersonRegisterDto;
import com.alexandersaul.Alexander.dto.person.PersonResponseDto;
import com.alexandersaul.Alexander.entity.Person;
import com.alexandersaul.Alexander.mapper.PersonMapper;
import com.alexandersaul.Alexander.mapper.PersonRepository;
import com.alexandersaul.Alexander.service.PersonService;
import com.alexandersaul.Alexander.service.internal.PersonInternalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, PersonInternalService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public PersonResponseDto save(PersonRegisterDto personRegisterDto) {
        Person person = personMapper.toEntity(personRegisterDto);
        Person personSaved = personRepository.save(person);
        return personMapper.toResponseDto(personSaved);
    }

    @Override
    public void delete(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("La persona no existe"));
        personRepository.delete(person);
    }

    @Override
    public Person saveEntity(PersonRegisterDto personRegisterDto) {
        return personRepository.save(personMapper.toEntity(personRegisterDto));
    }
}
