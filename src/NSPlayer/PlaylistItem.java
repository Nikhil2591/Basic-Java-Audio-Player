/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NSPlayer;

/**
 *
 * @author Nikhil
 */

import java.io.*;
import java.net.*;

public class PlaylistItem {
    
    	private File audioFile= null;	
	// declare audioFile as file
	public PlaylistItem(File file) {		
		this.audioFile=file;
	}
        // get path of the audio file
	public String getPath() {
		return audioFile.getPath();
	}	
	
	// override toString so that filename shows in the JList component
	@Override public String toString() {
		return audioFile.getName();		
	}
    
}
