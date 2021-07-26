package mk.ukim.finki.eshop.util.validators

class LiveDataValidatorResolver(private val validators: List<LiveDataValidator>) {
    fun isValid(): Boolean {
        var validForm = true
        for (validator in validators) {
            if (!validator.isValid())
                validForm = false
        }
        return validForm
    }
}