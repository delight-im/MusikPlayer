package musikplayer;

/**
 * Copyright (C) 2013 www.delight.im <info@delight.im>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

public class Main extends JFrame implements ActionListener, MouseListener {

	public static final String NEWLINE = System.getProperty("line.separator");
	public static final String APPLICATION_INFO = "MusikPlayer 0.1.2"+NEWLINE+"https://github.com/delight-im/MusikPlayer"+NEWLINE+"GNU General Public License v3"+NEWLINE+NEWLINE+"JLayer by JavaZOOM (LGPL)"+NEWLINE+"ionicons by Drifty (MIT License)";
	public static final String DIRECTORY_MUSIC_COLLECTION = "";
	public static final String ICON_PATH = "/images/png/";
	private static final long serialVersionUID = 2L;
	private static final String TABLE_EVENT_KEY_ENTER = "keyEnter";
	private static final String PREFS_KEY_FONTSIZE = "prefs_fontsize_index";
	private static final String LIBRARY_FILE_NAME = "Library";
	private static final String LIBRARY_FILE_EXTENSION = ".ini";
	private static final int FONT_SIZE_INDEX_DEFAULT = 1;
	private static PlayingThread p = null;
	private static PlaylistTableModel mModel = null;
	private static JTable table = null;
	private static BufferedReader libraryReader;
	private static FileReader libraryReader_help;
	private static File fLibrary;
	// MENU-ITEMS ANFANG
	private static ImageIcon icEditingFalse;
	private static ImageIcon icEditingTrue;
	private static JButton btnPlay;
	private static JButton btnStop;
	private static JButton btnEditingMode;
	private static JButton btnAdd;
	private static JButton btnDelete;
	private static JButton btnZoom;
	private static JMenuItem miAbout;
	private static JMenuItem miExit;
	// MENU-ITEMS ENDE
	private static int[] mFontSizes = { 10, 12, 14, 16, 18, 20, 22, 24 };
	private static int mFontSizeIndex = FONT_SIZE_INDEX_DEFAULT;
	private static int mCurrentZoomDirection = 1;
	private static boolean isEditingMode = false;
	private static Preferences mPrefs = Preferences.userNodeForPackage(musikplayer.Main.class);

	public static void main(String[] args) {
		new Main();
	}

	private void exitApplication() {
		if (mModel != null) {
			FileWriter libraryWriter = null;
			String renameLibraryTo = LIBRARY_FILE_NAME+"_"+String.valueOf(System.currentTimeMillis()/1000L)+LIBRARY_FILE_EXTENSION;
			if (fLibrary != null) {
				String newLibraryName = fLibrary.getAbsolutePath().replace(LIBRARY_FILE_NAME+LIBRARY_FILE_EXTENSION, renameLibraryTo);
				try {
					Files.move(fLibrary.toPath(), Paths.get(newLibraryName), StandardCopyOption.REPLACE_EXISTING);
				}
				catch (IOException e) { }
				if (!fLibrary.exists()) {
					try {
						fLibrary.createNewFile();
					}
					catch (IOException e) {
						fLibrary = null;
					}
				}
				else if (fLibrary.delete()) {
					try {
						fLibrary.createNewFile();
					}
					catch (IOException e) {
						fLibrary = null;
					}
				}
				else {
					fLibrary = null;
				}
				if (fLibrary != null) {
					try {
						libraryWriter = new FileWriter(fLibrary, true);
					}
					catch (IOException e) {
						libraryWriter = null;
					}
					if (libraryWriter != null) {
						try {
							libraryWriter.append(mModel.getDataCSV());
						}
						catch (IOException e) {
							showMessage("Fehler", "Playlist konnte nicht gespeichert werden!", true);
						}
					}
				}
			}
			if (libraryWriter != null) {
				try {
					libraryWriter.close();
				}
				catch (IOException e) { }
			}
		}
		stopPlayingThread();
		if (mPrefs != null) {
			mPrefs.putInt(PREFS_KEY_FONTSIZE, mFontSizeIndex);
			mPrefs = null;
		}
		System.gc();
		System.exit(0);
	}

	private void showMessage(String title, String message, boolean is_warning) {
		int flag = JOptionPane.PLAIN_MESSAGE;
		if (is_warning) {
			flag = JOptionPane.WARNING_MESSAGE;
		}
		JOptionPane.showMessageDialog(Main.this, message, title, flag);
	}

	private int confirmYesNo(String title, String message) {
		return JOptionPane.showConfirmDialog(Main.this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}

	private class PromptExit extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showOptionDialog(Main.this, "Bist Du sicher, dass Du MusikPlayer beenden möchtest?", "Beenden", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			if (option == JOptionPane.YES_OPTION ) {
				exitApplication();
			}
		}
	}

	private void initializeGUI() {
		setBounds(100, 100, 494, 405);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setTitle("MusikPlayer");
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(ICON_PATH+"ic_headphones.png")));
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		mModel = new PlaylistTableModel();
		table = new JTable(mModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setAutoCreateRowSorter(true);
		table.getColumnModel().getColumn(3).setCellEditor(mModel.new GenreCellEditor());
		table.getColumnModel().getColumn(7).setCellEditor(mModel.new TanzCellEditor());
		table.getColumnModel().getColumn(8).setCellEditor(mModel.new BewertungCellEditor());
		table.addMouseListener(this);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		setFontSize(mPrefs.getInt(PREFS_KEY_FONTSIZE, FONT_SIZE_INDEX_DEFAULT), true);
		KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyEnter, TABLE_EVENT_KEY_ENTER);
		table.getActionMap().put(TABLE_EVENT_KEY_ENTER, new AbstractAction() {
			private static final long serialVersionUID = 140663257578981178L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopPlayingThread();
				playCurrentSelection();
			}
		});
		// READ LIBRARY FILE ANFANG
		fLibrary = new File(DIRECTORY_MUSIC_COLLECTION+LIBRARY_FILE_NAME+LIBRARY_FILE_EXTENSION);
		if (fLibrary.exists()) {
			try {
				libraryReader_help = new FileReader(fLibrary);
				libraryReader = new BufferedReader(libraryReader_help);
			}
			catch (Exception e) {
				showMessage("Fehler", LIBRARY_FILE_NAME+LIBRARY_FILE_EXTENSION+" verweigert Zugriff!", true);
				exitApplication();
			}
		}
		else {
			fLibrary = null;
			showMessage("Fehler", LIBRARY_FILE_NAME+LIBRARY_FILE_EXTENSION+" nicht gefunden!", true);
			exitApplication();
		}
		if (libraryReader != null) {
			try {
				String s;
				String[] l;
				while ((s = libraryReader.readLine()) != null) {
					l = s.split("/", -1); // flag -1 wichtig sonst werden leere Strings weggelassen
					if (l.length >= 9) {
						mModel.addRow(l[0], l[1], l[2], l[4], l[5], l[6], l[7], l[8]);
					}
					else if (l.length >= 8) {
						mModel.addRow(l[0], l[1], l[2], l[4], l[5], l[6], l[7], "");
					}
				}
			}
			catch (Exception e) {
				showMessage("Fehler", LIBRARY_FILE_NAME+LIBRARY_FILE_EXTENSION+" nicht beschreibbar!", true);
				exitApplication();
			}
			finally {
				try {
					libraryReader_help.close();
					libraryReader.close();
				}
				catch (IOException e) { }
			}
		}
		// READ LIBRARY FILE ENDE
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel((LayoutManager) new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(panel, BorderLayout.NORTH);
		
		btnPlay = new JButton(new ImageIcon(this.getClass().getResource(ICON_PATH+"ic_play.png")));
		btnPlay.setBorder(null);
		btnPlay.setBackground(null);
		btnPlay.addActionListener(this);
		panel.add(btnPlay);
		
		btnStop = new JButton(new ImageIcon(this.getClass().getResource(ICON_PATH+"ic_stop.png")));
		btnStop.setBorder(null);
		btnStop.setBackground(null);
		btnStop.addActionListener(this);
		panel.add(btnStop);
		
		icEditingFalse = new ImageIcon(this.getClass().getResource(ICON_PATH+"ic_write.png"));
		icEditingTrue = new ImageIcon(this.getClass().getResource(ICON_PATH+"ic_write_active.png"));
		btnEditingMode = new JButton(icEditingFalse);
		btnEditingMode.setBorder(null);
		btnEditingMode.setBackground(null);
		btnEditingMode.addActionListener(this);
		panel.add(btnEditingMode);
		
		btnAdd = new JButton(new ImageIcon(this.getClass().getResource(ICON_PATH+"ic_add.png")));
		btnAdd.setBorder(null);
		btnAdd.setBackground(null);
		btnAdd.addActionListener(this);
		panel.add(btnAdd);
		
		btnDelete = new JButton(new ImageIcon(this.getClass().getResource(ICON_PATH+"ic_delete.png")));
		btnDelete.setBorder(null);
		btnDelete.setBackground(null);
		btnDelete.addActionListener(this);
		panel.add(btnDelete);
		
		btnZoom = new JButton(new ImageIcon(this.getClass().getResource(ICON_PATH+"ic_resize.png")));
		btnZoom.setBorder(null);
		btnZoom.setBackground(null);
		btnZoom.addActionListener(this);
		panel.add(btnZoom);
		
		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		final JMenu mnPlayer = new JMenu("Player");
		menuBar.add(mnPlayer);
			
		miExit = new JMenuItem("Beenden");
		miExit.addActionListener(this);
		mnPlayer.add(miExit);
		
		final JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);
		
		miAbout = new JMenuItem("Über MusikPlayer");
		miAbout.addActionListener(this);
		mnHilfe.add(miAbout);
		
		addWindowListener(new PromptExit());
		
		pack();
		setVisible(true);
		
	}

	public class PlayingThread extends SwingWorker <Void, Void> {
		
		private String fileName = "";
		private boolean cancelled = false;
		private Sequencer sq;
		
		private PlayingThread(String fName) {
			fileName = fName;
		}
		
		private void stop() {
			cancelled = true;
			this.cancel(true);
		}

	    @Override
	    protected Void doInBackground() throws Exception {
	    	sq = new Sequencer();
    		sq.play(fileName);
	        return null;
	    }

	    @Override
	    protected void done() {
	    	if (sq != null) {
		    	sq.stop();
		    	sq = null;
	    	}
	    	if (!cancelled) {
		    	advancePlaylistCursor();
	    	}
	    }

	}

	public Main() {
		super("Main");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initializeGUI();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void playFile(final String fileName) {
		p = new PlayingThread(DIRECTORY_MUSIC_COLLECTION+fileName);
		p.execute();
	}

	public static boolean isEditingMode() {
		return isEditingMode;
	}

	private void playCurrentSelection() {
		if (table.getSelectedRow() != -1) {
			String file = (String) table.getValueAt(table.getSelectedRow(), -1);
			playFile(file);
		}
		else {
			showMessage("Fehler", "Bitte einen Song auswählen, der abgespielt werden soll!", true);
		}
	}

	private void stopPlayingThread() {
		if (p != null) {
			p.stop();
			p = null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final Object s = event.getSource();
		if (s == btnPlay) {
			stopPlayingThread();
			playCurrentSelection();
		}
		else if (s == btnStop) {
			stopPlayingThread();
		}
		else if (s == btnEditingMode) {
			if (isEditingMode) {
				isEditingMode = false;
				btnEditingMode.setIcon(icEditingFalse);
			}
			else {
				isEditingMode = true;
				btnEditingMode.setIcon(icEditingTrue);
			}
		}
		else if (s == btnAdd) {
			final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
			for (int i = 0; i < 3; i++) { // go three steps up to <Computer> directory
				fc.changeToParentDirectory();
			}
			int fcReturn = fc.showOpenDialog(null);
			if (fcReturn == JFileChooser.APPROVE_OPTION) {
				File returnedFile = fc.getSelectedFile();
				if (returnedFile.exists()) {
					String fileName = returnedFile.getName();
					if (fileName.endsWith(".mp3")) {
						String fileType = fileName.substring(fileName.length()-4);
						String[] originalFileNameParts = fileName.replace(".mp3", "").split(" - ", -1);
						String fileMD5 = MD5.get(returnedFile);
						if (!fileMD5.equals("")) {
							try {
								String newFileName = fileMD5+fileType;
								String newInterpret = "";
								String newSongtitel = "";
								String newAlbum = "";
								Files.copy(returnedFile.toPath(), Paths.get(DIRECTORY_MUSIC_COLLECTION+newFileName), StandardCopyOption.REPLACE_EXISTING);
								if (originalFileNameParts.length == 4) {
									newInterpret = originalFileNameParts[1].trim();
									newSongtitel = originalFileNameParts[2].trim();
									newAlbum = originalFileNameParts[3].trim();
								}
								mModel.addRow(newInterpret, newSongtitel, newFileName, "", newAlbum, "", "", "");
								showMessage("Song hinzugefügt", "Der Song wurde erfolgreich hinzugefügt!", false);
							}
							catch (IOException e) {
								e.printStackTrace();
								showMessage("Fehler", "Der Song konnte nicht hinzugefügt werden! (Fehler beim Kopieren)", true);
							}
						}
						else {
							showMessage("Fehler", "Der Song konnte nicht hinzugefügt werden! (Fehler beim Zugriff)", true);
						}
					}
					else {
						showMessage("Fehler", "Der Song konnte nicht hinzugefügt werden! (keine MP3-Datei)", true);
					}
				}
				else {
					showMessage("Fehler", "Der Song konnte nicht hinzugefügt werden! (Datei existiert nicht)", true);
				}
			}
		}
		else if (s == miAbout) {
			String aboutText = APPLICATION_INFO+NEWLINE+NEWLINE+table.getRowCount()+" Songs";
			showMessage("Über", aboutText, false);
		}
		else if (s == miExit) {
			exitApplication();
		}
		else if (s == btnDelete) {
			if (table.getSelectedRow() != -1) {
				if (confirmYesNo("Song löschen", "Soll der ausgewählte Song wirklich gelöscht werden?") == JOptionPane.YES_OPTION) {
					mModel.removeRow(table.getSelectedRow());
				}
			}
		}
		else if (s == btnZoom) {
			setFontSize(mFontSizeIndex+mCurrentZoomDirection, false);
		}
	}

	private void setFontSize(int index, boolean isInitial) {
		if (index < 0) {
			index = 1;
			mCurrentZoomDirection *= -1;
		}
		else if (index >= mFontSizes.length) {
			index = mFontSizes.length-2;
			mCurrentZoomDirection *= -1;
		}
		if (isInitial) {
			if (index >= FONT_SIZE_INDEX_DEFAULT) {
				mCurrentZoomDirection = 1;
			}
			else {
				mCurrentZoomDirection = -1;
			}
		}
		mFontSizeIndex = index;
		table.setFont(new Font(table.getFont().getFontName(), Font.PLAIN, mFontSizes[mFontSizeIndex]));
		table.setRowHeight(14+(mFontSizeIndex-FONT_SIZE_INDEX_DEFAULT)*2);			
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getSource().getClass() == JTable.class) {
			if (event.getClickCount() == 2) {
				stopPlayingThread();
				playCurrentSelection();
			}			
		}
	}

	private void advancePlaylistCursor() {
		int oldPos = table.getSelectedRow();
		if ((oldPos+1) == table.getRowCount()) {
			table.setRowSelectionInterval(0, 0);
		}
		else {
			table.setRowSelectionInterval(oldPos+1, oldPos+1);
		}
		int newPos = table.getSelectedRow();
		if ((boolean) table.getModel().getValueAt(newPos, 0)) {
			playCurrentSelection();
		}
		else {
			advancePlaylistCursor();
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) { }

	@Override
	public void mouseExited(MouseEvent event) { }

	@Override
	public void mousePressed(MouseEvent event) { }

	@Override
	public void mouseReleased(MouseEvent event) { }

}