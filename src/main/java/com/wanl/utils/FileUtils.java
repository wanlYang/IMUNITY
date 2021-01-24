/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {
    @SuppressWarnings("resource")
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = (new FileInputStream(source)).getChannel();
            outputChannel = (new FileOutputStream(dest)).getChannel();
            outputChannel.transferFrom(inputChannel, 0L, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
}
