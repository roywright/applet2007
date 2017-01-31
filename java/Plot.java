import java.awt.*;
import java.util.*;

public abstract class Plot implements Plotable{

    protected Color color;

    public Plot(Color c) {
        color = c;
    }

	public void moreFine(){
	}

	public void lessFine(){
	}

    public void setColor(Color c) {
        color = c;
    }

    public abstract void plot(AbstractDrawer draw, Color c);

    public abstract double[] getMin();
    public abstract double[] getMax();


}