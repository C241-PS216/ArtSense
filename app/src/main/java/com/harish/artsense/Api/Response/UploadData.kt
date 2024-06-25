package com.harish.artsense.Api.Response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadData(
    val url : String,
    val nama : String
): Parcelable
