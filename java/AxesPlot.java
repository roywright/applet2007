import java.awt.*;

public class AxesPlot extends Plot {

	private NodeSet nodeSet;

    public AxesPlot(NodeSet ns) {
        super(Color.BLACK);
        nodeSet = ns;
    }

    public void plot(AbstractDrawer draw) {
		plot(draw, Color.BLACK);
	}


    public void plot(AbstractDrawer draw, Color c) {
		draw.setColor(Color.BLACK);
		draw.drawLine(new double[]{0., 0., 0.}, new double[]{nodeSet.maxX, 0., 0.});
		draw.drawLine(new double[]{0., 0., 0.}, new double[]{0., nodeSet.maxY, 0});
		draw.drawLine(new double[]{0., 0., 0.}, new double[]{0., 0., 1.});
    }


	public double[] getMin(){
		return new double[]{0., 0., 0.};
	}


	public double[] getMax(){
		double max = nodeSet.maxX;
		if (nodeSet.maxX < nodeSet.maxY)
			max = nodeSet.maxY;


		return new double[]{max, max, max};
	}





}