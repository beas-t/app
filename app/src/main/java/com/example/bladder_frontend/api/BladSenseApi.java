package com.example.bladder_frontend.api;

import com.example.bladder_frontend.api.models.*;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

public interface BladSenseApi {
    // --- Auth Endpoints ---
    @POST("api/login/")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("api/signup/")
    Call<AuthResponse> signUp(@Body Map<String, String> data);

    @POST("api/logout/")
    Call<Map<String, String>> logout(@Body Map<String, String> refresh);

    @GET("api/profile/")
    Call<Map<String, Object>> getProfile();

    // --- Dashboard & Analytics ---
    @GET("api/home-dashboard/")
    Call<DashboardData> getHomeDashboard();

    @GET("api/analytics/")
    Call<AnalyticsData> getAnalytics();

    // --- Patients ---
    @GET("api/patients/")
    Call<List<Patient>> getPatients(@Query("q") String query, @Query("show_archived") Boolean showArchived);

    @POST("api/patients/")
    Call<Patient> createPatient(@Body Map<String, Object> data);

    @PATCH("api/patients/{id}/")
    Call<Patient> updatePatient(@Path("id") int id, @Body Map<String, Object> data);

    @POST("api/patients/{id}/archive/")
    Call<Map<String, String>> archivePatient(@Path("id") int id);

    @POST("api/patients/{id}/unarchive/")
    Call<Map<String, String>> unarchivePatient(@Path("id") int id);

    // --- Scan Reports ---
    @GET("api/reports/")
    Call<List<ScanReport>> getReports(@Query("patient") Integer patientId, @Query("q") String query);

    @Multipart
    @POST("api/save-report/")
    Call<ScanReport> saveReport(
            @Part("patient_id") RequestBody patientId,
            @Part("notes") RequestBody notes,
            @Part MultipartBody.Part image
    );

    // --- Appointments ---
    @GET("api/appointments/")
    Call<List<Appointment>> getAppointments(@Query("date") String date);

    @POST("api/appointments/")
    Call<Appointment> createAppointment(@Body Map<String, Object> data);

    // --- Team Endpoints ---
    @GET("api/team-members/")
    Call<List<TeamMember>> getTeamMembers(@Query("category") String category);

    @POST("api/invitations/")
    Call<Map<String, String>> sendInvitation(@Query("email") String email, @Query("fullName") String fullName,
            @Query("role") String role);

    // --- Training Endpoints ---
    @GET("api/training/summary/")
    Call<TrainingSummary> getTrainingSummary();

    @GET("api/training/")
    Call<List<TrainingModule>> getTrainingModules();

    @POST("api/training/{id}/complete/")
    Call<Map<String, String>> completeModule(@Path("id") int moduleId);

    // --- Notifications ---
    @GET("api/notifications/")
    Call<List<Notification>> getNotifications();

    @POST("api/data-sync/")
    Call<ResponseBody> syncData();

    @POST("api/notifications/{id}/mark_as_read/")
    Call<Map<String, String>> markNotificationRead(@Path("id") int id);

    // --- Backups ---
    @GET("api/equipment/")
    Call<List<Equipment>> getEquipment();
    @GET("api/backups/")
    Call<List<Backup>> getBackupHistory();

    @POST("api/backups/")
    Call<Backup> performBackup();

    @GET("api/backup-settings/")
    Call<BackupSettings> getBackupSettings();

    @PATCH("api/backup-settings/")
    Call<BackupSettings> updateBackupSettings(@Body BackupSettings settings);

    // --- App Info ---
    @GET("api/app-info/")
    Call<AppInfo> getAppInfo();

    // --- Profile Update ---
    @Multipart
    @PATCH("api/profile/")
    Call<Map<String, Object>> updateFullProfile(
            @Part("fullName") RequestBody fullName,
            @Part("email") RequestBody email,
            @Part("specialty") RequestBody specialty,
            @Part MultipartBody.Part profile_picture
    );

    @Multipart
    @PATCH("api/profile/")
    Call<Map<String, Object>> uploadProfilePicture(@Part MultipartBody.Part image);

    // --- Feedback ---
    @POST("api/feedback/")
    Call<Feedback> submitFeedback(@Body Feedback feedback);
}
