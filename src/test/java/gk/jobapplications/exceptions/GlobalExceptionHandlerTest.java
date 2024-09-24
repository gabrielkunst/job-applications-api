package gk.jobapplications.exceptions;

import gk.jobapplications.responses.ApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleResourceAlreadyExistsException() {
        String errorMessage = "Resource already exists";
        ResourceAlreadyExistsException exception = Mockito.mock(ResourceAlreadyExistsException.class);
        Mockito.when(exception.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleResourceAlreadyExistsException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testHandleResourceNotFoundException() {
        String errorMessage = "Resource not found";
        ResourceNotFoundException exception = Mockito.mock(ResourceNotFoundException.class);
        Mockito.when(exception.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleResourceNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testHandleGeneralException() {
        String errorMessage = "Internal server error";
        Exception exception = new Exception(errorMessage);

        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleGeneralException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
