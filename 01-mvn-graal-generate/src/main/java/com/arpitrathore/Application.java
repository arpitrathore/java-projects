package com.arpitrathore;

import com.arpitrathore.util.ConsoleLogger;
import com.arpitrathore.util.FileUtil;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import java.util.Arrays;

@QuarkusMain
public class Application implements QuarkusApplication {

  @Inject
  private ConsoleLogger log;

  @Inject
  private FileUtil fileUtil;

  @Override
  public int run(final String... args) throws Exception {
    validateArgs(args);
    var groupId = args[0];
    var artifactId = args[1];
    fileUtil.createMavenProject(groupId, artifactId);
    return 0;
  }

  /**
   * Validates program arguments.
   *
   * @param args program arguments.
   */
  private void validateArgs(final String... args) {
    log.debug("Arguments size: %d, values: %s", args.length, Arrays.asList(args));

    if (args.length < 2) {
      log.handleError("Need 2 arguments: <groupId> <artifactId>");
    }
  }


}
