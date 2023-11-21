package com.santansarah.scan.utils.logging

import android.util.Log
import timber.log.Timber

class ReleaseTree: Timber.Tree() {

    companion object {
        const val Priority = "priority"
        const val Tag = "tag"
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)

        if (priority == Log.ERROR) {

        }
    }
}