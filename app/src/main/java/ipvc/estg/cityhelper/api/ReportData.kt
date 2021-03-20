package ipvc.estg.cityhelper.api

import java.sql.Blob

data class ReportData(
    val report: Report,
    val user: String,
    val city: City
)

data class Report (
    val id: Int,
    val report_title: String,
    val report_description: String,
    val report_street: String,
    val report_location_latitude: Double,
    val report_location_longitude: Double,
    val problem_picture: Blob,
    val report_isResolved: Int
)

data class City (
    val id: Int,
    val city_lat: Double,
    val city_long: Double,
    val city_name: String
)