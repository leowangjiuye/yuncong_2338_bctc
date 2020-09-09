package com.cloudwalk.livenesscameraproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Covert {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static byte[] BitmapToBGR(Bitmap image) {
        int bits = 4;//ARGB
        int bytes = image.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer
        image.copyPixelsToBuffer(buffer); // Move the byte data to the buffer
        byte[] temp = buffer.array(); // Get the underlying array containing the data.
        byte[] pixels = new byte[(temp.length / bits) * 3]; // Allocate for BGR
        // Copy pixels into place
        int count = temp.length / bits;
        for (int i = 0; i < count; ++i) {
            pixels[i * 3 + 0] = temp[i * 4 + 0]; // B
            pixels[i * 3 + 1] = temp[i * 4 + 1]; // G
            pixels[i * 3 + 2] = temp[i * 4 + 2]; // R
        }
        buffer.clear();
        buffer = null;
        temp = null;
        return pixels;
    }

    public static Bitmap BGRToBitmap(byte[] data, int width, int height) {
        try {
            int frameSize = width * height;
            int[] argb = new int[frameSize];

            for (int i = 0; i < height * width; i++) {
                int b = Math.max(0, Math.min(255, (int) data[i * 3 + 0] & 0x00FF));
                int g = Math.max(0, Math.min(255, (int) data[i * 3 + 1] & 0x00FF));
                int r = Math.max(0, Math.min(255, (int) data[i * 3 + 2] & 0x00FF));

                argb[i] = 0xff000000 + (r << 16) + (g << 8) + b;
            }

            Bitmap bmp = Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888);
            argb = null;
            return bmp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap RGBToBitmap(byte[] data, int width, int height) {
        try {
            int frameSize = width * height;
            int[] argb = new int[frameSize];

            for (int i = 0; i < height * width; i++) {
                int r = Math.max(0, Math.min(255, (int) data[i * 3 + 0] & 0x00FF));
                int g = Math.max(0, Math.min(255, (int) data[i * 3 + 1] & 0x00FF));
                int b = Math.max(0, Math.min(255, (int) data[i * 3 + 2] & 0x00FF));

                argb[i] = 0xff000000 + (r << 16) + (g << 8) + b;
            }

            Bitmap bmp = Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888);
            argb = null;
            return bmp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void RecyleBitmap(Bitmap bitmap) {
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static Bitmap rotate(Bitmap source, int angle, boolean turnHor, boolean turnVer) {
        return rotate(source, angle, false, turnHor, turnVer);
    }

    public static Bitmap rotate(Bitmap source, int angle, boolean isClockwise, boolean turnHor, boolean turnVer) {
        if (null != source) {
            Matrix m = new Matrix();

            if (turnHor && !turnVer)
                m.postScale(-1, 1);//水平镜像
            if (!turnHor && turnVer)
                m.postScale(1, -1);//垂直镜像
            if (turnHor && turnVer)
                m.postScale(-1, -1);//垂直镜像

            if (angle > 0) {
                if (isClockwise)
                    m.postRotate(angle % 360);//顺时针旋转
                else
                    m.postRotate(360 - angle % 360);//逆时针旋转
            }

            try {
                Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);


                return result;
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return source;
    }

    public static boolean saveToJpeg(Bitmap bitmap, String path, int quality) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap decodeBitmap(byte[] is, int inSampleSize) {
        try {
            //BitmapFactory.Options optTmp = new BitmapFactory.Options();
            //optTmp.inJustDecodeBounds = true;
            //BitmapFactory.decodeByteArray(is, 0, is.length, optTmp);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            //opts.inJustDecodeBounds = false;
            opts.inSampleSize = inSampleSize;
            //opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //opts.inPurgeable = true;
            //opts.inInputShareable = true;
            opts.inTempStorage = new byte[16 * 1024];
            Bitmap bitmap = BitmapFactory.decodeByteArray(is, 0, is.length, opts);
            opts = null;
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        //return BitmapFactory.decodeStream(is);
    }

    public static byte[] rotateNV21Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    public static byte[] rotateNV21Degree270(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }// Rotate the U and V color components
        i = imageWidth * imageHeight;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i++;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i++;
            }
        }
        return yuv;
    }

    public static int[] convertYUV420_NV21toRGB8888(byte[] data, int width, int height) {
        int size = width * height;
        int offset = size;
        int[] pixels = new int[size];
        int u, v, y1, y2, y3, y4;

        // i percorre os Y and the final pixels
        // k percorre os pixles U e V
        for (int i = 0, k = 0; i < size; i += 2, k += 2) {
            y1 = data[i] & 0xff;
            y2 = data[i + 1] & 0xff;
            y3 = data[width + i] & 0xff;
            y4 = data[width + i + 1] & 0xff;

            u = data[offset + k] & 0xff;
            v = data[offset + k + 1] & 0xff;
            u = u - 128;
            v = v - 128;

            pixels[i] = convertYUVtoRGB(y1, u, v);
            pixels[i + 1] = convertYUVtoRGB(y2, u, v);
            pixels[width + i] = convertYUVtoRGB(y3, u, v);
            pixels[width + i + 1] = convertYUVtoRGB(y4, u, v);

            if (i != 0 && (i + 2) % width == 0)
                i += width;
        }

        return pixels;
    }

    private static int convertYUVtoRGB(int y, int u, int v) {
        int r = Math.max(0, Math.min(255, y + (int) (1.402f * v)));
        int g = Math.max(0, Math.min(255, y - (int) (0.344f * u + 0.714f * v)));
        int b = Math.max(0, Math.min(255, y + (int) (1.772f * u)));
        return 0xff000000 | (b << 16) | (g << 8) | r;
    }

    public static int fitSampleSize(int nOrgW, int nOrgH, int minW, int minH, int maxW, int maxH) {
        int nWidth = Math.max(minW, Math.min(nOrgW, maxW));
        int nHeight = Math.max(minH, Math.min(nOrgH, maxH));

        if ((minW <= nOrgW && nOrgW <= maxW) && (minH <= nOrgH && nOrgH <= maxH)) {
            System.out.println(String.format("inSampleSize=%d %dx%d", 1, nOrgW, nOrgH));
            return 1;
        }

        double beW = (double) nOrgW / (double) nWidth;
        if (beW < 1)
            beW = 1;
        double beH = (double) nOrgH / (double) nHeight;
        if (beH < 1)
            beH = 1;
        double fBe = Math.max(beW, beH);

        int inSampleSize = (int) Math.ceil(fBe);

        int fitW = (int) Math.floor(nOrgW / inSampleSize);
        int fitH = (int) Math.floor(nOrgH / inSampleSize);

        System.out.println(String.format("inSampleSize=%d %dx%d", inSampleSize, fitW, fitH));
        return inSampleSize;
    }

    public static Map<String, String> detmineScaleSizeAndRect(int width, int height) {
        int left = 0;
        int top = 0;
        int destW = 0;
        int destH = 0;

        float ratioOld = 1.0f * 640 / 480;
        float ratioNew = 1.0f * width / height;

        if (width >= height) {
            float ratio1 = 1.0f * width / 640;
            float ratio2 = 1.0f * height / 480;

            if (Math.abs(ratioOld - ratioNew) < 0.001) {
                destW = (int) (width / ratio2);
                destH = (int) (height / ratio2);

            } else {

                destW = (int) (width / ratio2);
                destH = (int) (height / ratio2);

                left = (destW - 640) / 2;
                top = (destH - 480) / 2;
            }
        } else {
            float ratio1 = 1.0f * width / 480;
            float ratio2 = 1.0f * height / 640;

            if (Math.abs(ratioOld - (1.0f / ratioNew)) < 0.001) {
                destW = (int) (width / ratio1);
                destH = (int) (height / ratio1);

            } else {
                destW = (int) (width / ratio1);
                destH = (int) (height / ratio1);

                left = (destW - 480) / 2;
                top = (destH - 640) / 2;
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("destW", "" + destW);
        map.put("destH", "" + destH);
        map.put("left", "" + left);
        map.put("top", "" + top);
        map.put("right", "" + (destW - left));
        map.put("bottom", "" + (destH - top));
        return map;
    }

}
