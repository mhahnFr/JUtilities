/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2017 - 2022  mhahnFr
 *
 * This file is part of the JUtilities. This library is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this library, see the file LICENSE.  If not, see <https://www.gnu.org/licenses/>.
 */

package mhahnFr.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a few helper functions to convert numbers into bytes and the other round.
 * IMPORTANT: not compatible with {@link java.io.DataInput} or {@link java.io.DataOutput}!
 *
 * @author mhahnFr
 * @since 01.06.2017
 */
public abstract class ByteHelper {

    /**
     * Returns the first byte of the given number.
     *
     * @param x the number whose first byte should be returned
     * @return the first byte of the given number
     */
    private static byte shortPart1(short x) {
        return (byte) x;
    }

    /**
     * Returns the second byte of the given number.
     *
     * @param x the number whose second byte should be returned
     * @return the second byte of the given number
     */
    private static byte shortPart2(short x) {
        return (byte) (x >> 8);
    }

    /**
     * Returns the third byte of the given number.
     *
     * @param x the number whose third byte should be returned
     * @return the third byte of the given number
     */
    private static byte intPart3(int x) {
        return (byte) (x >> 16);
    }

    /**
     * Returns the forth byte of the given number.
     *
     * @param x the number whose forth byte should be returned
     * @return the forth byte of the given number
     */
    private static byte intPart4(int x) {
        return (byte) (x >> 24);
    }

    /**
     * Returns the fifth byte of the given number.
     *
     * @param x the number whose fifth byte should be returned
     * @return the fifth byte of the given number
     */
    private static byte longPart5(long x) {
        return (byte) (x >> 32);
    }

    /**
     * Returns the sixth byte of the given number.
     *
     * @param x the number whose sixth byte should be returned
     * @return the sixth byte of the given number
     */
    private static byte longPart6(long x) {
        return (byte) (x >> 40);
    }

    /**
     * Returns the seventh byte of the given number.
     *
     * @param x the number whose seventh byte should be returned
     * @return the seventh byte of the given number
     */
    private static byte longPart7(long x) {
        return (byte) (x >> 48);
    }

    /**
     * Returns the eighth byte of the given number.
     *
     * @param x the number whose eighth byte should be returned
     * @return the eighth byte of the given number
     */
    private static byte longPart8(long x) {
        return (byte) (x >> 56);
    }

    /**
     * Converts the given {@code short} to a byte array consisting of two bytes.
     *
     * @param x the {@code short} that should be converted
     * @return two bytes corresponding the given {@code short}
     */
    public static byte[] shortToBytes(short x) {
        return new byte[] {
                shortPart1(x),
                shortPart2(x)
        };
    }

    /**
     * Converts the given {@code int} to a byte array consisting of four bytes.
     *
     * @param x the number that should be converted
     * @return a byte array containing the bytes of the {@code int}
     */
    public static byte[] intToBytes(int x) {
        return new byte[] {
                shortPart1((short) x),
                shortPart2((short) x),
                intPart3(x),
                intPart4(x)
        };
    }

    /**
     * Converts the given {@code long} to a byte array consisting of eight bytes.
     *
     * @param x the {@code long} that should be converted
     * @return eight bytes corresponding the given {@code long}
     */
    public static byte[] longToBytes(long x) {
        return new byte[] {
                shortPart1((short) x),
                shortPart2((short) x),
                intPart3((int) x),
                intPart4((int) x),
                longPart5(x),
                longPart6(x),
                longPart7(x),
                longPart8(x)
        };
    }

    /**
     * Converts the given byte array into a {@code short}.
     *
     * @param bytes the bytes that should be converted
     * @return a short from the byte array
     */
    public static short bytesToShort(byte[] bytes) {
        return (short) ((bytes[0] & 0xFF) | (bytes[1] & 0xFF) << 8);
    }

    /**
     * Converts the given byte array into an {@code int}.
     *
     * @param bytes the bytes that should be converted
     * @return a {@code int} from the byte array
     */
    public static int bytesToInt(byte[] bytes) {
        return (bytes[0] & 0xFF) | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF) << 24;
    }

    /**
     * Converts the given byte array into a {@code long}.
     *
     * @param bytes the bytes that should be converted
     * @return a {@code long} from the byte array
     */
    public static long bytesToLong(byte[] bytes) {
        return (bytes[0] & 0xFF) | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (long) (bytes[3] & 0xFF) << 24
                | (long) (bytes[4] & 0xFF) << 32 | (long) (bytes[5] & 0xFF) << 40 | (long) (bytes[6] & 0xFF) << 48 | (long) (bytes[7] & 0xFF) << 56;
    }

    /**
     * Casts {@code Byte[]} to {@code byte[]}.
     *
     * @param bytes the {@code Byte} array
     * @return a {@code byte} array
     */
    public static byte[] castToByte(Byte[] bytes) {
        byte[] toReturn = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) {
            toReturn[i] = bytes[i];
        }
        return toReturn;
    }

    /**
     * Converts a list containing byte arrays into a single byte array.
     *
     * @param list the list to be converted
     * @return a byte array containing all bytes from the list
     */
    public static byte[] castListToBytes(List<byte[]> list) {
        ArrayList<Byte> bytes = new ArrayList<>();
        for(byte[] bts : list) {
            for(byte b : bts) {
                bytes.add(b);
            }
        }
        return castToByte(bytes.toArray(new Byte[list.size()]));
    }

    /**
     * Copies the bytes in the range from {@code fromIndex} to {@code endIndex} of the given byte array into
     * a new array, which is returned.
     *
     * @param bytes the array from which to copy the bytes
     * @param fromIndex the beginning index
     * @param endIndex the end index
     * @return a new byte array consisting of the bytes in the given range of the given byte array
     */
    public static byte[] subBytes(byte[] bytes, int fromIndex, int endIndex) {
        byte[] toReturn = new byte[endIndex - fromIndex];
        System.arraycopy(bytes, fromIndex, toReturn, 0, toReturn.length);
        return toReturn;
    }
}