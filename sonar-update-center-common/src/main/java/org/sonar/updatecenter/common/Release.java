/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2011 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.updatecenter.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.google.common.collect.Sets.newHashSet;

public class Release implements Comparable<Release> {

  private Artifact artifact;
  private Version version;
  private String description;
  private String downloadUrl;
  private String changelogUrl;
  private Set<Release> outgoingDependencies;
  private Set<Release> incomingDependencies;
  /**
   * from oldest to newest sonar versions
   */
  private SortedSet<Version> requiredSonarVersions = new TreeSet<Version>();
  private Date date;

  public Release(Artifact artifact, Version version) {
    this.artifact = artifact;
    this.version = version;
    this.outgoingDependencies = newHashSet();
    this.incomingDependencies = newHashSet();
  }

  public Release(Artifact artifact, String version) {
    this(artifact, Version.create(version));
  }

  public Artifact getArtifact() {
    return artifact;
  }

  public Version getVersion() {
    return version;
  }

  public Release setVersion(Version version) {
    this.version = version;
    return this;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public Release setDownloadUrl(String s) {
    this.downloadUrl = s;
    return this;
  }

  public String getFilename() {
    return StringUtils.substringAfterLast(downloadUrl, "/");
  }

  public SortedSet<Version> getRequiredSonarVersions() {
    return requiredSonarVersions;
  }

  public boolean supportSonarVersion(Version version) {
    return requiredSonarVersions.contains(version);
  }

  public Release addRequiredSonarVersions(Version... versions) {
    if (versions != null) {
      requiredSonarVersions.addAll(Arrays.asList(versions));
    }
    return this;
  }

  public Release addRequiredSonarVersions(String... versions) {
    if (versions != null) {
      for (String v : versions) {
        requiredSonarVersions.add(Version.create(v));
      }
    }
    return this;
  }

  public Version getLastRequiredSonarVersion() {
    if (requiredSonarVersions != null && !requiredSonarVersions.isEmpty()) {
      return requiredSonarVersions.last();
    }
    return null;
  }

  public Version getMinimumRequiredSonarVersion() {
    if (requiredSonarVersions != null && !requiredSonarVersions.isEmpty()) {
      return requiredSonarVersions.first();
    }
    return null;
  }

  public Date getDate() {
    return date;
  }

  public Release setDate(Date date) {
    this.date = date;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Release setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getChangelogUrl() {
    return changelogUrl;
  }

  public Release setChangelogUrl(String changelogUrl) {
    this.changelogUrl = changelogUrl;
    return this;
  }

  public Set<Release> getOutgoingDependencies() {
    return outgoingDependencies;
  }

  public Release addOutgoingDependency(Release required) {
    outgoingDependencies.add(required);
    return this;
  }

  public Set<Release> getIncomingDependencies() {
    return incomingDependencies;
  }

  public Release addIncomingDependency(Release required) {
    incomingDependencies.add(required);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Release release = (Release) o;

    if (!artifact.equals(release.artifact)) {
      return false;
    }
    if (!version.equals(release.version)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = artifact.hashCode();
    result = 31 * result + version.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("version", version)
        .append("downloadUrl", downloadUrl)
        .append("changelogUrl", changelogUrl)
        .append("description", description)
        .toString();
  }

  public int compareTo(Release o) {
    return getVersion().compareTo(o.getVersion());
  }
}
