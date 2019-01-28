# kyy-container-deploy-plugin

Jenkins 容器服务部署插件

## 开发

### 本地运行

`mvn hpi:run`

Tips:

- 需要在 `src/main/resources/testData` 目录添加 cert.json，用于容器部署测试，正式环境不需要。

### 打包

`mvn package`

插件会生成在 target 目录，后缀为 `.hpi`

## TODO:

- [ x ] 蓝绿部署失败时，更改构建任务为失败状态。

## Changelog

[查看](changelog)
