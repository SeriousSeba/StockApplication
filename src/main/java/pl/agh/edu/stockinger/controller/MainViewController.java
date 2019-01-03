package pl.agh.edu.stockinger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.agh.edu.stockinger.model.SingleDayQuote;
import pl.agh.edu.stockinger.service.QuotationsService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/quotations")
public class MainViewController {

        @Autowired
        private QuotationsService quotationsService;

        @GetMapping
        public String getDailyQuotations(
                Model model
        ){
            try {
                List<SingleDayQuote> quotations = quotationsService.getLastQuoatations();
                model.addAttribute("quotations", quotations);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "quotations";
        }

//        @GetMapping
//        public String getSingleQuotationView(
//                Model model
//        ){
//            return "quotation";
//        }


}
