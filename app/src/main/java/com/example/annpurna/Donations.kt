package com.example.annpurna

data class Donations(
    val Dname: String, // Donor name (from local or Firebase)
    val Dcontact: String, // Donor contact (from local or Firebase)
    val Dcity: String, // Donor city
    val foodItem: String, // Name of the food item
    val description: String, // Description of the food item
    val expiryDate: String, // Expiry date
    val quantityType: String, // e.g., kg, litres, net
    val quantity: String, // Quantity of food
    val perishable: String, // Perishable or other options
    val Rname: String? = null, // Receiver name (to be filled later)
    val Rcontact: String? = null, // Receiver contact (to be filled later)
    val Accepted: Int = 0, // Default is 0 (not accepted)
    val Vname: String? = null, // Volunteer name (to be filled later)
    val Vcontact: String? = null, // Volunteer contact (to be filled later)
    val Delivered: Boolean? = null // Default is null
)
