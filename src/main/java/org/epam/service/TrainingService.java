package org.epam.service;

import org.epam.entity.Training;

import java.util.Optional;

public interface TrainingService {
    Training create(Training training);
    Optional<Training> findById(Long id);
}