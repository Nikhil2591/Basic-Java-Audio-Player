package NSPlayer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nikhil
 *
 */
import java.awt.Color;
import java.util.Map;
import java.io.*;
import java.io.File;
import java.io.PrintStream;
import java.util.*;
import javazoom.jlgui.basicplayer.*;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class Player implements BasicPlayerListener {

    private BasicController playerControl;
    private boolean repeat;
    private JFramePlayer nsPlayer;
    private Playlist playlist;


    Player(JFramePlayer nsPlayer, Playlist playlist) {
        this.nsPlayer = nsPlayer;
        this.playlist = playlist;
        

    }

    public void play(PlaylistItem playlistItem) {
        System.out.println("playing " + playlistItem);
        int trackNumber = playlist.getPosition() + 1;
        nsPlayer.setStatusBarText("Playing Track " + trackNumber + "/" + playlist.getPlaylistSize());
        // Instantiate BasicPlayer.
        BasicPlayer player = new BasicPlayer();
        // BasicPlayer is a BasicController.
        playerControl = (BasicController) player;

        // Register BasicPlayerTest to BasicPlayerListener events.
        // It means that this object will be notified on BasicPlayer
        // events such as : opened(...), progress(...), stateUpdated(...)
        player.addBasicPlayerListener(this);

        try {
            // Open audio file to play.
            playerControl.open(new File(playlistItem.getPath()));

            // Start audio playback in a thread.
            playerControl.play();

            // Set Volume (0 to 1.0).
            // setGain should be called after control.play().
            playerControl.setGain(0.85);

            // Set Pan (-1.0 to 1.0).
            // setPan should be called after control.play().
            playerControl.setPan(0.0);
            


            // If you want to pause/resume/pause the played file then
            // write a Swing player and just call playerControl.pause(),
            // playerControl.resume() or playerControl.stop().			
            // Use playerControl.seek(bytesToSkip) to seek file
            // (i.e. fast forward and rewind). seek feature will
            // work only if underlying JavaSound SPI implements
            // skip(...). True for MP3SPI (JavaZOOM) and SUN SPI's
            // (WAVE, AU, AIFF).
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void seek(float percentage) {

    }
   
    public void setController(BasicController controller) {
        display("setController : " + controller);
    }

    public void stateUpdated(BasicPlayerEvent event) {
        // Notification of BasicPlayer states (opened, playing, end of media, ...)
        display("stateUpdated : " + event.toString());
        if (event.getCode() == BasicPlayerEvent.EOM) {
            stop();
            // If end of playlist and repeat not checked then stop
            if (!repeat && playlist.endOfPlaylist()) {
                nsPlayer.setListIndex(0);
                return;
            }
            playNext();
        }
    }

    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
        // Pay attention to properties. It depends on underlying JavaSound SPI
        //  MP3SPI provides mp3.equalizer.

        display("progress : " + properties.toString());

    }

    public void opened(Object stream, Map properties) {
        // Pay attention to properties. It's useful to get duration, 
        // bitrate, channels, even tag such as ID3v2.
        display("opened : " + properties.toString());

    }

    private void display(String msg) {
        //if (out != null) out.println(msg);
        //System.out.println(msg);
    }

    boolean getRepeat() {
        return (repeat);
    }

    void setRepeat(boolean rep) {
        repeat = rep;
    }


    private void playNext() {
        int plposn = playlist.incrementPosition();
        nsPlayer.setListIndex(plposn);
        nsPlayer.ensureIndexVisible(plposn);
        play(nsPlayer.getPlaylistItem());
    }

    public void stop() {
        try {
            playerControl.stop();
        } catch (Exception ex) {
        }
        nsPlayer.setStatusBarText("Stopped");
    }

    public void pause() {
        try {
            playerControl.pause();
        } catch (Exception ex) {
        }
        int trackNumber = playlist.getPosition() + 1;
        nsPlayer.setStatusBarText("Paused Playing " + trackNumber + "/" + playlist.getPlaylistSize());
    }

    public void resume() {
        try {
            playerControl.resume();
        } catch (Exception ex) {
        }
        int trackNumber = playlist.getPosition() + 1;
        nsPlayer.setStatusBarText("Playing Track " + trackNumber + "/" + playlist.getPlaylistSize());
    }
}
