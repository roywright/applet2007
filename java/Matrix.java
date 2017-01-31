public class Matrix{
	private double[][] A;



	public Matrix(double[][] input){
		int m = input.length;
		int n = input[0].length;
		A = new double[m][n];
		for (int i = 0; i < m; i++)
		for (int j = 0; j < n; j++)
			A[i][j] = input[i][j];
	}

	public Matrix(double input){
		A = new double[1][1];
		A[0][0] = input;
	}


	public Matrix(int m, int n){
		A = new double[m][n];
		for (int i = 0; i < m; i++)
		for (int j = 0; j < n; j++)
			A[i][j] = 0.0;
	}


	public Matrix(int m){
		A = new double[m][m];
		for (int i = 0; i < m; i++)
		for (int j = 0; j < m; j++){
			if (i == j)
				A[i][j] = 1.0;
			else
				A[i][j] = 0.0;

		}
	}


	public Matrix times(Matrix input){
		int m = input.size(0);
		int n = input.size(1);
		double[][] retArray;
		if (m == 1 && n == 1){
			retArray = new double[size(0)][size(1)];
			for (int i = 0; i < size(0); i++)
			for (int j = 0; j < size(1); j++){
				retArray[i][j] = A[i][j]*input.entry(0,0);
			}
			return new Matrix(retArray);
		}
		if (size(0) == 1 && size(1) == 1){
			retArray = new double[m][n];
			for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++){
				retArray[i][j] = input.entry(i,j)*A[0][0];
			}
			return new Matrix(retArray);
		}
		if (m != size(1)){
			System.out.print(size(0));
			System.out.print("x");
			System.out.print(size(1));
			System.out.print(" ");
			System.out.print(m);
			System.out.print("x");
			System.out.println(n);
			return new Matrix(0.0);
		}
		retArray = new double[size(0)][n];
		for (int i = 0; i < size(0); i++)
		for (int j = 0; j < n; j++){
			retArray[i][j] = 0.0;
			for (int k = 0; k < m; k++)
				retArray[i][j] += A[i][k]*input.entry(k,j);
		}
		return new Matrix(retArray);
	}



	public Matrix plus(Matrix input){
		int m = input.size(0);
		int n = input.size(1);
		double[][] retArray = new double[m][n];
		if (m != size(0) || n != size(1)){
			return new Matrix(0.0);
		}
		for (int i = 0; i < m; i++)
		for (int j = 0; j < n; j++){
			retArray[i][j] = A[i][j] + input.entry(i,j);
		}
		return new Matrix(retArray);
	}


	public Matrix transpose(){
		double[][] retArray = new double[size(1)][size(0)];
		for (int i = 0; i < size(1); i++)
		for (int j = 0; j < size(0); j++){
			retArray[i][j] = A[j][i];
		}
		return new Matrix(retArray);
	}



	public double entry(int i, int j){
		return A[i][j];
	}

	public int size(int i){
		if (i == 0) return A.length;
		return A[0].length;
	}




	public Matrix inverse(){
		int m = size(0);
		int n = size(1);
		if (m != n)
			return new Matrix(0.0);
		Matrix inv = new Matrix(m);
		Matrix left = new Matrix(A);

		for(int i = 0; i < m-1; i++)
		for(int j = i; j < m; j++)
			if (Math.abs(left.entry(i,i)) < Math.abs(left.entry(j,i))){
				inv.swaprows(i,j);
				left.swaprows(i,j);
			}


		for(int i = 0; i < m-1; i++)
		for(int j = i+1; j < m; j++){
			inv.subtractrows(j, i, left.entry(j,i) / left.entry(i,i));
			left.subtractrows(j, i, left.entry(j,i) / left.entry(i,i));
		}

		for(int i = m-1; i > 0; i--)
		for(int j = i-1; j >= 0; j--){
			inv.subtractrows(j, i, left.entry(j,i) / left.entry(i,i));
			left.subtractrows(j, i, left.entry(j,i) / left.entry(i,i));
		}


		for(int i = 0; i < m; i++){
			inv.dividerow(i, left.entry(i,i));
			left.dividerow(i, left.entry(i,i));
		}

		return inv;

	}




	private void swaprows(int k, int l){
		double[][] newA = new double[size(0)][size(1)];
		for (int i = 0; i < size(0); i++){
			int ii = i;
			if (ii == k)
				ii = l;
			else if (ii == l)
				ii = k;
		for (int j = 0; j < size(1); j++)
			newA[ii][j] = A[i][j];
		}
		A = newA;
	}

	private void subtractrows(int k, int l, double factor){
		double[][] newA = new double[size(0)][size(1)];
		for (int j = 0; j < size(1); j++)
			A[k][j] -= factor*A[l][j];
	}

	private void dividerow(int k, double factor){
		double[][] newA = new double[size(0)][size(1)];
		for (int j = 0; j < size(1); j++)
			A[k][j] /= factor;
	}







}