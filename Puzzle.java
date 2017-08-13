/*
 * Please solve the following puzzle which simulates generic directory structures.
 * The solution should be directory agnostic.
 * Be succinct yet readable. You should not exceed more than 200 lines.
 * Consider adding comments and asserts to help the understanding.
 * Code can be compiled with javac Directory.java
 * Code should be executed as: java -ea Directory (-ea option it's to enabled the assert)
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Shell is the class which allow to simulate change directory based on a
 * generic directory structure. In order to walk through the directory structure
 * it simulates the behavior of 'cd' command
 * 
 * Assumptions: root path is '/' path separator is '/'
 * 
 * @author Leonardo Hildt
 *
 */
class Shell {

	private String path;

	private static final String ROOT_PATH = "/";

	private static final String SEPARATOR = "/";

	private static final String PARENT_DIR = "..";

	private static final String CURRENT_DIR = ".";

	public Shell() {
		this.path = ROOT_PATH;
	}

	/**
	 * Changes directory based on the path supplied as parameter.
	 * 
	 * @param newPath
	 *            the new path to move in the directory structure
	 * @return
	 */
	public Shell cd(final String newPath) {
		if (isAbsolutePath(newPath)) {
			this.path = normalizePath(newPath);
			return this;
		}

		this.path = normalizePath(path + SEPARATOR + newPath);
		return this;
	}

	/**
	 * Returns the current path in the generic directory structure
	 * 
	 * @return <code>String</code> the current path
	 */
	public String path() {
		return path;
	}

	/**
	 * Checks if a given path whether is absolute or not
	 * 
	 * @param pathToCheck
	 *            the path to validate
	 * 
	 * @return <code>true</code> if the path is absolute; <code>false</code>
	 *         otherwise.
	 */
	private boolean isAbsolutePath(String pathToCheck) {
		if (pathToCheck.startsWith(SEPARATOR)) {
			return true;
		}
		return false;
	}

	/**
	 * Normalizes the path based on the directory separator. Also applies
	 * current path to partially qualified (relative) paths and evaluates
	 * relative directory like current(.) and parent(..)
	 * 
	 * @param path
	 *            the path to normalize
	 * @return <code>String</code> a normalized path
	 */
	private String normalizePath(String path) {
		boolean isAbsolute = isAbsolutePath(path);

		List<String> parts = new ArrayList<>();
		for (String part : path.split(SEPARATOR)) {
			if (part.isEmpty() || part.equals(CURRENT_DIR)) {
				// no change - same level
				continue;
			}
			if (part.equals(PARENT_DIR)) {
				if (parts.isEmpty()) {
					if (isAbsolute) {
						// no change - same level
						continue;
					}
				} else {
					if (!parts.get(parts.size() - 1).equals(PARENT_DIR)) {
						// remove the last element to move one level above
						parts.remove(parts.size() - 1);
						continue;
					}
				}
			}
			// add the part component of path
			parts.add(part);
		}

		String prefix = isAbsolute ? SEPARATOR : "";
		return join(prefix, SEPARATOR, parts);
	}

	/**
	 * This helper method joins the different components(prefix, directories and
	 * directory separator) in order to build the full path
	 * 
	 * @param prefix
	 *            the prefix is the first component in the path
	 * @param separator
	 *            the separator for levels in directory structure
	 * @param parts
	 *            the list of parts(directories) to build the path
	 * @return
	 */
	private String join(String prefix, String separator, List<String> parts) {
		Iterator<? extends String> iter = parts.iterator();
		if (!iter.hasNext())
			return ROOT_PATH;
		StringBuilder buffer = new StringBuilder(prefix);
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext())
				buffer.append(separator);
		}

		return buffer.toString();
	}

}

public class Puzzle {

	public static void main(String[] args) {

		final Shell shell = new Shell();
		assert shell.path().equals("/");

		shell.cd("/");
		assert shell.path().equals("/");

		shell.cd("usr/..");
		assert shell.path().equals("/");

		shell.cd("usr").cd("local");
		shell.cd("../local").cd("./");
		assert shell.path().equals("/usr/local");

		shell.cd("..");
		assert shell.path().equals("/usr");

		shell.cd("//lib///");
		assert shell.path().equals("/lib");

	}

}