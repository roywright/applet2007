import java.awt.*;

public interface Plotable {
    public void plot(AbstractDrawer draw, Color c);
    public void plot(AbstractDrawer draw);
    public void setColor(Color c);
}