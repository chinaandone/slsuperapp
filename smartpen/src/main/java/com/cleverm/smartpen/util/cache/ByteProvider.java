package com.cleverm.smartpen.util.cache;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by xiong,An android project Engineer,on 30/3/2016.
 * Data:30/3/2016  上午 12:02
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public interface ByteProvider {
    void writeTo(OutputStream os) throws IOException;
}
