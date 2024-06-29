package org.epam.service;

import org.epam.entity.TrainingType;
import org.epam.entity.TrainingTypeEntity;

import java.util.List;

public interface TrainingTypeService {

    TrainingTypeEntity create(TrainingTypeEntity trainingTypeEntity);

    TrainingTypeEntity findByName(TrainingType trainingType);

    List<TrainingTypeEntity> getAllTrainingTypes();



}