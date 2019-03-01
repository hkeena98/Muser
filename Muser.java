/**
 *@author: Henry Keena
 *@version: 0.1
 *@license: MIT
 */

import javafx.application.*;
import javafx.application.Application;
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
import java.io.*;
import java.util.*;

public class Muser extends Application implements EventHandler<ActionEvent>
{
	//Declares Stage and Scene
	private Stage stage;
	private Scene scene;
	
	//Declares VBox
	private VBox root = new VBox(8);
	
	//Declares GridPanes
	private GridPane grid = new GridPane();
	private GridPane grid2 = new GridPane();

	//Declares MenuBar
	private MenuBar menuBar = new MenuBar();

	//Declares Menu 
	private Menu menuOpt = new Menu("Options");

	//Declares MenuItems
	private MenuItem menFiles = new MenuItem("Open Files");
	private MenuItem menExit = new MenuItem("Exit");

	//Declares Buttons
	private Button btnPlay = new Button("Play");
		private Button btnNext = new Button("Next");
	private Button btnPrev = new Button("Last");

	//Declares MediaPlayer
	private MediaPlayer player = null;

	//Variable That Tracks Loaded Song File
	private ArrayList<File> fileArr = new ArrayList();

	//Variable To Index/Tracks Songs
	int track;

	//Variable To Keep Track of Song
	File loader = null;


	public void loadPlayer(File load)
	{
		try
		{
			stopPlayer();
			loader = load;
			final Media media = new Media(load.toURI().toURL().toString());
			this.player = new MediaPlayer(media);
			btnPlay.setText("Play");	
		}
		catch(Exception ext)
		{
			System.out.println(ext);
		}
	}

	public void stopPlayer()
	{
		try
		{
			player.stop();
			btnPlay.setText("Play");
		}
		catch(Exception ext)
		{
			System.out.println(ext);
		}
	}

	public void playSong()
	{
		try
		{
			player.play();
			btnPlay.setText("Pause");
			player.setOnEndOfMedia(new Runnable() 
			{
				public void run() 
				{
   					nextSong();					
				}
			});
		}
		catch(Exception ext)
		{
			System.out.println(ext);
		}	
	}

	public void pauseSong()
	{
		try
		{
			player.pause();
			btnPlay.setText("Play");
		}
		catch(Exception ext)
		{
			System.out.println(ext);
		}	
	}

	public void nextSong()
	{
		try
		{
			stopPlayer();
			int pos = fileArr.indexOf(loader);
			if(pos == fileArr.size()-1)
			{
				pos = 0;
				loadPlayer(fileArr.get(pos));
				playSong();			
			}
			else
			{
				pos++;
				loadPlayer(fileArr.get(pos));
				playSong();
			}
		}
		catch(Exception ext)
		{
			System.out.println(ext);
		}
	}


	public void lastSong()
	{
		try
		{
			stopPlayer();
			int pos = fileArr.indexOf(loader);
			if(pos == 0)
			{
				pos = fileArr.size()-1;
				loadPlayer(fileArr.get(pos));
				playSong();
			}
			else
			{
				pos--;
				loadPlayer(fileArr.get(pos));
				playSong();
			}
		}
		catch(Exception ext)
		{
			System.out.println(ext);
		}	
	}

	public void readDirectory(File _file)
	{
		fileArr.clear();
		File dir = new File(_file.getParent());
		for(File sort : dir.listFiles())
		{
			String[] fileEx = sort.getName().split("\\.");
			String fileExt = Arrays.toString(fileEx);
			int len = fileEx.length-1;
			if(fileEx[len].equals("mp3")||fileEx[len].equals("wav"))
			{
				fileArr.add(sort);
				System.out.println("Added File: "+sort.getName());
			}
		}
	}
	
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
			loader = selFile;
			loadPlayer(loader);
			playSong();
		}
		catch(Exception ext)
		{
			System.out.println(ext);
		}	
	}

	public void exit()
	{
		System.exit(0);
	}

	//Start Method
	public void start(Stage _stage) throws Exception
	{
		stage = _stage;
		stage.setTitle("Muser");
		
		int width = 300;
		int height = 150;

		stage.setMaxHeight(height);
		stage.setMinHeight(height);
		stage.setMaxWidth(width);
		stage.setMinWidth(width);
		
		menuBar.getMenus().addAll(menuOpt);
		menuOpt.getItems().addAll(menFiles, menExit);

   		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.addRow(1, btnPrev, btnPlay, btnNext);
		
		//Button Handler
		btnPlay.setOnAction(this);

		//Button Handler
		btnNext.setOnAction(this);

		//Button Handler
		btnPrev.setOnAction(this);

		//Menu Handler
		menFiles.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				fileChooser();
			}
		});

		//Menu Handler
		menExit.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent evt)
			{
				exit();
			}
		});

		//Window Exit Handler
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent we)
			{
				exit();
			}
		});

		root.getChildren().addAll(menuBar, grid, grid2);		

		scene = new Scene(root, width, height);
		scene.getStylesheets().add("assets/css/styles.css");
		

		stage.setScene(scene);
		stage.show();
	}

	//Button Handler Method
	public void handle(ActionEvent evt)
	{
		Button btn = (Button)evt.getSource();
				
		switch(btn.getText())
		{
			case "Play":
				playSong();
				break;
			case "Pause":
				pauseSong();
				break;
			case "Last":
				lastSong();
				break;
			case "Next":
				nextSong();
				break;
		}
	}

   	// Main just instantiates an instance of this GUI class
   	public static void main(String[] args)
	{
		launch(args);
   	}
}
