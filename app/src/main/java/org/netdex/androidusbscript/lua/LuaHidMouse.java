package org.netdex.androidusbscript.lua;

import org.netdex.androidusbscript.function.DeviceStream;
import org.netdex.androidusbscript.util.FileSystem;

import java.io.Closeable;
import java.io.IOException;


public class LuaHidMouse extends DeviceStream implements Closeable {

    public LuaHidMouse(FileSystem fs, String devicePath) throws IOException {
        super(fs, devicePath);
    }

    /**
     * A        B        C        D
     * XXXXXXXX XXXXXXXX XXXXXXXX XXXXXXXX
     * <p>
     * A: Mouse button mask
     * B: Mouse X-offset
     * C: Mouse Y-offset
     * D: Mouse wheel offset
     *
     * @param offset HID mouse bytes
     */
    public void raw(byte... offset) throws IOException, InterruptedException {
        byte[] buffer = new byte[4];
        if (offset.length > 4)
            throw new IllegalArgumentException("Too many parameters in HID report");
        System.arraycopy(offset, 0, buffer, 0, offset.length);
        this.write(buffer);
    }

    public void click(byte mask, long duration) throws IOException, InterruptedException {
        raw(mask);
        if (duration > 0) {
            Thread.sleep(duration);
        }
        raw();
    }

    public void move(byte dx, byte dy) throws IOException, InterruptedException {
        raw((byte) 0, dx, dy);
    }

    public void scroll(byte offset) throws IOException, InterruptedException {
        raw((byte) 0, (byte) 0, (byte) 0, offset);
    }
}