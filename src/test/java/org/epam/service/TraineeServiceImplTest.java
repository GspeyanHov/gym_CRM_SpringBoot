package org.epam.service;

import org.epam.repository.TraineeRepository;
import org.epam.entity.Address;
import org.epam.entity.Trainee;
import org.epam.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;
    private Address address;
    private Set<String> existingUsernames;


    @BeforeEach
    void setUp(){

        trainee = Trainee.builder()
                .firstName("Foe")
                .lastName("Fof")
                .username(null)
                .password(null)
                .isActive(true)
                .dateOfBirth(LocalDate.of(1993, 2, 4))
                .build();

        address = Address.builder()
                .city("Moscow")
                .street("New str")
                .buildingNumber(580)
                .apartmentNumber(60)
                .trainee(trainee)
                .build();
        trainee.setAddress(address);

        existingUsernames = new HashSet<>();

    }

    @Test
    void givenUsernameNotExist_whenCreate_thenSuccess(){
        when(traineeRepository.getExistingUsernames()).thenReturn(existingUsernames);
        when(traineeRepository.create(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.create(trainee);

        verify(traineeRepository).create(trainee);

        assertEquals(trainee.getFirstName(), createdTrainee.getFirstName());
        assertEquals(trainee.getLastName(), createdTrainee.getLastName());
        assertNotNull(trainee.getUsername());
        assertNotNull(trainee.getPassword());

    }

}