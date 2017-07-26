/* 
 * Copyright 2014 OpenMarket Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.matrix.androidsdk.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.matrix.androidsdk.util.Log;

import android.webkit.MimeTypeMap;

import org.matrix.androidsdk.rest.model.ImageInfo;

import java.io.File;

/**
 * Static content utility methods.
 */
public class ContentUtils {
    private static final String LOG_TAG = "ContentUtils";

    /**
     * Build an ImageInfo object based on the image at the given path.
     *
     * @param filePath the path to the image in storage
     * @return the image info
     */
    public static ImageInfo getImageInfoFromFile(String filePath) {
        ImageInfo imageInfo = new ImageInfo();
        try {
            Bitmap imageBitmap = BitmapFactory.decodeFile(filePath);
            imageInfo.w = imageBitmap.getWidth();
            imageInfo.h = imageBitmap.getHeight();

            File file = new File(filePath);
            imageInfo.size = file.length();

            imageInfo.mimetype = getMimeType(filePath);
        } catch (OutOfMemoryError oom) {
            Log.e(LOG_TAG, "## getImageInfoFromFile() : oom");
        }

        return imageInfo;
    }

    public static String getMimeType(String filePath) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase());
    }

    /**
     * Delete a directory with its content
     *
     * @param directory the base directory
     * @return true if the directory is deleted
     */
    public static boolean deleteDirectory(File directory) {
        // sanity check
        if (null == directory) {
            return false;
        }

        boolean succeed = true;

        if (directory.exists()) {
            File[] files = directory.listFiles();

            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        succeed &= deleteDirectory(files[i]);
                    } else {
                        succeed &= files[i].delete();
                    }
                }
            }
        }
        if (succeed) {
            return (directory.delete());
        } else {
            return false;
        }
    }

    /**
     * Recursive method to compute a directory sie
     *
     * @param directory the directory.
     * @return the directory size in bytes.
     */
    public static long getDirectorySize(File directory) {
        long size = 0;

        File[] files = directory.listFiles();

        if (null != files) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                if (!file.isDirectory()) {
                    size += file.length();
                } else {
                    size += getDirectorySize(file);
                }
            }
        }

        return size;
    }

}
