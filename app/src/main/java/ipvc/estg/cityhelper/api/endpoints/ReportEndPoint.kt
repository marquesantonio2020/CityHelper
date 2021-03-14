package ipvc.estg.cityhelper.api.endpoints

import ipvc.estg.cityhelper.api.ReportData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReportEndPoint {
    @GET("/COMMOV_APIS/index.php/api/reports/user")
    fun getReports(): Call<List<ReportData>>

    @GET("/COMMOV_APIS/index.php/api/reports/user/{id}")
    fun getReportsOfUser(@Path("id") id:Int): Call<List<ReportData>>
}