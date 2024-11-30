package com.example.clientesappf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.clientesappf.databinding.ActivityPersonDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonDetailBinding
    private val apiService = ApiClient.getClient().create(ApiService::class.java)
    private var personId: Int = 0

    private val editPersonLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode ==RESULT_OK){
            getPersonDetail()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        personId = intent.getIntExtra("PERSON_ID", 0)

        //BOTON PARA EDITAR PERSON
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddEditPersonActivity::class.java).apply {
                putExtra("PERSON_ID", personId)
            }
            editPersonLauncher.launch(intent)
        }

        //BOTON PARA BORRAR PERSON
        binding.btnDelete.setOnClickListener {
            deletePerson()
        }

        getPersonDetail()
    }

    private fun getPersonDetail() {
        apiService.getPersonById(personId).enqueue(object : Callback <List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                val persons = response.body()
                if (response.isSuccessful) {
                    if(!persons.isNullOrEmpty()){
                        val person = persons[0]
                        binding.tvPersonType.text = person.PersonType
                        binding.tvFirstName.text = person.FirstName
                        binding.tvLastName.text = person.LastName
                    }
                }else{
                    Log.i("ERROR","ERROR EN LA SOLICITUD")
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                // Handle
                Log.e("API_CALL", "Fallo en la llamada a la API: ${t.message}")
                // Mostrar un mensaje al usuario
            }
        })
    }

    private fun deletePerson() {
        apiService.deletePerson(personId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    //Val indica que la operacion fue exitosa
                    val intent = Intent()
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }else{
                    Log.e("DELETE ERROR","Error al eliminar: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

                Log.e("DELET ERROR","ERROR AL DELETE")
            }
        })
    }
}
