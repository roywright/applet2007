public class plotableVector{

	private Plotable[] plotables = new Plotable[0];

    public plotableVector() {
    }

	public int size(){
		return plotables.length;
	}

    public void add(Plotable newPlotable){
		Plotable[] pp = new Plotable[plotables.length + 1];
		for (int i = 0; i<plotables.length; i++){
			pp[i] = plotables[i];
		}
        pp[plotables.length] = newPlotable;
        plotables = pp;
	}

    public void remove(int n){
		Plotable[] pp = new Plotable[plotables.length - 1];
		for (int i = 0; i < n; i++){
			pp[i] = plotables[i];
		}
		for (int i = n+1; i < plotables.length; i++){
			pp[i-1] = plotables[i];
		}
        plotables = pp;
	}

    public void remove(Plotable p){
		for (int i = 0; i < plotables.length; i++)
			if (plotables[i] == p){
				remove(i);
				return;
			}
	}

	public void copyInto(Plotable[] newPlotables){
		for (int i = 0; i<newPlotables.length; i++)
			add(newPlotables[i]);
	}

	public Plotable get(int n){
		return plotables[n];
	}

	public void setElementAt(Plotable p, int I){
		plotables[I] = p;
	}

	public void removeAllElements(){
		plotables = new Plotable[0];
	}


}
