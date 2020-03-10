package com.rushadvisualexpressions.sudokuyoutube.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rushadvisualexpressions.sudokuyoutube.R
import com.rushadvisualexpressions.sudokuyoutube.view.custom.SudokuBoardView
import com.rushadvisualexpressions.sudokuyoutube.view.custom.SudokuBoardView.OnTouchListener
import com.rushadvisualexpressions.sudokuyoutube.viewmodel.PlaySudokuViewModel
import kotlinx.android.synthetic.main.activity_main.*

class PlaySudokuActivity : AppCompatActivity(), OnTouchListener {

    private lateinit var viewModel : PlaySudokuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sudokuBoardView.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(PlaySudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe( this, Observer { updateSelectedCellUI(it) })
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI( cell.first, cell.second)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}
