package com.example.proyectotienda.baseDatos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyectotienda.modelo.Ropa;

import java.util.List;

@Dao
public interface RopaDao {
    // El objetivo es definir el CRUD

    @Insert
    long insertar(Ropa ropa);

    @Update
    void actualizar(Ropa ropa);

    @Delete
    void eliminar(Ropa ropa);

    @Query("select * from tblRopa where id=:idRp")
    List<Ropa> obtenerRopa(int idRp);

    @Query("select * from tblRopa order by id")
    LiveData<List<Ropa>> listaRopas();
}
