package mk.ukim.finki.eshop.util.validators

import android.util.Patterns

class ValidationMessagesAndRules {
    companion object {

        //MESSAGES
        const val USERNAME_REQUIRED = "Username is required"
        const val PASSWORD_REQUIRED = "Password is required"
        const val PASSWORD_VALID ="Password must contain minimum eight and maximum 10 characters, at least one uppercase letter, one lowercase letter, one number and one special character"
        const val NAME_REQUIRED = "Name is required"
        const val SURNAME_REQUIRED = "Surname is required"
        const val EMAIL_REQUIRED = "Email is required"
        const val EMAIL_VALID = "The entered email is not valid"

        // REGEXES
        val EMAIL_REGEX = Patterns.EMAIL_ADDRESS
        val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{8,10}""".toRegex()

    }
}