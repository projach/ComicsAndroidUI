package com.projach.comics_tracker.domain.validation

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class UsernameValidatorTest {
    @Test
    fun `too short`() = assertEquals(UsernameValidationError.TooShort, validateUsername("abc"))

    @Test
    fun `too long`() =
        assertEquals(UsernameValidationError.TooLong, validateUsername("a".repeat(15)))

    @Test
    fun `has space`() = assertEquals(
        UsernameValidationError.Common(CommonValidationError.ContainsWhitespaces),
        validateUsername("ab cd")
    )
    @Test fun `valid boundaries`() {
        assertNull(validateUsername("abcd"))
        assertNull(validateUsername("a".repeat(14)))
    }
}

class PasswordValidatorTest {
    @Test
    fun `too short`() = assertEquals(PasswordValidationError.TooShort, validatePassword("abc"))

    @Test
    fun `has space`() = assertEquals(
        PasswordValidationError.Common(CommonValidationError.ContainsWhitespaces),
        validatePassword("ab cd www")
    )
    @Test fun `valid boundaries`() {
        assertNull(validateUsername("abcdspsd"))
    }
}

class EmailValidatorTest {
    @Test
    fun `not in email format`() =
        assertEquals(EmailValidationError.InvalidEmail, validateEmail("abc"))

    @Test
    fun `has space`() = assertEquals(
        EmailValidationError.Common(CommonValidationError.ContainsWhitespaces),
        validateEmail("ab cd")
    )
    @Test fun `valid boundaries`() {
        assertNull(validateEmail("abcd@gm.co"))
    }
}
