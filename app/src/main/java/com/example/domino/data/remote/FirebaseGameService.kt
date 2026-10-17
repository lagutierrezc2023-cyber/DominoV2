package com.example.domino.data.remote

import com.example.domino.data.model.GameState
import com.example.domino.data.model.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseGameService {

    private val rooms = FirebaseDatabase.getInstance().getReference("rooms")
    private var listenedRoom: DatabaseReference? = null
    private var listener: ValueEventListener? = null

    fun saveRoom(room: Room, onDone: () -> Unit = {}) {
        rooms.child(room.code).setValue(room).addOnSuccessListener { onDone() }
    }

    fun saveState(code: String, state: GameState, onDone: () -> Unit = {}) {
        rooms.child(code).child("state").setValue(state).addOnSuccessListener { onDone() }
    }

    // Devuelve la sala, o null si no existe
    fun getRoom(code: String, onResult: (Room?) -> Unit) {
        rooms.child(code).get()
            .addOnSuccessListener { onResult(it.getValue(Room::class.java)) }
            .addOnFailureListener { onResult(null) }
    }

    fun deleteRoom(code: String) {
        rooms.child(code).removeValue()
    }

    // Avisa cada vez que alguien cambia la sala
    fun listenRoom(code: String, onChange: (Room) -> Unit) {
        stopListening()
        listenedRoom = rooms.child(code)
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Room::class.java)?.let(onChange)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        listenedRoom?.addValueEventListener(listener!!)
    }

    fun stopListening() {
        listener?.let { listenedRoom?.removeEventListener(it) }
        listener = null
    }
}