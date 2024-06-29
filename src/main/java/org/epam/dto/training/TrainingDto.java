package org.epam.dto.training;

import org.epam.entity.DayOfWeek;
import lombok.Data;

import java.time.Duration;
import java.util.Set;

@Data
public class TrainingDto {

    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private Long trainingTypeId;
    private Set<DayOfWeek> trainingDays;
    private Duration trainingDuration;
}