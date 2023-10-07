package io.github.cooperlyt.commons.cloud.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;


/**
 * Exception thrown when an HTTP 4xx is received.
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @see DefaultResponseErrorHandler
 */
public class ResponseDefineException extends ResponseStatusException {

  @Getter
  private DefineStatusCode defineStatusCode;

  @Getter
  private String[] args = new String[0];

  public ResponseDefineException(HttpStatus httpStatus, DefineStatusCode statusCode){
    super(httpStatus);
    this.defineStatusCode = statusCode;
  }

  public ResponseDefineException(ExceptionStatusCode status) {
    super(status.getHttpStatus());
    this.defineStatusCode = status;
  }

  public ResponseDefineException(ExceptionStatusCode status, String... args) {
    super(status.getHttpStatus());
    this.args = args;
    this.defineStatusCode = status;
  }

  public ResponseDefineException(ExceptionStatusCode status, Throwable cause){
    super(status.getHttpStatus(), cause.getMessage(), cause);
    this.defineStatusCode = status;
  }

  public ResponseDefineException(ExceptionStatusCode status, Throwable cause, String... args){
    super(status.getHttpStatus(), cause.getMessage(), cause);
    this.args = args;
    this.defineStatusCode = status;
  }

  public ResponseDefineException(ExceptionStatusCode status, String reason) {
    super(status.getHttpStatus(), reason);
    this.defineStatusCode = status;
  }

  public ResponseDefineException(ExceptionStatusCode status, String reason, String... args) {
    super(status.getHttpStatus(), reason);
    this.args = args;
    this.defineStatusCode = status;
  }

  protected ResponseDefineException(ExceptionStatusCode status, String reason, Throwable cause, String... args) {
    super(status.getHttpStatus(), reason, cause);
    this.args = args;
    this.defineStatusCode = status;
  }


  // Subclasses for specific HTTP status codes
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 400 Bad Request.
//   * @since 5.1
//   */
//
//  public static final class BadRequest extends ResponseDefineException {
//
//    public BadRequest(int errorCode) {
//      super(HttpStatus.BAD_REQUEST, errorCode);
//    }
//
//    public BadRequest(String reason, int errorCode) {
//      super(HttpStatus.BAD_REQUEST, reason, errorCode);
//    }
//
//    public BadRequest( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.BAD_REQUEST, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 401 Unauthorized.
//   * @since 5.1
//   */
//
//  public static final class Unauthorized extends ResponseDefineException {
//
//    public Unauthorized(int errorCode) {
//      super(HttpStatus.UNAUTHORIZED, errorCode);
//    }
//
//    public Unauthorized(String reason, int errorCode) {
//      super(HttpStatus.UNAUTHORIZED, reason, errorCode);
//    }
//
//    public Unauthorized(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.UNAUTHORIZED, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 403 Forbidden.
//   * @since 5.1
//   */
//
//  public static final class Forbidden extends ResponseDefineException {
//
//    public Forbidden(int errorCode) {
//      super(HttpStatus.FORBIDDEN, errorCode);
//    }
//
//    public Forbidden(String reason, int errorCode) {
//      super(HttpStatus.FORBIDDEN, reason, errorCode);
//    }
//
//    public Forbidden(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.FORBIDDEN, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 404 Not Found.
//   * @since 5.1
//   */
//
//  public static final class NotFound extends ResponseDefineException {
//
//    public NotFound(int errorCode) {
//      super(HttpStatus.NOT_FOUND, errorCode);
//    }
//
//    public NotFound(String reason, int errorCode) {
//      super(HttpStatus.NOT_FOUND, reason, errorCode);
//    }
//
//    public NotFound(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.NOT_FOUND, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 405 Method Not Allowed.
//   * @since 5.1
//   */
//
//  public static final class MethodNotAllowed extends ResponseDefineException {
//
//    public MethodNotAllowed(int errorCode) {
//      super(HttpStatus.METHOD_NOT_ALLOWED, errorCode);
//    }
//
//    public MethodNotAllowed(String reason, int errorCode) {
//      super(HttpStatus.METHOD_NOT_ALLOWED, reason, errorCode);
//    }
//
//    public MethodNotAllowed(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.METHOD_NOT_ALLOWED, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 406 Not Acceptable.
//   * @since 5.1
//   */
//
//  public static final class NotAcceptable extends ResponseDefineException {
//
//    public NotAcceptable(int errorCode) {
//      super(HttpStatus.NOT_ACCEPTABLE, errorCode);
//    }
//
//    public NotAcceptable(String reason, int errorCode) {
//      super(HttpStatus.NOT_ACCEPTABLE, reason, errorCode);
//    }
//
//    public NotAcceptable(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.NOT_ACCEPTABLE, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 409 Conflict.
//   * @since 5.1
//   */
//
//  public static final class Conflict extends ResponseDefineException {
//
//    public Conflict(int errorCode) {
//      super(HttpStatus.CONFLICT, errorCode);
//    }
//
//    public Conflict(String reason, int errorCode) {
//      super(HttpStatus.CONFLICT, reason, errorCode);
//    }
//
//    public Conflict(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.CONFLICT, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 410 Gone.
//   * @since 5.1
//   */
//
//  public static final class Gone extends ResponseDefineException {
//
//    public Gone( int errorCode) {
//      super(HttpStatus.GONE, errorCode);
//    }
//
//    public Gone(String reason, int errorCode) {
//      super(HttpStatus.GONE, reason, errorCode);
//    }
//
//    public Gone(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.GONE, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 415 Unsupported Media Type.
//   * @since 5.1
//   */
//
//  public static final class UnsupportedMediaType extends ResponseDefineException {
//
//    public UnsupportedMediaType(int errorCode) {
//      super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, errorCode);
//    }
//
//    public UnsupportedMediaType(String reason, int errorCode) {
//      super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason, errorCode);
//    }
//
//    public UnsupportedMediaType(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 422 Unprocessable Entity.
//   * @since 5.1
//   */
//
//  public static final class UnprocessableEntity extends ResponseDefineException {
//
//    public UnprocessableEntity(int errorCode) {
//      super(HttpStatus.UNPROCESSABLE_ENTITY, errorCode);
//    }
//
//    public UnprocessableEntity(String reason, int errorCode) {
//      super(HttpStatus.UNPROCESSABLE_ENTITY, reason, errorCode);
//    }
//
//    public UnprocessableEntity(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.UNPROCESSABLE_ENTITY, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@link ResponseDefineException} for status HTTP 429 Too Many Requests.
//   * @since 5.1
//   */
//
//  public static final class TooManyRequests extends ResponseDefineException {
//
//    public TooManyRequests(int errorCode) {
//      super(HttpStatus.TOO_MANY_REQUESTS, errorCode);
//    }
//
//    public TooManyRequests(String reason, int errorCode) {
//      super(HttpStatus.TOO_MANY_REQUESTS, reason, errorCode);
//    }
//
//    public TooManyRequests(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.TOO_MANY_REQUESTS, reason, cause, errorCode);
//    }
//  }
//
//
//  // --- 5xx Server Error ---
//
//
//  /**
//   * {@code 500 Internal Server Error}.
//   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
//   */
//  public static final class InternalServerError extends ResponseDefineException {
//    public InternalServerError(int errorCode) {
//      super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode);
//    }
//
//    public InternalServerError(String reason, int errorCode) {
//      super(HttpStatus.INTERNAL_SERVER_ERROR, reason, errorCode);
//    }
//
//    public InternalServerError(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.INTERNAL_SERVER_ERROR, reason, cause, errorCode);
//    }
//  }
//
//
//  /**
//   * {@code 501 Not Implemented}.
//   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.2">HTTP/1.1: Semantics and Content, section 6.6.2</a>
//   */
//
//  public static final class NotImplemented extends ResponseDefineException{
//
//    public NotImplemented(int errorCode) {
//      super(HttpStatus.NOT_IMPLEMENTED, errorCode);
//    }
//
//    public NotImplemented(String reason, int errorCode) {
//      super(HttpStatus.NOT_IMPLEMENTED, reason, errorCode);
//    }
//
//    public NotImplemented(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.NOT_IMPLEMENTED, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@code 502 Bad Gateway}.
//   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.3">HTTP/1.1: Semantics and Content, section 6.6.3</a>
//   */
//  public static final class BadGateway extends ResponseDefineException{
//    public BadGateway(int errorCode) {
//      super(HttpStatus.BAD_REQUEST, errorCode);
//    }
//
//    public BadGateway(String reason, int errorCode) {
//      super(HttpStatus.BAD_REQUEST, reason, errorCode);
//    }
//
//    public BadGateway(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.BAD_REQUEST, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@code 503 Service Unavailable}.
//   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.4">HTTP/1.1: Semantics and Content, section 6.6.4</a>
//   */
//  public static final class ServiceUnavailable extends ResponseDefineException{
//    public ServiceUnavailable(int errorCode) {
//      super(HttpStatus.SERVICE_UNAVAILABLE, errorCode);
//    }
//
//    public ServiceUnavailable(String reason, int errorCode) {
//      super(HttpStatus.SERVICE_UNAVAILABLE, reason, errorCode);
//    }
//
//    public ServiceUnavailable(String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.SERVICE_UNAVAILABLE, reason, cause, errorCode);
//    }
//  }
//  /**
//   * {@code 504 Gateway Timeout}.
//   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.5">HTTP/1.1: Semantics and Content, section 6.6.5</a>
//   */
//  public static final class GatewayTimeout extends ResponseDefineException{
//    public GatewayTimeout( int errorCode) {
//      super(HttpStatus.GATEWAY_TIMEOUT, errorCode);
//    }
//
//    public GatewayTimeout( String reason, int errorCode) {
//      super(HttpStatus.GATEWAY_TIMEOUT, reason, errorCode);
//    }
//
//    public GatewayTimeout( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.GATEWAY_TIMEOUT, reason, cause, errorCode);
//    }
//  }
//  /**
//   * {@code 505 HTTP Version Not Supported}.
//   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.6">HTTP/1.1: Semantics and Content, section 6.6.6</a>
//   */
//
//  public static final class HttpVersionNotSupported extends ResponseDefineException{
//    public HttpVersionNotSupported( int errorCode) {
//      super(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, errorCode);
//    }
//
//    public HttpVersionNotSupported( String reason, int errorCode) {
//      super(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, reason, errorCode);
//    }
//
//    public HttpVersionNotSupported( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, reason, cause, errorCode);
//    }
//  }
//  /**
//   * {@code 506 Variant Also Negotiates}
//   * @see <a href="https://tools.ietf.org/html/rfc2295#section-8.1">Transparent Content Negotiation</a>
//   */
//  public static final class VariantAlsoNegotiates extends ResponseDefineException{
//    public VariantAlsoNegotiates( int errorCode) {
//      super(HttpStatus.VARIANT_ALSO_NEGOTIATES, errorCode);
//    }
//
//    public VariantAlsoNegotiates( String reason, int errorCode) {
//      super(HttpStatus.VARIANT_ALSO_NEGOTIATES, reason, errorCode);
//    }
//
//    public VariantAlsoNegotiates( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.VARIANT_ALSO_NEGOTIATES, reason, cause, errorCode);
//    }
//  }
//  /**
//   * {@code 507 Insufficient Storage}
//   * @see <a href="https://tools.ietf.org/html/rfc4918#section-11.5">WebDAV</a>
//   */
//  public static final class InsufficientStorage extends ResponseDefineException{
//    public InsufficientStorage( int errorCode) {
//      super(HttpStatus.INSUFFICIENT_STORAGE, errorCode);
//    }
//
//    public InsufficientStorage( String reason, int errorCode) {
//      super(HttpStatus.INSUFFICIENT_STORAGE, reason, errorCode);
//    }
//
//    public InsufficientStorage( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.INSUFFICIENT_STORAGE, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@code 508 Loop Detected}
//   * @see <a href="https://tools.ietf.org/html/rfc5842#section-7.2">WebDAV Binding Extensions</a>
//   */
//  public static final class LoopDetected extends ResponseDefineException{
//    public LoopDetected( int errorCode) {
//      super(HttpStatus.LOOP_DETECTED, errorCode);
//    }
//
//    public LoopDetected( String reason, int errorCode) {
//      super(HttpStatus.LOOP_DETECTED, reason, errorCode);
//    }
//
//    public LoopDetected( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.LOOP_DETECTED, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@code 509 Bandwidth Limit Exceeded}
//   */
//  public static final class BandwidthLimitExceeded extends ResponseDefineException{
//    public BandwidthLimitExceeded( int errorCode) {
//      super(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, errorCode);
//    }
//
//    public BandwidthLimitExceeded( String reason, int errorCode) {
//      super(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, reason, errorCode);
//    }
//
//    public BandwidthLimitExceeded( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@code 510 Not Extended}
//   * @see <a href="https://tools.ietf.org/html/rfc2774#section-7">HTTP Extension Framework</a>
//   */
//  public static final class NotExtended extends ResponseDefineException{
//    public NotExtended( int errorCode) {
//      super(HttpStatus.NOT_EXTENDED, errorCode);
//    }
//
//    public NotExtended( String reason, int errorCode) {
//      super(HttpStatus.NOT_EXTENDED, reason, errorCode);
//    }
//
//    public NotExtended( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.NOT_EXTENDED, reason, cause, errorCode);
//    }
//  }
//
//  /**
//   * {@code 511 Network Authentication Required}.
//   * @see <a href="https://tools.ietf.org/html/rfc6585#section-6">Additional HTTP Status Codes</a>
//   */
//  public static final class NetworkAuthenticationRequired extends ResponseDefineException{
//    public NetworkAuthenticationRequired( int errorCode) {
//      super(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, errorCode);
//    }
//
//    public NetworkAuthenticationRequired( String reason, int errorCode) {
//      super(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, reason, errorCode);
//    }
//
//    public NetworkAuthenticationRequired( String reason, Throwable cause, int errorCode) {
//      super(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, reason, cause, errorCode);
//    }
//  }


}