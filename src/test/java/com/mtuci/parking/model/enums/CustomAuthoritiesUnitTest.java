package com.mtuci.parking.model.enums;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomAuthoritiesUnitTest {

    @Test
    public void test_values() {
        Assertions.assertNotNull(CustomAuthorities.valueOf("USER"));
        Assertions.assertNotNull(CustomAuthorities.valueOf("ADMIN"));
    }
}
