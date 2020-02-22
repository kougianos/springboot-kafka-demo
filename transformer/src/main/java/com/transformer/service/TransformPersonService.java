package com.transformer.service;

import com.transformer.dto.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransformPersonService {

    private static final Logger logger = LoggerFactory.getLogger(TransformPersonService.class);

    public Person transformPerson(Person person) {

        logger.info("Transforming person: {}", person.getName());
        person.setTransformed(true);
        person.setName("TRANSFORMER");
        return person;

    }
}
