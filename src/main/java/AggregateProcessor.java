
import ij.process.ImageProcessor;

public class AggregateProcessor implements HaralickImageProcessor {
    private BasicPreprocessor bpRows, bpCols, bpMainDiag, bpAuxDiag;

    public AggregateProcessor( int [][]window ) {
        bpRows= new BasicPreprocessor(window, RowwiseTraverser.class);
        bpCols= new BasicPreprocessor(window, ColumnwiseTraverser.class);
        bpMainDiag= new BasicPreprocessor(window, MaindiagonalTraverser.class);
        bpAuxDiag= new BasicPreprocessor(window, AuxiliarydiagonalTraverser.class);
    }

    public AggregateProcessor( ImageProcessor ip ) {
        bpRows= new BasicPreprocessor(ip, RowwiseTraverser.class);
        bpCols= new BasicPreprocessor(ip, ColumnwiseTraverser.class);
        bpMainDiag= new BasicPreprocessor(ip, MaindiagonalTraverser.class);
        bpAuxDiag= new BasicPreprocessor(ip, AuxiliarydiagonalTraverser.class);
    }

    @Override
    public double getValue( TextureFeatures feature ) {
        return (bpRows.getValue(feature)+bpCols.getValue(feature)+bpMainDiag.getValue(feature)+bpAuxDiag.getValue(feature))/4.00;
    }
}
