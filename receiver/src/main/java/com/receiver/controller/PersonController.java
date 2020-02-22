package com.receiver.controller;

import com.receiver.dto.Person;
import com.receiver.service.SendPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class PersonController {

    private SendPersonService sendPersonService;
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    public PersonController(SendPersonService sendPersonService) {
        this.sendPersonService = sendPersonService;
    }

    /**
     * Handle asset post controller.
     */
    @PostMapping("/postPrepaidAsset")
    @ResponseStatus(value = HttpStatus.OK)
    public void handleAsset(@Valid @RequestBody Person person) {
        logger.info("Received person: {}", person);
        sendPersonService.sendPersonToKafka(person);
    }

}
