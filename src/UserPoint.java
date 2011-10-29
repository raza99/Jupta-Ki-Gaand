import java.awt.Color;
import java.awt.geom.Point2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ahsan
 */
public class UserPoint extends Point2D.Double
{

	 public Color color;

	 public double getX()
	 {
		  return x;
	 }

	 public void setX(double x)
	 {
		  this.x = x;
	 }

	 public double getY()
	 {
		  return y;
	 }

	 public void setY(double y)
	 {
		  this.y = y;
	 }

	 public Color getColor()
	 {
		  return color;
	 }

	 public void setColor(Color c)
	 {
		  this.color = c;
	 }

	 public UserPoint(double x, double y)
	 {		  
		  this (x, y, Color.black);
	 }

	 public UserPoint(double x, double y, Color c)
	 {
		  super (x,y);
		  this.color = c;
	 }

	 public boolean equals(Object o)
	 {
		  if (!(o instanceof UserPoint))
		  {
			   return false;
		  }

		  UserPoint p = (UserPoint) o;

		  return x == p.x && y == p.y;

	 }

	 public String toString()
	 {
		  return "(" + x + ", " + y + ")";
	 }

	
}
