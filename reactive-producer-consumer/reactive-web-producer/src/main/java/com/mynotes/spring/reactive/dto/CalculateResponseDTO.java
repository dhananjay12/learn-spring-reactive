package com.mynotes.spring.reactive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateResponseDTO {

    int input;
    long square;
    double sqrt;

}
