
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ahsan
 */
public class UserPoints implements Iterable<UserPoint>
{

	private ArrayList<UserPoint> points = new ArrayList<UserPoint>();
	private double minX, minY, maxX, maxY;
	private boolean isPlottable = true;

	public double getMaxX() throws NoUserPointException
	{
		if (points.isEmpty())
		{
			throw new NoUserPointException("No points currently exist.");
		}

		return maxX;
	}

	public double getMaxY() throws NoUserPointException
	{
		if (points.isEmpty())
		{
			throw new NoUserPointException("No points currently exist.");
		}

		return maxY;
	}

	public double getMinX() throws NoUserPointException
	{
		if (points.isEmpty())
		{
			throw new NoUserPointException("No points currently exist.");
		}

		return minX;
	}

	public double getMinY() throws NoUserPointException
	{
		if (points.isEmpty())
		{
			throw new NoUserPointException("No points currently exist.");
		}

		return minY;
	}

	public ArrayList<UserPoint> getPoints()
	{
		return points;
	}

	public void addPoint(UserPoint p)
	{
		if (!points.contains(p))
		{
			if (points.isEmpty())
			{
				minX = p.x < 0D ? p.x : 0D;
				minY = p.y < 0D ? p.y : 0D;
				maxX = p.x > 0D ? p.x : 0D;
				maxY = p.y > 0D ? p.y : 0D;

				if (p.x == 0)
				{
					minX = -1;
					maxX = 1;
				}

				if (p.y == 0)
				{
					minY = -1;
					maxY = 1;
				}
			}
			else
			{
				if (p.x < minX)
				{
					minX = p.x;
				}

				if (p.y < minY)
				{
					minY = p.y;
				}

				if (p.x > maxX)
				{
					maxX = p.x;
				}

				if (p.y > maxY)
				{
					maxY = p.y;
				}

			}

			if (isPlottable)
			{
				Set<Double> checkSet = new HashSet<Double>();

				for (UserPoint point : this)
				{
					if (!checkSet.add(point.x))
					{
						isPlottable = false;
					}
				}
			}

			points.add(p);

		}
	}

	public boolean isEmpty()
	{
		return points.isEmpty();
	}

	public void addPoint(double x, double y, Color c)
	{
		addPoint(new UserPoint(x, y, c));
	}

	public UserPoint getPoint(int index)
	{
		return points.get(index);
	}

	public void addPoint(String str, Color c)
	{
		// System.out.println (str);
		// str = str.replace(",", " ");
		String inputSeperated[] = str.split("(,(\\ )*)|((\\ )+)");


		if (inputSeperated.length < 2)
		{
			throw new IllegalArgumentException("You must enter atleast 2 numbers");
		}
		try
		{
			// System.out.println (inputSeperated[0] + " " + inputSeperated[1] + " " +inputSeperated.length);

			addPoint(Double.parseDouble(inputSeperated[0]), Double.parseDouble(inputSeperated[1]), c);
		}
		catch (NumberFormatException nfe)
		{
			throw new IllegalArgumentException("Input must be an integer or real number");
		}

	}

	public int size()
	{
		return points.size();
	}

	public String toString()
	{
		return points.toString();
	}

	public Iterator<UserPoint> iterator()
	{
		return points.iterator();
	}

	public UserPoint[] toArray()
	{
		return points.toArray(new UserPoint[0]);
	}
	
	public boolean isPlottable()
	{
		return isPlottable;
	}
}
