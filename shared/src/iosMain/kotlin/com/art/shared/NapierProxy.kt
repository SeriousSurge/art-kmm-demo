package com.art.shared

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object NapierProxy {
    fun debugBuild() {
        Napier.base(DebugAntilog())
    }
}