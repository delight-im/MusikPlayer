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

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class PlaylistTableModel implements TableModel {

	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	private ArrayList<Song> songs = new ArrayList<Song>();
	private String[] columnNames = new String[] { "", "Interpret", "Songtitel", "Kategorie", "Album", "Jahr", "Dauer", "Tanz", "" };
	public static final String NEWLINE = System.getProperty("line.separator");

	@Override
	public void addTableModelListener(TableModelListener l) {
		if (listeners.contains(l)) {
			return;
		}
		listeners.add(l);
	}
	
	public String getDataCSV() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < songs.size(); i++) {
			if (i > 0) {
				sb.append(NEWLINE);
			}
			sb.append(songs.get(i).getDataCSV());
		}
		return sb.toString();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return Boolean.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			case 3:
				return String.class;
			case 4:
				return String.class;
			case 5:
				return Integer.class;
			case 6:
				return Integer.class;
			case 7:
				return String.class;
			case 8:
				return Integer.class;
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return 9;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return songs.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Song songToReturn = songs.get(rowIndex);
		if (columnIndex == -1) {
			return songToReturn.getFileName();
		}
		if (columnIndex == 0) {
			return songToReturn.getSelected();
		}
		else if (columnIndex == 1) {
			return songToReturn.getInterpret();
		}
		else if (columnIndex == 2) {
			return songToReturn.getSongtitel();
		}
		else if (columnIndex == 3) {
			return songToReturn.getGenre();
		}
		else if (columnIndex == 4) {
			return songToReturn.getAlbum();
		}
		else if (columnIndex == 5) {
			return songToReturn.getJahr();
		}
		else if (columnIndex == 6) {
			return songToReturn.getDauerInSekunden();
		}
		else if (columnIndex == 7) {
			return songToReturn.getTanz();
		}
		else if (columnIndex == 8) {
			return songToReturn.getBewertung();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return true;
		}
		else {
			return Main.isEditingMode();
		}
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Song songToEdit = songs.get(rowIndex);
		if (columnIndex == 0) {
			songToEdit.setSelected((boolean) value);
		}
		else if (columnIndex == 1) {
			songToEdit.setInterpret((String) value);
		}
		else if (columnIndex == 2) {
			songToEdit.setSongtitel((String) value);
		}
		else if (columnIndex == 3) {
			songToEdit.setGenre((String) value);
		}
		else if (columnIndex == 4) {
			songToEdit.setAlbum((String) value);
		}
		else if (columnIndex == 5) {
			songToEdit.setJahr((int) value);
		}
		else if (columnIndex == 6) {
			songToEdit.setDauerInSekunden((int) value);
		}
		else if (columnIndex == 7) {
			songToEdit.setTanz((String) value);
		}
		else if (columnIndex == 8) {
			songToEdit.setBewertung((int) value);
		}
		TableModelEvent tEvent = new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE);
		for (TableModelListener l: listeners) {
			l.tableChanged(tEvent); // inform listeners that table has been changed
		}
	}

	public void addRow(String interpret, String songtitel, String fileName, String genre, String album, String bewertung, String jahr, String tanz) {
		int intJahr = 0;
		int intBewertung = 0;
		try {
			intJahr = Integer.parseInt(jahr);
			intBewertung = Integer.parseInt(bewertung);
		}
		catch (Exception e) { }
		Song songToAdd = new Song(interpret, songtitel, fileName, genre, album, tanz, intJahr, 0, intBewertung);
		songs.add(songToAdd);
		TableModelEvent event = new TableModelEvent(this, songs.size()-1, songs.size()-1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
		for (TableModelListener l: listeners) {
			l.tableChanged(event);
		}
	}

	public void removeRow(int selectedRow) {
		songs.remove(selectedRow);
		TableModelEvent event = new TableModelEvent(this, selectedRow, selectedRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
		for (TableModelListener l: listeners) {
			l.tableChanged(event);
		}
	}
	
	public class TanzCellEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;

		String[] selectOptions = { "", "Cha-Cha-Cha", "Blues", "Bolero", "Boogie-Woogie", "Bossa Nova", "Calypso", "Discofox", "Flamenco", "Foxtrott", "Jive", "Langsamer Walzer", "Merengue", "Paso Doble", "Polka", "Quickstep", "Rock'n'Roll", "Rumba", "Salsa", "Samba", "Tango", "Two-Step", "Wiener Walzer" };
	    JComponent component = new JComboBox<String>(selectOptions);

	    @SuppressWarnings("unchecked")
	    public Object getCellEditorValue() {
	        return ((JComboBox<String>) component).getSelectedItem(); // returns new value (result of editing)
	    }

	    @SuppressWarnings("unchecked")
	    @Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean is_selected, int row, int column) {
			((JComboBox<String>) component).setSelectedItem(((String) value));
			return component;
		}
	}
	
	public class GenreCellEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;

		String[] selectOptions = { "", "Karneval", "Musical", "Oldies", "R&B", "Reggae", "Rock & Pop", "Soul" };
	    JComponent component = new JComboBox<String>(selectOptions);

	    @SuppressWarnings("unchecked")
		public Object getCellEditorValue() {
	        return ((JComboBox<String>) component).getSelectedItem(); // returns new value (result of editing)
	    }

	    @SuppressWarnings("unchecked")
	    @Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean is_selected, int row, int column) {
			((JComboBox<String>) component).setSelectedItem(((String) value));
			return component;
		}
	}
	
	public class BewertungCellEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;

		String[] selectOptions = { "0", "1", "2", "3", "4", "5", "6" };
	    JComponent component = new JComboBox<String>(selectOptions);

	    @SuppressWarnings("unchecked")
	    public Object getCellEditorValue() {
	        return ((JComboBox<String>) component).getSelectedItem(); // returns new value (result of editing)
	    }

	    @SuppressWarnings("unchecked")
	    @Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean is_selected, int row, int column) {
			((JComboBox<String>) component).setSelectedItem(((int) value));
			return component;
		}
	}

}
