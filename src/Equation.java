import java.math.BigDecimal;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ahsan
 */
public class Equation
{

	 private double[] coefs;

	 public Equation(double coefs[])
	 {
		  this.coefs = coefs;
	 }

	 public double solve(int x)
	 {
		  return solve((double) x);
	 }

	 public double solve(double x)
	 {
		  BigDecimal val = BigDecimal.ZERO;

		  for (int i = 0; i < coefs.length; i++)
		  {
			   
			   val = val.add(BigDecimal.valueOf(Math.pow(x, coefs.length - i - 1) * coefs[i]));
		  }

		  return val.doubleValue();
	 }

	 public int getDegree()
	 {
		  return coefs.length - 1;
	 }
}
