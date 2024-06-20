package com.example.notes

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notes.data.Note
import com.example.notes.noteview.NoteViewModel
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.converters.ConverterLongToString

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
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Route.screenOne) {
                        composable(route = Route.screenOne) {
                            Main(
                                navigateToAddNote = { navController.navigate(route = Route.screenTwo) },
                                viewModel
                            )
                        }
                        composable(route = Route.screenTwo) {
                            AddNote (
                                navigateMain = {navController.popBackStack()},
                                viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

object Route {
    const val screenOne = "Main"
    const val screenTwo = "AddNote"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    navigateToAddNote:() -> Unit,
    vm: NoteViewModel = viewModel()
) {
    val noteList by vm.noteList.observeAsState(initial = listOf())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "My notes", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
        },
        floatingActionButton = {
            FloatingActionButton(onClick =
                navigateToAddNote
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            NoteList(notes = noteList, delete = {vm.deleteNote(it)})
        }
    }

}

@Composable
fun NoteList(notes: List<Note>, delete: (Int)->Unit) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        items(notes) {n -> NoteBox(n, {delete(n.id)})}
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteBox(note: Note, delete: (Int) -> Unit) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(shape = RoundedCornerShape(18.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp)

            ){
                Text(
                    text = note.title,
                    modifier = Modifier.padding(4.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = note.description,
                    modifier = Modifier.padding(end = 4.dp),
                    fontSize = 16.sp
                )
                Text(
                    text = ConverterLongToString(note.dateAdded),
                    modifier = Modifier.padding(4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

            }

            IconButton(
                onClick = { delete(note.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary
                    )
            }
        }
    }
}
