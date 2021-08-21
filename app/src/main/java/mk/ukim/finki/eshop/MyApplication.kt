package mk.ukim.finki.eshop

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import mk.ukim.finki.eshop.util.Constants.Companion.STRIPE_PUBLISHABLE_KEY

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this);
        PaymentConfiguration.init(
            applicationContext,
            STRIPE_PUBLISHABLE_KEY
        )
    }
}