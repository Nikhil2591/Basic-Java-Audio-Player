/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nikhil
 */
package NSPlayer;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.*;
import java.awt.Window;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.DefaultListModel;
import java.io.File;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.swing.ListModel;
import javax.swing.JList;
import java.util.Arrays;
import javazoom.jlgui.basicplayer.*;
import sun.audio.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class JFramePlayer extends javax.swing.JFrame {

    private JFrame me;
    private DefaultListModel listModel;
    private Player player;
    private Playlist playlist;
    private BasicController control;
    static BasicPlayer bpl;

    static public boolean sliderEvent = false;

    private JFramePlayer() {
        super("NS Player");

        listModel = new DefaultListModel();
        listTrack = new JList(listModel);
        listTrack.setModel(listModel);

        initComponents();

        bpl = new BasicPlayer();

        playlist = new Playlist();
        player = new Player(this, playlist);

    }

    PlaylistItem getPlaylistItem() {
        return ((PlaylistItem) listModel.getElementAt(listTrack.getSelectedIndex()));
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    /*
     * Playback audio track when selects 'play' button
     */
    private void playTrack() {
        player.stop();
        int plposn = listTrack.getSelectedIndex();
        playlist.setPosition(plposn);
        player.play((PlaylistItem) listModel.getElementAt(plposn));
    }
    /*
     * Playback the next audio track in the playlist when the user selects 'next' button
     */
    private void nextTrack() {
        player.stop();
        int plposn = playlist.incrementPosition();
        listTrack.setSelectedIndex(plposn);
        listTrack.ensureIndexIsVisible(plposn);
        player.play((PlaylistItem) listModel.getElementAt(plposn));
    }
    /*
     * playback the previous audio track in the playlist when the user selects 'previous' button
     */
    private void previousTrack() {
        player.stop();
        int plposn = playlist.decrementPosition();
        listTrack.setSelectedIndex(plposn);
        listTrack.ensureIndexIsVisible(plposn);
        player.play((PlaylistItem) listModel.getElementAt(plposn));

    }
    /*
     * pauses current audio track from playing in the playlist when the user selects 'pause' button
     */
    private void pauseTrack() {
        player.pause();
        int plposn = listTrack.getSelectedIndex();
        playlist.setPosition(plposn);
    }
    /*
     * resumes playback of current audio track from a paused state in the playlist when the user selects 'resume' button
     */
    private void resumeTrack() {
        player.resume();
        int plposn = listTrack.getSelectedIndex();
        playlist.setPosition(plposn);

    }

    private void disableButtons() {
        Play.setEnabled(false);
        Stop.setEnabled(false);
        Next.setEnabled(false);
        Previous.setEnabled(false);
        Pause.setEnabled(false);
        Resume.setEnabled(false);
    }

    private void enableButtons() {
        Play.setEnabled(true);
        Stop.setEnabled(true);
        Next.setEnabled(true);
        Previous.setEnabled(true);
        Pause.setEnabled(true);
        Resume.setEnabled(true);
    }

   public void ensureIndexVisible(int index) {
        listTrack.ensureIndexIsVisible(index);
    }

   public void setListIndex(int position) {

        listTrack.setSelectedIndex(position);
    }

   public void setStatusBarText(String text) {
        statusLabel.setText(text);
    }
    /*
     * clears the playlist of any audio tracks
     */
    private void clearList() {
        player.stop();
        listModel.clear();

    }
    /*
     * removes selected audio track from the playlist
     */
    private void removeFile() {

        listModel.removeElementAt(listTrack.getSelectedIndex());
        playlist.init(listModel.size());
    }

    private static final String PREF_FOLDER = "prefFolder";
    
    /*
     * open an audio file when user selects 'open file' in the menu bar.
     */
    public void openFile() {

        JFileChooser openFile;
        openFile = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "mp3, flac, ogg, wav, aiff, au & mp2 files", "mp3", "flac", "ogg", "wav", "aiff", "au", "mp2");
        openFile.setFileFilter(filter);
        openFile.setDialogTitle("Open a file");
        int returnVal = openFile.showDialog(me, null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = openFile.getSelectedFile();
            openFile.getSelectedFile().getAbsolutePath();

            listModel.clear();
            listModel.addElement(new PlaylistItem(file));
            listTrack.setSelectedIndex(0);
            playlist.init(listModel.size());

            //  stop anything already playing
            player.stop();
            player.play((PlaylistItem) listModel.getElementAt(0));

            enableButtons();
        }
    }
    
     /*
     * Add an audio file to an existing playlist
     */

    public void AddFile() {

        JFileChooser addFile;
        addFile = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "mp3, flac, ogg, wav, aiff, au & mp2 files", "mp3", "flac", "ogg", "wav", "aiff", "au", "mp2");
        addFile.setFileFilter(filter);
        addFile.setDialogTitle("Add a file to playlist");
        int returnVal = addFile.showDialog(me, null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = addFile.getSelectedFile();
            addFile.getSelectedFile().getAbsolutePath();

            listModel.addElement(new PlaylistItem(file));
            listTrack.setSelectedIndex(0);
            playlist.init(listModel.size());

        }
    }
    
     /*
     * open a folder containing audio files when user selects 'open folder' in the menu bar.
     */

    public void openFolder() {
        String dir = Preferences.userNodeForPackage(getClass()).get(PREF_FOLDER, System.getProperty("user.home"));
        JFileChooser openFolder;
        openFolder = new JFileChooser(dir);
        openFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        openFolder.setDialogTitle("Select target directory");
        int returnVal = openFolder.showDialog(me, null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dir = openFolder.getSelectedFile().getAbsolutePath();
            try {
            } catch (Exception ae) {
            }
            File fdir = new File(dir);
            listModel.clear();
            addToPlayList(fdir);
            if (listModel.size() == 0) {
                disableButtons();
                player.stop();
                return;
            }
            playlist.init(listModel.size());
            int plposn = playlist.getPosition();
            listTrack.setSelectedIndex(plposn);
            listTrack.ensureIndexIsVisible(plposn);

            // stop audio files already playing
            player.stop();
            int trackNumber = plposn + 1;
            setStatusBarText("Playing " + trackNumber + "/" + playlist.getPlaylistSize());
            player.play((PlaylistItem) listModel.getElementAt(plposn));
            enableButtons();
        }

    }
    
    public void addToPlayList(File dir) {
        File[] files;
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File file) {
                String fname = file.getName().toLowerCase();
                if (fname.endsWith(".mp3") || fname.endsWith(".flac") || fname.endsWith(".ogg") || fname.endsWith(".wav") || fname.endsWith(".au") || fname.endsWith(".aiff") || fname.endsWith(".mp2")) {
                    return true;
                } else if (file.isDirectory()) {
                    return true;
                }
                return false;
            }

            public boolean accept(File dir, String name) {
                String fname = name.toLowerCase();
                File file = new File(dir, name);
                if (fname.endsWith(".mp3") || fname.endsWith(".flac") || fname.endsWith(".ogg") || fname.endsWith(".wav") || fname.endsWith(".au") || fname.endsWith(".aiff") || fname.endsWith(".mp2")) {
                    return true;
                } else if (file.isDirectory()) {
                    return true;
                }
                return false;
            }
        };
        files = dir.listFiles(filter);
        if (files.length == 0) {
            return;
        }
        Arrays.sort(files);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addToPlayList(files[i]);
            } else {
                listModel.addElement(new PlaylistItem(files[i]));
            }
        }
    }

    /**
     * Creates new form JFramePlayer
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Play = new javax.swing.JButton();
        Stop = new javax.swing.JButton();
        Pause = new javax.swing.JButton();
        Previous = new javax.swing.JButton();
        Next = new javax.swing.JButton();
        ShuffleCB = new javax.swing.JCheckBox();
        PlaylistLabel = new java.awt.Label();
        statusLabel = new javax.swing.JLabel();
        Add = new javax.swing.JButton();
        Remove = new javax.swing.JButton();
        Resume = new javax.swing.JButton();
        clearList = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listTrack = new javax.swing.JList();
        repeatCB = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        OpenFile = new javax.swing.JMenuItem();
        OpenDir = new javax.swing.JMenuItem();
        Exit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        About = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(java.awt.Color.white);

        Play.setText("Play");
        Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayActionPerformed(evt);
            }
        });

        Stop.setText("Stop");
        Stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopActionPerformed(evt);
            }
        });

        Pause.setText("Pause");
        Pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PauseActionPerformed(evt);
            }
        });

        Previous.setText("Previous");
        Previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreviousActionPerformed(evt);
            }
        });

        Next.setText("Next");
        Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextActionPerformed(evt);
            }
        });

        ShuffleCB.setText("Shuffle");
        ShuffleCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ShuffleCBItemStateChanged(evt);
            }
        });

        PlaylistLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        PlaylistLabel.setText("Playlist");

        statusLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        statusLabel.setText("Stopped");

        Add.setText("Add");
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });

        Remove.setText("Remove");
        Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveActionPerformed(evt);
            }
        });

        Resume.setText("Resume");
        Resume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResumeActionPerformed(evt);
            }
        });

        clearList.setText("Clear List");
        clearList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearListActionPerformed(evt);
            }
        });

        listTrack.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        listTrack.setModel(listModel);
        listTrack.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listTrack.setToolTipText("");
        listTrack.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        listTrack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listTrackMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listTrack);

        repeatCB.setText("Repeat");
        repeatCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                repeatCBItemStateChanged(evt);
            }
        });

        jMenu1.setText("File");

        OpenFile.setText("Open File");
        OpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenFileActionPerformed(evt);
            }
        });
        jMenu1.add(OpenFile);

        OpenDir.setText("Open Directory");
        OpenDir.setActionCommand("Open");
        OpenDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenDirActionPerformed(evt);
            }
        });
        jMenu1.add(OpenDir);

        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        jMenu1.add(Exit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        About.setText("About");
        About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutActionPerformed(evt);
            }
        });
        jMenu2.add(About);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Remove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearList)
                        .addGap(31, 31, 31)
                        .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(repeatCB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ShuffleCB))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Play)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Stop)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Pause)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Resume)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Previous)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Next))
                    .addComponent(PlaylistLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Play)
                    .addComponent(Stop)
                    .addComponent(Pause)
                    .addComponent(Previous)
                    .addComponent(Next)
                    .addComponent(Resume))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ShuffleCB)
                    .addComponent(repeatCB))
                .addGap(12, 12, 12)
                .addComponent(PlaylistLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Add)
                    .addComponent(Remove)
                    .addComponent(statusLabel)
                    .addComponent(clearList))
                .addGap(76, 76, 76))
        );

        statusLabel.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        System.exit(0);
        // TODO add your handling code here:
    }//GEN-LAST:event_ExitActionPerformed

    private void PlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayActionPerformed
        // TODO add your handling code here:

        playTrack();
    }//GEN-LAST:event_PlayActionPerformed

    private void OpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenFileActionPerformed
// TODO add your handling code here:
        openFile();
    }//GEN-LAST:event_OpenFileActionPerformed

    private void NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextActionPerformed
        // TODO add your handling code here:
        nextTrack();
    }//GEN-LAST:event_NextActionPerformed

    private void StopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopActionPerformed
        // TODO add your handling code here:

        player.stop();
    }//GEN-LAST:event_StopActionPerformed

    private void PauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PauseActionPerformed
        // TODO add your handling code here:

        pauseTrack();
    }//GEN-LAST:event_PauseActionPerformed

    private void OpenDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenDirActionPerformed
        // TODO add your handling code here:

        openFolder();
    }//GEN-LAST:event_OpenDirActionPerformed

    private void PreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreviousActionPerformed
        // TODO add your handling code here:

        previousTrack();
    }//GEN-LAST:event_PreviousActionPerformed

    private void ResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResumeActionPerformed
        // TODO add your handling code here:

        resumeTrack();
    }//GEN-LAST:event_ResumeActionPerformed

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        // TODO add your handling code here:

        AddFile();
    }//GEN-LAST:event_AddActionPerformed

    private void AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutActionPerformed
        // TODO add your handling code here:
        
        JOptionPane.showMessageDialog(null, "NS Audio Player");
    }//GEN-LAST:event_AboutActionPerformed

    private void RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveActionPerformed
        // TODO add your handling code here:

        removeFile();
    }//GEN-LAST:event_RemoveActionPerformed

    private void clearListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearListActionPerformed
        // TODO add your handling code here:

        clearList();
    }//GEN-LAST:event_clearListActionPerformed

    private void listTrackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listTrackMouseClicked
        // TODO add your handling code here:
        
        if (evt.getClickCount() == 2) {
            player.stop();
            int plposn = listTrack.getSelectedIndex();
            playlist.setPosition(plposn);
            player.play((PlaylistItem) listModel.getElementAt(plposn));
        }
    }//GEN-LAST:event_listTrackMouseClicked

    private void ShuffleCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ShuffleCBItemStateChanged
        // TODO add your handling code here:
        
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            playlist.setShuffle(true);
        } else {
            playlist.setShuffle(false);
        }

    }//GEN-LAST:event_ShuffleCBItemStateChanged

    private void repeatCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_repeatCBItemStateChanged
        // TODO add your handling code here:

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            player.setRepeat(true);
        } else {
            player.setRepeat(false);
        }
    
    }//GEN-LAST:event_repeatCBItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem About;
    private javax.swing.JButton Add;
    private javax.swing.JMenuItem Exit;
    private javax.swing.JButton Next;
    private javax.swing.JMenuItem OpenDir;
    private javax.swing.JMenuItem OpenFile;
    private javax.swing.JButton Pause;
    private javax.swing.JButton Play;
    private java.awt.Label PlaylistLabel;
    private javax.swing.JButton Previous;
    private javax.swing.JButton Remove;
    private javax.swing.JButton Resume;
    private javax.swing.JCheckBox ShuffleCB;
    private javax.swing.JButton Stop;
    private javax.swing.JButton clearList;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList listTrack;
    private javax.swing.JCheckBox repeatCB;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

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
            java.util.logging.Logger.getLogger(JFramePlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFramePlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFramePlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFramePlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JFramePlayer().setVisible(true);
            }
        });

    }
}
