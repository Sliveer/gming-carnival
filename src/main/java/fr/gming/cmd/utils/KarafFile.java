package fr.gming.cmd.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KarafFile {

	private static final Logger LOGGER = Logger.getLogger("KarafFile");

	private static final String dataPathName = System.getProperty("karaf.data");
	private static final String etcPathName = System.getProperty("karaf.etc");

	private static final Path dataPath = Paths.get(dataPathName);
	private static final Path etcPath = Paths.get(etcPathName);

	public static Path dataPath() {
		return dataPath;
	}

	public static Path etcPath() {
		return etcPath;
	}

	public static void overrideFileInData(byte[] content, String... file) {
		overrideFile(content, dataPathName, file);
	}

	public static void overrideFileInData(List<String> content, String... file) {
		overrideFile(content.stream().map(String::valueOf).reduce("", (i1, i2) -> i1 + "\r\n" + i2).getBytes(),
				dataPathName, file);
	}

	public static void overrideFileInEtc(byte[] content, String... file) {
		overrideFile(content, etcPathName, file);
	}

	public static void overrideFile(byte[] content, String base, String... others) {
		Path filePath = Paths.get(base, others);
		try {
			LOGGER.log(Level.INFO, "Saving dynamic content to file " + filePath);
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, content);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error writing to file " + filePath, e);
			System.out.println("!ERROR! writing to file " + filePath);
		}
	}

	public static List<String> readAllLinesFileInEtc(String... file) throws IOException {
		return readAllLinesFile(etcPathName, file);
	}

	public static List<String> readAllLinesFileInData(String... file) throws IOException {
		return readAllLinesFile(dataPathName, file);
	}

	public static List<String> readAllLinesFile(String base, String... others) throws IOException {
		return Files.readAllLines(Paths.get(base, others));
	}

	public static boolean existsInEtc(String... file) {
		return exists(etcPathName, file);
	}

	public static boolean existsInData(String... file) {
		return exists(dataPathName, file);
	}

	public static boolean exists(String base, String... others) {
		return Files.exists(Paths.get(base, others));
	}

	public static void deleteInEtc(String... file) {
		delete(etcPathName, file);
	}

	public static void deleteInData(String... file) {
		delete(dataPathName, file);
	}

	public static void delete(String base, String... others) {
		Path filePath = Paths.get(base, others);
		try {
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error deleting file " + filePath, e);
			System.out.println("!ERROR! writing to file " + filePath);
		}
	}

	public static Map<String, List<String>> listFilesUnderDir(String base, String... others) {
		Path dirPath = Paths.get(base, others);
		try (Stream<Path> paths = Files.list(dirPath)) {

			return paths.collect(Collectors.toMap(
					f -> removeFileExtension(f.getFileName().toString(), true), f -> {
						try {
							return Files.readAllLines(f);
						} catch (IOException e) {
							LOGGER.log(Level.SEVERE, "Error reading file " + f, e);
							System.out.println("!ERROR! reading file " + f);
						}
						return new ArrayList<>();
					}));
		} catch (
				IOException e) {
			LOGGER.log(Level.SEVERE, "Error searching files under " + dirPath, e);
			System.out.println("!ERROR! searching files under " + dirPath);
		}
		return new HashMap<>();
	}

	public static String removeFileExtension(String filename, boolean removeAllExtensions) {
		if (filename == null || filename.isEmpty()) {
			return filename;
		}

		String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
		return filename.replaceAll(extPattern, "");
	}

	public static Map<String, List<String>> listFilesUnderDirInEtc(String... dir) {
		return listFilesUnderDir(etcPathName, dir);
	}

}
