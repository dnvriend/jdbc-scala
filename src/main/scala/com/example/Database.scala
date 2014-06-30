package com.example

import javax.sql.DataSource

import org.apache.commons.dbcp.BasicDataSource
import scalikejdbc.ConnectionPool

trait JdbcConfig {
  def username: String
  def password: String
  def driverClassName: String
  def url: String
  def maxActive: Int = 2
  def maxIdle: Int = 1
  def initialSize: Int = 1
}

trait H2JdbcConfig extends JdbcConfig {
  override def username: String = "sa"
  override def password: String = ""
  override def driverClassName: String = "org.h2.Driver"
  override def url: String = "jdbc:h2:mem:db"
}

trait ScalikeConnection extends JdbcConfig {
  Class.forName(driverClassName)
  ConnectionPool.singleton(url, username, password)
}

trait DbcpConnection extends JdbcConfig {
  lazy val dataSource: DataSource = {
    val ds = new BasicDataSource
    ds.setDriverClassName(driverClassName)
    ds.setUrl(url)
    ds.setUsername(username)
    ds.setPassword(password)
    ds.setMaxActive(maxActive)
    ds.setMaxIdle(maxIdle)
    ds.setInitialSize(initialSize)
    ds
  }
}


