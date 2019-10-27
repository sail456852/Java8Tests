package interview;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: yz<br/>
 * Date: 10/23/2019<br/>
 * Time: 9:15 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
@Data
public class People {
    static  SimpleDateFormat sd  =  new SimpleDateFormat("dd/MM/yyyy");
    private String firstName;
    private String lastName;
    private String sex;
    private Date birthDay;
    private String color;

    @Override
    public String toString(){
        String bday = sd.format(birthDay);
        StringJoiner joiner = new StringJoiner(",");
        StringJoiner joinedString = joiner.add(firstName).add(lastName).add(sex).add(bday).add(color);
        return joinedString.toString();
    }
}
