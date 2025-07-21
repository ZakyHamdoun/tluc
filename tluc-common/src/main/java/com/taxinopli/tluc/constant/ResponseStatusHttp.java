package com.taxinopli.tluc.constant;

import org.springframework.stereotype.Component;

@Component
public class ResponseStatusHttp {
    private ResponseStatusHttp() {
        throw new IllegalStateException("Utility class");
    }

    public static final String HTTP_STATUS_OK = "HTTP 200 - OK";
    public static final String HTTP_STATUS_CREATED = "HTTP 201 - Created";
    public static final String HTTP_BAD_REQUEST = "HTTP 400 - Bad Request";
    public static final String HTTP_UNAUTHORIZED = "HTTP 401 - Unauthorized";
    public static final String HTTP_FORBIDDEN = "HTTP 403 - Forbidden";
    public static final String HTTP_NOT_FOUND = "HTTP 404 - Not Found";
    public static final String HTTP_CONFLICT = "HTTP 409 - Conflict";
    public static final String HTTP_INTERNAL_SERVER_ERROR = "HTTP 500 - Internal Server Error";
    public static final String HTTP_NOT_IMPLEMENTED = "HTTP 501 - Not Implemented";
    public static final String HTTP_BAD_GATEWAY = "HTTP 502 - Bad Gateway";
    public static final String HTTP_GATEWAY_TIMEOUT = "HTTP 504 - Gateway Timeout";
    public static final String HTTP_HTTP_VERSION_NOT_SUPPORTED = "HTTP Version Not Supported";
    public static final String HTTP_INSUFFICIENT_STORAGE = "HTTP Insufficient Storage";
    public static final String HTTP_UNSUPPORTED_MEDIA_TYPE = "HTTP Unsupported Media Type";
    public static final String HTTP_UNSUPPORTED_ENCODING = "HTTP Unsupported Encoding";
    public static final String HTTP_INTERNAL_ERROR = "HTTP Internal Error";
}
