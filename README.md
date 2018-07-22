# Каталог контрактов  
Программа предоставляет API по работе с каталогом контрактов.

  **Заявленная функциональность:**  
 - Возможность просмотра всех контрактов  
 - Возможность просмотра всех контрактов,  
 - Просмотр объекта контракта по идентификатору, со всеми полями,  
 - Возможность удаления зарегистрированного контракта,  
 - Добавление нового контракта в реестр контрактов с заполнением всех полей. 
   
  **Возможные типы контрактов:**  
 - Договор купли-продажи,
 - Декларация о продаже,
 - Отчет о продаже.

# Запуск

Запуск осуществляется скриптом 
```bash 
run.sh
```

# Примеры запросов

_При старте приложения для БД заполняется тестовыми данными_

Просмореть все контракты
```bash
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/contracts/
```
Просмотр всех договоров купли-продажи
```bash
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/salecontracts/
```
Просмотр всех деклараций о продажи
```bash
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/saledeclarations/
```
http://localhost:8080/salereports/
```bash
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/salereports/
```

Просмотр базы данных контрактов возможен по адресу
```bash
http://localhost:8080/h2-console
```
```
url: "jdbc:h2:mem:datajpa"
username: sa
password:
```
К проекту прилагается набор тестовых запросов в PostMan
```
Contracts Catalog.postman_collection.json
```