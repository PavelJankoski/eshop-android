package mk.ukim.finki.eshop.ui.addressbook.enteraddress

import androidx.lifecycle.MutableLiveData
import mk.ukim.finki.eshop.util.validators.LiveDataValidator
import mk.ukim.finki.eshop.util.validators.LiveDataValidatorResolver
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.CITY_REQUIRED
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.NAME_REQUIRED
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.POSTAL_CODE_REQUIRED
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.STREET_REQUIRED
import mk.ukim.finki.eshop.util.validators.ValidationMessagesAndRules.Companion.SURNAME_REQUIRED

class EnterAddressFormValidator {

    val streetLiveData = MutableLiveData<String>()
    val streetValidator = LiveDataValidator(streetLiveData).apply {
        addRule(STREET_REQUIRED) { it.isNullOrBlank() }
    }

    val cityLiveData = MutableLiveData<String>()
    val cityValidator = LiveDataValidator(cityLiveData).apply {
        addRule(CITY_REQUIRED) { it.isNullOrBlank() }
    }

    val countryLiveData = MutableLiveData<String>()
    val countryValidator = LiveDataValidator(countryLiveData).apply {
        addRule(CITY_REQUIRED) { it.isNullOrBlank() }
    }

    val postalCodeLiveData = MutableLiveData<String>()
    val postalCodeValidator = LiveDataValidator(postalCodeLiveData).apply {
        addRule(POSTAL_CODE_REQUIRED) { it.isNullOrBlank() }
    }

    val streetNumberLiveData = MutableLiveData<String>()

    val isDefaultLiveData = MutableLiveData<Boolean>(false)


    fun validateForm(): Boolean {
        val validators = listOf(
            streetValidator,
            cityValidator,
            countryValidator,
            postalCodeValidator
        )
        val validatorResolver = LiveDataValidatorResolver(validators)
        return validatorResolver.isValid()
    }
}