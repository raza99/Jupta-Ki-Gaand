
import java.awt.Color;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UserLegend.java
 *
 * Created on Oct 23, 2011, 8:44:09 PM
 */

/**
 *
 * @author Wale
 */
public class UserLegend extends javax.swing.JDialog {


    private ArrayList<Color> colorList;

    /** Creates new form UserLegend */
    public UserLegend(java.awt.Frame parent, boolean modal)
    {
            super(parent, modal);

            initComponents();
            colorList = new ArrayList<Color>();

    }
    
    public void add(Color selectedColor)
    {

            if(!colorList.contains(selectedColor))
            {
                    colorList.add(selectedColor);
            }
    }

    public ArrayList<Color> getAllColors()
    {
            return colorList;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}