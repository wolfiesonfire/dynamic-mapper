# Dynamic Mapper

运行时动态生成 `ServiceImpl`、`BaseMapper`，基于 Mybatis-plus

根据数据库实体创建，不会重复创建已经定义的 `Service`、`Mapper`

[具体效果](src/test/java/com/example/service/TestService.java)

<br>

### 使用步骤

---

定义一个 `@TableName` 修饰的数据库实体

```java
import com.baomidou.mybatisplus.annotation.TableName;

@TableName
public class Foo {
}
```

使用

```java

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Foo;

import javax.annotation.Resource;

public class Test {

    @Resource
    private BaseMapper<Foo> fooMapper;

    @Resource
    private ServiceImpl<BaseMapper<Foo>, Foo> fooService;

    public void test() {
        fooMapper.selectList(null);
        fooService.lambdaQuery().list();
    }
}

```

