

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;

/**
 * to note, will not work on Mac yet.
 * edit: WILL WORK ON MAC MOTHER FUCKERS
 * 
 * 
 * @author Marcus
 * 
 */
public class Variable {
	private String value;
	private String filePath;
	private String fileDir;
	private static String fileExtension;

	static {
		// first the default value is set. this is used
		// only to get the real value.
		fileExtension = "var";
		// make a new variable, for the extension
		// do not force it to be var.
		Variable var = new Variable("" + System.getenv("APPDATA") + "\\MAndApps\\core\\", "extension", "var", false);
		// grab its value and reset the extension.
		fileExtension = var.getValue();
	}

	/**
	 * dir - where the variable file is stored. - should be formatted like this:
	 * - "C:/folder/folder/" - or on mac, - "~/folder/folder/"
	 * 
	 * name - simple, name of variable file
	 * 
	 * value - value to try and set the file to. though if it already has a
	 * value, it won't do anything
	 * 
	 * force - if true, value will always be set to the value given, regardless
	 * of if the value is already there.
	 * 
	 * @param dir
	 * @param name
	 * @param value
	 * @param force
	 */

	public Variable(String dir, String name, String value, boolean force) {
		// first lets get the full path.
		if (dir.startsWith("~/")) { //TODO FIX THIS LATER
			dir = dir.substring(1);
		} else {
			dir = dir.replace('/', '\\');
		}
		// dir should now be fixed... i guess.

		fileDir = dir;
		filePath = dir + name + "." + fileExtension;
		// try and load value from file, if null, screw it.

		String str = getValueFromFile();
		
		// if we could not load a value from the file
		// AKA didnt fucking exist.
		// ORRRRRRR if you were an ass, and forced
		// the value.
		if (str == null) {
			this.value = value;
			saveValue();
		} else if(force) {
			this.value = value;
			saveValue();
		} else {
			this.value = str;
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		saveValue();
	}

	private void saveValue() {
		deleteFile();
		createFile();
		try {
			// Q&D write value to current filepath.
			Formatter f = new Formatter(filePath);
			f.format("" + value);
			f.close();
		} catch (Exception e) {
			// if(weArriveHere){
			// we.are("fucked");
			// }
			e.printStackTrace();
		}
	}

	private void deleteFile() {
		File f = new File(filePath);
		f.delete();
	}

	private void createFile() {
		File f = new File(fileDir);
		f.mkdirs();
		f = new File(filePath);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// if(weArriveHere){
			// we.are("fucked");
			// }
			e.printStackTrace();
		}
	}

	private String getValueFromFile() {
		try {
			File f = new File(filePath);
			Scanner s = new Scanner(f);
			String str = s.nextLine();
			s.close();
			return str;
		} catch (Exception e) {
			return null;
		}
	}
}
