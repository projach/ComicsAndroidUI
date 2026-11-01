package com.projach.comics_tracker.domain.validation

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class UsernameValidatorTest {
    @Test
    fun `too short`() = assertEquals(ValidationError.TOO_SHORT, validateUsername("abc"))
    @Test fun `too long`() = assertEquals(ValidationError.TOO_LONG, validateUsername("a".repeat(15)))
    @Test fun `has space`() = assertEquals(ValidationError.CONTAINS_WHITESPACE, validateUsername("ab cd"))
    @Test fun `valid boundaries`() {
        assertNull(validateUsername("abcd"))
        assertNull(validateUsername("a".repeat(14)))
    }
}

class PasswordValidatorTest {
    @Test
    fun `too short`() = assertEquals(ValidationError.TOO_SHORT, validatePassword("abc"))
    @Test fun `has space`() = assertEquals(ValidationError.CONTAINS_WHITESPACE, validatePassword("ab cd www"))
    @Test fun `valid boundaries`() {
        assertNull(validateUsername("abcdspsd"))
    }
}

class EmailValidatorTest {
    @Test
    fun `not in email format`() = assertEquals(ValidationError.INVALID_EMAIL, validateEmail("abc"))
    @Test fun `has space`() = assertEquals(ValidationError.CONTAINS_WHITESPACE, validateEmail("ab cd"))
    @Test fun `valid boundaries`() {
        assertNull(validateEmail("abcd@gm.co"))
    }
}
