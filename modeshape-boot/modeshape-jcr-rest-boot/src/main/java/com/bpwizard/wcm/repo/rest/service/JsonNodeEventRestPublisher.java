package com.bpwizard.wcm.repo.rest.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.jcr.RepositoryException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;

@Component
public class JsonNodeEventRestPublisher {
	@Autowired
	private RestTemplate restTemplate;
	
	public void syndicateCndTypes(WcmEvent wcmEvent, Syndicator syndicator, String token) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		// headers.setBearerAuth(token);
		headers.set(HttpHeaders.AUTHORIZATION, token);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		
		body.add("file", this.getTemplFile("cnd_", wcmEvent.getContent()));
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		String serverUrl = String.format("http://%s:%s/wcm/api/import/%s/%s/nodetypes", syndicator.getCollector().getHost(), syndicator.getCollector().getPort(), syndicator.getRepository(), syndicator.getWorkspace());
		restTemplate.postForEntity(serverUrl, requestEntity, Void.class);
	}
	
	public void syndicate(WcmEvent wcmEvent, Syndicator syndicator, String token) throws RepositoryException, IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		// headers.setBearerAuth(token);
		headers.set(HttpHeaders.AUTHORIZATION, token);
		MultiValueMap<String, Object> wcmItemBody = new LinkedMultiValueMap<>();
		wcmItemBody.add("id", wcmEvent.getId());
		wcmItemBody.add("library", wcmEvent.getLibrary());
		wcmItemBody.add("nodePath", wcmEvent.getNodePath());
		wcmItemBody.add("operation", wcmEvent.getOperation().name());
		wcmItemBody.add("itemType", wcmEvent.getItemType().name());		
		wcmItemBody.add("content", this.getTemplFile("wcmItem_", wcmEvent.getContent()));
		String serverUrl = String.format("http://%s:%s/wcm/api/collector/%s/%s/jsonItem", syndicator.getCollector().getHost(), syndicator.getCollector().getPort(), syndicator.getRepository(), syndicator.getWorkspace());
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>( wcmItemBody, headers);
		restTemplate.postForEntity(serverUrl, requestEntity, Void.class);
	}
	
	public Resource getTemplFile(String prefix, byte[] content) throws IOException {
        Path cndFile = Files.createTempFile(prefix + System.currentTimeMillis(), ".txt");
        Files.write(cndFile, content);
        return new FileSystemResource(cndFile.toFile());
    }
}
