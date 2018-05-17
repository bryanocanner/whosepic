package whosepic.whosepic.AppCode.ImageLearner;

import whosepic.whosepic.AppCode.ObjectModels.FaceInfo;

/**
 * Created by aligunes on 11/03/2018.
 */

public class SimilarityFinder {
    private static SimilarityFinder similarityFinder;

    private SimilarityFinder() {

    }

    public static void createInstance() {
        similarityFinder = new SimilarityFinder();
    }

    public static SimilarityFinder getInstance() {
        return similarityFinder;
    }

    public double getSimilarity(FaceInfo f1, FaceInfo f2) {
        double [][] d1 = f1.getInfo();
        double [][] d2 = f2.getInfo();
        double dot = 0;
        double a = 0;
        double b = 0;
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                dot += d1[i][j] * d2[i][j];
                a += Math.pow(d1[i][j], 2);
                b += Math.pow(d2[i][j], 2);
            }
        }
        double res = Math.acos(dot / (Math.sqrt(a) * Math.sqrt(b)));
        return res;
    }
}
