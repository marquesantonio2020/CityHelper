package ipvc.estg.cityhelper.api.endpoints

import ipvc.estg.cityhelper.api.ServerResponse
import ipvc.estg.cityhelper.api.ReportData
import retrofit2.Call
import retrofit2.http.*

interface ReportEndPoint {
    @GET("/COMMOV_APIS/index.php/api/reports/user")
    fun getReports(): Call<List<ReportData>>

    @GET("/COMMOV_APIS/index.php/api/reports/user/{id}")
    fun getReportsOfUser(@Path("id") id:Int): Call<List<ReportData>>

    @GET("/COMMOV_APIS/index.php/api/report/{id}")
    fun getSingleReport(@Path("id") id: Int): Call<ReportData>

    @FormUrlEncoded
    @POST("/COMMOV_APIS/index.php/api/report/new_report")
    fun newReport(@Field("title") title: String?,
                  @Field("description") description: String?,
                  @Field("report_lat") lat: Double?,
                  @Field("report_long") long: Double?,
                  @Field("report_street") street: String,
                  @Field("picture") picture: String,
                  @Field("userId") userId: Int,
                  @Field("cityId") cityId: Int,
                  @Field("typeId") typeId: Int): Call<ServerResponse>

    @FormUrlEncoded
    @POST("/COMMOV_APIS/index.php/api/report/update_report")
    fun updateReport(@Field("title") title: String?,
                  @Field("description") description: String?,
                  @Field("report_street") street: String,
                  @Field("picture") picture: String,
                  @Field("userId") userId: Int,
                  @Field("cityId") cityId: Int,
                  @Field("typeId") typeId: Int,
                  @Field("reportId") reportId: Int): Call<ServerResponse>

    @FormUrlEncoded
    @POST("/COMMOV_APIS/index.php/api/report/delete")
    fun deleteReport(@Field("id") id: Int): Call<ServerResponse>

}