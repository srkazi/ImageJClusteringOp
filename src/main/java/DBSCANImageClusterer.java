
import ij.process.ImageProcessor;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.Collection;
import java.util.List;


public class DBSCANImageClusterer {

    private DBSCANClusterer<AnnotatedPixelWrapper> dbscanClusterer;
    private int [][]g;

    //FIXME: make eps adjustable from the UI
    public DBSCANImageClusterer( final ImageProcessor ip, DistanceMeasure measure ) {
        dbscanClusterer= new DBSCANClusterer<>(1e-9,3,measure==null?new EuclideanDistance():measure);
        g= ip.getIntArray();
    }

    public List<Cluster<AnnotatedPixelWrapper>> cluster( Collection<AnnotatedPixelWrapper> points ) {
        return dbscanClusterer.cluster( Utils.annotateWithSlidingWindow(g,Utils.DEFAULT_WINDOW_SIZE) );
    }


}

