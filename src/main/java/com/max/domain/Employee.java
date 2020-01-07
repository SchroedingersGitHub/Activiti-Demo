package com.max.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long id;

    private String name;

    private String password;

    private String email;

    private String role;

    private Employee manager; // 上一层的领导


}