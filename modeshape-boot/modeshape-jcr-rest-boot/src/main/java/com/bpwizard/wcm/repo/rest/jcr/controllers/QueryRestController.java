package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.common.SystemFailureException;
import org.modeshape.jcr.JcrI18n;
import org.modeshape.jcr.api.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ColumnValue;
import com.bpwizard.wcm.repo.rest.jcr.model.QueryStatement;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.bpwizard.wcm.repo.validation.ValidateString;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(QueryRestController.BASE_URI)
@Validated
public class QueryRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(QueryRestController.class);
	
	public static final String BASE_URI = "/wcm/api/queryStatement";

	@Autowired
	private WcmRequestHandler wcmRequestHandler;

	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QueryStatement[]> loadQueryStatements(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@RequestParam(name="filter", defaultValue = "") String filter,
		    @RequestParam(name="sort", defaultValue = "asc") 
			@ValidateString(acceptedValues={"asc", "desc"}, message="Sort order can only be asc or desc")
			String sortDirection,
		    @RequestParam(name="pageIndex", defaultValue = "0") int pageIndex,
		    @RequestParam(name="pageSize", defaultValue = "3") @Min(3) @Max(10) int pageSize,
			HttpServletRequest request) throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}

		try {
			QueryStatement[] queryStatements = this.wcmRequestHandler.loadQueryStatements(repository, workspace, request);
			if ("asc".equals(sortDirection)) {
				Arrays.sort(queryStatements);
			} else if ("desc".equals(sortDirection)) {
				Arrays.sort(queryStatements, Collections.reverseOrder());
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.OK).body(queryStatements);
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		}
	}
	
	@PostMapping(path = "/query", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> executeQueryStatement(
			@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String repositoryName = query.getRepository();
		try {
			QueryManager qrm = this.repositoryManager.getSession(repositoryName, query.getWorkspace()).getWorkspace().getQueryManager();
			com.bpwizard.wcm.repo.rest.jcr.model.QueryResult result = new com.bpwizard.wcm.repo.rest.jcr.model.QueryResult();
			String queryStatement = query.getQuery();
			if (!StringUtils.hasText(queryStatement)) {
				queryStatement = this.doGetQueryStatement(query, request);
			}
			javax.jcr.query.Query jcrQuery = qrm.createQuery(queryStatement, Query.JCR_SQL2);
			QueryResult jcrResult = jcrQuery.execute();
			String columnNames[] = jcrResult.getColumnNames();
			RowIterator iterator = jcrResult.getRows();
			List<Map<String, ColumnValue>> rowValues = new ArrayList<>();
			result.setRows(rowValues);
			while (iterator.hasNext()) {
				Row row = iterator.nextRow();
				Map<String, ColumnValue> rowValue = new HashMap<>();
				rowValues.add(rowValue);
				for (String columnName: columnNames) {
					if (null != row.getValue(columnName)) {
						rowValue.put(columnName, getColumnValue(row.getValue(columnName)));
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.traceEntry();
			}
			return ResponseEntity.ok(result);
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_QUERY_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createQueryStatement(
			@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		this.wcmRequestHandler.createQueryStatement(query, request);
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveQuery(@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_QUERY_PATH_PATTERN, query.getLibrary(), query.getName());
			String repositoryName = query.getRepository();
			
			ObjectNode qJson = (ObjectNode) query.toJson();
			try {
				QueryManager qrm = this.repositoryManager.getSession(repositoryName, WcmConstants.DEFAULT_WS).getWorkspace().getQueryManager();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.query, baseUrl, repositoryName, WcmConstants.DEFAULT_WS, path, qJson);
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_QUERY_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	

	//TODO: failed scenario - all or nothing
	@PostMapping(path= "/{repositoryName}/{workspaceName}/loadqueries",
		    consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
		    produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadQueries( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		@RequestParam("file") MultipartFile file ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		this.wcmRequestHandler.loadQueries(request, repository, workspace, file.getInputStream());
	    	logger.debug("Exiting ...");
	    	return ResponseEntity.status(HttpStatus.CREATED).build();
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
    	} 
    }
	
	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeQuery(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String wcmPath,
  			HttpServletRequest request) { 
  		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
  		String baseUrl = RestHelper.repositoryUrl(request);
  		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
  		try {
	  		
  			this.wcmItemHandler.deleteItem(WcmEvent.WcmItemType.query, baseUrl, repository, workspace, absPath);
  			
  	  		if (logger.isDebugEnabled()) {
  				logger.traceExit();
  			}
  	  		
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}

  	};
	
	private String doGetQueryStatement(QueryStatement query, HttpServletRequest request) throws RepositoryException {
		String baseUrl = RestHelper.repositoryUrl(request);
		String queryAbsPath = String.format(WcmConstants.NODE_QUERY_PATH_PATTERN, query.getLibrary(), query.getName());
		RestNode queryNode = (RestNode) this.wcmItemHandler.item(baseUrl, query.getRepository(), query.getWorkspace(),
				queryAbsPath, WcmConstants.READ_DEPTH_TWO_LEVEL);
		String queryStatement = null;
		for (RestNode node: queryNode.getChildren()) {
			if (WcmConstants.WCM_ITEM_ELEMENTS.equals(node.getName())) {
				for (RestProperty restProperty : node.getJcrProperties()) {
					if ("query".equals(restProperty.getName())) {
						queryStatement = restProperty.getValues().get(0);
						break;
					} 
				}
				break;
			}
		}
		return queryStatement;
	
	}
	
	private ColumnValue getColumnValue(Value jcrValue) throws RepositoryException {
		ColumnValue columnValue = new ColumnValue();
		columnValue.setType(jcrValue.getType());
		
        switch (jcrValue.getType()) {
            case PropertyType.STRING:
            	columnValue.setValue(jcrValue.getString());
            	break;
            case PropertyType.BINARY:
                columnValue.setValue(jcrValue.getBinary());
            	break;
            case PropertyType.BOOLEAN:
            	columnValue.setValue(jcrValue.getBoolean());
             	break;
            case PropertyType.DOUBLE:
            	columnValue.setValue(jcrValue.getDouble());
             	break;
            case PropertyType.LONG:
            	columnValue.setValue(jcrValue.getLong());
             	break;
            case PropertyType.DECIMAL:
            	columnValue.setValue(jcrValue.getDecimal());
             	break;
            case PropertyType.DATE:
            case PropertyType.PATH:
            case PropertyType.NAME:
            case PropertyType.REFERENCE:
            case PropertyType.WEAKREFERENCE:
            case org.modeshape.jcr.api.PropertyType.SIMPLE_REFERENCE:
            case PropertyType.URI:
                columnValue.setValue(jcrValue.getString());
                break;
            default:
                throw new SystemFailureException(JcrI18n.invalidPropertyType.text(jcrValue.getType()));
        }
        
		return columnValue;
	}
}
