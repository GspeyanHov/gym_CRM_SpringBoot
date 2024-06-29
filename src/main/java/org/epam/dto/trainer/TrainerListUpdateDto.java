package org.epam.dto.trainer;

import lombok.Data;

import java.util.List;

@Data
public class TrainerListUpdateDto {

    private String traineeUsername;
    private List<String> trainerUsernames;

}