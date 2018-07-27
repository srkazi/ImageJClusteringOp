
import ij.process.ImageProcessor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.Collection;
import java.util.List;

public class MultiKMeansPlusPlusImageClusterer {
    private RandomAccessibleInterval<Integer> img;
    private MultiKMeansPlusPlusClusterer<AnnotatedPixelWrapper> multiKMeansPlusPlusClusterer;
    private int [][]g;

    //FIXME: make NUM_TRIALS somehow flexible, e.g. getting this value from user interface
    public MultiKMeansPlusPlusImageClusterer( final RandomAccessibleInterval<Integer> img, int k, DistanceMeasure measure ) {
        this.img= img;
        assert img != null;
        multiKMeansPlusPlusClusterer= new MultiKMeansPlusPlusClusterer<>(new KMeansPlusPlusClusterer<>(k),Utils.NUM_TRIALS);
        extractMatrix();
    }

    public List<CentroidCluster<AnnotatedPixelWrapper>> cluster() {
        System.out.printf("Starting clustering for %d %d\n",g.length,g[0].length);
        return multiKMeansPlusPlusClusterer.cluster( Utils.annotateWithSlidingWindow(g,Utils.DEFAULT_WINDOW_SIZE) );
    }

    private void extractMatrix() {
        int m= (int)img.max(0);
        int n= (int)img.max(1);
        System.out.println(String.format("[extractMatrix] m= %d, n= %d",m,n));
        final RandomAccess<Integer> r= img.randomAccess();
        if ( r == null )
            System.out.println("r is null");
        System.out.println(String.format("[extractMatrix] obtained randomaccess"));
        g= new int[55][55];
        System.out.println(String.format("[extractMatrix] allocated matrix"));
        System.out.println(String.format("%d %d",img.min(0),img.min(1)));
        for ( int i= 0; i < 10; ++i ) {
            for ( int j = 0; j < 10; ++j ) {
                int []dd= {i+(int)img.min(0),j+(int)img.min(1),0};
                try {
                    r.setPosition(dd);
                    g[i][j] = r.get();
                } catch ( Exception ie ) {
                    System.out.println("Exception: "+ie.getClass()+" "+ie.getCause()+" "+ie.getMessage());
                }
            }
        }
        System.out.println("[Done extracting matrix]");
    }
}

