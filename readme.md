# simple_orm

## 一、项目目的
  为了学习orm框架底层原理

## 二、项目进度
  已完成数据库逆向工程

## 三、项目结构简介
utils：存放各种工具类  
utils.contants：存放常量接口

## 四、项目使用  
先在resources目录下创建一个```jdbc.properties```   
```
db.driver=com.mysql.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/xxx
db.name=数据库名字  
db.user=root  
db.password=root  
basePackage=org.com.po  //项目包路径  
method.way=lombok\normal
```
**basePackage** ： 
普通工程中，会在当前指定目录 生成类。而在maven工程中，会在src\main\java\指定目录 生成类  

**method.way**  
lombok：lombok模式，Getter和Setter用lombok生成，自动导包+自动加注解 形如  
```java
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class User{
    
}
```
normal：默认模式，形如  
```java
public String getDate() {  
    return date;
}
public void setDate(String date) {
    this.date = date;
}  
```
调用 **```DbUtil.generateClassFromDb();```** 会自动根据数据库生成相应的类
