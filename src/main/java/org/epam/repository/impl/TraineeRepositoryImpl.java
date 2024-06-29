package org.epam.repository.impl;

import org.epam.repository.TraineeRepository;
import org.epam.entity.Trainee;
import org.epam.entity.Trainer;
import org.epam.entity.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class TraineeRepositoryImpl implements TraineeRepository {

    private static final Logger logger = LoggerFactory.getLogger(TraineeRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Trainee create(@NonNull Trainee trainee) {
        entityManager.persist(trainee);
        return trainee;
    }

    @Override
    @Transactional
    public Trainee update(@NonNull Trainee trainee) {
        return entityManager.merge(trainee);
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainee.class, id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Trainee trainee = entityManager.find(Trainee.class, id);
        if (trainee != null) {
            entityManager.remove(trainee);
        }
    }

    @Override
    public Set<String> getExistingUsernames() {
        List<String> usernames = entityManager.createQuery("SELECT username FROM Trainee", String.class).getResultList();
        return new HashSet<>(usernames);
    }

    @Override
    public Optional<Trainee> findTraineeByUsername(String username) {
        try {
            TypedQuery<Trainee> query = entityManager.createQuery(
                    "SELECT t FROM Trainee t LEFT JOIN FETCH t.trainings WHERE t.username = :username", Trainee.class
            );
            query.setParameter("username", username);
            Trainee trainee = query.getSingleResult();
            return Optional.of(trainee);
        } catch (Exception e) {
            logger.error("Error finding trainee by username: {}", username, e);
            return Optional.empty();
        }
    }


    @Override
    @Transactional
    public void setActiveStatus(Long id, boolean isActive) {
        Trainee trainee = entityManager.find(Trainee.class, id);
        if (trainee != null) {
            trainee.setActive(isActive);
            entityManager.merge(trainee);
        }
    }

    @Override
    @Transactional
    public boolean deleteByUsername(String username) {
        Optional<Trainee> traineeOptional = findTraineeByUsername(username);
        if (traineeOptional.isPresent()) {
            entityManager.remove(traineeOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Training> getTrainingsByTraineeUsernameAndTrainerName(String traineeUsername, String trainerName) {
        return entityManager.createQuery(
                        "SELECT tr FROM Training tr " +
                                "JOIN tr.trainee t " +
                                "JOIN tr.trainer trn " +
                                "WHERE t.username = :traineeUsername AND trn.lastName = :trainerName", Training.class)
                .setParameter("traineeUsername", traineeUsername)
                .setParameter("trainerName", trainerName)
                .getResultList();
    }

    @Override
    @Transactional
    public Trainee updateTraineeTrainersList(Long traineeId, Set<Trainer> trainers) {
        Trainee trainee = entityManager.find(Trainee.class, traineeId);
        trainee.setTrainers(trainers);
        entityManager.merge(trainee);
        return trainee;
    }

    @Override
    public List<Trainer> getNotAssignedTrainers(String traineeUsername) {
        String jpql = "SELECT t FROM Trainer t " +
                "WHERE NOT EXISTS (" +
                "   SELECT 1 FROM Training tr " +
                "   WHERE tr.trainee.username = :traineeUsername " +
                "   AND tr.trainer = t" +
                ")";

        TypedQuery<Trainer> query = entityManager.createQuery(jpql, Trainer.class);
        query.setParameter("traineeUsername", traineeUsername);

        return query.getResultList();

    }
}