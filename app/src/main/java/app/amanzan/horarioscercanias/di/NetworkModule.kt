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
                        .header("Accept-Language", "en-US,en;q=0.5")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .header("Origin", "https://horarios.renfe.com")
                        .header("Referer", "https://horarios.renfe.com/cer/hjcer300.jsp?NUCLEO=10&CP=NO&I=s")
                        .header("Sec-Fetch-Dest", "empty")
                        .header("Sec-Fetch-Mode", "cors")
                        .header("Sec-Fetch-Site", "same-origin")
                        .header("Sec-GPC", "1")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                        .header("sec-ch-ua", "\"Chromium\";v=\"136\", \"Brave\";v=\"136\", \"Not.A/Brand\";v=\"99\"")
                        .header("sec-ch-ua-mobile", "?0")
                        .header("sec-ch-ua-platform", "\"Windows\"")
                        .addHeader("Cookie", "rxVisitor=1746797998089HF2PDQHFBG74EJIF2EVMDTRQCK4P89F8; dtCookie=v_4_srv_8_sn_KG74AQIFMK8VSN6CETOG3497EIGU1LHN_perc_100000_ol_0_mul_1_app-3Aea7c4b59f27d43eb_0; JSESSIONID=0000P544xYJ63HHYK6hCwdwDgoF:1fiumv9g6; dtSa=-; rxvt=1747054520053|1747052720003; dtPC=$452720001_531h-vNRMWKKLONTQROJSMVAQSRHMWKFCGCUAV-0e0")
                        .method(original.method, original.body)
                        .build()
                    
                    Log.d("NetworkModule", "Modified request: ${request.url}")
                    Log.d("NetworkModule", "Modified headers: ${request.headers}")
                    
                    chain.proceed(request)
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