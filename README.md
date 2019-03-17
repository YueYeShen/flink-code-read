### 简介
本项目可以作为Flink 代码的中文注释版本，其中很多类添加了个人的想法注释，建立这个项目的初衷是为了更好的了解Flink源码底层实现，和相关原理细节，在这个做一个记录，便于以后的回顾和学习。

### 代码导读
1. DataStream流作业提交流程：RemoteStreamEnvironment.execute()开始看起
2. JobManager收到作业提交消息开始构建ExecutionGraph:JobManager.hanleMessage,case SubmitJob消息处理
3. TaskManager 

### Flink自定义序列化
1. IntSerializer -> 4字节
2. FloatSerializer -> 4字节
3. EnumSerializer -> 4字节
4. LongSerializer -> 8字节
5. ShortSerializer -> 2字节
6. DoubleSerializer -> 8字节
7. BooleanSerializer -> 1字节
8. ByteSerializer -> 1字节
