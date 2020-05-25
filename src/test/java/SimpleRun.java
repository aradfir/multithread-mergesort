import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicArrowButton;

import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.Mapper;
import net.ericaro.surfaceplotter.surface.AbstractSurfaceModel;
import net.ericaro.surfaceplotter.surface.ArraySurfaceModel;
import net.ericaro.surfaceplotter.surface.SurfaceVertex;

public class SimpleRun {

	public void testSomething(int sampleCount, int minThread, int maxThread, int minItem, int maxItem) throws InterruptedException {
		JSurfacePanel jsp = new JSurfacePanel();

		jsp.setTitleText("Merge sort multi-thread performance scale check");

		JFrame jf = new JFrame("Graph");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().add(jsp, BorderLayout.CENTER);
		jf.pack();
		jf.setVisible(true);




		ArraySurfaceModel sm = new ArraySurfaceModel();
		sm.setDisplayXY(true);
		sm.setDisplayZ(true);
		float[][] vals=SortData.getData(sampleCount,minThread,maxThread,minItem,maxItem);
		sm.setValues(minItem-1,maxItem,minThread-1,maxThread,sampleCount,vals , null);

		//sm.autoScale();
		jsp.setModel(sm);
		// sm.doRotate();

		// canvas.doPrint();
		// sm.doCompute();
	}

	public static float f1(float x, float y) {
		// System.out.print('.');
		return (float) (Math.sin(x * x + y * y) / (x * x + y * y));
		// return (float)(10*x*x+5*y*y+8*x*y -5*x+3*y);
	}

	public static float f2(float x, float y) {
		return (float) (Math.pow(Math.tan(x * x - y * y),200) / Math.pow(x * x + y * y,0.1));
		// return (float)(10*x*x+5*y*y+15*x*y-2*x-y);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					new SimpleRun().testSomething(10,1,20,1000000,100000000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
