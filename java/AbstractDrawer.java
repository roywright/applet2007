import java.awt.*;

public abstract class AbstractDrawer {

    //TODO comment this source for other implementations (java3d, jogl or vtk ?)

    protected Font font = DEFAULT_FONT;

    protected Color color = DEFAULT_COLOR;

    protected int dot_type = ROUND;

    protected int dot_radius = DEFAULT_DOT_RADIUS;

    protected boolean[][] dot_pattern = DOT_TRIANGLE_PATTERN;

    public PlotCanvas canvas;

    protected Graphics2D comp2D;

    public final static int ROUND = 1;

    public final static int CROSS = 2;

    public final static int PATTERN = 0;

    public final static int DEFAULT_DOT_RADIUS = 2;

    public final static boolean[][] DOT_TRIANGLE_PATTERN = { {} };

    public final static boolean[][] DOT_SQUARE_PATTERN = { {} };

    public final static Font DEFAULT_FONT = new Font("BitStream Vera Sans", Font.PLAIN, 14);

    public final static Color DEFAULT_COLOR = Color.BLACK;

    public AbstractDrawer(PlotCanvas _canvas) {
        canvas = _canvas;
    }

    /**
     * Method used to initialize drawer to DEFAULT values
     */
    public void initGraphics(Graphics2D _comp2D) {
        comp2D = _comp2D;
    }

    /**
     * Method used to reinitialize the plot when the base has changed (bounds or scale)
     */
    public abstract void resetBaseProjection();

    public void setColor(Color c) {
        color = c;
    }

    public void setFont(Font f) {
        font = f;
    }

    public void setDotType(int _dot_type) {
        dot_type = _dot_type;
    }

    public void setDotRadius(int _dot_radius) {
        dot_radius = _dot_radius;
    }

    public void setDotPattern(boolean[][] _dot_pattern) {
        dot_pattern = _dot_pattern;
    }

    public Color getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    public int getDotType() {
        return dot_type;
    }

    public int getDotRadius() {
        return dot_radius;
    }

    public boolean[][] getDotPattern() {
        return dot_pattern;
    }

    /**
     * Returns the screen coordinates coresponding to plot coordinates
     * Used to test if mouse is pointing on a plot.
     * @param pC plot ccordinates to project in screen
     * @return scrren coordinates
     */
    public abstract int[] project(double[] pC);

    /**
     * Returns the screen coordinates coresponding to plot coordinates
     * Used to test if mouse is pointing on a plot.
     * @param pC plot ccordinates to project in screen
     * @return scrren coordinates
     */
    public abstract int[] projectRatio(double[] rC);

    /**
     * Plot ActionMode : translation of the plot
     * @param t mouse translation in pixels
     */
    public abstract void translate(int[] t);

    /**
     * Plot ActionMode : dilatation of the plot
     * @param screenOrigin mouse initial position
     * @param screenRatio mouse final position relative to plot panel size
     */
    public abstract void dilate(int[] screenOrigin, double[] screenRatio);

    public void drawCoordinate(double[] pC) {
    }

    public abstract void drawString(String label, double[] pC, double angle, double cornerE, double cornerN);

    public abstract void drawStringRatio(String label, double[] rC, double angle, double cornerE, double cornerN);

    public abstract void drawLineRatio(double[] rC1, double[] rC2);

    public abstract void drawLine(double[] pC1, double[] pC2);

    public abstract void drawLargeLine(double[] pC1, double[] pC2);

    public abstract void drawDot(double[] pC);

    public abstract void drawPloygon(double[][] pC);

    public abstract void fillPloygon(double[][] pC);

    public abstract void drawImage(Image im, double[][] pC1, double[][] pC2);

}