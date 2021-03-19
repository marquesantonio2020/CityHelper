package ipvc.estg.cityhelper.api.endpoints

import ipvc.estg.cityhelper.api.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserEndPoint {
    @FormUrlEncoded
    @POST("/COMMOV_APIS/index.php/api/user/login")
    fun userLogin(@Field("username") username: String?,
    @Field("password") password: String?): Call<User>
}