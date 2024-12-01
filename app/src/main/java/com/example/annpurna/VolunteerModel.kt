package com.example.annpurna

import android.os.Parcel
import android.os.Parcelable

data class VolunteerModel(
    var Dname: String = "",         // Donor name
    var Dcontact: String = "",      // Donor contact
    var Saddress: String? = "",     // Source address (Donor's address)
    var Rname: String = "",         // Receiver name
    var Rcontact: String = "",      // Receiver contact
    var Daddress: String? = "",     // Destination address (Receiver's address)
    var Delivered: String? = "",    // Delivery status (0 = not delivered, 1 = delivered)
    var expiryDate: String = "",    // Expiry date of the food item
    var quantityType: String = "",  // Type of quantity (kg, liters, etc.)
    var perishable: String = "",    // Perishable status (Yes/No)
    var quantity: String = "",      // Quantity of the food item
    var foodItem: String = "",      // Name of the food item
    var accepted: Int? = 0,         // Accepted status (-1 = expired, 0 = not taken, 1 = taken)
    var description: String = "",   // Short description of the food item
    var dcity: String = "",         // Donor's city
    var Raddress: String? = ""      // Receiver address (new field for destination)
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        Dname = parcel.readString() ?: "",
        Dcontact = parcel.readString() ?: "",
        Saddress = parcel.readString() ?: "",
        Rname = parcel.readString() ?: "",
        Rcontact = parcel.readString() ?: "",
        Daddress = parcel.readString() ?: "",
        Delivered = parcel.readString() ?: "",
        expiryDate = parcel.readString() ?: "",
        quantityType = parcel.readString() ?: "",
        perishable = parcel.readString() ?: "",
        quantity = parcel.readString() ?: "",
        foodItem = parcel.readString() ?: "",
        accepted = parcel.readInt(),
        description = parcel.readString() ?: "",
        dcity = parcel.readString() ?: "",
        Raddress = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Dname)
        parcel.writeString(Dcontact)
        parcel.writeString(Saddress)
        parcel.writeString(Rname)
        parcel.writeString(Rcontact)
        parcel.writeString(Daddress)
        parcel.writeString(Delivered)
        parcel.writeString(expiryDate)
        parcel.writeString(quantityType)
        parcel.writeString(perishable)
        parcel.writeString(quantity)
        parcel.writeString(foodItem)
        accepted?.let { parcel.writeInt(it) }
        parcel.writeString(description)
        parcel.writeString(dcity)
        parcel.writeString(Raddress)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<VolunteerModel> {
        override fun createFromParcel(parcel: Parcel): VolunteerModel {
            return VolunteerModel(parcel)
        }

        override fun newArray(size: Int): Array<VolunteerModel?> {
            return arrayOfNulls(size)
        }
    }
}
