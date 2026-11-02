package com.projach.comics_tracker.ui.validation

import androidx.annotation.StringRes
import com.projach.comics_tracker.R
import com.projach.comics_tracker.domain.validation.CommonValidationError
import com.projach.comics_tracker.domain.validation.EmailValidationError
import com.projach.comics_tracker.domain.validation.PasswordValidationError
import com.projach.comics_tracker.domain.validation.UsernameValidationError

@StringRes
private fun CommonValidationError.toStringRes(): Int? = when(this){
    CommonValidationError.ContainsWhitespaces -> R.string.register_user_contains_whitespace_error
    CommonValidationError.Empty -> null
}

@StringRes
fun EmailValidationError.toStringRes(): Int? = when(this){
    is EmailValidationError.Common -> error.toStringRes()
    EmailValidationError.InvalidEmail -> R.string.register_user_email_error
    EmailValidationError.Conflict -> R.string.register_user_email_conflict
}

@StringRes
fun UsernameValidationError.toStringRes(): Int? = when(this){
    is UsernameValidationError.Common -> error.toStringRes()
    UsernameValidationError.TooLong -> R.string.register_user_username_is_too_long
    UsernameValidationError.TooShort -> R.string.register_user_username_is_too_short
    UsernameValidationError.Conflict -> R.string.register_user_username_conflict
}

@StringRes
fun PasswordValidationError.toStringRes(): Int? = when(this){
    is PasswordValidationError.Common -> error.toStringRes()
    PasswordValidationError.TooShort -> R.string.register_user_password_is_too_short
}