package org.camunda.bpm.runtime.plugin;


import java.io.InputStream;

/**
 * Used to replace a plugin resource. An implementation of this interface
 * may conditionally replace the content of another plugin's static resource
 * with it's own content.
 *
 */
public interface PluginResourceOverride {

  /**
   * Invoked after a static plugin resource has been resolved.
   *
   * If the implementation decides not to modify the resource, it must return the
   * original input stream passed in as parameter.
   *
   * @param inputStream the content of the resource
   * @param requestInfo contains information about the request.
   * @return the original input stream or a modified input stream or null to remove the resource.
   */
  public InputStream filterResource(InputStream inputStream, RequestInfo requestInfo);

}
