/**
 *@author: Henry Keena
 *@version: 1.0
 *@license: MIT
 */

import java.io.*;
import java.util.*;

/**
 * 
 */
public class PlayList implements Serializable
{
	private String name;
	private ArrayList<Song> list; 

	/**
	 * 
	 * @param _name
	 */
	public PlayList(String _name)
	{
		name = _name;
		list = new ArrayList<Song>();
	}

	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @param _name
	 */
	public void setName(String _name)
	{
		name = _name;
	}

	/**
	 * 
	 * @param song
	 */
	public void addSong(Song song)
	{
		list.add(song);
	}

	/**
	 * 
	 * @param song
	 */
	public void removeSong(Song song)
	{
		list.remove(song);
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public Song getSong(int x)
	{
		return list.get(x);
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public int indexOf(Song x)
	{
		return list.indexOf(x);
	}

	/**
	 * 
	 */
	public void clearPlayList()
	{
		list.clear();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTrackCount()
	{
		return list.size();
	}
}
