package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class FormGroupDeserializer extends StdDeserializer<BaseFormGroup> { 
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper();
	public FormGroupDeserializer() { 
        this(null); 
    } 
 
    public FormGroupDeserializer(Class<?> vc) { 
        super(vc); 
    }
 
    @Override
    public BaseFormGroup deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
    	BaseFormGroup group = null;
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.has("steps")) {
        	group = new FormSteps();
        	FormStep[] steps = mapper.readValue(node.get("steps").toString(), FormStep[].class);
        	((FormSteps)group).setSteps(steps);
        } else if (node.has("rows")) {
        	group = new FormRows();
        	FormRow[] rows = mapper.readValue(node.get("rows").toString(), FormRow[].class);
        	((FormRows)group).setRows(rows);
        } else if (node.has("tabs")) {
        	group = new FormTabs();
        	FormTab[] tabs = mapper.readValue(node.get("tabs").toString(), FormTab[].class);
        	((FormTabs)group).setTabs(tabs);
        } else {
        	group = new FormRow();
        	FormColumn[] columns = mapper.readValue(node.get("columns").toString(), FormColumn[].class);
        	((FormRow)group).setColumns(columns);
        }
        return group;
    }
}