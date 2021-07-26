package mk.ukim.finki.eshop.ui.account.login

import androidx.lifecycle.MutableLiveData
import mk.ukim.finki.eshop.util.validators.LiveDataValidator
import mk.ukim.finki.eshop.util.validators.LiveDataValidatorResolver
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules

class LoginFormValidator {

    val usernameLiveData = MutableLiveData<String>()
    val usernameValidator = LiveDataValidator(usernameLiveData).apply {
        addRule(ValidationMessagesAndRules.USERNAME_REQUIRED) { it.isNullOrBlank() }
    }

    val passwordLiveData = MutableLiveData<String>()
    val passwordValidator = LiveDataValidator(passwordLiveData).apply {
        addRule(ValidationMessagesAndRules.PASSWORD_REQUIRED) { it.isNullOrBlank() }
    }

    fun validateForm(): Boolean {
        val validators = listOf(usernameValidator, passwordValidator)
        val validatorResolver = LiveDataValidatorResolver(validators)
        return validatorResolver.isValid()
    }
}