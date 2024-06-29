package org.epam.dto.trainer;

import org.epam.entity.TrainingTypeEntity;
import lombok.Data;

@Data
public class TrainerRegistrationDto {
    private String firstName;
    private String lastName;
    private TrainingTypeEntity specialization;

}