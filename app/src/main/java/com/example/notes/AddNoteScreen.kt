package com.example.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.noteview.NoteViewModel

@Composable
fun AddNote(
    navigateMain: () -> Unit,
    vm: NoteViewModel = viewModel()
    ) {
    Column(
        modifier = Modifier.fillMaxSize()
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
        Button(
            onClick = {
                vm.addNote()
                navigateMain()
            }
        ) {
            Text(text = "Add")
        }
    }
}