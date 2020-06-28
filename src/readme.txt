simple_orm

一、项目目的
  为了学习orm框架底层原理

二、项目进度
  已完成数据库逆向工程

三、项目结构简介
  constant包下存放 各种常量定义
  utils包下存放 各种工具类
    utils包下的CommonUtil 代表通用的工具类

四、项目使用
    先在resources目录下创建一个jdbc.properties
        basePackage=目标类路径 eg:org.com.po
        db.driver=com.mysql.jdbc.Driver
        db.url=jdbc:mysql://localhost:3306/xxx
        db.name=数据库名字
        db.user=root
        db.password=root
        method.way=lombok\normal
            lombok：lombok模式，Getter和Setter用lombok生成，自动导包+自动加注解
            normal：默认模式，形如
                            public String getDate() {
                          		return date;
                          	}
                          	public void setDate(String date) {
                          		this.date = date;
                          	}
    调用 "DbUtil.generateClassFromDb();" 会自动根据数据库生成相应的类

