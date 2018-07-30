import io.scif.img.IO;
import net.imagej.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;

public class MWE {
    public MWE() {
		Img<FloatType> img= IO.openImgs( "/home/sj/IdeaProjects/tutorials/maven-projects/create-a-new-op/src/main/resources/128_6_031.bmp",new FloatType()).get(0);
		//ImageJFunctions.show(img);
	}
	public static void main(final String[] args) {
		final ImageJ ij= new ImageJ();
		//ij.launch(args);
		new MWE();
	}
}

