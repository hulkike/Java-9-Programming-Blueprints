package com.steeplesoft.photobeans.main.options;

import com.steeplesoft.photobeans.manager.PhotoManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

final class SourceDirectoriesPanel extends javax.swing.JPanel {

    private final SourceDirectoriesOptionsPanelController controller;
    private DefaultListModel<String> model;

    SourceDirectoriesPanel(SourceDirectoriesOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // TODO listen to changes in form fields and call controller.changed()
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sourceList = new javax.swing.JList<>();
        buttonAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SourceDirectoriesPanel.class, "SourceDirectoriesPanel.jLabel1.text")); // NOI18N

        jScrollPane1.setViewportView(sourceList);

        org.openide.awt.Mnemonics.setLocalizedText(buttonAdd, org.openide.util.NbBundle.getMessage(SourceDirectoriesPanel.class, "SourceDirectoriesPanel.buttonAdd.text")); // NOI18N
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(buttonRemove, org.openide.util.NbBundle.getMessage(SourceDirectoriesPanel.class, "SourceDirectoriesPanel.buttonRemove.text")); // NOI18N
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRemove)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
        List<Integer> indexes = IntStream.of(sourceList.getSelectedIndices())
                .boxed().collect(Collectors.toList());
        Collections.sort(indexes);
        Collections.reverse(indexes);
        indexes.forEach(i -> ensureModel().remove(i));
    }//GEN-LAST:event_buttonRemoveActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemove1ActionPerformed
        String lastDir = NbPreferences.forModule(PhotoManager.class).get("lastDir", null);
        JFileChooser chooser = new JFileChooser();
        if (lastDir != null) {
            chooser.setCurrentDirectory(new java.io.File(lastDir));
        }
        chooser.setDialogTitle("Add Source Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                String dir = chooser.getSelectedFile().getCanonicalPath();
                ensureModel().addElement(dir);
                NbPreferences.forModule(PhotoManager.class).put("lastDir", dir);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_buttonRemove1ActionPerformed

    protected void store() {
        Set<String> dirs = new HashSet<>();
        ensureModel();
        for (int i = 0; i < model.getSize(); i++) {
            final String dir = model.getElementAt(i);
            if (dir != null && !dir.isEmpty()) {
                dirs.add(dir);
            }
        }

        if (!dirs.isEmpty()) {
            NbPreferences.forModule(PhotoManager.class).put("sourceDirs", String.join(";", dirs));
        } else {
            NbPreferences.forModule(PhotoManager.class).remove("sourceDirs");
        }
    }

    protected void load() {
        String dirs = NbPreferences.forModule(PhotoManager.class).get("sourceDirs", "");
        if (dirs != null && !dirs.isEmpty()) {
            ensureModel();
            model.clear();
            Set<String> set = new HashSet<>(Arrays.asList(dirs.split(";")));
            set.forEach(i -> model.addElement(i));
        }
    }

    private DefaultListModel<String> ensureModel() {
        if (model == null) {
            model = new DefaultListModel<>();
            sourceList.setModel(model);
        }
        return model;
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> sourceList;
    // End of variables declaration//GEN-END:variables
}