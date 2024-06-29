package org.epam.service.impl;

import org.epam.repository.TrainingRepository;
import org.epam.entity.Training;
import org.epam.service.TrainingService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository){
        this.trainingRepository = trainingRepository;
    }
    @Override
    public Training create(@NonNull Training training) {
        Training createdTraining = trainingRepository.create(training);
        logger.info("Training created with ID: {}", createdTraining.getId());
        return createdTraining;    }

    @Override
    public Optional<Training> findById(Long id) {
        Optional<Training> training = trainingRepository.findById(id);
        if (training != null) {
            logger.info("Found training with ID: {}", id);
            return training;
        } else {
            logger.warn("Training with ID: {} not found", id);
        }
        return Optional.empty();
    }

}