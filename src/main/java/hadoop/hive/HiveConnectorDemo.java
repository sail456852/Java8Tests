package hadoop.hive;

import geohash.GeoHash;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.*;

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
    public String driverName = "org.apache.hive.jdbc.HiveDriver";

    public void alterHiveTable() throws SQLException, ClassNotFoundException {
        Class.forName(driverName);
        Connection con = DriverManager.getConnection("jdbc:hive2://sandbox-hdp.hortonworks.com:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2", "hive", "Gbhnjmk,");

        String tableName = "weather";
        String year = "2000";
        String month = "1"; // 8 9 10
        String day = "1";
        String alterPartionSql = "ALTER TABLE " + tableName + " ADD PARTITION(year=" + year + ", month=" + month + ", day=" + day + ") LOCATION 'hdfs://sandbox-hdp.hortonworks.com/sandbox/weather/year=" + year + "/month=" + month + "/day=" + day + "";
        logger.info(alterPartionSql);
        executeHiveSql(con, alterPartionSql);

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
