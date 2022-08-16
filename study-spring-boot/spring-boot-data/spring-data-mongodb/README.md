## 简介
MongoDB是一个跨平台，面向文档的数据库，提供高性能，高可用性和易于扩展。MongoDB是工作在集合和文档上一种概念。\
采用集合的方式，集合是MangoDB一组文件,一个集合存在于数据库中,合中的文档可以有不同的字段。通常情况下，在一个集合中的所有文件都是类似或相关目的 \
文档是一组键值对。文档具有动态模式。动态模式是指，在同一个集合的文件不必具有相同一组集合的文档字段或结构，并且相同的字段可以保持不同类型的数据。 \
[官方文档](https://www.mongodb.com/docs/manual/tutorial/getting-started/) \
数据库 > 集合 > 文档

## CRUD
### 数据库操作
* 创建数据库 
  >&gt; use DATABASE_NAME
* 查看当前数据库 
  >&gt; db
* 查看所有数据库 
  >&gt; show dbs  或者 show databases
* 删除数据库 
  >&gt; db.dropDatabase()
### 集合操作
  * 创建集合 
    >&gt; db.createCollection(name, options) \
    &gt; db.createCollection("mycol", {capped : true, autoIndexId : true, size : 6142800, max : 10000 })
  
| 字段          | 类型      | 描述                                                                              |
|-------------|---------|---------------------------------------------------------------------------------|
| capped      | boolean | (可选)如果为true，则启用封闭的集合。上限集合是固定大小的集合，它在达到其最大大小时自动覆盖其最旧的条目。 <br/>如果指定true，则还需要指定size参数。  |
| autoIndexId | boolean |(可选)如果为true，则在_id字段上自动创建索引。默认值为false|
|size| number  |(可选)指定上限集合的最大大小(以字节为单位)。 如果capped为true，那么还需要指定此字段的值。|
|max| number  |(可选)指定上限集合中允许的最大文档数。|
 
 * 删除集合
 >&gt; db.collectionName.drop()
### 文档操作
 * 插入文档 
 >&gt; db.COLLECTION_NAME.insert(document) \
 &gt; db.COLLECTION_NAME.insertOne(document) \
 &gt; db.COLLECTION_NAME.insertMany(document)
 * 查询文档  
 >&gt; db.mycol.find() \
 &gt; db.mycol.find().pretty()   // pretty() 格式化显示（json）
 * 更新文档 
 >&gt; db.COLLECTION_NAME.update(SELECTION_CRITERIA, UPDATED_DATA)

 eg:
 > &gt;db.book.update({'name':'Kubernetes权威指南'},{$set:{'publishedInformation.price':138.01}}) 
* 原子更新
>&gt;db.products.findAndModify({
    query:{_id:2,product_available:{$gt:0}},
    update:{
        $inc:{product_available:-1},
        $push:{product_bought_by:{customer:"Curry",date:"2017-08-08"}}
    }    
})


除此之外save() 也可以更新文档 
>&gt; db.COLLECTION_NAME.save({_id:ObjectId(),NEW_DATA})
 * 删除文档
 >&gt; db.COLLECTION_NAME.remove(DELETION_CRITERIA,1) \
 &gt; db.COLLECTION_NAME.remove()   //删除所有
 * 投影查询
 KEY 的值如果是0 表示隐藏该字段显示，1 表示不隐藏
 > &gt; db.COLLECTION_NAME.find({},{KEY:1})
 * 限制记录数
 > &gt;db.COLLECTION_NAME.find().limit(NUMBER) \
  &gt; db.COLLECTION_NAME.find().limit(NUMBER).skip(NUMBER)   // 跳过文档个数后，查询个数，可用于分页
 * 排序记录 \
   指定排序顺序1和-1。 1用于升序，而-1用于降序
 > &gt; db.COLLECTION_NAME.find().sort({KEY:1}) 
 * 聚合查询 \
 
| 表达式  | 描述                   | 示例  |
|------|----------------------|-----|
 | $sum | 求和                   |     |
| $avg | 均值                   |     |
| $min | 最小值                  |     |
 | $max | 最大值                  |     |
|$push| 将值插入到生成的文档数组中        ||
|$addToSet| 将值插入生成的文档中的数组，不会有重复值 ||
|$first|根据分组从源文档获取第一个文档。 通常情况下，这只适用于以前应用的“$sort”阶段。||
|$last|根据分组从源文档获取最后一个文档。通常情况下，这只适用于以前应用的“$sort”阶段。||

 > &gt; db.article.aggregate([{$group : {_id : "$by_user", num_tutorial : {$sum : 1}}}])

## 索引
* MongoDB 索引基于B-Tree
  * 多路非二叉树
  * 每个节点既保存数据又保存索引
  * 搜索时相当于二分查找 \
* 创建普通索引
  这里的key是要在其上创建索引的字段的名称，1是升序。 要按降序创建索引，需要使用-1
> &gt; db.COLLECTION_NAME.ensureIndex({KEY:1}) \
在ensureIndex()方法中，可以传递多个字段，以在多个字段上创建索引。
* 创建唯一索引
> &gt; db.book.ensureIndex({'name':1},{'unique':true}) 
* 创建复合索引
> &gt; db.book.ensureIndex({'name':1,'author':1}) 
* 创建文本索引
> &gt; db.book.ensureIndex({'intro':"text"})
* 查看索引
> &gt; db.book.getIndexes() \
[ \
  { v: 2, key: { _id: 1 }, name: '_id_' },\
  { v: 2, key: { name: 1 }, name: 'name_1', unique: true }, \
  { \
    v: 2, \
    key: { _fts: 'text', _ftsx: 1 }, \
    name: 'intro_text', \
    weights: { intro: 1 }, \
    default_language: 'english', \
    language_override: 'language',\
    textIndexVersion: 3 \
    } \
]
* 删除索引
> &gt;db.book.dropIndex("intro_text")
* 索引最大范围 \
  集合不能有超过64个索引。  \
  索引名称的长度不能超过125个字符。 \
  复合索引最多可以编号31个字段。  

## 备份与恢复
创建备份：
> &gt; mongodump --out=<path>\
> &gt; mongodump --uri="mongodb://mongodb0.example.com:27017" [additional options]

该命令将连接到运行在 127.0.0.1 和端口 27017 的服务器，并将服务器的所有数据恢复到目录/bin/dump/。 以下是命令的输出
备份恢复：
> &gt; mongorestore --uri "mongodb://user@mongodb1.example.net:27017/?authSource=admin" /opt/backup/mongodump-2011-10-24
## GridFS
GridFS是用于存储和检索大型文件(如图像，音频文件，视频文件等)的MongoDB规范。它是一种用于存储文件的文件系统，但其数据存储在MongoDB集合中。 GridFS存储文件可超过文件大小限制为16MB的功能。 \
mongofiles <options> <connection-string> <command> <filename or _id>

## 集群搭建
集群分为三种模式：主从模式，副本模式，分片模式
### 主从模式
### 副本模式
### 分片模式



