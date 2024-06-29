package org.epam.service.impl;

import org.epam.repository.TraineeRepository;
import org.epam.dto.trainer.TrainerDto;
import org.epam.entity.Trainee;
import org.epam.entity.Trainer;
import org.epam.entity.Training;
import org.epam.exception.AuthenticationException;
import org.epam.repository.TrainerRepository;
import org.epam.service.TraineeService;
import org.epam.util.AuthenticationService;
import org.epam.util.ProfileGenerationHelper;
import lombok.NonNull;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;

@Service
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final AuthenticationService authenticationService;

    public TraineeServiceImpl(
        TraineeRepository traineeRepository,
        AuthenticationService authenticationService,
        TrainerRepository trainerRepository
    ) {
        this.traineeRepository = traineeRepository;
        this.authenticationService = authenticationService;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Trainee create(@NonNull Trainee trainee) {
        logger.info("Creating a new trainee with first name: {} and last name: {}", trainee.getFirstName(), trainee.getLastName());
        Set<String> existingUsernames = traineeRepository.getExistingUsernames();
        String username = ProfileGenerationHelper.generateUsername(trainee.getFirstName(), trainee.getLastName(),
                existingUsernames);
        logger.info("Generated username: {}", username);
        String password = ProfileGenerationHelper.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        Trainee createdTrainee = traineeRepository.create(trainee);
        logger.info("Created trainee with ID: {}", createdTrainee.getId());
        return createdTrainee;
    }

    @Override
    public Trainee update(Long id, @NonNull Trainee updatedTrainee, String username, String password) {
        logger.info("Attempting to update trainee with ID: {}, by user: {}", id, username);
        if (authenticationService.authenticate(id, username, password)) {
            Optional<Trainee> existingTraineeOpt = traineeRepository.findById(id);
            if (existingTraineeOpt.isPresent()) {
                logger.info("Found existing trainee with ID: {}", id);
                Trainee existingTrainee = existingTraineeOpt.get();
                existingTrainee.setFirstName(updatedTrainee.getFirstName());
                existingTrainee.setLastName(updatedTrainee.getLastName());
                existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
                existingTrainee.setAddress(updatedTrainee.getAddress());
                existingTrainee.setTrainers(updatedTrainee.getTrainers());
                existingTrainee.setTrainings(updatedTrainee.getTrainings());
                logger.info("Updating trainee with new details: {}", updatedTrainee);
                return traineeRepository.update(existingTrainee);
            } else {
                logger.warn("Trainee with ID: {} not found", id);
                throw new IllegalArgumentException("Trainee not found");
            }
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
    }

    @Override
    public Optional<Trainee> findById(long id, String username, String password) {
        logger.info("Attempting to find trainee with ID: {}, by user: {}", id, username);
        if (authenticationService.authenticate(id, username, password)) {
            Optional<Trainee> existingTraineeOpt = traineeRepository.findById(id);
            if (existingTraineeOpt.isPresent()) {
                logger.info("Found trainee with ID: {}", id);
                return existingTraineeOpt;
            } else {
                logger.warn("Failed to select trainee. Trainee with ID: {} not found.", id);
            }
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
        return Optional.empty();
    }

    @Override
    public void deleteTraineeById(Long id, String username, String password) {
        logger.info("Attempting to delete trainee with ID: {}, by user: {}", id, username);
        if (authenticationService.authenticate(id, username, password)) {
            traineeRepository.deleteById(id);
            logger.info("Deleted trainee with ID: {}", id);
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        Optional<Trainee> traineeOpt = traineeRepository.findTraineeByUsername(username);
        if (traineeOpt.isPresent()) {
            logger.info("Found trainee with username: {}", username);
        } else {
            logger.warn("Failed to find trainee with username: {}", username);
        }
        return traineeOpt;
    }


    @Override
    public void activateTrainee(Long id, String username, String password) {
        if (authenticationService.authenticate(id, username, password)) {
            Optional<Trainee> traineeOpt = findById(id, username, password);
            if (traineeOpt.isPresent()) {
                traineeRepository.setActiveStatus(id, true);
                logger.info("Activated trainee with ID: {}", id);
            } else {
                logger.error("Trainee not found with ID: {}", id);
                throw new IllegalArgumentException("Trainee not found");
            }
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
    }

    @Override
    public void deactivateTrainee(Long id, String username, String password) {
        if (authenticationService.authenticate(id, username, password)) {
            Optional<Trainee> traineeOpt = findById(id, username, password);
            if (traineeOpt.isPresent()) {
                traineeRepository.setActiveStatus(id, false);
                logger.info("Deactivated trainee with ID: {}", id);
            } else {
                logger.error("Trainee not found with ID: {}", id);
                throw new IllegalArgumentException("Trainee not found");
            }
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
    }

    @Override
    public boolean deleteTraineeByUsername(String username, String password) {
        Optional<Trainee> traineeOptional = traineeRepository.findTraineeByUsername(username);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            if (authenticationService.authenticate(trainee.getId(), username, password)) {
                boolean result = traineeRepository.deleteByUsername(username);
                logger.info("Deleted trainee with username: {}", username);
                return result;
            } else {
                logger.error("Authentication failed for user: {}", username);
                throw new AuthenticationException("Authentication failed for user: " + username);
            }
        } else {
            logger.error("Trainee not found with username: {}", username);
            throw new IllegalArgumentException("Trainee not found");
        }
    }

    @Override
    public List<Training> getTrainingsByTraineeUsernameAndTrainerName(String traineeUsername, String trainerName,
                                                                      Long traineeId, String traineePassword) {
        if (authenticationService.authenticate(traineeId, traineeUsername, traineePassword)) {
            List<Training> trainings = traineeRepository.getTrainingsByTraineeUsernameAndTrainerName(traineeUsername, trainerName);
            logger.info("Retrieved trainings for traineeUsername: {} and trainerName: {}", traineeUsername, trainerName);
            return trainings;
        } else {
            logger.error("Authentication failed for user: {}", traineeUsername);
            throw new AuthenticationException("Authentication failed for user: " + traineeUsername);
        }
    }

    @Override
    public Trainee updateTraineeTrainersList(Long traineeId, Set<Trainer> trainers) {
        Trainee updatedTrainee = traineeRepository.updateTraineeTrainersList(traineeId, trainers);
        logger.info("Updated trainers list for trainee with ID: {}", traineeId);
        return updatedTrainee;    }

    @Override
    public void setActiveStatus(String username, boolean isActive) {
        Optional<Trainee> traineeOptional = findByUsername(username);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            traineeRepository.setActiveStatus(trainee.getId(), isActive);
        } else {
            throw new RuntimeException("Trainee not found with username: " + username);
        }
    }

    @Override
    public List<Trainer> getNotAssignedTrainers(String traineeUsername) {
        return traineeRepository.getNotAssignedTrainers(traineeUsername);
    }

    @Override
    public List<TrainerDto> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        Optional<Trainee> traineeOptional = traineeRepository.findTraineeByUsername(traineeUsername);
        if (!traineeOptional.isPresent()) {
            throw new IllegalArgumentException("Trainee not found");
        }

        Set<Trainer> trainers = new HashSet<>();
        for (String trainerUsername : trainerUsernames) {
            Optional<Trainer> trainer = trainerRepository.findTrainerByUsername(trainerUsername);
            if (!trainer.isPresent()) {
                throw new IllegalArgumentException("Trainer not found: " + trainerUsername);
            }
            trainers.add(trainer.get());
        }

        traineeOptional.get().setTrainers(trainers);
        traineeRepository.create(traineeOptional.get());

        List<TrainerDto> trainerDtos = new ArrayList<>();
        for (Trainer trainer : trainers) {
            TrainerDto dto = new TrainerDto();
            dto.setUsername(trainer.getUsername());
            dto.setFirstName(trainer.getFirstName());
            dto.setLastName(trainer.getLastName());
            dto.setSpecialization(trainer.getSpecialization());
            trainerDtos.add(dto);
        }
        return trainerDtos;
    }

}