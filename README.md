# Lucky Draw (JAVA)

## Background

This project is the backend part of lucky draw system, it is written in JAVA (SpringBoot).

## API part

1. **Get unique ticket** (GetMapping with path: "**ip:10066/lottery/ticket**/")
   - It allows client to get unique ticket based on client session.
2. **Check winner **(GetMapping with path "**ip:10066/lottery/ticket/winner**/")
   - Check to see if you have won the lottery
3. **Remove ticket**  (DeleteMapping with path: "**ip:10066/lottery/ticket**/")
   - Before participating in the next round of lottery, need to delete the previous ticket

Remakr: Port 10066 is pre-defined in the config file

## Limitation

1. User ticket is stored in session, it may loss when connection is closed
   1. **Current solution**
      1. Log all the generated ticket and winner ticket in each round
      2. Front end remind user to screenshot or keep the ticket personally
      3. Check the log file when user report
   2. **Further improvement**
      1. Store all ticket, lucky draw result in the Database 
      2. Authenticate contestant in every participation
2. User need to check the lucky draw result maunally
   1. **Current solution**
      1. Front end call API to check user won the lottery or not
      2. Server will response "**true**" and "**false**" resepctivelly
      3. Front end display related message to user
   2. **Further improvement**
      1. Backend push the lucky draw result to client by websocket
      2. Push API (https://developer.mozilla.org/en-US/docs/Web/API/Push_API)

## Log file

There are **3** log files, all have log-rotation (Max single file size: 50MB and Max file number: 10)

1. **Error file** (Path: ./logs/error/, Pattern: Error_%d{yyyy-MM-dd}_%i.log)

   1. It contains system error log

2. **Ticket file** (Path: ./logs/ticket/, Pattern: Ticket_%d{yyyy-MM-dd}_%i.log)

   1. It contains lucky draw result

   ```java
   #Example
   luckyDraw()-> Round 0 ticket winner is T_0#2022-01-25 21:37:36#192.168.1.15
   luckyDraw()-> Round 1 ticket winner is T_1#2022-01-25 21:45:40#192.168.1.15
   ```

   2. It contains the checking result

   ```java
   #Example
   checkWinner()-> Winner ticket T_2#2022-01-25 21:06:56#192.168.1.15 is checked.
   ```

3. **API file** (Path: ./logs/api/, Pattern: api_%d{yyyy-MM-dd}_%i.log)

   1. It contains the controller api access log

   ```java
   #Example
   getTicket()-> IP: 192.168.1.15 contains ticket number: T_0#2022-01-25 21:47:23#192.168.1.15
   removeTicket()-> The ticket number (T_0#2022-01-25 21:47:23#192.168.1.15) is removed.
   getTicket()-> 192.168.1.15: no ticket is found, new ticket is generated, T_0#2022-01-25 21:50:19#192.168.1.15
   ```