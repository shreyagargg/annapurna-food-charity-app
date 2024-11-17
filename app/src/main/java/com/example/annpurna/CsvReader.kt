package com.example.annpurna

import android.content.Context
import com.opencsv.CSVReader
import java.io.InputStreamReader

data class Person(
    val state: String,
    val district: String,
    val location: String,
    val pincode: String
)


class CsvReader {

    // Function to read CSV from raw folder and return a list of Person objects
    fun readCSVFromRaw(context: Context, rawResId: Int): List<Person> {
        val personList = mutableListOf<Person>()

        // Read the CSV file from raw resources
        val inputStream = context.resources.openRawResource(rawResId)
        val reader = CSVReader(InputStreamReader(inputStream))

        // Skip the header line
        reader.readNext()

        // Read each row in the CSV
        var line: Array<String>?
        while (reader.readNext().also { line = it } != null) {
            // Assuming CSV structure: State, District, Location, Pincode
            val state = line!![0]  // State is the first column
            val district = line!![1]  // District is the second column
            val location = line!![2]  // Location is the third column
            val pincode = line!![3]  // Pincode is the fourth column

            // Create Person object and add to the list
            val person = Person(state, district, location, pincode)
            personList.add(person)
        }
        reader.close()

        return personList
    }
}

