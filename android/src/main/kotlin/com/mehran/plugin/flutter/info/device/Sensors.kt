package com.mehran.plugin.flutter.info.device
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
//import androidx.appcompat.app.AppCompatActivity
import android.os.IBinder
import java.lang.System.arraycopy
import kotlin.math.round


/*
Types of com.mehran.plugin.flutter.info.device.Sensors
The Android platform supports three broad categories of sensors:

Motion com.mehran.plugin.flutter.info.device.Sensors
These sensors track movement; they include accelerometers, gyroscopes and gravity sensors. They provide data on forces like acceleration and rotation that act on the sensor’s three-dimensional axes.

Environmental com.mehran.plugin.flutter.info.device.Sensors
Barometers and thermometers are types of sensors that access environmental metrics. These sensors monitor environmental variables like air pressure and temperature.

Position com.mehran.plugin.flutter.info.device.Sensors
Magnetometer and orientation sensors help determine the physical position of a device.

Each of these categories represents many specific sensors that are available on a device. You will go through them next.

List of com.mehran.plugin.flutter.info.device.Sensors
Android SDK provides you with a list of various types of sensors that you can use in your app. The availability of these sensors may vary from device to device.

Here’s a quick rundown of each sensor:

TYPE_ACCELEROMETER
Type: Hardware
Computes the acceleration in m/s2 applied on all three axes (x, y and z), including the force of gravity.
TYPE_AMBIENT_TEMPERATURE
Type: Hardware
Monitors the temperature of the surroundings in degrees Celsius.
TYPE_GRAVITY
Type: Software or Hardware
Computes the gravitational force in m/s2 applied on all three axes (x, y and z).
TYPE_GYROSCOPE
Type: Hardware
Computes the rate of rotation in rad/s around each of the three axes (x, y and z).
TYPE_LIGHT
Type: Hardware
Evaluates the light around a surrounding in lx units.
TYPE_LINEAR_ACCELERATION
Type: Software or Hardware
Computes the acceleration force in m/s2 applied on all three axes (x, y and z), excluding the force of gravity.
TYPE_MAGNETIC_FIELD
Type: Hardware
Computes the geomagnetic field for all three axes in tesla (μT).
TYPE_ORIENTATION
Type: Software
Computes the degree of rotation around all three axes.
TYPE_PRESSURE
Type: Hardware
Computes the air pressure in hPa or mbar.
TYPE_PROXIMITY
Type: Hardware
Computes the proximity of the device’s screen to an object in centimeters.
TYPE_RELATIVE_HUMIDITY
Type: Hardware
Computes the humidity of the surrounding air as a percentage (%).
TYPE_ROTATION_VECTOR
Type: Software or Hardware
Computes the orientation of a device by the device’s rotation vector.
TYPE_TEMPERATURE
Type: Hardware
Monitors the temperature of the surroundings in degrees Celsius. In API 14, the TYPE_AMBIENT_TEMPERATURE sensor replaced this sensor.
Combining Sensor Data With Sensor Fusion
Typically, you can develop a compass just with a magnetometer. If you want more accurate data, however, you can combine a magnetometer with an accelerometer. This method of using data from two or more sensors to get a more accurate result is known as Sensor Fusion.

Now, it’s time to start putting to use all this information. You already have the starter app up and running. You are going to start adding implementation details in order to make the compass functional. Head over to the next section to begin.*/


class Sensors : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager

//These variables will hold the latest accelerometer and magnetometer values.
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)


//These two arrays will hold the values of the rotation matrix and orientation angles.
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)



    companion object {
        val KEY_ANGLE = "angle"
        val KEY_DIRECTION = "direction"
        val KEY_BACKGROUND = "background"
        val KEY_NOTIFICATION_ID = "notificationId"
        val KEY_ON_SENSOR_CHANGED_ACTION = "com.raywenderlich.android.locaty.ON_SENSOR_CHANGED"
        val KEY_NOTIFICATION_STOP_ACTION = "com.raywenderlich.android.locaty.NOTIFICATION_STOP"
    }





    /*
    Initializes SensorManager.
Registers a sensor event callback to listen to changes in the accelerometer.
Registers a sensor event callback to listen to changes in the magnetometer.
Android SDK provides four constants, which inform the Android system how often to tap into the computed events:

SENSOR_DELAY_FASTEST: Gets sensors data as soon as possible
SENSOR_DELAY_GAME: Gets sensors data at a rate suitable for games
SENSOR_DELAY_UI: Gets sensors data at a rate suitable for working with user interfaces
SENSOR_DELAY_NORMAL: Gets sensors data at a rate suitable for screen orientation changes

    */
    override fun onCreate() {
        super.onCreate()
        // 1
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
// 2
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
            }
        }
// 3
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }








    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {


        // 1
        if (event == null) {
            return
        }
        // 2
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // 3
           arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }


        updateOrientationAngles()


    }




/*
First, it gets the rotation matrix.
It then uses that rotation matrix, which consists of an array of nine values, and maps it to a usable matrix with three values.
In the variable orientation, you get values that represent

orientation[0] = Azimuth (rotation around the -ve z-axis)
orientation[1] = Pitch (rotation around the x-axis)
orientation[2] = Roll (rotation around the y-axis)
All these values are in radians.

Next, it converts the azimuth to degrees, adding 360 because the angle is always positive.
Finally, it rounds the angle up to two decimal places.*/

    fun updateOrientationAngles() {
        // 1
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        // 2
        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)
        // 3
        val degrees = (Math.toDegrees(orientation.get(0).toDouble()) + 360.0) % 360.0
        // 4
        val angle = round(degrees * 100) / 100

        var direction =getDirection(angle)

        // 1
        val intent = Intent()
        intent.putExtra(KEY_ANGLE, angle)
        intent.putExtra(KEY_DIRECTION, direction)
        intent.action = KEY_ON_SENSOR_CHANGED_ACTION
// 2

//        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }



/* GETTING DIRECTION BASE ANGLE

You find the cardinal and intercardinal directions based on the angle you pass.

Note: Cardinal directions are north, east, south and west. They define a clockwise rotation from north to west, with west and east being perpendicular to north and south.
Intercardinal directions are the intermediate directions: Northeast is 45°, southeast is 135°, southwest is 225° and northwest is 315°.

The theory behind the function above is that, according to cardinal directions, north is 0° or 360°, east is 90°, south is 180° and west is 270°.

*/
    private fun getDirection(angle: Double): String {
        var direction = ""

        if (angle >= 350 || angle <= 10)
            direction = "N"
        if (angle < 350 && angle > 280)
            direction = "NW"
        if (angle <= 280 && angle > 260)
            direction = "W"
        if (angle <= 260 && angle > 190)
            direction = "SW"
        if (angle <= 190 && angle > 170)
            direction = "S"
        if (angle <= 170 && angle > 100)
            direction = "SE"
        if (angle <= 100 && angle > 80)
            direction = "E"
        if (angle <= 80 && angle > 10)
            direction = "NE"

        return direction
    }



    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}