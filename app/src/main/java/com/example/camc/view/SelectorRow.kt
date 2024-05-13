package com.example.camc.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.camc.model.SelectionItem


@Composable
fun SelectorRow(
    items: List<SelectionItem>,
    selectedValue: Int,
    onItemSelected: (SelectionItem) -> Unit
) {
    LaunchedEffect(selectedValue) {
        println("selection changed to $selectedValue")
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEach { item ->
            val isSelected = item.associatedValue == selectedValue
            SelectionItemView(item, isSelected) {
                if (!isSelected) {
                    onItemSelected(item)
                }
            }
        }
    }
}

@Composable
fun SelectionItemView(
    item: SelectionItem,
    isSelected: Boolean,
    onItemSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(
                if (isSelected) Color.Gray.copy(alpha = 0.5f) else Color.White,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable { onItemSelected() }
    ) {
        Text(
            text = item.label,
            modifier = Modifier.padding(16.dp)
        )
    }
}