package com.samplural.fivecrownsscorekeeper

import android.app.Application
import com.samplural.fivecrownsscorekeeper.data.AppContainer
import com.samplural.fivecrownsscorekeeper.data.AppDataContainer

class PlayersApplication : Application() {
    lateinit var container: AppContainer
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}