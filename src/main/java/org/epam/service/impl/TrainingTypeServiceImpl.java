package org.epam.service.impl;

import org.epam.repository.TrainingTypeRepository;
import org.epam.entity.TrainingType;
import org.epam.entity.TrainingTypeEntity;
import org.epam.service.TrainingTypeService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeServiceImpl.class);

    private TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository){
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public TrainingTypeEntity create(@NonNull TrainingTypeEntity trainingTypeEntity) {
        TrainingTypeEntity createdTrainingTypeEntity = trainingTypeRepository.create(trainingTypeEntity);
        logger.info("Training type created with ID: {}", createdTrainingTypeEntity.getId());
        return createdTrainingTypeEntity;
    }

    @Override
    public TrainingTypeEntity findByName(@NonNull TrainingType trainingType) {
        TrainingTypeEntity trainingTypeEntity = trainingTypeRepository.findByName(trainingType);
        if (trainingTypeEntity != null) {
            logger.info("Found training type with name: {}", trainingType);
        } else {
            logger.warn("Training type with name: {} not found", trainingType);
        }
        return trainingTypeEntity;
    }

    @Override
    public List<TrainingTypeEntity> getAllTrainingTypes() {
        return trainingTypeRepository.getAllTrainingTypes();
    }

}