package com.project.pos;

import com.project.pos.store.dto.PosDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeAdvice {
    @ModelAttribute("selectedPos")
    public Long currentPos(HttpSession session){
        PosDTO posDTO = (PosDTO) session.getAttribute("currentPos");
        if(posDTO==null) return 0L;
        return posDTO.getNumber();
    }
}
