package int20h.troipsa.pseudocalendar.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.LocalDate


class LocalDateAdapter : TypeAdapter<LocalDate?>() {

    override fun write(writer: JsonWriter, value: LocalDate?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.toString())
        }
    }

    override fun read(reader: JsonReader): LocalDate? {
        return if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            null
        } else {
            val string: String = reader.nextString()
            try {
                LocalDate.parse(string)
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }
    }
}