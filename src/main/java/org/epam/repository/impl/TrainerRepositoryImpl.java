package org.epam.repository.impl;

import org.epam.repository.TrainerRepository;
import org.epam.entity.Trainer;
import org.epam.entity.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public class TrainerRepositoryImpl implements TrainerRepository {
    private static final Logger logger = LoggerFactory.getLogger(TrainerRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public Trainer create(@NonNull Trainer trainer) {
        entityManager.persist(trainer);
        logger.info("Created trainer with ID: {}", trainer.getId());
        return trainer;
    }

    @Override
    @Transactional
    public Trainer update(@NonNull Trainer trainer) {
        return entityManager.merge(trainer);
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainer.class, id));
    }


    @Override
    public Set<String> getExistingUsernames() {
        String jpql = "SELECT username FROM Trainer";
        List<String> usernames = entityManager.createQuery(jpql, String.class).getResultList();
        return new HashSet<>(usernames);
    }

    @Override
    public Optional<Trainer> findTrainerByUsername(String username) {
        try {
            Trainer trainer = entityManager.createQuery("SELECT t FROM Trainer t LEFT JOIN FETCH t.trainings WHERE " +
                            "t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(trainer);
        } catch (Exception e) {
            logger.error("Error finding trainer by username: {}", username, e);
            return Optional.empty();
        }
    }


    @Override
    @Transactional
    public void setActiveStatus(Long id, boolean isActive) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer != null) {
            trainer.setActive(isActive);
            entityManager.merge(trainer);
        }
    }

    @Override
    public List<Training> getTrainingsByTrainerUsernameAndTraineeName(String trainerUsername, String traineeName) {
        return entityManager.createQuery(
                        "SELECT tr FROM Training tr " +
                                "JOIN tr.trainer t " +
                                "JOIN tr.trainee trn " +
                                "WHERE t.username = :trainerUsername AND trn.firstName = :traineeName", Training.class)
                .setParameter("trainerUsername", trainerUsername)
                .setParameter("traineeName", traineeName)
                .getResultList();
    }


    @Override
    public List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername) {
        return entityManager.createQuery(
                        "SELECT t FROM Trainer t WHERE t.id NOT IN " +
                                "(SELECT tr.trainer.id FROM Training tr WHERE tr.trainee.username = :traineeUsername)",
                        Trainer.class)
                .setParameter("traineeUsername", traineeUsername)
                .getResultList();
    }

}