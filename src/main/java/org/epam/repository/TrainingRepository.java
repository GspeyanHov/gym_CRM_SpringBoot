package org.epam.repository;

import org.epam.entity.Training;

import java.util.Optional;

public interface TrainingRepository {

    Training create(Training training);

    Optional<Training> findById(Long id);

}