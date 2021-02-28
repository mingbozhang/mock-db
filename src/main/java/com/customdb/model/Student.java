package com.customdb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author mingbozhang
 */
@Data
@AllArgsConstructor
public class Student {

    private int sex;
    private String name;
    private Integer age;
    private LocalDate birthday;
}
