package com.example.restspringapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mark {

    private long id;

    private int rating;

    private long studentId;

    private long teacherId;
}
