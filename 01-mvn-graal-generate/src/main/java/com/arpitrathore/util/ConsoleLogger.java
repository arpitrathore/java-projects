package com.arpitrathore.util;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author arathore
 */
@ApplicationScoped
public class ConsoleLogger {

  @ConfigProperty(name = "debug.enabled")
  private String debugEnabled;

  /**
   * Checks if debug mode is enabled and then logs to System.out
   *
   * @param message the message
   * @param args    the placeholder arguments
   */
  public void debug(String message, Object... args) {
    if (debugEnabled.equals("true")) {
      System.out.println(String.format(message, args));
    }
  }

  /**
   * Logs error messages and then exits the program
   *
   * @param error the error message
   * @param args  the placeholder arguments
   */
  public void handleError(String error, Object... args) {
    System.out.println(String.format(error, args));
    System.exit(-1);
  }
}
