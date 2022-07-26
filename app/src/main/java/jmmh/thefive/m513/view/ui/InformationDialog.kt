package jmmh.ibcpt.control

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import jmmh.thefive.m513.databinding.DialogInformationBinding

class QuantityDialog() : DialogFragment() {

    private lateinit var binding: DialogInformationBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogInformationBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        readUsuario()
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun readUsuario() {
        db.collection(
            jmmh.thefive.m513.data.utils
                .Constants.LOCATION_DATABASE
        )
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    binding.textFecha.setText(
                        "Date : " + document.get("datetime").toString()
                    )
                    binding.textLatitude.setText(
                        "Lat : " + document.get("latitude").toString()
                    )
                    binding.textLongitud.setText(
                        "Lon: " + document.get("longitud").toString()
                    )
                }
            }
            .addOnFailureListener { exception ->
            }
    }
}