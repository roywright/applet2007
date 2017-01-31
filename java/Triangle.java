public class Triangle{

	public double[] x;
	public double[] y;


    public Triangle() {

		x = new double[3];
		y = new double[3];

	}

    public Triangle(double[] xx, double[] yy) {

		x = xx;
		y = yy;

    }

	public static Triangle[] subdivide(NodeSet ns){
		if (ns.count == 3)
			return subdivide(new Triangle(new double[]{ns.nodeX[0], ns.nodeX[1], ns.nodeX[2]}, new double[]{ns.nodeY[0], ns.nodeY[1], ns.nodeY[2]}));

		double centerX = 0;
		double centerY = 0;
		Triangle[] toReturn = new Triangle[ns.hullCount];

		for (int i = 0; i < ns.hullCount; i++){
			centerX = centerX + ns.hullX[i];
			centerY = centerY + ns.hullY[i];
		}
		centerX = centerX / ns.hullCount;
		centerY = centerY / ns.hullCount;


		int i;
		for (i = 0; i < ns.hullCount - 1; i++)
			toReturn[i] = new Triangle(new double[]{ns.hullX[i], ns.hullX[i+1], centerX}, new double[]{ns.hullY[i], ns.hullY[i+1], centerY});

		toReturn[i] = new Triangle(new double[]{ns.hullX[i], ns.hullX[0], centerX}, new double[]{ns.hullY[i], ns.hullY[0], centerY});
		return toReturn;
	}

	public static Triangle[] subdivideConcave(NodeSet ns){

		double x1, x2, x3, x4, y1, y2, y3, y4, xint;
		boolean crosses = false;
		int otherNode = 0;
		int node;
		int ii;
		NodeSet ns1 = new NodeSet();
		NodeSet ns2 = new NodeSet();


		for (node = 0; node < ns.count - 2; node++){
			x1 = ns.nodeX[node];
			y1 = ns.nodeY[node];

			int nn = node - 2;
			if (nn < 0)
				nn = ns.count + nn;
			else
				nn = ns.count - 1;
			crosses = false;
			for (otherNode = node + 2; otherNode <= nn; otherNode++){
				x2 = ns.nodeX[otherNode];
				y2 = ns.nodeY[otherNode];
				for (int i = 0; i < ns.count; i++){
					if (i == ns.count - 1)
						ii = 0;
					else
						ii = i + 1;
					if (i == node || i == otherNode || ii == node || ii == otherNode)
						continue;
					x3 = ns.nodeX[i];
					y3 = ns.nodeY[i];
					x4 = ns.nodeX[ii];
					y4 = ns.nodeY[ii];
					xint = ((x1*y2-x2*y1)*(x4-x3) - (x3*y4-x4*y3)*(x2-x1)) / ((x4-x3)*(y2-y1)-(x2-x1)*(y4-y3));
					if (((x1 < xint && xint < x2) || (x1 > xint && xint > x2)) && ((x3 < xint && xint < x4) || (x3 > xint && xint > x4)))
						crosses = true;
				}
				if (!crosses){
					ns1 = new NodeSet();
					ns2 = new NodeSet();
					for (int j = node; j <= otherNode; j++)
						ns1.add(ns.nodeX[j], ns.nodeY[j]);
					for (int j = otherNode; j < ns.count; j++)
						ns2.add(ns.nodeX[j], ns.nodeY[j]);
					for (int j = 0; j <= node; j++)
						ns2.add(ns.nodeX[j], ns.nodeY[j]);
					if (ns1.intersects(ns2) || ns2.intersects(ns1))
						crosses = true;
				}

				if (!crosses)
					break;
			}

			if (!crosses){
				break;
			}

			for (otherNode = 0; otherNode < node - 1; otherNode++){
				x2 = ns.nodeX[otherNode];
				y2 = ns.nodeY[otherNode];
				for (int i = 0; i < ns.count; i++){
					if (i == ns.count - 1)
						ii = 0;
					else
						ii = i + 1;
					if (i == node || i == otherNode || ii == node || ii == otherNode)
						continue;
					x3 = ns.nodeX[i];
					y3 = ns.nodeY[i];
					x4 = ns.nodeX[ii];
					y4 = ns.nodeY[ii];
					xint = ((x1*y2-x2*y1)*(x4-x3) - (x3*y4-x4*y3)*(x2-x1)) / ((x4-x3)*(y2-y1)-(x2-x1)*(y4-y3));
					if (((x1 < xint && xint < x2) || (x1 > xint && xint > x2)) && ((x3 < xint && xint < x4) || (x3 > xint && xint > x4)))
						crosses = true;
				}
				if (!crosses){
					ns1 = new NodeSet();
					ns2 = new NodeSet();
					for (int j = node; j <= otherNode; j++)
						ns1.add(ns.nodeX[j], ns.nodeY[j]);
					for (int j = otherNode; j < ns.count; j++)
						ns2.add(ns.nodeX[j], ns.nodeY[j]);
					for (int j = 0; j <= node; j++)
						ns2.add(ns.nodeX[j], ns.nodeY[j]);
					if (ns1.intersects(ns2) || ns2.intersects(ns1))
						crosses = true;
				}
				if (!crosses){
					break;
				}
			}

			if (!crosses){
				break;
			}
		}







		Triangle[] t1;
		if (ns1.isConvex())
			t1 = subdivide(ns1);
		else
			t1 = subdivideConcave(ns1);
		Triangle[] t2;
		if (ns2.isConvex())
			t2 = subdivide(ns2);
		else
			t2 = subdivideConcave(ns2);

		Triangle[] ret = new Triangle[t1.length + t2.length];
		for (int j = 0; j < t1.length; j++)
			ret[j] = t1[j];
		for (int j = 0; j < t2.length; j++)
			ret[j + t1.length] = t2[j];
		return ret;



	}




	public static Triangle[] subdivide(Triangle t){
		Triangle[] toReturn = new Triangle[4];
		double[] newX = new double[3];
		double[] newY = new double[3];

		newX[0] = (t.x[0] + t.x[1]) / 2;
		newY[0] = (t.y[0] + t.y[1]) / 2;
		newX[1] = (t.x[1] + t.x[2]) / 2;
		newY[1] = (t.y[1] + t.y[2]) / 2;
		newX[2] = (t.x[2] + t.x[0]) / 2;
		newY[2] = (t.y[2] + t.y[0]) / 2;
		toReturn[0] = new Triangle(new double[]{t.x[0], newX[0], newX[2]}, new double[]{t.y[0], newY[0], newY[2]});
		toReturn[1] = new Triangle(new double[]{t.x[1], newX[1], newX[0]}, new double[]{t.y[1], newY[1], newY[0]});
		toReturn[2] = new Triangle(new double[]{t.x[2], newX[2], newX[1]}, new double[]{t.y[2], newY[2], newY[1]});
		toReturn[3] = new Triangle(new double[]{newX[0], newX[1], newX[2]}, new double[]{newY[0], newY[1], newY[2]});
		return toReturn;
	}


	public double[] center(){
		double[] ret = new double[2];
		ret[0] = (x[0] + x[1] + x[2]) / 3;
		ret[1] = (y[0] + y[1] + y[2]) / 3;
		return ret;
	}



}
