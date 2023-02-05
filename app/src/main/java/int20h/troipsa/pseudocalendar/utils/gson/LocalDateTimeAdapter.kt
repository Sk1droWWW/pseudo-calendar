package int20h.troipsa.pseudocalendar.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime?>() {

    override fun write(writer: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.toString())
        }
    }

    override fun read(reader: JsonReader): LocalDateTime? {
        return if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            null
        } else {
            val string: String = reader.nextString()
            try {
                LocalDateTime.parse(string)
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }
    }
}