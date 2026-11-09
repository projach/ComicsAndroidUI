package com.projach.comics_tracker.data.security

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    private fun payload(token: String): JSONObject? = try {
        val part = token.split(".")[1]
        val bytes = Base64.decode(part, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        JSONObject(String(bytes, Charsets.UTF_8))
    } catch (e: Exception) {
        null
    }

    fun getClaim(token: String, name: String): String? =
        payload(token)?.optString(name)?.takeIf { it.isNotBlank() }

    fun isExpired(token: String): Boolean {
        val exp = payload(token)?.optLong("exp", 0L) ?: return true
        if (exp == 0L) return false
        return System.currentTimeMillis() / 1000 >= exp
    }
}