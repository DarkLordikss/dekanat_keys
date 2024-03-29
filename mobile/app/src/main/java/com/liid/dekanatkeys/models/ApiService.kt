package com.liid.dekanatkeys.models

import com.liid.dekanatkeys.models.user.LoginRequest
import com.liid.dekanatkeys.models.user.LoginResponse
import com.liid.dekanatkeys.models.user.LogoutResponse
import com.liid.dekanatkeys.models.user.RegistrationRequest
import com.liid.dekanatkeys.models.user.RegistrationResponse
import com.liid.dekanatkeys.models.user.UserInfo
import com.liid.dekanatkeys.models.user.UserProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDate

interface ApiService {
    @POST("user/login/")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
    @POST("user/register/")
    fun registrationUser(@Body request: RegistrationRequest): Call<RegistrationResponse>
    @POST("user/logout/")
    fun logoutUser(@Header("Authorization") token: String): Call<LogoutResponse>
    @GET("user/")
    fun fetchUserProfile(@Header("Authorization") token: String): Call<UserProfile>
    @GET("building/get-all-buildings")
    fun fetchBuildings() : Call<BuildingsResponse>
    @GET("building/get-classrooms-from-building")
    fun fetchClassrooms(@Query("building") building:Int): Call<ClassroomResponse>

    @GET("applications/show_with_status")
    fun fetchApplications(@Query("building") building: Int,
                          @Query("start_date") start_date : LocalDate,
                          @Query("end_date") end_date : LocalDate,
                          @Query("statuses") statuses: List<Int>,
                          @Query("classrooms") classrooms: List<Int>): Call<ApplicationsResponse>
    @POST("applications/create/")
    fun createApplication(@Body request: CreateApplicationRequest, @Header("Authorization") token: String): Call<MessageResponse>

    @GET("user/users/")
    fun fetchUsers(@Query("roles") roles: List<Int>, @Header("Authorization") token: String): Call<List<UserInfo>>

    @GET("applications/show_my")
    fun fetchMyApplications(@Query("start_date") start_date : LocalDate,
                          @Query("end_date") end_date : LocalDate,
                          @Query("statuses") statuses: List<Int>,
                            @Header("Authorization") token: String): Call<ApplicationsResponse>

    @POST("applications/transfer_key/")
    fun transferKey(@Query("application_id") application_id : String,
                    @Query("user_recipient_id") user_recipient_id : String,
                    @Header("Authorization") token: String): Call<MessageResponse>

    @GET("applications/show_concrete_application/")
    fun getApplication(@Query("application_id") application_id : String):Call<ApplicationWithDateStatus>

}
