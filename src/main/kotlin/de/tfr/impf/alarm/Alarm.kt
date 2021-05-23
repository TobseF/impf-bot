package de.tfr.impf.alarm

import de.tfr.impf.config.Config
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException


fun main() {
    play(800, 1000, 0.5)
}

class Alarm {
    private val duration = Config.alarmDuration
    private val frequency = Config.alarmFrequency
    private val volume = Config.alarmVolumeInPercentage
    fun alert() {
        play(frequency, duration, volume)
    }
}

//https://stackoverflow.com/a/29714237
@Throws(LineUnavailableException::class)
private fun play(hz: Int, msecs: Int, vol: Double) {
    val buf = ByteArray(1)
    val af = AudioFormat(
        8000f,  // sampleRate
        8,  // sampleSizeInBits
        1,  // channels
        true,  // signed
        false
    ) // bigEndian
    val sdl = AudioSystem.getSourceDataLine(af)
    sdl.open(af)
    sdl.start()
    for (i in 0 until msecs * 8) {
        val angle: Double = i / (8000f / hz) * 2.0 * Math.PI
        buf[0] = (Math.sin(angle) * 127.0 * vol).toByte()
        sdl.write(buf, 0, 1)
    }
    sdl.drain()
    sdl.stop()
    sdl.close()
}
