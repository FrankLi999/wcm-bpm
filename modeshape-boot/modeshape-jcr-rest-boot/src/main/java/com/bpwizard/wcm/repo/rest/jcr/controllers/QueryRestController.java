package com.bpwizard.wcm.repo.rest.jcr.controllers;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.jcr.api.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.QueryStatement;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(QueryRestController.BASE_URI)
@Validated
public class QueryRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(QueryRestController.class);
	
	public static final String BASE_URI = "/wcm/api/queryStatement";

	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public QueryStatement getQueryStatement(
			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String nodePath, 
			HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode queryNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					nodePath, 2);
			QueryStatement queryStatement = this.toQueryStatement(queryNode);
			queryStatement.setRepository(repository);
			queryStatement.setWorkspace(workspace);
			queryStatement.setLibrary(nodePath.split("/", 5)[3]);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return queryStatement;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}		
	}

	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createQueryStatement(
			@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = query.getRepository();
			ObjectNode qJson = (ObjectNode) query.toJson();
			try {
				QueryManager qrm = this.repositoryManager.getSession(repositoryName, DEFAULT_WS).getWorkspace().getQueryManager();
				javax.jcr.query.Query jcrQuery = qrm.createQuery(query.getQuery(), Query.JCR_SQL2);
				QueryResult jcrResult = jcrQuery.execute();
				String columnNames[] = jcrResult.getColumnNames();
				if (columnNames != null && columnNames.length > 0) {
					ArrayNode valueArray = JsonUtils.creatArrayNode();
					for (String value : columnNames) {
						valueArray.add(value);
					}
					qJson.set("bpw:columns", valueArray);	
				}
				RowIterator iterator = jcrResult.getRows();
				while (iterator.hasNext()) {
					Row row = iterator.nextRow();
					System.out.println("==================================================");
					System.out.println("element.jcr:path" + row.getValue("element.jcr:path"));
					System.out.println("element.jcr:name" + row.getValue("element.jcr:name"));
					System.out.println("element.bpw:value" + row.getValue("element.bpw:value"));
					System.out.println("content.jcr:name" + row.getValue("content.jcr:name"));
					System.out.println("content.jcr:path" + row.getValue("content.jcr:path"));
					System.out.println("content.jcr:score" + row.getValue("content.jcr:score"));
					System.out.println("element.jcr:score" + row.getValue("element.jcr:score"));
					System.out.println("==================================================");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// javax.jcr.query.qom.QueryObjectModel qom = null
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_QUERY_PATH_PATTERN, query.getLibrary(), query.getName());
			
			this.itemHandler.addItem(baseUrl,  repositoryName, DEFAULT_WS, path, qJson);
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveQuery(@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_QUERY_PATH_PATTERN, query.getLibrary(), query.getName());
			String repositoryName = query.getRepository();
			ObjectNode qJson = (ObjectNode) query.toJson();
			try {
				QueryManager qrm = this.repositoryManager.getSession(repositoryName, DEFAULT_WS).getWorkspace().getQueryManager();
				javax.jcr.query.Query jcrQuery = qrm.createQuery(query.getQuery(), Query.JCR_SQL2);
				QueryResult jcrResult = jcrQuery.execute();
				String columnNames[] = jcrResult.getColumnNames();
				if (columnNames != null && columnNames.length > 0) {
					ArrayNode valueArray = JsonUtils.creatArrayNode();
					for (String value : columnNames) {
						valueArray.add(value);
					}
					qJson.set("bpw:columns", valueArray);	
				}
				RowIterator iterator = jcrResult.getRows();
				while (iterator.hasNext()) {
					Row row = iterator.nextRow();
					System.out.println("==================================================");
					System.out.println("element.jcr:path" + row.getValue("element.jcr:path"));
					System.out.println("element.jcr:name" + row.getValue("element.jcr:name"));
					System.out.println("element.bpw:value" + row.getValue("element.bpw:value"));
					System.out.println("content.jcr:name" + row.getValue("content.jcr:name"));
					System.out.println("content.jcr:path" + row.getValue("content.jcr:path"));
					System.out.println("content.jcr:score" + row.getValue("content.jcr:score"));
					System.out.println("element.jcr:score" + row.getValue("element.jcr:score"));
					System.out.println("==================================================");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.itemHandler.updateItem(baseUrl, repositoryName, DEFAULT_WS, path, qJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, qJson);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	private QueryStatement toQueryStatement(RestNode node) {
		QueryStatement queryStatement = new QueryStatement();
		queryStatement.setName(node.getName());
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				queryStatement.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:query".equals(restProperty.getName())) {
				queryStatement.setQuery(restProperty.getValues().get(0));
			} 
		}
		return queryStatement;
	}
}
