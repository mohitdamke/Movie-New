package com.example.mymovie.onboarding.app_entry

import com.example.mymovie.onboarding.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppEntry(
    private val localUserManager : LocalUserManager
) {

      operator fun invoke() : Flow<Boolean> {
      return  localUserManager.readAppEntry()
    }
}