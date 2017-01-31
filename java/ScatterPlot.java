import java.awt.*;

public class ScatterPlot extends Plot {

	NodeSet nodeSet;
	double minX = Double.MAX_VALUE;
	double maxX = -Double.MAX_VALUE;
	double minY = Double.MAX_VALUE;
	double maxY = -Double.MAX_VALUE;


    public ScatterPlot(Color c, NodeSet ns) {
        super(c);
        nodeSet = ns;
    }

    public void plot(AbstractDrawer draw) {
		plot(draw, Color.RED);
	}


    public void plot(AbstractDrawer draw, Color c) {
        draw.setColor(c);
        draw.setDotRadius(AbstractDrawer.DEFAULT_DOT_RADIUS);
        draw.setDotType(AbstractDrawer.ROUND);
        for (int i = 0; i < nodeSet.count; i++)
            draw.drawDot(new double[]{nodeSet.nodeX[i], nodeSet.nodeY[i], 0});
    }


	public double[] getMin(){
		double min = nodeSet.minX;
		if (nodeSet.minX > nodeSet.minY)
			min = nodeSet.minY;


		return new double[]{min, min, min};
	}


	public double[] getMax(){
		double max = nodeSet.maxX;
		if (nodeSet.maxX < nodeSet.maxY)
			max = nodeSet.maxY;


		return new double[]{max, max, max};
	}



}