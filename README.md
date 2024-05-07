# 基于 Docker 的在线判题系统后端
## 技术栈
- [x] Spring Cloud
- [x] Mybatis-Plus
- [x] Hutool
- [ ] RabbitMQ
## 亮点
- [x] 代码沙箱基于 Docker 容器，编译和执行均在容器中进行，大大增强安全性。

- [x] 多语言支持方便，需要增加新语言时，只需要在配置文件中新增对应的镜像名称、编译命令（编译型编程语言）和执行命令即可。

- [x] 微服务架构，服务解耦，便于维护