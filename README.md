# Тестовый проект для API сервиса [Reqres.in](https://reqres.in/)
> </a> Проект представляет собой автоматизированные тесты для REST API сервиса [reqres.in](https://reqres.in/), реализованные на Java с использованием различных инструментов тестирования.

## **Содержание:**
____

* <a href="#tools">Технологии и инструменты</a>

* <a href="#cases">Примеры автоматизированных тест-кейсов</a>

* <a href="#jenkins">Сборка в Jenkins</a>

* <a href="#allure">Allure отчет</a>
____
<a id="tools"></a>
## <a name="Технологии и инструменты">**Технологии и инструменты:**</a>

<p align="center">  
<a href="https://www.jetbrains.com/idea/"><img src="src/test/java/media/intellij-original.svg" width="50" height="50"  alt="IDEA"/></a>  
<a href="https://www.java.com/"><img src="src/test/java/media/java-original.svg" width="50" height="50"  alt="Java"/></a>  
<a href="https://github.com/"><img src="src/test/java/media/github-original.svg" width="50" height="50"  alt="Github"/></a>  
<a href="https://junit.org/junit5/"><img src="src/test/java/media/junit-original.svg" width="50" height="50"  alt="JUnit 5"/></a>  
<a href="https://gradle.org/"><img src="src/test/java/media/gradle-original.svg" width="50" height="50"  alt="Gradle"/></a>   
<a href="ht[images](images)tps://github.com/allure-framework/allure2"><img src="src/test/java/media/Allure_Report.svg" width="50" height="50"  alt="Allure"/></a>  
<a href="https://www.jenkins.io/"><img src="src/test/java/media/jenkins-original.svg" width="50" height="50"  alt="Jenkins"/></a>
<a href="https://rest-assured.io/"><img width="5%" title="REST-Assured" src="src/test/java/media/Rest-Assured.svg"></a>
<a href="https://web.telegram.org/"><img width="5%" title="Telegram" src="src/test/java/media/icons8-телеграм.svg"></a>
</p>

____
<a id="cases"></a>
## <a name="Примеры автоматизированных тест-кейсов">**Примеры автоматизированных тест-кейсов:**</a>
____
- ✓ *Получение списка пользователей с использованием метода GET*
- ✓ *Получение одного пользователя с использованием метода GET*
- ✓ *Пользователь не найден с использованием метода GET*
- ✓ *Создание пользователя с использованием метода POST*
- ✓ *Обновление пользователя с использованием метода PUT*
- ✓ *Удаление пользователя с использованием метода DELETE*
- ✓ *Успешная регистрация с использованием метода POST*
- ✓ *Неуспешная регистрация с использованием метода POST*


  ____
<a id="jenkins"></a>
## <img alt="Jenkins" height="25" src="src/test/java/media/jenkins-original.svg" width="25"/></a><a name="Сборка"></a>Сборка в [Jenkins](https://jenkins.autotests.cloud/job/ApiReqresTests-diplom/)</a>
____
<p align="center">  
<a href="https://jenkins.autotests.cloud/job/ApiReqresTests-diplom/"><img src="src/test/java/media/jenkins.png" width="950"/></a>  
</p>


### **Параметры сборки в Jenkins:**

- *browser (браузер, по умолчанию chrome)*
- *browserSize (размер окна браузера, по умолчанию 1920x1080)*
- *baseUrl (адрес тестируемого веб-сайта)*


___
<a id="allure"></a>
## <img alt="Allure" height="25" src="src/test/java/media/Allure_Report.svg" width="25"/></a> <a name="Allure"></a>Allure [отчет](https://jenkins.autotests.cloud/job/ApiReqresTests-diplom/5/allure/#)</a>
___

### *Основная страница отчёта*

<p align="center">  
<img title="Allure Overview Dashboard" src="src/test/java/media/allure1.png" width="850">  
</p>

### *Тест-кейсы*

<p align="center">  
<img title="Allure Tests" src="src/test/java/media/allure2.png" width="850">  
</p>

## <img width="4%" style="vertical-align:middle" title="Telegram" src="src/test/java/media/icons8-телеграм.svg"> Уведомления в Telegram с использованием бота

<p align="center">
<img width="70%" title="Telegram bot" src="src/test/java/media/telegram_report.png">
</p>
