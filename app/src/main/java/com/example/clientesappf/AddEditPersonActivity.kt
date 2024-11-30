package com.example.clientesappf

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clientesappf.databinding.ActivityAddEditPersonBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEditPersonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditPersonBinding
    private val apiService = ApiClient.getClient().create(ApiService::class.java)
    private var personId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        personId = intent.getIntExtra("PERSON_ID", 0)

        if (personId != 0) {
            getPersonDetail()
        }

        binding.btnSave.setOnClickListener {
            if (personId == 0) {
                createPerson()
            } else {
                updatePerson()
            }
        }
    }

    private fun getPersonDetail() {
        apiService.getPersonById(personId).enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                val persons = response.body()
                if (response.isSuccessful) {
                    if(!persons.isNullOrEmpty()) {
                        val person = persons[0]
                        binding.etPersonType.setText(person.PersonType)
                        binding.etFirstName.setText(person.FirstName)
                        binding.etLastName.setText(person.LastName)
                    }
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun createPerson() {
        val person = Person(
            PersonType = binding.etPersonType.text.toString(),
            FirstName = binding.etFirstName.text.toString(),
            LastName = binding.etLastName.text.toString()
        )

        apiService.createPerson(person).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    val intent = Intent()
                    setResult(RESULT_OK,intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun updatePerson() {
        val person = Person(
            BusinessEntityID = personId,
            PersonType = binding.etPersonType.text.toString(),
            FirstName = binding.etFirstName.text.toString(),
            LastName = binding.etLastName.text.toString()

        )

        apiService.updatePerson(personId, person).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val intent = Intent()
                    intent.putExtra("IS_UPDATE", true)
                    setResult(RESULT_OK,intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle error
            }
        })
    }
}
