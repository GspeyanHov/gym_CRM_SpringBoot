package org.epam.service;

import org.epam.repository.TrainingTypeRepository;
import org.epam.entity.TrainingType;
import org.epam.entity.TrainingTypeEntity;
import org.epam.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {
    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    private TrainingTypeEntity trainingTypeEntity;

    @BeforeEach
    void setUp() {
        trainingTypeEntity = TrainingTypeEntity.builder()
                .trainingTypeName(TrainingType.CARDIO)
                .build();

    }

    @Test
    void whenCreateTrainingTypeEntity_thenSuccess() {
        when(trainingTypeRepository.create(any(TrainingTypeEntity.class))).thenReturn(trainingTypeEntity);

        TrainingTypeEntity createdTrainingTypeEntity = trainingTypeService.create(trainingTypeEntity);

        assertEquals(trainingTypeEntity, createdTrainingTypeEntity);
    }

    @Test
    void givenTrainingTypeExist_whenSelectByName_thenSuccess() {
        when(trainingTypeRepository.findByName(any(TrainingType.class))).thenReturn(trainingTypeEntity);

        TrainingTypeEntity result = trainingTypeService.findByName(trainingTypeEntity.getTrainingTypeName());

        assertEquals(trainingTypeEntity, result);
    }

    @Test
    void givenTrainingTypeNotExist_whenSelectByName_thenReturnNull() {
        when(trainingTypeRepository.findByName(any(TrainingType.class))).thenReturn(null);

        TrainingTypeEntity result = trainingTypeService.findByName(trainingTypeEntity.getTrainingTypeName());

        assertNull(result);
    }

}