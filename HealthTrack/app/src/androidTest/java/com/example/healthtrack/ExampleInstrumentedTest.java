package com.example.healthtrack;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void additionIsCorrect() {
        assertEquals(4, 2 + 2);
    }

    // Input tests
    @Test
    public void testWhiteSpaceOnlyInput() {
        assertFalse(isValidInput("   "));
    }

    @Test
    public void testNullInput() {
        assertFalse(isValidInput(null));
    }

    @Test
    public void testEmptyInput() {
        assertFalse(isValidInput(""));
    }

    @Test
    public void testValidInput() {
        assertTrue(isValidInput("validInput"));
    }

    private boolean isValidInput(String input) {
        return input != null && !input.trim().isEmpty();
    }


    // Firebase
    private FirebaseAuth mAuth;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.healthtrack", appContext.getPackageName());
    }

    // Login Tests
    @Before
    public void setUp() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Test login with correct info
    @Test
    public void testLoginSuccess() {
        mAuth.signInWithEmailAndPassword("fakeUser@gmail.com", "password")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assertNotNull(user);
                        assertTrue(user.getEmail().equals("fakeuser@gmail.com"));
                    } else {
                        assertNull(task.getException());
                    }
                });
    }

    // Test login with incorrect password
    @Test
    public void testLoginWithInvalidCredentials() {
        mAuth.signInWithEmailAndPassword("fakeuser@gmail.com", "wrongpassword")
                .addOnCompleteListener(task -> {
                    assertNull(mAuth.getCurrentUser());
                    assertNotNull(task.getException());
                });
    }

    // Test Login With empty email
    @Test
    public void testLoginWithEmptyEmail() {
        try {
            mAuth.signInWithEmailAndPassword("", "password")
                    .addOnCompleteListener(task -> {
                        assertFalse(task.isSuccessful());
                    });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // if illegal argument exception is thrown, code is working fine
            assertEquals("Given String is empty or null", e.getMessage());
        }
    }

    // Test login with empty password
    @Test
    public void testLoginWithEmptyPassword() {
        try {
            mAuth.signInWithEmailAndPassword("fakeuser@gmail.com", "")
                    .addOnCompleteListener(task -> {
                        assertFalse(task.isSuccessful());
                    });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // if illegal argument exception is thrown, code is working fine
            assertEquals("Given String is empty or null", e.getMessage());
        }
    }

    // Test logout button
    @Test
    public void testLogoutButton() {
        mAuth.signOut();
        assertNull(mAuth.getCurrentUser());
    }

    // Test Login with invalid email
    @Test
    public void testLoginWithInvalidEmail() {
        mAuth.signInWithEmailAndPassword("invalidEmail", "password")
                .addOnCompleteListener(task -> {
                    assertNull(mAuth.getCurrentUser());
                    assertNotNull(task.getException());
                });
    }
}