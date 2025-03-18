import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ro.mpp2024.model.ComputerRepairRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ComputerRepairRequestTest {

        @Test
        @DisplayName("First test")
        public void testExample(){
            ComputerRepairRequest crr = new ComputerRepairRequest();
            assertEquals( "", crr.getOwnerName());
            assertEquals( "", crr.getOwnerAddress());

        }

        @Test
        @DisplayName("Test Example")
        public void testExample2(){
            assertEquals(2, 2, "Numerele ar trebui sa fie egale!" );
        }
    }
      

