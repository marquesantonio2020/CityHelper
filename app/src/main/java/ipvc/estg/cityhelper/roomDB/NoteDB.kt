package ipvc.estg.cityhelper.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.cityhelper.dao.NoteDao
import ipvc.estg.cityhelper.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//Annotates class to be a Room Database with a table (entity) of the Note Class

//Note: When you modify the database schema, you'll need to update the version number and define a migration strategy
//For a sample, a destroy and re-create strategy can be sufficient. But, or a real app, you must implement a migration strategy.

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
public abstract class NoteDB : RoomDatabase() {

    abstract fun noteDao() : NoteDao

    private class NoteDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let{ database ->
                scope.launch {
                    var noteDao = database.noteDao()

                    /**
                    //Delete all content here
                    noteDao.deleteAll()

                    //Add sample words
                    var note = Note(1, "Título do nota 1", "Está foi a minha primeira nota criada para testar o funcionamento da aplicação e do RoomDatabase!")
                    noteDao.insert(note)
                     */
                }
            }
        }
    }

    companion object {
        //Singleton prevents multiple instances of a database opening at the
        //same time
        @Volatile
        private var INSTANCE: NoteDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteDB {
            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDB::class.java,
                    "notes_database"
                )
                .addCallback(NoteDatabaseCallback(scope))
                .build()

                INSTANCE = instance

                return instance
            }
        }
    }
}