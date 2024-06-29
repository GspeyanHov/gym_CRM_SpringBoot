package org.epam.repository;

import org.epam.entity.TrainingType;
import org.epam.entity.TrainingTypeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.epam.repository.impl.TrainingTypeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
 class TrainingTypeRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery query;
    @InjectMocks
    private TrainingTypeRepositoryImpl trainingTypeRepository;

    TrainingTypeEntity trainingTypeEntity;



    @BeforeEach
    void setUp() {
        trainingTypeEntity = TrainingTypeEntity.builder()
                .trainingTypeName(TrainingType.CARDIO)
                .build();

    }

    @Test
    void whenCreateTrainingTypeEntity_thenSuccess() {

        TrainingTypeEntity createdTrainingTypeEntity = trainingTypeRepository.create(trainingTypeEntity);

        assertNotNull(createdTrainingTypeEntity);
        assertEquals(trainingTypeEntity, createdTrainingTypeEntity);
    }


}