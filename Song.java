/**
 *@author: Henry Keen
 *@version: 1.0
 *@license: MIT
 */

import java.io.*;
import java.util.*;
import javafx.util.*;

public class Song implements Serializable
{
	private String title;
	private String artist;
	private String album;
	private String genre;
	private File songFile;
	private String format;
	private Duration duration;
	
	public Song(String _title, String _artist, String _album, String _genre, File _songFile, String _format)
	{
		title = _title;
		artist = _artist;
		album = _album;
		genre = _genre;
		songFile = _songFile;
		format = _format;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String _title)
	{
		title = _title;
	}	

	public String getArtist()
	{
		return artist;
	}

	public void setArtist(String _artist)
	{
		artist = _artist;
	}

	public String getAlbum()
	{
		return album;
	}

	public void setAlbum(String _album)
	{
		album = _album;
	}

	public String getGenre()
	{
		return genre;
	}

	public void setGenre(String _genre)
	{
		genre = _genre;
	}

	public File getFile()
	{
		return songFile;
	}

	public void setFile(File _file)
	{
		songFile = _file;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String _format)
	{
		format = _format;
	}
}	
