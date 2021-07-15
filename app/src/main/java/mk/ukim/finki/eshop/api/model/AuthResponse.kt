package mk.ukim.finki.eshop.api.model

data class AuthResponse (
    private val jwt: String,
    private val userId: Long
    ) {
    fun getToken(): String {
        return jwt
    }
}