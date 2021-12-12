package mk.ukim.finki.eshop.ui.userinfo

import androidx.lifecycle.MutableLiveData
import mk.ukim.finki.eshop.util.validators.LiveDataValidator
import mk.ukim.finki.eshop.util.validators.LiveDataValidatorResolver
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.NAME_REQUIRED
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.SURNAME_REQUIRED

class UserInfoFormValidator {

    val nameLiveData = MutableLiveData<String>()
    val nameValidator = LiveDataValidator(nameLiveData).apply {
        addRule(NAME_REQUIRED) { it.isNullOrBlank() }
    }

    val surnameLiveData = MutableLiveData<String>()
    val surnameValidator = LiveDataValidator(surnameLiveData).apply {
        addRule(SURNAME_REQUIRED) { it.isNullOrBlank() }
    }

    val phoneNumberLiveData = MutableLiveData<String>()

    fun validateForm(): Boolean {
        val validators = listOf(
            nameValidator,
            surnameValidator
        )
        val validatorResolver = LiveDataValidatorResolver(validators)
        return validatorResolver.isValid()
    }
}