### 简介
本项目可以作为Flink 代码的中文注释版本，其中很多类添加了个人的想法注释，建立这个项目的初衷是为了更好的了解Flink源码底层实现，和相关原理细节，在这个做一个记录，便于以后的回顾和学习。

### 代码导读
1. DataStream流作业提交流程：RemoteStreamEnvironment.execute()开始看起
2. JobManager收到作业提交消息开始构建ExecutionGraph:JobManager.hanleMessage,case SubmitJob消息处理
3. TaskManager 

### 关联类信息
1. org.apache.flink.api.common.typeutils.base包 -> Flink 自定义序列化框架
