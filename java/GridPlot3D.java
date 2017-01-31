import java.awt.*;

public class GridPlot3D extends Plot {

	private int fineLevel = 4;
	int node;
	private int type;
	private int topColor = 220;
	private int oldType = 0;
	NodeSet tempNS;
	double[] mlsRadius;
	int beta = 0;
	double theta = 0;
	double phi = 0;


	ShapeFunction sf;
	private NodeSet nodeSet;
	private Triangle[] triangles;
	private double[][] values;
	double minZ = Double.MAX_VALUE;
	double maxZ = - Double.MAX_VALUE;


    public GridPlot3D(Color c, NodeSet ns, int t, int n, int fl, double[] r, int b) {
        super(c);
        beta = b;
        mlsRadius = r;
		fineLevel = fl;
        node = n;
		type = t;
		nodeSet = ns;
		makeStuff();

	}



    public GridPlot3D(Color c, NodeSet ns, int t, int n) {
        super(c);
        node = n;
		type = t;
		nodeSet = ns;
		mlsRadius = ns.thirdRadius();
		makeStuff();

	}

	public GridPlot3D(Color c, NodeSet ns, int t, int n, int fl){
        super(c);
		fineLevel = fl;
        node = n;
		type = t;
		nodeSet = ns;
		mlsRadius = ns.thirdRadius();
		makeStuff();
	}

	public void moreFine(){
		fineLevel++;
		makeStuff();
	}

	public void lessFine(){
		if (fineLevel > 1){
			fineLevel--;
			makeStuff();
		}
	}




	private void makeStuff(){







		if (type == ShapeFunction.LAPLACE){
			double x = nodeSet.nodeX[node];
			double y = nodeSet.nodeY[node];
			tempNS = new NodeSet();
			for (int i = 0; i < nodeSet.hullCount; i++)
				tempNS.add(nodeSet.hullX[i], nodeSet.hullY[i]);
			nodeSet = new NodeSet();
			for (int i = 0; i < tempNS.count; i++)
				nodeSet.add(Math.cos(2.0*i*Math.PI / tempNS.count), Math.sin(2.0*i*Math.PI / tempNS.count));
			oldType = type;
			node = tempNS.nodeNum(x,y);
			type = ShapeFunction.WACH;
		}

		triangles = Triangle.subdivide(nodeSet);
		if (type == ShapeFunction.MVC){
			if (!(nodeSet.isLooselyConvex()))
				triangles = Triangle.subdivideConcave(nodeSet);
			else {
				if (!(nodeSet.isSimple())){
					NodeSet tempNS = new NodeSet();
					for (int i = 0; i < nodeSet.hullCount; i++)
						tempNS.add(nodeSet.hullX[i], nodeSet.hullY[i]);
					node = nodeSet.hullFromNode(node);
					nodeSet = tempNS;
				}
				triangles = Triangle.subdivide(nodeSet);
			}

		}



		sf = new ShapeFunction(nodeSet, type, node);
		sf.mlsRadius = mlsRadius;
		sf.beta = beta;
		Triangle[] newTriangles;
		Triangle[] temp;
		for(int k = 0; k < fineLevel; k++){
			newTriangles = new Triangle[4*triangles.length];
			for (int i = 0; i < triangles.length; i++){
				temp = Triangle.subdivide(triangles[i]);
				for (int j = 0; j < 4; j++)
					newTriangles[4*i + j] = temp[j];
			}
			triangles = newTriangles;
		}
		values = new double[triangles.length][3];
		for (int i = 0; i < triangles.length; i++){
			for (int j = 0; j < 3; j++){
				values[i][j] = sf.f(triangles[i].x[j], triangles[i].y[j]);
				if (values[i][j] > maxZ)
					maxZ = values[i][j];
				if (values[i][j] < minZ)
					minZ = values[i][j];
			}
		}




		if (oldType == ShapeFunction.LAPLACE){

			ShapeFunction tempSF = new ShapeFunction(nodeSet, type, node);
			double x;
			double y;

			for(int i = 0; i < triangles.length; i++)
				for(int j = 0; j < 3; j++){
					x = 0.0;
					y = 0.0;
					for(int k = 0; k < nodeSet.count; k++){
						tempSF.node = k;
						x = x + tempSF.f(triangles[i].x[j], triangles[i].y[j]) * tempNS.nodeX[k];
						y = y + tempSF.f(triangles[i].x[j], triangles[i].y[j]) * tempNS.nodeY[k];
					}
					triangles[i].x[j] = x;
					triangles[i].y[j] = y;
				}


			nodeSet = tempNS;
			type = oldType;
		}


	}


    public void plot(AbstractDrawer draw) {
		plot(draw, Color.BLUE);
	}

    public void plot(AbstractDrawer draw, Color c) {
		double colorScale;
		int R;
		int G;
		int B;
		int under;
        order(draw);
        double pC[][] = new double[3][3];
        for (int i = 0; i < triangles.length; i++){
			for (int j = 0; j < 3; j++){
				pC[j][0] = triangles[i].x[j];
				pC[j][1] = triangles[i].y[j];
				pC[j][2] = values[i][j];
			}

			colorScale = ((pC[0][2] + pC[1][2] + pC[2][2]) / 3 - minZ) / (1.01 * (maxZ - minZ));

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

			draw.setColor(new Color(R, G, B));
			draw.fillPloygon(pC);
			draw.drawPloygon(pC);



		}

    }


    public double[] getBounds() {
		return new double[] {minZ, maxZ};
	}


	public double[] getMin(){
		double z = minZ;
		double min = nodeSet.minX;
		if (nodeSet.minX > nodeSet.minY)
			min = nodeSet.minY;

		if (minZ > 0)
			z = 0;

		return new double[]{min, min, z};
	}


	public double[] getMax(){
		double z = maxZ;
		double max = nodeSet.maxX;
		if (nodeSet.maxX < nodeSet.maxY)
			max = nodeSet.maxY;

		if (maxZ < 1)
			z = 1;


		return new double[]{max, max, z};
	}


	private void order(AbstractDrawer draw){

		double thetaNew;
		thetaNew = ((Projection3D)((AWTDrawer)draw).projection).theta;
		phi = ((Projection3D)((AWTDrawer)draw).projection).phi;
		double[] ref = new double[2];
		Triangle temp = new Triangle(new double[]{0,1,2}, new double[]{2,0,1});
		double[] temp2 = new double[3];

		if (phi > Math.PI / 2 && phi < 3*Math.PI / 2)
			thetaNew = thetaNew + Math.PI;


//		if (Math.abs(theta - thetaNew) < Math.PI/4)
//			return;

		theta = thetaNew;

		ref[0] = Math.cos(theta);
		ref[1] = Math.sin(theta);
		while ((ref[0] < nodeSet.maxX && ref[0] > nodeSet.minX) || (ref[1] < nodeSet.maxY && ref[1] > nodeSet.minY)){
			ref[0] = 2*ref[0];
			ref[1] = 2*ref[1];
		}
//		for (int iterations = 1; iterations < triangles.length; iterations++)
		for (int iterations = 1; iterations < triangles.length / 10; iterations++)
		for (int i = 0; i < triangles.length - 1; i++)
				if (dist(triangles[i].center(), ref) < dist(triangles[i+1].center(), ref)){
					temp = triangles[i];
					temp2 = values[i];
					triangles[i] = triangles[i+1];
					values[i] = values[i+1];
					triangles[i+1] = temp;
					values[i+1] = temp2;
				}


	}

	private double dist(double a[], double b[]){
		return Math.sqrt((a[0] - b[0])*(a[0] - b[0]) + (a[1] - b[1])*(a[1] - b[1]));
	}



}
