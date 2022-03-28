package com.onlinemed.config.converters;

import com.google.gson.*;

import java.lang.reflect.Type;

public class EnumConverter implements JsonSerializer<Enum<?>>, JsonDeserializer<Enum<?>> {

    private JsonParser jsonParser = new JsonParser();

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param jsonElement The Json data being deserialized
     * @param type        The type of the Object to deserialize to
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public Enum<?> deserialize(JsonElement jsonElement, Type type,
                               JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        int enumInt = jsonElement.getAsInt();
        if (type instanceof Class && ((Class<?>) type).isEnum()) {
            Class<Enum> enumClass = (Class<Enum>) type;
            if (enumInt >= enumClass.getEnumConstants().length) {
                throw new IllegalStateException(String.format("%d is out of bound in %s", enumInt, enumClass.getName()));
            }
            return Enum.valueOf(enumClass, enumClass.getEnumConstants()[enumInt].toString());
        } else {
            throw new IllegalArgumentException(String.format("It is not an enum: %s", ((Class<Enum>) type).getName()));
        }
    }

    /**
     * Gson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     *
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
     * non-trivial field of the {@code src} object. However, you should never invoke it on the
     * {@code src} object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).</p>
     *
     * @param anEnum the object that needs to be converted to Json.
     * @param type   the actual type (fully genericized version) of the source object.
     * @return a JsonElement corresponding to the specified object.
     */
    @Override
    public JsonElement serialize(Enum<?> anEnum, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonParser.parse(((Integer) anEnum.ordinal()).toString());
    }
}
