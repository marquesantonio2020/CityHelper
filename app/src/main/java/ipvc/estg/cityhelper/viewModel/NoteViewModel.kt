package ipvc.estg.cityhelper.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.cityhelper.entities.Note
import ipvc.estg.cityhelper.roomDB.NoteDB
import ipvc.estg.cityhelper.roomDB.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application){

    private val repository: NoteRepository
    //Using LiveData and caching what getAllNotes return has several benefits:
    //- We can put an observer on the data (instead of polling for changes) and only update the
    //UI when the data actually changes.
    //- Repository is completely separated from the  UI through the ViewModel.
    val allNotes: LiveData<List<Note>>

    init{
        val notesDao = NoteDB.getDatabase(application, viewModelScope).noteDao()
        repository = NoteRepository(notesDao)
        allNotes = repository.allNotes
    }

    /**
     * Lauching a new coroutine to insert the data in a non-blocking way
     */
    fun getNoteById(id: Int): LiveData<Note>{
        return repository.getNoteById(id)
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }

    fun deleteById(id: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteById(id)
    }

    fun updateNoteById(id: Int, title: String, description: String) = viewModelScope.launch (Dispatchers.IO){
        repository.updateNoteById(id, title, description)
    }
}