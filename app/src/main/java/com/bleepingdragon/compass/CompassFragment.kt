package com.bleepingdragon.compass

import android.annotation.SuppressLint
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bleepingdragon.compass.modules.SharedPreferences
import com.bleepingdragon.compass.databinding.FragmentCompassBinding


class CompassFragment : Fragment(), SensorEventListener {

    //Fragment binding
    private var _binding: FragmentCompassBinding? = null

    //This property is only valid between onCreateView and onDestroyView.
    //https://developer.android.com/topic/libraries/view-binding?hl=es-419
    private val binding get() = _binding!!

    //Sensors
    private lateinit var sensorManager: SensorManager
    private lateinit var orientationSensor: Sensor

    //Values
    private var newRotationSensorValue: Float = 0.0F
    private var previousRotationValue: Float = 0.0F


    //When creating the fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        //Get the sensor manager
        sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager
    }


    //When creating the fragment ui
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Inflate the layout for this fragment
        _binding = FragmentCompassBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    //When the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get the Screen on preference, leave screen on if it's active in the compass fragment
        val keepScreenOn = SharedPreferences.GetBoolPreference("keepScreenOn", this.requireActivity())

        if (keepScreenOn) { requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
        else { requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }

        //Get the compass details prefeference and show details if active
        val isDetailsActive = SharedPreferences.GetBoolPreference("isDetailsActive", this.requireActivity())
        Log.d("isDetailsActive", isDetailsActive.toString())

        if (!isDetailsActive) {
            binding.detailsView.isVisible = false
        }
    }


    //When the sensor changes it's information, update the compass
    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {

        //Switch (depending on the sensor type, update it's values)
        when (event!!.sensor.type) {
            Sensor.TYPE_ORIENTATION -> newRotationSensorValue = event.values[0]
        }

        try {
            val rotationAnimation = RotateAnimation(
                previousRotationValue,
                -newRotationSensorValue,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )

            rotationAnimation.duration = 150
            binding.compassView.startAnimation(rotationAnimation)
            previousRotationValue = -newRotationSensorValue

            binding.degreesView.text = " " + newRotationSensorValue.toInt() + "º"

            if (binding.detailsView.isVisible) {
                binding.detailsView.startAnimation(rotationAnimation)
            }
        }
        catch(error: Exception){
            Log.e("SensorsError", error.toString())
        }
    }


    //When the accuracy of any sensor has changed
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }


    //When resuming fragment (and app), register the compass sensor if not registered before
    override fun onResume() {
        super.onResume()

        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        //"this" is the listener from the main activity SensorEventListener interface
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME)
    }


    //When the fragment (and app) is on pause, unregister the sensors
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    //Delete the bindings
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}