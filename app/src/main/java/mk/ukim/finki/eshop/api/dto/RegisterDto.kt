package mk.ukim.finki.eshop.api.dto

data class RegisterDto(
    private val name: String,
    private val surname: String,
    private val email: String,
    private val username: String,
    private val password: String
)
