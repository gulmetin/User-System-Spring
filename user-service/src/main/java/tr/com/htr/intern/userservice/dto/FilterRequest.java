package tr.com.htr.intern.userservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FilterRequest {
    //desc,asc
    private String sort="";
    //id,username,firstName,lastName
    private String sortBy="";
    //username,firstName,lastName,phoneNumber
    private String filter="";
    //operator: according to equals-like-permission
    private String operator="";
    private String value="";


}
