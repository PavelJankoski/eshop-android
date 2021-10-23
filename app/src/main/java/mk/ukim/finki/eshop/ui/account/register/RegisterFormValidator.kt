package mk.ukim.finki.eshop.ui.account.register

import androidx.lifecycle.MutableLiveData
import mk.ukim.finki.eshop.util.validators.LiveDataValidator
import mk.ukim.finki.eshop.util.validators.LiveDataValidatorResolver
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.EMAIL_REGEX
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.EMAIL_REQUIRED
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.EMAIL_VALID
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.NAME_REQUIRED
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.PASSWORD_REGEX
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.PASSWORD_REQUIRED
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.PASSWORD_VALID
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.SURNAME_REQUIRED

class RegisterFormValidator {

    val nameLiveData = MutableLiveData<String>()
    val nameValidator = LiveDataValidator(nameLiveData).apply {
        addRule(NAME_REQUIRED) { it.isNullOrBlank() }
    }

    val surnameLiveData = MutableLiveData<String>()
    val surnameValidator = LiveDataValidator(surnameLiveData).apply {
        addRule(SURNAME_REQUIRED) { it.isNullOrBlank() }
    }

    val emailLiveData = MutableLiveData<String>()
    val emailValidator = LiveDataValidator(emailLiveData).apply {
        addRule(EMAIL_REQUIRED) { it.isNullOrBlank() }
        addRule(EMAIL_VALID) { !EMAIL_REGEX.matcher(it ?: "").matches() }
    }

    val passwordLiveData = MutableLiveData<String>()
    val passwordValidator = LiveDataValidator(passwordLiveData).apply {
        addRule(PASSWORD_REQUIRED) { it.isNullOrBlank() }
        addRule(PASSWORD_VALID) { it?.matches(PASSWORD_REGEX) == false }
    }

    fun validateForm(): Boolean {
        val validators = listOf(
            nameValidator,
            surnameValidator,
            emailValidator,
            passwordValidator
        )
        val validatorResolver = LiveDataValidatorResolver(validators)
        return validatorResolver.isValid()
    }
}