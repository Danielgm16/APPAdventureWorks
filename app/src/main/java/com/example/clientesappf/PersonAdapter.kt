package com.example.clientesappf

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clientesappf.databinding.ItemPersonBinding

class PersonAdapter(private val onItemClicked: (Person) -> Unit) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    // Se inizializa una lista vacia
    private var persons: List<Person> = emptyList()
    // se actualiza cuando se llama a la funcion updatePerson
    @SuppressLint("NotifyDataSetChanged")
    fun updatePersons(persons: List<Person>) {
        this.persons = persons
        //Log.d("PersonAdapter","Personas obtenidas: ${persons.size}" )
        notifyDataSetChanged() //Notifica al adaptador que los datos han cambiado
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(persons[position])
    }
    //Retorna la cantidad de elementos que hay en la lista personas.
    override fun getItemCount(): Int = persons.size


    inner class PersonViewHolder(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var person: Person

        init {
            binding.root.setOnClickListener {
                onItemClicked(person)
            }
        }

        fun bind(person: Person) {
            this.person = person
            binding.tvPersonName.text = "${person.FirstName} ${person.LastName}"
        }
    }
}
