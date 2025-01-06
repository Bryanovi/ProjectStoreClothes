import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MiBaseDeDatos.db"
        private const val DATABASE_VERSION = 1

        // Definición de la tabla "Usuario"
        private const val TABLE_NAME = "Usuario"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_APELLIDO = "apellido"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_TELEFONO = "telefono"
        private const val COLUMN_CONTRASENA = "contrasena"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla "Usuario"
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NOMBRE TEXT, $COLUMN_APELLIDO TEXT, $COLUMN_EMAIL TEXT, $COLUMN_TELEFONO TEXT, $COLUMN_CONTRASENA TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Manejar las actualizaciones de la base de datos si es necesario
        // Aquí puedes agregar lógica para migrar datos de versiones anteriores
    }

    fun insertarUsuario(nombre: String, apellido: String, email: String, telefono: String, contrasena: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_APELLIDO, apellido)
            put(COLUMN_EMAIL, email)
            put(COLUMN_TELEFONO, telefono)
            put(COLUMN_CONTRASENA, contrasena)
        }
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    // Otros métodos para consultar, actualizar y eliminar registros
}
