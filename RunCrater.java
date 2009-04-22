import se.joakimbergman.crater.*;

public class RunCrater {
	public static void main(String[] args) throws Exception{
		for (String arg : args) {
			Crate c = new Crate(arg);
			System.out.println(arg);
			System.out.println("Version: " + c.getVersion());
			System.out.println("Number of tracks: " + c.getTracks().size());
			System.out.println("Columns:");
			for (Column column : c.getColumns()) {
				System.out.print(column.getName() + "\t");
			}
			System.out.println("Sorted by: " + c.getSortedBy().getName());
			System.out.println("Tracks:");
			for (Track track : c.getTracks()) {
				System.out.println(track.getPath());
			}
		}
	}
}
