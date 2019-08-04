# docker 入门以及常用命令

安装:
# 在WIN系统下载软件为了在WIN上运作docker
用 docker  for windows installer，安装完成需要电脑打开虚拟化，经过测试，这个始终会与VMware有冲突，于是我采用Vmware来安装一个Linux环境，在Linux下学习使用docker
[WIN系统的docker安装包下载，注意WIN平台上与VMware 有冲突二选一，](https://download.docker.com/win/stable/Docker%20for%20Windows%20Installer.exe)


# 在WIN系统下，尝试使用 Kitematic 可视化管理 Docker
Kitematic 是什么
Kitematic是一个 Docker GUI 工具，它可以更快速、更简单的运行Docker，现在已经支持 Mac 和 Windows。Kitematic 目前在 Github 上开源，而它也早在 2015 年就已经被 Docker 收购。Kitematic 完全自动化了 Docker 安装和设置过程，并提供了一个直观的图形用户接口（GUI）来运行 Docker。通过 GUI 你可以非常容易的创建、运行和管理你的容器，不需要使用命令行或者是在 Docker CLI 和 GUI之间来回切换；同时也可以方便的修改环境变量、查看日志以及配置数据卷等。


#在linux系统上：
1.搭建docker环境
需要linux系统必须是centOS7以上

准备安装环境，执行一下命令：
yum install epel-release –y
yum clean all
yum list

2.安装
yum install docker-io –y

3.启动
systemctl start docker

4.测试docker是否成功
docker info

使用别人现成的镜像（可以理解为就是个软件）直接跑，类似git， pull 镜像 然后传入参数 run即可

构建自己的镜像，然后自己玩自己的，则需要使用到配置文件做一些事情后才能愉快的玩耍




```$text.

docker使用
编译方式：dockerfile
Linux镜像：centos7
jdk：jdk1.8.0_144
这里要构建一个基于centos7和jdk1.8的镜像。



1. 查找centos7镜像
docker search centos

2. 下载centos7镜像
docker pull centos:7

3. 查看本地下载好的centos7镜像命令：
docker images
显示：

[root@localhost ~]# docker images
REPOSITORY                        TAG                 IMAGE ID            CREATED             SIZE
mcr.microsoft.com/mssql/server    2017-latest         d9b9b96627b7        3 days ago          1.36 GB
app1                              0.0.1               c9e45db07fbf        3 days ago          791 MB
centos7_jdk8                      8                   24f471ff99c3        3 days ago          773 MB
docker.io/nginx                   latest              e445ab08b2be        11 days ago         126 MB
docker.io/mysql                   latest              990386cbd5c0        2 months ago        443 MB
docker.io/tomcat                  latest              27600aa3d7f1        2 months ago        463 MB
docker.io/redis                   latest              a4fe14ff1981        2 months ago        95 MB
docker.io/centos                  7                   9f38484d220f        4 months ago        202 MB
docker.io/exoplatform/sqlserver   latest              f2445f289489        7 months ago        1.43 GB


4. 编辑docker的约定默认名称：Dockerfile
如果使用自定义名称，build的时候情加参数来指定dockerfile，命令： -f  自定义文件名称

Dockerfile既是docker的配置文件，里面的内容做一个简单的介绍:
到这里我们对于代码的修改就完成了,下面我们在idea的命令行执行maven命令,或者点击idea的maven插件中的docker:build
  

命令：例子：新建一个文件，这里命名为 jdkdockerfile
vi jdkdockerfile

（jdkdockerfile 中的内容：）

FROM centos:7
MAINTAINER lamymay lamymay@outlook.com
ADD jdk-8u181-linux-x64.tar.gz /usr/local/

ENV JAVA_HOME /usr/local/jdk/jdk1.8.0_181
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin

ADD docker-0.0.1-SNAPSHOT.jar /data



注意：
1、这里使用的镜像是上面下载的centos镜像； 
2、编译镜像所需要的文件情拷贝到当前目录（配置文件所在目录，压缩包不用解压，jdk的话强烈建议使用rpm的包，以免配置由错，构建出来的镜像中java环境是失败的）jdk拷贝到dockerfile同级目录，如果在其它目录拷贝的时候可能出现找不到目录错误； 
3、另外使用ADD指令会直接对 jdk-8u181-linux-x64.tar.gz  进行解压缩，不用再单独的tar解压jdk了

FROM                    指明当前镜像继承的基镜像,编译当前镜像时候会自动下载基镜像
MAINTAINER      当前镜像的作者和邮箱,使用空格隔开
ADD<src><dest>从当前工作目录复制文件到镜像目录中并重新命名，SRC可以是URL，也可以是压缩包，默认是当前目录
COPY                    只复制文件，和ADD类似，但是不支持URL和解压缩
CMD                     只能一个，容器启动后执行的命令
EXPOSE                声明容器运行时对外提供服务的端口，监听的端口号
WORKDIR           容器工作路径
ENV                      环境变量
RUN                     构建阶段执行的命令，在当前镜像上执行Linux命令,这里我执行了2个run指令,第二个run指令是为了解决容器和宿主机时间不一致的问题
VOLUME              挂载目录
ENTRYPOINT      让容器像一个可执行程序一样运行




5. 使用Dockerfile构建镜像

构建镜像的时候可以先清除本地缓存，命令：
docker images -q --filter "dangling=true" | xargs -t --no-run-if-empty docker rmi


构建语法：docker build -t  <构建出来的镜像的名称>:<版本> <路径> -f <配置文件名称，默认的名称的话，可不写>



6. 删除刚刚生成的镜像images，通过image的名称和tag来指定删除谁 
 语法：docker rmi <imagename:tag>
 语法：docker rmi  -f <imagename:tag>
 
命令例子
docker rmi centos7_jdk_8u181_linux_x64:20190731

6.1 注意如果是正在运行的镜像，则：先删除容器后删除仓库中镜像

先查看正在运行中的镜像
docker ps -a
会显示：
[root@localhost data]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                    PORTS               NAMES
8a6b5cc82262        redis               "docker-entrypoint..."   2 months ago        Exited (0) 2 months ago                       tender_euler
d1cb80c01dc4        redis               "docker-entrypoint..."   2 months ago        Exited (1) 2 months ago                       zen_darwin
83a03933c8c0        redis               "docker-entrypoint..."   2 months ago        Exited (1) 2 months ago                       eager_panini
8e5651fe0454        redis               "docker-entrypoint..."   2 months ago        Exited (0) 2 months ago                       agitated_lovelace

在删除: 语法docker rm  <CONTAINER ID >
命令例子：
docker rm  8a6b5cc82262


7. docker创建好的镜像导出,docker镜像保存save、加载load
查看要要保存的镜像的ID
保存镜像：语法 docker save  <imageName>  -o  <outputFile>
docker save  centos7_jdk_8u181_linux_x64  -o  /data/docker/centos7_jdk_8u181_linux_x64

至此，基础镜像已经完成了，并且怎么拷贝使用都交给你了，下一步就是把我们的app部署到镜像中，运行这个镜像，把我们的服务跑起来

命令：
docker build -t  centos7_jdk8:8 . 
#文件名称是自定义的 需要指定一下，二参数中有个‘.’ 含义是当前文件夹，请勿遗漏
docker build -t  centos7_jdk8:8 . -f jdkdockerfile

9. 运行
先拉取镜像，然后启动
docker pull <镜像名字>:<版本>
说明：pull默认会从本地仓库来拉取，不加版本号，会拉取最新的版本


https://www.cnblogs.com/lsgxeva/p/8746644.html
https://www.jianshu.com/p/59e0a8828b3b
启动容器语法: docker run <相关参数> <镜像 ID> <初始命令>
命令：
docker run -i -t -v /root/software/:/mnt/software/ 4cbf48630b46 /bin/bash
docker run -p 8001:8080  -d  <imgaeName>
docker run -p 8001:8080  -d app1:0.0.1
docker run -p 9000:8001 -di  app1:0.0.1
docker run  centos7jdk8:8 /bin/bash -it
docker run --name myNginx --rm -p 80:80 nginx

-p 端口映射，指定端口号 第一个端口号8001为容器内部的端口号，第二个8080为外界访问的端口号，将容器内的8001端口号映射到外部的8080端口号
如果想用域名来访问的话，需要把数据库连接改为服务器的ip+数据库端口号，并且命令改为：docker run -d -p 80:80 mystory

--rm 临时容器
--name 指定名称
--ip 指定容器ip
-h  设置hostname
-u  指定运行用户
-e  设置环境变量
-d  表示在后台运行
-v  设置挂在目录（通过分割两边文件，两百年都要写绝对路径）



#查看日志
 docker logs myNginx


显示所有容器IP地址：
docker inspect --format='{{.Name}} - {{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)

常用方法有两种
docker inspect 容器ID | grep IPAddress

进入docker
docker exec -it myNginx bash

/etc/nginx


docker ps -a
docker stop


docker history nginx

overlay2 实际存储位置

/var/liv/docker/overlay2/镜像ID
diff    镜像改动的文件目录
merged 挂载合并文件
work 工作目录

容器中的文件操作的实质： 

文件增加    在diff目录中新增
文件修改    在镜像中拷贝至diff目录，然后在修改
文件删除   添加一个表示至diff目录

Kubernetes 是一个开源的，用于管理云平台中多个主机上的容器化的应用

```

------------------------

```$text

# curl 测试一下
curl -X GET http://127.0.0.1:9000/info
curl -X GET \
  http://127.0.0.1:8001/info \
  -H 'cache-control: no-cache'



```


附：


FROM ：表示使用 Jdk8 环境 为基础镜像，如果镜像不是本地的会从 DockerHub 进行下载
MAINTAINER ：指定维护者的信息
VOLUME ：VOLUME 指向了一个/tmp的目录，由于 Spring Boot 使用内置的Tomcat容器，Tomcat 默认使用/tmp作为工作目录。这个命令的效果是：在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
ADD ：拷贝文件并且重命名(前面是上传jar包的名字，后面是重命名)
RUN ：每条run指令在当前基础镜像执行，并且提交新镜像
ENTRYPOINT ：为了缩短 Tomcat 的启动时间，添加java.security.egd的系统属性指向/dev/urandom作为 ENTRYPOINT





ERROR
# docker run -p 8001:8080  -d app1
WARNING: IPv4 forwarding is disabled. Networking will not work.
647888a6dad3801347a4c29fcd07c1793ffa9c2c47380f53401c968063eeb802

启动docker时映射到宿主机时出现/usr/bin/docker-current: Error response from daemon: oci runtime error: container_linux.go:235: 
starting container process caused "exec: \"java\": executable file not found in $PATH".



-----------
启动docker时映射到宿主机时出现 /usr/bin/docker-current: Error response from daemon: driver failed……的解决方案

https://www.clxz.top/2019/03/31/111040/







-----------



state 和 status 的区别：

一、意思不同
state的意思是状态，Status不仅有状态也有身份的意思。

二、使用环境不同
state：比较常用，各种状态都可以用它，但是它更着重于一种心理状态或者物理状态。

status 用在人的身上一般是其身份和地位，状态时指的是政治和商业。



state：比较常用，各种状态都可以用它，但是它更着重于一种心理状态或者物理状态。
Status：用在人的身上一般是其身份和地位，作“状态，情形”讲时，多指政治和商业。
state倾向于condition，是一种延续性的状态。status常用于描述一个过程中的某阶段（phase），类似于C语言中枚举型变量某一个固定的值，这个值属于一个已知的集合。
比如淘宝买家问卖家“我的网购现在是什么状况？”
这个问题的背景是讲话双方都清楚，交易状态有“买家选购”“买家已付款”“卖家已发货”“买家已签收”或者有“买家已
投诉”等等状态。这些状态描述一件事情发展过程中的不同阶段。而且，这些阶段的先后顺序也是双方默许的。
所以在这里可以问“What's the status of my purchase?”，此处用state不太贴切，如果硬用上去从语感上可能听着别扭。
说物态变化用state再恰当不过。如果说一个物质的四种状态，可以说“solid state”，但如果你说“solid status”，第
一，这两个词的组合不像是描述物态，更像是在说“确定的状况（solid产生歧义‘确定的/确凿的’）”；第二，这个说法即
使不被误解，也需要事先约定一组物态变化顺序，比如把这个物质从固态开始加热然后电离，可能先后经历固态、液态、气态、等离子态这四个阶段。类似先定义枚举，然后引用的方式。
扩展：
ajax中readyState，statusText，onreadystatechange，window.status怎么一会state一会是status都晕乎了
state所指的状态，一般都是有限的、可列举的，status则是不可确定的。
比如
readyState -- 就那么四五种值
statusText -- 描述性的文字，可以任意
onreadystatechange -- 那么四五种值之间发生变化
window.status -- 描述性的文字，可以任意
来个形象的比方，你体重多少公斤，属于status，但说你体重属于偏瘦、正常还是偏胖，那就是state.


