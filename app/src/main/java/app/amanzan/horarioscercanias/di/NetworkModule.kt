package app.amanzan.horarioscercanias.di

import android.util.Log
import app.amanzan.horarioscercanias.data.api.TrainScheduleApi
import app.amanzan.horarioscercanias.data.repository.TrainScheduleRepositoryImpl
import app.amanzan.horarioscercanias.domain.repository.TrainScheduleRepository
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindTrainScheduleRepository(
        trainScheduleRepositoryImpl: TrainScheduleRepositoryImpl
    ): TrainScheduleRepository

    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson = Gson()

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                Log.d("OkHttp", message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    Log.d("NetworkModule", "Original request: ${original.url}")
                    Log.d("NetworkModule", "Original headers: ${original.headers}")
                    
                    val request = original.newBuilder()
                        .header("Accept", "application/json, text/plain, */*")
                        .header("Accept-Language", "es-ES,es;q=0.9")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .header("Origin", "https://horarios.renfe.com")
                        .header("Referer", "https://horarios.renfe.com/cer/hjcer300.jsp?NUCLEO=10&CP=NO&I=s")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                        .method(original.method, original.body)
                        .build()
                    
                    Log.d("NetworkModule", "Modified request: ${request.url}")
                    Log.d("NetworkModule", "Modified headers: ${request.headers}")
                    Log.d("NetworkModule", "Request body: ${request.body}")
                    
                    val response = chain.proceed(request)
                    Log.d("NetworkModule", "Response code: ${response.code}")
                    Log.d("NetworkModule", "Response headers: ${response.headers}")
                    Log.d("NetworkModule", "Response body: ${response.peekBody(Long.MAX_VALUE).string()}")
                    
                    response
                }
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://horarios.renfe.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        @Provides
        @Singleton
        fun provideTrainScheduleApi(retrofit: Retrofit): TrainScheduleApi {
            return retrofit.create(TrainScheduleApi::class.java)
        }
    }
} 