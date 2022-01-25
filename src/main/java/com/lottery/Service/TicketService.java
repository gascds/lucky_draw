package com.lottery.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
@EnableScheduling
public class TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    //Identify the round number of lucky draw
    private static Integer ticketIndex = 0;

    //Store each round winner ticket
    private static HashMap<Integer,String> winnerList = new HashMap<>();

    //Store current round ticket list for lucky draw
    private static List<String> currentTicketList = new ArrayList<>();

    private static final String TICKET_LABEL = "T_";

    private static final char SEPARATOR = '#';

    //Generate unique ticket with format T_X#Y#Z, where X is index, Y is Timestamp and Z is client ip
    public String generateTicket(String ipAddress){
        try{
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String current = df.format(LocalDateTime.now());
            String result =  TICKET_LABEL+ ticketIndex + SEPARATOR + current + SEPARATOR + ipAddress;
            currentTicketList.add(result);
            return result;
        }catch (Exception e){
            log.error("generateTicket()-> " + ipAddress + ": something wrong when generating ticket\n"+e);
            return null;
        }
    }

    //Do lucky draw, random pick one ticket from current ticketList
    @Scheduled (cron = "${luckyDraw.cron.expression}")
    private void luckyDraw(){
        Random randomizer = new Random();
        if (currentTicketList.size()>0){
            String winner = currentTicketList.get(randomizer.nextInt(currentTicketList.size()));
            winnerList.put(ticketIndex,winner);
            log.info("luckyDraw()-> Round "+ ticketIndex + " ticket winner is " + winner);
            currentTicketList.clear();
            ticketIndex+=1;
        }
    }

    public Boolean checkWinner(String ticketNumber){
        try{
            if (ticketNumber == null){
                return false;
            }

            Integer indexNumber = Integer.parseInt(ticketNumber.split("#")[0].replace(TICKET_LABEL,""));
            if (indexNumber>winnerList.size()){
                return false;
            }
            String winerTicket = winnerList.get(indexNumber);
            if (ticketNumber.equals(winerTicket)){
                log.info("checkWinner()-> Winner ticket " + winerTicket + " is checked.");
                return true;
            }else {
                log.info("checkWinner()-> Ticket " + winerTicket + " is checked, but is not winner.");
                return false;
            }

        }catch (Exception e){
            log.error("checkWinner()-> Something wrong when checking winner\n"+e);
            return false;
        }
    }
}
