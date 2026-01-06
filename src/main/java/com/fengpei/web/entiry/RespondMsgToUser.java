package com.fengpei.web.entiry;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "returnsms")
public class RespondMsgToUser {

    @JacksonXmlProperty(localName = "returnstatus")
    public String returnstatus;

    @JacksonXmlProperty(localName = "message")
    public String message;

    @JacksonXmlProperty(localName = "remainpoint")
    public Integer remainpoint;

    @JacksonXmlProperty(localName = "taskID")
    public Long taskID;

    @JacksonXmlProperty(localName = "successCounts")
    public Integer successCounts;
}