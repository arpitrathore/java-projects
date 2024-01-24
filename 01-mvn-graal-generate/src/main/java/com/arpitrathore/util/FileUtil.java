package com.arpitrathore.util;

import com.arpitrathore.builder.PomModelBuilder;
import com.arpitrathore.log.SysLogger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

/**
 * @author arathore
 */
@ApplicationScoped
public class FileUtil {

  private static final String APPLICATION_JAVA_FILE_NAME = "/Application.java.txt";
  private static final String GIT_IGNORE_FILE_NAME = "/gitignore.txt";

  @Inject
  private SysLogger log;

  /**
   * Generates maven project under directory specified by artifact id.
   *
   * @param groupId     maven group id.
   * @param projectRoot artifact id or project root directory
   */
  public void createMavenProject(final String groupId, final String projectRoot) {
    try {
      checkIfProjectDirectoryExist(projectRoot);
      final var applicationFileDirectory =
          projectRoot + "/src/main/java/" + convertGroupIdToFilePath(groupId);
      Files.createDirectories(Path.of(applicationFileDirectory));
      Files.createDirectories(Path.of(projectRoot + "/src/main/resources"));

      createApplicationFile(groupId, applicationFileDirectory);
      createPomFile(groupId, projectRoot);
      createGitIgnoreFile(projectRoot);
    } catch (IOException e) {
      log.handleError("Failed to create maven directory structure. Error: %s", e.getMessage());
    }
  }

  /**
   * Creates the Application file with main method.
   *
   * @param groupId           maven group id.
   * @param mainFileDirectory Directory where Application.java file has to be created.
   * @throws IOException If unable to create
   */
  private void createApplicationFile(final String groupId, final String mainFileDirectory)
      throws IOException {
    final Path javaMailFilePath = Path.of(mainFileDirectory + "/Application.java");
    Files.createFile(javaMailFilePath);
    final var applicationSourceCode = new String(
        readFileFromResource(APPLICATION_JAVA_FILE_NAME)).formatted(groupId);
    Files.write(javaMailFilePath, applicationSourceCode.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Creates the pom file.
   *
   * @param groupId     the group id.
   * @param projectRoot the artifact id.
   * @throws IOException
   */
  private void createPomFile(final String groupId, final String projectRoot) throws IOException {
    final var pomModel = PomModelBuilder.build(groupId, projectRoot);
    try {
      MavenXpp3Writer writer = new MavenXpp3Writer();
      writer.write(new FileOutputStream(projectRoot + "/pom.xml"), pomModel);
    } catch (Exception e) {
      log.handleError("Failed to write to pom file. Error: %s", e.getMessage());
    }
  }

  /**
   * Create .gitignore file
   *
   * @param projectRoot the project root directory
   * @throws IOException
   */
  private void createGitIgnoreFile(final String projectRoot) throws IOException {
    final var gitIgnoreFileContent = readFileFromResource(GIT_IGNORE_FILE_NAME);
    Files.write(Path.of(projectRoot + "/.gitignore"), gitIgnoreFileContent);
  }

  /**
   * Checks if project directory exists
   *
   * @param artifactId
   */
  public void checkIfProjectDirectoryExist(final String artifactId) {
    final var directory = new File(artifactId);
    if (directory.exists()) {
      log.handleError("%s already exists", artifactId);
    }

    if (directory.isFile()) {
      log.handleError("%s is a file", artifactId);
    }
  }

  /**
   * Reads file from resource folder as byte array
   *
   * @param fileName the file name
   * @return
   */
  private byte[] readFileFromResource(String fileName) {
    try {
      return getClass().getResourceAsStream(fileName).readAllBytes();
    } catch (Exception e) {
      log.handleError("Failed to read file from resources folder: %s", fileName);
    }
    return new byte[0];
  }

  /**
   * Converts dot separated group id to directory path separated by slash
   *
   * @param groupId the group id
   * @return directory path
   */
  private String convertGroupIdToFilePath(String groupId) {
    final var path = groupId.replaceAll("\\.", "/");
    log.debug("Group id: %s and converted path: %s ", groupId, path);
    return path;
  }
}
