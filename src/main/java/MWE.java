import io.scif.img.IO;
import net.imagej.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import io.scif.img.IO;
import net.imagej.ImageJ;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;

import java.util.Random;

public class MWE {
	public MWE() {
		Img<UnsignedByteType> img = ArrayImgs.unsignedBytes(64, 64, 3);
		RandomAccess<UnsignedByteType> ra = img.randomAccess();

		Random random = new Random();

		long[] position = new long[3];
		for (int x = 0; x < img.dimension(0); x ++) {
			for (int y = 0; y < img.dimension(1); y ++) {
				position[0] = x;
				position[1] = y;


				position[2] = 0;
				ra.setPosition(position);
				ra.get().set(random.nextInt(255));
				position[2] = 1;
				ra.setPosition(position);
				ra.get().set(random.nextInt(255));
				position[2] = 2;
				ra.setPosition(position);
				ra.get().set(random.nextInt(255));
			}
		}

		ImagePlus imp = ImageJFunctions.wrap(img, "result");
		imp = new Duplicator().run(imp);
		imp.show();
		IJ.run("Stack to RGB", "");
	}
	public static void main(final String[] args) {
		final ImageJ ij= new ImageJ();
		ij.ui().showUI();

		//ij.launch(args);
		new MWE();
	}
}


/*
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
*/
