package mk.ukim.finki.eshop.api.dto.request

import com.google.gson.annotations.SerializedName

data class RegisterDto(
    @SerializedName("firstName")
    private val name: String,
    @SerializedName("lastName")
    private val surname: String,
    private val email: String,
    private val phoneNumber: String,
    private val password: String
)
