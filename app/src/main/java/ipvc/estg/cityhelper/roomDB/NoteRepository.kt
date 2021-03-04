package ipvc.estg.cityhelper.roomDB

import androidx.lifecycle.LiveData
import ipvc.estg.cityhelper.dao.NoteDao
import ipvc.estg.cityhelper.entities.Note

//Declares the DAO as a private propertu in the constructor. Pass in the DAO
//instead of the whole database, because you only need access to the DAO
class NoteRepository(private val noteDao: NoteDao){

    //Room executes all queries on a separate thread.
    //Observed LiveData will notify the observer when the data has changed
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }
}