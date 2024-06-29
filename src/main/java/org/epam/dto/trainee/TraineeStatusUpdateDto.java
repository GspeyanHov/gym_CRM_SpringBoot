package org.epam.dto.trainee;

import lombok.Data;

@Data
public class TraineeStatusUpdateDto {

    private String username;
    private boolean isActive;
}