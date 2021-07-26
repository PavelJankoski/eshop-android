package mk.ukim.finki.eshop.api.model

data class User(
    val createDate: String,
    val email: String,
    val id: Int,
    val image: ImageX,
    val name: String,
    val roles: List<Role>,
    val surname: String,
    val userName: String
)