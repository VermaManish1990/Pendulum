package com.pendulum.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by dilip on 31/12/14.<Br>
 * Media Utils has util methods <Br>
 * 1.Image Rotate
 *
 * @author Dilip.Kumar.Chaudhary
 */
public final class MediaUtil {
    /**
     * Front Camera ID
     */
    public static final int FRONT_CAMERA = 1;

    /**
     * Rotate image on the basis of it's angle
     *
     * @param source Bitmap
     * @param angle  rotate angle
     * @return Bitmap
     */
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Check whether device has front camera or not
     *
     * @return true/false
     */
    public static boolean hasFontCamera() {
        boolean camera = false;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camera = true;
                break;
            } else {
                camera = false;
            }
        }
        return camera;
    }

    public static int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        Log.d("PolicyBazaar", "Number of cameras connected " + numberOfCameras);
        for (int index = 0; index < numberOfCameras; index++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(index, info);
            Log.d("PolicyBazaar", "CameraValue for index "+index+ info.facing+"Comparison value"+Camera.CameraInfo.CAMERA_FACING_FRONT);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = index;
                break;
            }
        }
        return cameraId;
    }

    /**
     * Check whether device has Camera or not
     *
     * @param context
     * @return true/false
     */
    public static boolean hasCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
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

    private static  int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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


