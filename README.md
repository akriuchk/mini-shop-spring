# mini-shop-spring

## 1. Linens
Linen CRUD = Products CRUD
Mapper lib: https://mapstruct.org (models, streams(!))

.roles("ADMIN") => @Secured("ROLE_ADMIN")

## 2. Categories

Drop h2 with 
```java
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
```

!!! Test with posgreSQL !!!

Cross usage of mappers:

```java
@Mapper(componentModel = "spring", uses = ProductMapper.class)
```

NB: make recheck what intellij automatically imports (toList)
