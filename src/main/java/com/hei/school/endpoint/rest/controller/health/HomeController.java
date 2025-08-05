package com.hei.school.endpoint.rest.controller.health;

import com.hei.school.domain.Donation;
import com.hei.school.domain.DonationForm;
import com.hei.school.repository.DonationRepository;
import com.hei.school.repository.HelpRepository;
import com.hei.school.service.DonationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    private final DonationService donationService;
    private final HelpRepository helpRepository;
    private final DonationRepository donationRepository;

    public HomeController(DonationService donationService, HelpRepository helpRepository, DonationRepository donationRepository) {
        this.donationService = donationService;
        this.helpRepository = helpRepository;
        this.donationRepository = donationRepository;
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "OK";
    }

    @GetMapping("/")
    public String index(Model model) {
        System.out.println(">>> Méthode index() appelée");
        // model.addAttribute("donations", donationRepository.findAllOrderedByDateDesc());
        // model.addAttribute("helps", helpRepository.findAllOrderedByDateDesc());
        model.addAttribute("newDonation", new DonationForm());
        return "index";
    }

    @PostMapping("/donate")
    public String donate(@ModelAttribute DonationForm form) {
        Donation donation = form.toDonation();
        donationService.createDonation(donation);
        return "redirect:/";
    }
}
