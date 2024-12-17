package com.hiremarknolan.art

import android.app.Application
import com.art.shared.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class ArtApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
        Napier.base(DebugAntilog())
    }
}
