package com.lottery.Controller;

import com.lottery.Service.TicketService;
import com.lottery.Utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    private static final Logger log = LoggerFactory.getLogger(TicketController.class);

    private static final String SESSION_TICKET_NAME = "2022Ticket";

    @Autowired
    HttpSession session;

    @Autowired
    TicketService ticketService;

    //API for client to get unique ticket based on httpsession
    @GetMapping("/")
    public ResponseEntity<Map>  getTicket(HttpServletRequest request){
        try {
            String ipAddress = request.getRemoteAddr();
            String ticketNumber = null;
            if (session.getAttribute(SESSION_TICKET_NAME) == null || session.getAttribute(SESSION_TICKET_NAME).equals("")){
                ticketNumber = ticketService.generateTicket(request.getRemoteAddr());
                session.setAttribute(SESSION_TICKET_NAME , ticketNumber);
                log.info("getTicket()-> " + ipAddress + ": no ticket is found, new ticket is generated, "+ticketNumber);
            }else{
                ticketNumber = session.getAttribute(SESSION_TICKET_NAME).toString();
                log.info("getTicket()-> IP: "+ request.getRemoteAddr() + " contains ticket number: " + ticketNumber);
            }

            return ResponseEntity.ok(ResponseUtil.buildOk(ticketNumber));
        }catch(Exception e){
            log.error("getTicket()-> Something wrong when getting ticket \n" + e.toString());
            return ResponseEntity.ok(ResponseUtil.buildInternalError());
        }
    }

    //API for front-end to remove ticket after checking winner, or attend next round lucky draw
    @DeleteMapping("/")
    public ResponseEntity<Map>  removeTicket(HttpServletRequest request){
        try {
            if (session.getAttribute(SESSION_TICKET_NAME)!=null){
                String ticketNumber = session.getAttribute(SESSION_TICKET_NAME).toString();
                session.removeAttribute(SESSION_TICKET_NAME);
                session.invalidate();
                log.info("removeTicket()-> The ticket number (" + ticketNumber + ") is removed.");
            }
            return ResponseEntity.ok(ResponseUtil.buildOk("OK"));
        }catch(Exception e){
            log.error("removeTicket()-> Something wrong when removing ticket\n"+ e.toString());
            return ResponseEntity.ok(ResponseUtil.buildInternalError());
        }
    }

    //API for front-end to check winner
    @GetMapping("/winner")
    public ResponseEntity<Map>  checkTicket(HttpServletRequest request){
        try {
            if (session.getAttribute(SESSION_TICKET_NAME)!=null){
                String ticketName = session.getAttribute(SESSION_TICKET_NAME).toString();
                boolean result = ticketService.checkWinner(ticketName);
                return ResponseEntity.ok(ResponseUtil.buildOk(result));
            }else{
                return ResponseEntity.ok(ResponseUtil.buildForBiddenError());
            }
        }catch(Exception e){
            log.error("getAllTicket()-> Something wrong when checking winner \n"+ e.toString());
            return ResponseEntity.ok(ResponseUtil.buildInternalError());
        }
    }

}
