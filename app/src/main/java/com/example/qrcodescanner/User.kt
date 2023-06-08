package com.example.qrcodescanner

import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: String = "",
    val name: String= "",
    val email: String="",
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!

    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(email)



    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
data class QrDetail(
    val qrDetails: String = ""
): Parcelable {
    constructor(parcel: Parcel) :
            this(parcel.readString()!!) {
        parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(qrDetails)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QrDetail> {
        override fun createFromParcel(parcel: Parcel): QrDetail {
            return QrDetail(parcel)
        }

        override fun newArray(size: Int): Array<QrDetail?> {
            return arrayOfNulls(size)
        }
    }
}
