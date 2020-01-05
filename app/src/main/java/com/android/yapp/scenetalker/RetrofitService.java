package com.android.yapp.scenetalker;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @POST("chat/save/")
    Call<JsonObject> saveChat(@Body ChatMessage chatMessage);
    @GET("chat/count/{drama_id}/{episode}/")
    Call<JsonObject> getCurrentCount(@Path("drama_id")String drama_id, @Path("episode")String episode);
    @POST("user/authenticate/")
    Call<JsonObject> getUser(@Body Token token);
    @GET("drama/")
    Call<JsonObject> getDramaList(@Query("page") int page);
    @GET("drama/")
    Call<JsonObject> getDramaList(@Query("onair") boolean onair,@Query("page")int page);
    @GET("drama/{drama_id}/each-episode/")
    Call<JsonArray> getDramaCount(@Path("drama_id")int drama_id);
    @POST("rest-auth/registration/")
    Call<JsonObject> signup(@Body User user);
    @POST("rest-auth/login/")
    Call<JsonObject> login(@Body User user);

    @Multipart
    @POST("feed/{feed_id}/post/")
    Call<JsonObject> writePostWithImage(@Part MultipartBody.Part image, @Part("content") RequestBody content , @Path("feed_id")int feed_id);

    @Multipart
    @POST("feed/{feed_id}/post/")
    Call<JsonObject> writePostOnlyContent(@Part("content") RequestBody content , @Path("feed_id")int feed_id);

    @GET("feed/{feed_id}/post/")
    Call<JsonArray> feed(@Path("feed_id")int feed_id,@Query("content") String content);

    @GET("feed/{feed_id}/post/")
    Call<JsonArray> getFeed(@Path("feed_id")int feed_id);

    @POST("feed/{feed_id}/post/{post_id}/like/")
    Call<JsonObject> setLike(@Path("feed_id") String feed_id,@Path("post_id") String post_id);

    @GET("feed/{feed_id}/post/{id}/")
    Call<JsonObject> getOneFeed(@Path("feed_id")String feed_id,@Path("id")String id);

    @GET("feed/{feed_id}/post/{id}/comment/")
    Call<JsonArray> getComment(@Path("feed_id")String feed_id,@Path("id")String id);

    @POST("feed/{feed_id}/post/{post_id}/comment/")
    Call<JsonObject> addComment(@Body PostInfo postInfo , @Path("feed_id")String feed_id, @Path("post_id")String post_id);

    @GET("user/posts/write/")
    Call<JsonArray> myWrite();

    @GET("user/posts/like/")
    Call<JsonArray> myLike();

    @GET("user/bookmark-best-drama/")
    Call<JsonObject> getUserBookmarkBestDrama();

    @GET("user/bookmark-dramas/")
    Call<JsonArray> getUserBookmarkDramas();

    @POST("user/{drama_id}/bookmark/")
    Call<JsonObject> toggleUserDramaBookmark(@Path("drama_id")String drama_id);

    @GET("user/recent-searches/")
    Call<JsonObject> getUserRecentSearches();

    @PUT("user/change/username/")
    Call<JsonObject> putUsername(@Body String username);

    @POST("rest-auth/password/change/")
    Call<JsonObject> changeUserPassword(@Body NewPassword newPassword);

    @HTTP(method = "DELETE", path = "user/recent-searches/", hasBody = true)
    Call<JsonObject> deleteUserRecentSearches(@Body SearchWordInfo search_word);

    @HTTP(method="DELETE",path="feed/{feed_id}/post/{id}/",hasBody = true)
    Call<JsonObject> deleteFeedPost(@Body FeedInfo feedinfo,@Path("feed_id")String feed_id,@Path("id")int id);

    @HTTP(method="DELETE",path="feed/{feed_id}/post/{post_id}/comment/",hasBody = true)
    Call<JsonObject> deleteFeedPostComment(@Body PostInfo postInfo,@Path("feed_id")String feed_id,@Path("post_id")String post_id,int id);//피드 게시물 댓글
}