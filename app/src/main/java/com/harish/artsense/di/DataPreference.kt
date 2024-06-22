package com.harish.artsense.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.Api.Response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class DataPreference private constructor(
    private val preference: DataStore<Preferences>
) {

    suspend fun saveSession(user: LoginResponse){
        preference.edit { preferences ->
            preferences[USER_ID] = user.userid.toString()
            preferences[TOKEN_KEY] = user.token.toString()
            preferences[NAME] = user.username.toString()
            preferences[LOGIN] = true
            preferences[IS_GUEST] = false
        }
    }

    fun getSession(): Flow<LoginData> {
        return preference.data.map {preference->
            LoginData(
                preference[NAME] ?:"",
                preference[TOKEN_KEY] ?:"",
                preference[USER_ID] ?:"",
                preference[LOGIN] ?: false,
                preference[IS_GUEST] ?: false
                )
        }
    }

     suspend fun guestLogin(user: LoginData){
        preference.edit { preferences ->
            preferences[USER_ID] = user.userId
            preferences[TOKEN_KEY] = user.token
            preferences[NAME] = user.name
            preferences[LOGIN] = true
            preferences[IS_GUEST] = true
        }
    }
    suspend fun logout() {
        preference.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: DataPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = DataPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
        private val USER_ID = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME = stringPreferencesKey("name")
        private val LOGIN = booleanPreferencesKey("login")
        private val IS_GUEST = booleanPreferencesKey("is_guest")

    }
}