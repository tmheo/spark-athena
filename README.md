# AWS Athena Data Source for Apache Spark

This library provides support for reading an [Amazon Athena](https://aws.amazon.com/athena/)
table with [Apache Spark](https://spark.apache.org/) via Athena JDBC Driver.

I developed this library for the following reasons:

Apache Spark is implemented to use PreparedStatement when reading data through JDBC.
However, because Athena JDBC Driver provided by AWS only implements Statement of JDBC Driver Spec and PreparedStatement is not implemented,
Apache Spark can not read Athena data through JDBC.

So I refer to the JDBC data source implementation code in [spark-sql](https://github.com/apache/spark/tree/master/sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/jdbc)
and change it to call Statement of Athena JDBC Driver so that Apache Spark can read Athena data.

**Table of Contents**

- [DataFrame Usage](#dataframe-usage)
  - [Configuration](#configuration)

## DataFrame Usage

You can register a Athena table and run SQL queries against it, or query with the Apache Spark SQL DSL.

```
import io.github.tmheo.spark.athena._

// Read a table from current region with default s3 staging directory.
val users = spark.read.athena("(select * from users)")

// Read a table from current region with s3 staging directory.
val users2 = spark.read.athena("users", "s3://staging_dir")

// Read a table from another region with s3 staging directory.
val users3 = spark.read.athena("users", "us-east-1", "s3://staging_dir")
```

### Configuration

| Option | Description |
| --- | --- |
| `dbtable` | Athena Table or SQL Query |
| `region` | AWS Region. Default value is current region |
| `s3_staging_dir` | The Amazon S3 location to which your query output is written. Default value is **s3://aws-athena-query-results-${accountNumber}-${region}/** |
| `user` | AWS Access Key Id. If you do not specify user, password, the library will try to use [InstanceProfileCredentialsProvider](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/InstanceProfileCredentialsProvider.html). |
| `password` | AWS Secret Access Key. If you do not specify user, password, the library will try to use [InstanceProfileCredentialsProvider](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/InstanceProfileCredentialsProvider.html). |
