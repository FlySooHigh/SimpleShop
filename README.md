# SimpleShop

При запуске сервера указать на каком порту он будет запущен, например:  
`java ServerThread {port}`    
или, если собрать проект через mvn clean package, то  
`java -jar SimpleShop.jar {port}`  
Затем может подключиться клиент с помощью Putty (localhost:{port}) через Telnet  

В items.xml - 3 предмета, которые можно купить, ball(стоимость 10), puck(5) и baloon(1).  
Также добавлены 3 юзера под которыми можно залогиниться и покупать предметы - zidan(100 денег), ovechkin(50) и vinni(5).  
Доступные команды:  
-login {userName}  
-logout  
-viewshop  
-myinfo  
-but {itemName}  
-sell {itemName}  
-exit  
В одном терминале может залогиниться только 1 юзер, в разных терминалах могут логиниться несколько разных юзеров одновременно.



