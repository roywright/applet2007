public class Base {

    public final static int LINEAR = 0;

    public final static int LOG = 1;

    public double[][] baseCoords;

    protected double[] precisionUnit;

    public double[] roundXmin;

    public double[] roundXmax;

    protected double[] trueXmin;

    protected double[] trueXmax;

    public int dimension;

    public int[] axesScales;

    public Base(double[] Xmi, double[] Xma, int[] scales) {
        trueXmin = Xmi;
        trueXmax = Xma;
        dimension = trueXmin.length;
        axesScales = scales;
        initBounds(trueXmin.length);
        setRoundBounds(trueXmin, trueXmax);
        setBaseCoords();
    }

    public void setAxesScales(int[] scales) {
        axesScales = scales;
        setRoundBounds(trueXmin, trueXmax);
        setBaseCoords();
    }

    public void setAxesScales(int i, int scale) {
        axesScales[i] = scale;
        setRoundBounds(trueXmin, trueXmax);
        setBaseCoords();
    }


    private void setPrecisionUnit(int i, double Xmi, double Xma) {
//        if (Xma - Xmi > 0) {
//            precisionUnit[i] = Math.pow(10, Math.floor(Math.log(Xma - Xmi) / Math.log(10)));
//        } else {
//            precisionUnit[i] = 1;
//        }
        precisionUnit[i] = 1.0E-7;
    }


    public double[][] getCoords() {
        return baseCoords;
    }


    public int[] getAxesScales() {
        return axesScales;
    }

    public int getAxeScale(int i) {
        return axesScales[i];
    }

    public double[] getMinBounds() {
        return roundXmin;
    }

    public double[] getMaxBounds() {
        return roundXmax;
    }

    public double[] getPrecisionUnit() {
        return precisionUnit;
    }


    private void initBounds(int i) {
        roundXmin = new double[i];
        roundXmax = new double[i];
        trueXmin = new double[i];
        trueXmax = new double[i];
    }

    private void setBounds(int i, double Xmi, double Xma) {
        if ((Xmi < 0) && (axesScales[i] == Base.LOG)) {
            throw new IllegalArgumentException("Error while bounding dimension " + (i + 1) + " : bounds [" + Xmi + "," + Xma
                    + "] are incompatible with Logarithm scale.");
        }
        if (Xma - Xmi < 1) {
//            Xmi = Xma - 1;
        }
        if (Xmi > Xma) {
            throw new IllegalArgumentException("Error while bounding dimension " + (i + 1) + " : min " + Xmi + " must be < to max " + Xma);
        }
        roundXmin[i] = Xmi;
        roundXmax[i] = Xma;
    }

    private void setBounds(double[] Xmi, double[] Xma) {
        for (int i = 0; i < Xmi.length; i++) {
            setBounds(i, Xmi[i], Xma[i]);
        }
    }

    public void setFixedBounds(int i, double Xmi, double Xma) {
        setPrecisionUnit(i, Xmi, Xma);
        setBounds(i, Xmi, Xma);
    }

    public void setFixedBounds(double[] Xmi, double[] Xma) {
        for (int i = 0; i < Xmi.length; i++) {
            setFixedBounds(i, Xmi[i], Xma[i]);
        }
    }

    public void setRoundBounds(int i, double Xmi, double Xma) {
        setPrecisionUnit(i, Xmi, Xma);
        if (axesScales[i] == Base.LOG) {
            setBounds(i, Math.pow(10, Math.floor(Math.log(Xmi) / Math.log(10))), Math.pow(10, Math.ceil(Math.log(Xma) / Math.log(10))));
        } else if (axesScales[i] == Base.LINEAR) {
            setBounds(i, precisionUnit[i] * (Math.floor(Xmi / precisionUnit[i])), precisionUnit[i] * (Math.ceil(Xma / precisionUnit[i])));
        }
    }

    public void setRoundBounds(double[] Xmi, double[] Xma) {
        precisionUnit = new double[Xmi.length];
        for (int i = 0; i < Xmi.length; i++) {
            trueXmin[i] = Xmi[i];
            trueXmax[i] = Xma[i];
            setRoundBounds(i, trueXmin[i], trueXmax[i]);
        }
    }

    public void includeInBounds(int dim, double XY) {
        for (int i = 0; i < roundXmin.length; i++) {
            if (i == dim)
                if (XY < trueXmin[i])
                    trueXmin[i] = XY;
        }
        for (int i = 0; i < roundXmax.length; i++) {
            if (i == dim)
                if (XY > trueXmax[i])
                    trueXmax[i] = XY;
        }
        setRoundBounds(dim, trueXmin[dim], trueXmax[dim]);
        setBaseCoords();
    }

    public void includeInBounds(double[] XY) {
        for (int i = 0; i < roundXmin.length; i++) {
            if (XY[i] < trueXmin[i])
                trueXmin[i] = XY[i];
        }
        for (int i = 0; i < roundXmax.length; i++) {
            if (XY[i] > trueXmax[i])
                trueXmax[i] = XY[i];
        }
        setRoundBounds(trueXmin, trueXmax);
        setBaseCoords();
    }


    public void setBaseCoords() {

        baseCoords = new double[dimension + 1][];
        for (int i = 0; i < baseCoords.length; i++) {
            baseCoords[i] = (double[]) (roundXmin.clone());
            if (i > 0)
                baseCoords[i][i - 1] = roundXmax[i - 1];
        }
    }


    public boolean authorizedLogScale(int i) {
        if (roundXmin[i] > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < baseCoords.length; i++) {
            s.append("[");
            for (int j = 0; j < baseCoords[i].length; j++)
                s.append(baseCoords[i][j] + ",");
            s.deleteCharAt(s.length() - 1);
            s.append("]");
        }
        return s.toString();
    }
}