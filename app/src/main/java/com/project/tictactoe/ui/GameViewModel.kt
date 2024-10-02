package com.project.tictactoe.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.project.tictactoe.UserAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    private val _gameState = mutableStateOf(GameState())
    val gameState: State<GameState> = _gameState

    private val _boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE
    )
    val boardItems: Map<Int, BoardCellValue> = _boardItems

    fun onAction(action: UserAction) {
        when(action) {
            is UserAction.BoardTapped -> {
                addValueToBoard(action.cellNo)
            }
            UserAction.PlayAgainButtonClicked -> {
                gameReset()
            }
        }
    }

    private fun gameReset() {
        _boardItems.forEach {(i, _) ->
            _boardItems[i] = BoardCellValue.NONE
        }
        _gameState.value = _gameState.value.copy(
            hintText = "Player 0's turn",
            currentTurn = BoardCellValue.CIRCLE,
            victoryType = VictoryType.NONE,
            hasWon = false
        )
    }

    private fun addValueToBoard(cellNo: Int) {
        if(_boardItems[cellNo] != BoardCellValue.NONE) {
            return
        }
        if(_gameState.value.currentTurn == BoardCellValue.CIRCLE) {
            _boardItems[cellNo] = BoardCellValue.CIRCLE
            if(checkForVictory(BoardCellValue.CIRCLE)) {
                _gameState.value = _gameState.value.copy(
                    hintText = "Player '0' Won",
                    playerCrossCount = _gameState.value.playerCircleCount.inc(),
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if(hasBoardFull()) {
                _gameState.value = _gameState.value.copy(
                    hintText = "Game Draw",
                    drawCount = _gameState.value.drawCount.inc()
                )
            } else {
                _gameState.value = _gameState.value.copy(
                    hintText = "Player X's turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }
        } else if(_gameState.value.currentTurn == BoardCellValue.CROSS) {
            _boardItems[cellNo] = BoardCellValue.CROSS
            if(checkForVictory(BoardCellValue.CROSS)) {
                _gameState.value = _gameState.value.copy(
                    hintText = "Player 'X' Won",
                    playerCrossCount = _gameState.value.playerCrossCount.inc(),
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if(hasBoardFull()) {
                _gameState.value = _gameState.value.copy(
                    hintText = "Game Draw",
                    drawCount = _gameState.value.drawCount.inc()
                )
            } else {
                _gameState.value = _gameState.value.copy(
                    hintText = "Player 0's turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
        }
    }

    private fun checkForVictory(boardValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == boardValue && boardItems[2] == boardValue && boardItems[3] == boardValue -> {
                _gameState.value = _gameState.value.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }
            boardItems[4] == boardValue && boardItems[5] == boardValue && boardItems[6] == boardValue -> {
                _gameState.value = _gameState.value.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }
            boardItems[7] == boardValue && boardItems[8] == boardValue && boardItems[9] == boardValue -> {
                _gameState.value = _gameState.value.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[4] == boardValue && boardItems[7] == boardValue -> {
                _gameState.value = _gameState.value.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }
            boardItems[2] == boardValue && boardItems[5] == boardValue && boardItems[8] == boardValue -> {
                _gameState.value = _gameState.value.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }
            boardItems[3] == boardValue && boardItems[6] == boardValue && boardItems[9] == boardValue -> {
                _gameState.value = _gameState.value.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[5] == boardValue && boardItems[9] == boardValue -> {
                _gameState.value = _gameState.value.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }
            boardItems[3] == boardValue && boardItems[5] == boardValue && boardItems[7] == boardValue -> {
                _gameState.value = _gameState.value.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }
            else -> return false
        }
    }

    private fun hasBoardFull(): Boolean {
        return !boardItems.containsValue(BoardCellValue.NONE)
    }
}