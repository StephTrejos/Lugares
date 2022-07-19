package com.example.lugares.ui.lugar

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lugares.R
import com.example.lugares.databinding.FragmentAddLugarBinding
import com.example.lugares.model.Lugar
import com.example.lugares.utiles.AudioUtiles
import com.example.lugares.viewmodel.LugarViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AddLugarFragment : Fragment() {
    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var lugarViewModel: LugarViewModel

    private lateinit var audioUtiles: AudioUtiles

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)
        binding.btAgregar.setOnClickListener {
            addLugar()
        }

        audioUtiles = AudioUtiles(
            requireActivity(),
            requireContext(),
            binding.btAccion,
            binding.btPlay,
            binding.btDelete,
            getString(R.string.msg_graba_audio),
            getString(R.string.msg_detener_audio)
        )

        ubicaGPS()

        return binding.root
    }

    private fun ubicaGPS() {
        val ubicacion: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {  //Si no tengo los permisos entonces los solicito
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 105
            )
        } else {  //Si se tienen los permisos
            ubicacion.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    binding.tvLatitud.text = "${location.latitude}"
                    binding.tvLongitud.text = "${location.longitude}"
                    binding.tvAltura.text = "${location.altitude}"
                } else {
                    binding.tvLatitud.text = "0.00"
                    binding.tvLongitud.text = "0.00"
                    binding.tvAltura.text = "0.00"
                }
            }
        }

    }

    private fun addLugar() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        val altura = binding.tvAltura.text.toString().toDouble()

        if (nombre.isNotEmpty()) { //Si puedo crear un lugar
            val lugar = Lugar(
                "", nombre, correo, telefono, web, latitud,
                longitud, altura, "", ""
            )
            lugarViewModel.addLugar(lugar)
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_lugar_added),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        } else {  //Mensaje de error...
            Toast.makeText(requireContext(), getString(R.string.msg_data), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}