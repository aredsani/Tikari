import Utils.GeometricObjects.Point_2D;

import java.util.Vector;

/**
 * Created by polojushravan on 1/8/2016.
 */
public class Calibrator {

    private double[] c=new double[9];
    double[] x;

    public Calibrator(Vector<Point_2D> Cal_Col, double length){

        double[][] A=new double[][]{{Cal_Col.elementAt(0).getI(),0,Cal_Col.elementAt(0).getJ(),0,1, 0,0,0},{0, Cal_Col.elementAt(0).getI(), 0 ,Cal_Col.elementAt(0).getJ(), 0 ,1, 0, 0},{Cal_Col.elementAt(1).getI(), 0, Cal_Col.elementAt(1).getJ(), 0, 1, 0, length * Cal_Col.elementAt(1).getI(), length * Cal_Col.elementAt(1).getJ()},{0, Cal_Col.elementAt(1).getI(), 0 ,Cal_Col.elementAt(1).getJ(), 0, 1, 0,0},{  Cal_Col.elementAt(2).getI(), 0, Cal_Col.elementAt(2).getJ() ,0 ,1, 0, length * Cal_Col.elementAt(2).getI(), length * Cal_Col.elementAt(2).getJ()},{0 ,Cal_Col.elementAt(2).getI(), 0, Cal_Col.elementAt(2).getJ() ,0 ,1 ,length * Cal_Col.elementAt(2).getI(), length * Cal_Col.elementAt(2).getJ()},{Cal_Col.elementAt(3).getI() ,0 ,Cal_Col.elementAt(3).getJ() ,0 ,1, 0 ,0 ,0},{0, Cal_Col.elementAt(3).getI(), 0, Cal_Col.elementAt(3).getJ(), 0, 1, length*Cal_Col.elementAt(3).getI(), length*Cal_Col.elementAt(3).getJ()}};
        double[] B=new double[]{0, 0, length, 1, length, length, 0, length};

        x=solve(A,B);

        c[0]=-(x[0]*x[0]+x[1]*x[1]-x[2]*x[2]-x[3]*x[3]+(x[7]*x[7]-x[6]*x[6])/(x[6]*x[7])*(x[0]*x[2]+x[1]*x[3]))/(2*x[3]*x[7]-2*x[1]*x[6]-(x[7]*x[7]-x[6]*x[6])/(x[6]*x[7])*(x[3]*x[6]+x[1]*x[7]));
        c[1]=x[0];
        c[2]=x[1]-c[0]*x[6];
        c[3]=x[2];
        c[4]=x[3]-c[0]*x[7];
        c[5]=x[4];
        c[6]=x[5]+c[0];
        c[7]=x[6];
        c[8]=x[7];

    }
    public double[] getC(){
        return c;
    }
    private  double[] solve(double[][] A, double[] B) {
        double[][] Ainv= invert(A);
        return multiply(Ainv,B);
    }
    private  double[][] invert(double a[][])    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i]*b[index[i]][k];

        // Perform backward substitutions
        for (int i=0; i<n; ++i)
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k)
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }
    private  void gaussian(double a[][], int index[])    {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i=0; i<n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i)
        {
            double c1 = 0;
            for (int j=0; j<n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j)
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i)
            {
                double pj = a[index[i]][j]/a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }
    private  double[] multiply(double[][] a, double[] b) {
        int rowsInA = a.length;
        int columnsInA = a[0].length; // same as rows in
        double[] c = new double[rowsInA];
        for (int i = 0; i < rowsInA; i++) {
            for (int k = 0; k < columnsInA; k++) {
                c[i] = c[i] + a[i][k] * b[k];
            }
        }
        return c;
    }
}
