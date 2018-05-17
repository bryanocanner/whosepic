package whosepic.whosepic.AppCode.ImageProcessor;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ObjectModels.FaceInfo;
import whosepic.whosepic.AppCode.ObjectModels.Image;

/**
 * Created by aligunes on 11/03/2018.
 */

public class ImageAnalyzer {
    private static ImageAnalyzer imageAnalyzer;
    private Object monitor;
    private boolean opencvLoaded;

    static {
        System.loadLibrary("MyLibs");
    }

    private ImageAnalyzer(Application app) {
        opencvLoaded = false;
        monitor = new Object();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, app,
                    new BaseLoaderCallback(app) {
                        @Override
                        public void onManagerConnected(int status) {
                            switch (status) {
                                case LoaderCallbackInterface.SUCCESS: {
                                    opencvLoaded = true;
                                    synchronized (monitor) {
                                        monitor.notifyAll();
                                    }
                                }
                                break;
                                default: {
                                    super.onManagerConnected(status);
                                }
                                break;
                            }
                        }
                    });
        } else {
            opencvLoaded = true;
        }
    }

    public static void createInstance(Application app) {
        imageAnalyzer = new ImageAnalyzer(app);
    }

    public static ImageAnalyzer getInstance() {
        return imageAnalyzer;
    }

    public ArrayList<FaceInfo> processImage(Image img) {
        if (!opencvLoaded) {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {

                }
            }
        }
        ArrayList<FaceInfo> faceInfos = new ArrayList<>();
        Mat imageMat = Imgcodecs.imread(img.getPath());
        float[][][] res = FaceDetector.faceDetection(imageMat.getNativeObjAddr());
        for (int i = 0; i < res.length; i++) {
            double [][] info = new double[res[i].length][res[i].length];
            for (int j = 0; j < res[i].length; j++) {
                for (int k = 0; k < res[i][j].length; k++) {
                    info[j][k] = res[i][j][k];
                }
            }
            faceInfos.add(new FaceInfo(info));
        }
        return faceInfos;
    }
}
