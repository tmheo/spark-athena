package io.github.tmheo.spark

import java.util.Properties

import com.amazonaws.athena.jdbc.shaded.com.amazonaws.regions.Regions
import org.apache.spark.sql.{DataFrame, DataFrameReader}

import scala.collection.JavaConverters._

package object athena {

  implicit class AthenaDataFrameReader(reader: DataFrameReader) {

    def athena(table: String): DataFrame = {
      reader.format("io.github.tmheo.spark.athena")
        .option(JDBCOptions.JDBC_TABLE_NAME, table)
        .load
    }

    def athena(table: String, region: String, s3StatingDir: String): DataFrame = {
      reader.format("io.github.tmheo.spark.athena")
        .option(JDBCOptions.JDBC_TABLE_NAME, table)
        .option("region", region)
        .option("s3_staging_dir", s3StatingDir)
        .load
    }

    def athena(table: String, s3StatingDir: String): DataFrame = {
      athena(table, Regions.getCurrentRegion.getName, s3StatingDir)
    }

    def athena(table: String, properties: Properties): DataFrame = {
      val options = properties.asScala
      options += (JDBCOptions.JDBC_TABLE_NAME -> table)
      reader.format("io.github.tmheo.spark.athena").options(options).load
    }

  }

}
