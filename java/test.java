public class test {

   public static void main(String []args) {


      double[][] temp = new double[3][3];
      temp[0][0] = 3;
      temp[0][1] = 4;
      temp[0][2] = 5;
      temp[1][0] = 5;
      temp[1][1] = 6;
      temp[1][2] = 9;
      temp[2][0] = 1;
      temp[2][1] = 4;
      temp[2][2] = 2;
      Matrix A = new Matrix(temp);
      Matrix Ainv = A.transpose();
      System.out.print(Ainv.entry(0,0));
      System.out.print(" ");
      System.out.print(Ainv.entry(0,1));
      System.out.print(" ");
      System.out.println(Ainv.entry(0,2));
      System.out.print(Ainv.entry(1,0));
      System.out.print(" ");
      System.out.print(Ainv.entry(1,1));
      System.out.print(" ");
      System.out.println(Ainv.entry(1,2));
      System.out.print(Ainv.entry(2,0));
      System.out.print(" ");
      System.out.print(Ainv.entry(2,1));
      System.out.print(" ");
      System.out.println(Ainv.entry(2,2));



   }
}
