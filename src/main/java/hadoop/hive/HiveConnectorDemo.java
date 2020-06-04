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

        updateWeather(con, stmt, "eugene_weather_origin", "eugene_weather", " set geohash = ? where lat = ? and lng = ? and geohash is null", "select lat, lng, avg_tmpr_f, avg_tmpr_c, wthr_date from eugene.");
        updateWeather(con, stmt, "eugene_hotel_origin", "eugene_hotel", " set geohash = ? where Latitude = ? and Longitude = ? and geohash is null", "select Latitude, Longitude from eugene.");
    }

    private static void updateWeather(Connection con, Statement stmt, String sourceTableName, String targetTableName, String updateClause, String queryClause) throws SQLException {
        String selectSql = queryClause + sourceTableName;
        logger.info("querySql: " + selectSql);
        ResultSet queryFromOriginTable = stmt.executeQuery(selectSql);


        String updateSql = "update eugene." + targetTableName + updateClause;
        logger.info("updateSql : " + updateSql);
        PreparedStatement updatePreStatement = con.prepareStatement(updateSql);

        while (queryFromOriginTable.next()) {
            double latitude = queryFromOriginTable.getDouble(1);
            double longitude = queryFromOriginTable.getDouble(2);
            String geoHashValue = geoConvert(latitude, longitude);
//            logger.info("geo from query: " + latitude + " ,  " + longitude);
            // update eugene.eugene_weather set geohash = 1 where lat is null and lng is null and geohash is null;
            logger.info("geo hash value: " + geoHashValue);
            updatePreStatement.setString(1, geoHashValue);
            updatePreStatement.setDouble(2, latitude);
            updatePreStatement.setDouble(3, longitude);
            logger.info("executing update");
            int r = updatePreStatement.executeUpdate();
            logger.info(" " + r);
        }
    }

    public static String geoConvert(Double lat, Double Lon) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, Lon, 5);
        return geoHash.toBase32();
    }
}
