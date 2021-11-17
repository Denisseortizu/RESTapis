package com.example.restapis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    private  lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etPokemonName = findViewById<EditText>(R.id.et_pokemon_to_search_for)
        val btnSearch = findViewById<Button>(R.id.btn_search)



        queue = Volley.newRequestQueue(this)

        //val apiRequest = URL("https://pokeapi.co/api/v2/pokemon/").readText()

        btnSearch.setOnClickListener{
            getPokemon(etPokemonName.text.toString())
            etPokemonName.text.clear()
        }



    }
    fun getPokemon(pokemonName: String){
        val url = "https://pokeapi.co/api/v2/pokemon/${pokemonName.lowercase()}"
        val pokemonInfo = findViewById<TextView>(R.id.tv_pokemon_info)
        //val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String>{}, Response.ErrorListener{})
        val jsonRequest = JsonObjectRequest(url,
            { response ->
                val name = response.getString("name")
                Log.d("LISTENER", "NOMBRE : $name")
                val id = response.getString("id")
                Log.d("LISTENER", "ID : $id")

                var type : String = ""
                val max = response.getJSONArray("types").length() - 1
                for (i in 0..max){
                    val res = response.getJSONArray("types").getJSONObject(i).getJSONObject("type").getString("name")
                    type = "$type $res"
                }

                val hp = response.getJSONArray("stats").getJSONObject(0).getString("base_stat")

                val attack = response.getJSONArray("stats").getJSONObject(1).getString("base_stat")

                val defence = response.getJSONArray("stats").getJSONObject(2).getString("base_stat")

                val speed = response.getJSONArray("stats").getJSONObject(5).getString("base_stat")

                val weight = response.getString("weight")
                //TODO hp, attack, type, speed, defence, weight
                val infoString = "NAME:${name.replaceFirstChar { it.uppercase() }}  ID:$id  TYPE:$type  HP:$hp  ATTACK:$attack  DEFENCE:$defence  SPEED:$speed  WEIGTH:$weight "
                Log.d("RESULTADO", "INFOSTRING : $infoString")
                //.replaceFirstChar{it.uppercase()}
                pokemonInfo.text = infoString

              Log.d("JSONResponse", "Response: $infoString")
            },
            { errorMessage ->
                pokemonInfo.text = "Error 404: POKEMON NOT FOUND"
                Log.d("JSONResponse", "Error : $errorMessage")
            }
            )
        queue.add(jsonRequest)

    }


    override fun onStop(){
        super.onStop()
        queue.cancelAll("stopped")
    }
}