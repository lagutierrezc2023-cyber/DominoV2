package com.example.domino.viewModel

import androidx.lifecycle.ViewModel
import com.example.domino.data.model.GameState
import com.example.domino.data.model.Piece
import com.example.domino.data.model.Territory
import com.example.domino.data.remote.FirebaseAuthService
import com.example.domino.data.repository.GameRepository
import com.example.domino.data.repository.GameRepositoryImpl
import com.example.domino.domain.GameEngine
import com.example.domino.domain.TurnManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel(
    private val repository: GameRepository = GameRepositoryImpl()
) : ViewModel() {

    private val engine = GameEngine()
    private val turnManager = TurnManager(engine)

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    private val _selectedPiece = MutableStateFlow<Piece?>(null)
    val selectedPiece: StateFlow<Piece?> = _selectedPiece

    // Posiciones (fila * 5 + columna) de las 2 casillas elegidas
    private val _selectedCells = MutableStateFlow<List<Int>>(emptyList())
    val selectedCells: StateFlow<List<Int>> = _selectedCells

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    var myId = ""
        private set
    private var code = ""

    // Empieza a escuchar la partida de la sala
    fun start(roomCode: String) {
        code = roomCode
        myId = FirebaseAuthService().getCurrentUserId()
        repository.listenGame(code) { _state.value = it }
    }

    fun isMyTurn(): Boolean {
        return turnManager.isMyTurn(_state.value, myId)
    }

    fun canDraw(): Boolean {
        return turnManager.mustDraw(_state.value, myId)
    }

    fun selectPiece(piece: Piece) {
        _selectedPiece.value = piece
    }

    // Elige hasta 2 casillas; la primera recibe el número izquierdo de la ficha
    fun selectCell(territory: Territory) {
        val index = territory.row * 5 + territory.col
        val cells = _selectedCells.value
        if (cells.size == 2) {
            _selectedCells.value = listOf(index)
        } else if (!cells.contains(index)) {
            _selectedCells.value = cells + index
        }
    }

    // Girar la ficha = intercambiar qué casilla recibe cada número
    fun rotate() {
        _selectedCells.value = _selectedCells.value.reversed()
    }

    fun cancel() {
        _selectedPiece.value = null
        _selectedCells.value = emptyList()
        _message.value = ""
    }

    // Coloca la ficha en las 2 casillas, pasa el turno y guarda en Firebase
    fun place() {
        val state = _state.value
        val piece = _selectedPiece.value ?: return
        val cells = _selectedCells.value
        if (cells.size < 2 || !isMyTurn()) return
        val first = state.board.getOrNull(cells[0]) ?: return
        val second = state.board.getOrNull(cells[1]) ?: return

        if (engine.placePiece(state, piece, first.row, first.col, second.row, second.col)) {
            engine.endTurn(state)
            cancel()
            repository.saveGame(code, state)
        } else {
            _message.value = "Movimiento no válido"
        }
    }

    // Sin jugada válida: toma una ficha del pozo o pierde el turno
    fun drawPiece() {
        val state = _state.value
        if (!canDraw()) return
        turnManager.drawOrSkip(state)
        repository.saveGame(code, state)
    }

    fun leave() {
        repository.leaveGame(code, myId)
    }
}