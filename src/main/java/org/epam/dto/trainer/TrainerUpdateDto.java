package org.epam.dto.trainer;

import org.epam.entity.TrainingTypeEntity;
import lombok.Data;

@Data
public class TrainerUpdateDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private TrainingTypeEntity specialization;
    private boolean isActive;
}