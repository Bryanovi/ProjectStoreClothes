package com.example.proyectotienda.baseDatos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proyectotienda.baseDatos.RopaDao;
import com.example.proyectotienda.modelo.Ropa;

@Database(entities = {Ropa.class}, version = 1, exportSchema = false)
public abstract class AppBaseDatos extends RoomDatabase {

    // Paso 1: Definir el DAO a utilizar
    public abstract RopaDao ropaDao();

    // Paso 2: Definir la base de datos
    private static AppBaseDatos instancia;

    // Manejar la instancia de base de datos
    public static synchronized AppBaseDatos getInstancia(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context.getApplicationContext(),
                    AppBaseDatos.class, "bdRopas").build();
        }
        return instancia;
    }
}
