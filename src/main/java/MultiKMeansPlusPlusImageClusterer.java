
import ij.process.ImageProcessor;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.Collection;
import java.util.List;

public class MultiKMeansPlusPlusImageClusterer {
    private MultiKMeansPlusPlusClusterer<AnnotatedPixelWrapper> multiKMeansPlusPlusClusterer;
    private int [][]g;

    //FIXME: make NUM_TRIALS somehow flexible, e.g. getting this value from user interface
    public MultiKMeansPlusPlusImageClusterer(final ImageProcessor ip, int k, double fuzziness, DistanceMeasure measure ) {
        multiKMeansPlusPlusClusterer= new MultiKMeansPlusPlusClusterer<>(new KMeansPlusPlusClusterer<>(k),Utils.NUM_TRIALS);
        g= ip.getIntArray();
    }

    public List<CentroidCluster<AnnotatedPixelWrapper>> cluster(Collection<AnnotatedPixelWrapper> points ) {
        return multiKMeansPlusPlusClusterer.cluster( Utils.annotateWithSlidingWindow(g,Utils.DEFAULT_WINDOW_SIZE) );
    }
}

