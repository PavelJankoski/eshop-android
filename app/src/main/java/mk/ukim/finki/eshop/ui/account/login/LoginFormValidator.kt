package mk.ukim.finki.eshop.ui.account.login

import androidx.lifecycle.MutableLiveData
import mk.ukim.finki.eshop.util.validators.LiveDataValidator
import mk.ukim.finki.eshop.util.validators.LiveDataValidatorResolver
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules

class LoginFormValidator {

    val emailLiveData = MutableLiveData<String>()
    val emailValidator = LiveDataValidator(emailLiveData).apply {
        addRule(ValidationMessagesAndRules.EMAIL_REQUIRED) { it.isNullOrBlank() }
        addRule(ValidationMessagesAndRules.EMAIL_VALID) { !ValidationMessagesAndRules.EMAIL_REGEX.matcher(it ?: "").matches() }
    }

    val passwordLiveData = MutableLiveData<String>()
    val passwordValidator = LiveDataValidator(passwordLiveData).apply {
        addRule(ValidationMessagesAndRules.PASSWORD_REQUIRED) { it.isNullOrBlank() }
    }

    fun validateForm(): Boolean {
        val validators = listOf(
            emailValidator,
            passwordValidator
        )
        val validatorResolver = LiveDataValidatorResolver(validators)
        return validatorResolver.isValid()
    }
}