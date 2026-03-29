package com.elschuyler.vhatsai

import android.app.Application
import com.elschuyler.vhatsai.db.VhatsaiDatabase
import com.elschuyler.vhatsai.repository.VhatsaiRepository

class VhatsaiApplication : Application() {
    private val database: VhatsaiDatabase by lazy { VhatsaiDatabase.getDatabase(this) }
    val repository: VhatsaiRepository by lazy { VhatsaiRepository(database) }
}
