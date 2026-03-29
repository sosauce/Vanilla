package com.sosauce.vanilla.data.sysui

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.sosauce.vanilla.MainActivity

@RequiresApi(Build.VERSION_CODES.N)
class QSTile : TileService() {

    override fun onClick() {
        super.onClick()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(pendingIntent)
        } else {
            val newIntent = Intent(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(newIntent)
        }
    }

}