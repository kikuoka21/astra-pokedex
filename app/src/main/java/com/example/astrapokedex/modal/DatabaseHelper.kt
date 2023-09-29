package com.example.astrapokedex.modal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.astrapokedex.modal.model.Pokemon
import javax.inject.Inject

class DatabaseHelper @Inject constructor(private val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VersionDB) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TabelPokemon ($FId INTEGER PRIMARY KEY, $FName TEXT, $FUrl TEXT)"
        db?.execSQL(createTableQuery)


    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(" DROP TABLE IF EXISTS $TabelPokemon")
        //   sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(p0)
    }

    companion object {
        const val VersionDB = 5

        const val DATABASE_NAME = "pokedex.db"

        const val TabelPokemon = "tpokemon"

        const val FId = "id"
        const val FName = "name"
        const val FUrl = "url"
        const val SearcAsc = "ASC"
        const val SearcDesc = "DESC"

    }


    fun insertPokemon(pokemonName: String, pokemonUrl: String): Long {
        val db = writableDatabase
        val query = "SELECT * FROM $TabelPokemon WHERE $FName = ?"

        val cursor = db.rawQuery(query, arrayOf(pokemonName))
        if (cursor.count == 0) {
            val contentValues = ContentValues()
            contentValues.put(FName, pokemonName)
            contentValues.put(FUrl, pokemonUrl)

            cursor.close()
            return db.insert(TabelPokemon, null, contentValues)
        }
        cursor.close()
        return -1

    }

    fun searchPokemonByName(keyword: String, short: String): List<Pokemon> {
        val pokemons = mutableListOf<Pokemon>()
        val db = readableDatabase
        val selection = "$FName LIKE ?"
        val selectionArgs = arrayOf("%$keyword%")
        val orderBy = "$FName $short"

        val cursor: Cursor = db.query(
            TabelPokemon, null, selection, selectionArgs, null, null, orderBy, "600"
        )

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(FId))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(FName))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(FUrl))
            pokemons.add(Pokemon(id, name, url))
        }

        cursor.close()
        return pokemons
    }

    fun deleteAllPokemons() {
        val db = writableDatabase
        db.delete(TabelPokemon, null, null)
    }

    fun getPokemonCount(): Int {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM $TabelPokemon"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }
}