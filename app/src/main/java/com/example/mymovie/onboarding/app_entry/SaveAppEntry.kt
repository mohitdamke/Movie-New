package com.example.mymovie.onboarding.app_entry

import com.example.mymovie.onboarding.LocalUserManager

class SaveAppEntry(
    private val localUserManager : LocalUserManager
) {

    suspend  operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}