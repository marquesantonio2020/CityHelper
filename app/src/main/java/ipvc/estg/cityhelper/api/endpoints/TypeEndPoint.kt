package ipvc.estg.cityhelper.api.endpoints

import ipvc.estg.cityhelper.api.ReportData
import ipvc.estg.cityhelper.api.Type
import retrofit2.Call
import retrofit2.http.GET

interface TypeEndPoint {
    @GET("/COMMOV_APIS/index.php/api/problems")
    fun getTypesProblems(): Call<List<Type>>
}