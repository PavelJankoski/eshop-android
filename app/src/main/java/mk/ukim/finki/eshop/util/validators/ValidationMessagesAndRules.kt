package mk.ukim.finki.eshop.util.validators

import android.util.Patterns

class ValidationMessagesAndRules {
    companion object {

        //MESSAGES
        const val USERNAME_REQUIRED = "Username is required"
        const val PASSWORD_REQUIRED = "Password is required"
        const val PASSWORD_VALID ="Password must contain minimum six characters, at least one lowercase letter and one number"
        const val NAME_REQUIRED = "Name is required"
        const val SURNAME_REQUIRED = "Surname is required"
        const val EMAIL_REQUIRED = "Email is required"
        const val EMAIL_VALID = "The entered email is not valid"
        const val STREET_REQUIRED = "Street is required"
        const val CITY_REQUIRED = "City is required"
        const val COUNTRY_REQUIRED = "Country is required"
        const val POSTAL_CODE_REQUIRED = "Postal code is required"

        // REGEXES
        val EMAIL_REGEX = Patterns.EMAIL_ADDRESS
        val PASSWORD_REGEX = """^(?=.*[a-z])(?=.*\d).{6,}$""".toRegex()

    }
}