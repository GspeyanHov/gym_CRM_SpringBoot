package org.epam.dto.trainer;

import org.epam.entity.TrainingTypeEntity;
import lombok.Data;


@Data
public class TrainerDto {

    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeEntity specialization;
}