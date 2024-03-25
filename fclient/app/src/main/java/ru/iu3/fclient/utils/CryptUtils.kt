package ru.iu3.fclient.utils

import org.apache.commons.codec.DecoderException
import org.apache.commons.codec.binary.Hex

fun String.toHex(): ByteArray {
    return try {
        Hex.decodeHex(this.toCharArray())
    } catch (e: DecoderException) {
        ByteArray(0)
    }
}