package io.github.tmheo.spark

import java.util.Properties

import org.apache.spark.sql.{DataFrame, DataFrameReader}

import scala.collection.JavaConverters._

package object athena {

  implicit class AthenaDataFrameReader(reader: DataFrameReader) {

    def athena(table: String, properties: Properties): DataFrame = {
      val options = properties.asScala
      // explicit url and dbtable should override all
      options += (JDBCOptions.JDBC_TABLE_NAME -> table)
      reader.format("io.github.tmheo.spark.athena").options(options).load()
    }

    def athena(url: String, table: String, properties: Properties): DataFrame = {
      // url
      properties.put(JDBCOptions.JDBC_URL, url)
      athena(table, properties)
    }

    def athena(
                url: String,
                table: String,
                columnName: String,
                lowerBound: Long,
                upperBound: Long,
                numPartitions: Int,
                connectionProperties: Properties): DataFrame = {
      // columnName, lowerBound, upperBound and numPartitions override settings in extraOptions.
      connectionProperties.put(JDBCOptions.JDBC_PARTITION_COLUMN, columnName)
      connectionProperties.put(JDBCOptions.JDBC_LOWER_BOUND, lowerBound.toString)
      connectionProperties.put(JDBCOptions.JDBC_UPPER_BOUND, upperBound.toString)
      connectionProperties.put(JDBCOptions.JDBC_NUM_PARTITIONS, numPartitions.toString)
      athena(url, table, connectionProperties)
    }

  }

}
