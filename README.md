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


## 3. Files

liquidbase columns -> https://docs.liquibase.com/change-types/community/add-column.html

Problem with swagger and multipart upload :(  In postman it should be formdata with key-value

Validate API requires implementation: validation-api + hibernate-validator

Right file upload: FileImportControllerTest.postFile() => requires all headers, content dispositions and so on, and so on

## 4. Images

## 5. Default category excel parser

## Naming convention for Repository

`findByName` => Optional<Product>

`getByName` => Product

## Spring JPA method names:

https://www.baeldung.com/spring-data-derived-queries

```java
interface JpaRepo {
    List<User> findByAgeGreaterThan(Integer age);
    List<User> findByAgeGreaterThanEqual(Integer age);

    List<User> findByNameOrderByName(String name);
    List<User> findByNameOrderByNameAsc(String name);

    List<User> findTop3ByAge();

    Stream<Product> findAllByImagesIsEmpty();

}
```
