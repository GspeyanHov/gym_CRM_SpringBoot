package org.epam.repository;

import org.epam.entity.Trainee;
import org.epam.entity.Trainer;
import org.epam.entity.Training;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TraineeRepository {

    Trainee create(Trainee trainee);

    Trainee update(Trainee trainee);

    Optional<Trainee> findById(Long id);

    void deleteById(Long id);

    Set<String> getExistingUsernames();

    Optional<Trainee> findTraineeByUsername(String username);

    void setActiveStatus(Long id, boolean isActive);

    boolean deleteByUsername(String username);

    List<Training> getTrainingsByTraineeUsernameAndTrainerName(String traineeUsername, String trainerName);

    Trainee updateTraineeTrainersList(Long traineeId, Set<Trainer> trainers);

    List<Trainer> getNotAssignedTrainers(String traineeUsername);


}