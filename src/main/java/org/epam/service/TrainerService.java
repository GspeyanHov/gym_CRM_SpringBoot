package org.epam.service;

import org.epam.entity.Trainer;
import org.epam.entity.Training;

import java.util.List;
import java.util.Optional;

public interface TrainerService {

    Trainer create(Trainer trainer);

    Trainer update(Long id, Trainer updatedTrainer, String username, String password);

    Optional<Trainer> findById(long id, String username, String password);

    Optional<Trainer> findTrainerByUsername(String username);

    void activateTrainer(Long id, String username, String password);

    void deactivateTrainer(Long id, String username, String password);

    List<Training> getTrainingsByTrainerUsernameAndTraineeName(
            String trainerUsername, String traineeName, Long trainerId, String trainerPassword);

    List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername);

    void setActiveStatus(String username, boolean isActive);


}