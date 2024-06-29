package org.epam.service.impl;

import org.epam.repository.TrainerRepository;
import org.epam.entity.Trainer;
import org.epam.entity.Training;
import org.epam.exception.AuthenticationException;
import org.epam.service.TrainerService;
import org.epam.util.AuthenticationService;
import org.epam.util.ProfileGenerationHelper;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerRepository trainerRepository;
    private AuthenticationService authenticationService;

    public TrainerServiceImpl(
        TrainerRepository trainerRepository,
        AuthenticationService authenticationService
    ){
        this.trainerRepository = trainerRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Trainer create(@NonNull Trainer trainer) {
        Set<String> existingUsernames = trainerRepository.getExistingUsernames();
        String username = ProfileGenerationHelper.generateUsername(trainer.getFirstName(), trainer.getLastName(),
                existingUsernames);
        String password = ProfileGenerationHelper.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        Trainer createdTrainer = trainerRepository.create(trainer);
        logger.info("Trainer created with username: {}", username);
        return createdTrainer;
    }

    @Override
    public Trainer update(Long id, @NonNull Trainer updatedTrainer, String username, String password) {
        if (authenticationService.authenticate(id, username, password)) {
            Optional<Trainer> existingTrainerOpt = trainerRepository.findById(id);
            if (existingTrainerOpt.isPresent()) {
                Trainer existingTrainer = existingTrainerOpt.get();
                existingTrainer.setFirstName(updatedTrainer.getFirstName());
                existingTrainer.setLastName(updatedTrainer.getLastName());
                existingTrainer.setSpecialization(updatedTrainer.getSpecialization());
                existingTrainer.setTrainees(updatedTrainer.getTrainees());
                existingTrainer.setTrainings(updatedTrainer.getTrainings());
                existingTrainer.setActive(true);
                Trainer updated = trainerRepository.update(existingTrainer);
                logger.info("Updated trainer with ID: {}", id);
                return updated;
            } else {
                logger.error("Trainer not found with ID: {}", id);
                throw new IllegalArgumentException("Trainer not found");
            }
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
    }

    @Override
    public Optional<Trainer> findById(long id, String username, String password) {
        if (authenticationService.authenticate(id, username, password)) {
            Optional<Trainer> existingTrainerOpt = trainerRepository.findById(id);
            if (existingTrainerOpt.isPresent()) {
                logger.info("Found trainer with ID: {}", id);
                return existingTrainerOpt;
            } else {
                logger.warn("Trainer with ID: {} not found.", id);
            }
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
        return Optional.empty();
    }
    @Override
    public Optional<Trainer> findTrainerByUsername(String username) {
        Optional<Trainer> trainerOpt = trainerRepository.findTrainerByUsername(username);
        if (trainerOpt.isPresent()) {
            logger.info("Found trainer with username: {}", username);
        } else {
            logger.warn("Trainer with username: {} not found", username);
        }

        return trainerOpt;
    }


    @Override
    public void activateTrainer(Long id, String username, String password) {
        if (authenticationService.authenticate(id, username, password)) {
            Optional<Trainer> trainerOpt = findById(id, username, password);
            if (trainerOpt.isPresent()) {
                trainerRepository.setActiveStatus(id, true);
                logger.info("Activated trainer with ID: {}", id);
            } else {
                logger.error("Trainer not found with ID: {}", id);
                throw new IllegalArgumentException("Trainer not found");
            }
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
    }

    @Override
    public void deactivateTrainer(Long id, String username, String password) {
        if (authenticationService.authenticate(id, username, password)) {
            Optional<Trainer> trainerOpt = findById(id, username, password);
            if (trainerOpt.isPresent()) {
                trainerRepository.setActiveStatus(id, false);
                logger.info("Deactivated trainer with ID: {}", id);
            } else {
                logger.error("Trainer not found with ID: {}", id);
                throw new IllegalArgumentException("Trainer not found");
            }
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
    }

    @Override
    public List<Training> getTrainingsByTrainerUsernameAndTraineeName(String trainerUsername, String traineeName,
                                                                      Long trainerId, String trainerPassword) {
        if (authenticationService.authenticate(trainerId, trainerUsername, trainerPassword)) {
            List<Training> trainings = trainerRepository.getTrainingsByTrainerUsernameAndTraineeName(trainerUsername, traineeName);
            logger.info("Retrieved trainings for trainerUsername: {} and traineeName: {}", trainerUsername, traineeName);
            return trainings;
        } else {
            logger.error("Authentication failed for trainer: {}", trainerUsername);
            throw new AuthenticationException("Authentication failed for trainer: " + trainerUsername);
        }
    }

    @Override
    public List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername) {
        logger.info("Getting unassigned trainers by traineeUsername: {}", traineeUsername);
        List<Trainer> unassignedTrainers = trainerRepository.getUnassignedTrainersByTraineeUsername(traineeUsername);
        logger.info("Retrieved unassigned trainers for traineeUsername: {}", traineeUsername);
        return unassignedTrainers;    }

    @Override
    public void setActiveStatus(String username, boolean isActive) {
        Optional<Trainer> trainerOptional = findTrainerByUsername(username);
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            trainerRepository.setActiveStatus(trainer.getId(), isActive);
        } else {
            throw new RuntimeException("Trainer not found with username: " + username);
        }
    }

}