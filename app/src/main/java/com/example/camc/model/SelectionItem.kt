package com.example.camc.model

class SelectionItem(val associatedValue: Int,
                    val label: String) {

    companion object {
        fun litFromLabels(vararg vs: String): List<SelectionItem> {
            var index = -1;
            return vs.map { label ->
                index++
                SelectionItem(
                    label = label,
                    associatedValue = index
                )
            }
        }
    }
}

