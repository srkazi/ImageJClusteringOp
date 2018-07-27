import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.io.Opener;
import ij.plugin.Grid;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.ImgPlus;
import net.imagej.display.ImageDisplay;
import net.imagej.display.OverlayService;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.imageplus.ImagePlusImg;
import net.imglib2.img.imageplus.ImagePlusImgs;
import org.scijava.app.StatusService;
import org.scijava.command.CommandService;
import org.scijava.log.LogService;
import org.scijava.thread.ThreadService;
import org.scijava.ui.UIService;
import org.scijava.util.RealRect;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class GLCMClusteringFrame extends JFrame {

	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

    private OpService ops;
	private LogService log;
	private StatusService status;
	private CommandService cmd;
	private ThreadService thread;
	private UIService ui;
	private ImageDisplay display;
	private OverlayService overlayService;
	private DatasetService datasetService;
	private RandomAccessibleInterval<Integer> img;

	private final JPanel contentPanel= new JPanel();
	private final JTabbedPane tabbedPane= new JTabbedPane();
	private JMenuBar bar;

	public GLCMClusteringFrame() {
		setBounds(100,100,300,300);

		JComponent panelForFuzzyKMeans= makeFuzzyKMeansTab();
		tabbedPane.addTab("Fuzzy K-Means",panelForFuzzyKMeans);
		JComponent panelForDBSCAN= makeDBSCANTab("DBSCAN");
		tabbedPane.addTab("DBSCAN",panelForDBSCAN);
		JComponent panelForMultiKMeansPP= makeMultiKMeansTab();
		tabbedPane.addTab("Multi-K-Means++",panelForMultiKMeansPP);

		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		makeMenuBar();
	}

	private JComponent makeMultiKMeansTab() {
	    JPanel panel= new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel label= new JLabel("#of clusters");
		GridBagConstraints c= new GridBagConstraints();
		c.gridx= 0;
		c.gridy= 0;
		c.gridwidth= 1;
		c.gridheight= 1;
		c.weightx= 0.3;
		panel.add(label,c);

		JFormattedTextField formattedTextField= new JFormattedTextField();
		c= new GridBagConstraints();
		c.gridx= 1;
		c.gridy= 0;
		c.gridwidth= 4;
		c.gridheight= 1;
		c.fill= GridBagConstraints.HORIZONTAL;
		c.anchor= GridBagConstraints.LINE_END;
		c.weightx= 0.7;
		panel.add(formattedTextField,c);

		JLabel label2= new JLabel("maximum #of iterations");
		c= new GridBagConstraints();
		c.gridx= 0;
		c.gridy= 1;
		c.gridwidth= 1;
		c.gridheight= 1;
		c.weightx= 0.3;
		panel.add(label2,c);

		JFormattedTextField formattedTextField2= new JFormattedTextField();
		c= new GridBagConstraints();
		c.gridx= 1;
		c.gridy= 1;
		c.gridwidth= 4;
		c.gridheight= 1;
		c.fill= GridBagConstraints.HORIZONTAL;
		c.anchor= GridBagConstraints.LINE_END;
		c.weightx= 0.7;
		panel.add(formattedTextField2,c);

		JLabel label3= new JLabel("#of trials");
		c= new GridBagConstraints();
		c.gridx= 0;
		c.gridy= 2;
		c.gridwidth= 1;
		c.gridheight= 1;
		c.weightx= 0.3;
		panel.add(label3,c);

		JFormattedTextField formattedTextField3= new JFormattedTextField();
		c= new GridBagConstraints();
		c.gridx= 1;
		c.gridy= 2;
		c.gridwidth= 4;
		c.gridheight= 1;
		c.fill= GridBagConstraints.HORIZONTAL;
		c.anchor= GridBagConstraints.LINE_END;
		c.weightx= 0.7;
		panel.add(formattedTextField3,c);

		final JButton clusterIt= new JButton("Cluster");
		clusterIt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//MultiKMeansPlusPlusImageClusterer clusterer= new MultiKMeansPlusPlusImageClusterer()
				thread.run( ()-> {
					int k= 3;
					try {
						k = Integer.parseInt(formattedTextField.getText());
					} catch ( NumberFormatException nfe ) {
						throw nfe;
					}
					log.info("Read k= "+k);
					multiKMeansPPClustering(k);
				});
			}
		});
		c= new GridBagConstraints();
		c.gridx= 0;
		c.gridy= 3;
		c.gridwidth= 1;
		c.gridheight= 1;
		c.fill= GridBagConstraints.HORIZONTAL;
		c.anchor= GridBagConstraints.CENTER;
		c.weightx= 0.7;
		panel.add(clusterIt,c);

		return panel;
	}

	public void multiKMeansPPClustering( int k ) {
	    /*
		RealRect r= overlayService.getSelectionBounds(display);
		List<Dataset> list= datasetService.getDatasets();
		Dataset dataset= list.get(0);
		Img img= dataset.getImgPlus().getImg();
		*/
		//ImageProcessor ip= new ByteProcessor(img);
		//Map<String,Object> map= imgPlus.getProperties();
		//ImagePlus imagePlus= IJ.getImage();
		//imageProcessor= imagePlus.getProcessor();
		MultiKMeansPlusPlusImageClusterer clusterer= new MultiKMeansPlusPlusImageClusterer(img,k,null);
		log.info("[Launching Clustering]");
		//clusterer.cluster();
		log.info("[DONE Clustering]");
	}

	private JComponent makeDBSCANTab( String dbscan ) {
		JPanel panel= new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel label= new JLabel("epsilon");
		GridBagConstraints c= new GridBagConstraints();
		c.gridx= 0;
		c.gridy= 0;
		c.gridwidth= 1;
		c.gridheight= 1;
		c.weightx= 0.3;
		panel.add(label,c);

		JFormattedTextField formattedTextField= new JFormattedTextField();
		c= new GridBagConstraints();
		c.gridx= 1;
		c.gridy= 0;
		c.gridwidth= 4;
		c.gridheight= 1;
		c.fill= GridBagConstraints.HORIZONTAL;
		c.anchor= GridBagConstraints.SOUTH;
		c.weightx= 0.7;
		panel.add(formattedTextField,c);

		JLabel label2= new JLabel("minimum # of points");
		c= new GridBagConstraints();
		c.gridx= 0;
		c.gridy= 1;
		c.gridwidth= 1;
		c.gridheight= 1;
		c.weightx= 0.3;
		panel.add(label2,c);

		JFormattedTextField formattedTextField2= new JFormattedTextField();
		c= new GridBagConstraints();
		c.gridx= 1;
		c.gridy= 1;
		c.gridwidth= 4;
		c.gridheight= 1;
		c.fill= GridBagConstraints.HORIZONTAL;
		c.anchor= GridBagConstraints.LINE_END;
		c.weightx= 0.7;
		panel.add(formattedTextField2,c);

		return panel;
	}

	protected JComponent makeTextPanel( String text ) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	protected void makeMenuBar() {
		bar= new JMenuBar();
		JMenu menu= new JMenu("Distance Measures");
		ButtonGroup group= new ButtonGroup();

		JRadioButtonMenuItem item= new JRadioButtonMenuItem("Euclidean");
		group.add(item);
		menu.add(item);

		item= new JRadioButtonMenuItem("Chebyshev");
		group.add(item);
		menu.add(item);

		item= new JRadioButtonMenuItem("Canberra");
		group.add(item);
		menu.add(item);

		item= new JRadioButtonMenuItem("Manhattan");
		group.add(item);
		menu.add(item);

		item= new JRadioButtonMenuItem("Earth Mover's");
		group.add(item);
		menu.add(item);

		bar.add(menu);
		this.setJMenuBar(bar);
	}

	protected JComponent makeFuzzyKMeansTab() {
		JPanel panel= new JPanel(false);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints;
		/*
		if ( shouldFill ) {
			constraints.fill= GridBagConstraints.HORIZONTAL;
		}
		if ( shouldWeightX ) {
			constraints.weightx= 0.5;
		}
		*/
		JLabel label1= new JLabel("Number of clusters");
		constraints= new GridBagConstraints();
		constraints.gridx= 0;
		constraints.gridy= 0;
		constraints.gridwidth= 1;
		constraints.gridheight= 1;
		constraints.weightx= 0.3;
		panel.add(label1,constraints);

		JSlider numberOfClusters= new JSlider(2,127);
		constraints= new GridBagConstraints();
		constraints.gridx= 1;
		constraints.gridy= 0;
		constraints.gridwidth= 4;
		constraints.gridheight= 1;
		constraints.fill= GridBagConstraints.HORIZONTAL;
		constraints.anchor= GridBagConstraints.LINE_END;
		constraints.weightx= 0.7;
		panel.add(numberOfClusters,constraints);

		JLabel label2= new JLabel("Fuzziness");
		constraints= new GridBagConstraints();
		constraints.gridx= 0;
		constraints.gridy= 1;
		constraints.gridwidth= 1;
		constraints.gridheight= 1;
		constraints.weightx= 0.3;
		panel.add(label2,constraints);

		JFormattedTextField textField= new JFormattedTextField();
		constraints= new GridBagConstraints();
		constraints.gridx= 1;
		constraints.gridy= 1;
		constraints.gridwidth= 4;
		constraints.gridheight= 1;
		constraints.fill= GridBagConstraints.HORIZONTAL;
		constraints.anchor= GridBagConstraints.LINE_END;
		constraints.weightx= 0.7;
		panel.add(textField,constraints);

		JLabel label3= new JLabel("Number of iterations");
		constraints= new GridBagConstraints();
		constraints.gridx= 0;
		constraints.gridy= 2;
		constraints.gridwidth= 1;
		constraints.gridheight= 1;
		constraints.weightx= 0.3;
		panel.add(label3,constraints);

		JFormattedTextField textField2= new JFormattedTextField();
		constraints= new GridBagConstraints();
		constraints.gridx= 1;
		constraints.gridy= 2;
		constraints.gridwidth= 4;
		constraints.gridheight= 1;
		constraints.fill= GridBagConstraints.HORIZONTAL;
		constraints.anchor= GridBagConstraints.LINE_END;
		constraints.weightx= 0.7;
		panel.add(textField2,constraints);

		return panel;
	}

	public static void main(final String[] args) {
		try {
			final GLCMClusteringFrame frame= new GLCMClusteringFrame();
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		}
		catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

	public OpService getOps() {
		return ops;
	}

	public void setOps(final OpService ops) {
		this.ops = ops;
	}

	public LogService getLog() {
		return log;
	}

	public void setLog(final LogService log) {
		this.log = log;
	}

	public StatusService getStatus() {
		return status;
	}

	public void setStatus(final StatusService status) {
		this.status = status;
	}

	public CommandService getCommand() {
		return cmd;
	}

	public void setCommand(final CommandService command) {
		this.cmd = command;
	}

	public ThreadService getThread() {
		return thread;
	}

	public void setThread(final ThreadService thread) {
		this.thread = thread;
	}

	public UIService getUi() {
		return ui;
	}

	public void setUi(final UIService ui) {
		this.ui = ui;
	}

	public void setDisplay( final ImageDisplay display ) {
		this.display= display;
	}

	public ImageDisplay getDisplay() {
		return display;
	}

	public void setOverlayService(OverlayService overlayService) {
		this.overlayService = overlayService;
	}

	public DatasetService getDatasetService() {
		return datasetService;
	}

	public void setDatasetService(DatasetService datasetService) {
		this.datasetService = datasetService;
	}

	public RandomAccessibleInterval<Integer> getImg() {
		return img;
	}

	public void setImg(RandomAccessibleInterval<Integer> img) {
		this.img = img;
	}
}
