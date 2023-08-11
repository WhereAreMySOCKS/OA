<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/17424059-b397-4c93-bd8a-93f84c6615ad"># 基于SpringBoot的办公办理系统

## 特性

* 使用Mybatis-plus
* [前端代码点这里](https://github.com/WhereAreMySOCKS/oa-front)

## 项目结构

    .
    ├── .idea                   # idea自动生成的配置文件
    ├── common                  # 存放util类
    │   ├── common-util         # 通用工具包，包括自定义结果集Result,授权工具JWTHelper，MD5加密工具包等    
    │   │   ├── src             
    │   │   ├── target
    │   │   └── pom.xml
    │   ├── service-util        # 业务工具包，包括自定义异常处理工具包exception,调试工具包knife4j，数据库中间件MybatisPlus配置类
    │   │   ├── src
    │   │   ├── target
    │   │   └── pom.xml
    │   ├── spring-security     # 安全工具包，包括Spring-security配置类,用户信息获取工具类LoginUserInfoHelper，自定义过滤器等
    │   │   ├── src
    │   │   ├── target
    │   │   └── pom.xml
    │   └── pom.xml
    ├── model                   # 存放实体类，包括数据库各个表的实体类和VO类
    │   ├── src
    │   ├── target
    │   └── pom.xml
    ├── service-oa              # 业务逻辑，涉及三个模块，用户权限模块，流程审批模块，微信服务模块
    │   ├── src
    │   ├── target
    │   └── pom.xml
    ├── pom.xml  
    └── README.md  

## 模块介绍

自定义结果集Result： 将页面结果通过静态类Result统一封装，便于前端处理。

Jwt：使用Jwt对客户端消息进行验证。获取用户基本信息（userID和userName）都需要验证token。登陆完成后，使用Jwt创建token对信息签名。客户端向服务器发送信息时必须附带该token，服务器通过验证签名token以保证信息不被篡改。使用session也可以实现验证token的功能，但session的缺点就是在分布式架构下，无法通过一个站点上的网页跳转至另一个站点上网页。

spring-securit： 为了防止用户绕开登陆而直接访问页面，使用spring-security进行授权和认证。

## 功能介绍
认证授权：
* 登陆：通过于数据库中的信息比对完成登陆，数据库中保存的密码经过MD5加密，因此在比对时使用到了MD5工具类中的encrypt方法。
<img width="807" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/f91107d3-c9e8-4c32-924f-79e4cde9bec5">

    
* 分配权限：对于不同的用户角色，其权限不同，显示的页面也不同。查找数据库中记录的权限，封装成HashMap返回给前端，前端根据数据动态生成页面。这一步的难点是如何将权限信息封装成层次结构，具体操作见MenuHelper工具类。
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/c0f604fb-65d8-4bd9-90be-2315e52115fc">
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/6494e28f-7d1d-4725-b3e1-8c3f2a42477b">

    
* 角色管理和用户管理：基础的增删改查操作。分页查询用户功能，通过前端传递page和pageSize两个参数，封装在MybatisPlus提供的page类中，调用service的page方法实现分页查询。不熟悉分页操作的可以回顾一下[Mybatis的分页查询](https://blog.csdn.net/huweiliyi/article/details/107910959)。
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/3d3d666e-9d3f-43af-a348-f6e6245a1603">
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/44921836-aea1-4bbe-9685-4476ff34ada5">


* 菜单管理：除了增删改查之外，重点是涉及菜单的层级关系的操作。
<img width="1469" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/ac0e818c-7200-40cd-8eb9-7e260485633f">

审批控制：
* 模版类型：审批模版类型的展示，主要是增删改查和分页查询。
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/ea9d5cb2-a8d9-4ec9-8c8b-0856c4317853">

* 审批模版：对已有的审批模版进行增删改查操作。这一部分使用了Activit7实现流程控制。同时在创建新的审批模版时，需要上传Activit7的流程定义文件。
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/d714e153-a155-4ea5-a570-fbac62d28305">

* 自定义审批模版
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/c28d2ab3-d165-4047-a074-6437c6adfb8f">

* 自动生成审批模版前端页面
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/8bf32ba0-b710-4498-806e-083a4c7d25ed">
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/a32aa0cf-a513-4b09-bd8a-006257262063">

* 上传Activiti流程设计
<img width="1470" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/482f8557-499c-41cf-a71d-e3340d2c5a02">

* 审批流程：审批流程在微信公众号上完成，通过微信公众号实现审批的提交，查看，通过，驳回，待处理，已完成等功能。

微信接口： 由于微信公众号需要认证才能进行接口开发，故本项目目前仅在微信公众号测试号上进行功能测试。
* 微信公众号菜：对公众号上的菜单进行发布和删除操作。
  
* 微信账号绑定手机号：绑定完成后即可通过服务器的验证，进行审批功能。

发起审批流程

<img width="230" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/97eeb94a-51b1-46c9-9796-d909e355bf19">
<img width="230" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/f767b435-d0bc-405f-8ccb-614b5493b92d">
<img width="230" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/eb8bf237-d575-4ef7-9f95-5b96df414912">
<img width="230" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/8f3119e9-0f2f-45eb-bfea-4d10c0e043e2">

审批人拒绝审批

<img width="230" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/424b3ab0-a7cb-4ce8-b3e0-78700ab7dcb5">
<img width="230" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/797feaa9-20e0-4b2c-b1a2-b0b2d74da05a">
<img width="230" alt="image" src="https://github.com/WhereAreMySOCKS/OA/assets/45731564/cb6086f9-f062-48dc-ad2b-136bab890b60">


    
