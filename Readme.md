# Табло теннисного матча (Tennis scoreboard tablo)

Третий учебный проект из [роадмапа Сергея Жукова](https://zhukovsd.github.io/java-backend-learning-course/) — «Обмен валют».
[ТЗ проекта](https://zhukovsd.github.io/java-backend-learning-course/projects/tennis-scoreboard/).

## Стек и структура

**Backend**

- REST API на чистом Spring MVC (без Spring ORM и Spring Boot), и подключение к бд Postgres через Hibernate и пул соединений HikariCP.

**Frontend** — [взят со страницы с проектом](https://github.com/zhukovsd/tennis-scoreboard-frontend), автор Сергей Жуков.

## Функциональность

- Создание нового матча (`POST /matches`)
- Начисление `point` и обновление счёта (`POST /matches/{uuid}/point`)
- Получение счёта (`GET /matches/{uuid}`)
- Получение списка сыгранных матчей через (`GET /matches`)

## Как запускается

### 1. Заходится в Ubuntu

- Арендуется vps сервер с Ubuntu (самый дешёвый) на одном из российских провайдеров - Beget Cloud, Timeweb Cloud, Selectel и др. 
- Там будут данные для входа в виде ssh login@000.000.000.000 и password, где вместо login - выданный логин, вместо 0.0.0.0 выданный ip адрес, а вместо password - выданный пароль
- Открывается командная строка БЕЗ имени администратора и вводится 'ssh login@000.000.000.000' * Enter * и потом password: 'mypassword' для захода в линукс терминал на сервере

### 2. Настраивается линукс для программы через терминал

**2.1. Установливается JDK**
```bash
apt update && apt upgrade -y
apt install -y openjdk-21-jre-headless
```

**2.2. Устанавливается Tomcat**

В `apt` уже есть Tomcat 9, а нужен Tomcat 10. Надо проверить версию на https://tomcat.apache.org/download-10.cgi и задать такие команды на установку Tomcat: (на 05.07.2026 самая новая версия - 10.1.56)

```bash
cd /opt
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.56/bin/apache-tomcat-10.1.56.tar.gz
tar -xzf apache-tomcat-10.1.56.tar.gz
mv apache-tomcat-10.1.56 tomcat
```

**2.3. Создается пользователь Tomcat**

```bash
useradd -m -d /opt/tomcat -U -s /bin/false tomcat
chown -R tomcat:tomcat /opt/tomcat
```

**2.4. Postgres**

Устанавливается postgresSQL, запускается, а потом создаётся бд



**2.5. Добавляется админ для Tomcat Manager**

Чтобы не загружать файлы через Linux и командную строку, можно воспользоваться веб интерфейсом Tomcat, для возможности этого нужно создать пользователя, набрав `/opt/tomcat/conf/tomcat-users.xml` (этим открывается файл):

В нём вместо этого:
```xml
....
</tomcat users>
```

Делается это
```xml
....
<role rolename="manager-gui"/>
<user username="admin" password="mypassword" roles="manager-gui"/>
</tomcat users>
```
(вместо mypassword вставляется свой придуманный пароль)

В Tomcat в `webapps/manager/META-INF/context.xml` может быть открыто (не закомментировано) значение `RemoteAddrValve`/`RemoteCIDRValve` которое ограничивает доступ к Manager, и его можно открыть только в убунту по адресу `localhost`, я проверил и закомментировал, если он был не закомментирован.


**2.7. Открыл порт 8080**

```bash
ufw allow 8080/tcp
```

**2.8. Запустить Tomcat от пользователя tomcat**

```bash
su -s /bin/bash tomcat -c /opt/tomcat/bin/startup.sh
```

В конце присланного сообщения увидел `Tomcat started.`
Но на всякий случай лучше проверить:
```bash
ps aux | grep tomcat              # процесс запущен
ss -tlnp | grep 8080               # порт слушается
```

### 3. Загрузка `.war` в Manager

По ссылке `http://000.000.000.000:8080/manager/html` открывается Manager Tomcat, и вводим логин и пароль из `<user username="admin" password="mypassword" roles="manager-gui"/>` (из того файла `/opt/tomcat/conf/tomcat-users.xml`).

и после этого собрал .war - `mvn clean package` в IntelliJ IDEA

Нашёл раздел **"WAR file to deploy"** и кнопку "Выбрать файл".

выбрать собранный `.war` из папки target → нажал **"Развернуть"**.

После этого приложение должно появиться в списке в начале страницы со статусом `running` (`true`), а также развернуться по адресу:
```
http://000.000.000.000:8080/currency-exchange/
```

## О том, чему я научился