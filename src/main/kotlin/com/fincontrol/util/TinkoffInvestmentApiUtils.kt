package com.fincontrol.util

import com.google.protobuf.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Converter for UTF string
 * @param utfCharset list of encoded chars
 * @return decoded string
 */
fun decodeName(utfCharset: String): String {
    return String(utfCharset.toByteArray())
}

/**
 * Converter between protobuf timestamp and LocalDateTime
 * @return LocalDateTime in UTC
 */
fun Timestamp.convertTimestampToLocalDateTime(): LocalDateTime {
    return Instant.ofEpochSecond(this.seconds, this.nanos.toLong()).atZone(ZoneOffset.UTC).toLocalDateTime()
}
