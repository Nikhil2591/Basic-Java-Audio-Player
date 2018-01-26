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
import java.util.Random;


public class Playlist 
{	
	private int playlistPosition=0;	
	private int shuffleIndex=0;	
	private int playlistSize;
	private int[] shuffledPlaylist;
	private boolean shuffle=false;


        
	public void init(int playlistSize) {
		this.playlistSize=playlistSize;		
		shuffledPlaylist=new int[playlistSize];		
		for (int i=0;i<playlistSize;i++) {
			shuffledPlaylist[i]=i;
		}
		//shuffle
		Random randomGenerator = new Random();		
		for (int i=0;i<playlistSize;i++) {
			int randomInt = randomGenerator.nextInt(playlistSize); // generates random number in range 0 to (playlistSize-1)
			int temp=shuffledPlaylist[i];			
			shuffledPlaylist[i]=shuffledPlaylist[randomInt];
			shuffledPlaylist[randomInt]=temp;
		}	
		if (shuffle) {
			shuffleIndex=0;
			playlistPosition=shuffledPlaylist[shuffleIndex];
		} else {
			playlistPosition=0;
		}
	}
	
	public int getPlaylistSize() {
		return playlistSize;
	}
        

	public void setPosition(int plposn) {
		playlistPosition=plposn;
	}
	
	public int getPosition() {	
		return playlistPosition;			
	}
	
	public int incrementPosition() {
		if (shuffle) {
			shuffleIndex++;
			if (shuffleIndex > (playlistSize - 1)) {
				shuffleIndex=0;
			}
			playlistPosition=shuffledPlaylist[shuffleIndex];			
		} else {
			playlistPosition++;
			if (playlistPosition > (playlistSize - 1)) {
				playlistPosition=0;
			}				
		}
		return playlistPosition;
	}
	
	public int decrementPosition() {
		if (shuffle) {
			shuffleIndex--;
			if (shuffleIndex < 0) {
				shuffleIndex=playlistSize - 1;
			}
			playlistPosition=shuffledPlaylist[shuffleIndex];			
		} else {
			playlistPosition--;
			if (playlistPosition < 0) {
				playlistPosition = playlistSize - 1;				
			}				
		}
		return playlistPosition;				
	}
	
	public boolean endOfPlaylist() {
		if (shuffle) {
			if (shuffleIndex == playlistSize - 1) {
				return true;
			}
			else {
				return false;
			}
		} else {
			if (playlistPosition == playlistSize - 1) {
				return true;
			}
			else {
				return false;
			}
		}
		
	}
	
	public boolean getShuffle() {
		return(shuffle);
	}
	
	public void setShuffle(boolean shuf) {
		shuffle=shuf;
	}
        

	
}