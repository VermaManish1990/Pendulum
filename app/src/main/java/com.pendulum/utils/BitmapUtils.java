package com.pendulum.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Various bitmap utilities
 *
 * @author alessandro
 */
public class BitmapUtils {

    /**
     * Resize a bitmap
     *
     * @param input
     * @param destWidth
     * @param destHeight
     * @return
     * @throws OutOfMemoryError
     */
    public static Bitmap resizeBitmap(final Bitmap input, int destWidth, int destHeight) throws OutOfMemoryError {
        return resizeBitmap(input, destWidth, destHeight, 0);
    }

    /**
     * Resize a bitmap object to fit the passed width and height
     *
     * @param input      The bitmap to be resized
     * @param destWidth  Desired maximum width of the result bitmap
     * @param destHeight Desired maximum height of the result bitmap
     * @return A new resized bitmap
     * @throws OutOfMemoryError if the operation exceeds the available vm memory
     */
    public static Bitmap resizeBitmap(final Bitmap input, int destWidth, int destHeight, int rotation) throws OutOfMemoryError {

        int dstWidth = destWidth;
        int dstHeight = destHeight;
        final int srcWidth = input.getWidth();
        final int srcHeight = input.getHeight();

        if (rotation == 90 || rotation == 270) {
            dstWidth = destHeight;
            dstHeight = destWidth;
        }

        boolean needsResize = false;
        float p;
        if ((srcWidth > dstWidth) || (srcHeight > dstHeight)) {
            needsResize = true;
            if ((srcWidth > srcHeight) && (srcWidth > dstWidth)) {
                p = (float) dstWidth / (float) srcWidth;
                dstHeight = (int) (srcHeight * p);
            } else {
                p = (float) dstHeight / (float) srcHeight;
                dstWidth = (int) (srcWidth * p);
            }
        } else {
            dstWidth = srcWidth;
            dstHeight = srcHeight;
        }

        if (needsResize || rotation != 0) {
            Bitmap output;

            if (rotation == 0) {
                output = Bitmap.createScaledBitmap(input, dstWidth, dstHeight, true);
            } else {
                Matrix matrix = new Matrix();
                matrix.postScale((float) dstWidth / srcWidth, (float) dstHeight / srcHeight);
                matrix.postRotate(rotation);
                output = Bitmap.createBitmap(input, 0, 0, srcWidth, srcHeight, matrix, true);
            }
            return output;
        } else
            return input;
    }

    public static Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);
        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);
        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(picturePath, sizeOptions);
            ExifInterface ei = new ExifInterface(picturePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return MediaUtil.rotateImage(bitmap, 90);

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return MediaUtil.rotateImage(bitmap, 270);

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return MediaUtil.rotateImage(bitmap, 180);

                default:
                    return bitmap;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (OutOfMemoryError e) {
        }
        return null;
    }

    public static Bitmap getScaledBitmapWithLandOrient(String picturePath, int width, int height) {

        try {
            BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
            sizeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, sizeOptions);
            int inSampleSize = calculateInSampleSize(sizeOptions, width, height);
            sizeOptions.inJustDecodeBounds = false;
            sizeOptions.inSampleSize = inSampleSize;
            Bitmap bitmap;

            bitmap = BitmapFactory.decodeFile(picturePath, sizeOptions);
            ExifInterface ei = new ExifInterface(picturePath);
            int wid = bitmap.getWidth();
            int hig = bitmap.getHeight();
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (wid > hig) {
                Bitmap bit = MediaUtil.rotateImage(bitmap, 90);
                FileOutputStream fos = new FileOutputStream(picturePath);
                bit.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return bit;
            }
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return MediaUtil.rotateImage(bitmap, 90);

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return MediaUtil.rotateImage(bitmap, 270);

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return MediaUtil.rotateImage(bitmap, 180);

                default:
                    return bitmap;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (OutOfMemoryError e) {
        }
        return null;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
