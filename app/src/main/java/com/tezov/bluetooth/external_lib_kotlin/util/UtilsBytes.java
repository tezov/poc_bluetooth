package com.tezov.bluetooth.external_lib_kotlin.util;

import com.tezov.bluetooth.external_lib_kotlin.cipher.SecureProvider;
import com.tezov.bluetooth.external_lib_kotlin.type.unit.UnitByte;

import java.security.SecureRandom;

public class UtilsBytes {
private static final float MAX_BYTES_ARRAY_SIZE = UnitByte.o.convert(20f, UnitByte.Mo);

private UtilsBytes(){
}

public static byte[] reverse(byte[] bytes){
    int i = 0;
    int j = bytes.length - 1;
    byte tmp;
    while (j > i) {
        tmp = bytes[j];
        bytes[j] = bytes[i];
        bytes[i] = tmp;
        j--;
        i++;
    }
    return bytes;
}

public static byte[] removeLeadingZero(byte[] bytes){
    int countZero = 0;
    for(byte b: bytes){
        if(b != 0x00){
            break;
        }
        countZero++;
    }
    int length = bytes.length - countZero;
    if(length == 0){
        return new byte[]{(byte)0x00};
    }
    byte[] out = obtain(length);
    System.arraycopy(bytes, countZero, out, 0, length);
    return out;
}
public static void copyAndRepeat(byte[] src, byte[] dest){
    copyAndRepeat(src, dest, 0);
}
public static void copyAndRepeat(byte[] src, byte[] dest, int destOffset){
    for(int end = dest.length, i = destOffset; i < end; i++){
        dest[i] = src[i % src.length];
    }
}
public static void copy(byte[] src, byte[] dest){
    copy(src, 0, dest, 0, src.length);
}
public static void copy(byte[] src, byte[] dest, int destOffset){
    copy(src, 0, dest, destOffset, src.length);
}
public static void copy(byte[] src, int srcOffset, byte[] dest, int destOffset, int length){
    System.arraycopy(src, srcOffset, dest, destOffset, length);
}

public static byte[] random(int length){
    try{
        SecureRandom sr = SecureProvider.randomGenerator();
        byte[] bytes = obtain(length);
        sr.nextBytes(bytes);
        return bytes;

    } catch(Throwable e){
        return null;
    }
}
public static Byte random(){
    try{
        SecureRandom sr = SecureProvider.randomGenerator();
        byte[] bytes = obtain(1);
        sr.nextBytes(bytes);
        return bytes[0];

    } catch(Throwable e){

        return null;
    }
}

public static byte[] obtain(int length){
    if(length > MAX_BYTES_ARRAY_SIZE){
        return null;
    }
    return new byte[length];
}


public static byte[] xor(byte[] bytes, byte alter){
    for(int i = 0; i < bytes.length; i++){
        bytes[i] = (byte)(bytes[i] ^ alter);
    }
    return bytes;
}
public static byte[] xor(byte[] bytes, byte[] alter){
    for(int i = 0; i < bytes.length; i++){
        bytes[i] = (byte)(bytes[i] ^ alter[i % alter.length]);
    }
    return bytes;
}

}
