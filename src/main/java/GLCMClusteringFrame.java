import ij.plugin.Grid;
import net.imagej.ops.OpService;
import org.scijava.app.StatusService;
import org.scijava.command.CommandService;
import org.scijava.log.LogService;
import org.scijava.thread.ThreadService;
import org.scijava.ui.UIService;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

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

	private final JPanel contentPanel= new JPanel();
	private final JTabbedPane tabbedPane= new JTabbedPane();

	public GLCMClusteringFrame() {
		setBounds(100,100,300,300);

		JComponent panelForFuzzyKMeans= makeFuzzyKMeansTab();
		tabbedPane.addTab("Fuzzy K-Means",panelForFuzzyKMeans);
		JComponent panelForDBSCAN= makeTextPanel("DBSCAN");
		tabbedPane.addTab("DBSCAN",panelForDBSCAN);

		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	protected JComponent makeTextPanel( String text ) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
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

		JMenuBar bar= new JMenuBar();
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

		bar.add(menu);
		this.setJMenuBar(bar);

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

}
