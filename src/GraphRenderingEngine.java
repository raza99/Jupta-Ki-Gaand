
import java.awt.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ahraza
 */
public class GraphRenderingEngine
{

	private UserPoints pointsList;
	private int pointRadius, pointDiameter;
	private GraphProperties properties;
	private CurveEngine lc;

	public GraphRenderingEngine(UserPoints p, int circleRadius)
	{
		this.pointsList = p;
		lc = new CurveEngine(pointsList);
		properties = new GraphProperties(p);
		setPointRadius(circleRadius);
	}

	public void setPointRadius(int radius)
	{
		this.pointRadius = radius;
		this.pointDiameter = radius * 2;
	}

	public int getPointRadius()
	{
		return pointRadius;
	}
	
	public void setDegree (int degree)
	{
		lc.setCurve(degree);
	}
	
	public int getDegree ()
	{
		return lc.getDegree();
	}

	public void renderImage(int width, int height, Graphics2D g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);

		if (pointsList.isEmpty())
		{
			return;
		}
		try
		{
			drawGrid(width, height, g);
		}
		catch (NoUserPointException ex)
		{
			ex.printStackTrace();
		}
	}

	public void drawGrid(int imageWidth, int imageHeight, Graphics2D g) throws NoUserPointException
	{
		String valueWithMaxYWidth;
		FontMetrics panelFontMetrics = g.getFontMetrics();
		if (((int) pointsList.getMaxY() + "").length() > ((int) pointsList.getMinY() + "").length())
		{
			valueWithMaxYWidth = (int) pointsList.getMaxY() + "e";
		}
		else
		{
			valueWithMaxYWidth = (int) pointsList.getMinY() + "e";
		}

		//int maxStringYWidthPixels = panelFontMetrics.stringWidth(valueWithMaxYWidth);
		properties.numPixelInsetsLeft = panelFontMetrics.stringWidth(valueWithMaxYWidth) - properties.numPixelsInsets + 2;

		if (valueWithMaxYWidth.length() == 3)
		{
			properties.numPixelInsetsLeft += 5;
		}


		int maxStringXWidthPixels = panelFontMetrics.stringWidth((int) pointsList.getMaxX() + "e");
		properties.numPixelsInsetsRight = (maxStringXWidthPixels / 2) - properties.numPixelsInsets;

		int numPixelsInsetsTop = properties.numPixelInsetsTopOrig;
		if (numPixelsInsetsTop + (pointRadius - 5) > numPixelsInsetsTop)
		{
			numPixelsInsetsTop = properties.numPixelInsetsTopOrig + (pointRadius - 5);
			properties.numPixelInsetsTopCurrent = properties.numPixelInsetsTopOrig + (pointRadius - 5);


			properties.numPixelInsetsLeft += (pointRadius - 5);


			properties.numPixelsInsetsRight += (pointRadius - 5);
		}

		properties.refresh(imageWidth, imageHeight);


		while (properties.gridWidth < 52)
		{
			properties.adjustToNearestValueX += properties.adjustToNearestValue;
			properties.refreshX(imageWidth, imageHeight);
		}

		while (properties.gridWidth > 65 && properties.adjustToNearestValueX > properties.adjustToNearestValue)
		{
			properties.adjustToNearestValueX -= properties.adjustToNearestValue;
			properties.refreshX(imageWidth, imageHeight);

		}

		while (properties.gridHeight < 52)
		{
			properties.adjustToNearestValueY += properties.adjustToNearestValue;
			properties.refreshY(imageWidth, imageHeight);
		}
		while (properties.gridHeight > 65 && properties.adjustToNearestValueY > properties.adjustToNearestValue)
		{
			properties.adjustToNearestValueY -= properties.adjustToNearestValue;
			properties.refreshY(imageWidth, imageHeight);

		}


		g.setColor(Color.black);

		int minX = properties.minX;
		int minY = properties.minY;
		int maxX = properties.maxX;
		int maxY = properties.maxY;

		//  int leftRightInsets = properties.numPixelsInsets + properties.numPixelInsetsLeft;


		if (pointsList.isPlottable())
		{
			if (lc.canStartPlotting())
			{
				g.setStroke(new BasicStroke(1.5F));
				//Equation eq = lc.calculateQuadCurve();
				Equation eq = lc.calculateCurve();


				//double qcVals []  = new double [] {1.0, 0.0, 0.0};
			   /* Line2D origLine = lc.calculateLinearCurve(adjustToNearestValueX);
				
				int xC1 = properties.translateX(origLine.getX1(), leftRightInsets);
				
				int yC1 = properties.translateY(origLine.getY1(), topBottomInsets);
				
				int xC2 = properties.translateX(origLine.getX2(), leftRightInsets);
				
				int yC2 = properties.translateY(origLine.getY2(), topBottomInsets);
				
				g.drawLine(xC1, yC1, xC2, yC2);		*/


				/*   for (int i = minX; i <= maxX; i += 1)
				{
				//  double y1 = getQuadY(i, qcVals);
				//    double y2 = getQuadY(i + 1, qcVals);
				
				//  int xC1 = properties.translateX(i, leftRightInsets);
				/
				int yC1 = properties.translateY(y1, topBottomInsets);
				
				int xC2 = properties.translateX(i + 1, leftRightInsets);
				
				int yC2 = properties.translateY(y2, topBottomInsets);
				
				//  g.drawLine(xC1, yC1, xC2, yC2);
				
				}*/




				if (eq != null)
				{
					int minXT = properties.translateX(minX);


					int maxXt = properties.translateX(maxX);

					for (int i = minXT; i <= maxXt; i += 1)
					{

						double iN1 = properties.inverseTranslateX(i, minXT);
						double iN2 = properties.inverseTranslateX(i + 1, minXT);


						double y1 = eq.solve(iN1);
						double y2 = eq.solve(iN2);

						//	    System.out.println (i + " " + iN1 + " " + y1);

						//   int xC1 = properties.translateX(i, leftRightInsets);

						int yC1 = properties.translateY(y1);

						//    int xC2 = properties.translateX(i + 1, leftRightInsets);

						int yC2 = properties.translateY(y2);

						g.drawLine(i, yC1, i + 1, yC2);


					}
				}
			}
		}

		g.setColor(Color.white);
		g.fillRect(0, 0, imageWidth, properties.numPixelsInsets + numPixelsInsetsTop - 3);
		g.setColor(Color.black);


		int leftRightInsets = properties.numPixelsInsets + properties.numPixelInsetsLeft;
		int topBottomInsets = properties.numPixelsInsets + properties.numPixelInsetsTopCurrent - properties.numPixelsInsetsBottom;

		for (int i = minX; i <= maxX; i += properties.adjustToNearestValueX)
		{
			int xC = properties.translateX((double) i);
			if (i == 0)
			{
				g.setStroke(new BasicStroke(3));
				g.drawLine(xC, topBottomInsets, xC, imageHeight);
				g.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(xC, topBottomInsets, xC, imageHeight);
			}

			g.drawString(i + "", xC - (panelFontMetrics.stringWidth(i + "") / 2), 15);
		}


		for (int i = minY; i <= maxY; i += properties.adjustToNearestValueY)
		{

			int yC = properties.translateY((double) i);
			if (i == 0)
			{

				g.setStroke(new BasicStroke(3));
				g.drawLine(leftRightInsets, yC, imageWidth, yC);
				g.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(leftRightInsets, yC, imageWidth, yC);
			}


			g.drawString(rightAlign(i + "", valueWithMaxYWidth), 6, yC + 6);

		}

		//  Path2D.Float linePath = new Path2D.Float();
		for (UserPoint p : pointsList)
		{
			g.setColor(p.color);
			int xC = properties.translateX(p.x);
			int yC = properties.translateY(p.y);

			g.fillOval(xC - pointRadius, yC - pointRadius, pointDiameter, pointDiameter);
		}


	}

	public String rightAlign(String val, String stringWithMaxWidth)
	{
		while (val.length() < stringWithMaxWidth.length() - 1)
		{
			val = " " + val;
		}

		return val;
	}
}
