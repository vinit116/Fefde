package club.potli

import android.app.Application
import club.potli.data.model.User
import club.potli.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = RealmConfiguration.Builder(setOf(User::class))
            .build()
        val realm = Realm.open(config)
    }
}