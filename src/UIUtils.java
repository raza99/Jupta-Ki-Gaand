import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ahsan
 */
public class UIUtils
{

	 JFrame parent;

	 public UIUtils(JFrame jf)
	 {
		  this.parent = jf;
	 }

	 public int getRadiusVal(int currentVal)
	 {
		  Integer ret=  showJSPinner (3, 12, currentVal, "Select the radius");

		  if (ret == null)
			  return currentVal;
		  
		  return ret;
	 }
	 
	 private Integer showJSPinner (int min, int max, int current, String message)
	 {
		 JSpinner valueSpinner = new JSpinner(new SpinnerNumberModel(current, min, max, 1));

		  ((JSpinner.DefaultEditor) valueSpinner.getEditor()).getTextField().setEditable(false);
		  
		  
		  JPanel jp = new JPanel ();
		  jp.setLayout(new BoxLayout (jp, BoxLayout.X_AXIS));
		  jp.add(Box.createHorizontalStrut(12));
		  jp.add(valueSpinner);
		  jp.add(Box.createHorizontalStrut(50));
		  
		  final JOptionPane pane = new JOptionPane( new Object [] {message, Box.createVerticalStrut(5),jp}, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_OPTION, null, new String [] {"OK"});

		  final JDialog mainDialog = pane.createDialog (parent, message);


		  mainDialog.setPreferredSize( new Dimension(180, mainDialog.getPreferredSize().height));
		  mainDialog.pack();
		  mainDialog.setVisible(true);

		  if (pane.getValue() == null)
			   return null;

		  if (pane.getValue().equals("OK"))
		  {
			   return Integer.parseInt(valueSpinner.getValue().toString());
		  }
		  else
		  {
			   return null;
		  }
	 }
	 
	 public int getCurve(int currentDegree, int maxDegree)
	 {
		 Object valC = JOptionPane.showInputDialog(parent, "Select a curve", "Select a curve", JOptionPane.QUESTION_MESSAGE, null, CurveEngine.CurveType.valuesNew(), CurveEngine.CurveType.valuesNew()[0]);
		 
		 if (valC == null)
		 {
			  return -1;
		 }
		 
		 if (valC == CurveEngine.CurveType.OTHER)
		 {
			 Integer selectedDegree = showJSPinner (1, maxDegree, currentDegree , "Select the degree");
			 
			 if (selectedDegree == null)
				 return -1;
			 
			 return selectedDegree;
			// CurveEngine.CurveType.OTHER.setDegree(selectedDegree);
			 
		 }
		 
		 return ((CurveEngine.CurveType) valC).getDegree();

	 }
}
