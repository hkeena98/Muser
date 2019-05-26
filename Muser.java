/**
 *@author: Henry Keena
 *@version: 1.0
 *@license: MIT
 */

 //Imports From Java Main Library
import java.io.*;
import java.util.*;
import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Imports From JavaFX Library */
import javafx.application.*;
import javafx.application.Application;
import javafx.beans.*;
import javafx.beans.value.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.geometry.*;
import javafx.util.*;

/** Imports From Java Apache Tika Framework */
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.MidiParser;
import org.apache.tika.parser.mp3.Mp3Parser;

/** Imports From XML Library */
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Start of Muser Class
 * Muser extends Application Class from the JavaFX Library
 * This class handles both the GUI elements of the program, as well as 
 * the background processes of the application. 
 * 
 * This class also utilizes methods from the Apache Tika Framework in order
 * to display song, playlist, and necessary information for the user's display.
 * The Apache License is included with the .jar files in the /assets/jar directory.
 */
public class Muser extends Application
{
	//Declares Stage and Scene
	private Stage stage;
	private Scene scene;
	
	//Declares VBox
	private VBox root = new VBox(8);
	
	//Declares GridPanes
	private GridPane grid = new GridPane();
	private GridPane grid1 = new GridPane();
	private GridPane grid2 = new GridPane();
	private GridPane grid3 = new GridPane();

	//Declares Labels
	private Label lblCurrentTitle = new Label("");
	private Label lblCurrentArtist = new Label("");	

	//Declares MenuBar
	private MenuBar menuBar = new MenuBar();

	//Declares Menu 
	private Menu menuOpt = new Menu("Options");

	//Declares MenuItems For Options Menu
	private MenuItem menFiles = new MenuItem("Open Files");
	private MenuItem menExit = new MenuItem("Exit");

	//Declares Buttons
	private Button btnPlay = new Button();
	private Button btnNext = new Button();
	private Button btnPrev = new Button();
	private Button btnShuffle = new Button();
	private Button btnLoop = new Button();

	//Declares Sliders
	private Slider sldVolume = new Slider(0, 1.0, 0.5);

	//Declares MediaPlayer
	private MediaPlayer player = null;

	//Creates ArrayList of All PlayLists
	private ArrayList<PlayList> playlists = new ArrayList<PlayList>();
	
	//Variable That Tracks Current PlayList
	private PlayList currentList;

	//Variable That Tracks Current Song
	private Song currentSong;

	//Variable That Tracks Shuffle
	private boolean isShuffled = false;

	//Variable That Tracks Loop
	private boolean isLooped = false;
	
	//Variable That Tracks If Playing
	private boolean isPlaying;

	//Variable That Tracks Volume Level
	private double currVolume = 0.5;

	/**
	 * This method sets the volume level of the current player object.
	 * When the slider on the GUI is moved, an event listener will 
	 * then call this method to change to the volume value of the player,
	 */
	public void setVolume(double volume)
	{
		try
		{
			player.setVolume(volume);
			currVolume = player.getVolume();
		}
		catch(Exception ext)
		{
			System.out.println("Muser Volume Error: "+ext);
		}
	}

	/**
	 * This method is used to set a loop on the currently playing song.
	 * 
	 * While the 'isLooped' attribute is set to 'true', the current playing
	 * song will continue to play after ending.
	 */
	public void setLoop()
	{
		try
		{
			if(isLooped == false)
			{
				isLooped = true;
			}
			else
			{
				isLooped = false;
			}

		}
		catch(Exception ext)
		{
			System.out.println("Muser Looper Error: "+ext);
		}
	}

	/**
	 * This method is used to set the current playlist to shuffle between songs.
	 * 
	 * While the 'isShuffled' attribute is set to 'true', the playlist will randomly
	 * choose the next song to play, instead of playing songs in their default order.
	 */
	public void setShuffle()
	{
		try
		{
			if(isShuffled == false)
			{
				isShuffled = true;
			}
			else
			{
				isShuffled = false;
			}
		}
		catch(Exception ext)
		{
			System.out.println("Muser Shuffle Error: "+ext);
		}
	}

	/**
	 * This method is responsible for loading the currently selected song
	 * into the mediaplayer object. 
	 */
	public void loadPlayer(Song load)
	{
		try	
		{
			stopPlayer();
			currentSong = load;
			File loaded = load.getFile();
			final Media media = new Media(loaded.toURI().toURL().toString());
			this.player = new MediaPlayer(media);
			setVolume(currVolume);
		}
		catch(Exception ext)
		{
			System.out.println("Muser Loading Error: "+ext);
		}
	}

	/**
	 * Method to stop the current mediaplayer object and the current song/playlist.
	 */
	public void stopPlayer()
	{
		try
		{
			isPlaying = false;
			player.stop();
		}
		catch(Exception ext)
		{
			System.out.println("Muser Stop Error: "+ext);
		}
	}

	/**
	 * Method that plays the current song.
	 * 
	 * When the mediaplayer object has been set to play, and loaded, this
	 * method will play the loaded song, and set the play/pause button on
	 * the GUI accordingly to the status of the mediaplayer object.
	 */
	public void playSong()
	{
		try
		{
			isPlaying = true;
			player.play();
			Image playImg = new Image(getClass().getResourceAsStream("assets/img/pause.png"));
			ImageView play = new ImageView(playImg);
			play.setFitHeight(50);
			play.setFitWidth(50);
			btnPlay.setGraphic(play);
			lblCurrentTitle.setText(currentSong.getTitle());
			lblCurrentArtist.setText(currentSong.getArtist());
			player.setOnEndOfMedia(new Runnable() 
			{
				public void run() 
				{
					if(isLooped == true)
					{
						player.seek(new Duration(0.0));
					}
					else if(isShuffled == true)
					{
						Random rand = new Random();
						int ran = rand.nextInt(currentList.getTrackCount());
						loadPlayer(currentList.getSong(ran));
						playSong();
					}					
					else
					{
   						nextSong();
					}					
				}
			});
		}
		catch(Exception ext)
		{
			System.out.println("Muser Play Error: "+ext);
		}	
	}

	/**
	 * Method to set the mediaplayer object to pause the current song.
	 * 
	 * When the mediaplayer object has been set to pause, this
	 * method will play the loaded song, and set the play/pause button on
	 * the GUI accordingly to the status of the mediaplayer object.
	 */
	public void pauseSong()
	{
		try
		{
			isPlaying = false;
			player.pause();
			Image playImg = new Image(getClass().getResourceAsStream("assets/img/play.png"));
			ImageView play = new ImageView(playImg);
			play.setFitHeight(50);
			play.setFitWidth(50);
			btnPlay.setGraphic(play);
		}
		catch(Exception ext)
		{
			System.out.println("Muser Pausing Error: "+ext);
		}	
	}

	/**
	 * Method responsible for switching between the current and next 
	 * song in the current playlist. When the GUI next song element is 
	 * fired, this method will load and play the next song in the playlist.
	 */
	public void nextSong()
	{
		try
		{
			stopPlayer();
			int pos = currentList.indexOf(currentSong);
			if(pos == currentList.getTrackCount()-1 && isShuffled == false)
			{
				pos = 0;
				loadPlayer(currentList.getSong(pos));
				playSong();			
			}
			else if(isShuffled == true)
			{
				Random rand = new Random();
				int ran = rand.nextInt(currentList.getTrackCount());
				loadPlayer(currentList.getSong(ran));
				playSong();
			}	
			else
			{
				pos++;
				loadPlayer(currentList.getSong(pos));
				playSong();
			}
		}
		catch(Exception ext)
		{
			System.out.println("Muser Next Error: "+ext);
		}
	}

	/**
	 * Method responsible for switching between the current and last 
	 * song in the current playlist. When the GUI last song element is 
	 * fired, this method will load and play the las song in the playlist.
	 */
	public void lastSong()
	{
		try
		{
			stopPlayer();
			int pos = currentList.indexOf(currentSong);
			if(pos == 0 && isShuffled == false)
		 	{
				pos = currentList.getTrackCount()-1;
				loadPlayer(currentList.getSong(pos));
				playSong();
			}
			else if(isShuffled == true)
			{
				Random rand = new Random();
				int ran = rand.nextInt(currentList.getTrackCount());
				loadPlayer(currentList.getSong(ran));
				playSong();
			}	
			else
			{
				pos--;
				loadPlayer(currentList.getSong(pos));
				playSong();
			}
		}
		catch(Exception ext)
		{
			System.out.println("Muser Last Error: "+ext);
		}	
	}

	/**
	 * This method is responsible for taking an input .wav or .mp3 file
	 * and using the Apache Tika framework, will parse through the metadata 
	 * of the selected file and find/make strings out of the song's title, artist,
	 * album, genre, file format, song duration, etc.
	 * 
	 * Metadata will be displayed in tracked and displayed in various places for the
	 * user on the GUI.
	 */
	public Song makeSong(File _file, String form)
	{
		String title = "";
		String artist = "";
		String album = "";
		String genre = "";
		String format = "";
		Duration songDur = null;
		try
		{
			InputStream input = new FileInputStream(_file);
			ContentHandler handler = new DefaultHandler();
			Metadata metadata = new Metadata();
			Parser parser = null;
			if(form.equals("mp3"))
			{
				parser = new Mp3Parser();
				format = "MP3 Audio";
			}
			else if(form.equals("wav"))
			{
				parser = new MidiParser();
				format = "WAV Audio";
			}	
			ParseContext parseCtx = new ParseContext();
			parser.parse(input, handler, metadata, parseCtx);
			input.close();
			
			title = metadata.get("title");
			if(title.equals("")||title.equals(" ")||title.equals(null))
			{
				title = _file.getName();
			}
			artist = metadata.get("xmpDM:artist");
			album = metadata.get("xmpDM:album");
			genre = metadata.get("xmpDM:genre");
		}
		catch(Exception ext)
		{
			System.out.println("Muser Make Song Error: "+ext);
		}
		Song newSong = new Song(title, artist, album, genre, _file, format);
		return newSong;
	}

	/** 
	 * Method responsible for opening a selected a folder/directory
	 * then filtering and reading all .wav and .mp3 files found in
	 * said directory.
	 * 
	 * Once all format appropriate files have been found in the directory
	 * then create Song objects from all files.
	 */
	public void readDirectory(File _file)
	{
		if(currentList == null)
		{
			currentList = new PlayList("Now Playing");
		}		
		File dir = new File(_file.getParent());
		String name = _file.getName();
		for(File sort : dir.listFiles())
		{
			String[] fileEx = sort.getName().split("\\.");
			String fileExt = Arrays.toString(fileEx);
			int len = fileEx.length-1;
			if(fileEx[len].equals("mp3"))
			{
				Song newSong = makeSong(sort, "mp3");
				currentList.addSong(newSong);
				System.out.println("Added File: "+sort.getName());
			}
			else if(fileEx[len].equals("wav"))
			{
				Song newSong = makeSong(sort, "wav");
				currentList.addSong(newSong);
				System.out.println("Added File: "+sort.getName());
			}
		}
		Song temp = null;
		for(int i = 0; i < currentList.getTrackCount(); i++)
		{
			if(currentList.getSong(i).getFile().getName().equals(name))
			{
				temp = currentList.getSong(i); 
			}
		}
		currentSong = temp;
		loadPlayer(currentSong);
		playSong();

	}
	
	/**
	 * Method for opening the file selection dialogue GUI, which will 
	 * allow the user to select a folder/directory.
	 * 
	 * The file selection dialogue will filter and only display .wav, .mp3, and 
	 * sub directories.
	 */
	public void fileChooser()
	{
		try
		{
			FileChooser fileCh = new FileChooser();
			fileCh.setTitle("Open MP3 or WAV Files");
			fileCh.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files (*.mp3)", "*.mp3"));
			fileCh.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV Files (*.wav)", "*.wav"));
			File selFile = fileCh.showOpenDialog(stage);
			if(selFile == null)
			{
				return;
			}
			stopPlayer();
			readDirectory(selFile);
		}
		catch(Exception ext)
		{
			System.out.println("Muser FileChooser Error: "+ext);
		}	
	}

	/**
	 * Application Exit Method
	 */
	public void exit()
	{
		System.exit(0);
	}

	

	/**
	 * Start method to build GUI components and application event handlers.
	 * 
	 * This method opens the GUI and starts the event listeners for user actions.
	 */
	public void start(Stage _stage) throws Exception
	{
		stage = _stage;
		stage.setTitle("Muser");
		
		int width = 420;
		int height = 270;

		stage.setMaxHeight(height);
		stage.setMinHeight(height);
		stage.setMaxWidth(width);
		stage.setMinWidth(width);

		//Adds all 
		menuBar.getMenus().addAll(menuOpt);
		menuOpt.getItems().addAll(menFiles, menExit);

		Image playImg = new Image(getClass().getResourceAsStream("assets/img/play.png"));
		ImageView play = new ImageView(playImg);
		play.setFitHeight(40);
		play.setFitWidth(40);
		btnPlay.setGraphic(play);
		
		Image fastImg = new Image(getClass().getResourceAsStream("assets/img/fastforward.png"));
		ImageView fastforward = new ImageView(fastImg);
		fastforward.setFitHeight(40);
		fastforward.setFitWidth(40);
		btnNext.setGraphic(fastforward);

		Image rewindImg = new Image(getClass().getResourceAsStream("assets/img/rewind.png"));
		ImageView rewind = new ImageView(rewindImg);
		rewind.setFitHeight(40);
		rewind.setFitWidth(40);
		btnPrev.setGraphic(rewind);

		Image loopImg = new Image(getClass().getResourceAsStream("assets/img/loop.png"));
		ImageView loop = new ImageView(loopImg);
		loop.setFitHeight(40);
		loop.setFitWidth(40);
		btnLoop.setGraphic(loop);

		Image shuffleImg = new Image(getClass().getResourceAsStream("assets/img/shuffle.png"));
		ImageView shuffle = new ImageView(shuffleImg);
		shuffle.setFitHeight(40);
		shuffle.setFitWidth(40);
		btnShuffle.setGraphic(shuffle);

		//Sets attributes to grid
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.addRow(0, lblCurrentTitle);
		lblCurrentTitle.setTextAlignment(TextAlignment.CENTER);

		//Sets attributes to grid1
		grid1.setAlignment(Pos.CENTER);
		grid1.setHgap(10);
		grid1.setVgap(10);
		grid1.addRow(1, lblCurrentArtist);
		lblCurrentArtist.setTextAlignment(TextAlignment.CENTER);

		//Sets attributes to grid2
		grid2.setAlignment(Pos.CENTER);
		grid2.setHgap(10);
		grid2.setVgap(10);
		grid2.addRow(1, btnShuffle, btnPrev, btnPlay, btnNext, btnLoop);

		//Sets attributes to grid3
		grid3.setAlignment(Pos.CENTER);
		grid3.setHgap(10);
		grid3.setVgap(10);
		grid3.addRow(1, sldVolume);

		//Button Handler
		btnPlay.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				if(isPlaying == false)
				{
					playSong();
				}
				else
				{
					pauseSong();
				}
			}
		});

		//Button Handler
		btnNext.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				nextSong();
			}
		});

		//Button Handler
		btnPrev.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				lastSong();
			}
		});

		//Button Handler
		btnLoop.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				setLoop();
			}
		});

		//Button Event Handler
		btnShuffle.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				setShuffle();
			}
		});

		//Menu Event Handler
		menFiles.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				fileChooser();
			}
		});
		
		//Menu Event Handler for Menu Exit Button
		menExit.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				exit();
			}
		});

		//Slider Event Handler for Volume Control Slider
		sldVolume.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> source_val, Number old_val, Number new_val)
			{
				double newVol = new_val.doubleValue();
				setVolume(newVol);
			}
		}); 

		//Window Exit Event Handler
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent we)
			{
				exit();
			}
		});

		//Adds child objects to root
		root.getChildren().addAll(menuBar, grid, grid1, grid2, grid3);		

		//Adds root to scene
		scene = new Scene(root, width, height);
		
		//Styles the scene and GUI with the css file in assets
		scene.getStylesheets().add("assets/css/styles.css");
		
		//Sets scene to stage objects and shows 
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * The main method simply launches the new instance of the JavaFX application.
	 */
	public static void main(String[] args)
	{
		launch(args);
   	}
}
