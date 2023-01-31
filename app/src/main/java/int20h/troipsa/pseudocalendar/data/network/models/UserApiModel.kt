package int20h.troipsa.pseudocalendar.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    @SerialName("user_id")
    val id: Int,
    @SerialName("user_name")
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String,
)
