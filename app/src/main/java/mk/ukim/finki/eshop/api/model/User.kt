package mk.ukim.finki.eshop.api.model

data class User(
    val createDate: String,
    val email: String,
    val id: Int,
    val image: UserProfilePicture,
    val name: String,
    val surname: String,
    val userName: String
)