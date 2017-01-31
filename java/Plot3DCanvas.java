import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.text.*;
import javax.imageio.*;
import javax.swing.*;

import java.lang.Math.*;

public class Plot3DCanvas extends PlotCanvas implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener, BaseDependant {

    public final static Color[] COLORLIST = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA };

    private final static RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    public AbstractDrawer draw;

	private boolean legendMove = false;

    Font font = draw.DEFAULT_FONT;

    public plotVector plots = new plotVector();

    public plotableVector objects = new plotableVector();

    protected int[] mouseCurent = new int[2];

    protected int[] mouseClick = new int[2];

	public NodeSet nodeSet;

	private NodeSet dummySet;

	public int type = 0;
	public int node = 0;

	public int fineLevel = 4;

	private int lxoffset = 50;
	private int lyoffset = 50;

	public final static int COORDS = 1;
	public final static int NUMBERS = 2;
	public final int DOTS = 3;

	public int labels = NUMBERS;

	public boolean clickNodes = true;
	public boolean legend = true;
	public boolean plotting = false;
	public boolean axes = true;
	public double[] mlsRadius;
	public int beta;
	boolean eps = false;


    public void initDrawer() {
        draw = new AWTDrawer3D(this);
    }

    public void initBasenGrid(double[] min, double[] max) {
        initBasenGrid(min, max, new int[] { Base.LINEAR, Base.LINEAR, Base.LINEAR });
    }

    public void initBasenGrid() {
        initBasenGrid(new double[] { 0, 0, 0 }, new double[] { 1, 1, 1 });
    }



    public int addScatterPlot(String name) {
        if (labels > 0)
        	addPlot(new ScatterPlot(Color.RED, nodeSet));
        switch (labels){
			case COORDS:
        	for (int i = 0; i < nodeSet.count; i++)
        		addPlotable(new Label(new double[]{nodeSet.nodeX[i], nodeSet.nodeY[i], 0}, font));
        	break;
        	case NUMBERS:
        	for (int i = 0; i < nodeSet.count; i++)
        		addPlotable(new Label((" ").concat(Integer.toString(i+1)), new double[]{nodeSet.nodeX[i], nodeSet.nodeY[i], 0}, font));
        	break;
		}
        return 1;
    }

    public int addGridPlot(Color c, int t, int n) {
		Graphics2D g = (Graphics2D)getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(getSize().width / 2 - 35,getSize().height / 2 - 7, 65, 18);
		g.setColor(Color.BLACK);
		g.drawString("Working...".concat(""),getSize().width / 2 - 30,getSize().height / 2 + 7);
        return addPlot(new GridPlot3D(c, nodeSet, t, n, fineLevel, mlsRadius, beta));
    }

	public int addGridPlot() {
		return addGridPlot(Color.BLUE, type, node);
	}


    public Plot3DCanvas() {
        initBasenGrid();
        initDrawer();
        initPanel();
    }

    public Plot3DCanvas(double[] min, double[] max) {
        initBasenGrid(min, max);
        initDrawer();
        initPanel();
    }

    public Plot3DCanvas(double[] min, double[] max, int[] axesScales) {
        initBasenGrid(min, max, axesScales);
        initDrawer();
        initPanel();
    }


	public void initPlots(){
		nodeSet = new NodeSet();
		dummySet = new NodeSet();
		dummySet.add(1.,0.);
		dummySet.add(0.,1.);
        objects = new plotableVector();
        plots = new plotVector();


        setSize(panelSize[0], panelSize[1]);
        setPreferredSize(new Dimension(panelSize[0], panelSize[1]));
        setBackground(Color.white);

        addPlot(new AxesPlot(dummySet));
	}


    private void initPanel() {
		initPlots();
        setDoubleBuffered(true);

        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public Plot[] getPlots() {
        Plot[] plotarray = new Plot[plots.size()];
        plots.copyInto(plotarray);
        return plotarray;
    }

    public Plot getPlot(int i) {
        return (Plot) plots.get(i);
    }

    public Plotable[] getPlotables() {
        Plotable[] plotablearray = new Plotable[objects.size()];
        objects.copyInto(plotablearray);
        return plotablearray;
    }

    public Plotable getPlotable(int i) {
        return (Plotable) objects.get(i);
    }

    public int[] getAxesScales() {
        return base.getAxesScales();
    }

    public void setAxesScales(int[] scales) {
        base.setAxesScales(scales);
        setAutoBounds();
    }

    public void setAxeScale(int axe, int scale) {
        base.setAxesScales(axe, scale);
        setAutoBounds(axe);
    }

    public void setFixedBounds(double[] min, double[] max) {
        base.setFixedBounds(min, max);
        resetBase();
        repaint();
    }

    public void setFixedBounds(int axe, double min, double max) {
        base.setFixedBounds(axe, min, max);
        resetBase();
        repaint();
    }

    public void includeInBounds(double[] into) {
        base.includeInBounds(into);
        resetBase();
        repaint();
    }

    public void includeInBounds(Plot plot) {
        base.includeInBounds(plot.getMin());
        base.includeInBounds(plot.getMax());
        resetBase();
        repaint();
    }

    public void setAutoBounds() {
        if (plots.size() > 0) {
            Plot plot0 = this.getPlot(0);
            base.setRoundBounds(plot0.getMin(), plot0.getMax());
        } else {
            double[] min = new double[base.dimension];
            double[] max = new double[base.dimension];
            for (int i = 0; i < base.dimension; i++) {
                min[i] = 0.0;
                max[i] = 1.0;
            }
            base.setRoundBounds(min, max);
        }
        for (int i = 1; i < plots.size(); i++) {
            Plot ploti = this.getPlot(i);
            base.includeInBounds(ploti.getMin());
            base.includeInBounds(ploti.getMax());
        }
        resetBase();
        repaint();
    }

    public void setAutoBounds(int axe) {
        if (plots.size() > 0) {
            Plot plot0 = this.getPlot(0);
            base.setRoundBounds(axe, plot0.getMin()[axe], plot0.getMax()[axe]);
        } else {
            double min = 0.0;
            double max = 1.0;
            base.setRoundBounds(axe, min, max);
        }

        for (int i = 1; i < plots.size(); i++) {
            Plot ploti = this.getPlot(i);
            base.includeInBounds(axe, ploti.getMin()[axe]);
            base.includeInBounds(axe, ploti.getMax()[axe]);
        }
        resetBase();
        repaint();
    }

    public void resetBase() {
        base.setBaseCoords();
        draw.resetBaseProjection();

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof BaseDependant) {
                ((BaseDependant) (objects.get(i))).resetBase();
            }
        }
        repaint();
    }

    public void addLabel(String text, double[] where, Color c) {
    }

    public void addBaseLabel(String text, double[] where, Color c) {
    }

    public void addPlotable(Plotable p) {
        objects.add(p);
        repaint();
    }

    public void removePlotable(Plotable p) {
        objects.remove(p);
        repaint();
    }

    public void removePlotable(int i) {
        objects.remove(i);
        repaint();
    }

    public int addPlot(Plot newPlot) {
        plots.add(newPlot);
        includeInBounds(newPlot);
        return plots.size() - 1;
    }

    public void setPlot(int I, Plot p) {
        plots.setElementAt(p, I);
        repaint();
    }

    public void changePlotColor(int I, Color c) {
        getPlot(I).setColor(c);
        repaint();
    }

    public void removePlot(int I) {
        plots.remove(I);
        if (plots.size() == 0) {
            initBasenGrid();
        } else {
            setAutoBounds();
        }
    }

    public void removePlot(Plot p) {
        plots.remove(p);
        if (plots.size() == 0) {
            initBasenGrid();
        } else {
            setAutoBounds();
        }
    }

    public void removeAllPlots() {
        plots.removeAllElements();
        objects.removeAllElements();
        initBasenGrid();
    }


    public String showEPS() {

		Graphics2D g = new EpsGraphics2D();
		eps = true;
		paint(g);
		eps = false;
		return g.toString();
    }



	private void drawLegend(Graphics g) {

 		boolean flag = false;
		double[] bounds = new double[] {0.0, 1.0};
        for (int i = 0; i < plots.size(); i++)
			if (getPlot(i) instanceof GridPlot3D){
				flag = true;
				bounds = ((GridPlot3D)getPlot(i)).getBounds();
			}

		int R, G, B;
		int topColor = 220;

		double colorScale;
		int under;
		if (flag){
			for (int i = 0; i < 101; i++) {
				colorScale = i/101.0;
				under = 0;
				B = (int)(2*(0.5 - colorScale) * topColor);
				if (B < 0)
					B = 0;
				if (B > under)
					under = B;
				G = (int)(2*colorScale*topColor);
				if (colorScale >= 0.5)
					G = 2*topColor - G;
				if (G > under)
					under = G;
				R = (int)(2*(colorScale - 0.5)*topColor);
				if (R < 0)
					R = 0;
				if (R > under)
					under = R;
				under = topColor - under;
				R = R + under;
				G = G + under;
				B = B + under;

				int y = 100 + getSize().width / 8 - i;

				g.setColor(new Color(R,G,B));
				g.fillRect(getSize().width / 20 + lxoffset, y + lyoffset, getSize().width / 40, 1);
			}
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			g.setColor(Color.BLACK);
			g.drawString(nf.format(bounds[0]), 3 * getSize().width / 40 + 2 + lxoffset, 100 + getSize().width / 8 + lyoffset);
			g.drawString(nf.format(bounds[1]), 3 * getSize().width / 40 + 2 + lxoffset, 10 + getSize().width / 8 + lyoffset);
		}
	}


	boolean isInLegend(double x, double y){

 		boolean flag = false;
        for (int i = 0; i < plots.size(); i++)
			if (getPlot(i) instanceof GridPlot3D){
				flag = true;
			}
		return (flag && (x > getSize().width / 20 + lxoffset) && (x < getSize().width / 20 + lxoffset + getSize().width / 40)
		     && (y < 100 + getSize().width / 8 + lyoffset) && (y > getSize().width / 8 + lyoffset) && legend);


	}



    public void paint(Graphics gcomp) {

        Graphics2D gcomp2D = (Graphics2D) gcomp;
        if (!eps){
        gcomp2D.addRenderingHints(AALIAS);
        gcomp2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        gcomp2D.setColor(getBackground());
        gcomp2D.fillRect(0, 0, getSize().width, getSize().height);
		}
        draw.initGraphics(gcomp2D);

        for (int i = 0; i < plots.size(); i++)
            getPlot(i).plot(draw);

        for (int i = 0; i < objects.size(); i++)
            getPlotable(i).plot(draw);

		if (legend)
			drawLegend(gcomp);

    }


	public void set2D(){
		((Projection3D)(((AWTDrawer)draw).projection)).setView(- Math.PI / 2, Math.PI / 2);
        repaint();
	}




    public void mouseDragged(MouseEvent e) {
        mouseCurent[0] = e.getX();
        mouseCurent[1] = e.getY();
        e.consume();
        if (legendMove){
			lxoffset += mouseCurent[0] - mouseClick[0];
			lyoffset += mouseCurent[1] - mouseClick[1];
		}
        else {
        int[] t;
        t = new int[] { mouseCurent[0] - mouseClick[0], mouseCurent[1] - mouseClick[1] };
    	    ((AWTDrawer3D) draw).rotate(t, panelSize);
		}
        mouseClick[0] = mouseCurent[0];
        mouseClick[1] = mouseCurent[1];
        repaint();
    }


    public void mousePressed(MouseEvent e) {
        mouseClick[0] = e.getX();
        mouseClick[1] = e.getY();
        if (isInLegend(mouseClick[0], mouseClick[1]))
        	legendMove = true;
        e.consume();
    }

    public void mouseClicked(MouseEvent e) {
        mouseCurent[0] = e.getX();
        mouseCurent[1] = e.getY();
        double[] coords = ((AWTDrawer)draw).projection.fromScreenProjection(mouseClick);
        if (clickNodes){
        	if (e.getModifiers() == 4){
        		nodeSet.remove(coords[0], coords[1]);
        		mlsRadius = nodeSet.thirdRadius();
//        		reset();
        	}
        	else {
        		nodeSet.add(coords[0], coords[1]);
        		mlsRadius = nodeSet.thirdRadius();
//        		reset();
			}
		}
//        e.consume();
    }

    public void mouseReleased(MouseEvent e) {
        mouseCurent[0] = e.getX();
        mouseCurent[1] = e.getY();
        legendMove = false;
        e.consume();
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        mouseCurent[0] = e.getX();
        mouseCurent[1] = e.getY();
        e.consume();
        mouseClick[0] = mouseCurent[0];
        mouseClick[1] = mouseCurent[1];
        repaint();
    }


    public void mouseWheelMoved(MouseWheelEvent e) {
        e.consume();
        int[] origin;
        double[] ratio;
        if (e.getWheelRotation() == -1) {
            origin = new int[] { (int) (mouseCurent[0] - panelSize[0] / 3/* (2*factor) */), (int) (mouseCurent[1] - panelSize[1] / 3/* (2*factor) */) };
            ratio = new double[] { 0.666/* 1/factor, 1/factor */, 0.666 };
        } else {
            origin = new int[] { (int) (mouseCurent[0] - panelSize[0] / 1.333/* (2/factor) */),
                  (int) (mouseCurent[1] - panelSize[1] / 1.333/* (2/factor) */) };
            ratio = new double[] { 1.5, 1.5 /* factor, factor */};
        }

        draw.dilate(origin, ratio);

        for (int i = 0; i<3; i++) {
        	base.roundXmin[i] = base.roundXmin[i]*ratio[0];
        	base.roundXmax[i] = base.roundXmax[i]*ratio[0];
		}

        repaint();
    }



    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        panelSize = new int[] { (int) (getSize().getWidth()), (int) (getSize().getHeight()) };
        draw.resetBaseProjection();
        repaint();
    }

    public void componentShown(ComponentEvent e) {
    }


	public void reset(){
		removeAllPlots();
		if (node >= nodeSet.count)
			node = 0;
		if (nodeSet.count > 2){
			if (plotting)
				addGridPlot();
			if (axes)
				addPlot(new AxesPlot(nodeSet));
		}
		else
			if (axes)
				addPlot(new AxesPlot(dummySet));
		if (nodeSet.count > 0){
			addScatterPlot("");
		}
		repaint();
	}



}