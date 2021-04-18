package com.idm.service.assertions;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UtilityClass
public class AssertMessages {
    public void assertThatLombokNonNullIsPresent(@NonNull Executable methodToCall, @NonNull String argName) {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                methodToCall);

        assertThat(
                "Error message is incorrect",
                thrown.getMessage(),
                equalTo(argName + " is marked @NonNull but is null"));
    }

    public <T extends Throwable> void assertMessage(
            @NonNull Executable methodToCall,
            @NonNull Class<T> clazz,
            @NonNull String message) {
        T thrown = assertThrows(
                clazz,
                methodToCall);

        assertThat(
                "Error message is incorrect",
                thrown.getMessage(),
                equalTo(message));
    }
}
