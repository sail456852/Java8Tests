package msc.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.gsonfire.GsonFireBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: eugene<br/>
 * Date: 2019/1/21<br/>
 * Time: 14:46<br/>
 * To change this template use File | Settings | File Templates.
 * copy from company's code base
 */
public class GsonUtils {
    private static Gson gson;

    public static String toJsonWithAll(Map<String, Object> map) {
        return getGsonInstance("yyyy-MM-dd HH:mm:ss").toJson(map);
    }

    private static Gson getGsonInstance(final String dateFormat) {
        if (gson == null) {
            GsonBuilder builder = (new GsonFireBuilder()).enableExposeMethodResult().createGsonBuilder().serializeNulls().disableHtmlEscaping().registerTypeAdapterFactory(new GsonUtils.NullStringToEmptyAdapterFactory()).registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                DateFormat df = new SimpleDateFormat(dateFormat);

                public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                    return src == null ? new JsonPrimitive("") : new JsonPrimitive(this.df.format(src));
                }
            });
            if (StringUtils.isNotEmpty(dateFormat)) {
                builder.setDateFormat(dateFormat);
            }
            gson = builder.create();
        }
        return gson;
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public NullStringToEmptyAdapterFactory() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            return rawType != String.class ? null : (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public StringAdapter() {
        }

        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            } else {
                return reader.nextString();
            }
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
            } else {
                writer.value(value);
            }
        }
    }
}
