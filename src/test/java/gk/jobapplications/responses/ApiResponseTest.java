package gk.jobapplications.responses;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseTest {

    @Test
    public void testConstructorWithoutData() {
        ApiResponse<String> response = new ApiResponse<>(200, "Success");

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void testConstructorWithData() {
        String data = "Example data";
        ApiResponse<String> response = new ApiResponse<>(200, "Success", data);

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    public void testSettersAndGetters() {
        ApiResponse<String> response = new ApiResponse<>(200, "Initial");

        response.setStatus(404);
        response.setMessage("Not Found");
        response.setData("Error processing data");

        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getMessage());
        assertEquals("Error processing data", response.getData());
    }

    @Test
    public void testGenericDataType() {
        ApiResponse<Integer> response = new ApiResponse<>(200, "Success", 12345);

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertEquals(12345, response.getData());
    }
}
