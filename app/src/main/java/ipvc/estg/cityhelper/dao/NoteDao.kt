package ipvc.estg.cityhelper.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipvc.estg.cityhelper.entities.Note

@Dao
interface NoteDao {
    @Query("Select * FROM note_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE title = :title")
    fun getNoteByTitle(title: String): LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM note_table WHERE title = :title")
    suspend fun deleteByTitle(title: String)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()
}