package org.epam.util;

import org.epam.entity.Address;
import org.epam.entity.Trainee;
import org.epam.exception.AuthenticationException;
import org.epam.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Trainee trainee;
    private Address address;

    @BeforeEach
    void setUp() {
        trainee = Trainee.builder()
                .id(1L)
                .firstName("Foe")
                .lastName("Fof")
                .username("username")
                .password("password")
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
    void givenTraineeExist_whenAuthenticate_thenSuccess() {
        when(userService.findByUsername(trainee.getUsername())).thenReturn(Optional.of(trainee));

        boolean isAuthenticated = authenticationService.authenticate(trainee.getId(), trainee.getUsername(), trainee.getPassword());

        assertTrue(isAuthenticated);
    }

    @Test
    void givenTraineeNotExist_whenAuthenticate_thenSuccess()  {
        when(userService.findByUsername(trainee.getUsername())).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate(trainee.getId(), trainee.getUsername(), trainee.getPassword());
        });
    }
}