
package com.microservice.orchestration.demo.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * ErrorMessage
 * <p>
 * An Error message
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "code",
    "message",
    "details"
})
public class ErrorMessage implements Serializable
{

    /**
     * A short description of the error code.
     * 
     */
    @JsonProperty("code")
    @JsonPropertyDescription("A short description of the error code.")
    private String code;
    /**
     * A detailed description of the error.
     * 
     */
    @JsonProperty("message")
    @JsonPropertyDescription("A detailed description of the error.")
    private String message;
    /**
     * A more detailed description of the error.
     * 
     */
    @JsonProperty("details")
    @JsonPropertyDescription("A more detailed description of the error.")
    private String details;
    private final static long serialVersionUID = 448671363026538201L;

    /**
     * A short description of the error code.
     * 
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     * A short description of the error code.
     * 
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    public ErrorMessage withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * A detailed description of the error.
     * 
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * A detailed description of the error.
     * 
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorMessage withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * A more detailed description of the error.
     * 
     */
    @JsonProperty("details")
    public String getDetails() {
        return details;
    }

    /**
     * A more detailed description of the error.
     * 
     */
    @JsonProperty("details")
    public void setDetails(String details) {
        this.details = details;
    }

    public ErrorMessage withDetails(String details) {
        this.details = details;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("message", message).append("details", details).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(details).append(code).append(message).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ErrorMessage) == false) {
            return false;
        }
        ErrorMessage rhs = ((ErrorMessage) other);
        return new EqualsBuilder().append(details, rhs.details).append(code, rhs.code).append(message, rhs.message).isEquals();
    }

}
