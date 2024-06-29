package org.epam.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ProfileGenerationHelperTest {

    private Set<String> existingUsernames;

    @BeforeEach()
    public void setUp(){
        existingUsernames = new HashSet<>();
    }

    @Test
    void givenUsernameNotExist_whenGenerateUsername_thenShouldConcatenateFirsNameLastNameByDot() {

        String generatedUsername = ProfileGenerationHelper.generateUsername("Anna", "Petrova",
                existingUsernames);
        assertEquals("Anna.Petrova", generatedUsername);
    }

    @Test
    void givenUsernameExist_whenGenerateUsername_thenShouldConcatenateFirsNameLastNameAndAddNumber() {
        existingUsernames.add("Anna.Smirnova");

        String generatedUsername = ProfileGenerationHelper.generateUsername("Anna", "Smirnova",
                existingUsernames);
        assertEquals("Anna.Smirnova1", generatedUsername);

        existingUsernames.add(generatedUsername);
        generatedUsername = ProfileGenerationHelper.generateUsername("Anna", "Smirnova", existingUsernames);
        assertEquals("Anna.Smirnova2", generatedUsername);
    }

    @Test
    void whenGeneratePassword_thenShouldGenerateRandom10CharsLengthString() {
        String generatedPassword = ProfileGenerationHelper.generatePassword();
        assertNotNull(generatedPassword);
        assertEquals(10, generatedPassword.length());
        assertTrue(generatedPassword.matches("[A-Za-z0-9]+"));
    }
}