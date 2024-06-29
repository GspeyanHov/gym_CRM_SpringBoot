package org.epam.repository;

import org.epam.entity.Trainer;
import org.epam.entity.Training;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainerRepository {

    Trainer create(Trainer trainer);

    Optional<Trainer> findById(Long id);

    Trainer update(Trainer trainer);

    Set<String> getExistingUsernames();

    Optional<Trainer> findTrainerByUsername(String username);

    void setActiveStatus(Long id, boolean isActive);

    List<Training> getTrainingsByTrainerUsernameAndTraineeName(String trainerUsername, String traineeName);

    List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername);




}