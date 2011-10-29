/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ahsan
 */
public class GraphProperties
{

	 protected int minX, maxX, minY, maxY, xRange, yRange, gridWidth, gridHeight;
	 protected int imageWidthAdjusted, imageHeightAdjusted, numPixelsInsets = 5, numPixelInsetsLeft,  numPixelInsetsTopOrig = 21, numPixelInsetsTopCurrent = numPixelInsetsTopOrig, numPixelsInsetsBottom = 4, numPixelsInsetsRight = 0;
	 protected static final int adjustToNearestValue = 2;
	 protected int adjustToNearestValueX = adjustToNearestValue, adjustToNearestValueY = adjustToNearestValue;
	 private UserPoints pointsList;
	 
	 public GraphProperties(UserPoints pointsList)
	 {
		  this.pointsList = pointsList;
		  
	 

	 }
	 
	 public void refresh(int imageWidth, int imageHeight) throws NoUserPointException
	 {
		  minX = (int) (Math.floor(pointsList.getMinX() / adjustToNearestValueX) * adjustToNearestValueX);
		  maxX = (int) (Math.ceil(pointsList.getMaxX() / adjustToNearestValueX) * adjustToNearestValueX);
		  minY = (int) (Math.floor(pointsList.getMinY() / adjustToNearestValueY) * adjustToNearestValueY);
		  maxY = (int) (Math.ceil(pointsList.getMaxY() / adjustToNearestValueY) * adjustToNearestValueY);

		  xRange = maxX - minX;
		  yRange = maxY - minY;

		  imageWidthAdjusted = imageWidth - (numPixelsInsets << 1) - numPixelInsetsLeft - numPixelsInsetsRight;

		  int churnX = imageWidthAdjusted - (((imageWidthAdjusted / adjustToNearestValueX)) * adjustToNearestValueX);
		  
		  imageHeightAdjusted = imageHeight - (numPixelsInsets << 1) - numPixelInsetsTopCurrent - numPixelsInsetsBottom;

		  int churnY = imageHeightAdjusted - (((imageHeightAdjusted / adjustToNearestValueY)) * adjustToNearestValueY);

		  gridWidth = translateX(minX + adjustToNearestValueX, 0);
		  gridWidth += churnX/((maxX - minX)/adjustToNearestValueX);		  
		  
		  gridHeight = translateY(maxY - adjustToNearestValueY,  0);
		  gridHeight += churnY/((maxY - minY)/adjustToNearestValueY);
	 }
	 
	 
	 public void refreshX(int imageWidth, int imageHeight) throws NoUserPointException
	 {
		  minX = (int)(Math.floor(pointsList.getMinX() / adjustToNearestValueX) * adjustToNearestValueX);
		  maxX = (int)(Math.ceil(pointsList.getMaxX() / adjustToNearestValueX) * adjustToNearestValueX);

		  xRange = maxX - minX;

		  imageWidthAdjusted = imageWidth - (numPixelsInsets << 1) - numPixelInsetsLeft - numPixelsInsetsRight;

		  int churnX = imageWidthAdjusted - (((imageWidthAdjusted / adjustToNearestValueX)) * adjustToNearestValueX);

		  gridWidth = translateX(minX + adjustToNearestValueX, 0);
		  gridWidth += churnX/((maxX - minX)/adjustToNearestValueX);		  
	 }
	 
	 public void refreshY(int imageWidth, int imageHeight) throws NoUserPointException
	 {
		  minY = (int)(Math.floor(pointsList.getMinY() / adjustToNearestValueY) * adjustToNearestValueY);
		  maxY = (int)(Math.ceil(pointsList.getMaxY() / adjustToNearestValueY) * adjustToNearestValueY);
		  
		  yRange = maxY - minY;

		  imageHeightAdjusted = imageHeight - (numPixelsInsets << 1) - numPixelInsetsTopCurrent - numPixelsInsetsBottom;
		  int churnY = imageHeightAdjusted - (((imageHeightAdjusted / adjustToNearestValueY)) * adjustToNearestValueY);
		  
		  gridHeight = translateY(maxY - adjustToNearestValueY,  0);
		  gridHeight += churnY/((maxY - minY)/adjustToNearestValueY);
	 }

	 
	  public  int translateX(double val)
	 {
		  return translateX(val, numPixelsInsets + numPixelInsetsLeft);
		  
	 }
	 public  int translateX(double val, int insetTrans)
	 {
		  if (val == minX)
		  {
			   return insetTrans;
		  }
		  
		  
		  return  ((int) (((double) imageWidthAdjusted) * ((val - ((double)minX)) / (double)xRange))) + insetTrans;

		 // return ((int) ((double) width * ((val - min) / range))) + insetTrans;
	 }
	 

	 public  int translateY(double val)
	 {
		  return translateY(val, numPixelsInsets + numPixelInsetsTopCurrent - numPixelsInsetsBottom);
		  
	 }
	 
	 
	 public int translateY (double val, int insets)
	 {
		  if (val == maxY)
		  {
			   return insets;
		  }
		  
		  return  ((int) (((double) imageHeightAdjusted) * ((((double)maxY) - val) / (double)yRange))) + insets;
	 }
	 
	 public  double inverseTranslateX(double val, int min)
	 {
		  int insetTrans = numPixelsInsets + numPixelInsetsLeft;
		  if (val == min)
		  {
			   return minX;
		  }
		  
		  return   (((double) xRange) * ((val - ((double)insetTrans)) / (double)imageWidthAdjusted)) + ((double)minX);
		  
	 }
	 
	 
}