package int20h.troipsa.pseudocalendar.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import int20h.troipsa.pseudocalendar.data.local.MainDatabase
import int20h.troipsa.pseudocalendar.data.network.ApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideMainDatabase(@ApplicationContext context: Context): MainDatabase {
        return MainDatabase.createInstance(context)
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val okHttpClient = createOkHttpClient()

        return createRetrofitService(okHttpClient, "BuildConfig.BASE_URL")
    }

    @OptIn(ExperimentalSerializationApi::class)
    private inline fun <reified T> createRetrofitService(
        okHttpClient: OkHttpClient,
        baseUrl: String,
    ): T {
        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()

        return retrofit.create(T::class.java)
    }

    private fun createOkHttpClient(action: (OkHttpClient.Builder.() -> Unit)? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .apply {
                action?.invoke(this)
            }
            .build()
    }

}
