package hadoop.hive;

import geohash.GeoHash;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
    final static Logger logger = LogManager.getLogger(HiveConnectorDemo.class);
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName(driverName);
        Connection con = DriverManager.getConnection("jdbc:hive2://sandbox-hdp.hortonworks.com:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2", "hive", "Gbhnjmk,");
        Statement stmt = con.createStatement();
//        String testSql = "insert into table eugene_weather values ('11.00','22','01','88','22','2020-08-08','xsdfs')";
//        stmt.execute(testSql);

        readHive(con, stmt, "eugene_weather_origin", "select lat, lng, avg_tmpr_f, avg_tmpr_c, wthr_date from default.");
    }


    private static void readHive(Connection con, Statement stmt, String sourceTableName, String sql) throws SQLException {
        String selectSql = sql + sourceTableName;
        logger.info("querySql: " + selectSql);
        ResultSet queryFromOriginTable = stmt.executeQuery(selectSql);
        int count = 0;
        while (queryFromOriginTable.next() && count < 10) {
            double latitude = queryFromOriginTable.getDouble(1);
            double longitude = queryFromOriginTable.getDouble(2);
            String geoHashValue = geoConvert(latitude, longitude, 3);
            logger.info(longitude  +  " : " + latitude);
            logger.info("geo hash value: " + geoHashValue);
            count++;
        }
    }

    public static String geoConvert(Double lat, Double Lon, int geoPrecision) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, Lon, geoPrecision);
        return geoHash.toBase32();
    }
}
