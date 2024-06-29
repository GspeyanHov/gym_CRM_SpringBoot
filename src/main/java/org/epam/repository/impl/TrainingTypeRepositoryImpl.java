package org.epam.repository.impl;

import org.epam.repository.TrainingTypeRepository;
import org.epam.entity.TrainingType;
import org.epam.entity.TrainingTypeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public TrainingTypeEntity create(@NonNull TrainingTypeEntity trainingTypeEntity) {
        entityManager.persist(trainingTypeEntity);
        return trainingTypeEntity;
    }

    @Override
    public TrainingTypeEntity findByName(@NonNull TrainingType trainingType) {
        TypedQuery<TrainingTypeEntity> query = entityManager.createQuery(
                "SELECT t FROM TrainingTypeEntity t WHERE t.trainingTypeName = :name", TrainingTypeEntity.class);
        query.setParameter("name", trainingType);
        return query.getSingleResult();

    }

    @Override
    public List<TrainingTypeEntity> getAllTrainingTypes() {
        TypedQuery<TrainingTypeEntity> query = entityManager.createQuery("SELECT t FROM TrainingTypeEntity t",
                TrainingTypeEntity.class);
            return query.getResultList();
        }
}