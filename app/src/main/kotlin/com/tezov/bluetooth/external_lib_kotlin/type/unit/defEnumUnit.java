package com.tezov.bluetooth.external_lib_kotlin.type.unit;

public interface defEnumUnit<E extends Enum<E>>{
long convert(long value);

long convert(long value, E unit);

float convert(float value);

float convert(float value, E unit);

String getSeparator();

}
