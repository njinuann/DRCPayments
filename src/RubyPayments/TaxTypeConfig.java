/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.util.StringTokenizer;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author NJINU
 */
public class TaxTypeConfig extends javax.swing.JPanel
{

    /**
     * Creates new form TCPanel
     */
    TXUtility txutil = new TXUtility();
    String Validate, BillerTc, validateTax;
    String AcctAbrv;
    String clicked;
    JDialog taxTypeDialog = null;

    public TaxTypeConfig()
    {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        billerTreeScroller = new javax.swing.JScrollPane();
        TaxTypeTree = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        TaxId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        TaxName = new javax.swing.JTextField();
        taxStatus = new javax.swing.JComboBox();

        TaxTypeTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Tax Type");
        TaxTypeTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        TaxTypeTree.setCellRenderer(new RubyPayments.TRenderer());
        TaxTypeTree.setShowsRootHandles(true);
        TaxTypeTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                TaxTypeTreeValueChanged(evt);
            }
        });
        billerTreeScroller.setViewportView(TaxTypeTree);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tax Types"));

        saveButton.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        saveButton.setText("Save");
        saveButton.setEnabled(false);
        saveButton.setPreferredSize(new java.awt.Dimension(82, 25));
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        TaxId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TaxIdFocusLost(evt);
            }
        });
        TaxId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TaxIdMouseClicked(evt);
            }
        });
        TaxId.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                TaxIdPropertyChange(evt);
            }
        });
        TaxId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TaxIdKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TaxIdKeyTyped(evt);
            }
        });

        jLabel2.setText("Tax Type Code");

        jLabel3.setText("Tax Type Name");

        TaxName.setEditable(false);
        TaxName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TaxNameMouseClicked(evt);
            }
        });
        TaxName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TaxNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TaxNameKeyTyped(evt);
            }
        });

        taxStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "Active", "Closed" }));
        taxStatus.setPreferredSize(new java.awt.Dimension(4, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(TaxId, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(taxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(TaxName))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TaxId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TaxName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(billerTreeScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(billerTreeScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void TaxTypeTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_TaxTypeTreeValueChanged
        // TODO add your handling code here:
        DefaultMutableTreeNode taxNode = (DefaultMutableTreeNode) TaxTypeTree.getLastSelectedPathComponent();
        if (taxNode != null)
        {
            displayValue(taxNode.getUserObject().toString().split("~")[0].trim());
        }

    }//GEN-LAST:event_TaxTypeTreeValueChanged
    private void displayValue(String dispValue)
    {
        DefaultMutableTreeNode selectedTaxNode = (DefaultMutableTreeNode) TaxTypeTree.getLastSelectedPathComponent();
        if (selectedTaxNode != null)
        {
            String selectedTax = selectedTaxNode.getUserObject().toString();
            if (selectedTax.equals("Tax Type"))
            {
                TaxId.setText("");
                TaxId.setEnabled(true);
                TaxId.setEditable(true);
                TaxName.setText("");
                saveButton.setText("Save");

            }
            else
            {
                String TaxTypeId = selectedTax.substring(0, selectedTax.indexOf(" ~ "));
                Object[][] taxData = txutil.queryTaxType(Integer.parseInt(dispValue));
                if (taxData == null)
                {
                    JOptionPane.showMessageDialog(ExtPayMain.uiFrame, " Failed to retrieve account information from database. Please check logs file for details.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    saveButton.setEnabled(false);
                    saveButton.setText("Save");
                }
                else if (taxData.length == 0)
                {
                    JOptionPane.showMessageDialog(ExtPayMain.uiFrame, "Cannot find account information in database. Please consult System Administrator.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    saveButton.setEnabled(false);
                    saveButton.setText("Save");
                }
                else
                {
                    TaxId.setText(dispValue);
                    TaxId.setEditable(false);
                    saveButton.setText("Update");
                    saveButton.setEnabled(true);
                    verifyTaxType(dispValue);
                }
            }
        }
    }

    private void verifyTaxType(String dispValue)
    {
        TaxId.setText("");
        String taxId = dispValue;

        TaxId.setText(taxId);
        Object[][] acctData = txutil.verifyTaxType(Integer.parseInt(taxId));
        if (acctData == null)
        {
            JOptionPane.showMessageDialog(this, "Error verifying  Tax type Record!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (acctData.length == 0)
        {
            JOptionPane.showMessageDialog(this, "Invalid or Tax Type Record!");
        }
        else
        {
            String cleanName = "";
            StringTokenizer tokenizer = new StringTokenizer(acctData[0][0].toString());
            while (tokenizer.hasMoreTokens())
            {
                cleanName += tokenizer.nextToken().trim() + " ";
            }
            TaxId.setText(cleanName.trim());
            setTaxDisplayRec();
        }
    }

    public void showTaxDialog()
    {
        refreshBanksTree();
        if (taxTypeDialog == null)
        {
            taxTypeDialog = new JDialog(ExtPayMain.uiFrame, "Tax Types Config");
            taxTypeDialog.setIconImage(ExtPayMain.uiFrame.getIconImage());

            taxTypeDialog.setContentPane(this);
            taxTypeDialog.pack();

            taxTypeDialog.setResizable(false);
            taxTypeDialog.setLocationRelativeTo(ExtPayMain.uiFrame);
            taxTypeDialog.setVisible(true);
        }
        else
        {
            taxTypeDialog.setVisible(true);
        }
    }

    public void setBillerTree()
    {
        DefaultMutableTreeNode taxRootNode = ((DefaultMutableTreeNode) TaxTypeTree.getModel().getRoot());
        taxRootNode.removeAllChildren();

        for (Object[] taxType : txutil.selectTaxTypes())
        {
            taxRootNode.add(new DefaultMutableTreeNode(taxType[0] + "~" + taxType[1]));
        }
        updateBillerTree();
        displayValue(BRController.PrimaryCurrency);
    }

    public void refreshBanksTree()
    {
        DefaultMutableTreeNode taxIdNode = ((DefaultMutableTreeNode) TaxTypeTree.getModel().getRoot());
        DefaultMutableTreeNode newBillerAccount = new DefaultMutableTreeNode("Biller Tax Types");

        taxIdNode.removeAllChildren();
        taxIdNode.add(newBillerAccount);

        Object[][] existingAccounts = txutil.selectTaxTypes();
        if (existingAccounts == null)
        {
            JOptionPane.showMessageDialog(this, "Error retrieving registered Records!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            for (Object[] bank : existingAccounts)
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(bank[0] + " ~ " + bank[1]);
                taxIdNode.add(node);
            }
        }
        billerTreeScroller.getHorizontalScrollBar().setValue(0);
        billerTreeScroller.getVerticalScrollBar().setValue(0);
        TaxTypeTree.updateUI();
    }

    public void updateBillerTree()
    {
        billerTreeScroller.getHorizontalScrollBar().setValue(0);
        billerTreeScroller.getVerticalScrollBar().setValue(0);
        TaxTypeTree.updateUI();
    }

    private void TaxIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TaxIdMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_TaxIdMouseClicked

    private void TaxIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TaxIdKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TaxIdKeyPressed

    private void TaxIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TaxIdKeyTyped
        // TODO add your handling code here:
        saveButton.setEnabled(true);
    }//GEN-LAST:event_TaxIdKeyTyped

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:     
        if (saveButton.getText().equals("Save"))
        {
            if (txutil.insertTaxTypeInfo(Integer.parseInt(TaxId.getText())))
            {
                JOptionPane.showMessageDialog(this, "Record Saved successfuly", "Sucess", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Error Saving Record!", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            if (txutil.updateTaxTypeInfo(Integer.parseInt(TaxId.getText()), TaxName.getText(), (taxStatus.getSelectedItem().toString().equalsIgnoreCase("Active") ? "A" : "C")))
            {
                JOptionPane.showMessageDialog(this, "Record Saved successfuly", "Sucess", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Error Saving Record!", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void TaxNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TaxNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TaxNameMouseClicked

    private void TaxNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TaxNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TaxNameKeyPressed

    private void TaxNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TaxNameKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TaxNameKeyTyped

    private void TaxIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TaxIdFocusLost
        // TODO add your handling code here:
        if (!TaxId.getText().isEmpty() || !TaxId.getText().equals(""))
        {
            TaxName.setText(txutil.queryBillerName(TaxId.getText()));
        }
    }//GEN-LAST:event_TaxIdFocusLost

    private void TaxIdPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_TaxIdPropertyChange
        // TODO add your handling code here:
        setTaxDisplayRec();
    }//GEN-LAST:event_TaxIdPropertyChange

    private void setTaxDisplayRec()
    {
        if (!TaxId.getText().isEmpty() || !TaxId.getText().equals(""))
        {
            //TaxName.setText(String.valueOf(txutil.queryTaxType(Integer.parseInt(TaxId.getText()))[0][0]));
            Object[][] taxTypeDetail = txutil.selectTaxTypesId(Integer.parseInt(TaxId.getText()));
            for (Object[] record : taxTypeDetail)
            {
                TaxName.setText(String.valueOf(record[1]));
                taxStatus.setSelectedItem(String.valueOf(record[2]));
                taxStatus.requestFocusInWindow();
            }

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TaxId;
    private javax.swing.JTextField TaxName;
    private javax.swing.JTree TaxTypeTree;
    private javax.swing.JScrollPane billerTreeScroller;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox taxStatus;
    // End of variables declaration//GEN-END:variables
}
