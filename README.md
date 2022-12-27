# For-Telegram-Bot
Application for telegram bot
This application is for restricting rights and granting rights to the user in a telegram chat.
For a volunteer project "
Creater Dmitrii Lebedev 
December 2022

Настройка:
1. Задайте имя бота и его хэш в параметрах usernameBot и BotHash в файле настроек resource.json
2. Установите параметр ожидания ответа пользователя на вопросы от бота в timeoutAnswer в секундах.
3. Настройте диалог бота в resource.json (параметры textBegin, question, answer, reaction, next, check, goodfinish, badfinish ) 
4. Настройте первоначальное приветствие в основном чате параметр firstMessage
5. Создайте групповой чат
6. Добавьте туда бота с правами админа
7. Установите права в чате по умолчанию, разрешено отправлять сообщения. Все остальные права должны быть запрещены. 
8. На сервере, где будет запускаться программа бота должны быть установлены следующие программы:
    а) java -version
openjdk version "11.0.17" 2022-10-18
OpenJDK Runtime Environment (build 11.0.17+0-suse-150000.3.86.2-x8664)
OpenJDK 64-Bit Server VM (build 11.0.17+0-suse-150000.3.86.2-x8664, mixed mode)
    Возможно заработает и с другой версией.
    б) Подключение к интернету, доступ телеграмму с него 
9. Копируем все файлы в отдельный каталог и запускаем java -jar CheckBob.jar, лучше в screen или через nohup ... & 
10. В файле CheckBot.log будет писаться лог-журнал событий. Возможно через какое-то время потребуется его ротация
