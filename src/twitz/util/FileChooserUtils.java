package twitz.util;

import java.io.File;

public class FileChooserUtils {
		public static final String jpg = "jpg";
		public static final String jpeg = "jpeg";
		public static final String png = "png";
		public static final String gif = "gif";

		public static String getFileExtension(File f)
		{
			String ext = null;
			String str = f.getName();
			int ind = str.lastIndexOf('.');

			if (ind > 0 && ind < str.length() - 1)
			{
				ext = str.substring(ind + 1).toLowerCase();
			}
			return ext;
		}
	}