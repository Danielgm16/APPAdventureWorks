package com.example.clientesappf

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientesappf.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var personAdapter: PersonAdapter
    private val apiService = ApiClient.getClient().create(ApiService::class.java)

    private val addEditPersonLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == RESULT_OK){
            getAllPersons()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        personAdapter = PersonAdapter { person -> showPersonDetail(person) }
        binding.recyclerViewPersons.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPersons.adapter = personAdapter

        //Manejamos el click de add nuevo
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddEditPersonActivity::class.java)
            addEditPersonLauncher.launch(intent)
        }
        //Se muestran todos los empleados
        getAllPersons()
    }

    override fun onResume() {
        super.onResume()
        getAllPersons()
    }

    private fun getAllPersons() {
        //Log.d("API_CALL", "Iniciando la llamda a la API para obtener todas las personas...")
        apiService.getAllPersons().enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if (response.isSuccessful) {
//                    val persons = response.body()?: emptyList()
//                    persons.forEach{person->
//                        Log.d("API", "Nombre: ${person.FirstName}, Apellido ${person.LastName}")
//                    }
                    //Log.d("API","Personas obtenidas: ${persons.size}")
                    Log.d("Exito","Exito en la solicitud")
                    personAdapter.updatePersons(response.body() ?: emptyList())
                }else{
                    Log.d("Fallo","Fallo en la solicitud")
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                Log.e("API_CALL", "Fallo en la llamada a la API: ${t.message}")
            }
        })
    }

    private fun showPersonDetail(person: Person) {
        val intent = Intent(this, PersonDetailActivity::class.java).apply {
            putExtra("PERSON_ID", person.BusinessEntityID)
        }
        addEditPersonLauncher.launch(intent)
    }
}
