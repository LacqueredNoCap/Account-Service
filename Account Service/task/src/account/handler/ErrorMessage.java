package account.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ErrorMessage {
    private Object timestamp;
    private Object status;
    private Object error;
    private Object message;
    private Object path;
}
