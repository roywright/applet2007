import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

import java.lang.Math.*;

public abstract class PlotCanvas extends JPanel implements MouseListener, MouseMotionListener, ComponentListener, BaseDependant {

    public int[] panelSize = new int[] { 400, 400 };

	Base base;

    public void initBasenGrid(double[] min, double[] max, int[] axesScales) {
        base = new Base(min, max, axesScales);
	}

    public PlotCanvas() {
    }


}