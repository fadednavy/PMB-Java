/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package global.view;
import javax.swing.JOptionPane;
import global.koneksi.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Furie
 */
public class FrameMHS extends javax.swing.JFrame {
Connection connection;
DefaultTableModel model;

    /** Creates new form FrameMHS */
    public FrameMHS() {
        initComponents();
        connection = koneksi.connection();
        getDataTable1();
        getCmbProdi();
        getCmbKonsen();
        tNim.requestFocus();
        refresh();
    }
private void getDataTable1(){
    model = (DefaultTableModel) table1.getModel();
    try{
        Statement stat = connection.createStatement();
        String sql = "select * from mhs,prodi,konsentrasi " + "where mhs.id_prodi=prodi.id_prodi and mhs.id_konsen=konsentrasi.id_konsen";
        ResultSet res = stat.executeQuery(sql);
        while(res.next()){
            Object[ ] obj = new Object[6];
            obj[0] = res.getString("nim");
            obj[1] = res.getString("nama");
            obj[2] = res.getString("alamat");
            obj[3] = res.getString("no_telp");
            obj[4] = res.getString("nama_prodi");
            obj[5] = res.getString("nama_konsen");
            model.addRow(obj);
        }
    }catch(SQLException err){
        err.printStackTrace();
    }
}
private void getCmbProdi(){
    cmbProdi.removeAllItems();
    try{
        Statement stat = connection.createStatement();
        String sql = "select * from prodi";
        ResultSet res = stat.executeQuery(sql);
        while(res.next()){
            cmbProdi.addItem(res.getString("nama_prodi"));
        }
    }catch(SQLException err){
        err.printStackTrace();
    }
}
private void getCmbKonsen(){
    cmbKonsen.removeAllItems();
    try{
        Statement stat = connection.createStatement();
        String sql = "select * from konsentrasi";
        ResultSet res = stat.executeQuery(sql);
        while(res.next()){
            cmbKonsen.addItem(res.getString("nama_konsen"));
        }
    }catch(SQLException err){
        err.printStackTrace();
    }
}
private void refresh(){
    model = (DefaultTableModel) table1.getModel();
    model.setRowCount(0);
    getDataTable1();
}

private void reset(){
    tNim.setText("");
    tNama.setText("");
    tAlamat.setText("");
    tNotelp.setText("");
    tNim.setEditable(true);
    bSimpan.setEnabled(true);
}
private void insert(){
    PreparedStatement statement = null;
    String sql = "insert into mhs (nim,nama,alamat,no_telp,id_prodi,id_konsen) values(?,?,?,?,?,?);";
    try {
        statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, tNim.getText());
        statement.setString(2, tNama.getText());
        statement.setString(3, tAlamat.getText());
        statement.setString(4, tNotelp.getText());
        statement.setInt(5, getIDProdi(cmbProdi.getSelectedItem().toString()));
        statement.setInt(6, getIDKonsen(cmbKonsen.getSelectedItem().toString()));
        statement.executeUpdate();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }finally {
        try {
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
private void update(){
    PreparedStatement statement = null;
    String sql = "update mhs set nama=?,alamat=?,no_telp=?,id_prodi=?,id_konsen=? where nim=?";
    try {
        statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, tNama.getText());
        statement.setString(2, tAlamat.getText());
        statement.setString(3, tNotelp.getText());
        statement.setInt(4, getIDProdi(cmbProdi.getSelectedItem().toString()));
        statement.setInt(5, getIDKonsen(cmbKonsen.getSelectedItem().toString()));
        statement.setString(6, tNim.getText());
        statement.executeUpdate();
    } catch (SQLException ex) {
        ex.printStackTrace();
    } finally {
        try {
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
private void delete(){
    PreparedStatement statement = null;
    String sql = "delete from mhs where nim=?";
    try {
        statement = connection.prepareStatement(sql);
        statement.setString(1, tNim.getText());
        statement.executeUpdate();
    } catch (SQLException ex) {
        ex.printStackTrace();
    } finally {
        try {
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
private void search(){
    model = (DefaultTableModel) table1.getModel();
    PreparedStatement statement = null;
    try {
        String sql = "select * from mhs, prodi,konsentrasi where " + "mhs.id_prodi=prodi.id_prodi and mhs.id_konsen=konsentrasi.id_konsen and nama like ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + tCari.getText() + "%");
        ResultSet res = statement.executeQuery();
        while(res.next()){
            Object[ ] obj = new Object[6];
            obj[0] = res.getString("nim");
            obj[1] = res.getString("nama");
            obj[2] = res.getString("alamat");
            obj[3] = res.getString("no_telp");
            obj[4] = res.getString("nama_prodi");
            obj[5] = res.getString("nama_konsen");
            model.addRow(obj);
        }
    }catch(SQLException err){
        err.printStackTrace();
    }
}
private int getIDProdi (String nama_prodi){
    int id = 0;
    try {
        PreparedStatement st = connection.prepareStatement("select id_prodi from prodi where nama_prodi=?",Statement.RETURN_GENERATED_KEYS);
        st.setString(1, nama_prodi);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            id = rs.getInt("id_prodi");
        }
        return id;
        } catch (SQLException ex) {
            Logger.getLogger(FrameMHS.class.getName()).log(Level.SEVERE, null, ex);
    }
    return id;
}
private int getIDKonsen(String namakonsen){
    int id = 0;
    try {
        PreparedStatement st = connection.prepareStatement("select id_konsen from konsentrasi where nama_konsen=?", Statement.RETURN_GENERATED_KEYS);
        st.setString(1, namakonsen);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            id = rs.getInt("id_konsen");
        }
        return id;
    } catch (SQLException ex) {
        Logger.getLogger(FrameMHS.class.getName()).log(Level.SEVERE, null,ex);
    }
    return id;
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tNim = new javax.swing.JTextField();
        tNama = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tAlamat = new javax.swing.JTextArea();
        tNotelp = new javax.swing.JTextField();
        cmbProdi = new javax.swing.JComboBox<>();
        cmbKonsen = new javax.swing.JComboBox<>();
        tCari = new javax.swing.JTextField();
        bCari = new javax.swing.JButton();
        bRefresh = new javax.swing.JButton();
        bSimpan = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bReset = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Data Mahasiswa");

        jLabel2.setText("NIM");

        jLabel3.setText("Nama");

        jLabel4.setText("Alamat");

        jLabel5.setText("No. Telephone");

        jLabel6.setText("Program Studi");

        jLabel7.setText("Konsentrasi");

        jLabel8.setText("Cari Data");

        tAlamat.setColumns(20);
        tAlamat.setRows(5);
        jScrollPane1.setViewportView(tAlamat);

        cmbProdi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbKonsen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        bCari.setText("Cari");
        bCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCariActionPerformed(evt);
            }
        });

        bRefresh.setText("Refresh");
        bRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRefreshActionPerformed(evt);
            }
        });

        bSimpan.setText("Simpan");
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });

        bUbah.setText("Ubah");
        bUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahActionPerformed(evt);
            }
        });

        bHapus.setText("Hapus");
        bHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusActionPerformed(evt);
            }
        });

        bReset.setText("Reset");
        bReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bResetActionPerformed(evt);
            }
        });

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "NIM", "Nama", "Alamat", "No Telp", "ID Prodi", "ID Konsen"
            }
        ));
        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(table1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(223, 223, 223)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(209, 209, 209))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel7)
                                        .addComponent(tNama)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                                        .addComponent(tNotelp)
                                        .addComponent(cmbKonsen, 0, 194, Short.MAX_VALUE)
                                        .addComponent(cmbProdi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(40, 40, 40)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tNim, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(bSimpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                            .addComponent(bHapus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(bUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bReset, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bCari, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bRefresh)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tNim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bCari)
                    .addComponent(bRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tNotelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbProdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbKonsen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSimpan)
                            .addComponent(bUbah))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bHapus)
                            .addComponent(bReset))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        if(!tNim.getText().trim().isEmpty() && !tNama.getText().trim().isEmpty() && !tAlamat.getText().trim().isEmpty() && !tNotelp.getText().trim().isEmpty()){
            insert();
            refresh();
            reset();
            JOptionPane.showMessageDialog(this, "Mahasiswa berhasil ditambahkan","Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(this, "Lengkapi form terlebih dahulu!","Notifikasi", JOptionPane.WARNING_MESSAGE);
            
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        if(!tNim.getText().trim().isEmpty()){
            int alert = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus mahasiswa ini ?","Notifikasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(alert == JOptionPane.YES_OPTION){
                delete();
                refresh();
                reset();
                JOptionPane.showMessageDialog(this, "Mahasiswaq berhasil dihapus","Notifikasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!","Notifikasi", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_bHapusActionPerformed
    }
    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        if(!tNim.getText().trim().isEmpty()){
            if(!tNim.getText().trim().isEmpty() && !tNama.getText().trim().isEmpty() && !tAlamat.getText().trim().isEmpty() && !tNotelp.getText().trim().isEmpty()){
                update();
                refresh();
                reset();
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil diubah","Notifikasi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lengkapi form terlebih dahulu!","Notifikasi", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data terlegih dahulu!","Notifikasi", JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_bUbahActionPerformed

    private void bResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bResetActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_bResetActionPerformed

    private void bCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariActionPerformed
        // TODO add your handling code here:
        if(!tCari.getText().trim().isEmpty()) {
            model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            search();
        } else {
            JOptionPane.showMessageDialog(this, "Masukan nama mahasiswa yang ingin dicari!","Notifikasi", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bCariActionPerformed

    private void table1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table1MouseClicked
        // TODO add your handling code here:
        tNim.setText(table1.getModel().getValueAt(table1.getSelectedRow(), 0).toString());
        tNama.setText(table1.getModel().getValueAt(table1.getSelectedRow(), 1).toString());
        tAlamat.setText(table1.getModel().getValueAt(table1.getSelectedRow(), 2).toString());
        tNotelp.setText(table1.getModel().getValueAt(table1.getSelectedRow(), 3).toString());
        cmbProdi.setSelectedItem(table1.getModel().getValueAt(table1.getSelectedRow(), 4).toString());
        cmbKonsen.setSelectedItem(table1.getModel().getValueAt(table1.getSelectedRow(), 5).toString());
        tNim.setEditable(false);
        bSimpan.setEnabled(false);
    }//GEN-LAST:event_table1MouseClicked

    private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRefreshActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_bRefreshActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameMHS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMHS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMHS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMHS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameMHS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCari;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bRefresh;
    private javax.swing.JButton bReset;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox<String> cmbKonsen;
    private javax.swing.JComboBox<String> cmbProdi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea tAlamat;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tNama;
    private javax.swing.JTextField tNim;
    private javax.swing.JTextField tNotelp;
    private javax.swing.JTable table1;
    // End of variables declaration//GEN-END:variables

}
