package tr.com.htr.intern.userservice.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {
    //    @JsonProperty
    private String notificationType;
    //    @JsonProperty
    private Date createdAt;
    //    @JsonProperty
    private String message;

//    @Override
//    public String toString() {
//        return "Notification{" +
//                "notification='" + notification + '\'' +
//                ", createdAt=" + createdAt +
//                ", seen=" + seen +
//                ", message='" + message + '\'' +
//                '}';
//    }
}
