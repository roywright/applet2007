public class ShapeFunction{


	public static final int MAXENT = 0;
	public static final int TRI3 = 1;
	public static final int WACH = 2;
	public static final int MVC = 3;
	public static final int LAPLACE = 4;
	public static final int MLS = 5;
	public static final int RELENT = 6;
	public static final int LMAXENT = 7;
	public static final int RELENTG = 8;
	public double epsilon = 1.0E-3;
	public double[] mlsRadius;
	public int beta = 0;

	private NodeSet nodeSet;
	private int type;
	public int node;





	public ShapeFunction(NodeSet ns, int t, int n){
		type = t;
		nodeSet = ns;
		node = n;
		mlsRadius = ns.thirdRadius();
	}

	public double f(double x, double y){


		switch (type) {
			case MAXENT:
				return maxent(x, y);
			case TRI3:
				return tri3(x, y);
			case WACH:
				return wachspress(x, y);
			case MVC:
				return MVC(x, y);
			case LAPLACE:
				return laplace(x, y);
			case MLS:
				return MLS(x, y);
			case RELENT:
				return relent(x, y);
			case LMAXENT:
				return lmaxent(x, y);
			case RELENTG:
				return relentg(x, y);


		}

		return 0.0;

	}






	private double tri3(double x, double y){
		int j = 1;
		int k = 2;
		if (node == 0){
			j = 1;
			k = 2;
		}
		if (node == 1){
			j = 2;
			k = 0;
		}
		if (node == 2){
			j = 0;
			k = 1;
		}

		return ((nodeSet.nodeY[k]-nodeSet.nodeY[j])*nodeSet.nodeX[j]-(nodeSet.nodeX[k]-nodeSet.nodeX[j])*nodeSet.nodeY[j]-(nodeSet.nodeY[k]-nodeSet.nodeY[j])*x+(nodeSet.nodeX[k]-nodeSet.nodeX[j])*y)/((nodeSet.nodeY[k]-nodeSet.nodeY[j])*(nodeSet.nodeX[j]-nodeSet.nodeX[node]) - (nodeSet.nodeX[k]-nodeSet.nodeX[j])*(nodeSet.nodeY[j]-nodeSet.nodeY[node]));
	}





    // begin MAXENT functions

	private double maxent(double x, double y){

		if(nodeSet.isOn(x,y) >= 0 && nodeSet.isOn(nodeSet.nodeX[node], nodeSet.nodeY[node]) < 0)
				return 0;

/*
		if(nodeSet.isOn(x,y) >= 0 && nodeSet.isHull(node)){

			if(nodeSet.nodeFromHull(nodeSet.isOn(x, y)) == node){
				int startNode = nodeSet.isOn(x, y) + 1;
				if (startNode == nodeSet.hullCount)
					startNode = 0;
				int endNode = nodeSet.isOn(x, y);
				return Math.sqrt((x - nodeSet.hullX[startNode])*(x - nodeSet.hullX[startNode]) + (y - nodeSet.hullY[startNode])*(y - nodeSet.hullY[startNode]))
				/Math.sqrt((nodeSet.hullX[endNode] - nodeSet.hullX[startNode])*(nodeSet.hullX[endNode] - nodeSet.hullX[startNode]) + (nodeSet.hullY[endNode] - nodeSet.hullY[startNode])*(nodeSet.hullY[endNode] - nodeSet.hullY[startNode]));
			}
			int nextHull = nodeSet.isOn(x, y) + 1;
			if (nextHull == nodeSet.hullCount)
				nextHull = 0;
			if(nodeSet.nodeFromHull(nextHull) == node){
				int startNode = nodeSet.isOn(x, y);
				int endNode = nextHull;
				return Math.sqrt((x - nodeSet.hullX[startNode])*(x - nodeSet.hullX[startNode]) + (y - nodeSet.hullY[startNode])*(y - nodeSet.hullY[startNode]))
				/Math.sqrt((nodeSet.hullX[endNode] - nodeSet.hullX[startNode])*(nodeSet.hullX[endNode] - nodeSet.hullX[startNode]) + (nodeSet.hullY[endNode] - nodeSet.hullY[startNode])*(nodeSet.hullY[endNode] - nodeSet.hullY[startNode]));
			}

			return 0;
		}
*/
		double[] lambda = minF(x, y);
		double tildeX[] = new double[nodeSet.count];
		double tildeY[] = new double[nodeSet.count];
		for (int j = 0; j < nodeSet.count; j++){
			tildeX[j] = nodeSet.nodeX[j] - x;
			tildeY[j] = nodeSet.nodeY[j] - y;
		}
		double Z = tildeZ(lambda[0], lambda[1], tildeX, tildeY);
		return Math.exp(-lambda[0]*tildeX[node] - lambda[1]*tildeY[node]) / Z;


	}




	private double[] minF(double x, double y){

		double lambda_2 = 0.0;
		double lambda_1 = 0.0;
		int k = 0;

		double alpha_3;
		double g3;
		double g1;
		double alpha_2;
		double g2;
 		double h1;
		double h2;
		double h3;
		double alpha_0;
		double g0;
		double alpha;
		double[] tilde_X = new double[nodeSet.count];
		double[] tilde_Y = new double[nodeSet.count];
		for (int j = 0; j < nodeSet.count; j++){
			tilde_X[j] = nodeSet.nodeX[j] - x;
			tilde_Y[j] = nodeSet.nodeY[j] - y;
		}

		double norm = 1.0;

		while (norm > epsilon && k < 10000){
			double[] delta_lambda = gradF(lambda_1, lambda_2, tilde_X, tilde_Y);
			norm = Math.sqrt(delta_lambda[0]*delta_lambda[0] + delta_lambda[1]*delta_lambda[1]);
			if (norm > epsilon / 2.0){

				delta_lambda[0] = - delta_lambda[0] / norm;
				delta_lambda[1] = - delta_lambda[1] / norm;

				alpha_3 = 1.0;
				g3 = F(lambda_1 + alpha_3*delta_lambda[0], lambda_2 + alpha_3*delta_lambda[1], tilde_X, tilde_Y);
				g1 = F(lambda_1, lambda_2, tilde_X, tilde_Y);
				while (g3 >= g1){
					alpha_3 = alpha_3 / 2.0;
					g3 = F(lambda_1 + alpha_3*delta_lambda[0], lambda_2 + alpha_3*delta_lambda[1], tilde_X, tilde_Y);
				}
				alpha_2 = alpha_3 / 2.0;
				g2 = F(lambda_1 + alpha_2*delta_lambda[0], lambda_2 + alpha_2*delta_lambda[1], tilde_X, tilde_Y);

				h1 = (g2 - g1) / alpha_2;
				h2 = (g3 - g2) / (alpha_3 - alpha_2);
				h3 = (h2 - h1) / alpha_3;
				alpha_0 = 0.5*(alpha_2 - h1 / h3);
				g0 = F(lambda_1 + alpha_0*delta_lambda[0], lambda_2 + alpha_0*delta_lambda[1], tilde_X, tilde_Y);
				if (g0 < g3)
					alpha = alpha_0;
				else
					alpha = alpha_3;


				lambda_1 = lambda_1 + alpha*delta_lambda[0];
				lambda_2 = lambda_2 + alpha*delta_lambda[1];
				k++;

			}

		}

		return new double[] {lambda_1, lambda_2};

	}






	private double[] gradF(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y){

		double h = 1.0E-3;
		double[] vec = new double[2];
		vec[0] = (F(lambda_1 + h, lambda_2, tilde_X, tilde_Y) - F(lambda_1 - h, lambda_2, tilde_X, tilde_Y)) / (2.0*h);
		vec[1] = (F(lambda_1, lambda_2 + h, tilde_X, tilde_Y) - F(lambda_1, lambda_2 - h, tilde_X, tilde_Y)) / (2.0*h);
		return vec;

	}

	private double F(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y){

		return Math.log(tildeZ(lambda_1, lambda_2, tilde_X, tilde_Y));

	}

	private double tildeZ(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y){

		double sum = 0.0;
		for (int j = 0; j < tilde_X.length; j++)
			sum = sum + Math.exp(-lambda_1*tilde_X[j] - lambda_2*tilde_Y[j]);
		return sum;

	}


	// end MAXENT functions






	// begin wachspress functions


	public double wachspress(double x, double y){


		if(nodeSet.isOn(x,y) >= 0){

			if(nodeSet.nodeFromHull(nodeSet.isOn(x, y)) == node){
				int startNode = nodeSet.isOn(x, y) + 1;
				if (startNode == nodeSet.hullCount)
					startNode = 0;
				int endNode = nodeSet.isOn(x, y);
				return Math.sqrt((x - nodeSet.hullX[startNode])*(x - nodeSet.hullX[startNode]) + (y - nodeSet.hullY[startNode])*(y - nodeSet.hullY[startNode]))
				/Math.sqrt((nodeSet.hullX[endNode] - nodeSet.hullX[startNode])*(nodeSet.hullX[endNode] - nodeSet.hullX[startNode]) + (nodeSet.hullY[endNode] - nodeSet.hullY[startNode])*(nodeSet.hullY[endNode] - nodeSet.hullY[startNode]));
			}
			int nextHull = nodeSet.isOn(x, y) + 1;
			if (nextHull == nodeSet.hullCount)
				nextHull = 0;
			if(nodeSet.nodeFromHull(nextHull) == node){
				int startNode = nodeSet.isOn(x, y);
				int endNode = nextHull;
				return Math.sqrt((x - nodeSet.hullX[startNode])*(x - nodeSet.hullX[startNode]) + (y - nodeSet.hullY[startNode])*(y - nodeSet.hullY[startNode]))
				/Math.sqrt((nodeSet.hullX[endNode] - nodeSet.hullX[startNode])*(nodeSet.hullX[endNode] - nodeSet.hullX[startNode]) + (nodeSet.hullY[endNode] - nodeSet.hullY[startNode])*(nodeSet.hullY[endNode] - nodeSet.hullY[startNode]));
			}

			return 0;
		}




		double[] Aside = new double[nodeSet.hullCount];
		double[] A = new double[nodeSet.hullCount];

		Aside[0] = area(nodeSet.hullX[nodeSet.hullCount - 1], nodeSet.hullY[nodeSet.hullCount - 1], 0, 1);
		A[0] = area(x, y, 0, 1);

		for (int i = 1; i < nodeSet.hullCount - 1; i++){
			Aside[i] = area(nodeSet.hullX[i - 1], nodeSet.hullY[i - 1], i, i + 1);
			A[i] = area(x, y, i, i + 1);
		}

		Aside[nodeSet.hullCount - 1] = area(nodeSet.hullX[nodeSet.hullCount - 2], nodeSet.hullY[nodeSet.hullCount - 2], nodeSet.hullCount - 1, 0);
		A[nodeSet.hullCount - 1] = area(x, y, nodeSet.hullCount - 1, 0);


		double denom = Aside[0] / (A[nodeSet.hullCount - 1] * A[0]);
		for (int k = 1; k < nodeSet.hullCount; k++)
			denom = denom + Aside[k] / (A[k-1] * A[k]);

		int j = nodeSet.hullFromNode(node);
		int jj = j - 1;
		if (j == 0)
			jj = nodeSet.hullCount - 1;
		return Aside[j] / (A[jj] * A[j]) / denom;


	}


	private double area(double x, double y, int j, int k){

		return 0.5*((nodeSet.hullX[j]*nodeSet.hullY[k] - nodeSet.hullX[k]*nodeSet.hullY[j])-(x*nodeSet.hullY[k] - nodeSet.hullX[k]*y)+(x*nodeSet.hullY[j] - nodeSet.hullX[j]*y));

	}


	// end wachspress functions



/*	// begin MVC functions

	public double MVC(double x, double y){


		if(nodeSet.isOnSimple(x,y) >= 0){
			if(nodeSet.isOnSimple(x, y) == node){
				int startNode = nodeSet.isOnSimple(x, y) + 1;
				if (startNode == nodeSet.count)
					startNode = 0;
				int endNode = nodeSet.isOnSimple(x, y);
				return Math.sqrt((x - nodeSet.nodeX[startNode])*(x - nodeSet.nodeX[startNode]) + (y - nodeSet.nodeY[startNode])*(y - nodeSet.nodeY[startNode]))
				/Math.sqrt((nodeSet.nodeX[endNode] - nodeSet.nodeX[startNode])*(nodeSet.nodeX[endNode] - nodeSet.nodeX[startNode]) + (nodeSet.nodeY[endNode] - nodeSet.nodeY[startNode])*(nodeSet.nodeY[endNode] - nodeSet.nodeY[startNode]));
			}
			int next = nodeSet.isOnSimple(x, y) + 1;
			if (next == nodeSet.count)
				next = 0;
			if(next == node){
				int startNode = nodeSet.isOnSimple(x, y);
				int endNode = next;
				return Math.sqrt((x - nodeSet.nodeX[startNode])*(x - nodeSet.nodeX[startNode]) + (y - nodeSet.nodeY[startNode])*(y - nodeSet.nodeY[startNode]))
				/Math.sqrt((nodeSet.nodeX[endNode] - nodeSet.nodeX[startNode])*(nodeSet.nodeX[endNode] - nodeSet.nodeX[startNode]) + (nodeSet.nodeY[endNode] - nodeSet.nodeY[startNode])*(nodeSet.nodeY[endNode] - nodeSet.nodeY[startNode]));
			}

			return 0;
		}





		double cos;
		double a, b, c;

		double[] d = new double[nodeSet.count];
		double[] alpha = new double[nodeSet.count];
		double[] w = new double[nodeSet.count];

		for (int i = 0; i < nodeSet.count - 1; i++){
			d[i] = dist(nodeSet.nodeX[i], nodeSet.nodeY[i], x, y);
			b = d[i];
			a = dist(nodeSet.nodeX[i], nodeSet.nodeY[i], nodeSet.nodeX[i+1], nodeSet.nodeY[i+1]);
			c = dist(nodeSet.nodeX[i+1], nodeSet.nodeY[i+1], x, y);
			cos = (b*b + c*c - a*a) / (2*b*c);
			alpha[i] = Math.acos(cos);
		}

		d[nodeSet.count - 1] = dist(nodeSet.nodeX[nodeSet.count - 1], nodeSet.nodeY[nodeSet.count - 1], x, y);
		b = d[nodeSet.count - 1];
		a = dist(nodeSet.nodeX[nodeSet.count - 1], nodeSet.nodeY[nodeSet.count - 1], nodeSet.nodeX[0], nodeSet.nodeY[0]);
		c = dist(nodeSet.nodeX[0], nodeSet.nodeY[0], x, y);
		cos = (b*b + c*c - a*a) / (2*b*c);
		alpha[nodeSet.count - 1] = Math.acos(cos);


		w[0] = (Math.tan(alpha[nodeSet.count - 1] / 2) + Math.tan(alpha[0] / 2)) / d[0];
		for (int i = 1; i < nodeSet.count; i++)
			w[i] = (Math.tan(alpha[i - 1] / 2) + Math.tan(alpha[i] / 2)) / d[i];

		double denom = w[0];
		for (int i = 1; i < nodeSet.count; i++)
			denom = denom + w[i];

		return w[node] / denom;

	}


	private double dist(double x1, double y1, double x2, double y2){

		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));

	}


	// end MVC functions */





























	// begin MVC functions

	public double MVC(double x, double y){


		double[] sx = new double[nodeSet.count];
		double[] sy = new double[nodeSet.count];
		double[] r = new double[nodeSet.count];
		double[] A = new double[nodeSet.count];
		double[] D = new double[nodeSet.count];
		double f, ff, W, w;

		for (int i = 0; i <  nodeSet.count; i++){
			sx[i] = nodeSet.nodeX[i] - x;
			sy[i] = nodeSet.nodeY[i] - y;
		}
		for (int i = 0; i <  nodeSet.count; i++){

			int ii = i+1;
			if (ii == nodeSet.count)
				ii = 0;
			r[i] = Math.sqrt(sx[i]*sx[i] + sy[i]*sy[i]);
			A[i] = sx[i] * sy[ii] - sx[ii] * sy[i];
			D[i] = sx[i] * sx[ii] + sy[i] * sy[ii];
			if (i == node)
				f = 1.0;
			else
				f = 0.0;
			if (ii == node)
				ff = 1.0;
			else
				ff = 0.0;
			if (Math.abs(r[i]) < 1e-10)
				return f;
			if (Math.abs(A[i]) < 1e-10 && D[i] < 0){
				r[ii] = Math.sqrt(sx[ii]*sx[ii] + sy[ii]*sy[ii]);
				return (r[ii] * f + r[i] * ff)/(r[i] + r[ii]);
			}
		}
		f = 0;
		W = 0;
		for (int i = 0; i <  nodeSet.count; i++){
			int ii = i+1;
			if (ii == nodeSet.count)
				ii = 0;
			int iii = i-1;
			if (iii == -1)
				iii = nodeSet.count - 1;
			w = 0;
			if (Math.abs(A[iii]) > 1e-10)
				w += (r[iii] - D[iii]/r[i])/A[iii];
			if (Math.abs(A[i]) > 1e-10)
				w += (r[ii] - D[i]/r[i])/A[i];
			double fi;
			if (i == node)
				fi = 1.0;
			else
				fi = 0.0;
			f += w * fi;
			W += w;
		}

		return f / W;

	}


	private double dist(double x1, double y1, double x2, double y2){

		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));

	}


	// end MVC functions

































	// begin Laplace functions


	private double laplace(double xx, double yy){

		double x = nodeSet.nodeX[node];
		double y = nodeSet.nodeY[node];
		NodeSet tempNS = new NodeSet();
		for (int i = 0; i < nodeSet.count; i++)
			tempNS.add(nodeSet.hullX[i], nodeSet.hullY[i]);
		nodeSet = new NodeSet();
		for (int i = 0; i < tempNS.count; i++)
			nodeSet.add(Math.cos(2.0*i*Math.PI / tempNS.count), Math.sin(2.0*i*Math.PI / tempNS.count));
		node = tempNS.nodeNum(x,y);


		ShapeFunction tempSF = new ShapeFunction(nodeSet, WACH, node);
		NodeSet triSet = new NodeSet();
		Triangle[] triangles = Triangle.subdivide(nodeSet);
		for (int i = 0; i < 20; i++){
			for (int k = 0; k < triangles.length; k++){


				triSet = new NodeSet();
				for(int j = 0; j < 3; j++){
					x = 0.0;
					y = 0.0;
					for(int m = 0; m < nodeSet.count; m++){
						tempSF.node = m;
						x = x + tempSF.f(triangles[k].x[j], triangles[k].y[j]) * tempNS.nodeX[m];
						y = y + tempSF.f(triangles[k].x[j], triangles[k].y[j]) * tempNS.nodeY[m];
					}
					triSet.add(x,y);
				}

				if(triSet.isInside(xx,yy)){
					triangles = Triangle.subdivide(triangles[k]);
					break;
				}

			}
		}

		tempSF.node = node;
		return (tempSF.f(triangles[0].x[0], triangles[0].y[0]) + tempSF.f(triangles[1].x[0], triangles[1].y[0]) + tempSF.f(triangles[2].x[0], triangles[2].y[0])) / 3.0;

	}



	// end Laplace functions






	// begin MLS functions

	private Matrix plin(double x, double y){
		double[][] temp = new double[3][1];
		temp[0][0] = 1.0;
		temp[1][0] = x;
		temp[2][0] = y;
		return new Matrix(temp);
	}


	private Matrix pquad(double x, double y){
		double[][] temp = new double[6][1];
		temp[0][0] = 1.0;
		temp[1][0] = x;
		temp[2][0] = y;
		temp[3][0] = x*x;
		temp[4][0] = x*y;
		temp[5][0] = y*y;
		return new Matrix(temp);
	}


	private double MLS(double x, double y){


		if (nodeSet.count < 3)
			return 0;

		Matrix A = new Matrix(3, 3);

		for(int i = 0; i < nodeSet.count; i++){
			Matrix p = plin(nodeSet.nodeX[i], nodeSet.nodeY[i]);
			A = A.plus(
				(p.times(p.transpose()))
				.times(new Matrix(splineweight(i, dist(x, y, nodeSet.nodeX[i], nodeSet.nodeY[i]))))
			);
		}


		Matrix B = (plin(nodeSet.nodeX[node], nodeSet.nodeY[node]))
		           .times(new Matrix(splineweight(node, dist(x, y, nodeSet.nodeX[node], nodeSet.nodeY[node]))));

		return ((plin(x,y).transpose())
		        .times(A.inverse())
		        .times(B))
		        .entry(0,0);


	}


	private double splineweight(int i, double rr){

		double r = rr/mlsRadius[i];

		if (r < 0.5)
			return 2.0/3.0 - 4.0*r*r + 4.0*r*r*r;
		if ((0.5 <= r) && (r <= 1))
			return 4.0/3.0 - 4.0*r + 4.0*r*r - 4.0*r*r*r/3.0;
		return 0.0;

	}

	// end MLS functions




	// begin RELENT functions



	private double relent(double x, double y){



		if(nodeSet.isOn(x,y) >= 0 && nodeSet.isOn(nodeSet.nodeX[node], nodeSet.nodeY[node]) < 0)
				return 0;
		double[] lambda = minF_rel(x, y);
		double tildeX[] = new double[nodeSet.count];
		double tildeY[] = new double[nodeSet.count];
		for (int j = 0; j < nodeSet.count; j++){
			tildeX[j] = nodeSet.nodeX[j] - x;
			tildeY[j] = nodeSet.nodeY[j] - y;
		}
		double Z = tildeZ_rel(lambda[0], lambda[1], tildeX, tildeY, x, y);


		double retval = splineweight(node, dist(x, y, nodeSet.nodeX[node], nodeSet.nodeY[node]))*Math.exp(-lambda[0]*tildeX[node] - lambda[1]*tildeY[node]) / Z;
		if (retval < Double.MAX_VALUE)
			return retval;
		else
			return 0;


	}




	private double[] minF_rel(double x, double y){

		double lambda_2 = 0.0;
		double lambda_1 = 0.0;
		int k = 0;

		double alpha_3;
		double g3;
		double g1;
		double alpha_2;
		double g2;
 		double h1;
		double h2;
		double h3;
		double alpha_0;
		double g0;
		double alpha;
		double[] tilde_X = new double[nodeSet.count];
		double[] tilde_Y = new double[nodeSet.count];
		for (int j = 0; j < nodeSet.count; j++){
			tilde_X[j] = nodeSet.nodeX[j] - x;
			tilde_Y[j] = nodeSet.nodeY[j] - y;
		}

		double norm = 1.0;

		while (norm > epsilon && k < 10000){
			double[] delta_lambda = gradF_rel(lambda_1, lambda_2, tilde_X, tilde_Y, x, y);
			norm = Math.sqrt(delta_lambda[0]*delta_lambda[0] + delta_lambda[1]*delta_lambda[1]);
			if (norm > epsilon / 2.0){

				delta_lambda[0] = - delta_lambda[0] / norm;
				delta_lambda[1] = - delta_lambda[1] / norm;

				alpha_3 = 1.0;
				g3 = F_rel(lambda_1 + alpha_3*delta_lambda[0], lambda_2 + alpha_3*delta_lambda[1], tilde_X, tilde_Y, x, y);
				g1 = F_rel(lambda_1, lambda_2, tilde_X, tilde_Y, x, y);
				while (g3 >= g1){
					alpha_3 = alpha_3 / 2.0;
					g3 = F_rel(lambda_1 + alpha_3*delta_lambda[0], lambda_2 + alpha_3*delta_lambda[1], tilde_X, tilde_Y, x, y);
				}
				alpha_2 = alpha_3 / 2.0;
				g2 = F_rel(lambda_1 + alpha_2*delta_lambda[0], lambda_2 + alpha_2*delta_lambda[1], tilde_X, tilde_Y, x, y);

				h1 = (g2 - g1) / alpha_2;
				h2 = (g3 - g2) / (alpha_3 - alpha_2);
				h3 = (h2 - h1) / alpha_3;
				alpha_0 = 0.5*(alpha_2 - h1 / h3);
				g0 = F_rel(lambda_1 + alpha_0*delta_lambda[0], lambda_2 + alpha_0*delta_lambda[1], tilde_X, tilde_Y, x, y);
				if (g0 < g3)
					alpha = alpha_0;
				else
					alpha = alpha_3;


				lambda_1 = lambda_1 + alpha*delta_lambda[0];
				lambda_2 = lambda_2 + alpha*delta_lambda[1];
				k++;

			}

		}

		return new double[] {lambda_1, lambda_2};

	}






	private double[] gradF_rel(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		double h = 1.0E-3;
		double[] vec = new double[2];
		vec[0] = (F_rel(lambda_1 + h, lambda_2, tilde_X, tilde_Y, x, y) - F_rel(lambda_1 - h, lambda_2, tilde_X, tilde_Y, x, y)) / (2.0*h);
		vec[1] = (F_rel(lambda_1, lambda_2 + h, tilde_X, tilde_Y, x, y) - F_rel(lambda_1, lambda_2 - h, tilde_X, tilde_Y, x, y)) / (2.0*h);
		return vec;

	}

	private double F_rel(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		return Math.log(tildeZ_rel(lambda_1, lambda_2, tilde_X, tilde_Y, x, y));

	}

	private double tildeZ_rel(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		double sum = 0.0;
		for (int j = 0; j < tilde_X.length; j++)
			sum = sum + splineweight(j, dist(x, y, nodeSet.nodeX[j], nodeSet.nodeY[j]))*Math.exp(-lambda_1*tilde_X[j] - lambda_2*tilde_Y[j]);
		return sum;

	}





	// end RELENT functions







	// begin RELENTG functions




	private double gaussweight(int i, double rr){

		double r = rr/mlsRadius[i];

		if (r < 1.0)
			return Math.exp(-r*r/(1-r));
		return 0.0;

	}




	private double relentg(double x, double y){



		if(nodeSet.isOn(x,y) >= 0 && nodeSet.isOn(nodeSet.nodeX[node], nodeSet.nodeY[node]) < 0)
				return 0;
		double[] lambda = minF_relg(x, y);
		double tildeX[] = new double[nodeSet.count];
		double tildeY[] = new double[nodeSet.count];
		for (int j = 0; j < nodeSet.count; j++){
			tildeX[j] = nodeSet.nodeX[j] - x;
			tildeY[j] = nodeSet.nodeY[j] - y;
		}
		double Z = tildeZ_relg(lambda[0], lambda[1], tildeX, tildeY, x, y);


		double retval = gaussweight(node, dist(x, y, nodeSet.nodeX[node], nodeSet.nodeY[node]))*Math.exp(-lambda[0]*tildeX[node] - lambda[1]*tildeY[node]) / Z;
		if (retval < Double.MAX_VALUE)
			return retval;
		else
			return 0;


	}




	private double[] minF_relg(double x, double y){

		double lambda_2 = 0.0;
		double lambda_1 = 0.0;
		int k = 0;

		double alpha_3;
		double g3;
		double g1;
		double alpha_2;
		double g2;
 		double h1;
		double h2;
		double h3;
		double alpha_0;
		double g0;
		double alpha;
		double[] tilde_X = new double[nodeSet.count];
		double[] tilde_Y = new double[nodeSet.count];
		for (int j = 0; j < nodeSet.count; j++){
			tilde_X[j] = nodeSet.nodeX[j] - x;
			tilde_Y[j] = nodeSet.nodeY[j] - y;
		}

		double norm = 1.0;

		while (norm > epsilon && k < 10000){
			double[] delta_lambda = gradF_relg(lambda_1, lambda_2, tilde_X, tilde_Y, x, y);
			norm = Math.sqrt(delta_lambda[0]*delta_lambda[0] + delta_lambda[1]*delta_lambda[1]);
			if (norm > epsilon / 2.0){

				delta_lambda[0] = - delta_lambda[0] / norm;
				delta_lambda[1] = - delta_lambda[1] / norm;

				alpha_3 = 1.0;
				g3 = F_relg(lambda_1 + alpha_3*delta_lambda[0], lambda_2 + alpha_3*delta_lambda[1], tilde_X, tilde_Y, x, y);
				g1 = F_relg(lambda_1, lambda_2, tilde_X, tilde_Y, x, y);
				while (g3 >= g1){
					alpha_3 = alpha_3 / 2.0;
					g3 = F_relg(lambda_1 + alpha_3*delta_lambda[0], lambda_2 + alpha_3*delta_lambda[1], tilde_X, tilde_Y, x, y);
				}
				alpha_2 = alpha_3 / 2.0;
				g2 = F_relg(lambda_1 + alpha_2*delta_lambda[0], lambda_2 + alpha_2*delta_lambda[1], tilde_X, tilde_Y, x, y);

				h1 = (g2 - g1) / alpha_2;
				h2 = (g3 - g2) / (alpha_3 - alpha_2);
				h3 = (h2 - h1) / alpha_3;
				alpha_0 = 0.5*(alpha_2 - h1 / h3);
				g0 = F_relg(lambda_1 + alpha_0*delta_lambda[0], lambda_2 + alpha_0*delta_lambda[1], tilde_X, tilde_Y, x, y);
				if (g0 < g3)
					alpha = alpha_0;
				else
					alpha = alpha_3;


				lambda_1 = lambda_1 + alpha*delta_lambda[0];
				lambda_2 = lambda_2 + alpha*delta_lambda[1];
				k++;

			}

		}

		return new double[] {lambda_1, lambda_2};

	}






	private double[] gradF_relg(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		double h = 1.0E-3;
		double[] vec = new double[2];
		vec[0] = (F_relg(lambda_1 + h, lambda_2, tilde_X, tilde_Y, x, y) - F_relg(lambda_1 - h, lambda_2, tilde_X, tilde_Y, x, y)) / (2.0*h);
		vec[1] = (F_relg(lambda_1, lambda_2 + h, tilde_X, tilde_Y, x, y) - F_relg(lambda_1, lambda_2 - h, tilde_X, tilde_Y, x, y)) / (2.0*h);
		return vec;

	}

	private double F_relg(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		return Math.log(tildeZ_relg(lambda_1, lambda_2, tilde_X, tilde_Y, x, y));

	}

	private double tildeZ_relg(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		double sum = 0.0;
		for (int j = 0; j < tilde_X.length; j++)
			sum = sum + gaussweight(j, dist(x, y, nodeSet.nodeX[j], nodeSet.nodeY[j]))*Math.exp(-lambda_1*tilde_X[j] - lambda_2*tilde_Y[j]);
		return sum;

	}





	// end RELENTG functions





































	// begin local MAXENT functions


	private double lmaxent(double x, double y){



		if(nodeSet.isOn(x,y) >= 0 && nodeSet.isOn(nodeSet.nodeX[node], nodeSet.nodeY[node]) < 0)
				return 0;
		double[] lambda = minF_loc(x, y);
		double tildeX[] = new double[nodeSet.count];
		double tildeY[] = new double[nodeSet.count];
		for (int j = 0; j < nodeSet.count; j++){
			tildeX[j] = nodeSet.nodeX[j] - x;
			tildeY[j] = nodeSet.nodeY[j] - y;
		}
		double Z = tildeZ_loc(lambda[0], lambda[1], tildeX, tildeY, x, y);

		return Math.exp(-lambda[0]*tildeX[node] - lambda[1]*tildeY[node] - beta*dist(x, y, nodeSet.nodeX[node], nodeSet.nodeY[node])) / Z;



	}




	private double[] minF_loc(double x, double y){

		double lambda_2 = 0.0;
		double lambda_1 = 0.0;
		int k = 0;

		double alpha_3;
		double g3;
		double g1;
		double alpha_2;
		double g2;
 		double h1;
		double h2;
		double h3;
		double alpha_0;
		double g0;
		double alpha;
		double[] tilde_X = new double[nodeSet.count];
		double[] tilde_Y = new double[nodeSet.count];
		for (int j = 0; j < nodeSet.count; j++){
			tilde_X[j] = nodeSet.nodeX[j] - x;
			tilde_Y[j] = nodeSet.nodeY[j] - y;
		}

		double norm = 1.0;

		while (norm > epsilon && k < 10000){
			double[] delta_lambda = gradF_loc(lambda_1, lambda_2, tilde_X, tilde_Y, x, y);
			norm = Math.sqrt(delta_lambda[0]*delta_lambda[0] + delta_lambda[1]*delta_lambda[1]);
			if (norm > epsilon / 2.0){

				delta_lambda[0] = - delta_lambda[0] / norm;
				delta_lambda[1] = - delta_lambda[1] / norm;

				alpha_3 = 1.0;
				g3 = F_loc(lambda_1 + alpha_3*delta_lambda[0], lambda_2 + alpha_3*delta_lambda[1], tilde_X, tilde_Y, x, y);
				g1 = F_loc(lambda_1, lambda_2, tilde_X, tilde_Y, x, y);
				while (g3 >= g1){
					alpha_3 = alpha_3 / 2.0;
					g3 = F_loc(lambda_1 + alpha_3*delta_lambda[0], lambda_2 + alpha_3*delta_lambda[1], tilde_X, tilde_Y, x, y);
				}
				alpha_2 = alpha_3 / 2.0;
				g2 = F_loc(lambda_1 + alpha_2*delta_lambda[0], lambda_2 + alpha_2*delta_lambda[1], tilde_X, tilde_Y, x, y);

				h1 = (g2 - g1) / alpha_2;
				h2 = (g3 - g2) / (alpha_3 - alpha_2);
				h3 = (h2 - h1) / alpha_3;
				alpha_0 = 0.5*(alpha_2 - h1 / h3);
				g0 = F_loc(lambda_1 + alpha_0*delta_lambda[0], lambda_2 + alpha_0*delta_lambda[1], tilde_X, tilde_Y, x, y);
				if (g0 < g3)
					alpha = alpha_0;
				else
					alpha = alpha_3;


				lambda_1 = lambda_1 + alpha*delta_lambda[0];
				lambda_2 = lambda_2 + alpha*delta_lambda[1];
				k++;

			}

		}

		return new double[] {lambda_1, lambda_2};

	}






	private double[] gradF_loc(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		double h = 1.0E-3;
		double[] vec = new double[2];
		vec[0] = (F_loc(lambda_1 + h, lambda_2, tilde_X, tilde_Y, x, y) - F_loc(lambda_1 - h, lambda_2, tilde_X, tilde_Y, x, y)) / (2.0*h);
		vec[1] = (F_loc(lambda_1, lambda_2 + h, tilde_X, tilde_Y, x, y) - F_loc(lambda_1, lambda_2 - h, tilde_X, tilde_Y, x, y)) / (2.0*h);
		return vec;

	}

	private double F_loc(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		return Math.log(tildeZ_loc(lambda_1, lambda_2, tilde_X, tilde_Y, x, y));

	}

	private double tildeZ_loc(double lambda_1, double lambda_2, double[] tilde_X, double[] tilde_Y, double x, double y){

		double sum = 0.0;
		for (int j = 0; j < tilde_X.length; j++)
			sum = sum + Math.exp(-lambda_1*tilde_X[j] - lambda_2*tilde_Y[j] - beta*dist(x, y, nodeSet.nodeX[j], nodeSet.nodeY[j]));
		return sum;

	}




	// end local MAXENT functions








}