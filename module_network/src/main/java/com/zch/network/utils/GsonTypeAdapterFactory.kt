package com.zch.network.utils

import com.google.gson.TypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
/**
 *
 * classtime-android
 * Description: gson类型转换
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
class GsonTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T?> {
        val adapter = gson.getDelegateAdapter(this, type)
        return object : TypeAdapter<T?>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T?) {
                adapter.write(out, value)
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): T? {
                return try {
                    adapter.read(`in`)
                } catch (e: Throwable) {
                    consumeAll(`in`)
                    null
                }
            }

            @Throws(IOException::class)
            private fun consumeAll(`in`: JsonReader) {
                if (`in`.hasNext()) {
                    val peek = `in`.peek()
                    if (peek == JsonToken.STRING) {
                        `in`.nextString()
                    } else if (peek == JsonToken.BEGIN_ARRAY) {
                        `in`.beginArray()
                        consumeAll(`in`)
                        `in`.endArray()
                    } else if (peek == JsonToken.BEGIN_OBJECT) {
                        `in`.beginObject()
                        consumeAll(`in`)
                        `in`.endObject()
                    } else if (peek == JsonToken.END_ARRAY) {
                        `in`.endArray()
                    } else if (peek == JsonToken.END_OBJECT) {
                        `in`.endObject()
                    } else if (peek == JsonToken.NUMBER) {
                        `in`.nextString()
                    } else if (peek == JsonToken.BOOLEAN) {
                        `in`.nextBoolean()
                    } else if (peek == JsonToken.NAME) {
                        `in`.nextName()
                        consumeAll(`in`)
                    } else if (peek == JsonToken.NULL) {
                        `in`.nextNull()
                    }
                }
            }
        }
    }
}