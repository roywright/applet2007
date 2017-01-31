public class Array {

    //  Create methods

    public static String[] duplicate(int m, String c) {
        String[] o = new String[m];
        for (int i = 0; i < o.length; i++)
            o[i] = c;
        return o;
    }

    public static int[] duplicate(int m, int c) {
        int[] o = new int[m];
        for (int i = 0; i < o.length; i++)
            o[i] = c;
        return o;
    }

    public static double[][] one(int m, int n) {
        return one(m, n, 1.0);
    }

    public static double[][] one(int m, int n, double c) {
        double[][] o = new double[m][n];
        for (int i = 0; i < o.length; i++)
            for (int j = 0; j < o[i].length; j++)
                o[i][j] = c;
        return o;
    }

    public static double[] one(int m) {
        return one(m, 1.0);
    }

    public static double[] one(int m, double c) {
        double[] o = new double[m];
        for (int i = 0; i < o.length; i++)
            o[i] = c;
        return o;
    }

    public static double[][] increment(int m, int n, double begin, double pitch) {
        double[][] array = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                array[i][j] = begin + i * pitch;
            }
        }
        return array;
    }

    public static double[] increment(int m, double begin, double pitch) {
        double[] array = new double[m];
        for (int i = 0; i < m; i++) {
            array[i] = begin + i * pitch;
        }
        return array;
    }

    // Modify rows & colmumns methods

    public static double[] copy(double[] M) {
        double[] array = new double[M.length];
        System.arraycopy(M, 0, array, 0, M.length);
        return array;
    }

    public static double[][] copy(double[][] M) {
        double[][] array = new double[M.length][M[0].length];
        for (int i = 0; i < array.length; i++)
            System.arraycopy(M[i], 0, array[i], 0, M[i].length);
        return array;
    }

    public static double[][] getSubMatrixRangeCopy(double[][] M, int i1, int i2, int j1, int j2) {
        double[][] array = new double[i2 - i1 + 1][j2 - j1 + 1];
        for (int i = 0; i < i2 - i1 + 1; i++)
            System.arraycopy(M[i + i1], j1, array[i], 0, j2 - j1 + 1);
        ;
        return array;
    }

    public static double[][] getColumnsRangeCopy(double[][] M, int j1, int j2) {
        double[][] array = new double[M.length][j2 - j1 + 1];
        for (int i = 0; i < M.length; i++)
            System.arraycopy(M[i], j1, array[i], 0, j2 - j1 + 1);
        return array;
    }

    public static double[] getColumnCopy(double[][] M, int j) {
        double[] array = new double[M.length];
        for (int i = 0; i < M.length; i++)
            array[i] = M[i][j];
        return array;
    }

    public static double[] getColumnCopy(double[][][] M, int j, int k) {
        double[] array = new double[M.length];
        for (int i = 0; i < M.length; i++)
            array[i] = M[i][j][k];
        return array;
    }

    public static double[] getRowCopy(double[][] M, int i) {
        double[] array = new double[M[0].length];
        System.arraycopy(M[i], 0, array, 0, M[i].length);
        return array;
    }

    public static double[][] getRowsRangeCopy(double[][] M, int i1, int i2) {
        double[][] array = new double[i2 - i1 + 1][M[0].length];
        for (int i = 0; i < i2 - i1 + 1; i++)
            System.arraycopy(M[i + i1], 0, array[i], 0, M[i + i1].length);
        return array;
    }

    public static double[] getRangeCopy(double[] M, int j1, int j2) {
        double[] array = new double[j2 - j1 + 1];
        System.arraycopy(M, j1, array, 0, j2 - j1 + 1);
        return array;
    }

    public static int getColumnDimension(double[][] M, int i) {
        return M[i].length;
    }

    public static double[][] insertColumns(double[][] x, double[][] y, int J) {
        double[][] array = new double[x.length][x[0].length + y[0].length];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(x[i], 0, array[i], 0, J);
            System.arraycopy(y[i], 0, array[i], J, y[i].length);
            System.arraycopy(x[i], J, array[i], J + y[i].length, x[i].length - J);
        }
        return array;
    }

    public static double[][] insertColumn(double[][] x, double[] y, int J) {
        double[][] array = new double[x.length][x[0].length + 1];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(x[i], 0, array[i], 0, J);
            array[i][J] = y[i];
            System.arraycopy(x[i], J, array[i], J + 1, x[i].length - J);
        }
        return array;
    }

    public static double[][] insertRows(double[][] x, double[][] y, int I) {
        double[][] array = new double[x.length + y.length][x[0].length];
        for (int i = 0; i < I; i++)
            System.arraycopy(x[i], 0, array[i], 0, x[i].length);
        for (int i = 0; i < y.length; i++)
            System.arraycopy(y[i], 0, array[i + I], 0, y[i].length);
        for (int i = 0; i < x.length - I; i++)
            System.arraycopy(x[i + I], 0, array[i + I + y.length], 0, x[i].length);
        return array;
    }

    public static double[][] insertRow(double[][] x, double[] y, int I) {
        double[][] array = new double[x.length + 1][x[0].length];
        for (int i = 0; i < I; i++)
            System.arraycopy(x[i], 0, array[i], 0, x[i].length);
        System.arraycopy(y, 0, array[I], 0, y.length);
        for (int i = 0; i < x.length - I; i++)
            System.arraycopy(x[i + I], 0, array[i + I + 1], 0, x[i].length);
        return array;
    }

    public static double[][] deleteColumnsRange(double[][] x, int J1, int J2) {
        double[][] array = new double[x.length][x[0].length - (J2 - J1 + 1)];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(x[i], 0, array[i], 0, J1);
            System.arraycopy(x[i], J2 + 1, array[i], J1, x[i].length - (J2 + 1));
        }
        return array;
    }

    public static double[][] deleteRowsRange(double[][] x, int I1, int I2) {
        double[][] array = new double[x.length - (I2 - I1 + 1)][x[0].length];
        for (int i = 0; i < I1; i++)
            System.arraycopy(x[i], 0, array[i], 0, x[i].length);
        for (int i = 0; i < x.length - I2 - 1; i++)
            System.arraycopy(x[i + I2 + 1], 0, array[i + I1], 0, x[i].length);
        return array;
    }

    public static double[] deleteRange(double[] x, int J1, int J2) {
        double[] array = new double[x.length - (J2 - J1 + 1)];
        System.arraycopy(x, 0, array, 0, J1);
        System.arraycopy(x, J2 + 1, array, J1, x.length - (J2 + 1));
        return array;
    }

    public static double[][] buildXY(double Xmin, double Xmax, double[] Y) {
        int n = Y.length;
        double[][] XY = new double[n][2];
        for (int i = 0; i < n; i++) {
            XY[i][0] = Xmin + (Xmax - Xmin) * (double) i / (double) (n - 1);
            XY[i][1] = Y[i];
        }
        return XY;
    }

    // min/max methods

    public static double[] min(double[][] M) {
        double[] min = new double[M[0].length];
        for (int j = 0; j < min.length; j++) {
            min[j] = M[0][j];
            for (int i = 1; i < M.length; i++)
                min[j] = Math.min(min[j], M[i][j]);
        }
        return min;
    }

    public static double min(double[] M) {
        double min = M[0];
        for (int i = 1; i < M.length; i++)
            min = Math.min(min, M[i]);
        return min;
    }

    public static double[] max(double[][] M) {
        double[] max = new double[M[0].length];
        for (int j = 0; j < max.length; j++) {
            max[j] = M[0][j];
            for (int i = 1; i < M.length; i++)
                max[j] = Math.max(max[j], M[i][j]);
        }
        return max;
    }

    public static double max(double[] M) {
        double max = M[0];
        for (int i = 1; i < M.length; i++)
            max = Math.max(max, M[i]);
        return max;
    }

    public static int[] minIndex(double[][] M) {
        int[] minI = new int[M[0].length];
        for (int j = 0; j < minI.length; j++) {
            minI[j] = 0;
            for (int i = 1; i < M.length; i++)
                if (M[i][j] < M[minI[j]][j])
                    minI[j] = i;

        }
        return minI;
    }

    public static int[] maxIndex(double[][] M) {
        int[] maxI = new int[M[0].length];
        for (int j = 0; j < maxI.length; j++) {
            maxI[j] = 0;
            for (int i = 1; i < M.length; i++)
                if (M[i][j] > M[maxI[j]][j])
                    maxI[j] = i;
        }
        return maxI;
    }

    // print methods

    // check methods

    public static void throwError(String msg) {
        throw new IllegalArgumentException(msg);
    }

    public static void checkColumnDimension(double[][] M, int n) {
        for (int i = 0; i < M.length; i++)
            if (M[i].length != n)
                throwError("row " + i + " have " + M[i].length + " columns instead of " + n + " columns expected.");
    }

    public static boolean isColumnDimension(double[][] M, int n) {
        for (int i = 0; i < M.length; i++)
            if (M[i].length != n)
                return false;
        return true;
    }

    public static void checkRowDimension(double[][] M, int m) {
        if (M.length != m)
            throwError("columns have " + M.length + " rows instead of " + m + " rows expected.");
    }

    public static boolean isRowDimension(double[][] M, int m) {
        if (M.length != m)
            return false;
        return true;
    }

    public static void checkLength(double[] M, int n) {
        if (M.length != n)
            throwError("row have " + M.length + " elements instead of " + n + " elements expected.");
    }

    public static boolean isLength(double[] M, int n) {
        if (M.length != n)
            return false;
        return true;
    }

}
