package model.sets;

/**
 * @author Peggy Li
 */

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Loader {
	
	

	@SuppressWarnings("rawtypes")
	public static Class[] getLoadedClasses(String filepath, Class superclass) {

		Set<URL> urls = new HashSet<URL>();
		iterate(new File(filepath), urls);
		
		ArrayList<Class> loadedClasses = new ArrayList<Class>();
		loadClasses(urls, loadedClasses);
		ArrayList<Class> classesToKeep = filter(loadedClasses, superclass);
		
		Class[] classes = new Class[classesToKeep.size()];
		for (int i = 0; i < classesToKeep.size(); i++) {
			classes[i] = classesToKeep.get(i);
		}
		return classes;
	}
	

	/**
	 * Filter out classes that are not a non-abstract subclass of PredefinedSet
	 * @param classes
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static ArrayList<Class> filter (ArrayList<Class> classes, Class parent) {
		ArrayList<Class> filtered = new ArrayList<Class>();
		for (Class c : classes) {
			if (!parent.isAssignableFrom(c) || Modifier.isAbstract(c.getModifiers())) {
				filtered.add(c);
			}
		}
		return filtered;
	}
	

	private static void loadClasses (Collection<URL> urls, Collection<Class> loadedClasses)
	{
		URL[] array = new URL[urls.size()];
		array = urls.toArray(array);

		URLClassLoader loader = new URLClassLoader(array);

		for (URL url : loader.getURLs())
		{
			try
			{
				String s = url.toURI().getPath();
				File dir = new File(s);

				for (File f : dir.listFiles())
				{
					String path = f.getAbsolutePath();

					int dotclass = path.lastIndexOf(".class");
					if (dotclass == -1) continue;

					path = path.substring(0, dotclass);

					Class c = loadClassFromPath(path, 0, loader);

					if (c != null)
					{
						loadedClasses.add(c);
					}

				}
			}
			catch (URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
	}


	/**
	 * Try loading class NOTE ClassLoader uses pathname in <package.classname>
	 * format when loading class to allow for multi-level packaging, recursively
	 * check parent directory until local root file is reached EXAMPLE
	 * /user/name/desktop/vooga/src/package/subpackage/target(.class) First try
	 * to load target, then subpackage.target... package.subpackage.target until
	 * user.name...target is reached, then base case is reached
	 * 
	 * @param path full file path for a class file
	 * @param level number of file hierarchy levels relative to class file
	 * @param loader ClassLoader object
	 * @return class loaded using given path name
	 */
	private static Class loadClassFromPath (String path, int level, ClassLoader loader)
	{

		Class c;

		int backslash = path.lastIndexOf("/");
		for (int i = 0; i < level; i++)
		{
			path =
					path.substring(0, backslash) + "." +
							path.substring(backslash + 1);

			/**
			 * BASE CASE occurs once root directory is reached recursively
			 */
			if (path.contains("/") == false)
			{
				return null;
			}
			backslash = path.lastIndexOf("/");
		}

		String subpath = path.substring(backslash + 1);

		try
		{
			c = loader.loadClass(subpath);
		}

		catch (ClassNotFoundException e)
		{
			c = loadClassFromPath(path, level + 1, loader);
		}
		catch (java.lang.NoClassDefFoundError err)
		{
			c = loadClassFromPath(path, level + 1, loader);
		}

		return c;
	}




	/**
	 * Checks each file to see whether name ends in .class and adds PARENT
	 * directory of each class file to list Recursively checks files within a
	 * directory
	 */
	private static void iterate (File file, Set<URL> urls)
	{
		if (file.isFile() && file.getName().endsWith(".class"))
		{
			try
			{
				urls.add(file.getParentFile().toURI().toURL());
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}

		// file is a directory
		File[] subfiles = file.listFiles();
		if (subfiles == null) return;

		for (File sub : subfiles)
		{
			iterate(sub, urls);
		}
	}

}
