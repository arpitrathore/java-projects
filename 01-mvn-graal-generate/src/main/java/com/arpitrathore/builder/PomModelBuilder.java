package com.arpitrathore.builder;

import java.util.List;
import java.util.Properties;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * @author arathore
 */
public class PomModelBuilder {

  /**
   * Build the pom {@link Model} using given group and artifact id
   *
   * @param groupId    the group id
   * @param artifactId the artifact id
   * @return the pom {@link Model}
   */
  public static Model build(final String groupId, final String artifactId) {
    Model model = new Model();
    model.setModelVersion("4.0.0");
    model.setGroupId(groupId);
    model.setArtifactId(artifactId);
    model.setVersion("0.0.1-SNAPSHOT");
    model.setName(artifactId);

    model.setProperties(buildProperties(groupId, artifactId));
    model.setProfiles(buildProfiles());
    return model;
  }

  private static List<Profile> buildProfiles() {
    final var execution = new PluginExecution();
    execution.setId("build-native");
    execution.setGoals(List.of("compile-no-fork"));
    execution.setPhase("package");

    final Xpp3Dom configuration = buildPluginConfiguration();

    final var plugin = new Plugin();
    plugin.setGroupId("org.graalvm.buildtools");
    plugin.setArtifactId("native-maven-plugin");
    plugin.setVersion("${native-maven-plugin.version}");
    plugin.setExtensions(true);
    plugin.setExecutions(List.of(execution));
    plugin.setConfiguration(configuration);

    final var build = new Build();
    build.setPlugins(List.of(plugin));

    Profile nativeProfile = new Profile();
    nativeProfile.setId("native");
    nativeProfile.setBuild(build);
    return List.of(nativeProfile);
  }

  private static Xpp3Dom buildPluginConfiguration() {
    final var mainClass = new Xpp3Dom("mainClass");
    mainClass.setValue("${main.class}");

    final var imageName = new Xpp3Dom("imageName");
    imageName.setValue("${image.name}");

    final var enabled = new Xpp3Dom("enabled");
    enabled.setValue("true");
    final var agent = new Xpp3Dom("agent");
    agent.addChild(enabled);

    final var configuration = new Xpp3Dom("configuration");
    configuration.addChild(mainClass);
    configuration.addChild(imageName);
    configuration.addChild(agent);
    return configuration;
  }

  private static Properties buildProperties(final String groupId, final String artifactId) {
    final var properties = new Properties();
    properties.setProperty("main.class", groupId + ".Application");
    properties.setProperty("image.name", artifactId);
    properties.setProperty("maven.compiler.source", "21");
    properties.setProperty("maven.compiler.target", "21");
    properties.setProperty("project.build.sourceEncoding", "UTF-8");
    properties.setProperty("native-maven-plugin.version", "0.9.28");
    return properties;
  }
}
