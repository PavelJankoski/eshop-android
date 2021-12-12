package mk.ukim.finki.eshop.api.dto.response

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("access_token")
    val token: String,
    val userId: Long,
    val email: String
)

