package com.rushadvisualexpressions.sudokuyoutube.viewmodel

import android.arch.lifecycle.ViewModel
import com.rushadvisualexpressions.sudokuyoutube.game.SudokuGame

class PlaySudokuViewModel : ViewModel() {
    val sudokuGame = SudokuGame()
}