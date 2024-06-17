package com.example.notes

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.data.Note
import com.example.notes.noteview.NoteViewModel
import com.example.notes.ui.theme.NotesTheme

class NoteViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(application) as T
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val owner = LocalViewModelStoreOwner.current

            owner?.let {
                val viewModel: NoteViewModel = viewModel(
                    it,
                    "NoteViewModel",
                    NoteViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                NotesTheme {
                    Main(viewModel)
                }

            }

        }
    }
}

@Composable
fun Main(vm: NoteViewModel = viewModel()) {
    val noteList by vm.noteList.observeAsState(initial = listOf())
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        OutlinedTextField(
            value = vm.noteTitleVal,
            modifier = Modifier.padding(8.dp),
            label = { Text(text = "Title")},
            onValueChange = {vm.changeTitle(it)}
        )
        OutlinedTextField(
            value = vm.noteDescriptionVal,
            modifier = Modifier.padding(8.dp),
            label = { Text(text = "Description")},
            onValueChange = {vm.changeDescription(it)}
        )
        Button(onClick = {
            vm.addNote()
        }, modifier = Modifier.padding(8.dp)) {
            Text(text = "Add", fontSize = 22.sp)
        }
        NoteList(notes = noteList, delete = {vm.deleteNote(it)})
    }
}

@Composable
fun NoteList(notes: List<Note>, delete: (Int)->Unit) {
    LazyColumn(
        Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        item { NoteTitleRow() }
        items(notes) {n -> NoteRow(n, {delete(n.id)})}
    }
}

@Composable
fun NoteRow(note: Note, delete: (Int) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ){
        Text(
            text = note.id.toString(),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.1f),
            fontSize = 14.sp)
        Text(text = note.title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.2f),
            fontSize = 14.sp)
        Text(
            text = note.description,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.2f),
            fontSize = 14.sp
        )
        Text(text = "Delete",
            Modifier
                .weight(0.2f)
                .clickable { delete(note.id) },
            color = Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
            )
    }
}

@Composable
fun NoteTitleRow(){
    Row (
        Modifier
            .background(color = MaterialTheme.colorScheme.onSecondary)
            .fillMaxWidth()
            .padding(5.dp)
    ){
        Text(
            text = "Id",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Title",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.2f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Description",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.2f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(0.2f))
    }
}