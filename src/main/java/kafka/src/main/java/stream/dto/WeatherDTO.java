package stream.dto;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.<br/>
 *
 * @author: Eugene_Wang<br />
 * Date: 6/16/2020<br/>
 * Time: 11:57 AM<br/>
 * To change this template use File | Settings | File Templates.
 */
@Data
public class WeatherDTO{
    private double lng;
    private double lat;
    private double avg_tmpr_f;
    private double avg_tmpr_c;
    private String wthr_date;
    private int year;
    private int month;
    private int day;
}
