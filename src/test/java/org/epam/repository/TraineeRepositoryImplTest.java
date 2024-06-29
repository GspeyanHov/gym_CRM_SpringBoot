package org.epam.repository;

import org.epam.entity.Address;
import org.epam.entity.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.epam.repository.impl.TraineeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeRepositoryImplTest {
    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery query;

    @InjectMocks
    private TraineeRepositoryImpl traineeRepository;

    private Trainee trainee;
    private Address address;


    @BeforeEach
    void setUp() {
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
    }

    @Test
    void whenCreateTrainee_thenSuccess() {

        Trainee createdTrainee = traineeRepository.create(trainee);

        assertNotNull(createdTrainee);
        assertEquals(trainee, createdTrainee);
    }

    @Test
    void givenTraineeExist_whenUpdateTrainee_thenSuccess() {
        Trainee updatedTrainee = Trainee.builder()
                .firstName("Anna")
                .lastName("Ivanova")
                .username(trainee.getUsername())
                .password(trainee.getPassword())
                .isActive(true)
                .dateOfBirth(LocalDate.of(1993,7,7))
                .address(address)
                .build();
        when(entityManager.merge(trainee)).thenReturn(updatedTrainee);

        Trainee actualTrainee = traineeRepository.update(trainee);

        verify(entityManager).merge(trainee);
        assertEquals(actualTrainee, updatedTrainee);
    }

    @Test
    void givenTraineeExist_whenFindById_thenSuccess() {
        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(trainee);

        Optional<Trainee> actualTrainee = traineeRepository.findById(trainee.getId());

        assertEquals(Optional.of(trainee), actualTrainee);
        verify(entityManager, times(1)).find(Trainee.class, trainee.getId());
    }

    @Test
    void givenTraineeNotExist_whenFindById_thenReturnNull() {
        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(null);

        Optional<Trainee> actualTrainee = traineeRepository.findById(trainee.getId());

        assertFalse(actualTrainee.isPresent());
        verify(entityManager, times(1)).find(Trainee.class, trainee.getId());
    }

    @Test
    void givenTraineeExist_whenDeleteById_thenSuccess() {
        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(trainee);

        traineeRepository.deleteById(trainee.getId());

        verify(entityManager, times(1)).find(Trainee.class, trainee.getId());
        verify(entityManager, times(1)).remove(trainee);
    }

    @Test
    void givenTraineeNotExist_whenDeleteById() {
        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(null);

        traineeRepository.deleteById(trainee.getId());

        verify(entityManager, times(1)).find(Trainee.class, trainee.getId());
        verify(entityManager, never()).remove(any(Trainee.class));
    }

    @Test
    void givenUsernamesExisted_whenGetExistingUsernames_thenSuccess() {
        List<String> usernames = Arrays.asList("Petr", "Anna", "Ivan");
        when(entityManager.createQuery("SELECT username FROM Trainee", String.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(usernames);

        Set<String> expectedUsernames = new HashSet<>(usernames);
        Set<String> actualUsernames = traineeRepository.getExistingUsernames();

        assertEquals(expectedUsernames, actualUsernames);
        verify(entityManager).createQuery("SELECT username FROM Trainee", String.class);
        verify(query).getResultList();
    }
//
//    @Test
//    void givenUsernameExisted_whenSelectTraineeByUsername_thenSuccess(){
//
//        when(entityManager.createQuery("SELECT t FROM Trainee t LEFT JOIN FETCH t.trainings WHERE t.username = :username", Trainee.class))
//                .thenReturn(query);
//        when(query.setParameter("username", trainee.getUsername())).thenReturn(query);
//        when(query.getSingleResult()).thenReturn(trainee);
//
//        Optional<Trainee> result = traineeDao.findTraineeByUsername(trainee.getUsername());
//
//        assertTrue(result.isPresent());
//        assertEquals(trainee, result.get());
//        assertEquals(trainee.getUsername(), result.get().getUsername());
//        verify(entityManager).createQuery("SELECT t FROM Trainee t LEFT JOIN FETCH t.trainings WHERE t.username = :username", Trainee.class);
//    }

//    @Test
//    void givenUsernameNotExisted_whenSelectTraineeByUsername_thenNotFound(){
//
//        when(entityManager.createQuery("SELECT t FROM Trainee t LEFT JOIN FETCH t.trainings WHERE t.username = :username", Trainee.class))
//                .thenReturn(query);
//        when(query.setParameter("username", trainee.getUsername())).thenReturn(query);
//        when(query.getSingleResult()).thenThrow(new NoResultException());
//
//        Optional<Trainee> result = traineeDao.findTraineeByUsername(trainee.getUsername());
//
//        assertFalse(result.isPresent());
//        verify(entityManager).createQuery("SELECT t FROM Trainee t LEFT JOIN FETCH t.trainings WHERE t.username = :username", Trainee.class);
//    }

//    @Test
//    void givenTraineeExisted_whenChangePassword_thenSuccess(){
//        String newPassword = "newPassword";
//        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(trainee);
//        when(entityManager.merge(trainee)).thenReturn(trainee);
//
//        Optional<Trainee> actualTrainee = traineeDao.changePassword(trainee.getId(), newPassword);
//
//        assertTrue(actualTrainee.isPresent());
//        assertEquals(newPassword, actualTrainee.get().getPassword());
//        verify(entityManager).find(Trainee.class, trainee.getId());
//        verify(entityManager).merge(trainee);
//    }
//
//    @Test
//    void givenTraineeNotExisted_whenChangePassword_thenNotFound(){
//        String newPassword = "newPassword";
//        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(null);
//
//        Optional<Trainee> actualTrainee = traineeDao.changePassword(trainee.getId(), newPassword);
//
//        assertFalse(actualTrainee.isPresent());
//        verify(entityManager).find(Trainee.class, trainee.getId());
//        verify(entityManager, never()).merge(any(Trainee.class));
//    }

    @Test
    void givenTraineeExisted_whenSetActiveStatus_thenSuccess(){
        boolean isActive = false;
        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(trainee);
        when(entityManager.merge(any(Trainee.class))).thenReturn(trainee);

        traineeRepository.setActiveStatus(trainee.getId(), isActive);

        verify(entityManager).find(Trainee.class, trainee.getId());
        verify(entityManager).merge(trainee);
        assertEquals(isActive, trainee.isActive());
    }

    @Test
    void givenTraineeNotExisted_whenSetActiveStatus_thenNotFound(){
        boolean isActive = false;
        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(null);

        traineeRepository.setActiveStatus(trainee.getId(), isActive);

        verify(entityManager).find(Trainee.class, trainee.getId());
        verify(entityManager, never()).merge(any(Trainee.class));

    }

    }