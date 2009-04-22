package se.joakimbergman.crater;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


public class Crate {
	private final static boolean DEBUG = false;
	
	private final static int TRACK = 1886679659; // ptrk as int
	private final static int SORT = 1869836916; // osrt
	private final static int COLUMN = 1953915758; // tvcn = COLUMN
	private final static int brev = 1651664246; // brev - after this one, columns start
	private final static int ovct = 1870029684; // ovct - column width?
	private final static int tvcw = 1953915767; // tvcw
	private final static int VERSION = 1987212142; // vrsn - version of scratchlive format
	
	
	private ArrayList<Track> tracks;
	private ArrayList<Column> columns;
	private String version;
	private Column sortedBy;

	public Column getSortedBy() {
		return sortedBy;
	}

	public void setSortedBy(Column sortedBy) {
		this.sortedBy = sortedBy;
	}

	public ArrayList<Track> getTracks() {
		return tracks;
	}
	
	public ArrayList<Column> getColumns() {
		return columns;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void printCrateFile(String filename) {
		try {
			RandomAccessFile file = new RandomAccessFile(filename, "r");
			long i;
			int int_value;
			short b, short_value;
			// boolean columns_ahead = false;
			String string, sortedByTmp = "";
			
			for (i = 0; i < file.length(); i++) {
				file.seek(i);
				if (i + 4 <= file.length()) {
					file.seek(i);
					int_value = file.readInt();
					switch (int_value) {
						case TRACK:
							file.seek(file.getFilePointer() + 2); // jump 2 bytes for UTF read
							string = file.readUTF();
							if (DEBUG) System.err.println("Found track: " + string);
							tracks.add(new Track(string));
							
							// minus 1 for the ++ in the next iteration
							i = file.getFilePointer() - 1;
							continue;
						case SORT:
							int_value = file.readInt();
							if (DEBUG) System.err.println("Found osrt: " + int_value);
							if (file.readInt() == COLUMN) {
								file.seek(file.getFilePointer() + 2);
								sortedByTmp = file.readUTF();
							}
							break;
						/*case COLUMN: // Handle this one in ovct for now
							file.seek(file.getFilePointer() + 2); // jump 2 bytes for UTF read later
							string = file.readUTF();
							if (columns_ahead) {
								if (DEBUG) System.err.println("Adding column: " + string);
								columns.add(new Column(string));
							}
							if (DEBUG) System.err.println("Found tvcn: " + string);
							break;*/
						case brev: // after this one, columns start
							// columns_ahead = true;
							int_value = file.readInt();
							if (DEBUG) System.err.println("Found brev: " + int_value);
							break;
						case ovct: // column width?
							int_value = file.readInt();
							if (DEBUG) System.err.println("Found ovct: " + int_value);
							if (file.readInt() == COLUMN) { // tvcn = COLUMN
								file.seek(file.getFilePointer() + 2); // jump 2 bytes for UTF read
								string = file.readUTF();
								if (DEBUG) System.err.println("Adding column: " + string + "(" + int_value + ")");
								Column c = new Column(string, int_value);
								if (string.equals(sortedByTmp)) {
									sortedBy = c;
								}
								columns.add(c);
							}
							break;
						case tvcw: // tvcw
							int_value = file.readInt();
							if (DEBUG) System.err.println("Found tvcw: " + int_value);
							break;
						case VERSION:
							file.seek(file.getFilePointer() + 2); // jump 2 bytes for UTF read
							version = file.readUTF();
							if (DEBUG) System.err.println("Found vrsn: " + version);
							break;
					}
				}
				if (DEBUG){
					file.seek(i);
					b = (short)file.read();
					int_value = 0;
					short_value = 0;
					if (i + 2 <= file.length()) {
						file.seek(i);
						short_value = file.readShort();
					}
					System.err.printf("%3d\t%c\t%x\t%10d\t%5d\n", i, b, b, int_value, short_value);
				}
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public Crate(String filename) {
		tracks = new ArrayList<Track>();
		columns = new ArrayList<Column>();
		printCrateFile(filename);
	}
}
