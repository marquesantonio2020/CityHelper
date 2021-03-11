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

    @Query("SELECT * FROM note_table WHERE id = :id")
    fun getNoteById(id: Int): LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM note_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("UPDATE note_table SET title = :title, description = :description WHERE id = :id")
    suspend fun updateNoteById(id: Int, title: String, description: String)
}