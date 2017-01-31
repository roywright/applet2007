public abstract class Projection {

    int[][] baseScreenCoords;

    public static double DEFAULT_BORDER = 0.15;

    protected double borderCoeff = DEFAULT_BORDER;

    protected AWTDrawer draw;

    public Projection(AWTDrawer _draw) {
        draw = _draw;
    }

    protected void initBaseCoordsProjection() {
        //System.out.println("Projection.initBaseCoordsProjection");
        baseScreenCoords = new int[draw.canvas.base.baseCoords.length][2];
        for (int i = 0; i < draw.canvas.base.dimension + 1; i++) {
            double[] ratio = baseCoordsScreenProjectionRatio(draw.canvas.base.baseCoords[i]);
            baseScreenCoords[i][0] = (int) (draw.canvas.panelSize[0] * (borderCoeff + (1 - 2 * borderCoeff) * ratio[0]));
            baseScreenCoords[i][1] = (int) (draw.canvas.panelSize[1] - draw.canvas.panelSize[1] * (borderCoeff + (1 - 2 * borderCoeff) * ratio[1]));
        }
    }

    // ///////////////////////////////////////////
    // ////// move methods ///////////////////////
    // ///////////////////////////////////////////

    public void translate(int[] screenTranslation) {
        for (int i = 0; i < draw.canvas.base.dimension + 1; i++) {
            baseScreenCoords[i][0] = baseScreenCoords[i][0] + screenTranslation[0];
            baseScreenCoords[i][1] = baseScreenCoords[i][1] + screenTranslation[1];
        }
    }

    public void dilate(int[] screenOrigin, double[] screenRatio) {
        //System.out.println("screenOrigin = "+screenOrigin[0]+" , "+screenOrigin[1]);
        //System.out.println("screenRatio = "+screenRatio[0]+" , "+screenRatio[1]);
        for (int i = 0; i < draw.canvas.base.dimension + 1; i++) {
            //System.out.println("baseScreenCoords["+i+"] = "+baseScreenCoords[i][0]+" , "+baseScreenCoords[i][1]);
            baseScreenCoords[i][0] = (int) ((baseScreenCoords[i][0] - screenOrigin[0]) / screenRatio[0]);
            baseScreenCoords[i][1] = (int) ((baseScreenCoords[i][1] - screenOrigin[1]) / screenRatio[1]);
            //System.out.println(" -> baseScreenCoords["+i+"] = "+baseScreenCoords[i][0]+" , "+baseScreenCoords[i][1]);
        }
    }

    // ///////////////////////////////////////////
    // ////// projection method //////////////////
    // ///////////////////////////////////////////

    public int[] screenProjection(double[] pC) {
        double[] sC = new double[2];
        sC[0] = baseScreenCoords[0][0];
        sC[1] = baseScreenCoords[0][1];
        for (int i = 0; i < draw.canvas.base.dimension; i++) {
            if (draw.canvas.base.axesScales[i] == Base.LOG) {
                sC[0] += ((log(pC[i]) - log(draw.canvas.base.baseCoords[0][i])) / (log(draw.canvas.base.baseCoords[i + 1][i]) - log(draw.canvas.base.baseCoords[0][i])))
                        * (baseScreenCoords[i + 1][0] - baseScreenCoords[0][0]);
                sC[1] += ((log(pC[i]) - log(draw.canvas.base.baseCoords[0][i])) / (log(draw.canvas.base.baseCoords[i + 1][i]) - log(draw.canvas.base.baseCoords[0][i])))
                        * (baseScreenCoords[i + 1][1] - baseScreenCoords[0][1]);
            } else if (draw.canvas.base.axesScales[i] == Base.LINEAR) {
                sC[0] += ((pC[i] - draw.canvas.base.baseCoords[0][i]) / (draw.canvas.base.baseCoords[i + 1][i] - draw.canvas.base.baseCoords[0][i]))
                        * (baseScreenCoords[i + 1][0] - baseScreenCoords[0][0]);
                sC[1] += ((pC[i] - draw.canvas.base.baseCoords[0][i]) / (draw.canvas.base.baseCoords[i + 1][i] - draw.canvas.base.baseCoords[0][i]))
                        * (baseScreenCoords[i + 1][1] - baseScreenCoords[0][1]);
            }
        }
        return new int[] { (int) sC[0], (int) sC[1] };
    }





    public double[] fromScreenProjection(int[] sC) {


		double x = 0.;
		double y = 0.;
		int[] _sC;
		int[] _sCnew;
		double dx;
		double dy;
		int k = 0;

		do {
			dx = (Math.random() - .5) * 2;
			dy = (Math.random() - .5) * 2;
			_sC = screenProjection(new double[]{x, y, 0});
			_sCnew = screenProjection(new double[]{x + dx, y + dy, 0});

			while (Math.sqrt((_sC[0] - sC[0])*(_sC[0] - sC[0]) + (_sC[1] - sC[1])*(_sC[1] - sC[1])) > Math.sqrt((_sCnew[0] - sC[0])*(_sCnew[0] - sC[0]) + (_sCnew[1] - sC[1])*(_sCnew[1] - sC[1]))){
				x = x + dx;
				y = y + dy;
				_sC = screenProjection(new double[]{x, y, 0});
				_sCnew = screenProjection(new double[]{x + dx, y + dy, 0});
			}
			k++;
			if (k > 10000000)
				break;

		} while (Math.abs(_sC[0] - sC[0]) > 1 || Math.abs(_sC[1] - sC[1]) > 1);

		return new double[]{x, y};

	}




    public int[] screenProjectionRatio(double[] rC) {
        double[] sC = new double[2];
        sC[0] = baseScreenCoords[0][0];
        sC[1] = baseScreenCoords[0][1];
        for (int i = 0; i < draw.canvas.base.dimension; i++) {
            sC[0] += rC[i] * (baseScreenCoords[i + 1][0] - baseScreenCoords[0][0]);
            sC[1] += rC[i] * (baseScreenCoords[i + 1][1] - baseScreenCoords[0][1]);
        }
        return new int[] { (int) sC[0], (int) sC[1] };
    }

    private double log(double x) {
        return Math.log(x);
    }

    protected abstract double[] baseCoordsScreenProjectionRatio(double[] xyz);

}
