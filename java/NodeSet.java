import java.text.*;

public class NodeSet{

	public double[] nodeX;
	public double[] nodeY;
	public double[] hullX;
	public double[] hullY;

	private int rightIndex;

	public double minX;
	public double maxX;
	public double minY;
	public double maxY;

	private double tolerance = 1.0E-12;

	public double mlsFudge = 1.01;

	public int count;
	public int hullCount;
	private boolean convex = true;

    public NodeSet() {

		clear();

    }


	public boolean isConvex(){

		return convex;

	}


	public boolean isLooselyConvex(){

		return (hullCount == count);

	}


    public void add(double newX, double newY){
		double[] XX = new double[nodeX.length + 1];
		double[] YY = new double[nodeX.length + 1];
		for (int i = 0; i<nodeX.length; i++){
			XX[i] = nodeX[i];
			YY[i] = nodeY[i];
		}
        XX[nodeX.length] = newX;
        YY[nodeX.length] = newY;
        nodeX = XX;
        nodeY = YY;
		if (newX < minX)
			minX = newX;
		if (newX == maxX && newY < nodeY[rightIndex]){
			rightIndex = count;
		}
		if (newX > maxX){
			maxX = newX;
			rightIndex = count;
		}
		if (newY < minY)
			minY = newY;
		if (newY > maxY)
			maxY = newY;
		count++;
		order();

	}



	public void remove(double x, double y){
		int n = -1;
		double d = Double.MAX_VALUE;
		for (int i = 0; i < nodeX.length; i++)
			if (dist(nodeX[i], nodeY[i], x, y) < d){
				d = dist(nodeX[i], nodeY[i], x, y);
				n = i;
			}
		if (n >= 0)
			remove(n);
	}


	public int nodeFromHull(int k){
		int n = -1;
		double d = Double.MAX_VALUE;
		for (int i = 0; i < nodeX.length; i++)
			if (dist(nodeX[i], nodeY[i], hullX[k], hullY[k]) < d){
				d = dist(nodeX[i], nodeY[i], hullX[k], hullY[k]);
				n = i;
			}
		return n;
	}

	public int hullFromNode(int k){
		int n = -1;
		double d = Double.MAX_VALUE;
		for (int i = 0; i < hullX.length; i++)
			if (dist(nodeX[k], nodeY[k], hullX[i], hullY[i]) < d){
				d = dist(nodeX[k], nodeY[k], hullX[i], hullY[i]);
				n = i;
			}
		return n;
	}


	public boolean isHull(int k){
		double d = Double.MAX_VALUE;
		int n = 0;
		for (int i = 0; i < hullX.length; i++)
			if (dist(nodeX[k], nodeY[k], hullX[i], hullY[i]) < d){
				d = dist(nodeX[k], nodeY[k], hullX[i], hullY[i]);
				n = i;
			}

		int n1 = n-1;
		int n2 = n+1;
		if (n1 < 0)
			n1 = 0;
		if (n2 == hullCount)
			n2 = 0;

		if (Math.abs((nodeY[k]-hullY[n1])*(hullX[n2]-hullX[n1])-(nodeX[k]-hullX[n1])*(hullY[n2]-hullY[n1])) < tolerance)
			return false;

		return (d < tolerance);
	}


	public boolean isNode(double x, double y){
		double d = Double.MAX_VALUE;
		for (int i = 0; i < nodeX.length; i++)
			if (dist(nodeX[i], nodeY[i], x, y) < d){
				d = dist(nodeX[i], nodeY[i], x, y);
			}
		return (d < tolerance);
	}


	public int nodeNum(double x, double y){
		double d = Double.MAX_VALUE;
		int n = 0;
		for (int i = 0; i < nodeX.length; i++)
			if (dist(nodeX[i], nodeY[i], x, y) < d && dist(nodeX[i], nodeY[i], x, y) < tolerance){
				d = dist(nodeX[i], nodeY[i], x, y);
				n = i;
			}
		return n;
	}



	public boolean isSimple(){
		if (count <= 3)
			return true;
		double x1, x2, x3, x4, y1, y2, y3, y4, xint;
		int ii;
		for (int j = 0; j < count-1; j++){
				x1 = nodeX[j];
				y1 = nodeY[j];
				x2 = nodeX[j+1];
				y2 = nodeY[j+1];
				for (int i = 0; i < count; i++){
					if (i == count - 1)
						ii = 0;
					else
						ii = i + 1;
					if (i == j || i == j+1 || ii == j || ii == j+1)
						continue;
					x3 = nodeX[i];
					y3 = nodeY[i];
					x4 = nodeX[ii];
					y4 = nodeY[ii];
					xint = ((x1*y2-x2*y1)*(x4-x3) - (x3*y4-x4*y3)*(x2-x1)) / ((x4-x3)*(y2-y1)-(x2-x1)*(y4-y3));
					if (((x1 < xint && xint < x2) || (x1 > xint && xint > x2)) && ((x3 < xint && xint < x4) || (x3 > xint && xint > x4)))
						return false;
				}
		}
		x1 = nodeX[count-1];
		y1 = nodeY[count-1];
		x2 = nodeX[0];
		y2 = nodeY[0];
		for (int i = 0; i < count; i++){
			if (i == count - 1)
				ii = 0;
			else
				ii = i + 1;
			if (i == 0 || i == count-1 || ii == 0 || ii == count-1)
				continue;
			x3 = nodeX[i];
			y3 = nodeY[i];
			x4 = nodeX[ii];
			y4 = nodeY[ii];
			xint = ((x1*y2-x2*y1)*(x4-x3) - (x3*y4-x4*y3)*(x2-x1)) / ((x4-x3)*(y2-y1)-(x2-x1)*(y4-y3));
			if (((x1 < xint && xint < x2) || (x1 > xint && xint > x2)) && ((x3 < xint && xint < x4) || (x3 > xint && xint > x4)))
				return false;
		}


		return true;

	}


	public boolean intersects(NodeSet ns){

		for (int i = 0; i < ns.count; i++)
			if (isInside(ns.nodeX[i], ns.nodeY[i]) && !isNode(ns.nodeX[i], ns.nodeY[i]))
				return true;
		return false;


	}


    public void remove(int n){
		double[] XX = new double[nodeX.length - 1];
		double[] YY = new double[nodeX.length - 1];
		for (int i = 0; i < n; i++){
			XX[i] = nodeX[i];
			YY[i] = nodeY[i];
		}
		for (int i = n+1; i < nodeX.length; i++){
			XX[i-1] = nodeX[i];
			YY[i-1] = nodeY[i];
		}
        nodeX = XX;
        nodeY = YY;
		count--;

    	minX = Double.MAX_VALUE;
		maxX = -Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
		maxY = -Double.MAX_VALUE;
		rightIndex = 0;
		for (int i = 0; i < count; i++){
			if (nodeX[i] < minX)
				minX = nodeX[i];
			if (nodeX[i] == maxX && nodeY[i] < nodeY[rightIndex]){
				rightIndex = i;
			}
			if (nodeX[i] > maxX){
				maxX = nodeX[i];
				rightIndex = i;
			}
			if (nodeY[i] < minY)
				minY = nodeY[i];
			if (nodeY[i] > maxY)
				maxY = nodeY[i];
		}

		order();

	}



	public void clear(){

		nodeX = new double[0];
		nodeY = new double[0];
		hullX = new double[0];
		hullY = new double[0];
    	minX = Double.MAX_VALUE;
		maxX = -Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
		maxY = -Double.MAX_VALUE;
		count = 0;
		hullCount = 0;
		rightIndex = 0;

	}


	private void order(){

		convex = true;

		if (count < 3){
			hullX = nodeX;
			hullY = nodeY;
			hullCount = count;
			return;
		}

		double[][] P = new double[count][3];
		double deltaX;
		double deltaY;
		double temp;
		int newCount = count;


		P[rightIndex][0] = nodeX[rightIndex];
		P[rightIndex][1] = nodeY[rightIndex];
		P[rightIndex][2] = 0.0;

		for (int i = 0; i < count; i++){
			if (i == rightIndex)
				continue;
			P[i][0] = nodeX[i];
			P[i][1] = nodeY[i];
			deltaX = nodeX[i] - P[rightIndex][0];
			deltaY = nodeY[i] - P[rightIndex][1];
			if (deltaX < 0)
				P[i][2] = Math.PI + Math.atan(deltaY / deltaX);
			else
				P[i][2] = Math.PI / 2.0;
		}

		for (int k = 1; k < count; k++)
			for (int i = 0; i < count - 1; i++){
				if (P[i][2] > P[i+1][2]){
					temp = P[i][0];
					P[i][0] = P[i+1][0];
					P[i+1][0] = temp;
					temp = P[i][1];
					P[i][1] = P[i+1][1];
					P[i+1][1] = temp;
					temp = P[i][2];
					P[i][2] = P[i+1][2];
					P[i+1][2] = temp;
				}
			}
/*
		for (int i = 0; i < newCount - 1; i++){
			if (P[i][2] == P[i+1][2]){
				int start;
				if (Math.pow((P[i][0] - P[0][0]),2) + Math.pow((P[i][1] - P[0][1]),2) < Math.pow((P[i+1][0] - P[0][0]),2) + Math.pow((P[i+1][1] - P[0][1]),2))
					start = i;
				else
					start = i + 1;
				newCount--;
				for (int j = start; j < newCount; j++){
					P[j][0] = P[j+1][0];
					P[j][1] = P[j+1][1];
					P[j][2] = P[j+1][2];
				}
			}
		}
*/

		for (int j = 0; j < newCount; j++)
		for (int i = 0; i < newCount - 1; i++){
			int exp = 0;
			if (i > 0 && dist(P[0][0], P[0][1], P[i][0], P[i][1]) < dist(P[0][0], P[0][1], P[i-1][0], P[i-1][1]))
				exp = 1;
			if (P[i][2] == P[i+1][2]){
				convex = false;
				int start;
				if (Math.pow(-1, exp)*dist(P[0][0], P[0][1], P[i][0], P[i][1]) > Math.pow(-1, exp)*dist(P[0][0], P[0][1], P[i+1][0], P[i+1][1])){
					double[] temp2 = new double[3];
					temp2[0] = P[i][0];
					temp2[1] = P[i][1];
					temp2[2] = P[i][2];
					P[i][0] = P[i+1][0];
					P[i][1] = P[i+1][1];
					P[i][2] = P[i+1][2];
					P[i+1][0] = temp2[0];
					P[i+1][1] = temp2[1];
					P[i+1][2] = temp2[2];
				}
			}
		}












		double[][] W = new double[newCount][3];
		int countW = 2;
		W[0][0] = P[0][0];
		W[0][1] = P[0][1];
		W[0][2] = P[0][2];
		W[1][0] = P[1][0];
		W[1][1] = P[1][1];
		W[1][2] = P[1][2];
		int i = 2;

		while (i < newCount && i > 1){

			if ((P[i][1] - W[countW - 2][1])*(W[countW - 1][0] - W[countW - 2][0]) - (P[i][0] - W[countW - 2][0])*(W[countW - 1][1] - W[countW - 2][1]) >= 0){
				W[countW][0] = P[i][0];
				W[countW][1] = P[i][1];
				W[countW][2] = P[i][2];
				countW++;
				i++;
			}
			else
				countW--;
		}


		hullX = new double[countW];
		hullY = new double[countW];
		hullCount = countW;
		for (int j = 0; j < countW; j++){
			hullX[j] = W[j][0];
			hullY[j] = W[j][1];
		}


		if (count != hullCount)
			convex = false;


	}



    public boolean isInside(double x, double y){
        if (hullCount < 3)
        	return false;
        int s = sign((y-hullY[0])*(hullX[1]-hullX[0])-(x-hullX[0])*(hullY[1]-hullY[0]));
        if (s == 0)
        	s = sign((y-hullY[1])*(hullX[2]-hullX[1])-(x-hullX[1])*(hullY[2]-hullY[1]));
        for (int i = 1; i < hullCount - 1; i++){
			if (s*sign((y-hullY[i])*(hullX[i+1]-hullX[i])-(x-hullX[i])*(hullY[i+1]-hullY[i])) < 0) return false;
		}
		if (s*sign((y-hullY[hullCount - 1])*(hullX[0]-hullX[hullCount - 1])-(x-hullX[hullCount - 1])*(hullY[0]-hullY[hullCount - 1])) < 0) return false;
		return true;
	}



    public int isOn(double x, double y){
        if (hullCount < 3)
        	return -1;
        for (int i = 0; i < hullCount - 1; i++){
			if (Math.abs((y-hullY[i])*(hullX[i+1]-hullX[i])-(x-hullX[i])*(hullY[i+1]-hullY[i])) < tolerance) return i;
		}
		if (Math.abs((y-hullY[hullCount - 1])*(hullX[0]-hullX[hullCount - 1])-(x-hullX[hullCount - 1])*(hullY[0]-hullY[hullCount - 1])) < tolerance) return hullCount - 1;
		return -1;
	}



    public int isOnSimple(double x, double y){
        if (count < 3)
        	return -1;
        for (int i = 0; i < count - 1; i++){
			if (Math.abs((y-nodeY[i])*(nodeX[i+1]-nodeX[i])-(x-nodeX[i])*(nodeY[i+1]-nodeY[i])) < tolerance) return i;
		}
		if (Math.abs((y-nodeY[count - 1])*(nodeX[0]-nodeX[count - 1])-(x-nodeX[count - 1])*(nodeY[0]-nodeY[count - 1])) < tolerance) return count - 1;
		return -1;
	}


	public double radius(){

		double r = 0.0;
		for (int i = 0; i < hullCount; i++)
			for (int j = i + 1; j < hullCount; j++)
				if (dist(hullX[i], hullY[i], hullX[j], hullY[j]) > r)
					r = dist(hullX[i], hullY[i], hullX[j], hullY[j]);
		return mlsFudge*r;

	}


	public double[] thirdRadius(){
		double[] r = new double[count];
		switch(count) {
			case 0:
			return new double[] {};

			case 1:
			return new double[] {radius()};

			case 2:
			return new double[] {radius(), radius()};

			case 3:
			return new double[] {radius(), radius(), radius()};
		}
		for(int k1 = 0; k1 < 101; k1++)
		for(int k2 = 0; k2 < 101; k2++){
			double r1, r2, r3, x, y, temp;
			int i1, i2, i3;
			x = minX + (maxX - minX)*k1 / 100;
			y = minY + (maxY - minY)*k2 / 100;
			if (!isInside(x,y))
					continue;
			i1 = 0;
			i2 = 0;
			i3 = 0;
			r1 = Double.MAX_VALUE;
			r2 = Double.MAX_VALUE;
			r3 = Double.MAX_VALUE;
			for (int j = 0; j < count; j++){
				temp = dist(x, y, nodeX[j], nodeY[j]);
				if (temp <= r1){
					r3 = r2;
					i3 = i2;
					r2 = r1;
					i2 = i1;
					r1 = temp;
					i1 = j;
				}
				else if (temp <= r2){
					r3 = r2;
					i3 = i2;
					r2 = temp;
					i2 = j;
				}
				else if (temp <= r3){
					r3 = temp;
					i3 = j;
				}
			}
			if (r[i1] < mlsFudge * r1)
				r[i1] = mlsFudge * r1;
			if (r[i2] < mlsFudge * r2)
				r[i2] = mlsFudge * r2;
			if (r[i3] < mlsFudge * r3)
				r[i3] = mlsFudge * r3;
		}

		for(int k = 0; k < 10000 + count; ){
			double r1, r2, r3, x, y, temp;
			int i1, i2, i3;
			if (k < count){
				x = nodeX[k];
				y = nodeY[k];
			}
			else {
				x = Math.random() * (maxX - minX) + minX;
				y = Math.random() * (maxY - minY) + minY;
				if (!isInside(x,y))
					continue;
			}
			k++;
			i1 = 0;
			i2 = 0;
			i3 = 0;
			r1 = Double.MAX_VALUE;
			r2 = Double.MAX_VALUE;
			r3 = Double.MAX_VALUE;
			for (int j = 0; j < count; j++){
				temp = dist(x, y, nodeX[j], nodeY[j]);
				if (temp <= r1){
					r3 = r2;
					i3 = i2;
					r2 = r1;
					i2 = i1;
					r1 = temp;
					i1 = j;
				}
				else if (temp <= r2){
					r3 = r2;
					i3 = i2;
					r2 = temp;
					i2 = j;
				}
				else if (temp <= r3){
					r3 = temp;
					i3 = j;
				}
			}
			if (r[i1] < mlsFudge * r1)
				r[i1] = mlsFudge * r1;
			if (r[i2] < mlsFudge * r2)
				r[i2] = mlsFudge * r2;
			if (r[i3] < mlsFudge * r3)
				r[i3] = mlsFudge * r3;

		}
		return r;
	}


    private int sign(double x){
		if (x == 0) return 0;
		if (x > 0) return 1;
		if (x < 0) return -1;
		return 0;
	}

	private double dist(double x1, double y1, double x2, double y2){

		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));

	}



}
