import java.math.BigDecimal;
import java.math.RoundingMode;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ahsan
 */
public class CurveEngine
{

	 private UserPoints points;
	 private Equation lastEquationCache;
	 private int lastEquationVals = -1;
	 private CurveType type;

	 public enum CurveType
	 {

		  LINEAR("Linear", 1), QUADRATIC("Quadratic", 2), CUBIC("Cubic", 3), NONE("None", -2), OTHER("N Degree Polynomial", null);
		  private String curveName; // in meters
		  private Integer degree;
		  private static UserPoints points;
		  private static CurveType[] secondary =
		  {
			   LINEAR, QUADRATIC, CUBIC, NONE
		  };

		  CurveType(String curveName, Integer degree)
		  {
			   this.curveName = curveName;
			   this.degree = degree;

		  }

		  public String toString()
		  {
			   return this.curveName;
		  }

		  public Integer getDegree()
		  {
			   return degree;
		  }

		  public void setDegree(int degree)
		  {
			   if (this == OTHER)
			   {
				    this.degree = degree;
			   }
		  }

		  public static CurveType[] valuesNew()
		  {
			   if (points.size() >= 5)
			   {
				    return values();
			   }
			   else
			   {
				    return secondary;
			   }
		  }
	 }

	 public CurveEngine(UserPoints p)
	 {
		  this.points = p;
		  CurveType.points = p;
	 }

	 public void setCurve(int degree)
	 {
		  if (degree == -1)
		  {
			   return;
		  }
		  
		  if (degree == -2)
		  {
			   type = null;
			   return;
		  }

		  if (degree == 1)
		  {
			   type = CurveType.LINEAR;
		  }
		  else if (degree == 2)
		  {
			   type = CurveType.QUADRATIC;
		  }
		  else if (degree == 3)
		  {
			   type = CurveType.CUBIC;
		  }
		  else
		  {
			   type = CurveType.OTHER;
			   type.setDegree(degree);
		  }
	 }

	 public int getDegree()
	 {
		  if (type == null && points.size() == 0)
		  {
			   return -1;
		  }

		  if (type == null)
		  {
			   return 1;
		  }

		  return type.degree;
	 }

	 public boolean canStartPlotting()
	 {
		  if (type == null)
		  {
			   return false;
		  }

		  return points.size() > type.degree;
	 }

	 public Equation calculateLinearCurve() throws NoUserPointException
	 {

		  if (lastEquationCache != null)
		  {
			   if (lastEquationVals == points.size() && lastEquationCache.getDegree() == 1)
			   {
				    return lastEquationCache;
			   }
		  }

		  double sumX = 0D, sumY = 0D;

		  for (UserPoint p : points)
		  {
			   sumX += p.x;
			   sumY += p.y;
		  }

		  double xBar = sumX / points.size(), yBar = sumY / points.size();

		  double xxBar = 0.0, xyBar = 0.0;

		  for (UserPoint p : points)
		  {
			   double xMinusXbar = p.x - xBar;
			   double yMinusYbar = p.y - yBar;

			   xxBar += xMinusXbar * xMinusXbar;
			   xyBar += xMinusXbar * yMinusYbar;

		  }

		  double eqnVal1 = xyBar / xxBar;
		  double eqnVal2 = yBar - (eqnVal1 * xBar);

		  if (Double.isNaN(eqnVal1) || Double.isNaN(eqnVal2))
		  {
			   return null;
		  }

		  lastEquationCache = new Equation(new double[]
				{
					 eqnVal1, eqnVal2
				});
		  lastEquationVals = points.size();

		  return lastEquationCache;
	 }

	 public Equation calculateCurve() throws NoUserPointException
	 {
		  if (type == null)
		  {
			   return null;
		  }
		  if (type == CurveType.LINEAR)
		  {
			   return calculateLinearCurve();
		  }

		  int degree = type.getDegree();

		  if (lastEquationCache != null)
		  {
			   if (lastEquationVals == points.size() && lastEquationCache.getDegree() == degree)
			   {
				    return lastEquationCache;
			   }
		  }

		  BigDecimal mainMatrix[][][] = new BigDecimal[degree + 1][degree + 1][degree + 1];

		  BigDecimal[] sXVals = new BigDecimal[degree * 2 + 1];
		  BigDecimal[] sYVals = new BigDecimal[degree + 1];

		  sXVals[degree * 2] = BigDecimal.valueOf(points.size());
		  for (int i = 1; i < sXVals.length; i++)
		  {
			   sXVals[sXVals.length - i - 1] = getSxy(i, 0);
		  }

		  for (int i = 0; i < sYVals.length; i++)
		  {
			   sYVals[sYVals.length - i - 1] = getSxy(i, 1);
		  }


		  BigDecimal[][] denoMatrix = new BigDecimal[degree + 1][degree + 1];

		  for (int i = 0; i < denoMatrix.length; i++)
		  {
			   for (int j = 0; j < denoMatrix.length; j++)
			   {
				    denoMatrix[i][j] = sXVals[j + i];
			   }
		  }

		  BigDecimal denominator = determinant(denoMatrix);

		  if (denominator.equals(BigDecimal.ZERO))
		  {
			   return null;
		  }

		  for (int i = 0; i < mainMatrix.length; i++)
		  {

			   for (int j = 0; j < mainMatrix.length; j++)
			   {

				    for (int k = 0; k < mainMatrix.length; k++)
				    {
						if (i == k)
						{
							 mainMatrix[i][j][k] = sYVals[j];
						}
						else
						{
							 mainMatrix[i][j][k] = sXVals[j + k];
						}
				    }
			   }
		  }
		  double vals[] = new double[degree + 1];

		  for (int i = 0; i < vals.length; i++)
		  {
			   vals[i] = determinant(mainMatrix[i]).divide(denominator, 5, RoundingMode.HALF_UP).doubleValue();

			   if (Double.isNaN(i))
			   {
				    return null;
			   }
		  }

		  lastEquationCache = new Equation(vals);

		  lastEquationVals = points.size();

		  return lastEquationCache;
	 }
	 /*
	 public Equation calculateQuadCurve() throws NoUserPointException
	 {
	 if (lastEquationCache != null)
	 {
	 if (lastEquationVals == points.size() && lastEquationCache.getDegree() == 2)
	 {
	 return lastEquationCache;
	 }
	 }
	 
	 
	 
	 BigDecimal s40 = getSxy(4, 0);
	 BigDecimal s30 = getSxy(3, 0);
	 BigDecimal s20 = getSxy(2, 0);
	 BigDecimal s10 = getSxy(1, 0);
	 BigDecimal s00 = BigDecimal.valueOf(points.size());
	 
	 BigDecimal s21 = getSxy(2, 1);
	 BigDecimal s11 = getSxy(1, 1);
	 BigDecimal s01 = getSxy(0, 1);
	 
	 BigDecimal[][] denoMatrix =
	 {
	 {s40, s30, s20},
	 {s30, s20, s10},
	 {s20, s10, s00}
	 };
	 
	 BigDecimal fullDivisor = determinant(denoMatrix);
	 
	 if (fullDivisor.equals(BigDecimal.ZERO))
	 {
	 return null;
	 }
	 
	 BigDecimal[][] aMatrix =
	 {
	 {
	 s21, s30, s20
	 },
	 {
	 s11, s20, s10
	 },
	 {
	 s01, s10, s00
	 }
	 };
	 
	 BigDecimal aDividendedVal = determinant(aMatrix);
	 
	 BigDecimal aValue = (aDividendedVal).divide(fullDivisor, 5, RoundingMode.HALF_UP);
	 
	 BigDecimal[][] bMatrix =
	 {
	 {
	 s40, s21, s20
	 },
	 {
	 s30, s11, s10
	 },
	 {
	 s20, s01, s00
	 }
	 };
	 
	 
	 BigDecimal bDividendedVal = determinant(bMatrix);
	 
	 BigDecimal bValue = (bDividendedVal).divide(fullDivisor, 5, RoundingMode.HALF_UP);
	 
	 
	 BigDecimal[][] cMatrix =
	 {
	 {
	 s40, s30, s21
	 },
	 {
	 s30, s20, s11
	 },
	 {
	 s20, s10, s01
	 }
	 };
	 
	 BigDecimal cDividendedVal = determinant(cMatrix);
	 
	 
	 BigDecimal cValue = (cDividendedVal).divide(fullDivisor, 5, RoundingMode.HALF_UP);
	 
	 
	 lastEquationCache = new Equation(new double[]
	 {
	 aValue.doubleValue(), bValue.doubleValue(), cValue.doubleValue()
	 });
	 lastEquationVals = points.size();
	 
	 return lastEquationCache;
	 }
	 
	 /*helper methods*/

	 private BigDecimal getSxy(int xPower, int yPower) // get sum of x^xPower * y^yPower
	 {
		  BigDecimal sXY = BigDecimal.ZERO;
		  for (UserPoint p : points)
		  {
			   BigDecimal xToPower = BigDecimal.ONE;
			   for (int i = 0; i < xPower; i++)
			   {
				    xToPower = xToPower.multiply(BigDecimal.valueOf(p.x));
			   }

			   BigDecimal yToPower = BigDecimal.ONE;
			   for (int i = 0; i < yPower; i++)
			   {
				    yToPower = yToPower.multiply(BigDecimal.valueOf(p.y));
			   }
			   sXY = sXY.add(xToPower.multiply(yToPower));
		  }
		  return sXY;
	 }

	 /*public Equation calculateCubicCurve() throws NoUserPointException
	 {
	 if (lastEquationCache != null)
	 {
	 if (lastEquationVals == points.size() && lastEquationCache.getDegree() == 3)
	 {
	 return lastEquationCache;
	 }
	 }
	 
	 BigDecimal s60 = getSxy(6, 0);//[S60]
	 BigDecimal s50 = getSxy(5, 0);//[S50]
	 BigDecimal s40 = getSxy(4, 0);//[S40]
	 BigDecimal s30 = getSxy(3, 0);//[S30]
	 BigDecimal s20 = getSxy(2, 0);//[S20]
	 BigDecimal s10 = getSxy(1, 0);//[S10]
	 BigDecimal s00 = BigDecimal.valueOf(points.size());//[S00]
	 
	 BigDecimal s31 = getSxy(3, 1);//[S31]
	 BigDecimal s21 = getSxy(2, 1);//[S21]
	 BigDecimal s11 = getSxy(1, 1);//[S11]
	 BigDecimal s01 = getSxy(0, 1);//[S01]
	 
	 BigDecimal[][] denoMatrix =
	 {
	 {
	 s60, s50, s40, s30
	 },
	 {
	 s50, s40, s30, s20
	 },
	 {
	 s40, s30, s20, s10
	 },
	 {
	 s30, s20, s10, s00
	 }
	 };
	 
	 BigDecimal denominator = determinant(denoMatrix);
	 
	 if (denominator.equals(BigDecimal.ZERO))
	 {
	 return null;
	 }
	 
	 
	 BigDecimal[][] numeMatrix1 =
	 {
	 {
	 s31, s50, s40, s30
	 },
	 {
	 s21, s40, s30, s20
	 },
	 {
	 s11, s30, s20, s10
	 },
	 {
	 s01, s20, s10, s00
	 }
	 };
	 
	 BigDecimal[][] numeMatrix2 =
	 {
	 {
	 s60, s31, s40, s30
	 },
	 {
	 s50, s21, s30, s20
	 },
	 {
	 s40, s11, s20, s10
	 },
	 {
	 s30, s01, s10, s00
	 }
	 };
	 
	 BigDecimal[][] numeMatrix3 =
	 {
	 {
	 s60, s50, s31, s30
	 },
	 {
	 s50, s40, s21, s20
	 },
	 {
	 s40, s30, s11, s10
	 },
	 {
	 s30, s20, s01, s00
	 }
	 };
	 
	 BigDecimal[][] numeMatrix4 =
	 {
	 {
	 s60, s50, s40, s31
	 },
	 {
	 s50, s40, s30, s21
	 },
	 {
	 s40, s30, s20, s11
	 },
	 {
	 s30, s20, s10, s01
	 }
	 };
	 
	 BigDecimal numerator1 = determinant(numeMatrix1);
	 BigDecimal numerator2 = determinant(numeMatrix2);
	 BigDecimal numerator3 = determinant(numeMatrix3);
	 BigDecimal numerator4 = determinant(numeMatrix4);
	 
	 BigDecimal aValue = numerator1.divide(denominator, 5, RoundingMode.HALF_UP);
	 BigDecimal bValue = numerator2.divide(denominator, 5, RoundingMode.HALF_UP);
	 BigDecimal cValue = numerator3.divide(denominator, 5, RoundingMode.HALF_UP);
	 BigDecimal dValue = numerator4.divide(denominator, 5, RoundingMode.HALF_UP);
	 
	 lastEquationCache = new Equation(new double[]
	 {
	 aValue.doubleValue(), bValue.doubleValue(),
	 cValue.doubleValue(), dValue.doubleValue()
	 });
	 lastEquationVals = points.size();
	 
	 return lastEquationCache;
	 }*/

	 /*public BigDecimal findDeterminant(BigDecimal[] s)
	 {
	 BigDecimal a = s[0], b = s[1], c = s[2], d = s[3], e = s[4], f = s[5];
	 BigDecimal g = s[6], h = s[7], i = s[8], j = s[9], k = s[10], l = s[11];
	 BigDecimal m = s[12], n = s[13], o = s[14], p = s[15];
	 
	 BigDecimal ad1 = a.multiply(f.multiply((k.multiply(p)).subtract(l.multiply(o))).subtract(g.multiply((j.multiply(p)).subtract(l.multiply(n)))).add(h.multiply((j.multiply(o)).subtract(k.multiply(n)))));
	 BigDecimal ad2 = b.multiply(e.multiply((k.multiply(p)).subtract(l.multiply(o))).subtract(g.multiply((i.multiply(p)).subtract(l.multiply(m)))).add(h.multiply((i.multiply(o)).subtract(k.multiply(m)))));
	 BigDecimal ad3 = c.multiply(e.multiply((j.multiply(p)).subtract(l.multiply(n))).subtract(f.multiply((i.multiply(p)).subtract(l.multiply(m)))).add(h.multiply((i.multiply(n)).subtract(j.multiply(m)))));
	 BigDecimal ad4 = d.multiply(e.multiply((j.multiply(o)).subtract(k.multiply(n))).subtract(f.multiply((i.multiply(o)).subtract(k.multiply(m)))).add(g.multiply((i.multiply(n)).subtract(j.multiply(m)))));
	 
	 // a (f (k p - l o) - g (j p - l n) + h (j o - k n)) -
	 // b (e (k p - l o) - g (i p - l m) + h (i o - k m)) +
	 // c (e (j p - l n) - f (i p - l m) + h (i n - j m)) -
	 // d (e (j o - k n) - f (i o - k m) + g (i n - j m))
	 return ((ad1.subtract(ad2)).add(ad3)).subtract(ad4);
	 
	 }*/
	 public BigDecimal determinant(BigDecimal[][] mat)
	 {

		  if (mat.length == 1)
		  {
			   return mat[0][0];
		  }

		  if (mat.length == 2)
		  {
			   return mat[0][0].multiply(mat[1][1]).subtract(mat[0][1].multiply(mat[1][0]));
		  }

		  BigDecimal result = BigDecimal.ZERO;


		  for (int i = 0; i < mat[0].length; i++)
		  {
			   BigDecimal temp[][] = new BigDecimal[mat.length - 1][mat[0].length - 1];

			   for (int j = 1; j < mat.length; j++)
			   {
				    System.arraycopy(mat[j], 0, temp[j - 1], 0, i);
				    System.arraycopy(mat[j], i + 1, temp[j - 1], i, mat[0].length - i - 1);
			   }

			   result = result.add(mat[0][i].multiply(BigDecimal.valueOf(Math.pow(-1, i))).multiply(determinant(temp)));
		  }

		  return result;

	 }
}
