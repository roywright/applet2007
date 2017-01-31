public class plotVector{

	private Plot[] plots = new Plot[0];

    public plotVector() {
    }

	public int size(){
		return plots.length;
	}

    public void add(Plot newPlot){
		Plot[] pp = new Plot[plots.length + 1];
		for (int i = 0; i<plots.length; i++){
			pp[i] = plots[i];
		}
        pp[plots.length] = newPlot;
        plots = pp;
	}

    public void remove(int n){
		Plot[] pp = new Plot[plots.length - 1];
		for (int i = 0; i < n; i++){
			pp[i] = plots[i];
		}
		for (int i = n+1; i < plots.length; i++){
			pp[i-1] = plots[i];
		}
        plots = pp;
	}

    public void remove(Plot p){
		for (int i = 0; i < plots.length; i++)
			if (plots[i] == p){
				remove(i);
				return;
			}
	}

	public void copyInto(Plot[] newPlots){
		for (int i = 0; i<newPlots.length; i++)
			add(newPlots[i]);
	}

	public Plot get(int n){
		return plots[n];
	}

	public void setElementAt(Plot p, int I){
		plots[I] = p;
	}

	public void removeAllElements(){
		plots = new Plot[0];
	}


}
