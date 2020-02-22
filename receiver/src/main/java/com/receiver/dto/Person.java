package com.receiver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Person {

    @NotBlank(message = "Missing name")
    private String name;
    private Date dateOfBirth;
    private ArrayList<Pet> pets;
    private boolean isTransformed;

}
