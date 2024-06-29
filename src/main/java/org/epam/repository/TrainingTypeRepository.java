package org.epam.repository;

import org.epam.entity.TrainingType;
import org.epam.entity.TrainingTypeEntity;

import java.util.List;

public interface TrainingTypeRepository {

    TrainingTypeEntity create(TrainingTypeEntity trainingType);

    TrainingTypeEntity findByName(TrainingType trainingType);

    List<TrainingTypeEntity> getAllTrainingTypes();
}