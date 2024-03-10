package com.liid.dekanatkeys.models

import com.liid.dekanatkeys.models.user.LoginRequest
import com.liid.dekanatkeys.models.user.LoginResponse
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
    fun createApplication(@Body request: CreateApplicationRequest, @Header("Authorization") token: String): Call<CreateApplicationResponse>

    @GET("user/users/")
    fun fetchUsers(@Query("roles") roles: List<Int>, @Header("Authorization") token: String): Call<List<UserInfo>>

}