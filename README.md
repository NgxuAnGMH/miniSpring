# miniSpring

https://github.com/YaleGuo/Minis/tree/geek_ioc1

> 注意：
> 1. 右键点击根目录，选择“Add Framework Support”，选择 Maven
> 2. resources文件夹需要右键，Make Directory as “Resources Root”
> 3. src文件夹需要右键，Make Directory as “Sources Root”


## 添加插件

在pom.xml中，并且自己补充多一对`<dependencies></dependencies>`标签

```XML
    <dependencies>
        <!-- https://mvnrepository.com/artifact/dom4j/dom4j -->
        <dependency>
            <groupId>dom4j</groupId>
            <artifactId>dom4j</artifactId>
            <version>1.6.1</version>
        </dependency>
    </dependencies>
```
Reload

## 设置JDK版本

File -》 Project Structure

1. SDK：选择1.8
2. Language Level：8

## Run HelloWorld.java

几个地方有错误

1. 首先是Test1.java中，需要import com.minis.context.ClassPathXmlApplicationContext; 
2. 然后是SimpleBeanFactory中，`<>`因版本变动不再可用，去掉。
3. 最后回到Test1.java，`aService = (AService)ctx.getBean("aservice");`需要使用Try-Catch包起来。

这样就可以跑通了。

