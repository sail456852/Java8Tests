package hadoop.hive;

import geohash.GeoHash;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * <dependency>
 * <groupId>org.apache.hive</groupId>
 * <artifactId>hive-jdbc</artifactId>
 * <version>1.2.1</version>
 * </dependency>
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpclient</artifactId>
 * <version>4.3.5</version>
 * </dependency>
 *
 * @author: Eugene_Wang<br />
 * Date: 6/4/2020<br/>
 * Time: 10:42 AM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class HiveConnectorDemo {
    final Logger logger = LogManager.getLogger(HiveConnectorDemo.class);

    public void alterHiveTable(Connection con) throws SQLException, ClassNotFoundException {
        String tableName = "weather";
        Map<String, List<String>> yearMonthsGiven = new HashMap<>();
        yearMonthsGiven.put("2016", new LinkedList<String>() {{
            add("10");
        }});
        yearMonthsGiven.put("2017", new LinkedList<String>() {{
            add("8");
            add("9");
        }});
        alterPartitionByYMD(con, tableName, "2016", yearMonthsGiven.get("2016"));
        alterPartitionByYMD(con, tableName, "2017", yearMonthsGiven.get("2017"));

    }

    private void alterPartitionByYMD(Connection con, String tableName, String year, List<String> monthList) throws SQLException {
        String day;
        String alterPartionSql;
        Calendar cal = Calendar.getInstance();
        for (String month : monthList) {
            cal.set(Integer.parseInt(year), Integer.parseInt(month), 1);
            int endDayOfThatDate = cal.getActualMaximum(Calendar.DATE);
            logger.info(endDayOfThatDate);
            for (int i = 1; i <= endDayOfThatDate; i++) {
                day = "" + i;
                alterPartionSql = "ALTER TABLE " + tableName + " ADD PARTITION(year=" + year + ", month=" + month + ", day=" + day + ") LOCATION 'hdfs:///sandbox/weather/year=" + year + "/month=" + month + "/day=" + day + "'";
                logger.info(alterPartionSql);
                executeHiveSql(con, alterPartionSql);
            }
        }
    }

    /**
     * @param con
     * @param querySql
     * @param columnIndex1
     * @param columnIndex2
     * @param countLimit
     * @throws SQLException //        readHive(con, "select * from hotels", 6, 7, 10);
     */
    public void readHive(Connection con, String querySql, int columnIndex1, int columnIndex2, int countLimit) throws SQLException {
        logger.info(querySql);
        Statement stmt = con.createStatement();
        ResultSet queryFromOriginTable = stmt.executeQuery(querySql);
        int count = 0;
        while (queryFromOriginTable.next() && count < countLimit) {
            double column1 = queryFromOriginTable.getDouble(columnIndex1);
            double column2 = queryFromOriginTable.getDouble(columnIndex2);
            logger.info(column1 + " : " + column2);
            count++;
        }
    }

    public void executeHiveSql(Connection con, String sql) throws SQLException {
        Statement stmt = con.createStatement();
        logger.info(sql);
        boolean execute = stmt.execute(sql);
        logger.info(execute);
    }

    public String geoConvert(Double lat, Double Lon, int geoPrecision) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, Lon, geoPrecision);
        return geoHash.toBase32();
    }
}
