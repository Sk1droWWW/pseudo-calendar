package int20h.troipsa.pseudocalendar.data.network

import int20h.troipsa.pseudocalendar.data.network.models.UserApiModel
import retrofit2.http.GET

interface ApiService {

    @GET("user")
    suspend fun getUser(): UserApiModel

}