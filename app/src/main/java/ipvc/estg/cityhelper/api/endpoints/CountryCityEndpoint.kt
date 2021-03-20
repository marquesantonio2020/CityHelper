package ipvc.estg.cityhelper.api.endpoints

import ipvc.estg.cityhelper.api.CountryCity
import retrofit2.Call
import retrofit2.http.GET

interface CountryCityEndpoint{
    @GET("/COMMOV_APIS/index.php/api/countrycities")
    fun getAllCities(): Call<List<CountryCity>>

}