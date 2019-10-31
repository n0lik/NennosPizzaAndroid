package com.test.nennospizzaandroid.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PizzaItem(

    @field:SerializedName("name")
    var name: String = "",

    @field:SerializedName("ingredients")
    var ingredients: ArrayList<Long> = ArrayList(),

    @field:SerializedName("imageUrl")
    var imageUrl: String? = null

): Parcelable